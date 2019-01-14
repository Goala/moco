package topgrost.mocoquizer.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Objects;

public class Game implements Serializable {

    private String name;
    private String password;
    private String deviceId;
    private int questionNr;
    private int questionCount;
    private int questionTime;
    private String quizId;
    private boolean running;
    private int currentTime;

    private String player1;
    private boolean feed1;
    private int score1;

    private String player2;
    private boolean feed2;
    private int score2;

    private String player3;
    private boolean feed3;
    private int score3;

    private String player4;
    private boolean feed4;
    private int score4;

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

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public int getQuestionTime() {
        return questionTime;
    }

    public void setQuestionTime(int questionTime) {
        this.questionTime = questionTime;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
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

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public boolean isFeed1() {
        return feed1;
    }

    public void setFeed1(boolean feed1) {
        this.feed1 = feed1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public boolean isFeed2() {
        return feed2;
    }

    public void setFeed2(boolean feed2) {
        this.feed2 = feed2;
    }

    public String getPlayer3() {
        return player3;
    }

    public void setPlayer3(String player3) {
        this.player3 = player3;
    }

    public boolean isFeed3() {
        return feed3;
    }

    public void setFeed3(boolean feed3) {
        this.feed3 = feed3;
    }

    public String getPlayer4() {
        return player4;
    }

    public void setPlayer4(String player4) {
        this.player4 = player4;
    }

    public boolean isFeed4() {
        return feed4;
    }

    public void setFeed4(boolean feed4) {
        this.feed4 = feed4;
    }

    public int getScore1() {
        return score1;
    }

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public int getScore2() {
        return score2;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

    public int getScore3() {
        return score3;
    }

    public void setScore3(int score3) {
        this.score3 = score3;
    }

    public int getScore4() {
        return score4;
    }

    public void setScore4(int score4) {
        this.score4 = score4;
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
                questionCount == game.questionCount &&
                questionTime == game.questionTime &&
                running == game.running &&
                currentTime == game.currentTime &&
                feed1 == game.feed1 &&
                score1 == game.score1 &&
                feed2 == game.feed2 &&
                score2 == game.score2 &&
                feed3 == game.feed3 &&
                score3 == game.score3 &&
                feed4 == game.feed4 &&
                score4 == game.score4 &&
                Objects.equals(name, game.name) &&
                Objects.equals(password, game.password) &&
                Objects.equals(deviceId, game.deviceId) &&
                Objects.equals(quizId, game.quizId) &&
                Objects.equals(player1, game.player1) &&
                Objects.equals(player2, game.player2) &&
                Objects.equals(player3, game.player3) &&
                Objects.equals(player4, game.player4) &&
                Objects.equals(firebaseKey, game.firebaseKey);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, password, deviceId, questionNr, questionCount, questionTime, quizId, running, currentTime, player1, feed1, score1, player2, feed2, score2, player3, feed3, score3, player4, feed4, score4, firebaseKey);
    }

    @Override
    public String toString() {
        return "Game{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", questionNr=" + questionNr +
                ", questionCount=" + questionCount +
                ", questionTime=" + questionTime +
                ", quizId='" + quizId + '\'' +
                ", running=" + running +
                ", currentTime=" + currentTime +
                ", player1='" + player1 + '\'' +
                ", feed1=" + feed1 +
                ", score1=" + score1 +
                ", player2='" + player2 + '\'' +
                ", feed2=" + feed2 +
                ", score2=" + score2 +
                ", player3='" + player3 + '\'' +
                ", feed3=" + feed3 +
                ", score3=" + score3 +
                ", player4='" + player4 + '\'' +
                ", feed4=" + feed4 +
                ", score4=" + score4 +
                ", firebaseKey='" + firebaseKey + '\'' +
                '}';
    }
}
