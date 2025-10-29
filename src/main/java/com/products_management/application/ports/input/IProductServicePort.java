package com.products_management.application.ports.input;

import java.util.List;

import com.products_management.domain.model.Product;

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
   * Obtiene una lista de todos los productos asociados a una empresa.
   *
   * @param enterpriseId el ID de la empresa.
   * @return una lista de todos los productos de la empresa.
   */
  List<Product> findAll(String enterpriseId);

  /**
   * Obtiene una lista de todos los productos activados asociados a una empresa.
   *
   * @param enterpriseId el ID de la empresa.
   * @return una lista de todos los productos activados de la empresa.
   */
  List<Product> findActivated(String enterpriseId);

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
