package com.tienda.service;

import com.tienda.exception.StockInsuficienteException;
import com.tienda.model.ItemCarrito;
import com.tienda.model.Venta;

import java.util.ArrayList;
import java.util.List;

public class VentaService {

    private final List<Venta> historialVentas;
    private final InventarioService inventarioService;

    public VentaService(InventarioService inventarioService) {
        this.historialVentas = new ArrayList<>();
        this.inventarioService = inventarioService;
    }

    public Venta procesarVenta(List<ItemCarrito> items) throws StockInsuficienteException {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("No hay items para procesar");
        }

        // Validar stock antes de procesar
        for (ItemCarrito item : items) {
            String productoId = item.getProducto().getId();
            int cantidad = item.getCantidad();

            if (!inventarioService.hayStockSuficiente(productoId, cantidad)) {
                throw new StockInsuficienteException(
                        productoId,
                        item.getProducto().getStock(),
                        cantidad);
            }
        }

        // Reducir stock de todos los productos
        for (ItemCarrito item : items) {
            inventarioService.reducirStock(
                    item.getProducto().getId(),
                    item.getCantidad());
        }

        // Crear y registrar la venta
        Venta venta = new Venta(items);
        historialVentas.add(venta);

        return venta;
    }

    public List<Venta> obtenerHistorial() {
        return new ArrayList<>(historialVentas);
    }

    public int cantidadVentas() {
        return historialVentas.size();
    }

    public double calcularTotalIngresos() {
        return historialVentas.stream()
                .mapToDouble(Venta::getTotal)
                .sum();
    }

    public Venta obtenerUltimaVenta() {
        if (historialVentas.isEmpty()) {
            return null;
        }
        return historialVentas.get(historialVentas.size() - 1);
    }
}
