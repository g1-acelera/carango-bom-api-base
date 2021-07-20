# Java API do Carango Bom

### Passos de configuração do projeto para rodar localmente

Quando baixar o projeto, rode ```mvn clean install compile``` no terminal (ou via IDE) para gerar um arquivo **.JAR** novo na pasta **target** do projeto.

Baixe e instale e o PostgreSQL na sua máquina local. Tenha certeza de que o banco está rodando em localhost e na porta 5432.

Rode ```mvn spring-boot:run``` no terminal para subir a aplicação Java ou (via IDE, e.g., Eclipse)).

### Passos de configuração do projeto para rodar com Docker

Baixe instale o docker

Rode ```make run-on-docker```
