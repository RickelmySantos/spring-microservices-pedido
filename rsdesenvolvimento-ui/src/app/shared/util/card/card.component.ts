import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';

@Component({
    selector: 'app-card',
    template: `
        <!-- <article class="w-28rem bg-white border-round-lg shadow-md overflow-hidden transition-all transition-duration-300 flex flex-column hover:shadow-lg">
            <section class="relative h-12rem overflow-hidden">
                <img [src]="item.imagemUrl" [alt]="item.alt" class="w-full h-full object-cover" />

                <div class="absolute top-2 right-2 bg-primary-custom text-white text-xs font-bold p-1 px-2 border-round-sm">
                    {{ item.categoria }}
                </div>
            </section>

            <section class="p-4 flex flex-column flex-grow-1">
                <div class="flex justify-content-between align-items-start mb-2">
                    <h3 class="text-xl font-bold text-gray-800 m-0">{{ item.nome }}</h3>
                    <span class="text-gray-600 font-bold white-space-nowrap ml-4">{{ item.preco | currency : 'BRL' }}</span>
                </div>

                <p class="text-gray-600 text-sm mb-4 flex-grow-1 m-0">
                    {{ item.descricao }}
                </p>

                <button
                    (click)="onAdicionar()"
                    [attr.aria-label]="'Adicionar ' + item.nome + ' ao pedido'"
                    class="w-full bg-primary-custom text-white p-2 border-round-md transition-colors transition-duration-200 flex align-items-center justify-content-center border-none cursor-pointer hover:bg-primary-custom-dark">
                    <span>Adicionar</span>
                </button>
            </section>
        </article> -->
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
