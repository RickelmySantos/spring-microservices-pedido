import { Route } from '@angular/router';

export default [
    {
        path: '',
        loadComponent: () => import('./footer.component').then(m => m.FooterComponent),
    },
] as Route[];
