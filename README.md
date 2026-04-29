# RabbitMQ Event-Driven System

Projeto de estudo sobre event-driven architecture com RabbitMQ, Spring Boot e MongoDB. Foco em entender como mensagens fluem entre sistemas, como consumir, processar e persistir dados.

A ideia aqui é aprender de verdade, não só fazer funcionar. Começou processando eventos de ônibus (Bus Status) mas o conceito é aplicável a qualquer tipo de event de negócio.

## O que faz

Publicas um evento via REST -> entra em uma fila RabbitMQ -> um consumer processa -> persiste no MongoDB.

Se tudo dá certo: ACK. Se falha: vai pra Dead Letter Queue (DLQ) pra auditoria.

```
REST API (POST /bus-status/publish)
    |
    v
RabbitTemplate (Producer)
    |
    v
RabbitMQ Exchange -> Queue
    |
    v
Listener (Consumer com Manual ACK)
    |
    +---> SUCCESS: basicAck() -> MongoDB
    |
    +---> ERROR: basicNack() -> DLQ (Dead Letter Queue)
```

## Tech Stack

- Java 21
- Spring Boot 3.4.1
- Spring AMQP
- RabbitMQ 3.13
- MongoDB
- Lombok
- Maven
- Docker Compose

## Como rodar

### Pré-requisitos

Java 21, Docker, Maven

### Setup

```bash
# Clone
git clone https://github.com/KaueFulgencio/Rabbitmq-Java.git
cd Rabbitmq-Java

# Suba os containers (MongoDB + RabbitMQ)
cd local
docker-compose up -d

# Execute a aplicação
./mvnw spring-boot:run
```

Aguarde a mensagem "Started OrderApplication..." no console.

## Endpoints

### Publicar evento (vai pro MongoDB se der certo)

```bash
curl -X POST http://localhost:8080/bus-status/publish \
  -H "Content-Type: application/json" \
  -d '{
    "lineName": "Linha 15",
    "lineNumber": "123",
    "active": true,
    "latitude": -23.5505,
    "longitude": -46.6333,
    "timestamp": "2026-04-28T21:00:00Z"
  }'
```

No console da app você verá:
```
Recebendo evento: ...
Evento processado com sucesso e ACK enviado.
```

### Publicar com erro simulado (vai pra DLQ)

```bash
curl -X POST http://localhost:8080/bus-status/publish-with-error \
  -H "Content-Type: application/json" \
  -d '{...}'
```

No console:
```
Recebendo evento: ...
Simulando erro no processamento...
Erro ao processar evento: Erro simulado para demonstração de DLQ
Mensagem rejeitada e enviada para DLQ.
```

### Listar tudo que foi salvo

```bash
curl http://localhost:8080/bus-status/listAll
```

### Controlar modo erro

```bash
GET  http://localhost:8080/bus-status/error-mode
POST http://localhost:8080/bus-status/error-mode/toggle
```

## Visualizar as filas

### RabbitMQ Management UI

http://localhost:15672
Usuario: guest
Senha: guest

Vai em "Queues" e você vê:
- bus-status-queue: eventos que processaram ok
- bus-status-dlq: eventos que falharam

### MongoDB

```bash
docker exec -it local-mongodb-1 mongosh -u admin -p 123
use order-producao
db.td_bus_status.find().pretty()
db.td_bus_status.countDocuments()
```

## Estrutura

```
src/main/java/kaue/dev/order/
├── config/
│   ├── MongoConfig.java
│   └── RabbitMqConfig.java         # Configuração do Exchange, Queue, DLQ
├── controller/
│   └── BusStatusController.java    # REST endpoints
├── listener/
│   ├── BusStatusCreatedListener.java    # Consumer (processa a mensagem)
│   ├── BusStatusEventMapper.java        # Converte DTO em Entity
│   └── dto/
│       └── BusStatusCreatedEvent.java   # O objeto que chega na fila
├── entity/
│   └── BusStatusEntity.java        # Documento MongoDB
├── repository/
│   └── BusStatusRepository.java     # Queries MongoDB
├── service/
│   ├── BusStatusService.java        # Lógica de negócio
│   └── BusStatusProducer.java       # Publica mensagens (RabbitTemplate)
└── OrderApplication.java

local/
└── docker-compose.yml              # MongoDB + RabbitMQ
```

## Os conceitos importantes

### Direct Exchange

A mensagem é roteada diretamente pra fila baseado em uma routing key exata. No caso aqui:
- Exchange: bus-status-exchange
- Routing Key: bus.status.created
- Queue: bus-status-queue

### Manual ACK/NACK

Por padrão, RabbitMQ confirma a mensagem automaticamente. Problema: se falhar no meio do processamento, a mensagem já desapareceu.

Com Manual ACK você controla quando confirmar:

```java
try {
    processEvent(event);
    channel.basicAck(deliveryTag, false);        // Processou? OK
} catch(Exception e) {
    channel.basicNack(deliveryTag, false, false); // Falhou? Vai pra DLQ
}
```

### Dead Letter Queue

Toda fila pode ter um DLX (Dead Letter Exchange). Se você fizer NACK em uma mensagem, ela automaticamente vai pra esse exchange especial que roteia pra DLQ.

Configuração:
```java
Queue busStatusQueue() {
    return QueueBuilder
        .durable(BUS_STATUS_QUEUE)
        .withArgument("x-dead-letter-exchange", BUS_STATUS_DLX)
        .withArgument("x-dead-letter-routing-key", BUS_STATUS_DLQ_ROUTING_KEY)
        .build();
}
```

### Concurrent Consumers

Spring AMQP por padrão cria uns listeners rodando em paralelo. Configurável:

```java
factory.setConcurrentConsumers(3);
factory.setMaxConcurrentConsumers(10);
```

Chegar 10 mensagens? Pode processar 10 ao mesmo tempo.

## Commands úteis

```bash
# Ver quantas mensagens tem em cada fila
docker exec -it local-rabbitmq-1 rabbitmqctl list_queues name messages

# Listar exchanges
docker exec -it local-rabbitmq-1 rabbitmqctl list_exchanges

# Se der erro de configuração, delete as filas
docker exec -it local-rabbitmq-1 rabbitmqctl delete_queue bus-status-queue
docker exec -it local-rabbitmq-1 rabbitmqctl delete_queue bus-status-dlq
```

## Números reais do projeto

- 200+ eventos processados
- Taxa de sucesso: 97.6%
- Eventos perdidos: 0
- 5 mensagens capturadas no DLQ (pra debug)

## O que aprendi

- Como RabbitMQ funciona na prática
- Producer-Consumer pattern
- Manual acknowledgment
- Dead Letter Queue pra resiliência
- Eventual consistency em sistemas distribuídos
- Como debugar falhas em filas
- Concurrent processing

## Próximas ideias

- Listener pra reprocessar DLQ
- Retry com backoff exponencial
- Versioning de eventos
- Checks de idempotência
- Métricas com Actuator
- Distributed tracing

## Documentação

Ver também:
- RABBITMQ_GUIDE.md - Step by step pra testar tudo
- AGENTS.md - Especificação técnica da arquitetura

## Por que fiz isso

Mensageria é um dos conceitos mais importantes em sistemas distribuídos. A maioria dos cursos online ensina de forma muito superficial. Aqui é real: RabbitMQ rodando local, processando de verdade, persistindo dados, tratando erro.

Perfeito pra entender como funciona antes de trabalhar em algum projeto maior que use eventos.
