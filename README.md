# Microserviços de Pedido com Spring Boot + RabbitMQ

Este projeto demonstra uma arquitetura baseada em **microserviços** utilizando **Spring Boot**, **Rabbitmq**, **Spring Cloud** e **JPA**. A aplicação simula o fluxo de um pedido completo, desde a criação do usuário até a notificação de finalização do pedido via **SendGrid**.

---

## 🔧 Tecnologias Utilizadas

- Java 21
- Spring Boot 3.x
- Spring Web
- Spring Data JPA
- Spring Validation
- Lombok
- PostgreSql + flyway
- Mapstruct

-Microsserviços e Integração

- Spring Cloud
- OpenFeing
- Eureka
- Resilienc4j
- Rabbitmq

**Observabilidade**

- Spring Boot Actuator
- Micrometer
- Prometheus
- Grafana

---

## 🧱 Arquitetura dos Microserviços

| Serviço                 | Responsabilidade                                              |
| ----------------------- | ------------------------------------------------------------- |
| **User Service**        | CRUD de usuários                                              |
| **Pedido Service**      | Recebe pedidos, valida usuários e envia para fila             |
| **Pagamento Service**   | Processa pagamento e envia evento de finalização              |
| **Notificacao Service** | Consome a fila do RabbitMQ e envia notificação para o cliente |

Comunicação entre serviços:

- REST: PedidoService → UserService (validação do usuário)
- RabbitMQ:
  - PedidoService → RabbitMQ → NotificacaoService

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

### 1. Subir com Docker Compose

```bash
docker-compose up -d
```
