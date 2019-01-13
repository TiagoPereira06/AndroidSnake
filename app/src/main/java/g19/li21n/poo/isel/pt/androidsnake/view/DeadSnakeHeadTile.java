package g19.li21n.poo.isel.pt.androidsnake.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import g19.li21n.poo.isel.pt.androidsnake.model.Cell;


public class DeadSnakeHeadTile extends CellTile {
    private final Paint brush;

    public DeadSnakeHeadTile(Cell cell) {
        this.brush = new Paint();
    }
        @Override
        public void draw (Canvas canvas,int side){
            brush.setColor(Color.BLACK);
            canvas.drawCircle(side / 2, side / 2, side / 2, brush);
            brush.setColor(Color.BLACK);
            brush.setStrokeWidth(10);
        }

        @Override
        public boolean setSelect ( boolean selected){
            return false;
        }
    }

