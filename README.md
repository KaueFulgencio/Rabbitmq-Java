# Order Messaging com RabbitMQ e MongoDB

## Sobre o projeto

Esse projeto foi criado com o objetivo **exclusivo de estudo e aprofundamento em mensageria**, utilizando **RabbitMQ** com **Spring Boot**. A ideia aqui não é apenas "fazer funcionar", mas entender de verdade como eventos trafegam entre sistemas, como são consumidos, processados e persistidos, simulando um cenário próximo do que é encontrado em sistemas reais de mercado.

O fluxo principal do projeto gira em torno da criação de um pedido (*Order Created*), onde um evento é publicado em uma fila do RabbitMQ e consumido por um listener, que então processa esse evento e persiste os dados no **MongoDB**.

Esse tipo de arquitetura é muito comum em ambientes que trabalham com **event-driven architecture**, **eventual consistency** e **desacoplamento entre serviços**.

---

## O que esse projeto faz

De forma resumida, o projeto:

* Recebe um evento de pedido criado via RabbitMQ
* Desserializa a mensagem automaticamente para um DTO de evento
* Processa o evento dentro de um listener
* Converte o evento para um modelo de persistência
* Persiste o pedido no MongoDB

Tudo isso utilizando boas práticas de organização de código, separação de responsabilidades e foco em aprendizado de mensageria.

---

## Tecnologias utilizadas

* Java
* Spring Boot
* Spring AMQP (RabbitMQ)
* MongoDB
* Lombok
* Maven

---

## Estrutura do projeto

O projeto segue uma separação clara de responsabilidades:

* **config**: configurações do RabbitMQ (queues, converters, etc)
* **listener**: consumidores das filas
* **listener.dto**: DTOs que representam os eventos recebidos
* **mapper**: conversão de eventos para documentos de persistência
* **document**: modelos que representam os documentos do MongoDB
* **repository**: acesso aos dados no MongoDB

Essa separação ajuda a manter o código organizado e facilita a evolução do projeto.

---

## Exemplo de evento consumido

Exemplo de mensagem enviada para a fila:

```json
{
  "orderId": 1001,
  "clientId": 1,
  "products": [
    {
      "productName": "Mouse",
      "quantity": 10,
      "price": 2.5
    },
    {
      "productName": "Teclado",
      "quantity": 10,
      "price": 9.5
    }
  ]
}
```

Esse JSON é convertido automaticamente para o evento:

```java
OrderCreatedEvent
```

---

## Objetivo de aprendizado

Os principais pontos de aprendizado desse projeto são:

* Entender como funciona o RabbitMQ na prática
* Aprender a configurar filas, listeners e conversores
* Trabalhar com eventos de forma desacoplada
* Persistir dados a partir de eventos
* Simular cenários reais de sistemas distribuídos

Esse projeto também serve como base para evoluções futuras, como:

* Dead Letter Queue (DLQ)
* Retry e backoff
* Ack manual
* Idempotência
* Versionamento de eventos

---

## Observação final

Esse projeto **não tem foco em interface ou regra de negócio complexa**. O foco é totalmente voltado para **mensageria, eventos e arquitetura**, servindo como um laboratório de ap
