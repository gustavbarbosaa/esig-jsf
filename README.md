# esig-jsf

## Estrutura do Projeto

O projeto segue a estrutura padrão de aplicações Java EE baseadas em JSF:

```
esig-jsf/
├── src/
│   ├── main/
│   │   ├── java/         # Código-fonte Java (beans, services, models, security, config, dao)
│   │   └── webapp/       # Recursos web
│   │       ├── WEB-INF/  # Configurações (faces-config.xml, web.xml)
│   │       └── pages/    # Páginas JSF (.xhtml)
├── pom.xml               # Gerenciamento de dependências Maven
```

## Bibliotecas e Tecnologias Utilizadas

- **Java** (JDK 8+)
- **Java Server Faces (JSF)** – Framework principal para construção das views web.
- **Maven** – Gerenciamento de dependências e build.
- **Jakarta EE / Java EE** – Suporte ao ciclo de vida da aplicação web.
- **PrimeFaces** – Biblioteca de componentes ricos para JSF (verifique no `pom.xml` se está presente).
- **Servlet Container** (Tomcat, WildFly, Payara, etc.) – Necessário para execução da aplicação.
- **JPA / Hibernate** – Para persistência dos dados.

## Configurações Importantes

- **web.xml**: Configuração do servlet do JSF, parâmetros de contexto, filtros, listeners.
- **pom.xml**: Todas as dependências e plugins Maven estão definidos aqui.

## Como Executar

1. Faça o build do projeto com Maven:
   ```bash
   mvn clean package
   ```
2. Desdobre o `.war` gerado no servidor de aplicação de sua escolha.
3. Acesse via navegador:
   ```
   http://localhost:8080/esig-jsf
   ```