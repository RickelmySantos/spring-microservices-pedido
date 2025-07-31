#!/bin/bash

echo "🔍 Iniciando análise SonarQube"
echo "=================================================="

echo "🔍 Verificando se o SonarQube está rodando..."
if ! curl -s http://localhost:9000/api/system/status > /dev/null; then
    echo "❌ SonarQube não está rodando em http://localhost:9000"
    exit 1
fi

echo "✅ SonarQube está rodando!"

# Navegar para o diretório do projeto
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_DIR="$(cd "$SCRIPT_DIR/../../rsdesenvolvimento-api" && pwd)"

echo "📁 Navegando para: $PROJECT_DIR"
cd "$PROJECT_DIR"

if [ ! -f "sonar-project.properties" ]; then
    echo "❌ Arquivo sonar-project.properties não encontrado em $PROJECT_DIR!"
    exit 1
fi

echo "📋 Configuração encontrada: sonar-project.properties"

# Opções de execução
echo ""
echo "Escolha o tipo de análise:"
echo "1) Análise completa (com testes e cobertura)"
echo "2) Análise rápida (sem testes)"
echo "3) Apenas cobertura JaCoCo"
echo "4) Análise específica de um módulo"

read -p "Digite sua opção (1-4): " opcao

case $opcao in
    1)
        echo "🧪 Executando análise completa..."
        mvn clean verify sonar:sonar \
            -Dsonar.projectKey=restaurante-web \
            -Dsonar.projectName='restaurante-web' \
            -Dsonar.host.url=http://localhost:9000 \
            -Dsonar.token=sqp_2520b13610ea01c5922fdc0f8d4a28609fbe6e6e
        ;;
    2)
        echo "⚡ Executando análise rápida..."
        mvn clean compile sonar:sonar \
            -DskipTests \
            -Dsonar.projectKey=restaurante-web \
            -Dsonar.projectName='restaurante-web' \
            -Dsonar.host.url=http://localhost:9000 \
            -Dsonar.token=sqp_2520b13610ea01c5922fdc0f8d4a28609fbe6e6e
        ;;
    3)
        echo "📊 Gerando relatório de cobertura JaCoCo..."
        mvn clean test jacoco:report
        echo "✅ Relatório gerado em: target/site/jacoco/index.html"
        ;;
    4)
        echo "📁 Módulos disponíveis:"
        echo "  - eureka-server"
        echo "  - estoque"
        echo "  - pedido-service"
        echo "  - pagamento-service"
        echo "  - notificacao-service"
        echo "  - api-gateway"

        read -p "Digite o nome do módulo: " modulo

        if [ -d "$modulo" ]; then
            echo "🔍 Analisando módulo: $modulo"
            cd "$modulo"
            mvn clean verify sonar:sonar \
                -Dsonar.projectKey=restaurante-web:$modulo \
                -Dsonar.projectName="restaurante-web-$modulo" \
                -Dsonar.host.url=http://localhost:9000 \
                -Dsonar.token=sqp_2520b13610ea01c5922fdc0f8d4a28609fbe6e6e
        else
            echo "❌ Módulo '$modulo' não encontrado!"
            exit 1
        fi
        ;;
    *)
        echo "❌ Opção inválida!"
        exit 1
        ;;
esac

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Análise concluída com sucesso!"
    echo "🌐 Acesse o relatório em: http://localhost:9000"
    echo "🔑 Login: admin / admin (primeira vez)"
else
    echo ""
    echo "❌ Erro durante a análise!"
    echo "💡 Verifique os logs acima para mais detalhes"
    exit 1
fi
