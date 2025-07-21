# Reestruturação do Projeto Angular

## 📁 Nova Estrutura de Pastas

Este documento descreve a nova estrutura de pastas implementada para melhorar a organização e escalabilidade do projeto.

### 🗂️ Estrutura Principal

```
src/app/
├── 📁 core/                    # Funcionalidades essenciais (singleton)
│   ├── guards/                 # Route guards
│   ├── interceptors/           # HTTP interceptors
│   ├── constants/              # Constantes da aplicação
│   └── services/               # Serviços core
│
├── 📁 shared/                  # Componentes e utilitários reutilizáveis
│   ├── components/
│   │   ├── ui/                 # Componentes de UI básicos
│   │   ├── layout/             # Componentes de layout
│   │   └── business/           # Componentes de negócio reutilizáveis
│   ├── pipes/                  # Pipes customizados
│   ├── directives/             # Diretivas customizadas
│   └── validators/             # Validadores customizados
│
├── 📁 features/                # Módulos de funcionalidades
│   ├── home/
│   ├── cardapio/
│   ├── pedidos/
│   └── usuario/
│
├── 📁 models/                  # Interfaces e types
│   ├── api/                    # Modelos da API
│   ├── ui/                     # Modelos de UI
│   └── common/                 # Modelos comuns
│
├── 📁 services/                # Serviços de negócio
│   ├── api/                    # Serviços de API
│   ├── business/               # Lógica de negócio
│   └── state/                  # Gerenciamento de estado
│
├── 📁 utils/                   # Utilitários e helpers
└── 📁 pages/                   # Páginas especiais (error, not-found)
```

## 🔄 Migração Realizada

### ✅ Componentes Migrados

1. **Header** → `shared/components/layout/header/`
2. **Footer** → `shared/components/layout/footer/`
3. **Carrinho** → `shared/components/business/carrinho/`
4. **Depoimentos** → `shared/components/business/depoimento/`
5. **Cardápio** → `features/cardapio/components/`

### ✅ Modelos Reorganizados

-   **Produto** → `models/api/produto.model.ts`
-   **ItemCarrinho** → `models/ui/itemCarrinho.model.ts`

### ✅ Arquivos Criados

-   **Constants** → `core/constants/app.constants.ts`
-   **Guards** → `core/guards/auth.guard.ts`
-   **Utilities** → `utils/helpers.ts`
-   **Error Pages** → `pages/not-found/not-found.component.ts`

## 🔧 Próximos Passos

### 1. Atualizar Imports

-   [ ] Atualizar imports nos componentes existentes
-   [ ] Verificar referências nos serviços
-   [ ] Atualizar rotas principais

### 2. Migrar Componentes Restantes

-   [ ] Newsletter → `shared/components/business/newsletter/`
-   [ ] Sobre → `shared/components/business/sobre/`
-   [ ] Contato → `features/contato/`

### 3. Reorganizar Serviços

-   [ ] Mover serviços para `services/api/` ou `services/business/`
-   [ ] Implementar serviços de estado se necessário

### 4. Implementar Features

-   [ ] Criar feature modules para cada área
-   [ ] Implementar lazy loading
-   [ ] Configurar roteamento por features

## 🚀 Benefícios

-   **📈 Escalabilidade**: Estrutura preparada para crescimento
-   **🛠️ Manutenibilidade**: Código mais organizado
-   **♻️ Reutilização**: Componentes claramente separados
-   **⚡ Performance**: Lazy loading por features
-   **🧪 Testabilidade**: Estrutura clara para testes

## 📝 Convenções

### Nomenclatura de Arquivos

-   Componentes: `component-name.component.ts`
-   Serviços: `service-name.service.ts`
-   Modelos: `model-name.model.ts`
-   Guards: `guard-name.guard.ts`

### Estrutura de Features

```
feature-name/
├── components/         # Componentes da feature
├── services/          # Serviços específicos
└── feature-name.routes.ts  # Rotas da feature
```

## 🔍 Comandos Úteis

```bash
# Encontrar imports antigos que precisam ser atualizados
grep -r "from 'src/app/modules" src/

# Encontrar referências a modelos antigos
grep -r "from 'src/app/models/produto.model'" src/

# Verificar componentes que ainda precisam ser migrados
find src/app/modules -name "*.component.ts"
```

---

**Nota**: Esta reestruturação segue as melhores práticas do Angular e padrões da comunidade para projetos escaláveis.
