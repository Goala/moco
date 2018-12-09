package topgrost.mocoquizer.quiz.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Question implements Serializable {

    private String text;
    private int time_seconds;
    private List<Answer> answers = new LinkedList<>();

    public Question() {
    }

    public Question(String text, int time_seconds, List<Answer> answers) {
        this.text = text;
        this.time_seconds = time_seconds;
        this.answers = answers;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTime_seconds() {
        return time_seconds;
    }

    public void setTime_seconds(int time_seconds) {
        this.time_seconds = time_seconds;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return time_seconds == question.time_seconds &&
                Objects.equals(text, question.text) &&
                Objects.equals(answers, question.answers);
    }

    @Override
    public int hashCode() {

        return Objects.hash(text, time_seconds, answers);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Question{");
        sb.append("text='").append(text).append('\'');
        sb.append(", time_seconds=").append(time_seconds);
        sb.append(", answers=").append(answers);
        sb.append('}');
        return sb.toString();
    }
}
