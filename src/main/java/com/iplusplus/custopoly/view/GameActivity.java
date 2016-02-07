package com.iplusplus.custopoly.view;

import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;

import com.iplusplus.custopoly.Custopoly;
import com.iplusplus.custopoly.controller.GameController;
import com.iplusplus.custopoly.model.GameTheme;
import com.iplusplus.custopoly.model.MusicPlayerService;
import com.iplusplus.custopoly.model.Utilities;
import com.iplusplus.custopoly.model.gamemodel.element.Board;
import com.iplusplus.custopoly.model.gamemodel.element.Game;
import com.iplusplus.custopoly.model.gamemodel.element.Player;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

/**
 * Corresponds with the game_activity in the mockup.
 * Allows to play a gameFacade, displaying it on the screen. In addition, it allows the communication
   with the gameFacade through a system of buttons
 */

public class GameActivity extends Activity  {

    //Attributes
    private ImageView boardBackgroundImageView;
    private Button buyButton;
    private boolean looping = false;
    //Constants
    private final String BOARDRESOURCE = "activity_game_board_";

    //Test for drawing players
    private TableLayout boardLayout;

    public ArrayList<SquareCell> getCells() {
        return cells;
    }

    private ArrayList<SquareCell> cells;

    public ArrayList<GridLayout> getSkinsViews() {
        return skinsViews;
    }

    private ArrayList<GridLayout> skinsViews;
    private int boardWidth, boardHeight;
    private ImageView squareImageView;
    private Button moreOptionsButton;
    private Button endTurn;

    // //
    // Methods implemented from Android events
    // //

    /**
	* Called when GameActivity is created. It's in charge of creating the activity, loading
	* the gameFacade and initializing all the visual components of the view and display them on
	* the screen
	*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //These will put the View on full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent = getIntent();
        Game game = (Game) intent.getSerializableExtra("game");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setupViews();
        new GameController(this, game);
    }

    public void setEnabledButton(String name, boolean enable) {
        switch (name) {
            case "buyButton":
                buyButton.setEnabled(enable);
                break;
            case "moreOptionsButton":
                moreOptionsButton.setEnabled(enable);
                break;
            case "endTurn":
                endTurn.setEnabled(enable);
                break;
        }
    }

    public void setVisibilityButton(String name, int visibility) {
        switch (name) {
            case "buyButton":
                buyButton.setVisibility(visibility);
                break;
            case "moreOptionsButton":
                moreOptionsButton.setVisibility(visibility);
                break;
            case "endTurn":
                endTurn.setVisibility(visibility);
                break;
        }
    }

    public void setOnClickListenerButton(String name, View.OnClickListener listener) {
        switch (name) {
            case "buyButton":
                buyButton.setOnClickListener(listener);
                break;
            case "moreOptionsButton":
                moreOptionsButton.setOnClickListener(listener);
                break;
            case "endTurn":
                endTurn.setOnClickListener(listener);
                break;
        }
    }

    /**
     * Called when the GameActivity is hidden. Automatically saves the gameFacade.
     *
     * @see android.app.Activity#onPause()
     */

    @Override
    public void onPause() {
        super.onPause();
        //MusicPlayerService.pause();
    }

    /**
     * Called every time the View is resumed. Loads the gameFacade and draw the boardLayout
     * and the players
     *
     * @see android.app.Activity#onResume()
     */
    @Override
    public void onResume() {
        super.onResume();
        //MusicPlayerService.resume();
    }


