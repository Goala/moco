package topgrost.mocoquizer.quiz.view;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import topgrost.mocoquizer.R;
import topgrost.mocoquizer.model.Answer;
import topgrost.mocoquizer.model.Question;
import topgrost.mocoquizer.quiz.QuizEditorActivity;

public class QuestionEditorFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        updateToolbarVisibility(View.GONE);
        return inflater.inflate(R.layout.quiz_editor_question, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TextView questionTextView = view.findViewById(R.id.quizEditorQuestionText);
        questionTextView.requestFocus();

        final Button btnSave = view.findViewById(R.id.quizEditorApply);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        updateToolbarVisibility(View.VISIBLE);
    }

    private void updateToolbarVisibility(int visibility) {
        getActivity().findViewById(R.id.quizEditorAddQuestion).setVisibility(visibility);
        getActivity().findViewById(R.id.quizEditorReset).setVisibility(visibility);
        getActivity().findViewById(R.id.quizEditorSave).setVisibility(visibility);
    }

    @Override
    public void onClick(View v) {
        // Close and remove fragment
        FragmentManager fm = getFragmentManager();
        final FragmentTransaction transaction = fm.beginTransaction();
        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        transaction.remove(this);
        transaction.commit();

        // Add question data
        ((QuizEditorActivity) getActivity()).addQuestion(getQuestionData());
    }

    private Question getQuestionData() {
        final Question question = new Question();
        question.setText(((TextView) getView().findViewById(R.id.quizEditorQuestionText)).getText().toString());

        final Answer answerOne = new Answer();
        answerOne.setText(((TextView) getView().findViewById(R.id.quizEditorAnswerText1)).getText().toString());
        answerOne.setCorrect(((CheckBox) getView().findViewById(R.id.quizEditorQuestionAnswer1)).isChecked());

        final Answer answerTwo = new Answer();
        answerTwo.setText(((TextView) getView().findViewById(R.id.quizEditorAnswerText2)).getText().toString());
        answerTwo.setCorrect(((CheckBox) getView().findViewById(R.id.quizEditorQuestionAnswer2)).isChecked());

        final Answer answerThree = new Answer();
        answerThree.setText(((TextView) getView().findViewById(R.id.quizEditorAnswerText3)).getText().toString());
        answerThree.setCorrect(((CheckBox) getView().findViewById(R.id.quizEditorQuestionAnswer3)).isChecked());

        question.getAnswers().add(answerOne);
        question.getAnswers().add(answerTwo);
        question.getAnswers().add(answerThree);
        return question;
    }
}
