package g19.li21n.poo.isel.pt.androidsnake.model;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Game {
    private final InputStream input;
    private int score = 0;
    private int levelNumber = 0;
    private Level curLevel = null;
    private Listener listener = null;


    void addScore(int points) {
        score += points;
        if (listener!=null) listener.scoreUpdated(score);
    }

    // --- Methods to be use by Controller ---

    public Game(InputStream levelsFile) {
        input = levelsFile.markSupported() ? levelsFile : new BufferedInputStream(levelsFile);
        input.mark(40*1024);
    }

    public Level loadNextLevel() throws Loader.LevelFormatException {
        try {
            input.reset();
            Scanner in = new Scanner(input);
            curLevel = new Loader(in).load(++levelNumber);
            if (curLevel!=null) {
                curLevel.init(this);
            }
            return curLevel;
        } catch (IOException e) {
            throw new RuntimeException("IOException",e);
        }
    }

    public int getScore() { return score; }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }


    public interface Listener {
        void scoreUpdated(int score);
    }
    public void setListener(Listener l) { listener = l; }
}
