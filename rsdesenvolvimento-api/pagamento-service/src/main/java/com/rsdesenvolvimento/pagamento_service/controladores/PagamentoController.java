package com.rsdesenvolvimento.pagamento_service.controladores;

import com.rsdesenvolvimento.pagamento_service.modelo.dtos.PagamentoRequestDto;
import com.rsdesenvolvimento.pagamento_service.modelo.entidades.Pagamento;
import com.rsdesenvolvimento.pagamento_service.services.PagamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pagamentos")
@RequiredArgsConstructor
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @PutMapping
    public ResponseEntity<Pagamento> pagar(@RequestBody PagamentoRequestDto dto) {
        return ResponseEntity.ok(this.pagamentoService.processarPagamento(dto));
    }

}
