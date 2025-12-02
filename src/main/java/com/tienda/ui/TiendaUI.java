package com.tienda.ui;

import com.tienda.exception.StockInsuficienteException;
import com.tienda.model.*;
import com.tienda.repository.ProductoRepositoryImpl;
import com.tienda.service.CarritoService;
import com.tienda.service.InventarioService;
import com.tienda.service.VentaService;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class TiendaUI {

    private final Scanner scanner;
    private final InventarioService inventarioService;
    private final CarritoService carritoService;
    private final VentaService ventaService;

    public TiendaUI() {
        this.scanner = new Scanner(System.in);

        // Inicializar repositorio y servicios
        ProductoRepositoryImpl productoRepository = new ProductoRepositoryImpl();
        this.inventarioService = new InventarioService(productoRepository);
        this.carritoService = new CarritoService(inventarioService);
        this.ventaService = new VentaService(inventarioService);

        // Cargar productos de ejemplo
        cargarProductosIniciales();
    }
    
    private void cargarProductosIniciales() {
        // Productos electrónicos
        inventarioService.agregarProducto(
                new ProductoElectronico("E001", "Laptop HP", 15000.00, 5, 12));
        inventarioService.agregarProducto(
                new ProductoElectronico("E002", "Mouse Logitech", 350.00, 15, 6));
        inventarioService.agregarProducto(
                new ProductoElectronico("E003", "Teclado Mecánico", 1200.00, 8, 12));
        inventarioService.agregarProducto(
                new ProductoElectronico("E004", "Monitor Samsung 24\"", 3500.00, 0, 24));
        inventarioService.agregarProducto(
                new ProductoElectronico("E005", "Auriculares Sony", 800.00, 12, 6));

        // Productos alimenticios
        inventarioService.agregarProducto(
                new ProductoAlimenticio("A001", "Leche Entera 1L", 25.00, 30, LocalDate.now().plusDays(5)));
        inventarioService.agregarProducto(
                new ProductoAlimenticio("A002", "Pan Integral", 35.00, 20, LocalDate.now().plusDays(3)));
        inventarioService.agregarProducto(
                new ProductoAlimenticio("A003", "Yogurt Natural", 18.00, 25, LocalDate.now().plusDays(10)));
        inventarioService.agregarProducto(
                new ProductoAlimenticio("A004", "Manzanas (kg)", 45.00, 50, LocalDate.now().plusDays(7)));
        inventarioService.agregarProducto(
                new ProductoAlimenticio("A005", "Cereal Fitness", 65.00, 0, LocalDate.now().plusDays(180)));
    }
    
    public void iniciar() {
        mostrarBienvenida();

        boolean continuar = true;

        do {
            try {
                mostrarMenuPrincipal();
                int opcion = leerOpcion();

                switch (opcion) {
                    case 1:
                        verCatalogo();
                        break;
                    case 2:
                        agregarAlCarrito();
                        break;
                    case 3:
                        verCarrito();
                        break;
                    case 4:
                        pagarYSalir();
                        continuar = false;
                        break;
                    case 5:
                        salir();
                        continuar = false;
                        break;
                    default:
                        System.out.println("\n Opción inválida. Por favor, seleccione una opción del 1 al 5.");
                }

                if (continuar) {
                    pausar();
                }

            } catch (InputMismatchException e) {
                System.out.println("\n Error: Debe ingresar un número válido.");
                scanner.nextLine(); // Limpiar buffer
                pausar();
            } catch (Exception e) {
                System.out.println("\n Error inesperado: " + e.getMessage());
                pausar();
            }

        } while (continuar);

        scanner.close();
    }
    
    private void mostrarBienvenida() {
        limpiarPantalla();
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("           BIENVENIDO A TIENDA VIRTUAL DRA                ");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println();
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                      MENÚ PRINCIPAL                           ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════╣");
        System.out.println("║  1.VVer Catálogo de Productos                             ║");
        System.out.println("║  2.Agregar Producto al Carrito                           ║");
        System.out.println("║  3.Ver Carrito de Compras                                ║");
        System.out.println("║  4.Pagar y Salir                                          ║");
        System.out.println("║  5.Salir sin Comprar                                      ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        System.out.print("\nSeleccione una opción: ");
    }
    
    private int leerOpcion() {
        int opcion = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer
        return opcion;
    }

    private void verCatalogo() {
        limpiarPantalla();
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("                     CATÁLOGO DE PRODUCTOS                    ");
        System.out.println("═══════════════════════════════════════════════════════════════\n");

        List<Producto> productos = inventarioService.obtenerTodosLosProductos();

        if (productos.isEmpty()) {
            System.out.println("No hay productos disponibles en este momento.");
            return;
        }

        System.out.println("┌─────┬──────────────────────────┬──────────────┬───────┬──────────────────────────┐");
        System.out.println("│ ID  │ NOMBRE                   │ PRECIO BASE  │ STOCK │ DETALLES                 │");
        System.out.println("├─────┼──────────────────────────┼──────────────┼───────┼──────────────────────────┤");

        for (Producto producto : productos) {
            String stockInfo = producto.getStock() == 0 ? "  0  (Sin Stock)"
                    : String.format("%5d", producto.getStock());

            System.out.printf("│ %-3s │ %-24s │ $%10.2f │ %-13s │ %-24s │%n",
                    producto.getId(),
                    truncar(producto.getNombre(), 24),
                    producto.getPrecio(),
                    stockInfo,
                    truncar(producto.obtenerDetallesEspecificos(), 24));
        }

        System.out.println("└─────┴──────────────────────────┴──────────────┴───────┴──────────────────────────┘");
        System.out.println("\n Nota: El precio final puede incluir impuestos según el tipo de producto.");
    }
    
    private void agregarAlCarrito() {
        limpiarPantalla();
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("                 AGREGAR PRODUCTO AL CARRITO                 ");
        System.out.println("═══════════════════════════════════════════════════════════════\n");

        try {
            System.out.print("Ingrese el ID del producto: ");
            String productoId = scanner.nextLine().trim().toUpperCase();

            // Verificar que el producto existe
            var productoOpt = inventarioService.buscarProducto(productoId);
            if (productoOpt.isEmpty()) {
                System.out.println("\n Error: Producto no encontrado.");
                return;
            }

            Producto producto = productoOpt.get();

            // Verificar que haya stock
            if (producto.getStock() == 0) {
                System.out.println("\n Error: Este producto no tiene stock disponible.");
                return;
            }

            System.out.print("Ingrese la cantidad: ");
            int cantidad = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer

            if (cantidad <= 0) {
                System.out.println("\n Error: La cantidad debe ser mayor a 0.");
                return;
            }

            // Intentar agregar al carrito
            carritoService.agregarProducto(productoId, cantidad);

            System.out.println("\n Producto agregado al carrito exitosamente!");
            System.out.printf("   %s x%d - $%.2f c/u%n",
                    producto.getNombre(),
                    cantidad,
                    producto.calcularPrecioFinal());

        } catch (InputMismatchException e) {
            System.out.println("\n Error: Debe ingresar un número válido para la cantidad.");
            scanner.nextLine(); // Limpiar buffer
        } catch (StockInsuficienteException e) {
            System.out.println("\n " + e.getMessage());
            System.out.println("   Por favor, reduzca la cantidad o elija otro producto.");
        } catch (Exception e) {
            System.out.println("\n Error: " + e.getMessage());
        }
    }
    
    private void verCarrito() {
        limpiarPantalla();
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("                     CARRITO DE COMPRAS                       ");
        System.out.println("═══════════════════════════════════════════════════════════════\n");

        if (carritoService.estaVacio()) {
            System.out.println(" El carrito está vacío.");
            System.out.println("\n Agregue productos desde el menú principal.");
            return;
        }

        List<ItemCarrito> items = carritoService.obtenerItems();

        System.out.println("┌──────────────────────────┬──────┬──────────────┬──────────────┐");
        System.out.println("│ PRODUCTO                 │ CANT │ PRECIO UNIT  │ SUBTOTAL     │");
        System.out.println("├──────────────────────────┼──────┼──────────────┼──────────────┤");

        for (ItemCarrito item : items) {
            System.out.printf("│ %-24s │ %4d │ $%10.2f │ $%10.2f │%n",
                    truncar(item.getProducto().getNombre(), 24),
                    item.getCantidad(),
                    item.getProducto().calcularPrecioFinal(),
                    item.calcularSubtotal());
        }

        System.out.println("└──────────────────────────┴──────┴──────────────┴──────────────┘");
        System.out.printf("\n%52s $%10.2f%n", "TOTAL:", carritoService.calcularTotal());
        System.out.println("\n Items en el carrito: " + carritoService.cantidadItems());
        System.out.println("Total de productos: " + carritoService.cantidadTotalProductos());
    }


    private void pagarYSalir() {
        limpiarPantalla();

        if (carritoService.estaVacio()) {
            System.out.println("═══════════════════════════════════════════════════════════════");
            System.out.println("                        CARRITO VACÍO                        ");
            System.out.println("═══════════════════════════════════════════════════════════════\n");
            System.out.println("No hay productos en el carrito para procesar.");
            System.out.println("\n¡Gracias por visitar Tienda Virtual DRA!");
            return;
        }

        try {
            // Procesar la venta
            Venta venta = ventaService.procesarVenta(carritoService.obtenerItems());

            // Mostrar ticket
            System.out.println(venta.generarTicket());

            // Vaciar el carrito
            carritoService.vaciar();

            System.out.println("\n Pago procesado exitosamente.");
            System.out.println(" ¡Gracias por su compra!");

        } catch (StockInsuficienteException e) {
            System.out.println("═══════════════════════════════════════════════════════════════");
            System.out.println("                     ERROR EN LA VENTA                       ");
            System.out.println("═══════════════════════════════════════════════════════════════\n");
            System.out.println(e.getMessage());
            System.out.println("\nAlgún producto se quedó sin stock. Por favor, revise su carrito.");
        } catch (Exception e) {
            System.out.println("\n Error al procesar la venta: " + e.getMessage());
        }
    }
    
    private void salir() {
        limpiarPantalla();
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("                     HASTA PRONTO                            ");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        System.out.println("Gracias por visitar Tienda Virtual DRA.");
        System.out.println("¡Esperamos verle pronto!");

        if (!carritoService.estaVacio()) {
            System.out.println("\n Nota: Los productos en su carrito no fueron procesados.");
        }
    }
    
    private String truncar(String texto, int maxLength) {
        if (texto.length() <= maxLength) {
            return texto;
        }
        return texto.substring(0, maxLength - 3) + "...";
    }
    
    private void pausar() {
        System.out.print("\nPresione Enter para continuar...");
        scanner.nextLine();
    }
    
    private void limpiarPantalla() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
}
