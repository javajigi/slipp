(function ($) {
	"use strict";

	if (typeof window.SLiPP === "undefined") {
		window.SLiPP = {};
	}


	SLiPP.namespace = function (ns) {
		var parts = ns.split("."),
				parent = SLiPP,
				i;

		if (parts[0] === "SLiPP") {
			parts = parts.slice(1);
		}

		for (i = 0; i < parts.length; i += 1) {
			if (typeof parent[parts[i]] === "undefined") {
				parent[parts[i]] = {};
			}

			parent = parent[parts[i]];
		}
		return parent;
	};

	/** 에러 메시지에 대한 Localization **/
	SLiPP.Localization = {
		message : function (text) {
			return function () {
				var args = [ text ];
				args = args.concat(Array.prototype.slice.apply(arguments));
				return SLiPP.Localization.format.apply(null, args);
			};
		},

		format : function (text) {
			var tokenCount, token;

			if (arguments.length <= 1) {
				return text;
			}
			tokenCount = arguments.length - 2;
			for (token = 0; token <= tokenCount; token += 1) {
				text = text.replace(new RegExp("\\{" + token + "\\}", "gi"), arguments[token + 1]);
			}
			return text;
		}
	};	
	
	window.SL10N = {};
}(jQuery));