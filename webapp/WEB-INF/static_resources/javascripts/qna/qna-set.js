mySettings = {
	previewParserPath:	'/wikis/preview', // path to your Textile parser
	onShiftEnter:		{keepDefault:false, replaceWith:'\n\n'},
	markupSet: [
		{name:'Heading 1', key:'1', openWith:'h1(!(([![Class]!]))!). ', placeHolder:'제목' },
		{name:'Heading 2', key:'2', openWith:'h2(!(([![Class]!]))!). ', placeHolder:'제목' },
		{name:'Heading 3', key:'3', openWith:'h3(!(([![Class]!]))!). ', placeHolder:'제목' },
		{separator:'---------------' },
		{name:'볼드', key:'B', closeWith:'*', openWith:'*'},
		{name:'Italic', key:'I', closeWith:'_', openWith:'_'},
		{name:'Stroke through', key:'S', closeWith:'-', openWith:'-'},
		{separator:'---------------' },
		{name:'Bulleted list', openWith:'(!(* |!|*)!)'},
		{name:'Numeric list', openWith:'(!(# |!|#)!)'}, 
		{separator:'---------------' },
		{name:'Upload',
			key:'M',
			beforeInsert:function(markItUp){InlineUpload.display(markItUp)}},
		{name:'Quotes', closeWith:'{quote}', openWith:'{quote}'},
		{name:'Code', closeWith:'{code}', openWith:'{code}'},
		{separator:'---------------' },
		{name:'Preview', key:'P', call:'preview', className:'preview'}
	]
}