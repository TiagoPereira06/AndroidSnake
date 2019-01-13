package g19.li21n.poo.isel.pt.androidsnake.view;


import android.graphics.Canvas;

import g19.li21n.poo.isel.pt.androidsnake.AndroidSnakeController;
import g19.li21n.poo.isel.pt.androidsnake.R;
import g19.li21n.poo.isel.pt.androidsnake.model.Cell;
import pt.isel.poo.tile.Img;
import pt.isel.poo.tile.Tile;

public class DeadSnakeHeadTile extends CellTile {
    ImageTile img = new ImageTile(new Img(AndroidSnakeController.getContext(),R.drawable.apple)); // TODO:

    @Override
    public void draw(Canvas canvas, int side) {
        img.draw(canvas,side);
    }

    @Override
    public boolean setSelect(boolean selected) {
        return img.setSelect(selected);
    }
}
