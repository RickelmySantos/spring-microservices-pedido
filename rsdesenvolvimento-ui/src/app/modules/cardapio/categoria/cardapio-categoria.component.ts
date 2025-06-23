import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA, EventEmitter, Input, Output } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';

@Component({
    selector: 'app-cardapio-categoria',
    template: `
        <div class="menu-categories">
            <button *ngFor="let categoria of categorias" [class.active]="categoriaAtiva === categoria.value" (click)="selecionarCategoria(categoria.value)">
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
    @Input() categoriaAtiva: string = 'all';

    @Output() onCategoriaChange = new EventEmitter<string>();

    selecionarCategoria(categoria: string): void {
        this.onCategoriaChange.emit(categoria);
    }

    categorias = [
        { label: 'Todos', value: 'all' },
        { label: 'Entradas', value: 'entradas' },
        { label: 'Pratos Principais', value: 'Prato Principal' },
        { label: 'Sobremesas', value: 'Sobremesa' },
        { label: 'Bebidas', value: 'bebidas' },
    ];
}
