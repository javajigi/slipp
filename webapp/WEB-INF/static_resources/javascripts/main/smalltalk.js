var smalltalkService = {
	messageField: '#smallTalkMessage',
	failMessageField: '.smalltalk-form-fail-msg',
	init: function() {
		var that = this;
		var $talk = $(that.messageField);
		var $fail = $(that.failMessageField);

		$('.smalltalk-form').on('submit', function(evt){
			evt.preventDefault();
			that.save();
		});
		$('.btn-smalltalk-list-expand').on('click', function(evt) {
			var smalltalkCount = $(this).data('smalltalk-count');
			if( typeof( smalltalkCount ) == "undefined" || smalltalkCount === 0 ) {
				return false;
			}
			that.expand();
		});
		$talk.on('keydown', function(){
			$fail.hide();
		});
	},
	save: function() {
		var that = this;
		var $talk = $(that.messageField);
		var $fail = $(that.failMessageField);

		$.post('/smalltalks', { 'talk' : $talk.val() }, function(data) {
			if (data == 'OK') {
				that.get();
				$talk.val('');
			}else{
				$fail.html("수다는 간단하게 100글자까지만~");
				$fail.show();
			}
		});
		that.expand();
	},
	get: function() {
		$.get('/smalltalks', function(data) {
			$('.smalltalk-list').html( tmpl('tmpl-smalltalk-list', data) );
		});
	},
	expand: function() {
		$('.smalltalk').removeClass('ui-smalltalk-list-collapse');
	}
};
