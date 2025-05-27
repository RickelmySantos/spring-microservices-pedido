import { Route } from '@angular/router';

export const ROUTES: Route[] = [
    {
        path: '',
        loadComponent: () => import('./layout/layout.component').then(m => m.LayoutComponent),
        children: [
            { path: '', loadChildren: () => import('./modules/home/home.routes') },
            { path: 'cardapio', loadChildren: () => import('./modules/cardapio/cardapio.routes') },
            { path: 'sobre', loadChildren: () => import('./modules/sobre/sobre.routes') },
            { path: 'contato', loadChildren: () => import('./modules/contato/contato.routes') },
        ],
    },

    {
        path: '**',
        redirectTo: 'pages/not-found',
    },
] as Route[];
