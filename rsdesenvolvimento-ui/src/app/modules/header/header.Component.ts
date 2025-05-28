import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { MenuComponent } from 'src/app/core/layout/menu/menu.component';
import { SharedModule } from 'src/app/shared/shared.module';

@Component({
    selector: 'app-header',
    template: `
        <header id="header">
            <app-menu></app-menu>
        </header>
    `,
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule, MenuComponent],
})
export class HeaderComponent {
    menuOpen = false;

    toggleMenu() {
        this.menuOpen = !this.menuOpen;
    }
}
