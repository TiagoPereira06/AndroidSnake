package g19.li21n.poo.isel.pt.androidsnake.model;

public class Cell {


    static Cell newInstance(char type) {
        if (type == 'X') return new ObstacleCell();
        if (type == '@') return new SnakeHeadCell();
        if (type == 'A') return new AppleCell();
        if (type == 'M') return new MouseCell();
        return new EmptyCell();

    }
}