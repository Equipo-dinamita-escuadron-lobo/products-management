# Documentación de Pruebas de Integración - PRODUCTOS

Esta documentación detalla la colección de pruebas de Postman para el microservicio de Gestión de Productos. La colección cubre el ciclo de vida completo de productos, incluyendo la creación de entidades auxiliares (Unidades de Medida, Tipos de Producto, Categorías), gestión de productos (CRUD), importación/exportación masiva y limpieza de datos.

## Variables de Entorno

La colección utiliza las siguientes variables de entorno:

| Variable | Descripción |
|----------|-------------|
| `baseUrl` | URL base del API Gateway o microservicio |
| `enterpriseId` | Identificador de la empresa para las pruebas |
| `username` | Usuario para autenticación |
| `password` | Contraseña para autenticación |
| `tokenKeycloak` | Token JWT generado automáticamente (no establecer manualmente) |
| `UnitMeasureId` | ID de la unidad de medida creada (automático) |
| `ProductTypeId` | ID del tipo de producto creado (automático) |
| `CategoryId` | ID de la categoría creada (automático) |
| `ProductId` | ID del producto creado (automático) |
| `importJobId` | ID del trabajo de importación (automático) |
| `exportJobId` | ID del trabajo de exportación (automático) |

## Estructura de la Colección

### 1. Token

Esta carpeta contiene la solicitud para obtener el token de autenticación necesario para todas las demás peticiones.

#### Generar Token
- **Método**: `POST`
- **URL**: `{{baseUrl}}/auth/realms/contapp/protocol/openid-connect/token`
- **Descripción**: Obtiene un token de acceso de Keycloak utilizando las credenciales configuradas.
- **Tests**:
  - Verifica que el código de estado sea 200.
  - Extrae el `access_token` y lo guarda en la variable de entorno `tokenKeycloak`.

---

### 2. Init (Inicialización)

Configuración inicial de datos necesarios para las pruebas de productos.

#### 2.1 Casos Correctos

Pruebas de creación exitosa de entidades auxiliares.

##### 2.1.1 Unidades de Medida
- **Crear Unidad de Medida**
  - **Método**: `POST`
  - **URL**: `{{baseUrl}}/api/unit-measures/create`
  - **Body**: JSON con `name`, `abbreviation`, `enterpriseId`.
  - **Tests**: Verifica creación (201), guarda `UnitMeasureId`.

##### 2.1.2 Tipos de Producto
- **Crear Tipo de Producto**
  - **Método**: `POST`
  - **URL**: `{{baseUrl}}/api/product-types`
  - **Body**: JSON con `name`, `description`, `enterpriseId`, `category`.
  - **Tests**: Verifica creación (201), guarda `ProductTypeId`.

##### 2.1.3 Categorías
- **Crear Categoría**
  - **Método**: `POST`
  - **URL**: `{{baseUrl}}/api/categories/create`
  - **Body**: JSON con `name`, `description`, `enterpriseId`, `inventoryId`, etc.
  - **Tests**: Verifica creación (201), guarda `CategoryId`.

#### 2.2 Casos Incorrectos

Validación de errores y restricciones para entidades auxiliares.

##### 2.2.1 Unidades de Medida
- **Validaciones de Creación**:
  - Name vacío/null/duplicado.
  - Abbreviation vacío/null.
  - EnterpriseId vacío.
  - Body vacío.
- **Validaciones de Actualización**:
  - Campos requeridos vacíos.
  - ID inexistente.
- **Validaciones de Consulta**:
  - Listar por ID inexistente.
  - Listar con parámetros inválidos (paginación negativa).

##### 2.2.2 Tipos de Producto
- **Validaciones de Creación**:
  - Name vacío/null/duplicado.
  - EnterpriseId vacío.
  - Category inválida.
- **Validaciones de Actualización**:
  - Campos vacíos.
  - ID inexistente.
- **Validaciones de Cambio de Estado**:
  - ID inexistente.

##### 2.2.3 Categorías
- **Validaciones de Creación**:
  - Name vacío/null/duplicado.
  - Description vacía.
  - EnterpriseId vacío.
  - InventoryId null.
- **Validaciones de Actualización**:
  - Campos vacíos.
  - ID inexistente.
- **Validaciones de Consulta**:
  - Listar con parámetros inválidos.

---

### 3. Integration (Productos)

Pruebas principales del flujo de gestión de productos.

#### 3.1 Casos Correctos Productos

Flujo feliz de gestión de productos.

