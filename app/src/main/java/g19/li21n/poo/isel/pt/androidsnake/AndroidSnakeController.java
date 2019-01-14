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
    private boolean firstMove;
    private static Context mContext;
    private Dir dir;
    private OnBeatListener onBeatListener;

    public static int STEP_TIME = 500,MAX_LEVEL = 4,levelNumber;                      // Milliseconds by step
    private long time;                                      // Current time for next step
    TextView currScore, currLevel, currApples;
    TilePanel panel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContext(this);
        onBeatListener = new OnBeatListener() {
            @Override
            public void onBeat(long beat, long time) {
                    if (dir != null)
                        level.setSnakeDirection(dir);
                    panel.invalidate();
                    if (!level.isFinished())
                        level.step();
                    if (level.getRemainingApples() == 0) {
                        panel.removeHeartbeatListener();
                        alertDialog("Level Terminated", false);

                    }
                    else if (level.snakeIsDead()) {
                        panel.removeHeartbeatListener();
                        alertDialog("Game Over", true);
                    }
                    if(levelNumber>MAX_LEVEL){
                        alertDialog("No more Levels",true);
                    }


                }};

        firstMove = true;
        panel = findViewById(R.id.panel);
        panel.setBackgroundColor(Color.LTGRAY);
        panel.setHeartbeatListener(STEP_TIME, onBeatListener);
        currScore = findViewById(R.id.curr_score);
        currLevel = findViewById(R.id.cur_level);
        currApples = findViewById(R.id.curr_apples);
        loadInitialGame();
        //run();
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
                    if (difY > 0)
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


    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context Context) {
        mContext = Context;
    }

    private void loadInitialGame() {
        ++levelNumber;
        try (InputStream file = this.getResources().openRawResource(R.raw.levels)) {
            if(firstMove) {
                model = new Game(file);
                model.setListener(updater);
                firstMove =false;
            }
            level = model.loadNextLevel(file);
            level.setObserver(updater);
            currLevel.setText(Integer.toString(level.getNumber()));                             // Update status View
            currApples.setText(Integer.toString(level.getRemainingApples()));
            currScore.setText(Integer.toString(model.getScore()));
            panel.setSize(level.getWidth(), level.getHeight());
            for (int l = 0; l < level.getHeight(); l++)                                // Create each tile for each cell
                for (int c = 0; c < level.getWidth(); c++)
                    panel.setTile(l, c, CellTile.tileOf(level.getCell(l, c), this));
        } catch (Loader.LevelFormatException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void alertDialog(String msg, final boolean closeGame) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AndroidSnakeController.this);
        builder.setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (closeGame)
                            finishAndRemoveTask();
                        else {
                            loadInitialGame();
                            panel.setHeartbeatListener(STEP_TIME,onBeatListener);
                        }

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Listener of model (Game and Level) to update View
     */
    private class Updater implements Game.Listener, Level.Observer {
        // Game.Listener
        @Override
        public void scoreUpdated(int score) {
            currScore.setText(Integer.toString(model.getScore()));
        }

        // Level.Listener
        @Override
        public void cellUpdated(int l, int c, Cell cell) {
            panel.invalidate(l, c);
        }

        @Override
        public void cellCreated(int l, int c, Cell cell) {
            panel.setTile(l, c, (CellTile.tileOf(cell, getContext())));
        }

        @Override
        public void cellRemoved(int l, int c) {
            panel.setTile(l, c, new EmptyTile());
        }

        @Override
        public void cellMoved(int fromL, int fromC, int toL, int toC, Cell cell) {
            Tile tile = panel.getTile(fromL, fromC);
            assert !(tile instanceof EmptyTile);
            panel.setTile(toL, toC, tile);
            cellRemoved(fromL, fromC);
            scoreUpdated(model.getScore());
        }

        @Override
        public void applesUpdated(int apples) {
            currApples.setText(Integer.toString(apples));
        }
    }

    private Updater updater = new Updater();
}
