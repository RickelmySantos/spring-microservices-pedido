import { HttpHandlerFn, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { OAuthStorage } from 'angular-oauth2-oidc';
import { environments } from 'src/environments/environments';

export const authInterceptor: HttpInterceptorFn = (req: HttpRequest<any>, next: HttpHandlerFn) => {
    console.debug(`[AuthInterceptor] Interceptada a requisição [${req.method}] ${req.url} `);

    const allowedUrls: string[] = [environments.apiUrl as string];
    const authStorage: OAuthStorage = inject(OAuthStorage);

    const url = req.url.toLowerCase();
    const urlPermitida = allowedUrls[0].toLowerCase();

    // LOGS DE DEPURAÇÃO
    console.log('[DEPURAÇÃO] URL da Requisição:', url);
    console.log('[DEPURAÇÃO] URL Permitida (de environments):', urlPermitida);
    console.log('[DEPURAÇÃO] Resultado do startsWith:', url.startsWith(urlPermitida));

    if (!allowedUrls.find(u => url.startsWith(u.toLowerCase()))) {
        console.debug('`[AuthInterceptor]  Não foi necessário incluir cabeçalho Bearer na requisição');
        return next(req);
    }

    const token = authStorage.getItem('access_token');

    if (token) {
        const header = 'Bearer ' + token;
        const headers = req.headers.set('Authorization', header);

        console.debug(`[AuthInterceptor] Cabeçalho 'Bearer [token]' incluído na requisição`, headers);

        req = req.clone({ headers });
    } else {
        console.debug(`[AuthInterceptor] Cabeçalho 'Bearer [token]' NÃO incluído na requisição`);
    }

    return next(req);
};
