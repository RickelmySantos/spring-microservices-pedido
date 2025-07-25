import { CustomMenuItem } from 'src/app/core/layout/menu-item/menu-item.component';
import { Permission } from 'src/app/shared/auth/permissions.enum.';

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
        permissionsAllowed: [Permission.GERENCIAR_ESTOQUE],
    },
];
