import { bootstrapApplication, BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';

import { ROUTES } from 'src/app/app.routes';

import { HttpClient, provideHttpClient, withInterceptors } from '@angular/common/http';
import { APP_INITIALIZER, importProvidersFrom } from '@angular/core';

import { provideCloudinaryLoader } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { provideOAuthClient } from 'angular-oauth2-oidc';
import { AppComponent } from 'src/app/app.component';
import { authInterceptor } from 'src/app/core/auth/auth.interceptor';
import { AuthService } from 'src/app/core/auth/auth.service';
import { httpLoaderFactory } from 'src/app/core/translate/translate-loader-factory';

export function initializeAuth(authService: AuthService): () => Promise<void> {
    return () => authService.initAuth();
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
                    provide: TranslateModule,
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
    ],
}).catch(err => console.error(err));
// bootstrapApplication(AppComponent, {
//     providers: [
//         provideRouter(ROUTES),
//         provideHttpClient(withInterceptors([authInterceptor])), // Mantenha seu interceptor
//         importProvidersFrom(
//             BrowserModule,
//             BrowserAnimationsModule,
//             TranslateModule.forRoot({
//                 loader: {
//                     provide: TranslateModule,
//                     useFactory: httpLoaderFactory,
//                     deps: [HttpClient],
//                 },
//             })
//         ),
//     ],
// }).catch(err => console.error(err));
