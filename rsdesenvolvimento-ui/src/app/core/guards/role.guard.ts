import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from 'src/app/core/auth/auth.service';
import { Permission } from 'src/app/shared/auth/permissions.enum.';

export const roleGuard: CanActivateFn = (route, state) => {
    const auth = inject(AuthService);
    const router = inject(Router);

    const requiredPermission = route.data?.['permission'] as Permission | undefined;

    if (requiredPermission === undefined) {
        return true;
    }

    if (auth.hasPermissions(requiredPermission)) {
        return true;
    }

    router.navigate(['/']);
    return false;
};
