package topgrost.mocoquizer.lobby.model;

import java.io.Serializable;
import java.util.Objects;

public class Game implements Serializable {

    private String name;
    private String password;
    private String deviceId;
    private String quizId;
    private boolean running;

    public Game() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return running == game.running &&
                Objects.equals(name, game.name) &&
                Objects.equals(password, game.password) &&
                Objects.equals(deviceId, game.deviceId) &&
                Objects.equals(quizId, game.quizId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, password, deviceId, quizId, running);
    }

    @Override
    public String toString() {
        return "Game{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", quizId='" + quizId + '\'' +
                ", running=" + running +
                '}';
    }
}
