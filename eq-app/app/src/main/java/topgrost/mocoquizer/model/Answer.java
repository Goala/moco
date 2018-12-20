package topgrost.mocoquizer.model;

import java.io.Serializable;
import java.util.Objects;

public class Answer implements Serializable {

    private String Text;
    private Boolean correct;

    public Answer() {
    }

    public Answer(String text, Boolean correct) {
        Text = text;
        this.correct = correct;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return Objects.equals(Text, answer.Text) &&
                Objects.equals(correct, answer.correct);
    }

    @Override
    public int hashCode() {

        return Objects.hash(Text, correct);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Answer{");
        sb.append("Text='").append(Text).append('\'');
        sb.append(", correct=").append(correct);
        sb.append('}');
        return sb.toString();
    }
}
