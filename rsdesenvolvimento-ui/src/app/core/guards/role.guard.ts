import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthorizationService } from 'src/app/core/services/authorization.service';
import { Permission } from 'src/app/shared/auth/permissions.enum.';

export const roleGuard: CanActivateFn = (route, state) => {
    const authorizationService = inject(AuthorizationService);
    const router = inject(Router);

    const requiredPermission = route.data?.['permission'] as Permission | undefined;

    if (requiredPermission === undefined) {
        return true;
    }

    if (authorizationService.hasPermissions(requiredPermission)) {
        return true;
    }

    router.navigate(['/']);
    return false;
};
