package topgrost.mocoquizer.quiz.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Quiz {

    private String name;
    private List<Question> questions = new LinkedList<>();

    public Quiz() {
    }

    public Quiz(String name, List<Question> questions) {
        this.name = name;
        this.questions = questions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quiz quiz = (Quiz) o;
        return Objects.equals(name, quiz.name) &&
                Objects.equals(questions, quiz.questions);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, questions);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Quiz{");
        sb.append("name='").append(name).append('\'');
        sb.append(", questions=").append(questions);
        sb.append('}');
        return sb.toString();
    }
}
