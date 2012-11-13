set "NODE_HOME=C:\Program Files (x86)\nodejs";
set "PATH=%NODE_HOME%;C:\Users\%USERNAME%\AppData\Roaming\npm"

stylus.cmd styl -c --include node_modules/nib/lib -o static_resources/stylesheets