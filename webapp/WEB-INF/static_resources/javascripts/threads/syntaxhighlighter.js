$(document).ready(function()	{
	function path(){
	  var args = arguments;
	  var result = [];
	       
	  for(var i = 0; i < args.length; i++){
	      result.push(args[i].replace('@', '/public/javascripts/sh/'));
	  }
	  return result;
	}
	
	SyntaxHighlighter.autoloader.apply(null, path(
		'bash shell             @shBrushBash.js',
		'css                    @shBrushCss.js',
		'groovy                 @shBrushGroovy.js',
		'java                   @shBrushJava.js',
		'js jscript javascript  @shBrushJScript.js',
		'perl pl                @shBrushPerl.js',
		'text plain             @shBrushPlain.js',
		'ruby rails ror rb      @shBrushRuby.js',
		'scala                  @shBrushScala.js',
		'sql                    @shBrushSql.js',
		'xml xhtml xslt html    @shBrushXml.js'
	));
	
	SyntaxHighlighter.all();
});