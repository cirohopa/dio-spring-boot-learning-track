# API de Orçamento Financeiro com Spring AI 🤖💰

## 📋 O que o projeto faz

O projeto é uma API REST de orçamento e controle financeiro construída em **Spring Boot**, que integra recursos avançados de **Inteligência Artificial (Spring AI)** para oferecer interação por voz com o usuário. 

A aplicação processa comandos de áudio, convertendo a fala em texto, identificando a intenção através de modelos de linguagem (LLM) e executando ações financeiras automáticas no banco de dados via **Tool Calling** (Function Calling). A resposta final gerada pela IA é sintetizada e retornada em áudio (MP3).

### 🔄 Fluxo Principal de Áudio e IA:
1. O usuário envia um arquivo de áudio (`.mp3` ou `.wav`) contendo um comando de voz.
2. A aplicação realiza a transcrição de voz para texto (**Speech-to-Text - STT**) utilizando o modelo `TranscriptionModel` (Whisper).
3. O **Spring AI (`ChatClient`)** analisa o texto transcrito e decide autonomamente qual ferramenta (*Tool*) de negócio deve ser chamada.
4. A ferramenta executa a operação no banco de dados (persistir transação, listar por categoria ou calcular o total).
5. O resultado é processado pelo LLM e a resposta textual final é convertida de voz para áudio (**Text-to-Speech - TTS**) via `TextToSpeechModel`.
6. A API responde com o áudio `.mp3` contendo a resposta sintetizada.

---

## 🚀 Como executar a aplicação

### Pré-requisitos
- Java 21 ou superior
- Chave de API da OpenAI (`OPENAI_API_KEY`)

### Passos:
1. Configure a variável de ambiente com a sua chave da OpenAI:
   - **Linux / macOS:**
     ```bash
     export OPENAI_API_KEY="sua_chave_aqui"
     ```
   - **Windows (PowerShell):**
     ```powershell
     $env:OPENAI_API_KEY="sua_chave_aqui"
     ```
   - **Windows (CMD):**
     ```cmd
     set OPENAI_API_KEY=sua_chave_aqui
     ```

2. Execute a aplicação utilizando o Gradle Wrapper:
   ```bash
   ./gradlew bootRun
   ```

3. A API estará pronta para receber requisições em `http://localhost:8080`.

---

## ✨ Melhoria Implementada (Nova Tool)

Foi implementada a nova ferramenta de IA **"Calcular o Total de Despesas Registradas"** (`calculate-total-expenses`), permitindo que a IA consulte a soma de todos os gastos salvos ao ser questionada pelo usuário.

### 📐 Respeitando DDD e SOLID:
- **Domínio (`domain`):** Atualização do contrato da interface `TransactionRepository` incluindo o método `findAll()`.
- **Aplicação (`application`):** Criação do caso de uso `CalculateTotalExpensesUseCase` encapsulando a regra de soma dos valores das transações e anotado com `@Tool` para expor o método semântico à IA.
- **Infraestrutura (`infrastructure`):** Atualização do repositório JPA e registro do novo caso de uso no `ChatClientBuilder` (`defaultTools`) dentro do `TransactionController`.

---

## 🛠️ Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3**
- **Spring AI:**
  - `TranscriptionModel` (Whisper - Speech to Text)
  - `ChatClient` com suporte a **Tool Calling / Function Calling**
  - `TextToSpeechModel` (OpenAI TTS - Text to Speech)
- **Spring Data JPA & H2 Database**
- **Gradle**
- **JUnit 5 & AssertJ** (para testes de integração com `@SpringBootTest`)
- **Lombok**

---

## 🧪 Como testar o fluxo principal

### 1. Teste de Chamada de Voz (Postman)
- **Método:** `POST`
- **URL:** `http://localhost:8080/transactions/ai`
- **Body:** Escolha a opção `form-data`
  - **Key:** `file` (Altere o tipo de texto para **File**)
  - **Value:** Escolha um arquivo de áudio de teste (ex: `pergunta.mp3`)
- **Exemplos de voz no áudio:**
  - *"Gastei 150 reais no mercado"* ➔ Aciona a tool de salvar transação.
  - *"Quais foram meus gastos com mercado?"* ➔ Aciona a tool de listar por categoria.
  - *"Qual é o total das minhas despesas salvas?"* ➔ Aciona a nova tool de cálculo total.

### 2. Executando os Testes Automatizados
Para rodar os testes de integração do projeto, incluindo o teste da nova tool (`CalculateTotalExpensesUseCaseIT`):

```bash
./gradlew test
```

---

## 🎓 Aprendizados Durante o Desafio

Neste projeto e desafio prático, foram desenvolvidos e consolidados os seguintes aprendizados:
- **Integração com APIs de IA com Spring AI:** Como utilizar o ecossistema Spring AI de forma idiomática e desacoplada dos provedores.
- **Pipeline Completo de Áudio (STT e TTS):** Manipulação de entrada e saída de mídia, recebendo áudio do cliente, convertendo para texto (Whisper) e respondendo com voz sintetizada (Text-to-Speech).
- **Tool Calling (Function Calling):** Capacidade de estender a IA dando a ela a habilidade de executar funções de código nativas em Java com base no contexto da conversa.
- **Preservação da Arquitetura DDD & Princípios SOLID:** Como adicionar inteligência artificial em um projeto Spring Boot profissional sem misturar regras de negócio com controllers ou poluir a camada de domínio com detalhes de framework.
