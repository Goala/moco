package topgrost.mocoquizer.lobby.view;

import android.content.Context;
import android.graphics.Color;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import de.codecrafters.tableview.TableDataAdapter;
import topgrost.mocoquizer.R;
import topgrost.mocoquizer.model.Game;

import java.util.List;

public class LobbyListAdapter extends TableDataAdapter<String> {

    private static final int DEFAULT_MIN_HEIGHT = 100;
    private static final int DEFAULT_PAD_LEFT = 20;
    private static final int DEFAULT_PAD_TOP = 30;
    private static final int DEFAULT_PAD_RIGHT = 0;
    private static final int DEFAULT_PAD_BOTTOM = 0;

    public LobbyListAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        String players = getRowData(rowIndex);
        switch (columnIndex) {
            case 0:
                return renderNumber(rowIndex, players);
            case 1:
                return renderPlayer(players);
        }
        return null;
    }

    private TextView renderNumber(int rowIndex, String value){
            TextView view = new TextView(getContext());
        if(value != null) {
            view.setText(Integer.toString(rowIndex + 1));
            view.setMinHeight(DEFAULT_MIN_HEIGHT);
            view.setPadding(DEFAULT_PAD_LEFT, DEFAULT_PAD_TOP, DEFAULT_PAD_RIGHT, DEFAULT_PAD_BOTTOM);
            view.setTextColor(Color.WHITE);
        }
        return view;
    }

    private TextView renderPlayer(String value) {
        TextView view = new TextView(getContext());
        view.setText(value);
        view.setMinHeight(DEFAULT_MIN_HEIGHT);
        view.setPadding(DEFAULT_PAD_LEFT, DEFAULT_PAD_TOP, DEFAULT_PAD_RIGHT, DEFAULT_PAD_BOTTOM);
        view.setTextColor(Color.WHITE);
        return view;
    }
}
