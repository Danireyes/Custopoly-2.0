package com.iplusplus.custopoly.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.iplusplus.custopoly.model.MusicPlayerService;
import com.iplusplus.custopoly.model.SaveGameHandler;
import com.iplusplus.custopoly.model.gamemodel.element.Game;

import java.io.IOException;

/**
 * Corresponds with the game_menu_activity in the mockup.
 *
 * Allows to create a new game (and to resume one if time allows).
 *
 * Access to the model is doe via ModelFacade.getInstance().[methodname]
 *  EXAMPLE:
 *          ModelFacade.getInstance().switchThemeTo(Themes.THEME1);
 */


public class GameMenuActivity extends Activity {

    private Button newGame;
    private Button loadGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //These will put the View on full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);

        //Associate the components of the XML file to the class attributes
        this.newGame = (Button) findViewById(R.id.activity_game_menu_btn_newGame);
        this.loadGame = (Button) findViewById(R.id.activity_game_menu_btn_loadGame);


        //Define behaviour of New Button when is pressed
        newGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Starts the Pre-Game Activity
                Intent myIntent = new Intent(GameMenuActivity.this, PreGameActivity.class);
                startActivity(myIntent);
                GameMenuActivity.this.finish();

            }
        });

        //Define behaviour of Load Button when is pressed
        loadGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Game game = SaveGameHandler.getInstance().loadGame();
                    if (game != null) {
                        Intent myIntent = new Intent(GameMenuActivity.this, GameActivity.class);
                        myIntent.putExtra("game", game);
                        startActivity(myIntent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "There is no saved game!!!",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent back = new Intent(GameMenuActivity.this, MainActivity.class);
        GameMenuActivity.this.finish();
        startActivity(back);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //MusicPlayerService.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //MusicPlayerService.resume();
    }
}
