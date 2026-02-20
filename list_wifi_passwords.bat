@echo off
setlocal enabledelayedexpansion
chcp 65001 >nul
title Wi-Fi 配置导出
color 0A

echo ==============================
echo 已保存的 Wi-Fi 名称与密码
echo ==============================

for /f "tokens=2,* delims=:" %%A in ('netsh wlan show profiles ^| findstr /R /C:"All User Profile" /C:"所有用户配置文件"') do (
    set "name=%%B"
    set "name=!name:~1!"
    call :showpass "!name!"
)

goto :eof

:showpass
set "ssid=%~1"
set "pwd="
for /f "tokens=2,* delims=:" %%A in ('netsh wlan show profile name^="%ssid%" key^=clear ^| findstr /R /C:"Key Content" /C:"关键内容"') do (
    set "pwd=%%B"
    set "pwd=!pwd:~1!"
)
if defined pwd (
    echo 名称: %ssid%    密码: %pwd%
) else (
    echo 名称: %ssid%    密码: [未找到/无密码]
)
exit /b

:end
echo.
echo 任务完成，按任意键退出...
pause >nul
