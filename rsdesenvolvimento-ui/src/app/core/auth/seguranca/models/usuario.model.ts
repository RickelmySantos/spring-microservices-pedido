export interface User {
    name: string;

    username?: string;

    email: string;

    roles: string[];
}

export function mapUser(claims: any): User {
    const realmRoles = claims?.realm_access?.roles || [];

    const clientRoles = Object.values(claims?.resource_access || {}).flatMap((resource: any) => resource.roles || []);

    const allRoles = [...new Set([...realmRoles, ...clientRoles])];

    return {
        name: claims?.name || '',
        username: claims?.preferred_username || '',
        email: claims?.email || '',
        roles: allRoles,
    };
}
