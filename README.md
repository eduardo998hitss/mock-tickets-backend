# Mock Tickets Backend (Spring Boot, Java 17)

Mock backend para criação/listagem de tickets, pensado para demos e integração com front-end.

## Como rodar
1. Requisitos: Java 17, Maven 3.9+
2. Instalar dependências e subir no perfil **mock**:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=mock
   ```
3. Endpoints:
   - `POST http://localhost:8080/api/tickets`
   - `GET  http://localhost:8080/api/tickets/{id}`
   - `GET  http://localhost:8080/api/tickets?page=0&size=10&q=teste`

4. Swagger/OpenAPI:
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - JSON: `http://localhost:8080/v3/api-docs`

## Mock controls
- `x-latency: <ms>` → adiciona latência por requisição.
- `x-force-error: any` → força erro 400 nesta requisição.
- `mock.errorRate` no `application-mock.yml` para taxa de erro randômico.

## Exemplo de criação (payload)
```json
{
  "company": "Ambev S.A.",
  "category": "Cadastro",
  "subject": "teste",
  "local": "Belo Horizonte",
  "ticket": "123321",
  "attachment": "C:\\fakepath\\Planilha sem título.xlsx"
}
```

## Resposta esperada (201)
```json
{
  "id": "uuid",
  "company": "Ambev S.A.",
  "category": "Cadastro",
  "subject": "teste",
  "local": "Belo Horizonte",
  "ticket": "123321",
  "attachment": "C:\\fakepath\\Planilha sem título.xlsx",
  "status": "OPEN",
  "createdAt": "2025-08-20T19:42:47.952584Z"
}
```