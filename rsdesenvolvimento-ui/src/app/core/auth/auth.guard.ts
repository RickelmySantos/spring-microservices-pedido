// import { inject } from '@angular/core';
// import { CanMatchFn, Router, UrlTree } from '@angular/router';
// import { firstValueFrom, of } from 'rxjs';
// import { AuthService } from 'src/app/core/auth/auth.service';
// import { environments } from 'src/environments/environments';

// export const authGuard: CanMatchFn = async (): Promise<boolean | UrlTree> => {
//     if (!environments.securityEnabled) {
//         console.warn('[AuthGuard] Segurança desativada, permitindo acesso a todas as rotas.');
//         return firstValueFrom(of(true));
//     }
//     console.debug('[AuthGuard] Segurança ativada, verificando autenticação do usuário.');
//     const authService = inject(AuthService);
//     const router = inject(Router);

//     await authService.waitUntilReady();

//     if (authService.isLoggedIn()) {
//         console.debug('[AuthGuard] Usuário autenticado, permitindo acesso.');
//         return true;
//     } else {
//         console.warn('[AuthGuard] Usuário não autenticado, p-redirecionando para a página de login.');
//         authService.login();

//         return router.parseUrl('/');
//     }
// };
