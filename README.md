# Conecta Geração Viva

Sistema web em Java com Spring Boot para a ONG Conecta Geração Viva.

Este projeto foi desenvolvido para digitalizar processos internos da ONG e aproximar a comunidade das atividades oferecidas.

## Requisitos

- Java 17 ou superior
- Maven
- MySQL
- VS Code ou outra IDE Java

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

## Banco de dados

O script do banco está na pasta `docs`:

- [banco-de-dados.sql](docs/banco-de-dados.sql)

## Estrutura principal

```text
src/main/java/br/org/conectageracaoviva
|-- config       -> classes de configuração e dados iniciais
|-- model        -> entidades JPA, que viram tabelas no banco
|-- repository   -> acesso ao banco de dados
|-- security     -> configuração de login, senha e perfis
|-- service      -> regras de negócio
`-- GeracaoVivaApplication.java -> classe que inicia o sistema

src/main/resources
|-- templates              -> páginas HTML Thymeleaf
`-- application.properties -> configuração do projeto e do MySQL
```

## Como executar

Antes de rodar, crie o banco no MySQL:

```sql
CREATE DATABASE geracao_viva CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Configure sua senha local em:

```text
src/main/resources/application-local.properties
```

Exemplo:

```properties
spring.datasource.password=sua_senha_mysql
```

Execute com:

```bash
mvn spring-boot:run
```

Abra no navegador:

```text
http://localhost:8080
```
