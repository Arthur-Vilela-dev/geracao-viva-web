# Parte 3 - API REST

API REST e uma forma de outros sistemas conversarem com o nosso sistema usando HTTP e JSON.

Neste projeto, criamos os endpoints minimos pedidos.

## Visitas

### POST /api/visitas

Agenda uma visita na ONG.

Exemplo de JSON para testar no Postman:

```json
{
  "nomeVisitante": "Maria Silva",
  "emailVisitante": "maria@email.com",
  "telefoneVisitante": "(11) 99999-9999",
  "dataHorario": "2026-06-20T09:00:00"
}
```

Se o horario estiver livre, o sistema retorna status `201 Created`.

### GET /api/visitas/disponibilidade

Consulta horarios disponiveis para uma data.

Exemplo:

```text
GET http://localhost:8080/api/visitas/disponibilidade?data=2026-06-20
```

### DELETE /api/visitas/{id}

Cancela uma visita agendada.

Exemplo:

```text
DELETE http://localhost:8080/api/visitas/1
```

## Voluntarios

### GET /api/voluntarios

Lista voluntarios cadastrados.

Este endpoint e protegido: apenas administrador pode acessar.

```text
GET http://localhost:8080/api/voluntarios
```

## Eventos

### GET /api/eventos

Lista proximos eventos cadastrados.

```text
GET http://localhost:8080/api/eventos
```

## Observacao importante

Os endpoints usam DTOs, que sao objetos feitos para entrada e saida da API.

Isso evita expor dados sensiveis, como senha de usuario, nas respostas JSON.
