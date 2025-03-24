Sistema Gerenciador de Alunos

📌 Sobre o Projeto

Este é um sistema de gerenciamento de alunos desenvolvido em Java seguindo o padrão MVC (Model-View-Controller). O sistema permite cadastrar, listar, editar e remover alunos de um banco de dados.

🛠️ Tecnologias Utilizadas

Java

MySQL

Hibernate/JPA

Swing (para a interface gráfica)

JDBC (para conexão com o banco de dados)

🎯 Funcionalidades

📌 Cadastro de alunos

📋 Listagem de alunos

✏️ Edição de informações dos alunos

❌ Remoção de alunos

🔍 Busca de alunos por nome ou ID

📂 Estrutura do Projeto (MVC)

📦 src
 ┣ 📂 dao
 ┃ ┣ 📜 AlunoDAO.java
 ┃ ┣ 📜 CursoDAO.java
 ┃ ┣ 📜 MatriculasDAO.java
 ┣ 📂 model
 ┃ ┣ 📜 Aluno.java
 ┃ ┣ 📜 Curso.java
 ┃ ┣ 📜 MatriculaCompleta.java
 ┃ ┣ 📜 NotasFaltas.java
 ┣ 📂 controller
 ┃ ┣ 📜 AlunoController.java
 ┃ ┣ 📜 CursoController.java
 ┣ 📂 view
 ┃ ┣ 📜 Interface.java
 ┃ ┣ 📜 InterfacePrincipal.java
 ┣ 📂 util
 ┃ ┣ 📜 ConnectionFactory.java
 ┣ 📜 script.sql
 ┣ 📜 .gitignore
 ┣ 📜 ProjetoCadastroAluno.iml

⚙️ Como Executar o Projeto

Pré-requisitos

Ter o Java JDK instalado

Ter um banco de dados MySQL configurado

Ter um IDE como Eclipse ou IntelliJ

Configurar o arquivo ConnectionFactory.java com as credenciais do banco de dados

Passos para execução

Clone este repositório:

git clone https://github.com/seuusuario/seuprojeto.git

Importe o projeto para sua IDE

Configure as credenciais do banco no arquivo ConnectionFactory.java

Execute o projeto a partir da classe principal (main)

Acesse a interface gráfica para gerenciar os alunos

📝 Melhorias Futuras

📊 Dashboard com estatísticas

📅 Implementação de um calendário acadêmico

📄 Geração de relatórios em PDF

🔑 Autenticação de usuários

📌 Autor

Desenvolvido por Lucas Oliveira Silva

✉️ Caso tenha dúvidas ou sugestões, sinta-se à vontade para entrar em contato!
