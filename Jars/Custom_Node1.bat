@echo off
java -jar selenium-server-standalone-2.48.2.jar -role node -hub http://10.8.205.105:4444/grid/register -Dwebdriver.chrome.driver="chromedriver.exe" -Dwebdriver.ie.driver="IEDriverServer.exe" -nodeConfig nodeconfig1.json
pause >nul