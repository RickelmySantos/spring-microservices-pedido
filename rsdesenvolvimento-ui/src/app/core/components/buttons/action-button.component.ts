import { NgClass } from '@angular/common';
import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA, EventEmitter, Input, Output } from '@angular/core';
import { IconProp } from '@fortawesome/fontawesome-svg-core';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { SharedModule } from 'src/app/shared/shared.module';

type ButtonType = 'card' | 'checkout' | 'action';

@Component({
    selector: 'action-button',
    template: `
        <ng-container>
            <button
                pRipple
                [class]="buttonClass"
                (click)="click($event)"
                [ngClass]="{
                    'p-button-raised': isActionButton(),
                    'p-button-sm p-button-rounded': isCheckoutButton(),
                    'w-full text-white p-3 border-round-lg flex align-items-center justify-content-center border-none cursor-pointer mt-auto bg-rose-800': isCardButton()
                }">
                <ng-content></ng-content>
            </button>
        </ng-container>
    `,
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule, ButtonModule, RippleModule, NgClass],
})
export class ActionButtonComponent {
    private _show: boolean;

    get show(): boolean {
        return this._show;
    }

    @Output()
    onClick = new EventEmitter<void>();

    @Input()
    contextLabel: string;

    @Input()
    ariaControls: string;

    @Input()
    ariaExpanded: boolean = false;

    @Input()
    responsive: boolean = false;

    @Input()
    disabled: boolean = false;

    @Input()
    iconsOnly: boolean = false;

    @Input()
    ariaLabel: string;

    @Input()
    label: string;

    @Input()
    icon: IconProp;

    @Input()
    buttonClass: string;

    @Input()
    type: ButtonType = 'action';

    isActionButton(): boolean {
        return !this.type || this.type === 'action';
    }

    isCheckoutButton(): boolean {
        return this.type === 'checkout';
    }

    isCardButton(): boolean {
        return this.type === 'card';
    }

    click(event: PointerEvent): void {
        event.preventDefault();
        this.onClick.emit();
    }
}
