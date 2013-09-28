var smalltalkService = {
	messageField: '#smallTalkMessage',
	failMessageField: '.smalltalk-form-fail-msg',
	init: function() {
		var that = this;
		var $talk = $(that.messageField);
		var $fail = $(that.failMessageField);

		//that.makeUrlToLink();
		that.ajaxLoad();

		$( document ).on( "submit", ".smalltalk-form", function(evt){
			evt.preventDefault();
			$('.btn-smalltalk-form-util-submit').attr("disabled", true).text('저장중...');
			that.save();
		});

		$( document ).on( "click", ".btn-smalltalk-list-expand", function(evt){
			var smalltalkCount = $(this).data('smalltalk-count');
			if( typeof( smalltalkCount ) == "undefined" || smalltalkCount === 0 ) {
				return false;
			}
			that.expand();
		});
		$( document ).on( "keydown", $talk, function(){
			$fail.hide();
		});
	},
	ajaxLoad: function() {
		var waitingHtml = '<li style="text-align: center;"><img src="/resources/images/ajax-document-loader.gif" width="100px" height="100px"></li>';
		$('.smalltalk-list').html(waitingHtml);
		$.get('/ajax/smalltalks', function(data){
			$('.smalltalk-list').html(data);
			$('.btn-smalltalk-form-util-submit').attr("disabled", false).text('나도 한마디');
		});
	},
	save: function() {
		var that = this;
		var $talk = $(that.messageField);
		var $fail = $(that.failMessageField);
		$.post('/smalltalks', { 'talk' : $talk.val() }, function(data) {
			if (data == 'OK') {
				$talk.val('');
				that.ajaxLoad();
			}else{
				$fail.html("수다는 간단하게 100글자까지만~");
				$fail.show();
				$('.btn-smalltalk-form-util-submit').attr("disabled", false).text('나도 한마디');
			}
		});
		//that.expand();
	},
	expand: function() {
		$('.smalltalk').removeClass('ui-smalltalk-list-collapse');
	},
	makeUrlToLink: function() {
		var $items = $('.smalltalk-item-cont');

		$items.each(function() {
			var cont = $(this).html();
				$(this).html(urlify(cont));
		});

		function urlify(text) {
			var urlRegex = /(https?:\/\/[^\s]+)/g;
			return text.replace(urlRegex, function(url) {
				return '<a href="' + url + '" target="_blank">' + url + '</a>';
			});
		}
	}

};
