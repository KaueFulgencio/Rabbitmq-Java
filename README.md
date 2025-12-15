# Order Messaging with RabbitMQ and MongoDB

## About the project

This project was created with the **exclusive goal of studying and deeply understanding messaging**, using **RabbitMQ** with **Spring Boot**. The intention here is not just to "make it work", but to truly understand how events flow between systems, how they are consumed, processed, and persisted, simulating scenarios very close to what is found in real-world production systems.

The main flow of the project revolves around an *Order Created* event, where an event is published to a RabbitMQ queue and consumed by a listener, which then processes this event and persists the data into **MongoDB**.

This type of architecture is very common in systems that adopt **event-driven architecture**, **eventual consistency**, and **service decoupling**.

---

## What this project does

In a simplified way, this project:

* Receives an order created event via RabbitMQ
* Automatically deserializes the message into an event DTO
* Processes the event inside a listener
* Converts the event into a persistence model
* Persists the order data into MongoDB

All of this is done following good practices such as clear separation of responsibilities, clean code organization, and a strong focus on learning messaging concepts.

---

## Technologies used

* Java
* Spring Boot
* Spring AMQP (RabbitMQ)
* MongoDB
* Lombok
* Maven

---

## Project structure

The project follows a clear separation of concerns:

* **config**: RabbitMQ configurations (queues, converters, etc.)
* **listener**: queue consumers
* **listener.dto**: DTOs that represent the received events
* **mapper**: conversion from events to persistence documents
* **document**: MongoDB document models
* **repository**: MongoDB data access layer

This structure helps keep the codebase organized and makes the project easier to evolve.

---

## Example of consumed event

Example of a message sent to the queue:

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

This JSON is automatically converted into the following event:

```java
OrderCreatedEvent
```

---

## Learning goals

The main learning goals of this project are:

* Understand how RabbitMQ works in practice
* Learn how to configure queues, listeners, and message converters
* Work with events in a decoupled way
* Persist data based on consumed events
* Simulate real-world distributed system scenarios

This project also serves as a foundation for future improvements, such as:

* Dead Letter Queue (DLQ)
* Retry and backoff strategies
* Manual acknowledgements
* Idempotency
* Event versioning

---

## Final notes

This project **does not focus on UI or complex business rules**. The entire focus is on **messaging, events, and architecture**, serving as a learning lab to experiment and gain hands-on experience with RabbitMQ and event-driven systems.
