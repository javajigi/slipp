mySettings = {
	previewParserPath:	'/wikis/preview', // path to your Textile parser
	onShiftEnter:		{keepDefault:false, replaceWith:'\n\n'},
	markupSet: [
		{name:'Heading 1', key:'1', openWith:'#(!(([![Class]!]))!) ', placeHolder:'제목' },
		{name:'Heading 2', key:'2', openWith:'##(!(([![Class]!]))!) ', placeHolder:'제목' },
		{name:'Heading 3', key:'3', openWith:'###(!(([![Class]!]))!) ', placeHolder:'제목' },
		{separator:'---------------' },
		{name:'볼드', key:'B', closeWith:'**', openWith:'**'},
		{name:'Italic', key:'I', closeWith:'*', openWith:'*'},
		{separator:'---------------' },
		{name:'Bulleted list', openWith:'(!(* |!|*)!)'},
		{separator:'---------------' },
		{name:'Upload',
			key:'M',
			beforeInsert:function(markItUp){InlineUpload.display(markItUp)}},
		{name:'Quotes', openWith:'> '},
		{name:'Code', closeWith:'```', openWith:'```'},
		{separator:'---------------' },
		{name:'Preview', key:'P', call:'preview', className:'preview'}
	]
}