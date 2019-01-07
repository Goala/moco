package topgrost.mocoquizer.browser.view;

import java.util.Comparator;

import topgrost.mocoquizer.model.Game;

public class GamePlayersComparator implements Comparator<Game> {

    @Override
    public int compare(Game o1, Game o2) {
        if (o1 == null) {
            return -1;
        } else if (o2 == null) {
            return 1;
        } else {
            return playerCount(o1) > playerCount(o2) ? 0 : 1 ;
        }
    }

    private int playerCount(Game o) {
        int playerCount = 0;
        if(o.getPlayer1() != null && !o.getPlayer1().trim().isEmpty()) {
            playerCount++;
        }
        if(o.getPlayer2() != null && !o.getPlayer2().trim().isEmpty()) {
            playerCount++;
        }
        if(o.getPlayer3() != null && !o.getPlayer3().trim().isEmpty()) {
            playerCount++;
        }
        if(o.getPlayer4() != null && !o.getPlayer4().trim().isEmpty()) {
            playerCount++;
        }
        return playerCount;
    }
}
