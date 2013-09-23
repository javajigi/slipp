#!
export PATH=:/usr/local/bin

stylus ./static_resources/stylus/*.styl -c -w --include ./node_modules/nib/lib -o ./static_resources/stylesheets
