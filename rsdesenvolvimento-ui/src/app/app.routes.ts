import { Route } from '@angular/router';
import { LayoutComponent } from 'src/app/layout/layout.component';

export const ROUTES: Route[] = [
    {
        path: '',
        component: LayoutComponent,
        children: [
            { path: '', loadChildren: () => import('./core/layout/home/home.routes') },
            { path: 'upload', loadChildren: () => import('./modules/upload/file-upload.routes') },
        ],
    },

    {
        path: '**',
        redirectTo: 'pages/not-found',
    },
] as Route[];
