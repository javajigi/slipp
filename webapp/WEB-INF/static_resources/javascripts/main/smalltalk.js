var smalltalkService = {
	init: function() {
		var that = this;
		$('.smalltalk-form').on('submit', function(evt){
			evt.preventDefault();
			that.save();
		});
		$('.btn-smalltalk-list-expand').on('click', function(evt) {
			var smalltalkCount = $(this).data('smalltalk-count');
			if( typeof( smalltalkCount ) == "undefined" || smalltalkCount == 0 ) {
				return false;
			}
			that.expand();
		})
	},
	MessageField: '#smallTalkMessage',
	save : function() {
		var that = this;
		var $talk = $(that.MessageField);

		$.post('/smalltalks', { 'talk' : $talk.val() }, function(data) {
			if (data == 'OK') {
				that.get();
				$talk.val('');
			}
		});
		that.expand();
	},
	get : function() {
		$.get('/smalltalks', function(data) {
			$('.smalltalk-list').html( tmpl('tmpl-smalltalk-list', data) );
		});
	},
	expand: function() {
		$('.smalltalk').removeClass('ui-smalltalk-list-collapse');
	}
};