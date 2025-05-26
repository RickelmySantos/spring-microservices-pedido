import { Route } from '@angular/router';

export default [
    {
        path: '',
        loadComponent: () => import('./contato.component').then(m => m.ContatoComponent),
    },
] as Route[];
