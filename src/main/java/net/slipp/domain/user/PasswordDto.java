package net.slipp.domain.user;

public class PasswordDto {

    private Long id;
    private String oldPassword;
    private String newPassword;
    private String newPasswordConfirm;

    public PasswordDto(Long id, String oldPassword, String newPassword, String newPasswordConfirm) {
        this.id = id;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.newPasswordConfirm = newPasswordConfirm;
    }

    public Long getId() {
        return id;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getNewPasswordConfirm() {
        return newPasswordConfirm;
    }

}
