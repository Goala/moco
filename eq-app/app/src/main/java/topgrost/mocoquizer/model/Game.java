package topgrost.mocoquizer.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Game implements Serializable {

    private String name;
    private String password;
    private String deviceId;
    private String quizId;
    private boolean running;
    private ArrayList<String> userIds;
    private ArrayList<String> feedbackToUserIds;

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

    public ArrayList<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(ArrayList<String> userIds) {
        this.userIds = userIds;
    }

    public ArrayList<String> getFeedbackToUserIds() {
        return feedbackToUserIds;
    }

    public void setFeedbackToUserIds(ArrayList<String> feedbackToUserIds) {
        this.feedbackToUserIds = feedbackToUserIds;
    }

    @Override
    public String toString() {
        return "Game{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", quizId='" + quizId + '\'' +
                ", running=" + running +
                ", userIds=" + userIds +
                ", feedbackToUserIds=" + feedbackToUserIds +
                '}';
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
                Objects.equals(userIds, game.userIds) &&
                Objects.equals(feedbackToUserIds, game.feedbackToUserIds);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, password, deviceId, quizId, running, userIds, feedbackToUserIds);
    }
}
