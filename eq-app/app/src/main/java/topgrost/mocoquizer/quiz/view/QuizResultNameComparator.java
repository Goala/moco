package topgrost.mocoquizer.quiz.view;

import android.util.Pair;
import topgrost.mocoquizer.model.Game;

import java.util.Comparator;

public class QuizResultNameComparator implements Comparator<Pair> {

    @Override
    public int compare(Pair o1, Pair o2) {
        if (o1 == null) {
            return -1;
        } else if (o2 == null) {
            return 1;
        } else {
            return ((String)o1.first).compareTo((String) o2.first);
        }
    }
}
