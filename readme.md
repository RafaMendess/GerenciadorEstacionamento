
# 🚗 Sistema de Estacionamento em Java

## 🎯 Objetivo
Este projeto tem como objetivo desenvolver um **sistema de estacionamento** utilizando **Java** e os princípios da **Programação Orientada a Objetos (POO)**.  
O sistema funciona via **menu no console**, permitindo registrar entrada e saída de veículos, controlar vagas, gerenciar fila de espera e gerar relatórios simples — tudo sem banco de dados.

Este trabalho foi desenvolvido como parte da disciplina de *Programação de Soluções Computacionais*.

---

## ⚙️ Funcionalidades Principais

### 🔸 Funcionalidades Obrigatórias
- Registrar **entrada de veículo**
- Registrar **saída de veículo**
- Calcular **tempo de permanência** e **valor a pagar**
- Controle de **vagas disponíveis e ocupadas**
- Listar **veículos presentes**
- Pesquisar veículo por **placa**
- Gerar **relatório de faturamento total**

---

## 🧩 Funcionalidades Extras
- Suporte a **dois tipos de veículos**:
    - **Carro** — 1ª hora: R$12 / adicional: R$8
    - **Moto** — 1ª hora: R$8 / adicional: R$5
- **Fila de espera** quando não há vagas:
    - O primeiro da fila entra automaticamente ao liberar vaga
- Arredondamento de permanência para cima  
  Exemplo: 1h01m → 2h cobradas
- Validação de placa e prevenção de entradas duplicadas

---

## 💰 Regras de Cobrança

| Tipo | Primeira hora | Adicional |
|------|---------------|-----------|
| Carro | R$12 | R$8 |
| Moto | R$8 | R$5 |

---

## 🧱 Estrutura do Projeto

```

src/
├── Main.java
│
├── view/
│   └── Menu.java
│
├── models/
│   ├── EntradaStatus.java
│   ├── Estacionamento.java
│   ├── Faturamento.java
│   ├── TipoVeiculo.java
│   ├── Vaga.java
│   └── Veiculo.java
│
└── controllers/
    ├── EstacionamentoController.java
    └── FaturamentoController.java


````

---

## ▶️ Como Executar

### Compilar:
```bash
javac Main.java
````

### Executar:

```bash
java Main
```

---

## 📋 Menu do Sistema

```
=== SISTEMA DE ESTACIONAMENTO ===
1. Registrar entrada de veículo
2. Registrar saída de veículo
3. Vagas disponíveis
4. Listar veículos presentes
5. Pesquisar veículo por placa
6. Relatório de faturamento
7. Fila de espera
0. Sair
```

---

## 📈 Relatórios Disponíveis

### ✔️ Faturamento

* Total arrecadado
* Quantidade de veículos atendidos
* Valores por tipo de veículo

### ✔️ Veículos Presentes

* Placa
* Tipo
* Hora de entrada

### ✔️ Fila de Espera

* Lista de placas aguardando vaga

---

## 🛠️ Tecnologias Utilizadas

* **Java 21+**
* Programação Orientada a Objetos (POO)
* APIs do Java:

    * `ArrayList`
    * `LinkedList` / `Queue`
    * `LocalDateTime`
    * `Duration`



## 👨‍💻 Autores
* **Rafael Mendes da Silva** 
* **Eduardo Luiz Gaia de Melo**


Projeto desenvolvido pelos alunos da Unidade Curricular de
**Programação de Soluções Computacionais**.


