# SysRestaurant

Sistema de gestão de restaurante desenvolvido na UDESC. Cobre o ciclo completo do atendimento: o cliente chega e ocupa uma mesa, faz e altera pedidos, os pedidos são entregues, o cliente paga e a mesa é liberada quando todos os clientes dela tiverem pago.

Monorepo com duas aplicações independentes:

| Pasta | Descrição | Stack |
|---|---|---|
| [`backend/`](backend) | API REST (mesas, clientes, funcionários, pratos, ingredientes, pedidos e itens de pedido) | Java 25, Spring Boot 4, Spring Data JPA, PostgreSQL, Gradle |
| [`frontend/`](frontend) | SPA "Comanda": CRUD de cada entidade e telas do fluxo de atendimento | React 19, TypeScript, Vite, React Router |

## Pré-requisitos

- **JDK 25** (o Gradle usa toolchain Java 25; o wrapper `./gradlew` já vem no repositório, não precisa instalar o Gradle)
- **PostgreSQL** em execução na porta `5432`
- **Node.js** e **npm** (versão compatível com Vite 8, ou seja, Node 20.19+ ou 22.12+)

## Como executar

### 1. Banco de dados

Crie um banco PostgreSQL com as credenciais esperadas pela configuração padrão:

| Banco | Usuário | Senha | Porta |
|---|---|---|---|
| `sysrestaurant` | `root` | `root` | `5432` |

Exemplo com Docker:

```bash
docker run -d --name sysrestaurant-db \
  -e POSTGRES_DB=sysrestaurant \
  -e POSTGRES_USER=root \
  -e POSTGRES_PASSWORD=root \
  -p 5432:5432 postgres
```

Para usar outras credenciais, altere `backend/src/main/resources/application.properties`.

O schema é criado e atualizado automaticamente pelo Hibernate (`ddl-auto=update`), e na primeira execução o `DataSeeder` popula o banco com dados de teste (somente se a tabela `mesa` estiver vazia). Para apagar o banco ou gerar mais dados de teste depois, use a engrenagem no canto inferior esquerdo do frontend (ou `POST /api/admin/limpar-dados` e `POST /api/admin/dados-teste`); essas rotas dependem de `app.dev-tools.enabled=true` e são destrutivas, então desabilite-as fora do desenvolvimento.

Observação: caso exista um postgres instalado na máquina rodando, é necessário finalizar a execução dele e deixar apenas o docker rodando. Caso contrário, as portas irão conflitar.
E o trabalho **não irá** funcionar.

### 2. Backend

```bash
cd backend
./gradlew bootRun
```

A API sobe em `http://localhost:8080`. A documentação interativa (Swagger UI, via springdoc) fica em `http://localhost:8080/swagger-ui.html`.

### 3. Frontend

Em outro terminal:

```bash
cd frontend
npm install
npm run dev
```

O Vite exibe no terminal o endereço da aplicação (por padrão `http://localhost:5173`).

Se o backend rodar em outra URL ou porta, ajuste `VITE_API_URL` no `.env`.

## Comandos úteis

**Backend** (dentro de `backend/`)

```bash
./gradlew test      # executa os testes
./gradlew build     # compila e empacota o .jar
```

**Frontend** (dentro de `frontend/`)

```bash
npm run build       # verificação de tipos + build de produção (gera dist/)
npm run preview     # serve o build de produção localmente
npm run lint        # ESLint
```
