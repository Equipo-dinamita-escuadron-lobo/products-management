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

### Capa de Aplicación
```
application/
├── ports/
│   ├── input/      # Interfaces de casos de uso (Use Cases)
│   └── output/     # Interfaces de infraestructura
├── service/        # Implementación de lógica de negocio
├── dto/           # Objetos de transferencia de datos
```

### Capa de Infraestructura
```
infraestructure/
├── input/
│   ├── rest/      # Controladores REST y DTOs
│   └── validation/# Validaciones de entrada
├── output/
│   ├── persistence/  # Repositorios JPA
│   ├── messageBroker/# Publicadores de eventos
│   └── multitenancy/ # Configuración multi-tenant
├── config/        # Configuraciones Spring
├── security/      # Seguridad y autenticación
└── utils/         # Utilidades de infraestructura
```

## 🛠️ Tecnologías

### Framework & Runtime
- **Java 17**: Lenguaje de programación
- **Spring Boot 3.4.7**: Framework principal
- **Spring Cloud 2024.0.1**: Microservicios y nube

### Persistencia
- **Spring Data JPA**: Abstracción de datos
- **PostgreSQL**: Base de datos relacional
- **H2 Database**: Base de datos para testing

### Mensajería
- **RabbitMQ**: Message broker para eventos
- **Spring AMQP**: Cliente RabbitMQ

### Seguridad
- **Spring Security**: Framework de seguridad
- **OAuth2/OpenID Connect**: Protocolo de autenticación
- **JWT**: Tokens de acceso

### Documentación
- **SpringDoc**: Integración Swagger con Spring Boot

### Testing
- **JUnit 5**: Framework de testing
- **Mockito**: Mocks para testing
- **JaCoCo**: Cobertura de código

### DevOps
- **Docker**: Contenedorización
- **Docker Compose**: Orquestación local
- **Maven**: Gestión de dependencias y build
- **GitHub Actions**: CI/CD

### Utilidades
- **MapStruct**: Mapeo objeto-objeto
- **Apache POI**: Procesamiento de archivos Excel
- **Lombok**: Reducción de boilerplate code
- **Eureka Client**: Service discovery

## 📋 Requisitos Previos

### Sistema
- **Java**: JDK 17 o superior
- **Maven**: 3.6+ (viene incluido el wrapper `mvnw`)
- **Docker**: 20.10+ (opcional, para contenedorización)
- **Docker Compose**: 2.0+ (opcional, para orquestación)

### Servicios Externos
- **PostgreSQL**: 15+ (base de datos)
- **RabbitMQ**: 3.12+ (message broker)
- **Keycloak**: 20+ (proveedor de identidad)
- **Eureka Server**: Para service discovery

## ⚙️ Instalación y Configuración

### 1. Clonación del Repositorio
```bash
git clone <repository-url>
cd products-management
```

### 2. Configuración del Entorno

#### Variables de Entorno Requeridas
```bash
# Base de datos
DB_URL=jdbc:postgresql://localhost:5432/products
DB_USER=postgres
DB_PASSWORD=your_password
DB_DRIVER=org.postgresql.Driver

# RabbitMQ
RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_USER=guest
RABBITMQ_PASSWORD=guest

# Seguridad (Keycloak)
JWT_ISSUER_URI=http://localhost:8090/auth/realms/oauth2-realm
JWT_JWK_SET_URI=http://localhost:8090/auth/realms/oauth2-realm/protocol/openid-connect/certs
JWT_PRINCIPAL_ATTR=preferred_username
JWT_RESOURCE_ID=microservices_client

# Eureka
EUREKA_URL=http://localhost:8761/eureka/

# Aplicación
PORT=8080
PROFILE=dev
```

### 3. Configuración de Base de Datos

El esquema se crea automáticamente con Hibernate (`ddl-auto: create-drop` en desarrollo).

#### Tablas Principales
- `product`: Productos
- `category`: Categorías
- `product_type`: Tipos de producto
- `unit_of_measure`: Unidades de medida
- `import_job_status`: Estado de jobs de importación
- `export_job_status`: Estado de jobs de exportación

### 4. Configuración de RabbitMQ

#### Exchanges y Queues
- **Exchange**: `products.exchange`
- **Queues**:
  - `products.sync.queue`: Sincronización de productos
  - `products.usage.queue`: Actualización de uso de productos

## 🚀 Ejecución

