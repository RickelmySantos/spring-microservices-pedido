import { Route } from '@angular/router';

export const ROUTES: Route[] = [
    {
        path: '',
        loadComponent: () => import('./layout/layout.component').then(m => m.LayoutComponent),
        children: [
            { path: '', loadChildren: () => import('./core/layout/home/home.routes') },
            { path: 'upload', loadChildren: () => import('./modules/upload/upload.routes') },
        ],
    },

    {
        path: '**',
        redirectTo: 'pages/not-found',
    },
] as Route[];
