import { User } from 'src/app/core/auth/seguranca/models/usuario.model';
import { PERMISSION_MAP } from 'src/app/shared/auth/authorization';
import { Permission } from 'src/app/shared/auth/permissions.enum.';
import { Role } from 'src/app/shared/auth/role.enum';

export function hasRole(user: User, roles: keyof typeof Role | Role | (keyof typeof Role)[] | Role[]): boolean {
    if (!user) return false;

    if (!roles || (Array.isArray(roles) && roles.length === 0)) {
        return true;
    }
    if (Array.isArray(roles)) {
        return roles.some(role => user.roles?.includes(role));
    }
    return user.roles?.includes(roles);
}
export function hasPermission(user: User, permissions: Permission | Permission[]): boolean {
    if (!user) return false;

    if (!permissions || (Array.isArray(permissions) && permissions.length === 0)) return true;

    const permissionList = Array.isArray(permissions) ? permissions : [permissions];

    return permissionList.every(p => checkPermissions(user, p));
}
function checkPermissions(user: User, permission: Permission): boolean {
    if (!user?.roles?.length) return false;

    const allowedRoles = PERMISSION_MAP[permission] || [];
    const hasAccess = user.roles.some(userRole => allowedRoles.includes(userRole as Role));

    return hasAccess;
}
