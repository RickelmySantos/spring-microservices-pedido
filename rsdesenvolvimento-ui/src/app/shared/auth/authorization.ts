import { Permission } from 'src/app/shared/auth/permissions.enum.';
import { Role } from 'src/app/shared/auth/role.enum';

export const PERMISSION_MAP: Record<Permission, Role[]> = {
    // --- Permissões do USUARIO (Garçom, Caixa) ---

    [Permission.ACESSAR_PAINEL_OPERACIONAL]: [Role.ADMIN, Role.GESTOR, Role.USUARIO],
    [Permission.CRIAR_NOVO_PEDIDO]: [Role.ADMIN, Role.GESTOR, Role.USUARIO],
    [Permission.EDITAR_PEDIDO_EM_ABERTO]: [Role.ADMIN, Role.GESTOR, Role.USUARIO],
    [Permission.FECHAR_CONTA_PEDIDO]: [Role.ADMIN, Role.GESTOR, Role.USUARIO],

    // --- Permissões do GESTOR/ADMIN (Gerente) ---
    [Permission.CANCELAR_PEDIDO]: [Role.ADMIN, Role.GESTOR],
    [Permission.GERENCIAR_CARDAPIO]: [Role.ADMIN, Role.GESTOR],
    [Permission.VISUALIZAR_ESTOQUE]: [Role.ADMIN, Role.GESTOR, Role.USUARIO],
    [Permission.GERENCIAR_ESTOQUE]: [Role.ADMIN, Role.GESTOR],
    [Permission.VISUALIZAR_RELATORIO_VENDAS]: [Role.ADMIN, Role.GESTOR],
    [Permission.VISUALIZAR_EQUIPE]: [Role.ADMIN, Role.GESTOR, Role.USUARIO],

    // --- Permissões do ADMIN (Dono, Administrador do Sistema) ---
    [Permission.VISUALIZAR_RELATORIOS_FINANCEIROS]: [Role.ADMIN],
    [Permission.GERENCIAR_EQUIPE]: [Role.ADMIN, Role.GESTOR],
    [Permission.GERENCIAR_CONFIGURACOES_SISTEMA]: [Role.ADMIN],

    [Permission.VER_CARDAPIO_PUBLICO]: [Role.CLIENTE],
    [Permission.REALIZAR_PEDIDO_ONLINE]: [Role.CLIENTE],
    [Permission.VER_MEUS_PEDIDOS]: [Role.CLIENTE],
    [Permission.GERENCIAR_MEU_PERFIL]: [Role.CLIENTE],
    [Permission.FAZER_RESERVA]: [Role.CLIENTE],
};
