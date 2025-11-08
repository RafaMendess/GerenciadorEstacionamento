# 🚗 Sistema de Estacionamento em Java

## 🎯 Objetivo
Este projeto tem como objetivo desenvolver um **sistema de estacionamento** utilizando **Java** e os princípios da **Programação Orientada a Objetos (POO)**.  
O sistema é executado via **menu de console**, permitindo o controle de **entrada e saída de veículos**, cálculo de **valores a pagar** e **relatórios simples**, sem uso de banco de dados.

Este trabalho foi desenvolvido como parte da disciplina de Programação de Soluções Computacionais, conforme as diretrizes do professor.

---

## ⚙️ Funcionalidades Principais

### 🔸 Funcionalidades Obrigatórias
- Registrar **entrada de veículo** (informando placa e hora de entrada — real ou manual)
- Registrar **saída de veículo**
- Calcular **tempo de permanência** e **valor a pagar**
    - Primeira hora: **R$12,00**
    - Hora adicional: **R$8,00**
    - Não há fração de hora (1h01m = 2h)
- Exibir **quantidade de vagas disponíveis e ocupadas**
- Listar **todos os veículos presentes**
- Pesquisar **veículo por placa**
- Gerar **relatório de faturamento total**

---

### 🧩 Funcionalidades Opcionais 
- Diferenciar **tipos de veículos** (carro e moto)
    - Carro: R$12/hora + R$8 por hora adicional
    - Moto: R$8/hora + R$5 por hora adicional
- Controlar **fila de espera** quando o estacionamento estiver cheio
    - O primeiro da fila entra automaticamente quando uma vaga é liberada
- Mostrar **total arrecadado por dia**

---

## 🧱 Estrutura do Projeto (Planejada)

