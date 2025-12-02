package com.tienda.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProductoAlimenticio extends Producto implements Descontable {

    private LocalDate fechaCaducidad;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ProductoAlimenticio(String id, String nombre, double precio, int stock, LocalDate fechaCaducidad) {
        super(id, nombre, precio, stock, "Alimentos");
        this.fechaCaducidad = fechaCaducidad;
    }

    public LocalDate getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(LocalDate fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public boolean estaPorCaducar() {
        return LocalDate.now().plusDays(7).isAfter(fechaCaducidad);
    }

    public boolean estaCaducado() {
        return LocalDate.now().isAfter(fechaCaducidad);
    }

    @Override
    public double calcularPrecioFinal() {
        return precio;
    }

    @Override
    public String obtenerDetallesEspecificos() {
        String estado = estaCaducado() ? " [CADUCADO]" : estaPorCaducar() ? " [PRÓXIMO A CADUCAR]" : "";
        return String.format("Caduca: %s%s",
                fechaCaducidad.format(FORMATTER), estado);
    }

    @Override
    public double aplicarDescuento(double porcentaje) {
        if (porcentaje < 0 || porcentaje > 100) {
            throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 100");
        }
        return precio - (precio * (porcentaje / 100));
    }

    @Override
    public boolean aceptaDescuentos() {
        return !estaCaducado();
    }

    @Override
    public String toString() {
        return String.format("%s | Caduca: %s | Precio Final: $%.2f",
                super.toString(),
                fechaCaducidad.format(FORMATTER),
                calcularPrecioFinal());
    }
}
