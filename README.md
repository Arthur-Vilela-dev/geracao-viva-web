# Conecta Geracao Viva

Sistema web em Java com Spring Boot para a ONG Conecta Geracao Viva.

Este projeto esta sendo construido por partes, com explicacoes simples para quem esta aprendendo Java, Spring Boot, MySQL, Thymeleaf e Spring Security.

## Tecnologias usadas

- Java 17
- Spring Boot
- Maven
- Spring Data JPA/Hibernate
- Spring Security
- MySQL
- Thymeleaf
- Bootstrap
- API REST
- Arquitetura MVC

## Como estudar este projeto

As explicacoes estao na pasta `docs`.

Comece por aqui:

1. [Parte 1 - Preparacao, estrutura e MySQL](docs/parte-1-preparacao.md)
2. [Parte 2 - DER e entidades JPA](docs/parte-2-der-entidades.md)
3. [Parte 3 - API REST](docs/parte-3-api-rest.md)
4. [Parte 4 - MVC, Services, Controllers e Security](docs/parte-4-mvc-security-telas.md)

## Estrutura principal

```text
src/main/java/br/org/conectageracaoviva
|-- config       -> classes de configuracao e dados iniciais
|-- model        -> entidades JPA, que viram tabelas no banco
|-- repository   -> acesso ao banco de dados
|-- security     -> configuracao de login, senha e perfis
|-- service      -> regras de negocio
`-- GeracaoVivaApplication.java -> classe que inicia o sistema

src/main/resources
|-- templates              -> paginas HTML Thymeleaf
`-- application.properties -> configuracao do projeto e do MySQL
```

## Como executar

Antes de rodar, crie o banco no MySQL:

```sql
CREATE DATABASE geracao_viva CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Depois ajuste seu usuario e senha em:

```text
src/main/resources/application.properties
```

Execute com:

```bash
mvn spring-boot:run
```

Abra no navegador:

```text
http://localhost:8080
```
