import { Route } from '@angular/router';
import { authGuard } from 'src/app/core/guards/auth.guard';
import { roleGuard } from 'src/app/core/guards/role.guard';
import { Role } from 'src/app/shared/auth/role.enum';

export default [
    {
        path: '',
        loadComponent: () => import('src/app/modules/upload/file-upload.component').then(m => m.FileUploadComponent),
        canActivate: [authGuard, roleGuard],
        data: { roles: [Role.ADMIN, Role.GESTOR] },
    },
] as Route[];
