package topgrost.mocoquizer.model;

import java.io.Serializable;
import java.util.Objects;


public class User implements Serializable {

    private String alias;
    private String login;
    private String password;

    public User() {

    }

    public User(String alias, String login, String password) {
        this.alias = alias;
        this.login = login;
        this.password = password;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(alias, user.alias) &&
                Objects.equals(login, user.login) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {

        return Objects.hash(alias, login, password);
    }

    @Override
    public String toString() {
        return "User{" +
                "alias='" + alias + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
