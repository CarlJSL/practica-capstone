package com.tienda.repository;

import com.tienda.model.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoRepository {

    void guardar(Producto producto);

    Optional<Producto> buscarPorId(String id);

    List<Producto> obtenerTodos();

    List<Producto> obtenerPorCategoria(String categoria);

    boolean eliminar(String id);

    boolean actualizar(Producto producto);

    java.util.Set<String> obtenerCategorias();

    boolean existe(String id);
}
