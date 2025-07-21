import { CanActivateFn } from '@angular/router';
// import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
    // const authService = inject(AuthService);

    // Implementar lógica de autenticação
    // return authService.isAuthenticated();

    return true; // Placeholder
};
