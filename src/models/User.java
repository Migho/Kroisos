package models;

public class User {
    private int id;
    private String username;
    private String name;
    private String mail;

    public User(int id, String username, String name, String mail) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.mail = mail;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }
    public String getName() {
        return name;
    }
    public String getMail() {
        return mail;
    }
    public String getUsername() { return username; }

    @Override
    public String toString() {
        return username + " (" + name + ")";
    }
}