    /**
     * Defines the behaviour of the back button.
     * It shows a dialog asking the user to confirm if he wants to quit the gameFacade
     */
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.ingame_quitgame_title))
                .setMessage(getString(R.string.ingame_quitgame_message))
                .setPositiveButton(getString(R.string.ingame_buyyesbutton), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent play = new Intent(GameActivity.this, MainActivity.class);
                        startActivity(play);
				        GameActivity.this.finish();
                    }

                })
                .setNegativeButton(getString(R.string.ingame_buynobutton), null)
                .show();
    }

    // //
    // Methods implemented from Game events
    // //

    /**
     * Displays the skins of the players according to their current position
	 * in the boardLayout. If there are more than one player in the same square, it
	 * spreads the players to the border of the square to fit them in them.
	 * Each player is a view that is include in a FrameLayout (players).
     * @param board
     */
    public void drawPlayers(Board board) {

        int boardHeight = this.boardHeight;
        int boardWidth = this.boardWidth;
        for (int i = 0; i < board.getSize(); i++) {
            this.skinsViews.get(i).removeAllViews();
        }
        for (int i = 0; i < board.getSize(); i++) {
            if (this.cells.get(i).playerSkins.size() != 0) {
                this.cells.get(i).setIndex(i);
                this.cells.get(i).createImageOfPlayersInCell();
                /*this.addContentView(this.cells.get(i).playersViews, new ActionBar.LayoutParams((this.cells.get(i).posX),
                        this.cells.get(i).posY));*/

                    this.addViewToCell(this.cells.get(i).playersViews.get(0), i);

            }
        }
    }

    public void changePlayerOfCell (Player currentPlayer) {
        int i = 0;
        boolean changed = false;
        while (!changed && i < this.cells.size()) {
            if (this.cells.get(i).playerSkins.size() != 0) {
                for (int j = 0; j < this.cells.get(i).playerSkins.size(); j++) {
                    if(this.cells.get(i).playerSkins.get(j).equals(currentPlayer.getSkin().getImageResourceName())) {
                            this.cells.get(i).playerSkins.remove(j);
                            j = this.cells.get(i).playerSkins.size();
                            changed = true;

                    }
                }
            }
            i++;
        }
        this.cells.get(currentPlayer.getLandIndex()).playerSkins.add(currentPlayer.getSkin().getImageResourceName());
    }

   /* public void SetupBoard () {
        for (int i = 0; i < this.cells.size(); i++) {
            if(this.cells.get(i).playerSkins.size() > 0) {
                for (int j = 0; j < this.cells.get(i).playersViews.size(); j++) {
                    this.cells.get(i).playersViews.remove(j);
                }
                this.cells.get(i).createImageOfPlayersInCell();

            }
        }
    }*/

    public void addViewToCell (ImageView view, int index) {
        synchronized (this) {
           // if(Thread.currentThread().getName() == Looper.getMainLooper().getThread().getName()) {
                this.skinsViews.get(index).addView(view);
           // }

            }
    }
    /*private boolean isSharedSquare(ArrayList<Player> players, int landIndex) {
        int i = 0;
        for (Player p:players) {
            if (p.getLandIndex() == landIndex)
                i++;
        }
        return i > 1;
    }/*

    /**
     * Displays the boardLayout of the theme on the image of the boardLayout
     * @param theme
     */
    public void drawBoard(GameTheme theme) {
        this.boardBackgroundImageView.setImageResource(Utilities.getResId(theme.getBackgroundPathResource(), R.drawable.class));
    }

    /**
     * Initializes all the components of the view
     */
    private void setupViews() {
        this.boardBackgroundImageView = (ImageView) findViewById(R.id.activity_game_iv_boardBackground);
        //Get the dimensions of the boardLayout
        this.boardBackgroundImageView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        this.boardWidth = this.boardBackgroundImageView.getMeasuredWidth();
        this.boardHeight = this.boardBackgroundImageView.getMeasuredHeight();

        this.boardLayout = (TableLayout)findViewById(R.id.activity_game_tl_board);
        this.buyButton = (Button) findViewById(R.id.activity_game_bt_buy);
        this.squareImageView = (ImageView) findViewById(R.id.activity_game_iv_square);
        this.moreOptionsButton  = (Button) findViewById( R.id.activity_game_bt_moreOptions);
        this.endTurn = (Button) findViewById (R.id.activity_game_bt_endTurn);

        this.skinsViews = new ArrayList<>();
        addSkinsViewsLayout();
    }

    public void addEndTurnOnClickListener(View.OnClickListener listener) {
        endTurn.setOnClickListener(listener);
    }

    public void addMoreOptionsButtonOnClickListener(View.OnClickListener listener) {
        moreOptionsButton.setOnClickListener(listener);
    }

    private void addSkinsViewsLayout() {
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_0));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_1));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_2));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_3));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_4));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_5));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_6));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_7));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_8));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_9));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_10));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_11));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_12));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_13));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_14));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_15));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_16));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_17));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_18));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_19));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_20));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_21));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_22));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_23));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_24));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_25));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_26));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_27));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_28));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_29));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_30));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_31));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_32));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_33));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_34));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_35));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_36));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_37));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_38));
        this.skinsViews.add((GridLayout) findViewById(R.id.activity_game_board_39));
    }
	/**
     * Initializes the coordinates, size and position of each square of the map
     */
    public void initSquares(GameTheme currentTheme, Board board, ArrayList<Player> playersList) {

        this.cells = new ArrayList<SquareCell>();

        int bigSquareHeight = (this.boardHeight / 13) * 2; //The height of a big square
        int bigSquareWidth = (this.boardWidth / 13) * 2; //The width of a big square
        int posY = this.boardHeight - bigSquareHeight;
        int posX = this.boardWidth - bigSquareWidth;
        Position edge = Position.DOWN;

        for (int i = 0; i < board.getSize(); i++) {

            //Get the resource of the square
            int resource = currentTheme.getCellResource(i);

            //Create square
            if (i % 10 == 0) {
                //Create big square
                this.cells.add(new SquareCell(posX, posY, bigSquareWidth, bigSquareHeight,resource));
                //Change current edge
                edge = Position.nextPosition(edge);
            } else {
                //Create small square
                switch (edge) {

                    case UP:
                        this.cells.add(new SquareCell(posX, posY, bigSquareWidth / 2, bigSquareHeight,resource));
                        break;
                    case RIGHT:
                        this.cells.add(new SquareCell(posX, posY, bigSquareWidth, bigSquareHeight / 2,resource));
                        break;
                    case DOWN:
                        this.cells.add(new SquareCell(posX, posY, bigSquareWidth / 2, bigSquareHeight,resource));
                        break;
                    case LEFT:
                        this.cells.add(new SquareCell(posX, posY, bigSquareWidth, bigSquareHeight / 2,resource));
                        break;
                }
            }
            //Update position
            switch (edge) {

                case UP:
                    posX += bigSquareWidth / 2;
                    break;
                case RIGHT:
                    posY += bigSquareHeight / 2;
                    break;
                case DOWN:
                    posX -= bigSquareWidth / 2;
                    break;
                case LEFT:
                    posY -= bigSquareHeight / 2;
                    break;
            }
        }

        //Fill in with players
        for (Player p : playersList) {
            this.cells.get(p.getLandIndex()).addPlayerSkin(p.getSkin().getImageResourceName());
        }
    }

    /**
     * Utility function for all functions related to moving the player views.
     * @param player
     *            the player to calculate the position for
     * @return X position relative to the center of the current space, in pixels

    private int calculateSpaceRelativePositionX(Player player, Square sq) {
        int distanceToCenter;
        if (sq.getSize() == Size.BIG)
            distanceToCenter = 12;
        else if (sq.getPos() == Position.DOWN || sq.getPos() == Position.UP)
            distanceToCenter = 8;
        else
            distanceToCenter = 14;
        return Utilities.dpToPx((player.getPlayerID() % 2 == 0 ? 1 : -1) * distanceToCenter,
                this);
    }*/

    /**
     * Utility function for all functions related to moving the player views.
     * Calculates the Y position of a player, so that the players don't all sit
     * on the same spot.
     *
     * @param player
     *            the player to calculate the position for
     * @return Y position in pixels

    private int calculateSpaceRelativePositionY(Player player, Square sq) {
        int distanceToCenter;
        if (sq.getSize() == Size.BIG || sq.getPos() == Position.DOWN || sq.getPos() == Position.UP)
            distanceToCenter = 11;
        else
            distanceToCenter = 6;
        return Utilities.dpToPx((player.getPlayerID() / 2 == 0 ? 1 : -1) * distanceToCenter, this);
    }*/



	/**
	 * Draws the money and name of the current player
     * @param currentPlayer
     */
    public void drawResources(Player currentPlayer) {
		TextView playerText = (TextView) findViewById(R.id.activity_game_tv_player);
		TextView moneyText = (TextView) findViewById(R.id.activity_game_tv_money);

		//We set the text
		playerText.setText(/*"PLAYER" + "\n" + */currentPlayer.getName());
		moneyText.setText(/*"MONEY" + "\n" + */String.valueOf(currentPlayer.getBalance()));

        //Draw square
        int index = currentPlayer.getLandIndex();
        int id = cells.get(index).getResource();
        squareImageView.setImageResource(id);
	}

    public void closeGame() {
        for (SquareCell cell : this.cells) {
            if (cell.t !=null) {
                cell.t.interrupt();
            }
        }
    }

    public void showAlertDialog(AlertDialog alertDialog) {
        alertDialog.show();
    }


    public void passActivity(Intent intent, String nameOfActivity) {
        switch (nameOfActivity) {
            case "PropertiesViewActivity":
                startActivityForResult(intent, 0);
                break;
            case "NegotiationActivity":
                startActivityForResult(intent, 1);
                break;
        }
    }



    public void showFragment(FragmentTransaction ft, String name, DialogFragment fragment) {
        fragment.show(ft, name);
    }
    // //
    //GameObserver Methods
    // //

    private enum Position {
        UP, RIGHT, DOWN, LEFT;

        public static Position nextPosition(Position edge) {
            Position newEdge = DOWN;
            switch (edge) {

                case UP:
                    newEdge = RIGHT;
                    break;
                case RIGHT:
                    newEdge = DOWN;
                    break;
                case DOWN:
                    newEdge = LEFT;
                    break;
                case LEFT:
                    newEdge = UP;
                    break;
            }
            return newEdge;
        }
    }

	/**
	* Inner class that defines the coordinates of the square, its position and its size.
	* It's private as it must be used only in this class
	*/

    public class SquareCell {

        private int posX, posY; //Upper-left coordinates
        private int width, height;
        private int resource;

        public int getIndex() {
            return index;
        }

        private int index;
        private Thread t;

        private ArrayList<ImageView> playersViews;
        private ArrayList<String> playerSkins;


        public ArrayList<ImageView> getPlayersViews() {
            return playersViews;
        }

        public ArrayList<String> getPlayerSkins() {
            return playerSkins;
        }

        public SquareCell(int posX, int posY, int width, int height,int resource) {
            this.posX = posX;
            this.posY = posY;
            this.width = width;
            this.height = height;
            this.resource = resource;
            this.playerSkins = new ArrayList<String>();
            this.playersViews = new ArrayList<ImageView>();
        }

        public void addPlayerSkin(String skin) {
            playerSkins.add(skin);
        }
        public void addPlayerView (ImageView playerView) {this.playersViews.add(playerView);}

        /*private int getIndex() {
            int i = 0;
            while (!cells.get(i).equals(this)) {
                i++;
            }
            return i;
        }*/

        public void addRunnableToThread(Runnable r) {
            if (r != null) {
                if (t != null) {
                    this.t.interrupt();
                }
                this.t = new Thread(r);
            }
        }

        public void createImageOfPlayersInCell() {
            this.playersViews = new ArrayList<ImageView>();
            for (int i = 0; i < this.playerSkins.size(); i++) {
                ImageView playerView = new ImageView(Custopoly.getAppContext());
                playerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                playerView.setImageResource(Utilities.getResId(this.playerSkins.get(i), R.drawable.class));
                addPlayerView(playerView);
            }
            if (this.playerSkins.size() > 1)
                    this.t.start();
            else
                this.t = null;
        }

        public int getResource() {
            return this.resource;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public void removePlayerSkin(String skin) {
            playerSkins.remove(skin);
        }
    }

}
