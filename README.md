# 🏨 Gestão de Hotel
 
API REST para gerenciamento de um hotel, desenvolvida com foco em boas práticas de desenvolvimento backend, organização em camadas, tratamento centralizado de erros e cobertura de testes próxima a cenários reais.
 
---
 
## 🛠️ Tecnologias
 
| Tecnologia | Detalhe |
|---|---|
| Java | 25 |
| Spring Boot | Framework principal |
| Spring Web | Exposição dos endpoints REST |
| Spring Data JPA / Hibernate | Mapeamento objeto-relacional |
| Spring Validation | Validação de dados nas requisições |
| Liquibase | Versionamento do schema do banco de dados |
| PostgreSQL | Banco de dados principal |
| H2 | Banco em memória utilizado nos testes de integração |
| JUnit 5 | Framework de testes |
| Mockito | Mocks nos testes unitários |
| DBUnit / DBRider | Controle de fixtures nos testes de integração |
| Gradle | Gerenciamento de dependências e build |
 
---
 
## 🏗️ Arquitetura e Organização
 
O projeto segue o padrão de **arquitetura em camadas**, com separação clara de responsabilidades:
 
```
Controller  →  Service  →  Repository  →  Banco de Dados
                  ↑
              Domain (entidades e regras de negócio)
```
 
- **Controller** — recebe as requisições HTTP, delega para o service e retorna as respostas
- **Service** — concentra as regras de negócio da aplicação
- **Repository** — abstrai o acesso ao banco de dados via Spring Data JPA
- **Domain / Entity** — entidades JPA com regras de negócio encapsuladas nos próprios objetos
---
 
## 🔄 Mapeamento com Mapper
 
A conversão entre entidades e DTOs é feita através de **classes Mapper dedicadas**, mantendo o desacoplamento entre a camada de domínio e a API. Isso evita expor o modelo interno diretamente nas respostas e centraliza a lógica de transformação em um único lugar.
 
---
 
## ⚠️ Tratamento de Exceções
 
O projeto conta com **exceptions personalizadas** para representar situações de negócio específicas, combinadas com um **GlobalExceptionHandler** centralizado. Dessa forma, todos os erros da aplicação são tratados em um único ponto, retornando respostas padronizadas e com os status HTTP adequados.
 
---
 
## 🗄️ Versionamento do Banco de Dados — Liquibase
 
O schema do banco é gerenciado com **Liquibase**, garantindo rastreabilidade e reprodutibilidade das alterações em qualquer ambiente.
 
- Utiliza **ChangeSets** para criação, manutenção e alteração das tabelas no PostgreSQL
- As migrações ficam em `src/main/resources/db/changelog/`
- Convenção de nomenclatura: `001-descricao-da-mudanca.xml`
- Executadas automaticamente na inicialização da aplicação
- Arquivos organizados respeitando a ordenação das constraints de chave estrangeira (tabelas pai antes das tabelas filhas)
---
 
## ✅ Testes
 
O projeto possui uma estrutura de testes em duas camadas, construída para simular cenários o mais próximo possível do ambiente real.
 
### Testes Unitários
 
Validam as **regras de negócio** da aplicação com execução real de todo o fluxo do service.
 
- Apenas o **repository é mockado** — o restante do comportamento do service é executado de verdade
- Cobrem fluxos de sucesso, validações de negócio e exceções esperadas
- Escritos com **JUnit 5** e **Mockito**
### Testes de Integração
 
Validam o **comportamento completo** da aplicação em conjunto com a camada de persistência.
 
- Utilizam banco de dados **H2 em memória**, configurado no profile de testes
- Os registros são inseridos via **DBUnit / DBRider** com datasets XML, deixando as consultas de repository e controller mais próximas do cenário real
- Cobrem tanto a camada de **Repository** quanto a de **Controller**, validando o fluxo de ponta a ponta
### Fixtures por Domínio
 
Para cada domínio da aplicação existe uma **classe de Fixture** dedicada, responsável pela criação padronizada dos objetos utilizados nos testes. Isso:
 
- Elimina duplicação de código de setup entre as classes de teste
- Facilita a manutenção quando um domínio muda
- Padroniza os cenários e torna os testes mais legíveis e reutilizáveis
---
 
## 📁 Estrutura de Pacotes
 
```
src/
├── main/
│   ├── java/
│   │   └── ...
│   │       ├── controller/
│   │       ├── service/
│   │       ├── repository/
│   │       ├── domain/
│   │       ├── dto/
│   │       ├── mapper/
│   │       └── exception/
│   └── resources/
│       ├── db/changelog/
│       └── application.properties
└── test/
    ├── java/
    │   └── ...
    │       ├── controller/       ← Testes de integração (Controller)
    │       ├── repository/       ← Testes de integração (Repository)
    │       ├── service/          ← Testes unitários
    │       └── fixture/          ← Objetos reutilizáveis por domínio
    └── resources/
        ├── datasets/             ← Fixtures XML para DBUnit/DBRider
        └── application-test.properties
```
 
---
