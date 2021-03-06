package topgrost.mocoquizer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;


import topgrost.mocoquizer.browser.GameBrowserActivity;
import topgrost.mocoquizer.lobby.LobbySetupActivity;
import topgrost.mocoquizer.quiz.QuizEditorActivity;

public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCreateQuiz = findViewById(R.id.btnCreateQuiz);
        btnCreateQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), QuizEditorActivity.class);
                startActivity(intent);
            }
        });

        Button btnHostQuiz = findViewById(R.id.btnHostQuiz);
        btnHostQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LobbySetupActivity.class);
                startActivity(intent);
            }
        });

        Button btnSearchQuiz = findViewById(R.id.btnSearchQuiz);
        btnSearchQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameBrowserActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        //In der Main Activity soll man den Zurück Button nicht betätigen können, um nicht wieder
        //In das Spiel geworfen zu werden
    }
}
