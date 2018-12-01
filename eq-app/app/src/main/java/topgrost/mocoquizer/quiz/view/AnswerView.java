package topgrost.mocoquizer.quiz.view;

import android.content.Context;
import android.view.View;

import topgrost.mocoquizer.quiz.model.Answer;

public class AnswerView extends View {

    public AnswerView(Context context) {
        super(context);
    }

    public Answer getAnswer() {
        return new Answer();
    }
}
