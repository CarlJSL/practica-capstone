package com.tienda.exception;

public class StockInsuficienteException extends Exception {

    private final String productoId;
    private final int stockDisponible;
    private final int cantidadSolicitada;

    public StockInsuficienteException(String productoId, int stockDisponible, int cantidadSolicitada) {
        super(String.format("Stock insuficiente para el producto '%s'. Disponible: %d, Solicitado: %d",
                productoId, stockDisponible, cantidadSolicitada));
        this.productoId = productoId;
        this.stockDisponible = stockDisponible;
        this.cantidadSolicitada = cantidadSolicitada;
    }

    public String getProductoId() {
        return productoId;
    }

    public int getStockDisponible() {
        return stockDisponible;
    }

    public int getCantidadSolicitada() {
        return cantidadSolicitada;
    }
}
