$(document).ready(function(){
	$('#answer').validate({
		rules: {
			contents: 'required'
		},
		messages: {
			contents: '내용을 입력하세요.'
		}
	});
});