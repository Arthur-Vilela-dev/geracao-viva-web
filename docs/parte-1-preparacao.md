# Parte 1 - Preparacao, estrutura e MySQL

Nesta primeira parte vamos entender o que ja existe no projeto e como preparar o ambiente para executar o sistema.

## 1. O que e este sistema?

O sistema sera uma aplicacao web para a ONG Conecta Geracao Viva.

Ele tera:

- paginas web para usuarios acessarem pelo navegador;
- login com perfis de acesso;
- cadastro de criancas, responsaveis, voluntarios, oficinas, eventos, doacoes e visitas;
- API REST para algumas funcionalidades;
- banco de dados MySQL.

## 2. O que voce precisa instalar

Para desenvolver no VS Code, instale:

- Java JDK 17;
- VS Code;
- extensao "Extension Pack for Java" no VS Code;
- extensao "Spring Boot Extension Pack" no VS Code;
- MySQL Server;
- MySQL Workbench ou outro cliente para ver o banco;
- Postman para testar a API.

## 3. Como criar um projeto Spring Boot do zero

Se voce fosse criar este projeto do zero, o caminho mais comum seria:

1. Entrar no site `https://start.spring.io`.
2. Escolher Maven.
3. Escolher Java.
4. Escolher Spring Boot.
5. Informar:
   - Group: `br.org.conectageracaoviva`
   - Artifact: `geracao-viva`
   - Java: `17`
6. Adicionar as dependencias:
   - Spring Web;
   - Thymeleaf;
   - Spring Data JPA;
   - Spring Security;
   - Validation;
   - MySQL Driver;
   - Spring Boot DevTools.
7. Baixar o arquivo `.zip`.
8. Extrair a pasta.
9. Abrir a pasta no VS Code.

Neste projeto, essa base ja existe no arquivo `pom.xml`.

## 4. O que e o Maven?

O Maven e a ferramenta que cuida das dependencias do projeto.

Dependencia e uma biblioteca pronta que o nosso sistema usa. Por exemplo:

- `spring-boot-starter-web`: permite criar paginas e endpoints;
- `spring-boot-starter-data-jpa`: permite acessar o banco com JPA/Hibernate;
- `mysql-connector-j`: permite conectar Java com MySQL;
- `spring-boot-starter-security`: permite criar login e controle de acesso.

O arquivo principal do Maven e:

```text
pom.xml
```

## 5. Estrutura das pastas

O codigo Java fica aqui:

```text
src/main/java/br/org/conectageracaoviva
```

As paginas HTML ficam aqui:

```text
src/main/resources/templates
```

A configuracao do projeto fica aqui:

```text
src/main/resources/application.properties
```

## 6. Pacotes do projeto

O projeto esta organizado assim:

```text
config
```

Classes de configuracao. Exemplo: criar dados iniciais no banco.

```text
model
```

Entidades JPA. Cada entidade representa uma tabela do banco.

Exemplo: a classe `Voluntario` vira uma tabela de voluntarios.

```text
repository
```

Interfaces que acessam o banco de dados.

Elas evitam que a gente escreva SQL manual para tarefas simples.

```text
service
```

Classes que guardam as regras de negocio.

Exemplo: verificar se uma oficina ainda tem vagas antes de matricular uma crianca.

```text
security
```

Classes de seguranca: login, senha criptografada e permissoes por perfil.

## 7. Configurando o MySQL

Abra o MySQL Workbench e execute:

```sql
CREATE DATABASE geracao_viva CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Esse comando cria o banco de dados chamado `geracao_viva`.

Depois confira o arquivo:

```text
src/main/resources/application.properties
```

Ele tem esta configuracao:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/geracao_viva?useSSL=false&serverTimezone=America/Sao_Paulo&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=senha_do_mysql
```

Troque `senha_do_mysql` pela senha do seu MySQL.

Exemplo:

```properties
spring.datasource.password=123456
```

Se o seu usuario do MySQL nao for `root`, troque tambem:

```properties
spring.datasource.username=seu_usuario
```

## 8. Entendendo o ddl-auto

No arquivo `application.properties`, existe esta linha:

```properties
spring.jpa.hibernate.ddl-auto=update
```

Ela faz o Hibernate criar ou atualizar as tabelas automaticamente.

Isso e util para estudo, porque voce altera uma entidade Java e o banco acompanha.

Em sistemas profissionais, o ideal e usar ferramentas como Flyway ou Liquibase, mas para aprender vamos manter `update`.

## 9. Como executar pelo terminal do VS Code

No VS Code:

1. Abra a pasta do projeto.
2. Abra o terminal.
3. Execute:

```bash
mvn spring-boot:run
```

Se tudo estiver certo, o Spring Boot vai iniciar na porta `8080`.

Depois abra:

```text
http://localhost:8080
```

## 10. Como saber se deu certo

Voce deve ver mensagens parecidas com:

```text
Tomcat started on port 8080
Started GeracaoVivaApplication
```

Se aparecer erro de senha do MySQL, revise:

```text
spring.datasource.username
spring.datasource.password
```

Se aparecer erro dizendo que o banco nao existe, execute novamente:

```sql
CREATE DATABASE geracao_viva CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

## 11. Erro comum: mvn nao e reconhecido

Se ao executar `mvn spring-boot:run` aparecer uma mensagem parecida com:

```text
mvn nao e reconhecido como nome de cmdlet
```

isso significa que o Maven nao esta instalado ou nao foi adicionado ao PATH do Windows.

Para resolver:

1. Instale o Apache Maven.
2. Configure a variavel de ambiente `MAVEN_HOME`.
3. Adicione a pasta `bin` do Maven ao `Path`.
4. Feche e abra novamente o VS Code.
5. Teste no terminal:

```bash
mvn -v
```

Se aparecer a versao do Maven, o comando esta funcionando.

## 12. Proxima parte

Na Parte 2 vamos estudar as entidades JPA e o DER:

- Usuario;
- Perfil;
- Voluntario;
- Crianca;
- Responsavel;
- Oficina;
- Matricula;
- Evento;
- Doacao;
- Visita.

Tambem vamos explicar os relacionamentos:

- 1:1;
- 1:N;
- N:N.
