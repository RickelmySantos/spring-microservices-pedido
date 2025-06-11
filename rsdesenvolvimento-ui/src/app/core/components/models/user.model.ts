export interface User {
    id: string;
    login: string;
    name: string;
    email: string;

    roles: string[];
}

export interface UserInfo {
    cpf: string;
    occupation?: string | Info;
    department?: string | Info;
    contact?: string;
}

export interface Info {
    id?: number | string;
    initials?: string;
    title?: string;
    description?: string;
}

export function isUser(obj: any): obj is User {
    return obj && 'login' in obj;
}

export function mapUser(user: any): User {
    if (!user) return null;
    return { login: user.login, name: user.nome, email: user.email } as User;
}
