package g19.li21n.poo.isel.pt.androidsnake.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import g19.li21n.poo.isel.pt.androidsnake.R;
import pt.isel.poo.tile.Img;

public class AppleTile extends CellTile  {
    private final Img image;
    private final Paint brush;

    public AppleTile(Img img){
        this.image = img;
        this.brush = new Paint();
    }
    public AppleTile(Context ctx){
        this.image = new Img(ctx,R.drawable.apple) ;
        this.brush = new Paint();
    }
    @Override
    public void draw(Canvas canvas, int side) {
        image.draw(canvas,side,side,brush);
    }

    @Override
    public boolean setSelect(boolean selected) {
        return false;
    }
}
