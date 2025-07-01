import { NgClass, NgIf } from '@angular/common';
import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { SharedModule } from 'src/app/shared/shared.module';

@Component({
    selector: 'app-error-details',
    template: `
        <section class="text-center select-none">
            <div [ngClass]="{ 'mb-4': !code }"><img src="assets/images/error/{{ image }}.svg" class="h-auto w-7" /></div>
            <p *ngIf="!!code" class="font-extrabold text-9xl text-primary">{{ code }}</p>
            <p *ngIf="!!title" class="font-extrabold text-4xl">{{ title }}</p>
            <p *ngIf="!!description" class="text-color-p-secondary text-lg font-medium">{{ description }}</p>
            <div class="flex justify-content-center gap-4 pt-6">
                <button *ngIf="showVoltar" pButton pRipple class="p-button-raised p-button-lg p-button-text" [routerLink]="['/']">
                    <fa-icon [icon]="['fas', 'arrow-left-long']" size="lg" class="mr-2"></fa-icon>
                    {{ 'botao.voltar' | translate }}
                </button>
                <ng-content></ng-content>
            </div>
        </section>
    `,
    standalone: true,
    imports: [RippleModule, ButtonModule, NgIf, NgClass, RouterLink, SharedModule],
})
export class ErrorDetailsComponent {
    @Input()
    code: string;

    @Input()
    title: string;

    @Input()
    description: string;

    @Input()
    image: string = 'error';

    @Input()
    showVoltar: boolean = true;
}
