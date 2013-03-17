$(document).ready(function() {
	var boxAddcomment = $('.box-write');
	var btnModeWrite = boxAddcomment.find('.btn-mode-write');
	var btnModePreview = boxAddcomment.find('.btn-mode-preview');
	var tfAddComment = boxAddcomment.find('.tf-write');
	var previewAddcomment = boxAddcomment.find('.preview-write');
	var footAddcomment = boxAddcomment.find('.foot-write');
	var formFileupload = $('.form-fileupload');

	formFileupload.offset(footAddcomment.offset());
	// https://github.com/malsup/form

	boxAddcomment.focusin(function () {
		boxAddcomment.addClass('focused');
	}).focusout(function() {
		boxAddcomment.removeClass('focused');
	});
	btnModeWrite.on('click', function () {
		$(this).addClass('active');
		btnModePreview.removeClass('active');
		tfAddComment.show();
		previewAddcomment.hide();
	});
	btnModePreview.on('click', function () {
		$(this).addClass('active');
		btnModeWrite.removeClass('active');
		previewAddcomment.show();
		tfAddComment.hide();
		// 여기에 ajax처리
	});
});
