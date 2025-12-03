## COPIAR EL ARCHIVO DE application-example.properties
## Y HACER EL application.properties

# ESTRUCTURA DE API

```
com.pasteleria
│
├── ApiPasteleriaApplication.java
│
├── auth/                              <-- Módulo 1: Autenticación
│   ├── config/
│   │   └── SecurityConfig.java
│   ├── controller/
│   │   └── AuthController.java
│   ├── dto/
│   │   ├── AuthResponse.java
│   │   ├── CambiarPasswordRequest.java
│   │   ├── LoginRequest.java
│   │   ├── LoginResponse.java
│   │   ├── RecuperarPasswordRequest.java
│   │   ├── RegisterRequest.java
│   │   └── VerificarCodigoRequest.java
│   ├── Filter/
│   │   └── UserAuthenticationFilter.java
│   ├── service/
│   │   ├── AuthService.java
│   │   ├── EmailService.java
│   │   └── RecuperarPasswordService.java
│   └── util/
│       ├── JwtUtil.java
│       └── SecretKeyGenerator.java
│
├── detalle_pedido/                    <-- Módulo: Detalle de Pedidos
│   ├── controller/
│   │   └── DetallePedidoController.java
│   ├── model/
│   │   └── DetallePedido.java
│   ├── repository/
│   │   └── DetallePedidoRepository.java
│   └── service/
│       └── DetallePedidoService.java
│
├── inventario/                        <-- Módulo 5: Gestión de Inventario (CRUD de Pasteles)
│   ├── controller/
│   │   ├── DecoracionController.java
│   │   ├── PastelInventarioController.java
│   │   ├── SaborController.java
│   │   └── TamanioController.java
│   ├── model/
│   │   ├── Decoracion.java
│   │   ├── PastelInventario.java
│   │   ├── Sabor.java
│   │   └── Tamanio.java
│   ├── repository/
│   │   ├── DecoracionRepository.java
│   │   ├── PastelInventarioRepository.java
│   │   ├── SaborRepository.java
│   │   └── TamanioRepository.java
│   └── service/
│       └── PastelService.java
│
├── pago/                              <-- Módulo: Gestión de Pagos
│   ├── PagoController.java
│   ├── PagoRequest.java
│   ├── PagoResponse.java
│   └── PagoService.java
│
├── pedido/                            <-- Módulos 3 y 4: Personalización, Pedido y Gestión de Estados
│   ├── controller/
│   │   └── PedidoController.java
│   ├── model/
│   │   ├── EstadoPedido.java
│   │   └── Pedido.java
│   ├── repository/
│   │   ├── EstadoPedidoRepository.java
│   │   └── PedidoRepository.java
│   └── service/
│       └── PedidoService.java
│
└── usuario/                           <-- Módulo 2: Gestión de Usuarios y Roles
    ├── controller/
    │   └── UsuarioController.java
    ├── model/
    │   ├── Rol.java
    │   ├── RolEntity.java
    │   ├── Usuario.java
    │   └── UsuarioDetails.java
    ├── repository/
    │   ├── RolRepository.java
    │   └── UsuarioRepository.java
    └── service/
        ├── UsuarioDetailsService.java
        └── UsuarioService.java
```