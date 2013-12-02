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

	$('.btn-like-article').on('click', likeAnswerTo);
	$('.btn-like-question').on('click', likeQuestionTo);
	$('.btn-dislike-article').on('click', dislikeAnswerTo);

	$('.form-delete').on('submit', function() {
		if ( !confirm('정말 삭제하시겠습니까?') ) {
			return false;
		}
	});

	setNicknamesLink();
	setImgRealSizeLink();
	setFloatingBtnLike();
	setFloatingContentSub();
	showFacebookComments();
	setRecentlyCommentPosition();
	
	function showFacebookComments() {
		var url = '/api/facebooks/' + questionId + '/comments';
		$.get(url,
			function(response) {
				$('.qna-facebook-comment').html(response);
				return false;
			}, 'html'
		);
	}
	
	function addAnswerTo() {
		var orgUserId = $(this).data('answer-user-id');
		var $contents = $('#contents');
		var contentsVal = arroundSpace( $contents.val(), orgUserId );

		$('body').animate({scrollTop: $contents.offset().top}, 'slow');
		$contents.focus().val(contentsVal).focus(); // 텍스트 추가 후 포커스 주기위해 포커스 두번

		return false;
	}

	function likeAnswerTo(e) {
		if (guestUser) {
			$(location).attr('href', '/users/login');
			return false;
		}
		e.preventDefault();
		$likeAnswerBtn = $(this);
		$.post($likeAnswerBtn.attr('href'), {},
			function(result) {
				$likeAnswerBtn.find('.like-article-count').html(result);
			}, 'json'
		);
		return false;
	}
	
	function likeQuestionTo(e) {
		e.preventDefault();
		$likeQuestionBtn = $(this);
		$.post($likeQuestionBtn.attr('href'), {},
			function(result) {
				$likeQuestionBtn.find('.like-question-count').html(result);
			}, 'json'
		);
		return false;
	}
	
	function dislikeAnswerTo(e) {
		if (guestUser) {
			$(location).attr('href', '/users/login');
			return false;
		}
		e.preventDefault();
		$dislikeAnswerBtn = $(this);
		$.post($dislikeAnswerBtn.attr('href'), {},
			function(result) {
				$dislikeAnswerBtn.find('.dislike-article-count').html(result);
			}, 'json'
		);
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
		var $images = $('.article-doc img');
		var imageUrl = $images.attr('src');

		$images.wrap('<a href="'+imageUrl+'" target="_blank"></a>');
	}

	function setFloatingBtnLike() {
		var $window = $(window);
		var $articles = $('.article');
		var srollTop;

		$window.scroll(function(event) {
			scrollTop = $window.scrollTop();

			$articles.each(function() {
				var $btnLikeArticle = $(this).find('.btn-like-article');
				var offsetTop = $(this).offset().top;
				var height = $(this).height();
				var isFloatingMode = scrollTop > offsetTop && scrollTop - offsetTop + 100 < height;

				if (isFloatingMode) {
					$btnLikeArticle.addClass('fixed');
				} else {
					$btnLikeArticle.removeClass('fixed');
				}
			});
		});
	}

	function setFloatingContentSub() {
		var $window = $(window);
		var $contentSub = $('.content-sub');
		var $floating = $contentSub.find('.floating')
		var srollTop;

		$window.scroll(function(event) {
			scrollTop = $window.scrollTop();
			var offsetTop = $contentSub.offset().top;
			var height = $(this).height();
			var isFloatingMode = scrollTop > offsetTop;

			if (isFloatingMode) {
				$floating.css({top:0, position: 'fixed'})
			} else {
				$floating.css({top:'', position: 'relative'})
			}
		});
	}

	function setRecentlyCommentPosition() {
		var $recentlySlippComment = $('.qna-comment-slipp-articles .article').last();
		var $recentlyFbComment = $('.qna-comment-fb-articles .article').last();

		$('#qna-recently-slipp-comment').insertBefore($recentlySlippComment);
		$('#qna-recently-fb-comment').insertBefore($recentlyFbComment);
	}
});
