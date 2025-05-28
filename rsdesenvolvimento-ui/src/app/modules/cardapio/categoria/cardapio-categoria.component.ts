import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';

@Component({
    selector: 'app-cardapio-categoria',
    template: `
        <div class="menu-categories">
            <button *ngFor="let categoria of categorias" [class.active]="categoriaSelecionada === categoria.value" [attr.data-categoria]="categoria.value" (click)="categoriaFIltrada(categoria.value)">
                {{ categoria.label }}
            </button>
        </div>
    `,
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule, NgFor],
})
export class CardapioCategoriaComponent {
    categoriaSelecionada = 'all';

    categoriaFIltrada(categoria: string) {
        this.categoriaSelecionada = categoria;
    }

    categorias = [
        { label: 'Todos', value: 'all' },
        { label: 'Entradas', value: 'entradas' },
        { label: 'Pratos Principais', value: 'principal' },
        { label: 'Sobremesas', value: 'sobremesas' },
        { label: 'Bebidas', value: 'bebidas' },
    ];
}
