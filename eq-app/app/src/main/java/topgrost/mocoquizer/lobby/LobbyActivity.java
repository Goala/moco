package topgrost.mocoquizer.lobby;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import topgrost.mocoquizer.R;
import topgrost.mocoquizer.model.Game;

public class LobbyActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        Intent intent = getIntent();
        Game game = (Game) intent.getSerializableExtra("game");


        TextView title = findViewById(R.id.lobbyTitle);
        title.setText(game.getName());


        Button lobbyPlayer1 = findViewById(R.id.lobbyPlayer1);
        lobbyPlayer1.setText(game.getPlayers().get(0).getName());
    }
}
