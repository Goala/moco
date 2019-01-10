package topgrost.mocoquizer.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.View;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.*;
import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.SortingOrder;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;
import topgrost.mocoquizer.BaseActivity;
import topgrost.mocoquizer.MainActivity;
import topgrost.mocoquizer.R;
import topgrost.mocoquizer.browser.view.*;
import topgrost.mocoquizer.lobby.LobbyActivity;
import topgrost.mocoquizer.model.Game;
import topgrost.mocoquizer.model.Quiz;
import topgrost.mocoquizer.quiz.view.QuizResultListAdapter;
import topgrost.mocoquizer.quiz.view.QuizResultNameComparator;
import topgrost.mocoquizer.quiz.view.QuizResultScoreComparator;

import java.util.LinkedList;
import java.util.List;

public class QuizResultActivity extends BaseActivity implements View.OnClickListener {

    private static final String[] TABLE_HEADERS = {"Name", "Punkte"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.quiz_result);

        initTable();
        loadResults();

        findViewById(R.id.quizResultClose).setOnClickListener(this);
    }

    private void initTable() {
        final SortableTableView<Pair> tableView = findViewById(R.id.quizResultTable);
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(QuizResultActivity.this, TABLE_HEADERS));

        TableColumnWeightModel columnModel = new TableColumnWeightModel(TABLE_HEADERS.length);
        columnModel.setColumnWeight(0, 3);
        columnModel.setColumnWeight(1, 2);
        tableView.setColumnModel(columnModel);

        // setup coloring of rows
        int colorEvenRows = getResources().getColor(R.color.colorPrimaryDark);
        int colorOddRows = getResources().getColor(R.color.colorPrimary);
        tableView.setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(colorEvenRows, colorOddRows));

        // Set comparators to allow sorting
        tableView.setColumnComparator(0, new QuizResultNameComparator());
        tableView.setColumnComparator(1, new QuizResultScoreComparator());

        // Pre sort by use score descending
        tableView.sort(1, SortingOrder.DESCENDING);
    }

    private void loadResults() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference gameRef = database.getReference(Game.class.getSimpleName().toLowerCase() + "s").child(getIntent().getStringExtra(LobbyActivity.GAME_ID_KEY));
        gameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Game game = dataSnapshot.getValue(Game.class);

                List<Pair> playerScores = new LinkedList<>();
                addPlayerScoreContainer(game.getPlayer1(), game.getScore1(), playerScores);
                addPlayerScoreContainer(game.getPlayer2(), game.getScore2(), playerScores);
                addPlayerScoreContainer(game.getPlayer3(), game.getScore3(), playerScores);
                addPlayerScoreContainer(game.getPlayer4(), game.getScore4(), playerScores);

                    final SortableTableView<Pair> tableView = findViewById(R.id.quizResultTable);
                tableView.setDataAdapter(new QuizResultListAdapter(getBaseContext(), playerScores));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(QuizResultActivity.this, "Fehler beim Aktualisieren der Highscore", Toast.LENGTH_LONG).show();
            }

            private void addPlayerScoreContainer(String playerName, int score, List<Pair> playerScores) {
                if(playerName == null ||playerName.trim().isEmpty()) {
                    return;
                }
                playerScores.add(new Pair<>(playerName, Integer.valueOf(score)));
            }
        });
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
