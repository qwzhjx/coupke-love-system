@echo off
chcp 65001 >nul
title 💕 情侣亲密度升温系统
echo.
echo   💕 情侣亲密度升温系统 启动中...
echo   ================================
echo.

:: Check MySQL
netstat -ano | findstr ":3306" >nul
if %errorlevel% neq 0 (
    echo   [WARN] MySQL 未运行，正在启动...
    start /B "" "D:\mysql2\bin\mysqld.exe" --console --port=3306
    timeout /t 5 /nobreak >nul
    echo   [OK] MySQL 已启动
)

:: Start the app
echo   [INFO] 启动应用服务...
start /B java --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/java.lang.reflect=ALL-UNNAMED -jar "%~dp0target\couple-love-system-0.0.1-SNAPSHOT.jar"

timeout /t 8 /nobreak >nul

:: Get IP
for /f "tokens=2 delims=:" %%a in ('ipconfig ^| findstr "IPv4"') do set IP=%%a
set IP=%IP: =%

echo.
echo   ================================
echo   ✅ 启动成功！
echo.
echo   📱 手机访问:
echo      http://%IP%:8080
echo      密码: 5201314
echo.
echo   🔧 后台管理:
echo      http://%IP%:8080/admin/login
echo      账号: admin / admin123
echo.
echo   💻 电脑访问:
echo      http://localhost:8080
echo   ================================
echo.
pause
