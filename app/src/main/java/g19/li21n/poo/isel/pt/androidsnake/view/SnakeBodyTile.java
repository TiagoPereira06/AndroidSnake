package g19.li21n.poo.isel.pt.androidsnake.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import g19.li21n.poo.isel.pt.androidsnake.AndroidSnakeController;
import g19.li21n.poo.isel.pt.androidsnake.R;
import g19.li21n.poo.isel.pt.androidsnake.model.Cell;
import pt.isel.poo.tile.Img;
import pt.isel.poo.tile.Tile;

public class SnakeBodyTile extends CellTile {
    private final Paint brush;
    public SnakeBodyTile(Cell cell) {
        this.brush=new Paint();
    }

    @Override
    public void draw(Canvas canvas, int side) {
        brush.setColor(Color.RED);
        RectF rect=new RectF(0,0,side,side);
        canvas.drawRoundRect(rect,side/2,side/2,brush);
        brush.setColor(Color.BLACK);
        canvas.drawCircle(side/2,side/2,side/4,brush);
    }

    @Override
    public boolean setSelect(boolean selected) {
        return false;
    }
}
