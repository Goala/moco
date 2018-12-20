package topgrost.mocoquizer.browser.view;

import java.util.Comparator;

import topgrost.mocoquizer.model.Game;

public class GameRunningComparator implements Comparator<Game> {

    @Override
    public int compare(Game o1, Game o2) {
        if (o1 == null) {
            return -1;
        } else if (o2 == null) {
            return 1;
        } else {
            return Boolean.valueOf(o1.isRunning()).compareTo(Boolean.valueOf(o2.isRunning()));
        }
    }
}
