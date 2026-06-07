# 🏥 Sistema de Agendamento e Telemedicina — Clínicas Populares

![CI Status](https://github.com/Electra115/poo-pbl-grupo-MCP/actions/workflows/ci.yml/badge.svg)

> **Projeto Prático Integrador — Tema 5**  
> Curso: Projeto de Programação | Disciplina: Orientação a Objetos

---

## 📋 Sobre o Projeto

Sistema de **Núcleo de Domínio (Core Domain)** para gestão de agendamentos médicos presenciais e por telemedicina, construído com **Domain-Driven Design (DDD)**, **Orientação a Objetos avançada** e **Test-Driven Development (TDD)**.

O sistema resolve o problema de clínicas populares que precisam:
- Agendar consultas médicas **presenciais ou online (telemedicina)**
- Gerenciar salas e **links de videoconferência**
- Manter **prontuários eletrônicos** dos pacientes
- Emitir **receitas médicas digitais**

---

## 🏗️ Arquitetura — Domain-Driven Design

```
poo-pbl-grupo-MCP/
├── .github/
│   └── workflows/
│       └── ci.yml                  ← Pipeline CI/CD GitHub Actions
│
├── src/
│   ├── domain/                     ← NÚCLEO: Apenas regras de negócio puras
│   │   ├── shared/                 ← Value Objects compartilhados
│   │   │   ├── Cpf.java            ← VO: Valida CPF brasileiro
│   │   │   ├── Email.java          ← VO: Valida formato de e-mail
│   │   │   ├── Crm.java            ← VO: Valida CRM médico (CRM/UF XXXXXX)
│   │   │   └── Periodo.java        ← VO: Intervalo de tempo com conflito
│   │   ├── patient/                ← Bounded Context: Paciente
│   │   │   ├── Paciente.java       ← Entidade + Aggregate Root
│   │   │   ├── Prontuario.java     ← Entidade (pertence ao Aggregate Paciente)
│   │   │   └── PacienteRepository.java ← Interface (contrato do domínio)
│   │   ├── doctor/                 ← Bounded Context: Agenda Médica
│   │   │   ├── Medico.java         ← Entidade + Aggregate Root
│   │   │   └── MedicoRepository.java
│   │   └── appointment/            ← Bounded Context: Atendimento/Faturamento
│   │       ├── Consulta.java       ← Entidade + Aggregate Root
│   │       ├── ReceitaMedica.java  ← Value Object imutável
│   │       ├── LinkVideoconferencia.java ← Value Object
│   │       ├── TipoConsulta.java   ← Enum (VO)
│   │       ├── StatusConsulta.java ← Enum (VO)
│   │       └── ConsultaRepository.java
│   │
│   ├── application/                ← Casos de uso e orquestração
│   │   └── usecases/
│   │       ├── AgendarConsultaUseCase.java
│   │       ├── CadastrarPacienteUseCase.java
│   │       └── CadastrarMedicoUseCase.java
│   │
│   └── infrastructure/             ← Implementações de persistência
│       └── repository/
│           ├── InMemoryPacienteRepository.java
│           ├── InMemoryMedicoRepository.java
│           └── InMemoryConsultaRepository.java
│
├── tests/                          ← Testes TDD (Red → Green → Refactor)
│   ├── domain/
│   │   ├── CpfTest.java
│   │   ├── EmailTest.java
│   │   ├── CrmTest.java
│   │   ├── PeriodoTest.java
│   │   ├── PacienteTest.java
│   │   ├── MedicoTest.java
│   │   ├── ConsultaTest.java
│   │   └── ProntuarioTest.java
│   └── application/
│       └── AgendarConsultaUseCaseTest.java
│
├── pom.xml                         ← Gerenciamento de dependências (Maven)
├── project-meta.json               ← Metadados do projeto
└── README.md
```

---

## 🎯 Conceitos DDD Aplicados

### Bounded Contexts (Contextos Delimitados)

| Contexto | Aggregate Root | Responsabilidade |
|---|---|---|
| **Paciente** | `Paciente` | Dados cadastrais e prontuário eletrônico |
| **Agenda Médica** | `Medico` | Disponibilidade e agenda do médico |
| **Atendimento** | `Consulta` | Ciclo de vida da consulta e receita digital |

### Value Objects (Imutáveis, sem identidade)

| Classe | Regra Encapsulada |
|---|---|
| `Cpf` | Validação de CPF brasileiro (algoritmo oficial) |
| `Email` | Validação de formato RFC de e-mail |
| `Crm` | Formato `CRM/UF XXXXXX` |
| `Periodo` | Intervalo de tempo + detecção de conflito |
| `ReceitaMedica` | Receita digital imutável após emissão |
| `LinkVideoconferencia` | URL válida para chamada de vídeo |

### Entidades (Com identidade própria)

- **`Paciente`** — identidade por UUID, encapsula dados sensíveis (privacidade DDD)
- **`Medico`** — identidade por UUID, controla disponibilidades
- **`Consulta`** — identidade por UUID, máquina de estados (AGENDADA → CONFIRMADA → REALIZADA)
- **`Prontuario`** — identidade por UUID, acesso somente via Aggregate Root Paciente

---

## 🧪 TDD — Ciclo Red → Green → Refactor

O desenvolvimento seguiu estritamente o ciclo TDD:

1. **🔴 RED** — Teste escrito antes, CI falha
2. **🟢 GREEN** — Implementação mínima para o teste passar
3. **🔵 REFACTOR** — Código limpo mantendo testes verdes

### Cobertura de Cenários

Cada classe de domínio possui testes cobrindo:
- ✅ Cenários de sucesso (caminho feliz)
- ❌ Cenários de falha com validações de regra de negócio
- 🔒 Imutabilidade de Value Objects
- 🔁 Ciclo de estados das entidades

---

## ⚙️ Como Executar

### Pré-requisitos
- Java 17+
- Maven 3.8+

### Rodar os testes
```bash
mvn clean test
```

### Build completo
```bash
mvn clean compile
```

---

## 🚀 CI/CD — GitHub Actions

A cada `push` ou `pull_request` na branch `main`, o pipeline executa automaticamente:

1. Checkout do código
2. Configuração do JDK 17
3. Build com Maven (`mvn clean compile`)
4. Execução de todos os testes unitários (`mvn test`)
5. Upload do relatório de testes como artefato

---

## 👩‍💻 Integrante

| Nome | GitHub |
|---|---|
| Maria Clara Gonçalves Pedroso | [@Electra115](https://github.com/Electra115) |
