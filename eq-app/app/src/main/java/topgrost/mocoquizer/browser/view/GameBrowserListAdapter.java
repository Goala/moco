package topgrost.mocoquizer.browser.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;
import topgrost.mocoquizer.R;
import topgrost.mocoquizer.model.Game;

public class GameBrowserListAdapter extends TableDataAdapter<Game> {

    private static final int DEFAULT_MIN_HEIGHT = 100;
    private static final int DEFAULT_PAD_LEFT = 20;
    private static final int DEFAULT_PAD_TOP = 30;
    private static final int DEFAULT_PAD_RIGHT = 0;
    private static final int DEFAULT_PAD_BOTTOM = 0;
    private static final String PLAYERS_SUFFIX = " of 4";

    public GameBrowserListAdapter(Context context, List<Game> data) {
        super(context, data);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        Game game = getRowData(rowIndex);
        switch (columnIndex) {
            case 0:
                return renderName(game.getName());
            case 1:
                return renderRunning(game.isRunning());
            case 2:
                return renderPlayers(game);
            case 3:
                return renderPassword(game.getPassword());
        }
        return null;
    }

    private TextView renderName(String name) {
        TextView view = new TextView(getContext());
        view.setText(name);
        view.setMinHeight(DEFAULT_MIN_HEIGHT);
        view.setPadding(DEFAULT_PAD_LEFT, DEFAULT_PAD_TOP, DEFAULT_PAD_RIGHT, DEFAULT_PAD_BOTTOM);
        return view;
    }

    private TextView renderRunning(boolean running) {
        TextView view = new TextView(getContext());
        view.setText(running ? "Im Spiel" : "In Lobby");
        view.setMinHeight(DEFAULT_MIN_HEIGHT);
        view.setMaxHeight(DEFAULT_MIN_HEIGHT);
        view.setPadding(DEFAULT_PAD_LEFT, DEFAULT_PAD_TOP, DEFAULT_PAD_RIGHT, DEFAULT_PAD_BOTTOM);
        return view;
    }

    private TextView renderPlayers(Game game) {
        int playerCount = 0;
        if(game.getPlayer1() != null && !game.getPlayer1().trim().isEmpty()) {
                playerCount++;
        }
        if(game.getPlayer2() != null && !game.getPlayer2().trim().isEmpty()) {
            playerCount++;
        }
        if(game.getPlayer3() != null && !game.getPlayer3().trim().isEmpty()) {
            playerCount++;
        }
        if(game.getPlayer4() != null && !game.getPlayer4().trim().isEmpty()) {
            playerCount++;
        }

        TextView view = new TextView(getContext());
        view.setText(playerCount + PLAYERS_SUFFIX);
        view.setMinHeight(DEFAULT_MIN_HEIGHT);
        view.setMaxHeight(DEFAULT_MIN_HEIGHT);
        view.setPadding(DEFAULT_PAD_LEFT, DEFAULT_PAD_TOP, DEFAULT_PAD_RIGHT, DEFAULT_PAD_BOTTOM);
        return view;
    }

    private View renderPassword(String password) {
        ImageView view = new ImageView(getContext());
        if (password != null && !password.trim().isEmpty()) {
            view.setImageResource(R.mipmap.icons8_schluessel_2_32);
        }
        view.setScaleType(ImageView.ScaleType.CENTER);
        view.setMinimumHeight(DEFAULT_MIN_HEIGHT);
        view.setMaxHeight(DEFAULT_MIN_HEIGHT);
        view.setPadding(DEFAULT_PAD_LEFT, 0, DEFAULT_PAD_RIGHT, 0);
        return view;
    }
}
