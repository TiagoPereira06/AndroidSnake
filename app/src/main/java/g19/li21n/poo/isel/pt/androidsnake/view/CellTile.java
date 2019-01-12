package g19.li21n.poo.isel.pt.androidsnake.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import g19.li21n.poo.isel.pt.androidsnake.AndroidSnakeController;
import g19.li21n.poo.isel.pt.androidsnake.R;
import g19.li21n.poo.isel.pt.androidsnake.model.*;
import pt.isel.poo.tile.Img;
import pt.isel.poo.tile.Tile;

public abstract class CellTile implements Tile {
    private static Cell element;
    public static final int SIDE = 1;
    private static Context context;
    private final Paint brush;

    public CellTile(){
        brush=new Paint();
    }
    public CellTile(Cell element) {
        this.element = element;
        brush=new Paint();
    }
    public CellTile(Context ctx){
        brush=new Paint();
        context=ctx;
    }

    public static Tile tileOf(Cell cell, Context ctx) {
        element= cell;
        if (cell instanceof SnakeHeadCell) return new SnakeHeadTile(cell);
        if (cell instanceof SnakeBodyCell) return new SnakeBodyTile(cell);
        if (cell instanceof MouseCell) return new MouseTile(new Img(ctx,R.drawable.mouse));
        if (cell instanceof AppleCell) return new AppleTile(new Img(ctx, R.drawable.apple));
        if (cell instanceof ObstacleCell) return new ObstacleTile(new Img(ctx,R.drawable.bricks));


        return new EmptyTile();
    }
}
