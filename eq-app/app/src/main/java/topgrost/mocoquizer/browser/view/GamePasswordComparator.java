package topgrost.mocoquizer.browser.view;

import java.util.Comparator;

import topgrost.mocoquizer.model.Game;

public class GamePasswordComparator implements Comparator<Game> {

    @Override
    public int compare(Game o1, Game o2) {
        if (o1 == null) {
            return -1;
        } else if (o2 == null) {
            return 1;
        } else {
            return o1.getPassword().compareTo(o2.getPassword());
        }
    }
}
