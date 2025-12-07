<br />
<div align="center">
<h3 align="center">PRAGMA POWER-UP - TRACEABILITY AND AUDIT MICROSERVICE</h3>
  <p align="center">
    Microservicio de trazabilidad y auditoría para el sistema de plazoleta de comidas. Registra y consulta el historial de cambios de estado de pedidos para auditoría y análisis de eficiencia.
  </p>
</div>

### Built With

* ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
* ![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
* ![MongoDB](https://img.shields.io/badge/MongoDB-47A248?style=for-the-badge&logo=mongodb&logoColor=white)
* ![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)

## Descripción General

Este microservicio es responsable de:

- **Registro de auditorías**: Almacena cada cambio de estado de pedidos con información detallada (timestamp, usuario responsable, estado anterior/nuevo)
- **Consulta de histórico**: Permite consultar el historial de cambios con filtros opcionales por cliente, pedido o tipo de acción
- **Seguridad**: Los clientes solo pueden consultar el historial de sus propios pedidos
- **Análisis de eficiencia**: Provee datos para calcular tiempos promedio de procesamiento de pedidos

**Puerto:** 8083  
**Base de datos:** MongoDB (traceability_audit)  
**Arquitectura:** Hexagonal (Puertos y Adaptadores)

### Arquitectura

El proyecto sigue **Arquitectura Hexagonal (Puertos y Adaptadores)**:

```
src/
├── domain/              # Lógica de negocio pura
│   ├── model/          # Modelos de dominio
│   ├── usecase/        # Casos de uso
│   ├── api/            # Puertos de entrada
│   └── spi/            # Puertos de salida
├── application/         # Capa de aplicación
│   ├── handler/        # Handlers
│   └── mapper/         # Mappers (MapStruct)
└── infrastructure/      # Adaptadores
    ├── input/rest/     # Controladores REST
    ├── out/mongodb/    # Adaptador MongoDB
    └── configuration/  # Configuración de beans
```

---

## Endpoints Implementados

### `POST /audit/order-status`

Registra una auditoría de cambio de estado de pedido.

> **Nota:** Este endpoint es llamado internamente por el microservicio `foodcourt` cada vez que ocurre un cambio de estado en un pedido.

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

**Response (201 Created):**
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
    "changedAt": "2025-12-07T10:30:00",
    "actionType": "STATUS_CHANGE",
    "employeeId": 7,
    "ipAddress": "192.168.1.100",
    "userAgent": "Mozilla/5.0",
    "notes": "Pedido asignado al empleado",
    "timeInPreviousStatusMinutes": 15
  }
}
```

---

### `GET /audit/order-status`

Consulta el historial de auditorías con filtros opcionales y paginación.

**Query Parameters:**
- `clientId`: ID del cliente (obligatorio para clientes, opcional para otros roles)
- `orderId`: ID del pedido (opcional)
- `actionTypes`: Lista de tipos de acción (filtro múltiple, opcional)
- `page`: Número de página (default: 0)
- `size`: Tamaño de página (default: 10)

**Ejemplo de consulta:**
```
GET /audit/order-status?clientId=10&actionTypes=STATUS_CHANGE&actionTypes=ASSIGNMENT&page=0&size=10
```

**Response (200 OK):**
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
      "changedAt": "2025-12-07T10:30:00",
      "actionType": "STATUS_CHANGE",
      "employeeId": 7,
      "ipAddress": "192.168.1.100",
      "userAgent": "Mozilla/5.0",
      "notes": "Pedido asignado al empleado",
      "timeInPreviousStatusMinutes": 15
    },
    {
      "id": "507f1f77bcf86cd799439012",
      "orderId": 1,
      "restaurantId": 5,
      "clientId": 10,
      "previousStatus": "EN_PREPARACION",
      "newStatus": "LISTO",
      "changedByUserId": 7,
      "changedByRole": "EMPLEADO",
      "changedAt": "2025-12-07T10:50:00",
      "actionType": "STATUS_CHANGE",
      "employeeId": 7,
      "timeInPreviousStatusMinutes": 20
    }
  ],
  "meta": {
    "page": 0,
    "size": 10,
    "totalElements": 2,
    "totalPages": 1
  }
}
```

---

## Cómo Ejecutar Localmente

### 1. Prerequisitos

- ✅ JDK 17
- ✅ Gradle
- ✅ MongoDB 4.4+

### Herramientas Recomendadas

- IntelliJ IDEA
- MongoDB Compass (GUI para gestión de MongoDB)
- Postman

### 2. Instalación

1. **Clonar el repositorio**
   ```bash
   git clone <repository-url>
   cd trazability-audit
   ```

2**Configurar conexión a MongoDB**
   
   Editar `src/main/resources/application-dev.yml`:
   ```yaml
   spring:
     data:
       mongodb:
         uri: mongodb://localhost:27017/traceability_audit
   ```

### 3. Compilar el Proyecto

```bash
./gradlew clean build
```

### 4. Ejecutar la Aplicación

**Opción 1: Desde terminal**
```bash
./gradlew bootRun
```

**Opción 2: Desde IntelliJ IDEA**
- Right-click `PowerUpApplication.java` → Run

### 5. Verificar MongoDB

**Usando MongoDB Compass:**
1. Conectar a `mongodb://localhost:27017`
2. Verificar que la base de datos `traceability_audit` se haya creado
3. Verificar la colección `order_status_audit`

---

## Cómo Correr las Pruebas

### Ejecutar todas las pruebas con cobertura

```bash
./gradlew test jacocoTestReport
```

### Ver reportes

```bash
# Reporte de tests
start build/reports/tests/test/index.html

# Reporte de cobertura
start build/reports/jacoco/test/html/index.html
```

### Ejecutar tests específicos

```bash
# OrderStatusAuditUseCaseTest (HU-17)
./gradlew test --tests "OrderStatusAuditUseCaseTest"
```

### Cobertura de Historias de Usuario

Este microservicio cubre **1 Historia de Usuario** con más de **15 pruebas unitarias**:

| Historia | Clase de Test | Pruebas |
|----------|---------------|---------|
| HU-17: Consultar trazabilidad | `OrderStatusAuditUseCaseTest` | ✅ Registro de auditoría<br>✅ Consulta por clientId<br>✅ Consulta por orderId<br>✅ Filtros múltiples<br>✅ Paginación<br>✅ Ordenamiento por fecha |

---

## Índices de MongoDB

Los siguientes índices se crean automáticamente gracias a las anotaciones `@Indexed` en el documento:

- `order_id`: Para búsquedas por pedido
- `client_id`: Para búsquedas por cliente
- `changed_at`: Para ordenamiento temporal
- `action_type`: Para filtros por tipo de acción

Esto garantiza consultas rápidas incluso con grandes volúmenes de datos.

---

## Integración con otros Microservicios

Este microservicio está diseñado para ser llamado desde el microservicio [`foodcourt`](https://github.com/Barcodehub/foodcourt) cada vez que ocurra un cambio de estado en un pedido.

---

## Autor

**Brayan Barco**

## Licencia

Este proyecto es parte de la prueba tecnica de Pragma.

