package com.tienda.model;

public class ProductoElectronico extends Producto implements Descontable {

    private static final double IMPUESTO_LUJO = 0.15;
    private int mesesGarantia;

    public ProductoElectronico(String id, String nombre, double precio, int stock, int mesesGarantia) {
        super(id, nombre, precio, stock, "Electrónica");
        this.mesesGarantia = mesesGarantia;
    }

    public int getMesesGarantia() {
        return mesesGarantia;
    }

    public void setMesesGarantia(int mesesGarantia) {
        this.mesesGarantia = mesesGarantia;
    }

    @Override
    public double calcularPrecioFinal() {
        return precio + (precio * IMPUESTO_LUJO);
    }

    @Override
    public String obtenerDetallesEspecificos() {
        return String.format("Garantía: %d meses | Impuesto: %.0f%%",
                mesesGarantia, IMPUESTO_LUJO * 100);
    }

    @Override
    public double aplicarDescuento(double porcentaje) {
        if (porcentaje < 0 || porcentaje > 100) {
            throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 100");
        }
        double precioConDescuento = precio - (precio * (porcentaje / 100));
        return precioConDescuento + (precioConDescuento * IMPUESTO_LUJO);
    }

    @Override
    public boolean aceptaDescuentos() {
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s | Garantía: %d meses | Precio Final: $%.2f",
                super.toString(), mesesGarantia, calcularPrecioFinal());
    }
}
