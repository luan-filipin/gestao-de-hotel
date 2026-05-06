# gestao-de-hotel
Simula o gerenciamento de uma hotel


## Liquibase

O projeto utiliza **Liquibase** para versionamento do schema do banco de dados. As migrações são executadas automaticamente na inicialização da aplicação, garantindo que o banco esteja sempre sincronizado com o código. As migrações ficam em: `src/main/resources/db/changelog/` e seguem o padrão de nomenclatura: `001-descricao-da-mudanca.xml`
