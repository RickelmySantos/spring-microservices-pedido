import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FooterComponent } from 'src/app/modules/footer/footer.component';
import { HeaderComponent } from 'src/app/modules/header/header.Component';
import { SharedModule } from 'src/app/shared/shared.module';

import { SobreComponent } from 'src/app/shared/components/sobre/sobre.component';
import { DepoimentoComponent } from '../shared/components/depoimentos/depoimento.component';
import { NewsletterComponent } from '../shared/components/newsletter/newsletter.component';

@Component({
    selector: 'app-layout',
    template: `
        <app-header></app-header>

        <router-outlet></router-outlet>
        <app-sobre></app-sobre>
        <app-depoimento></app-depoimento>
        <app-newsletter></app-newsletter>
        <app-footer></app-footer>
    `,
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule, HeaderComponent, FooterComponent, RouterOutlet, SobreComponent, DepoimentoComponent, NewsletterComponent],
})
export class LayoutComponent {}
