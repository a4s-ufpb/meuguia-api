# MeuGuiaPB API

API para o sistema MeuGuiaPB. Esta aplicação é construída em Java utilizando o framework Spring Boot e serve como backend para a gestão de guias turísticos e atrações.

# Tecnologias e Dependências

Este projeto utiliza Java 21 e Spring Boot 3

# Configuração

Antes de iniciar a aplicação, é necessário configurar as variáveis de ambiente. O projeto inclui um arquivo de exemplo chamado .env.example.

1. Crie uma cópia do arquivo .env.example e renomeie-o para .env:

```bash
cp .env.example .env
```

2. Edite o arquivo .env com as suas configurações locais (ou utilize os valores padrão para Docker):
```Properties
    API_PORT=8080
    LOG_LEVEL=DEBUG

    # Configuração da Base de Dados
    DB_HOST=db
    DB_PORT=5432
    DB_NAME=meu-guia-db
    DB_USERNAME=postgres
    DB_PASSWORD=postgres

    # Segurança JWT
    JWT_SECRET=a_sua_chave_secreta_aqui
    JWT_EXPIRATION=3600000
```

# Como Inicializar

Existem duas formas principais de executar o projeto: utilizando o Docker Compose (recomendado para ambiente completo) ou via Maven (para desenvolvimento local).

## Opção 1: Docker Compose

Este método levanta a base de dados PostgreSQL e a API automaticamente.

1. Certifique-se de que tem o Docker e o Docker Compose instalados.
2. Garanta que o arquivo .env está presente na raiz da pasta api. 
3. Execute o comando:

```Bash
docker-compose up -d
```
> Isto irá iniciar os serviços db (PostgreSQL 16) e meuguiapb-api na rede meuguiapb-network.

## Opção 2: Execução Local com Maven

Se preferir rodar a aplicação fora do Docker:

1. É necessário ter uma instância do PostgreSQL rodando. Pode usar o Docker para levantar apenas o banco de dados:

```Bash
docker compose up -d db
```

2. No arquivo .env, certifique-se de que DB_HOST está definido como localhost.

3. Utilize o Maven para iniciar a aplicação:
```Bash
mvn spring-boot:run
```
> Observação, é necessário ter o JDK 21 e o Maven instalados localmente.

# Documentação da API

Após a aplicação estar em execução, a documentação da API (gerada pelo SpringDoc/Swagger) deverá estar acessível em:
`` http://localhost:8080/swagger-ui.html ``

(A porta pode variar conforme a configuração definida em API_PORT no arquivo .env)