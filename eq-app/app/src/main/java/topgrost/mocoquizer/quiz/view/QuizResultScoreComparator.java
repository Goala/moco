package topgrost.mocoquizer.quiz.view;

import android.util.Pair;

import java.util.Comparator;

public class QuizResultScoreComparator implements Comparator<Pair> {

    @Override
    public int compare(Pair o1, Pair o2) {
        if (o1 == null) {
            return -1;
        } else if (o2 == null) {
            return 1;
        } else {
            return ((Integer)o1.second).compareTo((Integer) o2.second);
        }
    }
}
