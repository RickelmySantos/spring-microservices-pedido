export enum Permission {
    // Ações do Painel e Pedidos
    ACESSAR_PAINEL_OPERACIONAL,
    CRIAR_NOVO_PEDIDO,
    EDITAR_PEDIDO_EM_ABERTO,
    FECHAR_CONTA_PEDIDO,
    CANCELAR_PEDIDO,

    // Ações do Cardápio
    GERENCIAR_CARDAPIO,

    // Ações do Estoque
    VISUALIZAR_ESTOQUE,
    GERENCIAR_ESTOQUE,

    // Ações de Relatórios e Análises
    VISUALIZAR_RELATORIO_VENDAS,
    VISUALIZAR_RELATORIOS_FINANCEIROS,

    // Ações de Administração de Pessoal
    VISUALIZAR_EQUIPE,
    GERENCIAR_EQUIPE,

    // Ações de Configuração do Sistema
    GERENCIAR_CONFIGURACOES_SISTEMA,

    VER_CARDAPIO_PUBLICO,
    REALIZAR_PEDIDO_ONLINE,
    VER_MEUS_PEDIDOS,
    GERENCIAR_MEU_PERFIL,
    FAZER_RESERVA,
}
