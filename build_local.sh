#!/bin/bash
echo "DocTool 本地构建脚本"
echo "======================"

# 检查环境
if command -v gradle > /dev/null 2>&1; then
    echo "✅ Gradle 已安装"
    gradle --version | head -3
else
    echo "⚠️  Gradle 未安装，使用 wrapper"
    chmod +x gradlew
    ./gradlew --version | head -3
fi

echo ""
echo "构建选项:"
echo "1. 完整构建 (需要Android SDK)"
echo "2. 创建项目包 (立即可用)"
echo "3. 验证项目结构"

read -p "选择 (1-3): " choice

case $choice in
    1)
        echo "开始完整构建..."
        ./gradlew clean assembleDebug
        ;;
    2)
        echo "创建项目包..."
        tar -czf ../DocTool_Project_$(date +%Y%m%d_%H%M%S).tar.gz .
        echo "项目包已创建"
        ;;
    3)
        echo "验证项目结构..."
        find . -name "*.kt" -o -name "*.xml" -o -name "*.gradle*" | sort
        ;;
    *)
        echo "无效选择"
        ;;
esac
