export interface CriarPedidoRequest {
    observacao: string;
    usuarioId?: string;
    itensPedido: CriarItemPedido[];
}

export interface CriarItemPedido {
    produtoId: number;
    quantidade: number;
    precoUnitario: number;
    nomeProduto?: string;
}
