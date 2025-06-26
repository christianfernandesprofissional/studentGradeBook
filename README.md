
# Student Gradebook - Sistema de Controle de Notas

![alt text](https://img.shields.io/badge/Java-23-blue.svg)


![alt text](https://img.shields.io/badge/Spring%20Boot-3.4.0-brightgreen.svg)


![alt text](https://img.shields.io/badge/Maven-4.0.0-orange.svg)


![alt text](https://img.shields.io/badge/license-MIT-blue.svg)

Este projeto é uma aplicação web desenvolvida com Spring Boot, criada com o principal objetivo de estudar e aplicar conceitos avançados de testes unitários e de integração. A aplicação simula um sistema de gerenciamento de notas de estudantes (Gradebook), permitindo operações de CRUD (Criar, Ler, Atualizar, Deletar) para alunos e suas respectivas notas em diferentes matérias.

## 🎯 Foco Principal: Testes Abrangentes

O coração deste projeto não está apenas na sua funcionalidade, mas na robusta suíte de testes desenvolvida para garantir a qualidade e a corretude do código. O principal objetivo foi desenvolver habilidades em:

Testes de Integração da Camada de Serviço: Validar a lógica de negócio e a interação com a camada de persistência de dados.

Testes da Camada de Controle (Controller): Utilizar MockMvc para simular requisições HTTP e validar as respostas, o roteamento e a renderização das views.

Mocking de Dependências: Usar Mockito (@Mock) para isolar as camadas durante os testes, garantindo que os testes de controller, por exemplo, não dependam do comportamento real da camada de serviço.

Setup e Teardown de Banco de Dados de Teste: Utilizar um banco de dados em memória (H2) e scripts SQL (@Sql, JdbcTemplate) para popular e limpar o banco antes e depois de cada teste, garantindo um ambiente de teste limpo e previsível.

## ✨ Principais Funcionalidades

Listar todos os estudantes cadastrados.

Adicionar um novo estudante ao sistema.

Deletar um estudante e todas as suas notas associadas.

Visualizar informações detalhadas de um estudante, incluindo suas notas em:

Matemática (Math)

Ciências (Science)

História (History)

Adicionar novas notas para um estudante em uma matéria específica.

Deletar uma nota específica de um estudante.

## 🛠️ Tecnologias Utilizadas
Backend

Java 23

Spring Boot 3.4.0

Spring MVC: Para a arquitetura de controllers e o padrão MVC.

Spring Data JPA: Para a camada de persistência de dados e abstração do repositório.

Thymeleaf: Como motor de templates para renderizar as páginas web.

Banco de Dados

MySQL: Conector para o ambiente de "produção".

H2 Database: Banco de dados em memória utilizado para os testes automatizados.

Testes

JUnit 5: Framework principal para a escrita dos testes.

Spring Boot Test: Para carregar o contexto da aplicação e facilitar testes de integração.

Mockito: Para a criação de mocks e stubs de dependências.

MockMvc: Para testar os endpoints da API REST/Controllers sem a necessidade de um servidor web real.

Build & Dependências

Apache Maven

## 📂 Estrutura do Projeto

O projeto segue uma arquitetura em camadas, separando as responsabilidades:


com.studentGradeBook 
<br>├── controller/         # GradebookController: Gerencia as requisições HTTP e a interação com o usuário.<br>├── models/             # Entidades JPA (CollegeStudent, MathGrade, etc.) e DTOs.<br>├── repository/         # Interfaces (DAOs) que estendem JpaRepository para acesso ao banco de dados.<br>└── service/            # StudentAndGradeService: Contém a lógica de negócio da aplicação.<br>


A suíte de testes está localizada em src/test/java e espelha a estrutura do código-fonte.

## 🚀 Como Executar o Projeto

### **Pré-requisitos**

*   **JDK 23** ou superior.
*   **Apache Maven** instalado e configurado no PATH.
*   **Git** para clonar o repositório.
*   (Opcional) Um servidor **MySQL** para executar fora do ambiente de teste.

### **Passo a Passo**

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/christianfernandesprofissional/studentGradeBook.git
    cd studentGradeBook
    ```

2.  **Configuração do Banco de Dados:**
    *   O projeto está configurado para usar o banco de dados em memória **H2** para os testes (veja `src/test/resources/application-test.properties`).
    *   Para executar a aplicação principal, você pode precisar configurar a conexão com o seu banco de dados MySQL no arquivo `src/main/resources/application.properties`.

3.  **Executando os Testes:**
    Para validar que tudo está funcionando corretamente, execute a suíte de testes com o Maven:
    ```bash
    mvn test
    ```

4.  **Iniciando a Aplicação:**
    Use o plugin do Spring Boot para iniciar o servidor web embarcado:
    ```bash
    mvn spring-boot:run
    ```

5.  **Acessando a Aplicação:**
    Após iniciar, a aplicação estará disponível no seu navegador em:
    [http://localhost:8080](http://localhost:8080)
    
## Autor

Christian Fernandes do Carmo

GitHub: @christianfernandesprofissional
