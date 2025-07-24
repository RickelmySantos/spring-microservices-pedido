import { CustomMenuItem } from 'src/app/core/layout/menu-item/menu-item.component';
import { Role } from 'src/app/shared/enums/role.enum';

export const MENU: CustomMenuItem[] = [
    {
        label: 'menu.home',
        url: '#home',
    },
    {
        label: 'menu.cardapio',
        routerLink: ['/menu'],
    },
    {
        label: 'menu.sobre',
        url: '#sobre',
    },
    {
        label: 'menu.contato',
        url: '#contato',
    },
    {
        label: 'menu.upload',
        routerLink: ['/upload'],
        rolesAllowed: [Role.ADMIN, Role.GESTOR],
    },
];
