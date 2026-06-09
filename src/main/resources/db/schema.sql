CREATE DATABASE eco_store;
USE eco_store;

-- =========================
-- TABLE: client
-- =========================
CREATE TABLE client (
    id_client INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(255)
);

-- =========================
-- TABLE: category
-- =========================
CREATE TABLE category (
    id_category INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

-- =========================
-- TABLE: product
-- =========================
CREATE TABLE product (
    id_product INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    image VARCHAR(255),
    id_category INT NOT NULL,

    CONSTRAINT fk_product_category
        FOREIGN KEY (id_category)
        REFERENCES category(id_category),

    CONSTRAINT chk_price
        CHECK (price > 0),

    CONSTRAINT chk_stock
        CHECK (stock >= 0)
);

-- =========================
-- TABLE: orders
-- =========================
CREATE TABLE orders (
    id_order INT AUTO_INCREMENT PRIMARY KEY,
    order_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2) NOT NULL,
    status VARCHAR(50),
    id_client INT NOT NULL,

    CONSTRAINT fk_orders_client
        FOREIGN KEY (id_client)
        REFERENCES client(id_client)
);

-- =========================
-- TABLE: order_detail
-- =========================
CREATE TABLE order_detail (
    id_detail INT AUTO_INCREMENT PRIMARY KEY,
    quantity INT NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    id_product INT NOT NULL,
    id_order INT NOT NULL,

    CONSTRAINT fk_detail_product
        FOREIGN KEY (id_product)
        REFERENCES product(id_product),

    CONSTRAINT fk_detail_order
        FOREIGN KEY (id_order)
        REFERENCES orders(id_order),

    CONSTRAINT chk_quantity
        CHECK (quantity > 0),

    CONSTRAINT chk_subtotal
        CHECK (subtotal >= 0)
);