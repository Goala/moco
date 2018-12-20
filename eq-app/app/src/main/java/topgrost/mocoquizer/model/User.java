package topgrost.mocoquizer.model;

import java.io.Serializable;
import java.util.Objects;


public class User implements Serializable {

    private String name;
    private String email;

    public User() {}

    public User(String name, String login) {
        this.name = name;
        this.email = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) &&
                Objects.equals(email, user.email) ;
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, email);
    }

    @Override
    public String toString() {
        return "User{" +
                "alias='" + name + '\'' +
                ", login='" + email + '\'' +
                '}';
    }
}
