package g19.li21n.poo.isel.pt.androidsnake.view;

import android.graphics.Canvas;
import android.graphics.Color;

import pt.isel.poo.tile.Tile;

public class EmptyTile extends CellTile {
    @Override
    public void draw(Canvas canvas, int side) {

    }

    @Override
    public boolean setSelect(boolean selected) {
        return false;
    }
}
