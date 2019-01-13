package g19.li21n.poo.isel.pt.androidsnake.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import g19.li21n.poo.isel.pt.androidsnake.model.Cell;
import g19.li21n.poo.isel.pt.androidsnake.model.SnakeHeadCell;


public class SnakeHeadTile extends CellTile {
    private final Paint brush;
    private final SnakeHeadCell element;

    public SnakeHeadTile(Cell cell){
        this.element = (SnakeHeadCell)cell;
        this.brush=new Paint();
    }

    @Override
    public void draw(Canvas canvas, int side) {
        brush.setColor(Color.YELLOW);
        canvas.drawCircle(side/2,side/2,side/2,brush);
        brush.setColor(Color.BLACK);
        brush.setStrokeWidth(10);
        /*switch (element.getDir()) {
            case RIGHT: {
                canvas.drawPoint(side-15, 10, brush);
                canvas.drawPoint(side-15, side - 10, brush);
                break;
            }
            case DOWN: {
                canvas.drawPoint(15, side-10, brush);
                canvas.drawPoint(side-15, side - 10, brush);
                break;
            }
            case LEFT: {
                canvas.drawPoint(15, 10, brush);
                canvas.drawPoint(15, side - 10, brush);
                break;
            }
            case UP: {
                canvas.drawPoint(10,15 , brush);
                canvas.drawPoint(side - 10,15 , brush);
                break;
            }

        }
*/
    }

    @Override
    public boolean setSelect(boolean selected) {
        return false;
    }
}
