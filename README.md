# ⚔️Guilda de Aventureiros

![wallpaper](https://hackmd.io/_uploads/B1Pg50wwWl.png)

Projeto desenvolvido com **Spring Boot** utilizando **PostgreSQL** como banco de dados containerizado via Docker.

## 💻Tecnologias Utilizadas

- Java 25
- Maven 3.9.11
- Spring Boot 4.0.3
- Spring Data JPA 4.0.3
- Elasticsearch
- PostgreSQL
- Docker & Docker Compose

## 📂Estrutura do Projeto

```shell
src/
├───main
│   ├───java
│   │   └───br
│   │       └───com
│   │           └───infnet
│   │               └───guildaaventureiro
│   │                   ├───advice
│   │                   ├───controller
│   │                   │   └───elastic
│   │                   ├───domain
│   │                   │   ├───audit
│   │                   │   │   └───enums
│   │                   │   ├───elastic
│   │                   │   └───operacoes
│   │                   │       └───enums
│   │                   ├───dto
│   │                   │   ├───aventureiro
│   │                   │   ├───companheiro
│   │                   │   ├───elastic
│   │                   │   ├───missao
│   │                   │   │   └───enums
│   │                   │   └───relatorio
│   │                   ├───exception
│   │                   ├───mapper
│   │                   ├───repository
│   │                   │   ├───audit
│   │                   │   └───operacoes
│   │                   ├───scheduler
│   │                   └───service
│   │                       └───elastic
│   └───resources
│       └───elasticsearch
└───test
    └───java
        └───br
            └───com
                └───infnet
                    └───guildaaventureiro
                        ├───config
                        ├───repository
                        │   ├───audit
                        │   └───operacoes
                        └───service
                            └───elastic
```

## 📦Dependências

- Spring Boot Web
- Spring Data JPA
- Spring Boot Cache
- Spring Boot Validation
- Spring Boot Test
- Spring Boot Elasticsearch
- Lombok
- PostgreSQL Driver
- Jackson Databind
- Hypersistence Utils

## ▶️Como executar o projeto

1. Clonar o repositório

```shell
git clone https://github.com/rodrigo-cloureiro/guildaaventureiro-tp3.git
cd guildaaventureiro-tp3
```

2. Subir o banco com Docker
   > ⚠️ **Aviso importante**
   >
   > **A [imagem docker](https://hub.docker.com/r/leogloriainfnet/postgres-tp2-spring/tags) para realização do TP3 está
   disponível, até o momento, apenas na versão para macOS (arm64). Para utilizá-la no Windows, é necessário executar o
   container com a flag –platform linux/arm64.**

    1. Baixar a imagem
    ```shell
    docker pull leogloriainfnet/postgres-tp2-spring:2.0-mac
    ```

    2. Criar o container
    ```shell
   # Para Windows
   docker run --platform linux/arm64 -d --name <NOME_CONTAINER> -p 5432:5432 leogloriainfnet/postgres-tp2-spring:2.0-mac

   # Para macOS
   docker run -d --name <NOME_CONTAINER> -p 5432:5432 leogloriainfnet/postgres-tp2-spring:2.0-mac
    ```

    3. Acessar o terminal bash do container
   ```shell
   docker exec -it postgres-tp3-spring bash
   ```

    4. Conectar com o usuário postgres
   ```shell
   psql -U postgres
   ```

    5. Modificar a senha do usuário _postgres_
   ```shell
   ALTER USER postgres WITH password '<NOVA_SENHA>';
   ```

3. Subir o Elasticsearch com Docker
```shell
# Para Windows
docker run -d \
   --name guilda-es \
   -p 9200:9200 \
   -e ES_JAVA_OPTS="-Xms512m -Xmx512m" \
   elastic-tp2-spring:1.0-windows

# Para macOS
docker run -d \
   --name guilda-es \
   -p 9200:9200 \
   -e ES_JAVA_OPTS="-Xms512m -Xmx512m" \
   elastic-tp2-spring:1.0-mac
```

4. Executar a aplicação

```shell
mvn spring-boot:run
# Ou
./mvnw spring-boot:run
```

5. Acessar aplicação

```shell
http://localhost:8080
```

### 🧪 Testes

Para executar os testes:

```shell
mvn test
```

## ▶️Como executar o projeto com Docker Compose

1. Clonar o repositório

```shell
git clone https://github.com/rodrigo-cloureiro/guildaaventureiro-tp3.git
cd guildaaventureiro-tp3
```

2. Entrar na pasta docker/

```shell
cd docker/
```

3. Subir tudo com Docker Compose

```shell
docker compose up --build
```

## Autor

- [rodrigo-cloureiro](https://github.com/rodrigo-cloureiro)