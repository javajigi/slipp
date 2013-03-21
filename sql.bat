cd /d %~dp0
set INPUT=
set /P INPUT=Type input: %=%
mvn db-migration:new -Dname=%INPUT%_
pause > nul