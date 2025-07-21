import { Route } from '@angular/router';

export default [
    {
        path: '',
        loadComponent: () => import('./components/cardapio.component').then(m => m.CardapioComponent),
    },
] as Route[];
