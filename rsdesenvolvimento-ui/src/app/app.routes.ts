import { Route } from '@angular/router';
import { roleGuard } from 'src/app/core/guards/role.guard';
import { LayoutComponent } from 'src/app/layout/layout.component';
import { Permission } from 'src/app/shared/auth/permissions.enum.';

export const ROUTES: Route[] = [
    {
        path: '',
        component: LayoutComponent,
        children: [
            { path: '', loadChildren: () => import('./core/layout/home/home.routes') },
            {
                path: 'upload',
                canActivate: [roleGuard],
                data: { permission: Permission.GERENCIAR_ESTOQUE },
                loadChildren: () => import('./modules/upload/file-upload.routes'),
            },
        ],
    },

    {
        path: '**',
        redirectTo: 'pages/not-found',
    },
] as Route[];
