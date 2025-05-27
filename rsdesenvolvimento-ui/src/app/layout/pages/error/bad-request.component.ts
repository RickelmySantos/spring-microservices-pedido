import { Component, CUSTOM_ELEMENTS_SCHEMA, OnInit } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { SharedModule } from 'src/app/shared/shared.module';

@Component({
    selector: 'app-bad-request',

    template: `
        <div class="h-full flex justify-content-center align-items-center">
            <div class="w-8 md:w-5">
                <app-error-details image="bad-request" code="400" [title]="'mensagem.error.400.titulo' | translate" [description]="'mensagem.error.400.mensagem' | translate"></app-error-details>
            </div>
        </div>
    `,
    styles: [':host {display: block; height: 100%; }'],
    standalone: true,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [ButtonModule, SharedModule],
})
export class NameComponent implements OnInit {
    constructor() {}

    ngOnInit(): void {}
}
