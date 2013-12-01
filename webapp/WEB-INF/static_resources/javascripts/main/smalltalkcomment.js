var commentService = {
	init : function(){
		var service = this;
		service.show();
		service.save();
	},
	show : function(){
		var service = this;
		$(document).on('click', '.write-form-button', function(evt){
			evt.preventDefault();
			var smallTalkId = $(this).data('small-talk-id');
			service.form(smallTalkId);
		});
	},
	form : function(smallTalkId){
		$('#id_smallTalkId').val(smallTalkId);
		$('#id_commentForm').show();
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