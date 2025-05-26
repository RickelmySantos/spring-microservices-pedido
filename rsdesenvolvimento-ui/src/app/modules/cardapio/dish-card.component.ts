import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA, Input } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';

@Component({
    selector: 'app-dish-card',
    template: `
        <div class="dish-card">
            <img [src]="dish.img" [alt]="dish.title" class="dish-img" />
            <div class="dish-info">
                <h3>{{ dish.title }}</h3>
                <p>{{ dish.desc }}</p>
                <span>{{ dish.price }}</span>
            </div>
        </div>
    `,
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule],
})
export class DishCardComponent {
    @Input() dish!: { title: string; desc: string; price: string; img: string };
}
