package net.slipp.web.tag;

public class TagForm {
    private String email;
    
    private String name;
    
    private String description;
    
    public TagForm() {
    }
    
    public TagForm(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TagForm [email=" + email + ", name=" + name + ", description=" + description + "]";
    }
}
