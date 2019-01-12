package g19.li21n.poo.isel.pt.androidsnake;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import g19.li21n.poo.isel.pt.androidsnake.model.*;
import g19.li21n.poo.isel.pt.androidsnake.view.*;
import java.io.FileInputStream;
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

    public static int STEP_TIME=500;                           // Milliseconds by step
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
                if(!level.isFinished()) level.step();
                if (level.isFinished()&&!level.snakeIsDead()){
                    panel.removeHeartbeatListener();
                    /*AlertDialog.Builder builder = new AlertDialog.Builder(AndroidSnakeController.this);
                    builder.setMessage("Next Level")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    run();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                    */
                }
                if (level.isFinished()&& level.snakeIsDead()){
                    panel.removeHeartbeatListener();
/*                    AlertDialog.Builder builder = new AlertDialog.Builder(AndroidSnakeController.this);
                    builder.setMessage("Game Over")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finishAndRemoveTask();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                    */
                }



            }
        });
        currScore = findViewById(R.id.curr_score);
        currLevel = findViewById(R.id.cur_level);
        currApples = findViewById(R.id.curr_apples);
        //run();
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
                    if (difX > 0 )
                        dir = Dir.LEFT;
                    else
                        dir = Dir.RIGHT;

                } else {
                    if (difY > 0 )
                        dir = Dir.UP;
                    else
                        dir = Dir.DOWN;

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
            while ((level = model.loadNextLevel() ) != null )       // Load currLevel model
                /*if (!playLevel() || !win.question("Next Level"))*/ {  // Play currLevel
                //TODO SUBSTITUIR WIN PARA LOG
                   /* win.message("Bye.");*/
                    return;
                }
            /*win.message("No more Levels");*/
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
    private boolean playLevel() {
        // Opens panel of tiles with dimensions appropriate to the current currLevel.
        // Starts the viewer for each model cell.
        // Shows the initial state of all cells in the model.
        int height = level.getHeight(), width = level.getWidth();
        panel.setSize(width,height);                                     // Create view for cells
        //win.clear();                                                    // Clear area of previous Level
        currLevel.setText(Integer.toString(level.getNumber()));                            // Update status View
        currApples.setText(Integer.toString(level.getRemainingApples()));
        currScore.setText(Integer.toString(model.getScore()));
        for (int l = 0; l < height; l++)                                // Create each tile for each cell
            for (int c = 0; c < width; c++) {
                panel.setTile(l, c, CellTile.tileOf(level.getCell(l, c),this));
                panel.invalidate(l, c);
            }

        level.setObserver(updater);                                     // Set listener of currLevel
        time = System.currentTimeMillis();                              // Set step time
        do
            play();                                                      // Process keys and make a step
        while ( !escaped && !level.isFinished() );
        if (escaped || level.snakeIsDead()) return false;
       /* win.message("You win");*/
        return true;                   // Verify win conditions; false: finished without complete
    }

    /**
     * Listener of model (Game and Level) to update View
     */
    private class Updater implements Game.Listener, Level.Observer {
        // Game.Listener
        @Override
        public void scoreUpdated(int score) { currScore.setText(Integer.toString(score)); }
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
        }
        @Override
        public void applesUpdated(int apples) { currApples.setText(Integer.toString(apples)); }
    }
    private Updater updater = new Updater();

    /**
     * Process key pressed and makes one step.
     */
    private void play() {
       /* time += STEP_TIME;                  // Adjust step time
        int key = getKeyPressed();          // Wait a step time and read a key
        if (key > 0) {
            Dir dir = null;
            switch (key) {
                case VK_UP: dir = Dir.UP; break;
                case VK_DOWN: dir = Dir.DOWN; break;
                case VK_LEFT: dir = Dir.LEFT; break;
                case VK_RIGHT: dir = Dir.RIGHT; break;
                case VK_ESCAPE: escaped=true; return;
                case VK_PAUSE:
                    paused = !paused;
                    win.state(paused ? "PAUSED" : null);
                    return;
            }
            if (dir!=null) level.setSnakeDirection(dir);
        }
        if (!paused){
            level.step();
            updateStatus();
        }*/
    }

    private void updateStatus() {
        currScore.setText(model.getScore());
    }


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
}