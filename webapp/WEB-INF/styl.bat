set "NODE_HOME=C:\Program Files (x86)\nodejs";
set "PATH=C:\Users\%USERNAME%\AppData\Roaming\npm;%NODE_HOME%"

stylus.cmd styl -c --include node_modules/nib/lib -o static_resources/stylesheets