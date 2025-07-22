import { bootstrapApplication, BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';

import { ROUTES } from 'src/app/app.routes';

import { HttpClient, provideHttpClient, withInterceptors } from '@angular/common/http';
import { importProvidersFrom } from '@angular/core';

import { provideCloudinaryLoader } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { provideOAuthClient } from 'angular-oauth2-oidc';
import { AppComponent } from 'src/app/app.component';
import { AuthService } from 'src/app/core/auth/auth.service';
import { authInterceptor } from 'src/app/core/interceptors/auth.interceptor';
import { httpLoaderFactory } from 'src/app/core/translate/translate-loader-factory';
import { CarrinhoService } from './app/services/carrinho.service';
import { CARRINHO_SERVICE_TOKEN, PEDIDO_SERVICE_TOKEN } from './app/services/checkout.service';
import { PedidoService } from './app/services/pedido.service';

// export function initializeAuth(authService: AuthService): () => Promise<void> {
//     return () => authService.initAuth();
// }

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
        // {
        //     provide: APP_INITIALIZER,
        //     useFactory: initializeAuth,
        //     deps: [AuthService],
        //     multi: true,
        // },
        { provide: CARRINHO_SERVICE_TOKEN, useExisting: CarrinhoService },
        { provide: PEDIDO_SERVICE_TOKEN, useExisting: PedidoService },
    ],
}).catch(err => console.error(err));
