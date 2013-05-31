(function (m) {
	"use strict";

	SL10N.Global = {
		loginRequired: m("로그인을 해 주세요."),
		relogin: m("변경된 정보 반영을 위해 다시 로그인해야 합니다.")
	};
	
	SL10N.User = {
		requiredUserId: m("닉네임을 입력해 주세요."),
		minLenUserId: m("닉네임은 {0}자 이상이어야 합니다."),
		maxLenUserId: m("닉네임은 {0}자 이하이어야 합니다."),
		duplicateUserId: m("{0} 닉네임은 다른 사용자가 이미 사용하고 있는 닉네임입니다."),
		requiredEmail: m("이메일 주소를 입력해 주세요."),
		invalidEmailFormat: m("정확한 이메일을 입력해 주세요."),
		duplicateEmail: m("{0} 이메일은 다른 사용자가 이미 사용하고 있는 닉네임입니다."),
		requiredPassword: m("비밀번호를 입력해 주세요."),
		requiredOldPassword: m("현재 비밀번호를 입력해 주세요."),
		requiredNewPassword: m("신규 비밀번호를 입력해 주세요."),
		requiredNewPasswordConfirm: m("신규 비밀번호를 한번 더 입력해 주세요."),
		confirmPassword: m("입력한 비밀번호와 다릅니다.")
	};
	
	SL10N.Tag = {
		requiredName: m("태그명을 입력해 주세요."),
		minLenTag: m("태그명은 {0}자 이상이어야 합니다."),
		maxLenTag: m("태그명은 {0}자 이하이어야 합니다."),
		duplicateTag: m("{0} 태그는 등록되어 있는 태그입니다.")
	};
}(SLiPP.Localization.message));