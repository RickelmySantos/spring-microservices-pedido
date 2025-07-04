import { Route } from '@angular/router';

export default [
    {
        path: '',
        loadComponent: () => import('src/app/modules/upload/file-upload.component').then(m => m.FileUploadComponent),
    },
] as Route[];
