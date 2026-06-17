# Parte 4 - MVC, Services, Controllers e Security

Esta parte explica como o sistema foi organizado por camadas.

## MVC em linguagem simples

MVC significa:

- Model: as entidades JPA, como `Crianca`, `Voluntario`, `Oficina` e `Evento`.
- View: as paginas HTML em `src/main/resources/templates`.
- Controller: as classes que recebem cliques, formularios e chamadas da API.

Quando voce acessa uma pagina, o fluxo normalmente e:

```text
Navegador -> Controller -> Service -> Repository -> Banco de dados
```

Depois o caminho volta:

```text
Banco de dados -> Repository -> Service -> Controller -> Pagina HTML
```

## Entidades

As entidades ficam em:

```text
src/main/java/br/org/conectageracaoviva/model
```

Exemplo: `Oficina`.

Ela guarda nome, descricao, vagas, horarios e instrutor responsavel.

## Repositories

Os repositories ficam em:

```text
src/main/java/br/org/conectageracaoviva/repository
```

Eles herdam de `JpaRepository`.

Exemplo:

```java
public interface OficinaRepository extends JpaRepository<Oficina, Long> {
}
```

Isso ja permite usar metodos prontos:

- `findAll()`
- `findById(id)`
- `save(objeto)`
- `deleteById(id)`
- `count()`

## Services

Os services ficam em:

```text
src/main/java/br/org/conectageracaoviva/service
```

Eles guardam as regras de negocio.

Exemplo importante:

```java
public Matricula matricularCrianca(Long criancaId, Long oficinaId) {
    // Busca a oficina e a crianca no banco.
    // Verifica se a crianca ja esta matriculada.
    // Conta quantas matriculas a oficina ja possui.
    // Se ainda tiver vaga, cria a matricula.
}
```

Essa regra atende a RN-002:

```text
Uma crianca so pode ser matriculada em oficinas com vagas disponiveis.
```

## Controllers de tela

Os controllers de tela ficam em:

```text
src/main/java/br/org/conectageracaoviva/controller
```

Eles retornam nomes de templates Thymeleaf.

Exemplo:

```java
@GetMapping("/admin/eventos")
public String eventos(Model model) {
    model.addAttribute("eventos", eventoService.listarTodos());
    return "admin-eventos";
}
```

Esse metodo abre a pagina:

```text
src/main/resources/templates/admin-eventos.html
```

## Controllers de API

Os controllers REST ficam em:

```text
src/main/java/br/org/conectageracaoviva/controller/api
```

Eles usam `@RestController`, porque retornam JSON em vez de HTML.

Exemplo:

```java
@GetMapping
public List<EventoResponse> listarProximos() {
    return eventoService.proximosEventos()
            .stream()
            .map(EventoResponse::from)
            .toList();
}
```

## Spring Security

A configuracao de seguranca fica em:

```text
src/main/java/br/org/conectageracaoviva/security/SecurityConfig.java
```

Ela define quem pode acessar cada rota.

Exemplos:

- `/admin/**`: somente administrador.
- `/voluntario/**`: voluntario ou administrador.
- `/comunidade/**`: comunidade ou administrador.
- `/api/voluntarios`: somente administrador.
- `/api/eventos`: publico.
- `/api/visitas/**`: publico para facilitar agendamento e testes no Postman.

## Como testar no Postman

1. Abra o Postman.
2. Escolha o metodo HTTP, por exemplo `POST`.
3. Digite a URL:

```text
http://localhost:8080/api/visitas
```

4. Va em `Body`.
5. Escolha `raw`.
6. Escolha `JSON`.
7. Cole:

```json
{
  "nomeVisitante": "Maria Silva",
  "emailVisitante": "maria@email.com",
  "telefoneVisitante": "(11) 99999-9999",
  "dataHorario": "2026-06-20T09:00:00"
}
```

8. Clique em `Send`.

Se der certo, o retorno sera `201 Created`.

## Telas principais

- `/`: pagina inicial.
- `/login`: login por perfil.
- `/admin`: dashboard do administrador.
- `/voluntario`: dashboard do voluntario.
- `/comunidade`: dashboard da comunidade.
- `/admin/oficinas`: gestao de oficinas.
- `/admin/eventos`: gestao de eventos.
- `/admin/beneficiarios`: gestao de criancas e adolescentes.
- `/admin/doacoes`: visualizacao de doacoes.
- `/admin/relatorios`: relatorios gerais.
- `/doacoes/nova`: registrar doacao.
- `/visitas/nova`: agendar visita.
