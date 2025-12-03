-- =====================================================
-- SCRIPT DE BASE DE DATOS - PASTELERÍA
-- =====================================================

DROP DATABASE IF EXISTS pasteleria_db;
CREATE DATABASE pasteleria_db;
USE pasteleria_db;

-- =====================================================
-- TABLAS DE CATÁLOGOS
-- =====================================================

CREATE TABLE ROL (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(30) NOT NULL
);

CREATE TABLE ESTADO_PEDIDO (
    id_estado INT AUTO_INCREMENT PRIMARY KEY,
    nombre_estado VARCHAR(50) NOT NULL
);

CREATE TABLE SABOR (
    id_sabor INT AUTO_INCREMENT PRIMARY KEY,
    nombre_sabor VARCHAR(100) NOT NULL,
    precio DOUBLE NOT NULL DEFAULT 0
);

CREATE TABLE TAMANIO (
    id_tamanio INT AUTO_INCREMENT PRIMARY KEY,
    nombre_tamanio VARCHAR(100) NOT NULL,
    precio DOUBLE NOT NULL DEFAULT 0
);

CREATE TABLE DECORACION (
    id_decoracion INT AUTO_INCREMENT PRIMARY KEY,
    nombre_decoracion VARCHAR(100) NOT NULL,
    precio DOUBLE NOT NULL DEFAULT 0
);

-- =====================================================
-- TABLAS DE USUARIOS
-- =====================================================

CREATE TABLE USUARIO (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(60) NOT NULL,
    correo VARCHAR(60) NOT NULL UNIQUE,
    telefono VARCHAR(10) NOT NULL,
    contrasenia VARCHAR(255) NOT NULL,
    estatus INT NOT NULL DEFAULT 1
);

CREATE TABLE USUARIO_ROL (
    id_usuario INT NOT NULL,
    id_rol INT NOT NULL,
    PRIMARY KEY (id_usuario, id_rol),
    CONSTRAINT fk_usuariorol_usuario
        FOREIGN KEY (id_usuario) REFERENCES USUARIO (id_usuario)
        ON DELETE CASCADE,
    CONSTRAINT fk_usuariorol_rol
        FOREIGN KEY (id_rol) REFERENCES ROL (id_rol)
);

-- =====================================================
-- TABLAS DE INVENTARIO
-- =====================================================

CREATE TABLE PASTEL_INVENTARIO (
    id_pastel_inv INT AUTO_INCREMENT PRIMARY KEY,
    nombre_pastel VARCHAR(100) NOT NULL,
    imagen_referencia VARCHAR(500),
    precio DOUBLE NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    estatus BOOLEAN NOT NULL DEFAULT TRUE,
    
    id_sabor INT NOT NULL,
    id_tamanio INT NOT NULL,
    id_decoracion INT NOT NULL,
    CONSTRAINT fk_inv_sabor FOREIGN KEY (id_sabor) REFERENCES SABOR (id_sabor),
    CONSTRAINT fk_inv_tamanio FOREIGN KEY (id_tamanio) REFERENCES TAMANIO (id_tamanio),
    CONSTRAINT fk_inv_decoracion FOREIGN KEY (id_decoracion) REFERENCES DECORACION (id_decoracion)
);

-- =====================================================
-- TABLAS DE PEDIDOS Y PAGOS
-- =====================================================

CREATE TABLE PEDIDO (
    id_pedido INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_estado INT NOT NULL,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_entrega DATE NOT NULL,
    hora_entrega TIME NOT NULL,
    precio_total DOUBLE NOT NULL,
    metodo_pago VARCHAR(20) DEFAULT 'TARJETA',
    CONSTRAINT fk_pedido_usuario
        FOREIGN KEY (id_usuario) REFERENCES USUARIO (id_usuario),
    CONSTRAINT fk_pedido_estado
        FOREIGN KEY (id_estado) REFERENCES ESTADO_PEDIDO (id_estado)
);


CREATE TABLE PAGO (
    id_pago INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    tipo_pago ENUM('Efectivo', 'Tarjeta') NOT NULL,
    monto DOUBLE NOT NULL,
    fecha_pago DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pago_pedido
        FOREIGN KEY (id_pedido) REFERENCES PEDIDO (id_pedido)
);

