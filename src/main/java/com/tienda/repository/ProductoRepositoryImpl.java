package com.tienda.repository;

import com.tienda.model.Producto;
import java.util.*;
import java.util.stream.Collectors;

public class ProductoRepositoryImpl implements ProductoRepository {

    private final Map<String, Producto> productos;
    private final Set<String> categorias;

    public ProductoRepositoryImpl() {
        this.productos = new HashMap<>();
        this.categorias = new HashSet<>();
    }

    @Override
    public void guardar(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser null");
        }
        productos.put(producto.getId(), producto);
        categorias.add(producto.getCategoria());
    }

    @Override
    public Optional<Producto> buscarPorId(String id) {
        return Optional.ofNullable(productos.get(id));
    }

    @Override
    public List<Producto> obtenerTodos() {
        return new ArrayList<>(productos.values());
    }

    @Override
    public List<Producto> obtenerPorCategoria(String categoria) {
        return productos.values().stream()
                .filter(p -> p.getCategoria().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
    }

    @Override
    public boolean eliminar(String id) {
        Producto eliminado = productos.remove(id);
        if (eliminado != null) {
            actualizarCategorias();
            return true;
        }
        return false;
    }

    @Override
    public boolean actualizar(Producto producto) {
        if (producto == null || !productos.containsKey(producto.getId())) {
            return false;
        }
        productos.put(producto.getId(), producto);
        categorias.add(producto.getCategoria());
        return true;
    }

    @Override
    public Set<String> obtenerCategorias() {
        return new HashSet<>(categorias);
    }

    @Override
    public boolean existe(String id) {
        return productos.containsKey(id);
    }

    private void actualizarCategorias() {
        categorias.clear();
        productos.values().forEach(p -> categorias.add(p.getCategoria()));
    }

    public int cantidadProductos() {
        return productos.size();
    }
}
