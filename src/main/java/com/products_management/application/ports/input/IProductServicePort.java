package com.products_management.application.ports.input;

import java.util.List;
import java.util.Optional;

import com.products_management.domain.model.Product;

import org.springframework.data.domain.Page;

/**
 * Interfaz que define los puertos de entrada para el servicio de productos.
 * Los puertos de entrada representan las operaciones que pueden ser realizadas
 * sobre los productos en la aplicación.
 */
public interface IProductServicePort {

  /**
   * Busca un producto por su ID y empresa.
   *
   * @param id el ID del producto a buscar.
   * @param enterpriseId el ID de la empresa.
   * @return el producto encontrado.
   */
  Product findById(Long id, String enterpriseId);

  /**
   * Obtiene una página de productos asociados a una empresa con filtros de búsqueda y paginación.
   *
   * @param enterpriseId el ID de la empresa.
   * @param search el término de búsqueda (opcional).
   * @param pageNumber el número de página.
   * @param pageSize el tamaño de página.
   * @param sortField el campo de ordenamiento.
   * @param sortOrder el orden (asc/desc).
   * @return una página de productos.
   */
  Page<Product> findAllWithFilters(String enterpriseId, String search, int pageNumber, int pageSize, String sortField, String sortOrder);

  /**
   * Obtiene una página paginada de productos asociados a una empresa con filtros opcionales.
   *
   * @param enterpriseId el ID de la empresa.
   * @param numPage el número de página (opcional).
   * @param size el tamaño de página (opcional).
   * @param sortField el campo de ordenamiento.
   * @param sortOrder el orden (asc/desc).
   * @param search el término de búsqueda (opcional).
   * @return una página de productos.
   */
  Page<Product> findAllPaginated(String enterpriseId, Optional<Integer> numPage, Optional<Integer> size, String sortField, String sortOrder, Optional<String> search);

  /**
   * Cuenta productos por ID de empresa con filtros de búsqueda.
   *
   * @param enterpriseId el ID de la empresa.
   * @param search el término de búsqueda (opcional).
   * @return el número de productos que coinciden.
   */
  long countByEnterpriseIdWithFilters(String enterpriseId, String search);

  /**
   * Cuenta todos los productos por ID de empresa.
   *
   * @param enterpriseId el ID de la empresa.
   * @return el número total de productos.
   */
  long countByEnterpriseId(String enterpriseId);

  /**
   * Cuenta productos activos por ID de empresa.
   *
   * @param enterpriseId el ID de la empresa.
   * @return el número de productos activos.
   */
  long countActivatedByEnterpriseId(String enterpriseId);

  /**
   * Obtiene una página de productos activados asociados a una empresa con paginación.
   *
   * @param enterpriseId el ID de la empresa.
   * @param pageNumber el número de página.
   * @param pageSize el tamaño de página.
   * @return una página de productos activados.
   */
  Page<Product> findActivatedWithPagination(String enterpriseId, int pageNumber, int pageSize);

  /**
   * Obtiene una página paginada de productos activados asociados a una empresa.
   *
   * @param enterpriseId el ID de la empresa.
   * @param numPage el número de página (opcional).
   * @param size el tamaño de página (opcional).
   * @return una página de productos activados.
   */
  Page<Product> findActivatedPaginated(String enterpriseId, Optional<Integer> numPage, Optional<Integer> size);

  /**
   * @brief Crea un nuevo producto.
   * @param product el producto a crear.
   * @return el producto creado.
   */
  Product create(Product product);

  /**
   * @brief Actualiza un producto existente.
   * @param id el ID del producto a actualizar.
   * @param product los datos del producto actualizado.
   * @param enterpriseId el ID de la empresa.
   * @return el producto actualizado.
   */
  Product update(Long id, Product product, String enterpriseId);

  /**
   * @brief Elimina un producto por su ID.
   *
   * @param id el ID del producto a eliminar.
   * @param enterpriseId el ID de la empresa.
   */
  void deleteById(Long id, String enterpriseId);

  /**
   * Cambia el estado de un producto (por ejemplo, activado/desactivado).
   *
   * @param id el ID del producto cuyo estado se va a cambiar.
   * @param enterpriseId el ID de la empresa.
   */
  void changeState(Long id, String enterpriseId);

  /**
   * Obtiene una lista de todos los productos asociados a una categoría.
   *
   * @param categoryId el ID de la categoría.
   * @return una lista de todos los productos de la categoría.
   */
  List<Product> findAllByCategory(Long categoryId);

  /**
   * Obtiene una lista de todos los productos asociados a una unidad de medida.
   *
   * @param unitOfMeasureId el ID de la unidad de medida.
   * @return una lista de todos los productos de la unidad de medida.
   */
  List<Product> findAllByUnitOfMeasure(Long unitOfMeasureId);

  /**
   * Obtiene una lista de todos los productos asociados a un tipo de producto.
   *
   * @param productTypeId el ID del tipo de producto.
   * @return una lista de todos los productos del tipo de producto.
   */
  List<Product> findAllByProductType(Long productTypeId);
}
