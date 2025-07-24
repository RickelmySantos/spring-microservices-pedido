import { NgFor, NgIf } from '@angular/common';
import { ChangeDetectionStrategy, Component, CUSTOM_ELEMENTS_SCHEMA, Input } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { Role } from 'src/app/shared/enums/role.enum';
import { SharedModule } from 'src/app/shared/shared.module';

@Component({
    selector: 'app-menu-item',
    template: `
        <ng-container *ngIf="item && item.visible !== false">
            <ng-container>
                <li class="menu__item" [class.disabled]="item.disabled">
                    <!-- Grupo não clicável -->
                    <span *ngIf="!item.url && !item.routerLink && !item.items">
                        <i *ngIf="item.icon" [class]="item.icon"></i>
                        {{ item.label }}
                    </span>

                    <!-- Link externo -->
                    <a *ngIf="item.url" [href]="item.url" [attr.target]="item.target" (click)="item.command?.($event)">
                        <i *ngIf="item.icon" [class]="item.icon"></i>
                        {{ item.label }}
                    </a>

                    <!-- Link interno (routerLink) -->
                    <a *ngIf="item.routerLink" [routerLink]="item.routerLink" [attr.target]="item.target" (click)="item.command?.($event)">
                        <i *ngIf="item.icon" [class]="item.icon"></i>
                        {{ item.label }}
                    </a>

                    <!-- Submenu -->
                    <ul *ngIf="item.items?.length">
                        <ng-container *ngFor="let subItem of item.items">
                            <app-menu-item [item]="subItem"></app-menu-item>
                        </ng-container>
                    </ul>
                </li>
            </ng-container>
        </ng-container>
    `,
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    imports: [SharedModule, RouterModule, NgIf, NgFor],
})
export class MenuItemComponent {
    @Input()
    item!: CustomMenuItem;
}

export interface CustomMenuItem extends MenuItem {
    items?: CustomMenuItem[];
    rolesAllowed?: keyof typeof Role | Role | (keyof typeof Role)[] | Role[];
}
