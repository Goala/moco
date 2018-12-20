package topgrost.mocoquizer.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Game implements Serializable {

    private String name;
    private String password;
    private String deviceId;
    private String quizId;
    private boolean running;
    private ArrayList<Player> players;

    private String firebaseKey;

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

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    @Exclude
    public String getFirebaseKey() {
        return firebaseKey;
    }

    public void setFirebaseKey(String firebaseKey) {
        this.firebaseKey = firebaseKey;
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
                Objects.equals(quizId, game.quizId) &&
                Objects.equals(players, game.players) &&
                Objects.equals(firebaseKey, game.firebaseKey);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, password, deviceId, quizId, running, players, firebaseKey);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Game{");
        sb.append("name='").append(name).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", deviceId='").append(deviceId).append('\'');
        sb.append(", quizId='").append(quizId).append('\'');
        sb.append(", running=").append(running);
        sb.append(", players=").append(players);
        sb.append(", firebaseKey='").append(firebaseKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
