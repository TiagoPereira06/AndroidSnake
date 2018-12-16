package g19.li21n.poo.isel.pt.androidsnake;

import android.app.Activity;
import android.os.Bundle;

import pt.isel.poo.tile.TilePanel;

public class AndroidSnakeController extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TilePanel panel = findViewById(R.id.panel);

    }
}
