#!/bin/bash
# 高中数学 AI 诊断系统 - 一键部署脚本

set -e

BACKEND_DIR="$(cd "$(dirname "$0")/backend" && pwd)"
FRONTEND_DIR="$(cd "$(dirname "$0")/frontend" && pwd)"

case "$1" in
  backend)
    echo "═══════════════════════════════════════"
    echo " 启动后端服务 (Spring Boot + H2)"
    echo "═══════════════════════════════════════"
    cd "$BACKEND_DIR"
    if [ ! -f "target/teacher-backend-1.0.0.jar" ]; then
      echo "→ 首次构建..."
      mvn clean package -DskipTests -q
    fi
    echo "→ 启动后端 http://localhost:8080"
    java -jar target/teacher-backend-1.0.0.jar
    ;;

  frontend)
    echo "═══════════════════════════════════════"
    echo " 启动前端服务 (uni-app H5)"
    echo "═══════════════════════════════════════"
    cd "$FRONTEND_DIR"
    npm run dev:h5
    ;;

  all)
    echo "═══════════════════════════════════════"
    echo " 启动全部服务"
    echo "═══════════════════════════════════════"
    echo "→ 后端: http://localhost:8080"
    echo "→ 前端: http://localhost:5173"
    echo ""
    cd "$BACKEND_DIR"
    mvn spring-boot:run &
    BACKEND_PID=$!
    sleep 5
    cd "$FRONTEND_DIR"
    npm run dev:h5 &
    FRONTEND_PID=$!
    echo "后端 PID: $BACKEND_PID"
    echo "前端 PID: $FRONTEND_PID"
    echo "按 Ctrl+C 停止所有服务"
    wait
    ;;

  build)
    echo "═══════════════════════════════════════"
    echo " 构建后端"
    echo "═══════════════════════════════════════"
    cd "$BACKEND_DIR"
    mvn clean package -DskipTests
    echo "→ 构建完成: target/teacher-backend-1.0.0.jar"
    ;;

  install)
    echo "═══════════════════════════════════════"
    echo " 安装依赖"
    echo "═══════════════════════════════════════"
    echo "→ 安装前端依赖..."
    cd "$FRONTEND_DIR"
    npm install
    echo "→ 安装完成"
    ;;

  *)
    echo "用法: ./deploy.sh {backend|frontend|all|build|install}"
    echo ""
    echo "  backend   - 启动后端服务"
    echo "  frontend  - 启动前端服务"
    echo "  all       - 启动全部服务"
    echo "  build     - 构建后端 jar"
    echo "  install   - 安装前端依赖"
    ;;
esac
