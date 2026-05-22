# Microservicio de Gestión de Productos (Products Management)

[![Java](https://img.shields.io/badge/Java-17-orange)](https://openjdk.java.net/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.7-brightgreen)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)](https://www.postgresql.org/)
[![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3.12-orange)](https://www.rabbitmq.com/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue)](https://www.docker.com/)

Un microservicio robusto y escalable para la gestión completa de productos, construido con arquitectura hexagonal (ports & adapters). Ofrece operaciones CRUD, importación/exportación masiva vía Excel, multi-tenancy, y integración con sistemas externos mediante mensajería asíncrona.

## 📋 Tabla de Contenidos

- [Características Principales](#-características-principales)
- [Arquitectura](#-arquitectura)
- [Tecnologías](#-tecnologías)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación y Configuración](#-instalación-y-configuración)
- [Ejecución](#-ejecución)
- [API Documentation](#-api-documentation)
- [Funcionalidades](#-funcionalidades)
- [Testing](#-testing)
- [CI/CD](#-cicd)
- [Contribución](#-contribución)
- [Licencia](#-licencia)

## 🚀 Características Principales

### Gestión Completa de Productos
- **CRUD completo**: Crear, leer, actualizar y eliminar productos
- **Búsqueda y filtrado**: Paginación, ordenamiento y búsqueda por texto
- **Estados**: Activación/desactivación de productos
- **Validaciones**: Reglas de negocio y constraints de integridad

### Entidades Relacionadas
- **Categorías**: Organización jerárquica de productos
- **Tipos de Producto**: Clasificación específica de productos
- **Unidades de Medida**: Sistema de medidas (Kg, L, Unidades, etc.)

### Importación/Exportación Masiva
- **Excel Processing**: Soporte para archivos .xlsx y .xls
- **Procesamiento Asíncrono**: Jobs en background para grandes volúmenes
- **Validación Robusta**: Detección de errores y reportes detallados
- **Templates**: Plantillas pre-configuradas para importación

### Arquitectura Empresarial
- **Multi-tenancy**: Aislamiento por empresa (enterpriseId)
- **Eventos Asíncronos**: Integración vía RabbitMQ
- **Service Discovery**: Registro automático en Eureka
- **Seguridad**: Autenticación JWT con Keycloak
- **Monitoreo**: Health checks y métricas con Actuator

## 🏗️ Arquitectura

El proyecto implementa **Arquitectura Hexagonal** (Ports & Adapters), separando claramente:

### Capas del Dominio
```
domain/
├── model/          # Entidades del dominio
├── enums/          # Enumeraciones y constantes
├── exception/      # Excepciones de negocio
├── utils/          # Utilidades del dominio
```

---

## Bounded context `copy` (Hito 3 — REQ-PRODUCTS-01 a REQ-PRODUCTS-05)

### Propósito

Participante de copia para el orquestador en `enterprises-management`.
Copia productos, categorías, tipos de producto y unidades de medida entre empresas (Fase 2).

### Registro Eureka

```properties
spring.application.name=PRODUCTS
eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka
```

El orquestador resuelve este servicio vía `lb://PRODUCTS`.

### Estructura de paquetes

```
com.products_management.copy/
├── domain/
│   ├── enums/       CopyEstado, CopyModulo
│   ├── models/      CopyJobLog, CopyEquivalencia
│   └── exceptions/  DuplicateCopyJobException, MissingEquivalenceException
├── application/
│   ├── input/       IExecuteProductsCopyPhasePort, IGetProductsCopyStatusPort, ...
│   ├── output/      ICopyJobLogRepositoryPort, IUnitOfMeasureSourceRepositoryPort, ...
│   └── services/    CopyProductsService, CategoryFkRemapper, CopyEquivalenceMapper, ...
└── infraestructure/
    ├── adapters/input/rest/controller/   CopyProductsController (4 endpoints)
    └── adapters/output/persistence/jpa/ CopyJobLogEntity, repos fuente/destino, adapters
```

Nota: usa el typo `infraestructure` (sin h) por convención del servicio.

### Orden de copia (topológico)

```
UnitOfMeasure → ProductType → Category (remap FKs cross-servicio) → Product
```

`CategoryFkRemapper` recibe `equivalenciasPrev` y remap FKs opcionales:
- `inventoryAccountId` → tabla `account` (CATALOGUE)
- `costAccountId` → tabla `account` (CATALOGUE)
- `saleAccountId` → tabla `account` (CATALOGUE)
- `returnAccountId` → tabla `account` (CATALOGUE)
- `taxId` → tabla `tax` (CATALOGUE)

FKs faltantes generan advertencia (no error) → `estado=COMPLETADO_CON_ADVERTENCIAS`.

### Multi-tenancy

- `Product` y `Category` usan `@TenantId` → Hibernate asigna automáticamente `enterpriseId` desde `TenantContext`.
- `UnitOfMeasure` y `ProductType` tienen `enterpriseId` manual → `entity.setEnterpriseId(entDestino)`.
- Bloque `try { TenantContext.setTenantId(entDestino); ... } finally { TenantContext.clear(); }` obligatorio.

### Endpoints disponibles

| Método | URL | Descripción |
|--------|-----|-------------|
| POST | `/api/products/copy/phase` | Ejecuta la fase de copia |
| GET | `/api/products/copy/status/{idProceso}` | Estado del proceso |
| POST | `/api/products/copy/cancel/{idProceso}` | Cancela el proceso |
| DELETE | `/api/products/copy/cleanup/{idProceso}` | Elimina logs del proceso |

### Idempotencia

Cada ejecución registra un `CopyJobLog` con `(id_proceso, fase, modulo)` como clave única.
Si el log ya existe, retorna el resultado previo sin re-ejecutar.

### DDL

No usa Flyway — el esquema se aplica via `ddl-auto=update` de Hibernate.
Las entidades existentes (Product, Category, UnitOfMeasure, ProductType) obtienen `created_at` como campo Java `@Builder.Default Instant.now()`.
`CopyJobLogEntity` es una nueva entidad JPA creada en Hito 3.

### Cómo correr las pruebas

```bash
./mvnw test
```

Los tests usan H2 en memoria con `MODE=MySQL`. 44 tests en total (30+ en el bounded context copy).

