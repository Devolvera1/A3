# CoreRH — Sistema de RH com Controle de Ponto

O **CoreRH** é um sistema desktop para gerenciamento de recursos humanos e controle de ponto eletrônico, desenvolvido com foco em simplicidade, organização e controle de acesso por níveis de permissão.

O sistema permite o cadastro de funcionários, registro de jornadas de trabalho e acompanhamento do espelho de ponto, com distinção entre usuários administradores e comuns.

---

## Tecnologias Utilizadas

*  Java 21
*  JavaFX 25 (Interface Gráfica)
*  MySQL (Banco de Dados)
*  Maven (Gerenciamento de Dependências)
*  Scene Builder (Construção de Interfaces)

---

## Funcionalidades

### Administrador

* CRUD completo de funcionários
* Visualização de todos os registros de ponto
* Controle total sobre o sistema

### Usuário Comum

* Registro de ponto eletrônico
* Consulta do próprio espelho de ponto
* Acesso restrito (sem permissões de edição/exclusão)

### Controle de Jornada

* Identificação automática de status:

  *  Completo
  *  Pendente
  *  Inconsistente

---

## Telas do Sistema

* Tela de Login
* Painel Principal (Admin) 
* Cadastro de Funcionários (CRUD) 
* Espelho de Ponto 
* Registro de Ponto (Usuário) 

---

## Como Executar o Projeto

### 1. Clonar o repositório

```bash
git clone https://github.com/Devolvera1/A3
```

### 2. Configurar o Banco de Dados

* Instale o MySQL
* Crie o banco de dados
* Execute os scripts disponíveis na pasta `SQL FILES`
* Configure as credenciais no projeto Java

---

### 3. Executar o sistema

Via terminal:

```bash
mvn javafx:run
```

Ou:

* Abra no **IntelliJ IDEA**
* Aguarde o Maven baixar as dependências
* Execute a classe principal (`Login`)

---

## Estrutura do Projeto

```
CoreRH/
├── src/
├── resources/
├── SQL FILES/
├── pom.xml
└── README.md
```
---
Logins de Teste
Perfil	E-mail	Senha	Acesso
 Administrador	admin -	123	Acesso Total
 Usuário alice.silva -	123	- Acesso parcial
---

## Status do Projeto

Em desenvolvimento

---

## Licença

Este projeto está sob a licença MIT.

---

## Contato

Email: **[Contato.guilhermefsantana@gmail.com](mailto:Contato.guilhermefsantana@gmail.com)**

---

## Observações

* Usuários não administradores possuem acesso restrito ao sistema 

---

Se quiser, posso dar um próximo passo e deixar isso **nível portfólio top (com badges, gifs, roadmap e arquitetura)** — fica bem forte pra LinkedIn e recrutador.
