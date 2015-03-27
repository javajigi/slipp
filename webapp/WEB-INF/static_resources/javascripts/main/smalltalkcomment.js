var commentService = {
	init : function(){
		var service = this;
		$('body').on('click', '.smalltalk-item-show-comments', function(evt){
			service.showComments(evt);
		});
		$('body').on('click', '.btn-smalltalk-reply', function(evt){
			service.showWriteForm(evt);
		});
		
		$('body').on('submit', '#id_commentForm', function(){
			service.save();
		});
	},
	fireShowCommentsAndForm : function(){
		$('.smalltalk-item-show-comments').first().trigger('click');
		$('.btn-smalltalk-reply').first().trigger('click');
	},
	showComments : function(evt){
		evt.preventDefault();
		var $item = $(evt.target).parents('.smalltalk-item');
		var smallTalkId = $item.data('smalltalk-id');

		$.get('/ajax/smalltalks/'+smallTalkId+'/comments', function(data){
			$item.find('.smalltalk-item-replylist').html(data);
		});
	},
	showWriteForm : function(evt){
		evt.preventDefault();
		var $item = $(evt.target).parents('.smalltalk-item')
		var smallTalkId = $item.data('smalltalk-id');

		$('#id_smallTalkId').val(smallTalkId);
		var actionUrl = '/ajax/smalltalks/'+smallTalkId+'/comments';
		$('#id_commentForm').attr('action', actionUrl);
		
		var $formHtml = $('#id_commentFormDiv');
		$item.find('.smalltalk-item-replyform').append( $formHtml );
		$('.tf-smalltalk-replyform-msg').focus();
		$('#id_commentFormDiv').off('click').on('click', 'button.smalltalk-replyform-submit', function(evt){
			evt.preventDefault();
			var smallTalkId = $('#id_smallTalkId').val();
			var comment = $('#comments').val();
			if( typeof(smallTalkId) == 'undefined' || smallTalkId.length == 0 || comment == ''){
				return false;
			}
			var actionUrl = $('#id_commentForm').attr('action');
			$.post(actionUrl, 
				{comments:comment},
				function(data){
					commentService.showComments(evt);
					$('.tf-smalltalk-replyform-msg').val('');
				});
		})
	}
}