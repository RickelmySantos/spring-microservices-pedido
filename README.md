# 🛒 Microserviços de Pedido com Spring Boot + Kafka

Este projeto é uma demonstração de arquitetura baseada em **microserviços** utilizando **Spring Boot**, **Apache Kafka**, **Spring Cloud** e **JPA**. A aplicação simula o fluxo de um pedido completo, desde a criação do usuário até a notificação de finalização do pedido.

---

## 🔧 Tecnologias Utilizadas

- Java 21
- Spring Boot 3.x
- Spring Web / WebFlux
- Spring Data JPA
- Spring Validation
- Spring Kafka
- Apache Kafka + Zookeeper (via Docker)
- Lombok
- PostgreSql

---

## 🧱 Arquitetura dos Microserviços

| Serviço                 | Responsabilidade                                   |
| ----------------------- | -------------------------------------------------- |
| **User Service**        | CRUD de usuários                                   |
| **Pedido Service**      | Recebe pedidos, valida usuários e envia para Kafka |
| **Pagamento Service**   | Processa pagamento e envia evento de finalização   |
| **Notificacao Service** | Escuta eventos e simula envio de notificação       |

Comunicação entre serviços:

- REST: PedidoService → UserService (validação do usuário)
- Kafka:
  - PedidoService → Kafka → PagamentoService
  - PagamentoService → Kafka → NotificacaoService

---

## 🔄 Fluxo de Funcionamento

1. Usuário é criado via UserService.
2. Pedido é enviado via PedidoService.
3. PedidoService publica mensagem no tópico `pedido-criado`.
4. PagamentoService consome, processa, e publica em `pedido-finalizado`.
5. NotificacaoService consome e "envia" notificação ao usuário.

---

## 🚀 Como Executar Localmente

### Pré-requisitos

- Java 21
- Maven
- Docker + Docker Compose

### 1. Subir Kafka com Docker Compose

```bash
docker-compose up -d
```
