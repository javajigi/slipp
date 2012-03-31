test("findEndTag when empty", function() {
	equals('', Slipp.TagParser.findEndTag());
	equals('', Slipp.TagParser.findEndTag(''));
});

test("findEndTag when one tag", function() {
	equals('ja', Slipp.TagParser.findEndTag('ja'));
	equals('ja', Slipp.TagParser.findEndTag('ja '));
});

test("findEndTag when space separator, many tag", function() {
	equals('ja', Slipp.TagParser.findEndTag('javascript ja'));
	equals('ja', Slipp.TagParser.findEndTag(' javascript ja'));
	equals('ja', Slipp.TagParser.findEndTag('javascript  ja'));
	equals('ja', Slipp.TagParser.findEndTag('javascript ja '));
	equals('ja', Slipp.TagParser.findEndTag('javascript 자바스크립트   ja '));
});

test("findEndTag when comma separator, many tag", function() {
	equals('ja', Slipp.TagParser.findEndTag('javascript,ja'));
	equals('ja', Slipp.TagParser.findEndTag('javascript, ja'));
	equals('ja', Slipp.TagParser.findEndTag('javascript, ja '));
});

test("findEndTag when semicolon separator, many tag", function() {
	equals('ja', Slipp.TagParser.findEndTag('javascript;ja'));
	equals('ja', Slipp.TagParser.findEndTag('javascript ; ja'));
	equals('ja', Slipp.TagParser.findEndTag('javascript; ja '));
});

test("replaceTag when space separator, one tag", function() {
	equals('java', Slipp.TagParser.replaceTag('ja', 'java'));
	equals('java', Slipp.TagParser.replaceTag(' ja', 'java'));
	equals('java', Slipp.TagParser.replaceTag('ja ', 'java'));
});

test("replaceTag when space separator, many tag", function() {
	equals('javascript java', Slipp.TagParser.replaceTag('javascript ja', 'java'));
	equals('javascript java', Slipp.TagParser.replaceTag('javascript ja ', 'java'));
	equals('javascript java', Slipp.TagParser.replaceTag('javascript   ja ', 'java'));
});

test("isBlank when undefined, '', ' '", function() {
	equals(true, Slipp.TagParser.isBlank(null));
	equals(true, Slipp.TagParser.isBlank(''));
	equals(true, Slipp.TagParser.isBlank(' '));
});

test("isBlank when is not blank", function() {
	equals(false, Slipp.TagParser.isBlank('t'));
	equals(false, Slipp.TagParser.isBlank('t '));
	equals(false, Slipp.TagParser.isBlank(' t '));
});