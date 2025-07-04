import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { AuthService } from 'src/app/core/auth/auth.service';

let authInitialized = false;

export const authGuard: CanActivateFn = async (RouteConfigLoadEnd, state) => {
    const authService: AuthService = inject(AuthService);

    if (!authInitialized) {
        await authService.initAuth();
        authInitialized = true;
    }
    if (authService.isLoggedIn()) {
        return true;
    }

    return false;
};
