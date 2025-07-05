import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FooterComponent } from 'src/app/modules/footer/footer.component';
import { HeaderComponent } from 'src/app/modules/header/header.Component';
import { SharedModule } from 'src/app/shared/shared.module';

@Component({
    selector: 'app-layout',
    template: `
        <app-header></app-header>
        <main>
            <router-outlet></router-outlet>
        </main>

        <app-footer></app-footer>
    `,
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    imports: [SharedModule, HeaderComponent, FooterComponent, RouterOutlet],
})
export class LayoutComponent {}