1. **Crear Producto**
   - **Método**: `POST`
   - **URL**: `{{baseUrl}}/api/products/create`
   - **Body**:
     ```json
     {
         "name": "Laptop Dell Inspiron",
         "description": "Laptop Dell Inspiron 15 3000 Series",
         "quantity": 10,
         "unitOfMeasureId": {{UnitMeasureId}},
         "categoryId": {{CategoryId}},
         "enterpriseId": "{{enterpriseId}}",
         "cost": 1500000.0,
         "reference": "DELL-INS-15-3000",
         "presentation": "Caja individual",
         "productTypeId": {{ProductTypeId}}
     }
     ```
   - **Tests**:
     ```javascript
     const responseJson = pm.response.json();
     pm.test("El código de estado es 201 (Created)", function () {
         pm.response.to.have.status(201);
     });
     pm.test("La respuesta es JSON válido", function () {
         pm.response.to.be.json;
     });
     pm.test("La respuesta contiene el id del producto", function () {
         pm.expect(responseJson.id).to.be.a('number');
         pm.environment.set('ProductId', responseJson.id);
     });
     pm.test("El nombre del producto es correcto (normalizado a mayúsculas)", function () {
         pm.expect(responseJson.name).to.eql('LAPTOP DELL INSPIRON');
     });
     ```

2. **Actualizar Producto**
   - **Método**: `PUT`
   - **URL**: `{{baseUrl}}/api/products/update/{{ProductId}}`
   - **Body**: Datos actualizados (cantidad, precio, descripción).
   - **Tests**:
     ```javascript
     const responseJson = pm.response.json();
     pm.test("El código de estado es 200 (OK)", function () {
         pm.response.to.have.status(200);
     });
     pm.test("El id del producto coincide", function () {
         pm.expect(responseJson.id).to.eql(parseInt(pm.environment.get('ProductId')));
     });
     pm.test("El nombre actualizado es correcto (normalizado a mayúsculas)", function () {
         pm.expect(responseJson.name).to.eql('LAPTOP DELL INSPIRON ACTUALIZADA');
     });
     ```

3. **Obtener Producto por ID**
   - **Método**: `GET`
   - **URL**: `{{baseUrl}}/api/products/findById/{{ProductId}}/{{enterpriseId}}`
   - **Tests**:
     ```javascript
     const responseJson = pm.response.json();
     pm.test("El código de estado es 200 (OK)", function () {
         pm.response.to.have.status(200);
     });
     pm.test("La respuesta contiene todos los campos esperados", function () {
         pm.expect(responseJson).to.have.property('id');
         pm.expect(responseJson).to.have.property('code');
         pm.expect(responseJson).to.have.property('name');
         pm.expect(responseJson).to.have.property('description');
         pm.expect(responseJson).to.have.property('quantity');
         // ... otros campos
     });
     ```

4. **Listar Todos los Productos**
   - **Método**: `GET`
   - **URL**: `{{baseUrl}}/api/products/findAll`
   - **Parámetros**: `enterpriseId`, `numPage`, `size`.
   - **Tests**:
     ```javascript
     const responseJson = pm.response.json();
     pm.test("El código de estado es 200 (OK)", function () {
         pm.response.to.have.status(200);
     });
     pm.test("La respuesta tiene estructura paginada", function () {
         pm.expect(responseJson).to.have.property('content');
         pm.expect(responseJson).to.have.property('page');
     });
     pm.test("Hay al menos un producto en la lista", function () {
         pm.expect(responseJson.content.length).to.be.at.least(1);
     });
     ```

5. **Listar Productos Activos**
   - **Método**: `GET`
   - **URL**: `{{baseUrl}}/api/products/findActivate`
   - **Tests**:
     ```javascript
     const responseJson = pm.response.json();
     pm.test("El código de estado es 200 (OK)", function () {
         pm.response.to.have.status(200);
     });
     pm.test("Todos los productos están activos", function () {
         responseJson.content.forEach(function(product) {
             pm.expect(product.state).to.be.true;
         });
     });
     ```

6. **Cambiar Estado Producto**
   - **Método**: `PUT`
   - **URL**: `{{baseUrl}}/api/products/changeState/{{ProductId}}/{{enterpriseId}}`
   - **Tests**:
     ```javascript
     pm.test("El código de estado es 200 (OK)", function () {
         pm.response.to.have.status(200);
     });
     ```

7. **Importación Masiva (Excel)**
   - **Importar Productos**: `POST` a `/import/excel`. Envía archivo.
     - **Tests**:
       ```javascript
       const responseJson = pm.response.json();
       pm.test("El código de estado es 202 (Accepted)", function () {
           pm.response.to.have.status(202);
       });
       pm.test("La respuesta contiene jobId", function () {
           pm.expect(responseJson).to.have.property('jobId');
           pm.environment.set('importJobId', responseJson.jobId);
       });
       ```
   - **Consultar Estado**: `GET` a `/import/status/{{jobId}}`.
     - **Tests**:
       ```javascript
       const responseJson = pm.response.json();
       pm.test("El código de estado es 200 (OK)", function () {
           pm.response.to.have.status(200);
       });
       pm.test("La respuesta contiene status válido", function () {
           const validStatuses = ['PENDING', 'PROCESSING', 'COMPLETED', 'FAILED'];
           pm.expect(validStatuses).to.include(responseJson.status);
       });
       ```

