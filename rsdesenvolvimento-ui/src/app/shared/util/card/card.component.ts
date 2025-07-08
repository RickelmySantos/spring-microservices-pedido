import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';

@Component({
    selector: 'app-card',
    template: `
        <article
            class="w-28rem bg-white border-round-lg shadow-md overflow-hidden
                    transition-all transition-duration-300 flex flex-column hover:shadow-lg">
            <header>
                <ng-content select="[card-image]"></ng-content>
            </header>

            <section class="p-4 flex flex-column flex-grow-1">
                <ng-content select="[card-content]"></ng-content>
            </section>

            <footer class="p-4 pt-0">
                <ng-content></ng-content>
            </footer>
        </article>
    `,
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule],
})
export class CardComponent {}
