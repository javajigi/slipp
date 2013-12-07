var commentService = {
	init : function(){
		var service = this;
		service.showWriteForm();
		service.showComments();
		service.save();
	},
	showComments : function(){
		var service = this;
		$(document).on('click', '.show-comments-button', function(evt){
			evt.preventDefault();
			var smallTalkId = $(this).data('small-talk-id');
			$.get('/ajax/smalltalks/'+smallTalkId+'/comments', function(data){
				$('#id_commentShowDiv').html(data);
				$('#id_commentShowDiv').toggle();
			});
		});
	},
	showWriteForm : function(){
		var service = this;
		$(document).on('click', '.write-form-button', function(evt){
			evt.preventDefault();
			var smallTalkId = $(this).data('small-talk-id');
			$('#id_smallTalkId').val(smallTalkId);
			$('#id_commentFormDiv').toggle();
		});
	},
	save : function(){
		$(document).on('click', '.write-submit-button', function(evt){
			evt.preventDefault();
			var smallTalkId = $('#id_smallTalkId').val();
			if( typeof(smallTalkId) == 'undefined' || smallTalkId.length == 0){
				return false;
			}
			var actionUrl = '/smalltalks/'+smallTalkId+'/comments';
			$('#id_commentForm').attr('action', actionUrl);
			$('#id_commentForm').submit();
		});
		return false;
	}
}