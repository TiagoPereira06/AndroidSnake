package g19.li21n.poo.isel.pt.androidsnake;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;


import g19.li21n.poo.isel.pt.androidsnake.model.*;
import g19.li21n.poo.isel.pt.androidsnake.view.*;

import java.io.IOException;
import java.io.InputStream;

import pt.isel.poo.tile.OnTileTouchListener;
import pt.isel.poo.tile.*;


public class AndroidSnakeController extends Activity {
    private Game model;                                     // Model of game
    private Level level;                                    // Model of current currLevel
    private boolean escaped = false;
    private boolean paused = false;
    private static Context  mContext;
    private Dir dir;

    public static int STEP_TIME = 500;                      // Milliseconds by step
    private long time;                                      // Current time for next step
    TextView currScore, currLevel, currApples;
    TilePanel panel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContext(this);

        panel = findViewById(R.id.panel);
        panel.setBackgroundColor(Color.LTGRAY);
        panel.setHeartbeatListener(STEP_TIME, new OnBeatListener() {
            @Override
            public void onBeat(long beat, long time) {
                if(dir!=null)
                    level.setSnakeDirection(dir);
                panel.invalidate();
                if(!level.isFinished())
                    level.step();
                if (level.isFinished() && !level.snakeIsDead()){
                    panel.removeHeartbeatListener();
                    alertDialog("Level Terminated",false);

                    // TODO: LOAD NEXT LVL
                }
                if (level.snakeIsDead()){
                    panel.removeHeartbeatListener();
                    alertDialog("Game Over",true);
                }




            }
        });
        currScore = findViewById(R.id.curr_score);
        currLevel = findViewById(R.id.cur_level);
        currApples = findViewById(R.id.curr_apples);
        run();
        loadInitialGame();
        panel.setListener(new OnTileTouchListener() {
            @Override
            public boolean onClick(int xTile, int yTile) {
                return false;
            }

            @Override
            public boolean onDrag(int xFrom, int yFrom, int xTo, int yTo) {
                int difX = xFrom - xTo;
                int difY = yFrom - yTo;
                if (Math.abs(difX) > Math.abs(difY)) {
                    if (difX > 0)
                        dir = Dir.UP;
                    else
                        dir = Dir.DOWN;

                } else {
                    if (difY > 0 )
                        dir = Dir.LEFT;
                    else
                        dir = Dir.RIGHT;
                }
                return true;
            }

            @Override
            public void onDragEnd(int x, int y) {
            }

            @Override
            public void onDragCancel() {
            }
        });

    }

    /**
     * Main game loop.
     * Returns when there are no more levels or the player gives up.
     */
    private void run() {
        try (InputStream file = this.getResources().openRawResource(R.raw.levels)) { // Open description file
            model = new Game(file);                                 // Create game model
            model.setListener(updater);                             // Set listener of game
            level = model.loadNextLevel();
            level.setObserver(updater);
            currLevel.setText(Integer.toString(level.getNumber()));                            // Update status View
            currApples.setText(Integer.toString(level.getRemainingApples()));
            currScore.setText(Integer.toString(model.getScore()));
          // System.out.println("Antes do while");
/*            while ((level = model.loadNextLevel() ) != null )  {     // Load level model
                //if (!playLevel()) {  // Play level

                return;
                //}
                // TODO: LOAD NEXT LEVEL
            }*/
            alertDialog("No More Levels! You won!!",true);
        } catch (Loader.LevelFormatException e) {
            /*win.message(e.getMessage());*/
            System.out.println(e+", Line "+e.getLineNumber()+": "+e.getLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Main loop of each currLevel.
     * @return true - the currLevel has been completed. false - the player has given up.
     */


    /**
     * Listener of model (Game and Level) to update View
     */
    private class Updater implements Game.Listener, Level.Observer {
        // Game.Listener
        @Override
        public void scoreUpdated(int score) { currScore.setText(Integer.toString(model.getScore())); }
        // Level.Listener
        @Override
        public void cellUpdated(int l, int c, Cell cell) { panel.invalidate(l,c); }
        @Override
        public void cellCreated(int l, int c, Cell cell) {
            panel.setTile(l,c,(CellTile.tileOf(cell,getContext())));
        }
        @Override
        public void cellRemoved(int l, int c) { panel.setTile(l,c,new EmptyTile()); }
        @Override
        public void cellMoved(int fromL, int fromC, int toL, int toC, Cell cell) {
            Tile tile = panel.getTile(fromL,fromC);
            assert !(tile instanceof EmptyTile);
            panel.setTile(toL,toC,tile);
            cellRemoved(fromL,fromC);
            scoreUpdated(model.getScore());
        }
        @Override
        public void applesUpdated(int apples) {
            currApples.setText(Integer.toString(apples));
        }
    }
    private Updater updater = new Updater();

    /**
     * Process key pressed and makes one step.
     */


    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context Context) {
        mContext = Context;
    }

    private void loadInitialGame(){
        try(InputStream file = this.getResources().openRawResource(R.raw.levels)) {
            model = new Game(file);
            model.setListener(updater);
            level=model.loadNextLevel();
            level.setObserver(updater);
            currLevel.setText(Integer.toString(level.getNumber()));                             // Update status View
            currApples.setText(Integer.toString(level.getRemainingApples()));
            currScore.setText(Integer.toString(model.getScore()));
            panel.setSize(level.getWidth(),level.getHeight());
            for (int l = 0; l < level.getHeight(); l++)                                // Create each tile for each cell
                for (int c = 0; c < level.getWidth(); c++)
                    panel.setTile(l, c, CellTile.tileOf(level.getCell(l, c), this));
        } catch (Loader.LevelFormatException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void alertDialog(String msg, final boolean closeGame){
        AlertDialog.Builder builder = new AlertDialog.Builder(AndroidSnakeController.this);
        builder.setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (closeGame)
                            finishAndRemoveTask();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}