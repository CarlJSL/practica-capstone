package com.tienda.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Venta {

    private static int contadorVentas = 0;
    private final String id;
    private final List<ItemCarrito> items;
    private final LocalDateTime fechaHora;
    private double subtotal;
    private double impuestos;
    private double total;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public Venta(List<ItemCarrito> items) {
        this.id = generarIdVenta();
        this.items = new ArrayList<>(items);
        this.fechaHora = LocalDateTime.now();
        calcularTotales();
    }

    private String generarIdVenta() {
        contadorVentas++;
        return String.format("V%04d", contadorVentas);
    }

    private void calcularTotales() {
        this.subtotal = 0;
        double totalConImpuestos = 0;

        for (ItemCarrito item : items) {
            double precioBase = item.getProducto().getPrecio() * item.getCantidad();
            double precioFinal = item.calcularSubtotal();

            this.subtotal += precioBase;
            totalConImpuestos += precioFinal;
        }

        this.impuestos = totalConImpuestos - subtotal;
        this.total = totalConImpuestos;
    }

    public String getId() {
        return id;
    }

    public List<ItemCarrito> getItems() {
        return new ArrayList<>(items);
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getImpuestos() {
        return impuestos;
    }

    public double getTotal() {
        return total;
    }

    public String getFechaHoraFormateada() {
        return fechaHora.format(FORMATTER);
    }

    public String generarTicket() {
        StringBuilder ticket = new StringBuilder();
        ticket.append("\n");
        ticket.append("═══════════════════════════════════════════════════════════════\n");
        ticket.append("                    TIENDA VIRTUAL DRA                         \n");
        ticket.append("═══════════════════════════════════════════════════════════════\n");
        ticket.append(String.format("Venta ID: %-20s Fecha: %s\n", id, getFechaHoraFormateada()));
        ticket.append("───────────────────────────────────────────────────────────────\n");
        ticket.append(String.format("%-30s %5s %12s %12s\n",
                "PRODUCTO", "CANT", "PRECIO UNIT", "SUBTOTAL"));
        ticket.append("───────────────────────────────────────────────────────────────\n");

        for (ItemCarrito item : items) {
            ticket.append(String.format("%-30s %5d $%11.2f $%11.2f\n",
                    truncarTexto(item.getProducto().getNombre(), 30),
                    item.getCantidad(),
                    item.getProducto().calcularPrecioFinal(),
                    item.calcularSubtotal()));
        }

        ticket.append("───────────────────────────────────────────────────────────────\n");
        ticket.append(String.format("%48s $%11.2f\n", "SUBTOTAL:", subtotal));
        ticket.append(String.format("%48s $%11.2f\n", "IMPUESTOS:", impuestos));
        ticket.append("═══════════════════════════════════════════════════════════════\n");
        ticket.append(String.format("%48s $%11.2f\n", "TOTAL A PAGAR:", total));
        ticket.append("═══════════════════════════════════════════════════════════════\n");
        ticket.append("              ¡Gracias por su compra!                          \n");
        ticket.append("═══════════════════════════════════════════════════════════════\n");

        return ticket.toString();
    }

    private String truncarTexto(String texto, int maxLength) {
        if (texto.length() <= maxLength) {
            return texto;
        }
        return texto.substring(0, maxLength - 3) + "...";
    }

    @Override
    public String toString() {
        return String.format("Venta %s - %s - Total: $%.2f",
                id, getFechaHoraFormateada(), total);
    }
}
