$(document).ready(function() {
	$('#contents').markItUp(mySettings);
	
	$("#question").validate({
		rules: {
			title: "required",
			contents: "required",
			tagnames: "required"
		},
		messages: {
			title: "제목을 입력하세요.",
			contents: "내용을 입력하세요.",
			tagnames: "컨텐츠의 체계적인 관리를 위하여 태그를 하나 이상 입력하세요."
		}
	});
	
//	var tagnames = '';
//	$tagPreview = $('#tag-preview');
//	$('#plainTags').autocomplete('/tags/search', {
//		dataType: 'json',
//		cache: false,
//		autoFill: false,
//		extraParams: {
//			name: function() {
//			    tagnames = $('#plainTags').val();
//				return Slipp.TagParser.findEndTag(tagnames); 
//			}
//		},
//		parse: function(data) {
//			var array = new Array();
//            for(var i=0;i<data.length;i++) {
//                    array[array.length] = { data: data[i], value: data[i] };
//            }
//            return array;
//		},
//		matchSubset: false,
//		width: 320,
//		max: 4,
//		highlight: false,
//		scroll: true,
//		scrollHeight: 300,
//		formatItem: function(row, i, max) {
//			return row.name;
//		},
//		formatMatch: function(row, i, max) {
//			return row.name;
//		},
//		formatResult: function(row) {
//			return row.name;
//		}
//	}).result(function(event, row, formatted) {
//		$('#plainTags').val(Slipp.TagParser.replaceTag(tagnames, row.name));
//	});
});