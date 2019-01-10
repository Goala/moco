package topgrost.mocoquizer.quiz.view;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import de.codecrafters.tableview.TableDataAdapter;
import topgrost.mocoquizer.R;
import topgrost.mocoquizer.model.Game;

import java.util.List;

public class QuizResultListAdapter extends TableDataAdapter<Pair> {

    private static final int DEFAULT_MIN_HEIGHT = 100;
    private static final int DEFAULT_PAD_LEFT = 20;
    private static final int DEFAULT_PAD_TOP = 30;
    private static final int DEFAULT_PAD_RIGHT = 0;
    private static final int DEFAULT_PAD_BOTTOM = 0;

    public QuizResultListAdapter(Context context, List<Pair> data) {
        super(context, data);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        Pair playerScore = getRowData(rowIndex);
        switch (columnIndex) {
            case 0:
                return render(playerScore.first);
            case 1:
                return render(playerScore.second);
        }
        return null;
    }

    private TextView render(Object value) {
        TextView view = new TextView(getContext());
        view.setText(String.valueOf(value));
        view.setMinHeight(DEFAULT_MIN_HEIGHT);
        view.setPadding(DEFAULT_PAD_LEFT, DEFAULT_PAD_TOP, DEFAULT_PAD_RIGHT, DEFAULT_PAD_BOTTOM);
        return view;
    }
}
