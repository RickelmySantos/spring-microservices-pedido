import { HttpClient, provideHttpClient, withInterceptors } from '@angular/common/http';
import { importProvidersFrom } from '@angular/core';
import { bootstrapApplication, BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { provideOAuthClient } from 'angular-oauth2-oidc';
import { ROUTES } from 'src/app/app.routes';
import { authInterceptor } from 'src/app/core/auth/auth.interceptor';
import { AuthService } from 'src/app/core/auth/auth.service';
import { httpLoaderFactory } from 'src/app/core/translate/translate-loader-factory';
import { AppComponent } from './app/app.component';

bootstrapApplication(AppComponent, {
    providers: [
        provideRouter(ROUTES),
        provideHttpClient(withInterceptors([authInterceptor])),
        provideOAuthClient(),
        importProvidersFrom(
            BrowserModule,
            BrowserAnimationsModule,
            TranslateModule.forRoot({
                loader: {
                    provide: TranslateModule,
                    useFactory: httpLoaderFactory,
                    deps: [HttpClient],
                },
            })
        ),
        {
            provide: AuthService,
            useFactory: () => {
                const service = new AuthService();
                return service;
            },
        },
    ],
}).catch(err => console.error(err));
