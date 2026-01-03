# Gerenciamento Financeiro API

API RESTful desenvolvida em Java com Spring Boot para controle de finanças pessoais. O sistema permite o gerenciamento de contas, categorias e transações (receitas e despesas), oferecendo recursos avançados de filtragem e autenticação.

## 🚀 Tecnologias Utilizadas

- **Java 17+**
- **Spring Boot 3** (Web, Data JPA, Security, Validation)
- **Maven** (Gerenciamento de dependências)
- **Docker & Docker Compose** (Containerização)
- **Flyway** (Migração de banco de dados)
- **Swagger / OpenAPI** (Documentação da API)
- **PostgreSQL** (Banco de dados relacional)
- **H2 Database** (Banco em memória para dev/testes)
- **JUnit 5 & Mockito** (Testes unitários e de integração)

## 📋 Funcionalidades

- **Autenticação e Segurança**: Login seguro e proteção de rotas.
- **Gerenciamento de Contas**: Criação e visualização de contas bancárias.
- **Gerenciamento de Categorias**: Organização de transações por categorias.
- **Controle de Transações**:
  - Registro de Receitas e Despesas.
  - **Filtros Avançados**:
    - Por Conta (`/conta/{id}`)
    - Por Tipo (`/tipo/RECEITA` ou `/tipo/1`)
    - Por Período (`/periodo?dataInicio=...&dataFim=...`)
    - Combinados (Conta + Tipo)

## ⚙️ Como Executar o Projeto

### Pré-requisitos

- Java JDK 17 ou superior
- Maven
- Docker (Opcional)

### Opção 1: Via Docker (Recomendado)

Se você tiver o Docker e o Docker Compose instalados, basta executar:

```bash
docker-compose up --build
```

A aplicação estará disponível em `http://localhost:8080`.

### Opção 2: Via Maven (Local)

1. Clone o repositório.
2. Compile e baixe as dependências:
   ```bash
   ./mvnw clean install
   ```
3. Execute a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```

## 📚 Documentação da API (Swagger)

Após iniciar a aplicação, você pode acessar a documentação interativa e testar os endpoints diretamente pelo navegador:

- **URL**: `http://localhost:8080/swagger-ui.html`
- **JSON**: `http://localhost:8080/v3/api-docs`

## 🛣️ Principais Endpoints

### Autenticação
- `POST /auth/login` - Realizar login e obter credenciais.

### Transações
- `GET /transacoes` - Listar todas.
- `GET /transacoes/conta/{id}` - Listar por conta específica.
- `GET /transacoes/tipo/{tipo}` - Listar por tipo (Aceita `RECEITA`/`DESPESA` ou `1`/`2`).
- `GET /transacoes/periodo` - Filtrar por intervalo de datas.
- `POST /transacoes` - Criar nova transação.

### Contas
- `GET /contas` - Listar contas.
- `POST /contas` - Criar nova conta.

### Categorias
- `GET /categorias` - Listar categorias.
- `POST /categorias` - Criar nova categoria.

## 🧪 Executando os Testes

O projeto possui testes unitários e de integração (Controller, Service, Repository). Para executá-los:

```bash
./mvnw test
```

## 🗂️ Estrutura do Projeto

```text
src/main/java/com/example/Gerenciamento_Financeiro
├── config/          # Configurações (Swagger, Converters, DataSeeder)
├── controller/      # Controladores REST
├── dto/             # Objetos de Transferência de Dados
├── model/           # Entidades JPA e Enums
├── repository/      # Interfaces de acesso ao banco de dados
├── security/        # Configurações de Segurança e UserDetails
└── services/        # Regras de negócio
```

## 🤝 Contribuição

1. Faça um Fork do projeto
2. Crie uma Branch para sua Feature (`git checkout -b feature/MinhaFeature`)
3. Faça o Commit (`git commit -m 'Adicionando nova feature'`)
4. Faça o Push (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

