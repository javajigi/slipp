$(document).ready(function(){
	$('#answer').validate({
		rules: {
			contents: 'required'
		},
		messages: {
			contents: '내용을 입력하세요.'
		}
	});

	$('.link-answer-article').on('click', addAnswerTo);

	$('.form-delete').on('submit', function() {
		if ( !confirm('정말 삭제하시겠습니까?') ) {
			return false;
		}
	});

	setNicknamesLink();
	setImgRealSizeLink();

	function addAnswerTo() {
		var orgUserId = $(this).data('answer-user-id');
		var contents = arroundSpace( $('#contents').val(), orgUserId );

		$('#contents').val('').focus().val(contents);
		$('#contents').focus();
		$('html, body').animate({scrollTop: $(document).height()}, 'slow');
		return false;
	}
	function arroundSpace(contents, orgUserId){
		if ( $.trim(contents).length > 0) {
			contents += ' ';
		}
		contents += orgUserId + ' ';

		return contents;
	}
	function setNicknamesLink(){
		var nickNames = [];

		$('.article-author-name').each(function() {
			nickNames[$(this).text()] = $(this).attr('href');
		});

		$('.article-doc').each(function() {
			var cont = $(this).html();
			for (var key in nickNames) {
				cont = cont.replace(new RegExp('@' + key, 'gi'), '<a href="'+nickNames[key]+'"><b>@'+key+'</b></a>');
			}

			$(this).html(cont);
		});
	}
	function setImgRealSizeLink() {
		var images = $('.article-doc img');
		var imageUrl = images.attr('src');

		images.wrap('<a href="'+imageUrl+'" target="_blank"></a>');
	}
});
