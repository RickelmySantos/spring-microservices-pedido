import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FooterComponent } from 'src/app/modules/footer/footer.component';
import { HeaderComponent } from 'src/app/modules/header/HeaderComponent';
import { SharedModule } from 'src/app/shared/shared.module';

@Component({
    selector: 'app-layout',
    template: `
        <app-header></app-header>

        <router-outlet></router-outlet>

        <app-footer></app-footer>
    `,
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule, HeaderComponent, FooterComponent, RouterOutlet],
})
export class LayoutComponent {}
