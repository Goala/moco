package topgrost.mocoquizer.quiz.view;

import android.content.Context;
import android.widget.LinearLayout;

import topgrost.mocoquizer.R;
import topgrost.mocoquizer.quiz.model.Answer;

public class AnswerContainer extends LinearLayout {

    public AnswerContainer(Context context) {
        super(context);

        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.quiz_answer, this);
    }

    public Answer getAnswer () {
        return new Answer();
    }
}
