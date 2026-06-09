# Documentacion API - BioMarket

La documentacion Swagger/OpenAPI se genera automaticamente con SpringDoc.

## URLs locales

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## Endpoints principales

- `GET /api/products`: lista productos con filtros opcionales.
- `GET /api/products/{id}`: consulta un producto.
- `POST /api/products`: crea un producto desde JSON.
- `PUT /api/products/{id}`: actualiza un producto.
- `DELETE /api/products/{id}`: elimina un producto.
- `GET /api/orders`: lista pedidos con filtros opcionales.
- `GET /api/orders/{id}`: consulta un pedido con detalles.
- `PATCH /api/orders/{id}/status`: actualiza el estado de un pedido.

## Archivo exportado

El archivo `openapi.json` de esta carpeta es una copia exportada de `/v3/api-docs` para adjuntar como evidencia del proyecto.
