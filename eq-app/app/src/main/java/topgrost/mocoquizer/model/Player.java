package topgrost.mocoquizer.model;

import java.io.Serializable;
import java.util.Objects;

public class Player implements Serializable {

    private String name;
    private boolean feedback;

    public Player(){}

    public Player(String name, boolean feedback) {
        this.name = name;
        this.feedback = feedback;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFeedback() {
        return feedback;
    }

    public void setFeedback(boolean feedback) {
        this.feedback = feedback;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return feedback == player.feedback &&
                Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, feedback);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Player{");
        sb.append("name='").append(name).append('\'');
        sb.append(", feedback=").append(feedback);
        sb.append('}');
        return sb.toString();
    }
}
