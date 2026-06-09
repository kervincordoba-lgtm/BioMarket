USE eco_store;

START TRANSACTION;

INSERT INTO category (name, description)
SELECT 'Alimentos', 'Productos naturales y organicos para consumo diario.'
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Alimentos');

INSERT INTO category (name, description)
SELECT 'Cuidado personal', 'Productos ecologicos para higiene y cuidado personal.'
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Cuidado personal');

INSERT INTO category (name, description)
SELECT 'Limpieza del hogar', 'Productos biodegradables para la limpieza del hogar.'
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Limpieza del hogar');

INSERT INTO category (name, description)
SELECT 'Mascotas', 'Productos sostenibles para el cuidado de mascotas.'
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Mascotas');

INSERT INTO category (name, description)
SELECT 'Cocina y hogar', 'Utensilios y articulos reutilizables para la cocina y el hogar.'
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Cocina y hogar');

INSERT INTO product (name, description, price, stock, image, id_category)
SELECT 'Aceite de oliva', 'Aceite de oliva natural para preparaciones saludables.', 28900.00, 25, 'aceite_de_oliva.png',
       (SELECT id_category FROM category WHERE name = 'Alimentos' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Aceite de oliva');

INSERT INTO product (name, description, price, stock, image, id_category)
SELECT 'Arroz integral', 'Arroz integral de grano seleccionado para una alimentacion balanceada.', 9800.00, 40, 'arroz_integral.png',
       (SELECT id_category FROM category WHERE name = 'Alimentos' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Arroz integral');

INSERT INTO product (name, description, price, stock, image, id_category)
SELECT 'Cafe organico', 'Cafe organico de aroma intenso cultivado de forma responsable.', 22500.00, 30, 'cafe_organico.png',
       (SELECT id_category FROM category WHERE name = 'Alimentos' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Cafe organico');

INSERT INTO product (name, description, price, stock, image, id_category)
SELECT 'Granola artesanal', 'Granola artesanal con ingredientes naturales para desayunos y snacks.', 14500.00, 35, 'granola_artesanal.png',
       (SELECT id_category FROM category WHERE name = 'Alimentos' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Granola artesanal');

INSERT INTO product (name, description, price, stock, image, id_category)
SELECT 'Panela natural', 'Panela natural para endulzar bebidas y recetas de forma tradicional.', 7200.00, 45, 'panela_natural.png',
       (SELECT id_category FROM category WHERE name = 'Alimentos' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Panela natural');

INSERT INTO product (name, description, price, stock, image, id_category)
SELECT 'Cepillo dental de bambu', 'Cepillo dental con mango de bambu biodegradable.', 8900.00, 50, 'cepillo_dental_bambu.png',
       (SELECT id_category FROM category WHERE name = 'Cuidado personal' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Cepillo dental de bambu');

INSERT INTO product (name, description, price, stock, image, id_category)
SELECT 'Crema hidratante', 'Crema hidratante con ingredientes naturales para uso diario.', 18900.00, 24, 'crema_hidratante.png',
       (SELECT id_category FROM category WHERE name = 'Cuidado personal' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Crema hidratante');

INSERT INTO product (name, description, price, stock, image, id_category)
SELECT 'Desodorante natural', 'Desodorante natural libre de aluminio para cuidado diario.', 16500.00, 32, 'desodorante_natural.png',
       (SELECT id_category FROM category WHERE name = 'Cuidado personal' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Desodorante natural');

INSERT INTO product (name, description, price, stock, image, id_category)
SELECT 'Jabon natural', 'Jabon natural elaborado con componentes suaves para la piel.', 6900.00, 60, 'jabon_natural.png',
       (SELECT id_category FROM category WHERE name = 'Cuidado personal' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Jabon natural');

INSERT INTO product (name, description, price, stock, image, id_category)
SELECT 'Shampoo herbal', 'Shampoo herbal con extractos naturales para limpieza capilar.', 17500.00, 28, 'shampoo_herbal.png',
       (SELECT id_category FROM category WHERE name = 'Cuidado personal' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Shampoo herbal');

INSERT INTO product (name, description, price, stock, image, id_category)
SELECT 'Desinfectante hogar', 'Desinfectante para superficies con formula biodegradable.', 13900.00, 30, 'desinfectante_hogar.png',
       (SELECT id_category FROM category WHERE name = 'Limpieza del hogar' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Desinfectante hogar');

INSERT INTO product (name, description, price, stock, image, id_category)
SELECT 'Detergente liquido', 'Detergente liquido biodegradable para lavado de ropa.', 19800.00, 26, 'detergente_liquido.png',
       (SELECT id_category FROM category WHERE name = 'Limpieza del hogar' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Detergente liquido');

INSERT INTO product (name, description, price, stock, image, id_category)
SELECT 'Esponjas ecologicas', 'Set de esponjas ecologicas reutilizables para limpieza diaria.', 9500.00, 45, 'esponjas_ecologicas.png',
       (SELECT id_category FROM category WHERE name = 'Limpieza del hogar' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Esponjas ecologicas');

INSERT INTO product (name, description, price, stock, image, id_category)
SELECT 'Jabon lavaplatos', 'Jabon lavaplatos biodegradable para limpieza de utensilios.', 11200.00, 38, 'jabon_lavaplatos.png',
       (SELECT id_category FROM category WHERE name = 'Limpieza del hogar' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Jabon lavaplatos');

INSERT INTO product (name, description, price, stock, image, id_category)
SELECT 'Limpiador multiusos', 'Limpiador multiusos ecologico para diferentes superficies.', 12900.00, 34, 'limpiador_multiusos.png',
       (SELECT id_category FROM category WHERE name = 'Limpieza del hogar' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Limpiador multiusos');

INSERT INTO product (name, description, price, stock, image, id_category)
SELECT 'Alimento para gato', 'Alimento para gato con ingredientes seleccionados.', 24500.00, 22, 'alimento_para_gato.png',
       (SELECT id_category FROM category WHERE name = 'Mascotas' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Alimento para gato');

INSERT INTO product (name, description, price, stock, image, id_category)
SELECT 'Alimento para perro', 'Alimento para perro con aporte nutricional balanceado.', 26900.00, 20, 'alimento_para_perro.png',
       (SELECT id_category FROM category WHERE name = 'Mascotas' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Alimento para perro');

INSERT INTO product (name, description, price, stock, image, id_category)
SELECT 'Juguete mordedor', 'Juguete mordedor resistente para entretenimiento de mascotas.', 13500.00, 30, 'juguete_mordedor.png',
       (SELECT id_category FROM category WHERE name = 'Mascotas' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Juguete mordedor');

INSERT INTO product (name, description, price, stock, image, id_category)
SELECT 'Plato para mascotas', 'Plato reutilizable para comida o agua de mascotas.', 11800.00, 33, 'plato_para_mascotas.png',
       (SELECT id_category FROM category WHERE name = 'Mascotas' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Plato para mascotas');

INSERT INTO product (name, description, price, stock, image, id_category)
SELECT 'Shampoo para mascotas', 'Shampoo suave para el cuidado y limpieza de mascotas.', 16800.00, 27, 'shampoo_para_mascotas.png',
       (SELECT id_category FROM category WHERE name = 'Mascotas' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Shampoo para mascotas');

INSERT INTO product (name, description, price, stock, image, id_category)
SELECT 'Olla antiadherente', 'Olla antiadherente para cocinar con menos aceite.', 68900.00, 12, 'olla_antiadherente.png',
       (SELECT id_category FROM category WHERE name = 'Cocina y hogar' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Olla antiadherente');

INSERT INTO product (name, description, price, stock, image, id_category)
SELECT 'Recipientes hermeticos', 'Set de recipientes hermeticos reutilizables para almacenamiento.', 32500.00, 18, 'recipientes_hermeticos.png',
       (SELECT id_category FROM category WHERE name = 'Cocina y hogar' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Recipientes hermeticos');

INSERT INTO product (name, description, price, stock, image, id_category)
SELECT 'Sarten ecologico', 'Sarten ecologico antiadherente para preparaciones diarias.', 54900.00, 14, 'sarten_ecologico.png',
       (SELECT id_category FROM category WHERE name = 'Cocina y hogar' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Sarten ecologico');

INSERT INTO product (name, description, price, stock, image, id_category)
SELECT 'Set de cubiertos', 'Set de cubiertos reutilizables para uso diario.', 18500.00, 25, 'set_de_cubiertos.png',
       (SELECT id_category FROM category WHERE name = 'Cocina y hogar' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Set de cubiertos');

INSERT INTO product (name, description, price, stock, image, id_category)
SELECT 'Tabla de bambu', 'Tabla de bambu resistente para preparacion de alimentos.', 21900.00, 20, 'tabla_de_bambu.png',
       (SELECT id_category FROM category WHERE name = 'Cocina y hogar' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Tabla de bambu');

COMMIT;

SELECT
    c.id_category,
    c.name AS category_name,
    p.id_product,
    p.name AS product_name,
    p.price,
    p.stock,
    p.image
FROM category c
LEFT JOIN product p ON p.id_category = c.id_category
WHERE c.name IN (
    'Alimentos',
    'Cuidado personal',
    'Limpieza del hogar',
    'Mascotas',
    'Cocina y hogar'
)
ORDER BY c.name, p.name;
