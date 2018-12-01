package topgrost.mocoquizer.quiz.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import topgrost.mocoquizer.R;
import topgrost.mocoquizer.quiz.model.Question;

public class QuestionContainer extends RelativeLayout {

    private View rootView;

    private List<AnswerView> answerViews = new LinkedList<>();

    public QuestionContainer(Context context) {
        super(context);

        init(context);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.quiz_question, this);
    }

    public Question getQuestion() {
        final Question question = new Question();
        question.setText(((TextView)findViewById(R.id.quizQuestion)).getText().toString());
        return question;
    }
}
