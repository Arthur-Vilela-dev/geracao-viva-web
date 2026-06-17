@echo off
echo Iniciando o sistema Conecta Geracao Viva...
echo.

echo Verificando se ja existe uma versao antiga rodando na porta 8080...
for /f "tokens=5" %%p in ('netstat -ano ^| findstr ":8080" ^| findstr "LISTENING"') do (
    echo Parando processo antigo na porta 8080: %%p
    taskkill /PID %%p /F >nul 2>nul
)

echo.
echo Gerando o arquivo .jar atualizado...
call mvn package -DskipTests

if errorlevel 1 (
    echo.
    echo O Maven encontrou um erro. Revise as mensagens acima.
    pause
    exit /b 1
)

echo.
echo Sistema iniciado em http://localhost:8080
echo Mantenha esta janela aberta enquanto estiver usando o sistema.
echo Para parar, pressione CTRL + C.
echo.

java -jar target\geracao-viva-0.0.1-SNAPSHOT.jar

echo.
echo O sistema foi encerrado.
pause
