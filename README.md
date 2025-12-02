<br />
<div align="center">
<h3 align="center">PRAGMA POWER-UP - TRACEABILITY AND AUDIT MICROSERVICE</h3>
  <p align="center">
    Microservicio de trazabilidad y auditoría para el sistema de restaurantes. Registra y consulta el historial de cambios de estado de pedidos para auditoría y trazabilidad.
  </p>
</div>

### Built With

* ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
* ![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
* ![MongoDB](https://img.shields.io/badge/MongoDB-47A248?style=for-the-badge&logo=mongodb&logoColor=white)
* ![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)

## Descripción

Este microservicio es responsable de:
- **Registro de auditorías**: Almacena cada cambio de estado de pedidos con información detallada
- **Consulta de histórico**: Permite consultar el historial de cambios con filtros opcionales
- **Seguridad**: Los clientes solo pueden consultar el historial de sus propios pedidos

## Características

- **Puerto**: 8083
- **Base de datos**: MongoDB (traceability_audit)
- **Arquitectura**: Hexagonal (Puertos y Adaptadores)
- **Principios**: SOLID, Clean Code

## Endpoints

### POST /audit/order-status
Registra una auditoría de cambio de estado de pedido.

**Request Body:**
```json
{
  "orderId": 1,
  "restaurantId": 5,
  "clientId": 10,
  "previousStatus": "PENDIENTE",
  "newStatus": "EN_PREPARACION",
  "changedByUserId": 7,
  "changedByRole": "EMPLEADO",
  "actionType": "STATUS_CHANGE",
  "employeeId": 7,
  "ipAddress": "192.168.1.100",
  "userAgent": "Mozilla/5.0",
  "notes": "Pedido asignado al empleado",
  "timeInPreviousStatusMinutes": 15
}
```

**Response:**
```json
{
  "data": {
    "id": "507f1f77bcf86cd799439011",
    "orderId": 1,
    "restaurantId": 5,
    "clientId": 10,
    "previousStatus": "PENDIENTE",
    "newStatus": "EN_PREPARACION",
    "changedByUserId": 7,
    "changedByRole": "EMPLEADO",
    "changedAt": "2025-12-02T10:30:00",
    "actionType": "STATUS_CHANGE",
    "employeeId": 7,
    "ipAddress": "192.168.1.100",
    "userAgent": "Mozilla/5.0",
    "notes": "Pedido asignado al empleado",
    "timeInPreviousStatusMinutes": 15
  }
}
```

### GET /audit/order-status
Consulta el historial de auditorías con filtros opcionales y paginación.

**Query Parameters:**
- `clientId`: ID del cliente (obligatorio para clientes, opcional para otros roles)
- `orderId`: ID del pedido
- `actionTypes`: Lista de tipos de acción (filtro múltiple)
- `page`: Número de página (default: 0)
- `size`: Tamaño de página (default: 10)

**Ejemplo de consulta:**
```
GET /audit/order-status?clientId=10&actionTypes=STATUS_CHANGE&actionTypes=ASSIGNMENT&page=0&size=10
```

**Response:**
```json
{
  "data": [
    {
      "id": "507f1f77bcf86cd799439011",
      "orderId": 1,
      "restaurantId": 5,
      "clientId": 10,
      "previousStatus": "PENDIENTE",
      "newStatus": "EN_PREPARACION",
      "changedByUserId": 7,
      "changedByRole": "EMPLEADO",
      "changedAt": "2025-12-02T10:30:00",
      "actionType": "STATUS_CHANGE",
      "employeeId": 7,
      "ipAddress": "192.168.1.100",
      "userAgent": "Mozilla/5.0",
      "notes": "Pedido asignado al empleado",
      "timeInPreviousStatusMinutes": 15
    }
  ],
  "meta": {
    "page": 0,
    "size": 10,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

## Arquitectura

El proyecto sigue una arquitectura hexagonal con las siguientes capas:

### Domain (Dominio)
Contiene la lógica de negocio pura, sin dependencias de frameworks externos.

- **api**: Puertos de entrada (interfaces de servicios)
  - `IOrderStatusAuditServicePort`: Define las operaciones del servicio de auditoría
  
- **spi**: Puertos de salida (interfaces de persistencia)
  - `IOrderStatusAuditPersistencePort`: Define las operaciones de persistencia
  
- **model**: Modelos de dominio
  - `OrderStatusAuditModel`: Modelo de dominio para auditoría
  
- **usecase**: Casos de uso con lógica de negocio
  - `OrderStatusAuditUseCase`: Implementa las reglas de negocio para auditoría
  
- **exception**: Excepciones del dominio
  - `DomainException`: Excepción base del dominio

### Application (Aplicación)
Capa de orquestación entre la infraestructura y el dominio.

- **handler**: Handlers que coordinan entre controllers y domain
  - `IOrderStatusAuditHandler`: Interface del handler
  - `OrderStatusAuditHandler`: Implementación que coordina las operaciones
  
- **mapper**: Mappers entre DTOs y modelos de dominio
  - `IOrderStatusAuditMapper`: Mapper usando MapStruct

### Infrastructure (Infraestructura)
Implementaciones técnicas y adaptadores.

- **input/rest**: Controllers REST
  - `OrderStatusAuditController`: Controlador REST que expone los endpoints
  
- **out/mongodb**: Adaptadores de persistencia MongoDB
  - **document**: Documentos MongoDB
    - `OrderStatusAuditDocument`: Documento con índices optimizados
  - **repository**: Repositorios Spring Data MongoDB
    - `IOrderStatusAuditRepository`: Repositorio con queries personalizadas
  - **mapper**: Mappers entre documentos y modelos
    - `IOrderStatusAuditDocumentMapper`: Mapper usando MapStruct
  - **adapter**: Implementaciones de puertos de persistencia
    - `OrderStatusAuditMongoAdapter`: Adaptador MongoDB
    
- **configuration**: Configuración de beans
  - `BeanConfiguration`: Configuración de beans del dominio
  
- **exceptionhandler**: Manejo global de excepciones
  - `ControllerAdvisor`: Maneja excepciones globalmente

## Tecnologías

- **Java 17**: Lenguaje de programación
- **Spring Boot 3.2.0**: Framework principal
- **Spring Data MongoDB**: Persistencia de datos NoSQL
- **MongoDB**: Base de datos NoSQL orientada a documentos
- **MapStruct**: Mapeo automático de objetos
- **Lombok**: Reducción de código boilerplate
- **OpenAPI 3.0 / Swagger**: Documentación de API
- **Gradle**: Gestión de dependencias y compilación

## Configuración

### Variables de entorno

```properties
SERVER_PORT=8083
MONGODB_URI=mongodb://localhost:27017/traceability_audit
```

### Base de datos

MongoDB se encarga de crear la base de datos y la colección automáticamente. Los índices se crean automáticamente gracias a las anotaciones `@Indexed` en el documento:
- `order_id`: Para búsquedas por pedido
- `client_id`: Para búsquedas por cliente
- `changed_at`: Para ordenamiento temporal
- `action_type`: Para filtros por tipo de acción

## Ejecución

### Desarrollo local

```bash
./gradlew bootRun
```

### Compilación

```bash
./gradlew build
```

### Ejecutar tests

```bash
./gradlew test
```

## Documentación API

Una vez iniciado el proyecto, acceder a:

- **Swagger UI**: http://localhost:8083/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8083/api-docs

## Validaciones y Reglas de Negocio

### Registro de auditoría
- `orderId` es obligatorio
- `restaurantId` es obligatorio
- `clientId` es obligatorio
- `newStatus` es obligatorio
- `changedByUserId` es obligatorio
- `changedByRole` es obligatorio
- `changedAt` se establece automáticamente si no se proporciona

### Consulta de histórico
- Al menos un filtro debe ser proporcionado (clientId, orderId o actionTypes)
- Los clientes solo pueden consultar sus propios pedidos
- Soporta filtrado por múltiples tipos de acción
- Resultados ordenados por fecha descendente (más reciente primero)

## Estructura del Proyecto

```
trazability-audit/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/pragma/powerup/
│   │   │       ├── PowerUpApplication.java
│   │   │       ├── application/
│   │   │       │   ├── handler/
│   │   │       │   │   ├── IOrderStatusAuditHandler.java
│   │   │       │   │   └── impl/
│   │   │       │   │       └── OrderStatusAuditHandler.java
│   │   │       │   └── mapper/
│   │   │       │       └── IOrderStatusAuditMapper.java
│   │   │       ├── domain/
│   │   │       │   ├── api/
│   │   │       │   │   └── IOrderStatusAuditServicePort.java
│   │   │       │   ├── exception/
│   │   │       │   │   └── DomainException.java
│   │   │       │   ├── model/
│   │   │       │   │   └── OrderStatusAuditModel.java
│   │   │       │   ├── spi/
│   │   │       │   │   └── IOrderStatusAuditPersistencePort.java
│   │   │       │   └── usecase/
│   │   │       │       └── OrderStatusAuditUseCase.java
│   │   │       └── infrastructure/
│   │   │           ├── configuration/
│   │   │           │   └── BeanConfiguration.java
│   │   │           ├── exception/
│   │   │           │   └── NoDataFoundException.java
│   │   │           ├── exceptionhandler/
│   │   │           │   ├── ControllerAdvisor.java
│   │   │           │   └── ExceptionResponse.java
│   │   │           ├── input/
│   │   │           │   └── rest/
│   │   │           │       └── OrderStatusAuditController.java
│   │   │           └── out/
│   │   │               └── mongodb/
│   │   │                   ├── adapter/
│   │   │                   │   └── OrderStatusAuditMongoAdapter.java
│   │   │                   ├── document/
│   │   │                   │   └── OrderStatusAuditDocument.java
│   │   │                   ├── mapper/
│   │   │                   │   └── IOrderStatusAuditDocumentMapper.java
│   │   │                   └── repository/
│   │   │                       └── IOrderStatusAuditRepository.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── static/
│   │           └── open-api.yaml
│   └── test/
│       └── java/
│           └── com/pragma/powerup/
│               └── PowerUpApplicationTests.java
├── build.gradle
└── README.md
```

## Integración con otros microservicios

Este microservicio está diseñado para ser llamado desde otros microservicios (como el de pedidos) cada vez que ocurra un cambio de estado. No requiere autenticación JWT ya que es un servicio interno.

### Ejemplo de integración

```java
// Desde el microservicio de pedidos
OrderStatusAuditRequest auditRequest = OrderStatusAuditRequest.builder()
    .orderId(order.getId())
    .restaurantId(order.getRestaurantId())
    .clientId(order.getClientId())
    .previousStatus(order.getPreviousStatus())
    .newStatus(order.getCurrentStatus())
    .changedByUserId(currentUser.getId())
    .changedByRole(currentUser.getRole())
    .actionType("STATUS_CHANGE")
    .build();

restTemplate.postForObject(
    "http://localhost:8083/audit/order-status",
    auditRequest,
    OrderStatusAuditDataResponse.class
);
```

## Autor

Pragma PowerUp Team

## Licencia

Este proyecto es parte del programa PowerUp de Pragma.

