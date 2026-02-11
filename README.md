# SmartWallet: Financial Education SaaS 🚀

O **SmartWallet** é uma plataforma de gestão de ativos e educação financeira que utiliza Inteligência Artificial para transformar dados de carteira em insights preditivos e educativos. O projeto une rigor técnico de *Compliance* com modelos avançados de *Data Science*.

---

## 📌 Proposta de Valor
Diferente de gerenciadores de carteira genéricos, o SmartWallet foca no **Investimento Consciente**. Através da **QuestAPI (IA)**, o sistema identifica o perfil de adequação (*Suitability*) e educa o usuário sobre os fundamentos de seus ativos, mitigando riscos e promovendo literacia financeira.

---

## 🏗️ Arquitetura do Sistema (Visão Macro)

O ecossistema é desenhado para ser escalável e modular, dividindo-se em três pilares:

### 1. Core Engine (Backend)
* **Tecnologia:** Java (Spring Boot)
* **Banco de Dados:** PostgreSQL
* **Responsabilidade:** Autenticação segura, gestão de usuários, persistência de dados e lógica de *Compliance/PLD*.
* **Segurança:** Implementação de termos de consentimento LGPD e hashing de credenciais.

### 2. Brain Unit (AI/Python)
* **Tecnologia:** Python (FastAPI/Flask)
* **QuestAPI:** Algoritmo de scoring para definição de perfil de investidor (15 variáveis).
* **InsightPremium:** Agente de IA que processa notícias em tempo real e aplica modelos preditivos para gerar relatórios educativos fundamentados.

### 3. Interface (Frontend)
* **Tecnologia:** React + Vite / Tailwind CSS
* **Dashboard:** Visualização intuitiva de alocação de ativos e saúde financeira com foco em UX/UI.

---

## 🛠️ Roadmap de Desenvolvimento

- [x] Definição de Fluxograma e Lógica de Negócio.
- [ ] Implementação do Core Backend (Auth & Users).
- [ ] Integração com Banco de Dados PostgreSQL.
- [ ] Desenvolvimento da QuestAPI (Motor de Perfil em Python).
- [ ] MVP do Dashboard (Interface React).
- [ ] Lançamento do Módulo InsightPremium.

---

## ⚖️ Compliance & Legal
Este software foi projetado seguindo as diretrizes de:
* **LGPD:** Consentimento explícito de tratamento de dados.
* **CVM (Suitability):** Questionário obrigatório para alinhamento de risco.
* **Disclaimer:** Todo conteúdo gerado pela IA é estritamente educativo e não constitui recomendação direta de investimento.

---

## 👨‍💻 Autor
**Guilherme**
*Business Administration (Finance Focus) & Systems Development Student.*

> "Transformando dados financeiros em decisões inteligentes."
