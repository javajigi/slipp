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
			var array = new Array();
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
