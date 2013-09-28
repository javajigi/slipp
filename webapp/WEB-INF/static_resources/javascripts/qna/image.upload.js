var upload = false;
var InlineUpload = {
	dialog: null,
	block: '',
	offset: {},
	options: {
		container_class: 'markItUpInlineUpload',
		form_id: 'inline_upload_form',
		action: '/attachments',
		inputs: {
			file: { label: '', id: 'inline_upload_file1', name: 'inline_upload_file1' }
		},
		submit: { id: 'inline_upload_submit', value: '첨부하기' },
		close: 'inline_upload_close',
		iframe: 'inline_upload_iframe'
	},
	display: function(hash) {
		if( !$('.markItUpInlineUpload').length) {
			var self = this;

			/* Find position of toolbar. The dialog will inserted into the DOM elsewhere
			 * but has position: absolute. This is to avoid nesting the upload form inside
			 * the original. The dialog's offset from the toolbar position is adjusted in
			 * the stylesheet with the margin rule.
			 */
			this.offset = $(hash.textarea).prev('.markItUpHeader').offset();

			/* We want to build this fresh each time to avoid ID conflicts in case of
			 * multiple editors. This also means the form elements don't need to be
			 * cleared out.
			 */
			this.dialog = $([
				'<div class="',	this.options.container_class,
				'"><form class="inline_upload_form" id="', this.options.form_id,
				'" action="', this.options.action,
				'" target="', this.options.iframe,
				'" method="post" enctype="multipart/form-data"><label for="', this.options.inputs.file.id,
				'">', this.options.inputs.file.label,
				'</label><input name="file" id="', this.options.inputs.file.id,
				'" type="file" /><input id="', this.options.submit.id,
				'" class="', this.options.submit.id,
				'" type="button" value="', this.options.submit.value,
				'" /><small class="text">용량 제한 : 2MB</small></form><div id="', this.options.close,
				'" class="', this.options.close,
				'"><iframe class="', this.options.iframe,
				'" id="', this.options.iframe,
				'" name="',	this.options.iframe,
				'" src=""></iframe></div></div>',
			].join(''))
				.appendTo(document.body)
				.hide()
				.css('top', this.offset.top)
				.css('left', this.offset.left);


			//init submit button
			$('#'+this.options.submit.id).click(function(){
				if($('#inline_upload_file1').val() == ''){
					alert('Please select a file to upload');
					return false;
				}
				upload = true;
				$('#'+self.options.form_id).submit().fadeTo('fast', 0.2);
			});

			// init cancel button
			$('#'+this.options.close).click(this.cleanUp);

			// form response will be sent to the iframe
			$('#'+this.options.iframe).bind('load', function() {
				var result = document.getElementById('' + self.options.iframe).contentWindow.document.body.innerHTML;
				if(upload){
					var attachedFile = $.parseJSON(result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1));
					if (attachedFile.hasError){
						alert(attachedFile.errorMessage);
					} else {
						$.markItUp( {replaceWith:'!' + attachedFile.id + '!' });
					}
					InlineUpload.dialog.fadeOut().remove();
					upload = false;
				}
			});

			// Finally, display the dialog
			this.dialog.fadeIn('slow');
		}
	},
	cleanUp: function() {
		InlineUpload.dialog.fadeOut().remove();
	}
};