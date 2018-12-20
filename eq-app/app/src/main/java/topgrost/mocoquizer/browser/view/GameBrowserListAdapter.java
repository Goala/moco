package topgrost.mocoquizer.browser.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;
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
                return renderPlayers(game.getUserIds());
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

    private TextView renderPlayers(ArrayList<String> userIds) {
        int players = 0;
        for (String userId : userIds) {
            if(userId != null && !userId.trim().isEmpty()) {
                players++;
            }
        }

        TextView view = new TextView(getContext());
        view.setText(players + PLAYERS_SUFFIX);
        view.setMinHeight(DEFAULT_MIN_HEIGHT);
        view.setPadding(DEFAULT_PAD_LEFT, DEFAULT_PAD_TOP, DEFAULT_PAD_RIGHT, DEFAULT_PAD_BOTTOM);
        return view;
    }
}
