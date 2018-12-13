package topgrost.mocoquizer.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import topgrost.mocoquizer.R;

public class LobbySetupActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lobby_setup);

        Button btnStartGame = findViewById(R.id.lobbySetupStartGame);
        btnStartGame.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // TODO store data in firebase
        startActivity(new Intent(getApplicationContext(), LobbyActivity.class));
    }
}
