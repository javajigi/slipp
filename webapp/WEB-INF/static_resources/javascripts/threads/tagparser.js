if (!Slipp) {
	var Slipp = {};
}

Slipp.TagParser = {
	findEndTag: function(tagnames) {
		if (this.isBlank(tagnames)){
			return '';
		}
		
		splitedTagNames = tagnames.trim().split(/[\s,;]+/);
		return splitedTagNames[splitedTagNames.length-1];
	},
	replaceTag: function(tagnames, selectedTagName) {
		if (this.isBlank(tagnames)){
			return '';
		}
		
		splitedTagNames = tagnames.trim().split(/[\s,;]+/);
		if (splitedTagNames.length==1){
			return selectedTagName;
		} else {
			splitedTagNames[splitedTagNames.length - 1] = selectedTagName;
		}
		return splitedTagNames.join(' ');
	},
	isBlank: function(input) {
		return !(input&&input.trim().length);
	}
}
