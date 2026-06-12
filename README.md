# CoreRH - Sistema de Gestão de Recursos Humanos e Controle de Ponto

![Java](https://img.shields.io/badge/Java-21-orange)
![JavaFX](https://img.shields.io/badge/JavaFX-25-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.0-green)
![Maven](https://img.shields.io/badge/Maven-Build-red)
![Status](https://img.shields.io/badge/Status-Em%20Desenvolvimento-yellow)

## 📋 Sobre o Projeto

O **CoreRH** é um sistema desktop desenvolvido para gerenciamento de Recursos Humanos e Controle de Ponto Eletrônico.

O projeto foi desenvolvido para a Unidade Curricular **Programação de Soluções Computacionais (A3)**, atendendo aos requisitos estabelecidos no edital da disciplina, incluindo:

* Interface gráfica desktop;
* Controle de acesso por autenticação;
* Banco de dados MySQL;
* Múltiplos perfis de usuário;
* Dashboard administrativo;
* Operações CRUD;
* Relatórios gerenciais.

## 🌎 Objetivo de Desenvolvimento Sustentável (ODS)

O projeto está alinhado ao:

**ODS 8 – Trabalho Decente e Crescimento Econômico**

O sistema auxilia organizações no gerenciamento de colaboradores, registro de jornadas de trabalho e controle administrativo, promovendo maior transparência e organização dos processos internos.

## 🚀 Tecnologias Utilizadas

* Java 21
* JavaFX
* MySQL
* Maven
* Scene Builder
* JDBC

## 🏗️ Arquitetura do Sistema

O sistema foi organizado seguindo uma estrutura modular:

```text
src/main/java
│
├── config
│   └── Database.java
│
├── util
│   ├── Usuario.java
│   ├── Cargo.java
│   └── Departamento.java
│
├── view
│   ├── login
│   ├── principal
│   ├── cadastro
│   ├── ponto
│   ├── espelho
│   ├── demostrativoPagamento
│   └── relatorio
```

## 🔐 Controle de Acesso

O sistema possui autenticação obrigatória.

### Administrador

* Cadastro de funcionários
* Edição de funcionários
* Exclusão de funcionários
* Controle completo do sistema
* Visualização de relatórios
* Gestão dos registros de ponto

### Usuário Comum

* Registro de ponto
* Consulta de espelho de ponto
* Consulta de demonstrativos
* Acesso limitado

## 📌 Funcionalidades Implementadas

### Login

* Autenticação de usuários
* Controle de permissões

### Dashboard

* Tela principal administrativa
* Navegação entre módulos

### Cadastro de Funcionários

CRUD completo:

* Criar funcionário
* Consultar funcionário
* Atualizar funcionário
* Excluir funcionário

### Controle de Ponto

* Registro de entrada
* Registro de saída
* Controle de jornada

### Espelho de Ponto

* Consulta de registros
* Alteração de informações autorizadas

### Demonstrativo de Pagamento

* Cadastro
* Consulta
* Alteração
* Exclusão

### Relatórios

* Emissão de relatórios administrativos
* Consulta consolidada de informações

## 📊 CRUDs Implementados

O edital exige no mínimo 3 CRUDs.

CRUDs presentes no sistema:

1. Funcionários
2. Espelho de Ponto
3. Demonstrativo de Pagamento

## 🗄️ Banco de Dados

Banco utilizado:

**MySQL**

Script disponível em:

```text
SQLFILES/sql final.sql
```

Administrador cadastrado diretamente no banco de dados, conforme solicitado no edital.

## 🖥️ Telas do Sistema

### Login

* Login.fxml

### Dashboard

* principal.fxml

### Cadastro de Funcionários

* Cadastro.fxml

### Registro de Ponto

* MarcaPonto.fxml

### Espelho de Ponto

* EspelhoPonto.fxml

### Demonstrativo de Pagamento

* DemostrativoPagamento.fxml

### Relatórios

* Relatorio.fxml

## 📂 Estrutura do Projeto

```text
CoreRH
│
├── src
├── SQLFILES
├── resources
├── pom.xml
├── CoreRh.exe
├── video do software.mp4
└── README.md
```

## ▶️ Como Executar

### 1. Clonar o Repositório

```bash
git clone https://github.com/Devolvera1/A3.git
```

### 2. Configurar o Banco de Dados

* Instalar MySQL
* Criar banco de dados
* Executar:

```sql
SQLFILES/sql final.sql
```

* Configurar usuário e senha no arquivo:

```java
config/Database.java
```

### 3. Executar o Sistema

Via Maven:

```bash
mvn javafx:run
```

Ou:

* Abrir no IntelliJ IDEA
* Atualizar dependências Maven
* Executar a tela de Login

## 👥 Usuários de Teste

| Perfil        | Usuário     | Senha |
| ------------- | ----------- | ----- |
| Administrador | admin       | 123   |
| Usuário       | alice.silva | 123   |

## 🎥 Demonstração

O projeto contém:

* Vídeo demonstrativo
* Apresentação em PowerPoint
* Executável Windows (.exe)

Arquivos disponíveis na raiz do projeto:

```text
video do software.mp4
apresentaçãoJava.pptx
CoreRh.exe
```

## 📈 Status do Projeto

Em desenvolvimento.

## 👨‍💻 Desenvolvedor

**Guilherme Feitosa Santana**

GitHub:
https://github.com/Devolvera1

Contato:
[Contato.guilhermefsantana@gmail.com](mailto:Contato.guilhermefsantana@gmail.com)

## 📄 Licença

Projeto acadêmico desenvolvido para fins educacionais na Universidade São Judas Tadeu.
