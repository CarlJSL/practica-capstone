package com.tienda.service;

import com.tienda.exception.StockInsuficienteException;
import com.tienda.model.ItemCarrito;
import com.tienda.model.Producto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class CarritoService {

    private final List<ItemCarrito> items;
    private final InventarioService inventarioService;

    public CarritoService(InventarioService inventarioService) {
        this.items = new ArrayList<>();
        this.inventarioService = inventarioService;
    }

    public void agregarProducto(String productoId, int cantidad) throws StockInsuficienteException {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        Optional<Producto> productoOpt = inventarioService.buscarProducto(productoId);

        if (productoOpt.isEmpty()) {
            throw new IllegalArgumentException("Producto no encontrado: " + productoId);
        }

        Producto producto = productoOpt.get();

        // Verificar stock disponible
        if (!inventarioService.hayStockSuficiente(productoId, cantidad)) {
            throw new StockInsuficienteException(
                    productoId,
                    producto.getStock(),
                    cantidad);
        }

        // Buscar si el producto ya está en el carrito
        Optional<ItemCarrito> itemExistente = buscarItemEnCarrito(productoId);

        if (itemExistente.isPresent()) {
            // Si ya existe, aumentar la cantidad
            ItemCarrito item = itemExistente.get();
            int nuevaCantidad = item.getCantidad() + cantidad;

            // Verificar que haya stock para la nueva cantidad total
            if (!inventarioService.hayStockSuficiente(productoId, nuevaCantidad)) {
                throw new StockInsuficienteException(
                        productoId,
                        producto.getStock(),
                        nuevaCantidad);
            }

            item.agregarCantidad(cantidad);
        } else {
            // Si no existe, crear nuevo item
            items.add(new ItemCarrito(producto, cantidad));
        }
    }

    private Optional<ItemCarrito> buscarItemEnCarrito(String productoId) {
        return items.stream()
                .filter(item -> item.getProducto().getId().equals(productoId))
                .findFirst();
    }

    public boolean eliminarProducto(String productoId) {
        return items.removeIf(item -> item.getProducto().getId().equals(productoId));
    }

    public List<ItemCarrito> obtenerItems() {
        return new ArrayList<>(items);
    }

    public double calcularTotal() {
        return items.stream()
                .mapToDouble(ItemCarrito::calcularSubtotal)
                .sum();
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }

    public void vaciar() {
        items.clear();
    }

    public int cantidadItems() {
        return items.size();
    }

    public int cantidadTotalProductos() {
        return items.stream()
                .mapToInt(ItemCarrito::getCantidad)
                .sum();
    }
}
