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
import topgrost.mocoquizer.model.Player;

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
                return renderPlayers(game.getPlayers());
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
        view.setPadding(DEFAULT_PAD_LEFT, DEFAULT_PAD_TOP, DEFAULT_PAD_RIGHT, DEFAULT_PAD_BOTTOM);
        return view;
    }

    private TextView renderPlayers(List<Player> players) {
        int playerCount = 0;
        for (Player player : players) {
            if(player != null && !player.getName().trim().isEmpty()) {
                playerCount++;
            }
        }

        TextView view = new TextView(getContext());
        view.setText(playerCount + PLAYERS_SUFFIX);
        view.setMinHeight(DEFAULT_MIN_HEIGHT);
        view.setPadding(DEFAULT_PAD_LEFT, DEFAULT_PAD_TOP, DEFAULT_PAD_RIGHT, DEFAULT_PAD_BOTTOM);
        return view;
    }

    private View renderPassword(String password) {
        ImageView view = new ImageView(getContext());
        if (password != null && !password.trim().isEmpty()) {
            view.setImageResource(R.mipmap.icons8_sperren_32);
        } else  {
            view.setImageResource(R.mipmap.icons8_entsperren_32);
        }
        view.setPadding(DEFAULT_PAD_LEFT, DEFAULT_PAD_TOP, DEFAULT_PAD_RIGHT, DEFAULT_PAD_TOP);
        return view;
    }
}