8. **Exportación Masiva (Excel)**
   - **Iniciar Exportación**: `GET` a `/export/excel`.
     - **Tests**:
       ```javascript
       const responseJson = pm.response.json();
       pm.test("El código de estado es 202 (Accepted)", function () {
           pm.response.to.have.status(202);
       });
       pm.test("La respuesta contiene jobId", function () {
           pm.expect(responseJson).to.have.property('jobId');
           pm.environment.set('exportJobId', responseJson.jobId);
       });
       ```
   - **Consultar Estado**: `GET` a `/export/status/{{jobId}}`.
     - **Tests**:
       ```javascript
       const responseJson = pm.response.json();
       pm.test("El código de estado es 200 (OK)", function () {
           pm.response.to.have.status(200);
       });
       pm.test("La respuesta contiene progress", function () {
           pm.expect(responseJson).to.have.property('progress');
       });
       ```
   - **Descargar Archivo**: `GET` a `/export/download/{{jobId}}`.
     - **Tests**:
       ```javascript
       if (pm.response.code === 200) {
           pm.test("La respuesta contiene el archivo Excel", function () {
               const contentType = pm.response.headers.get('Content-Type');
               pm.expect(contentType).to.include('application/vnd.openxmlformats-officedocument.spreadsheetml.sheet');
           });
       }
       ```
   - **Exportar Plantilla**: `GET` a `/template/excel`.
     - **Tests**:
       ```javascript
       pm.test("El código de estado es 200 (OK)", function () {
           pm.response.to.have.status(200);
       });
       pm.test("La respuesta contiene el archivo Excel", function () {
           const contentType = pm.response.headers.get('Content-Type');
           pm.expect(contentType).to.include('application/vnd.openxmlformats-officedocument.spreadsheetml.sheet');
       });
       ```

#### 3.2 Casos Incorrectos Productos

Pruebas exhaustivas de validación y manejo de errores.

##### 3.2.1 Crear Producto (Validaciones)
- **Campos Requeridos**: Tests para `name`, `description`, `enterpriseId`, `unitOfMeasureId`, `categoryId`, `productTypeId`, `reference`, `presentation` cuando son null o vacíos.
- **Referencias Inválidas**: Tests con IDs inexistentes para `unitOfMeasureId`, `categoryId`, `productTypeId`.
- **Duplicados**: Test de creación con `name` ya existente (Status 409).
- **Body Vacío**: Status 400.

##### 3.2.2 Actualizar Producto (Validaciones)
- **Campos Requeridos**: Similar a creación.
- **IDs Inexistentes**: Actualizar producto con ID no válido (Status 404).
- **Referencias Rotas**: Actualizar con IDs de entidades relacionadas inexistentes.

##### 3.2.3 Obtener por ID (Validaciones)
- ID de producto inexistente (404).
- EnterpriseId vacío o inexistente (404).

##### 3.2.4 Listar Todos (Validaciones)
- EnterpriseId vacío/inexistente.
- Paginación inválida (`numPage` < 0, `size` < 0).

##### 3.2.5 Listar Activos (Validaciones)
- EnterpriseId vacío/inexistente.
- Paginación inválida.

##### 3.2.6 Cambiar Estado (Validaciones)
- ID producto inexistente.
- EnterpriseId vacío/inexistente.

##### 3.2.7 - 3.2.12 Importación/Exportación (Validaciones)
- **Importar**: Sin archivo, entId vacío/inexistente.
- **Consultar Estado**: JobId inexistente/vacío/inválido.
- **Descargar**: JobId inexistente.
- **Plantilla**: EntId vacío.

---

### 4. Tear Down (Limpieza)

Proceso de eliminación de datos creados durante las pruebas para mantener el entorno limpio.

1. **Listar Productos para Eliminar**
   - Busca todos los productos creados.
   - Almacena IDs en variable de entorno.

2. **Eliminar Producto en Bucle**
   - Itera sobre la lista de IDs y elimina cada producto.
   - **Método**: `DELETE`
   - **URL**: `{{baseUrl}}/api/products/delete/{{id}}/{{enterpriseId}}`

3. **Eliminar Entidades Auxiliares**
   - **Eliminar Unidad**: `DELETE` `{{baseUrl}}/api/unit-measures/delete/{{UnitMeasureId}}`
   - **Eliminar Tipo Producto**: `DELETE` `{{baseUrl}}/api/product-types/{{ProductTypeId}}`
   - **Eliminar Categoría**: `DELETE` `{{baseUrl}}/api/categories/delete/{{enterpriseId}}/{{CategoryId}}`
