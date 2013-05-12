(function (m) {
	"use strict";

	AL10N.Global = {
		loginRequired: m("로그인을 해 주세요.")
	},

	AL10N.User = {
		requiredUserId: m("아이디를 입력해 주세요."),
		minLenUserId: m("아이디는 {0}자 이상이어야 합니다."),
		maxLenUserId: m("아이디는 {0}자 이하이어야 합니다."),
		invalidUserIdFormat: m("아이디는 영문과 숫자만 사용할 수 있습니다."),
		duplicateUserId: m("{0} 아이디는 다른 사용자가 이미 사용하고 있는 아이디입니다."),
		requiredNickName: m("닉네임을 입력해 주세요."),
		minLenNickName: m("닉네임은 {0}자 이상이어야 합니다."),
		maxLenNickName: m("닉네임은 {0}자 이하이어야 합니다."),
		requiredEmail: m("이메일 주소를 입력해 주세요."),
		invalidEmailFormat: m("정확한 이메일을 입력해 주세요."),
		requiredPassword: m("비밀번호를 입력해 주세요."),
		requiredOldPassword: m("현재 비밀번호를 입력해 주세요."),
		requiredNewPassword: m("신규 비밀번호를 입력해 주세요."),
		requiredNewPasswordConfirm: m("신규 비밀번호를 한번 더 입력해 주세요."),
		confirmPassword: m("입력한 비밀번호와 다릅니다.")
	};
}(SLiPP.Localization.message));