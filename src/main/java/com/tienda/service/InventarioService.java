package com.tienda.service;

import com.tienda.exception.StockInsuficienteException;
import com.tienda.model.Producto;
import com.tienda.repository.ProductoRepository;

import java.util.List;
import java.util.Optional;


public class InventarioService {

    private final ProductoRepository productoRepository;

    public InventarioService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public void agregarProducto(Producto producto) {
        productoRepository.guardar(producto);
    }

    public Optional<Producto> buscarProducto(String id) {
        return productoRepository.buscarPorId(id);
    }

    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.obtenerTodos();
    }

    public boolean hayStockSuficiente(String productoId, int cantidad) {
        Optional<Producto> producto = productoRepository.buscarPorId(productoId);
        return producto.isPresent() && producto.get().getStock() >= cantidad;
    }

    public void reducirStock(String productoId, int cantidad) throws StockInsuficienteException {
        Optional<Producto> productoOpt = productoRepository.buscarPorId(productoId);

        if (productoOpt.isEmpty()) {
            throw new IllegalArgumentException("Producto no encontrado: " + productoId);
        }

        Producto producto = productoOpt.get();

        if (producto.getStock() < cantidad) {
            throw new StockInsuficienteException(
                    productoId,
                    producto.getStock(),
                    cantidad);
        }

        producto.reducirStock(cantidad);
        productoRepository.actualizar(producto);
    }

    public void aumentarStock(String productoId, int cantidad) {
        Optional<Producto> productoOpt = productoRepository.buscarPorId(productoId);

        if (productoOpt.isEmpty()) {
            throw new IllegalArgumentException("Producto no encontrado: " + productoId);
        }

        Producto producto = productoOpt.get();
        producto.aumentarStock(cantidad);
        productoRepository.actualizar(producto);
    }

    public List<Producto> obtenerPorCategoria(String categoria) {
        return productoRepository.obtenerPorCategoria(categoria);
    }
}
