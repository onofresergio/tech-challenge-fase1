# Tech Challenge - Fase 1

Este repositório contém a especificação da API em **OpenAPI (Swagger)**.

## 📖 Documentação da API

A documentação interativa da API pode ser acessada pelo Swagger UI hospedado no GitHub Pages:

👉 [Visualizar documentação da API](https://onofresergio.github.io/tech-challenge-fase1/)

## 📂 Estrutura do repositório

```
tech-challenge-fase1/
├── .mvn/wrapper/        # Arquivos de configuração do Maven Wrapper
├── collection-postman/  # Coleções do Postman para testes da API
├── init-scripts/        # Scripts de inicialização
├── src/                 # Código-fonte principal da aplicação
├── .gitattributes       # Configurações de atributos do Git
├── .gitignore           # Arquivo para ignorar arquivos/pastas no Git
├── Dockerfile           # Definição da imagem Docker
├── README.md            # Documentação do repositório
├── docker-compose.yaml  # Configuração de serviços com Docker Compose
├── index.html           # Página Swagger UI para visualizar a API
├── mvnw                 # Script Maven Wrapper (Linux/Mac)
├── mvnw.cmd             # Script Maven Wrapper (Windows)
├── openapi.json         # Especificação OpenAPI em formato JSON
└── pom.xml              # Configuração principal do Maven
````
## 🚀 Como usar

1. Clone este repositório:
   ```bash
   git clone https://github.com/onofresergio/tech-challenge-fase1.git

## 🐳 Executando com Docker Compose

Este projeto já possui um arquivo `docker-compose.yaml` que facilita a execução da aplicação em containers.

### Passo a passo

1. Certifique-se de ter o **Docker** e o **Docker Compose** instalados em sua máquina.
    - [Instalar Docker](https://docs.docker.com/get-docker/)
    - [Instalar Docker Compose](https://docs.docker.com/compose/install/)

2. No diretório raiz do projeto, execute:
   ```bash
   docker-compose up --build -d

3. Para remover as imagens geradas pelo comando anterior
   ```bash
    docker-compose down -v