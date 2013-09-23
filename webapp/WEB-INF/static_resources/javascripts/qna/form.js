$(document).ready(function() {
	$("#question").validate({
		rules: {
			title: "required",
			contents: "required"
		},
		messages: {
			title: SL10N.QnA.requiredTitle(),
			contents: SL10N.QnA.requiredContents()
		}
	});

	function showFacebookConnect() {
		var url = '/api/facebooks/groups';
		$.get(url,
			function(response) {
				$('.qna-connect-facebook').replaceWith(response);

				// checkbox evt handler
				$('.qna-connect-facebook-item').find('input:checkbox').on('change', function() {
					var $this = $(this);
					var $box = $this.parent('.qna-connect-facebook-item');
					var isChecked = $this.is(':checked');

					if (isChecked) {
						$box.addClass('checked');
					} else {
						$box.removeClass('checked');
					}
				});

				return false;
			}, 'html'
		);
	}

	showFacebookConnect();

	var tagnames = '';

	$('#plainTags').autocomplete('/tags/search', {
		dataType: 'json',
		cache: false,
		autoFill: false,
		extraParams: {
			name: function() {
					tagnames = $('#plainTags').val();
				return Slipp.TagParser.findEndTag(tagnames);
			}
		},
		parse: function(data) {
			var array = [];
			for(var i=0;i<data.length;i++) {
				array[array.length] = { data: data[i], value: data[i] };
			}
			return array;
		},
		matchSubset: false,
		width: $('.box-input-line').width(),
		max: 4,
		highlight: false,
		scroll: true,
		scrollHeight: 300,
		formatItem: function(row, i, max) {
			return row.name;
		},
		formatMatch: function(row, i, max) {
			return row.name;
		},
		formatResult: function(row) {
			return row.name;
		}
	}).result(function(event, row, formatted) {
		$('#plainTags').val(Slipp.TagParser.replaceTag(tagnames, row.name));
	});
});
