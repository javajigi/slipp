package net.slipp.domain.user;

public class PasswordDto {

    private Long id;
    private String oldPassword;
    private String newPassword;
    private String newPasswordConfirm;
    
    public PasswordDto() {
    }
    
    public PasswordDto(Long id) {
        this.id = id;
    }

    public PasswordDto(Long id, String oldPassword, String newPassword, String newPasswordConfirm) {
        this.id = id;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.newPasswordConfirm = newPasswordConfirm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setNewPasswordConfirm(String newPasswordConfirm) {
        this.newPasswordConfirm = newPasswordConfirm;
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
