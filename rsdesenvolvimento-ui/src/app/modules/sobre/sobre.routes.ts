import { Route } from '@angular/router';

export default [
    {
        path: '',
        loadComponent: () => import('./sobre.component').then(m => m.SobreComponent),
    },
] as Route[];