CREATE TABLE ITEM_CARRITO (
    id_carrito INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    precio_unitario DOUBLE NOT NULL,
    tipo_pastel VARCHAR(50) NOT NULL, -- 'Catalogo' o 'Personalizado'
    cantidad INT NOT NULL DEFAULT 1,
    mensaje_texto TEXT,	
    ruta_imagen VARCHAR(255),
    
    -- Llaves foráneas opcionales 
    id_pastel_inv INT,
    id_sabor INT,
    id_tamanio INT,
    id_decoracion INT,
    
    CONSTRAINT fk_carrito_usuario FOREIGN KEY (id_usuario) REFERENCES USUARIO (id_usuario) ON DELETE CASCADE,
    CONSTRAINT fk_carrito_inv FOREIGN KEY (id_pastel_inv) REFERENCES PASTEL_INVENTARIO (id_pastel_inv),
    CONSTRAINT fk_carrito_sabor FOREIGN KEY (id_sabor) REFERENCES SABOR (id_sabor),
    CONSTRAINT fk_carrito_tam FOREIGN KEY (id_tamanio) REFERENCES TAMANIO (id_tamanio),
    CONSTRAINT fk_carrito_dec FOREIGN KEY (id_decoracion) REFERENCES DECORACION (id_decoracion)
);

CREATE TABLE DETALLE_PEDIDO (
    id_detalle_ped INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    precio_unitario DOUBLE NOT NULL,
    tipo_pastel VARCHAR(50) NOT NULL,
    cantidad INT NOT NULL,
    mensaje_texto TEXT,
    ruta_imagen VARCHAR(255),
    
    -- Llaves foráneas opcionales
    id_pastel_inv INT,
    id_sabor INT,
    id_tamanio INT,
    id_decoracion INT,
    
    CONSTRAINT fk_detalle_pedido FOREIGN KEY (id_pedido) REFERENCES PEDIDO (id_pedido) ON DELETE CASCADE,
    CONSTRAINT fk_detalle_inv FOREIGN KEY (id_pastel_inv) REFERENCES PASTEL_INVENTARIO (id_pastel_inv),
    CONSTRAINT fk_detalle_sabor FOREIGN KEY (id_sabor) REFERENCES SABOR (id_sabor),
    CONSTRAINT fk_detalle_tam FOREIGN KEY (id_tamanio) REFERENCES TAMANIO (id_tamanio),
    CONSTRAINT fk_detalle_dec FOREIGN KEY (id_decoracion) REFERENCES DECORACION (id_decoracion)
);

-- =====================================================
-- INSERCIÓN DE DATOS INICIALES
-- =====================================================

-- Insertar roles
INSERT INTO ROL (nombre_rol) VALUES ('ADMINISTRADOR');
INSERT INTO ROL (nombre_rol) VALUES ('CLIENTE');

-- Insertar estados de pedido
INSERT INTO ESTADO_PEDIDO (nombre_estado) VALUES ('PENDIENTE');
INSERT INTO ESTADO_PEDIDO (nombre_estado) VALUES ('PREPARACION');
INSERT INTO ESTADO_PEDIDO (nombre_estado) VALUES ('ENTREGADO');
INSERT INTO ESTADO_PEDIDO (nombre_estado) VALUES ('RECHAZADO');

-- Insertar sabores
INSERT INTO SABOR (nombre_sabor, precio) VALUES 
('Chocolate', 50.00),
('Vainilla', 40.00),
('Fresa', 45.00),
('Café', 55.00),
('Red Velvet', 60.00);

-- Insertar tamaños
INSERT INTO TAMANIO (nombre_tamanio, precio) VALUES 
('Chico', 100.00),
('Mediano', 150.00),
('Grande', 200.00),
('Extra Grande', 280.00);

-- Insertar decoraciones
INSERT INTO DECORACION (nombre_decoracion, precio) VALUES 
('Sencilla', 30.00),
('Con Frutas', 50.00),
('Con Chocolate', 45.00),
('Temática', 80.00),
('Premium', 120.00);

-- =====================================================
-- COMANDOS POST-INSTALACIÓN
-- =====================================================
-- 
-- Una vez ejecutado este script y con el API corriendo correctamente,
-- puedes convertir un usuario existente a ADMINISTRADOR.
-- 
-- INSTRUCCIONES:
-- 1. Registra un usuario normalmente desde el front-end (se crea como CLIENTE)
-- 2. Descomenta las siguientes líneas (elimina los --)
-- 3. Cambia el id_usuario por el ID del usuario que deseas promover, por defecto será el 
-- primer usuario que registrarás.
-- 4. Ejecuta el comando en MySQL
--
-- UPDATE USUARIO_ROL SET id_rol = 1 WHERE id_usuario = 1 AND id_rol = 2;