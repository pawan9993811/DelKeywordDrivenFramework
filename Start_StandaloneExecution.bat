cd %~dp0
cd Jars
set classpath=%classpath%;%~dp0\Jars\*;
cd..
java DriverScript.StandaloneDriver