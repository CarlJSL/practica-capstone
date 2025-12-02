# Sistema de Gestión de Tienda Virtual

Sistema de gestión de tienda virtual desarrollado en Java con arquitectura MVC, patrones de diseño y programación orientada a objetos.

## 🚀 Inicio Rápido

### Compilar
```bash
javac -d out -sourcepath src\main\java src\main\java\com\tienda\Main.java
```

### Ejecutar
```bash
java -cp out com.tienda.Main
```

## 📁 Estructura del Proyecto

```
src/main/java/com/tienda/
├── exception/          # Excepciones personalizadas
├── model/             # Clases del modelo (POJOs)
├── repository/        # Acceso a datos (Patrón Repository)
├── service/           # Lógica de negocio
├── ui/                # Interfaz de usuario (consola)
└── Main.java          # Punto de entrada
```

## ✨ Características Principales

- **Arquitectura MVC**: Separación clara de responsabilidades
- **POO Completa**: Abstracción, herencia, polimorfismo, interfaces
- **Patrón Repository**: Abstracción del acceso a datos
- **Colecciones**: HashMap (inventario), ArrayList (carrito/ventas), HashSet (categorías)
- **Manejo de Excepciones**: Validación robusta y excepciones personalizadas
- **Tickets Profesionales**: Formato con String.format() y fecha/hora

## 🛒 Funcionalidades

1. Ver catálogo de productos
2. Agregar productos al carrito
3. Ver carrito de compras
4. Procesar pago y generar ticket
5. Historial de ventas

## 🏗️ Arquitectura

### Model
- `Producto` (abstracta): Clase base
- `ProductoElectronico`: Hereda de Producto, aplica impuesto 15%
- `ProductoAlimenticio`: Hereda de Producto, sin impuestos
- `Descontable` (interfaz): Para productos con descuentos
- `ItemCarrito`: Representa items en el carrito
- `Venta`: Gestiona transacciones

### Repository
- `ProductoRepository` (interfaz): Contrato de acceso a datos
- `ProductoRepositoryImpl`: Implementación con HashMap

### Service
- `InventarioService`: Gestión de productos
- `CarritoService`: Gestión del carrito
- `VentaService`: Procesamiento de ventas

### UI
- `TiendaUI`: Interfaz de usuario (Scanner/System.out)

## 📦 Productos Incluidos

**Electrónicos**: Laptop, Mouse, Teclado, Monitor, Auriculares  
**Alimentos**: Leche, Pan, Yogurt, Manzanas, Cereal

## 🎓 Conceptos Aplicados

- Clase abstracta con métodos abstractos
- Herencia y polimorfismo
- Interfaces
- HashMap para búsquedas rápidas
- ArrayList para listas dinámicas
- HashSet para elementos únicos
- Excepciones personalizadas
- Try-catch para validación
- LocalDateTime para fechas
- String.format() para formato profesional

## 📝 Requisitos

- Java 8 o superior
- JDK instalado

---

**Desarrollado siguiendo principios SOLID y buenas prácticas de Java**
