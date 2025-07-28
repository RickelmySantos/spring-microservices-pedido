import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { AuthService } from 'src/app/core/auth/auth.service';

export const authGuard: CanActivateFn = async (RouteConfigLoadEnd, state) => {
    const authService: AuthService = inject(AuthService);

    if (authService.isLoggedIn()) {
        return true;
    } else {
        authService.login();
        return false;
    }
};
