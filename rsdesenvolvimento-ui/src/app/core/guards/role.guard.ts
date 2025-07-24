import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthorizationService } from 'src/app/core/services/authorization.service';
import { Role } from 'src/app/shared/enums/role.enum';

export const roleGuard: CanActivateFn = (route, state) => {
    const authorizationService = inject(AuthorizationService);
    const router = inject(Router);

    const allowedRoles = route.data?.['roles'] as Role[] | undefined;

    if (!allowedRoles || allowedRoles.length === 0) {
        return true;
    }

    if (authorizationService.hasAnyRole(allowedRoles)) {
        return true;
    }

    router.navigate(['/']);
    return false;
};
