import { CUSTOM_ELEMENTS_SCHEMA, Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
    templateUrl: './empty-page-layout.component.html',
    standalone: true,
    imports: [RouterOutlet],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class EmptyPageLayoutComponent {}
