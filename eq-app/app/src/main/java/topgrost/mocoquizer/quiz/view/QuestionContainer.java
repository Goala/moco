package topgrost.mocoquizer.quiz.view;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import topgrost.mocoquizer.R;
import topgrost.mocoquizer.quiz.model.Question;

public class QuestionContainer extends RelativeLayout {

    private List<AnswerContainer> answerContainers = new LinkedList<>();

    public QuestionContainer(Context context) {
        super(context);

        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.quiz_question, this);

        NumberPicker timeSeconds = findViewById(R.id.quizTimeSeconds);
        timeSeconds.setMinValue(10);
        timeSeconds.setMaxValue(60);
        timeSeconds.setValue(30);

        Button btnAddAnswer = findViewById(R.id.quizBtnAddAnswer);
        btnAddAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AnswerContainer containerToAdd = new AnswerContainer(getContext());
                answerContainers.add(containerToAdd);

                QuestionContainer.this.addView(containerToAdd);
                QuestionContainer.this.invalidate();
            }
        });
    }

    public Question getQuestion() {
        final Question question = new Question();
        question.setText(((TextView)findViewById(R.id.quizQuestion)).getText().toString());

        for (AnswerContainer answerContainer : answerContainers) {
            question.getAnswers().add(answerContainer.getAnswer());
        }
        return question;
    }
}
