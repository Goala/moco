package topgrost.mocoquizer.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Game implements Serializable {

    private String name;
    private String password;
    private String deviceId;
    private int questionNr;
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

    public int getQuestionNr() {
        return questionNr;
    }

    public void setQuestionNr(int questionNr) {
        this.questionNr = questionNr;
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
        return questionNr == game.questionNr &&
                running == game.running &&
                Objects.equals(name, game.name) &&
                Objects.equals(password, game.password) &&
                Objects.equals(deviceId, game.deviceId) &&
                Objects.equals(quizId, game.quizId) &&
                Objects.equals(players, game.players) &&
                Objects.equals(firebaseKey, game.firebaseKey);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, password, deviceId, questionNr, quizId, running, players, firebaseKey);
    }

    @Override
    public String toString() {
        return "Game{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", questionNr=" + questionNr +
                ", quizId='" + quizId + '\'' +
                ", running=" + running +
                ", players=" + players +
                ", firebaseKey='" + firebaseKey + '\'' +
                '}';
    }
}
