# gestao-de-hotel
Simula o gerenciamento de uma hotel.


### Liquibase

O projeto utiliza **Liquibase** para versionamento do schema do banco de dados. As migrações são executadas automaticamente na inicialização da aplicação, garantindo que o banco esteja sempre sincronizado com o código. As migrações ficam em: `src/main/resources/db/changelog/` e seguem o padrão de nomenclatura: `001-descricao-da-mudanca.xml`

### Testes

O projeto possui uma camada de testes estruturada para validar tanto as regras de negócio quanto a integração da aplicação com o banco de dados, buscando simular cenários próximos de um ambiente real.

### Testes Unitários

Os testes unitários foram desenvolvidos com foco nas regras de negócio da aplicação, isolando dependências através de mocks para garantir validações precisas e independentes da infraestrutura externa. Os cenários contemplam fluxos reais da aplicação, incluindo validações, exceções e comportamentos esperados dos services.

### Testes de Integração

Os testes integrados utilizam o banco de dados em memória H2 para validar o comportamento completo da aplicação em conjunto com a camada de persistência. Durante a execução dos testes, as tabelas são criadas temporariamente de forma automática, permitindo verificar consultas, relacionamentos e operações no banco de forma segura e isolada.

### Organização dos Testes

Para melhorar a legibilidade e reduzir duplicação de código, foi implementada uma camada de factories/fixed objects responsável pela criação dos objetos utilizados nos testes. Isso facilita a manutenção, padroniza os cenários e torna os testes mais claros e reutilizáveis.
