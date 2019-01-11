package topgrost.mocoquizer.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Question implements Serializable {

    private String text;
    private List<Answer> answers = new LinkedList<>();

    public Question() {
    }

    public Question(String text, List<Answer> answers) {
        this.text = text;
        this.answers = answers;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
        return Objects.equals(text, question.text) &&
                Objects.equals(answers, question.answers);
    }

    @Override
    public int hashCode() {

        return Objects.hash(text, answers);
    }

    @Override
    public String toString() {
        return "Question{" +
                "text='" + text + '\'' +
                ", answers=" + answers +
                '}';
    }
}
