import { bootstrapApplication, BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';

import { ROUTES } from 'src/app/app.routes';

import { HttpClient, provideHttpClient, withInterceptors } from '@angular/common/http';
import { APP_INITIALIZER, importProvidersFrom } from '@angular/core';

import { provideCloudinaryLoader } from '@angular/common';
import { TranslateLoader, TranslateModule, TranslateService } from '@ngx-translate/core';
import { provideOAuthClient } from 'angular-oauth2-oidc';
import { AppComponent } from 'src/app/app.component';
import { AuthService } from 'src/app/core/auth/auth.service';
import { authInterceptor } from 'src/app/core/interceptors/auth.interceptor';
import { httpLoaderFactory } from 'src/app/core/translate/translate-loader-factory';
import { ProdutoRepositoryAdapter } from './app/services/adapters/produto-repository.adapter';
import { PRODUTO_REPOSITORY_TOKEN } from './app/services/cardapio.service';

export function initializeAuth(authService: AuthService): () => Promise<void> {
    return () => authService.initAuth();
}

export function initializeTranslate(translateService: TranslateService): () => Promise<void> {
    return () => {
        translateService.setDefaultLang('pt');
        return translateService.use('pt').toPromise();
    };
}

bootstrapApplication(AppComponent, {
    providers: [
        provideRouter(ROUTES),
        provideHttpClient(withInterceptors([authInterceptor])),
        provideOAuthClient(),
        provideCloudinaryLoader('https://res.cloudinary.com/rsdesenvolvimento-estoque-api'),
        importProvidersFrom(
            BrowserModule,
            BrowserAnimationsModule,
            TranslateModule.forRoot({
                loader: {
                    provide: TranslateLoader,
                    useFactory: httpLoaderFactory,
                    deps: [HttpClient],
                },
            })
        ),
        AuthService,
        {
            provide: APP_INITIALIZER,
            useFactory: initializeAuth,
            deps: [AuthService],
            multi: true,
        },
        {
            provide: APP_INITIALIZER,
            useFactory: initializeTranslate,
            deps: [TranslateService],
            multi: true,
        },
        { provide: PRODUTO_REPOSITORY_TOKEN, useExisting: ProdutoRepositoryAdapter },
    ],
}).catch(err => console.error(err));
