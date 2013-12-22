var commentService = {
	init : function(){
		var service = this;

		$('.smalltalk').on('click', '.smalltalk-item-show-comments', function(evt){
			service.showComments(evt);
		}).on('click', '.btn-smalltalk-reply', function(evt){
			service.showWriteForm(evt);
		});
		
		$('#id_commentForm').on('submit', function(){
			service.save();
		});
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
		var actionUrl = '/smalltalks/'+smallTalkId+'/comments';
		$('#id_commentForm').attr('action', actionUrl);

		$item.find('.smalltalk-item-replyform').append($('#id_commentFormDiv'));
		$('.tf-smalltalk-replyform-msg').focus();
	},
	save : function(){
		var smallTalkId = $('#id_smallTalkId').val();
		var comment = $('#comments').val();
		if( typeof(smallTalkId) == 'undefined' || smallTalkId.length == 0 || comment == ''){
			return false;
		}
		return true;
	}
}