### Desarrollo Local

#### Opción 1: Maven
```bash
# Compilar y ejecutar
./mvnw spring-boot:run

# Ejecutar tests
./mvnw test

# Build del proyecto
./mvnw clean package
```

#### Opción 2: Docker Compose
```bash
# Ejecutar con servicios locales
docker-compose up -d

# Ver logs
docker-compose logs -f product_management
```

### Producción

#### Docker
```bash
# Build de imagen
docker build -t products-management .

# Ejecutar contenedor
docker run -p 8080:8080 \
  -e DB_URL=jdbc:postgresql://db:5432/products \
  -e DB_USER=postgres \
  -e DB_PASSWORD=password \
  products-management
```

## 📚 API Documentation


### OpenAPI Specification
```
http://localhost:8080/api-docs
```

### Health Checks
```
http://localhost:8080/actuator/health
```

### Endpoints Principales

#### Productos
- `GET /api/products/findAll` - Listar productos paginados
- `POST /api/products/create` - Crear producto
- `PUT /api/products/update/{id}` - Actualizar producto
- `DELETE /api/products/delete/{id}/{enterpriseId}` - Eliminar producto

#### Import/Export
- `GET /api/products/template/excel` - Descargar plantilla Excel
- `POST /api/products/import/excel` - Importar productos desde Excel
- `GET /api/products/export/excel` - Exportar productos a Excel
- `GET /api/products/import/status/{jobId}` - Estado de importación
- `GET /api/products/export/status/{jobId}` - Estado de exportación

#### Categorías
- `GET /api/categories/findAll` - Listar categorías
- `POST /api/categories/create` - Crear categoría
- `PUT /api/categories/update/{id}` - Actualizar categoría

## 🎯 Funcionalidades

### Gestión de Productos
- **Código único**: Generación automática basada en nombre y categoría
- **Contador de uso**: Tracking de utilización del producto
- **Referencias**: Identificadores alternativos
- **Presentaciones**: Diferentes formas de empaque

### Procesamiento de Excel
- **Validación en tiempo real**: Detección de errores durante importación
- **Deduplicación**: Identificación de productos duplicados
- **Reportes de error**: Detalles específicos de validaciones fallidas
- **Procesamiento por lotes**: Optimización para grandes volúmenes

### Eventos y Integración
- **Producto creado**: Notificación a sistemas externos
- **Producto actualizado**: Sincronización de cambios
- **Uso de producto**: Tracking de consumo

### Multi-tenancy
- **Aislamiento completo**: Datos separados por empresa
- **Configuración dinámica**: Contexto por enterpriseId
- **Seguridad**: Validación de permisos por tenant

## 🧪 Testing

### Cobertura de Código
```bash
# Ejecutar tests con cobertura
./mvnw clean test jacoco:report

# Ver reporte de cobertura
open target/site/jacoco/index.html
```

### Tests por Capa
- **Unitarios**: Servicios de dominio y aplicación
- **Integración**: Controladores REST y persistencia
- **Contratos**: Interfaces de puertos

### Colección Postman
Archivos de testing completos en `.postman/`:
- `productos - collection.json`: Suite completa de pruebas
- `productos - environment.json`: Variables de entorno

## 🔄 CI/CD

### GitHub Actions
- **Build automático**: En push a `develop`
- **Testing**: Ejecución completa de tests
- **Docker**: Build y push de imagen
- **Quality Gates**: Cobertura mínima requerida

### Workflows
- `on-push-to-dev.yaml`: Build para rama develop
- `on-pull-request-to-dev.yaml`: Validación de PRs

## 🤝 Contribución

### Estándares de Código
1. **Arquitectura Hexagonal**: Mantener separación de capas
2. **TDD**: Tests antes del código
3. **Code Coverage**: Mínimo 80% requerido
4. **Commits**: Mensajes descriptivos en español

### Proceso de Desarrollo
1. Crear rama desde `develop`
2. Implementar funcionalidad con tests
3. Pull Request con descripción detallada
4. Code Review y aprobación
5. Merge automático tras CI/CD exitoso

### Convenciones
- **Nombres**: En inglés para código, español para documentación
- **Commits**: Convención semántica
- **PRs**: Templates estructurados

---


**Nota**: Este microservicio forma parte del ecosistema de microservicios CONTAPP, diseñado para empresas contables que requieren gestión robusta de inventarios y productos.
