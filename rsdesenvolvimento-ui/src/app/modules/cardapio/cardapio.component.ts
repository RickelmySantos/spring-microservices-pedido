import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';

@Component({
    selector: 'app-cardapio',
    template: `
        <section class="menu" id="cardapio">
            <div class="container">
                <app-section-title title="Nosso Cardápio"></app-section-title>
                <div class="dishes-grid">
                    <app-dish-card *ngFor="let dish of dishes" [dish]="dish"></app-dish-card>
                </div>
                <div class="menu-btn-container">
                    <a href="#" class="btn btn-outline">Ver Cardápio Completo</a>
                </div>
            </div>
        </section>
    `,
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule, NgFor],
})
export class CardapioComponent {
    dishes = [
        {
            title: 'Feijoada Completa',
            desc: 'O prato mais tradicional do Brasil...',
            price: 'R$ 45,90',
            img: 'URL1',
        },
        {
            title: 'Moqueca Baiana',
            desc: 'Peixe fresco cozido...',
            price: 'R$ 52,90',
            img: 'URL2',
        },
        {
            title: 'Pão de Queijo',
            desc: 'Delicioso pão de queijo...',
            price: 'R$ 12,90 (6 unidades)',
            img: 'URL3',
        },
    ];
}
