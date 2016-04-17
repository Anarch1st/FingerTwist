package dey.saikat.fingertwist;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.Random;

public class Game extends Activity implements View.OnClickListener {

    private int N;
    private int MAX_TOUCH;
    private int[] array;
    private TilesAdapter tiles;
    private Button but;
    private TextView p1, p2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        MAX_TOUCH = Integer.parseInt(sharedPreferences.getString("MaxTouch", ""));

        Intent i = new Intent(this, InformationDialog.class);
        i.putExtra("text", ("Your device supports " + MAX_TOUCH + " touches. So each player will get "
                + MAX_TOUCH / 2 + " chances to play." + " Best of luck"));
        startActivityForResult(i, 2);

        startActivityForResult(new Intent(this, OverlayDialog.class), 1);

        but = (Button) findViewById(R.id.button4);
        p1 = (TextView) findViewById(R.id.textView4);
        p2 = (TextView) findViewById(R.id.textView5);
        but.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d("MyApp", data.getStringExtra("result"));
                N = Integer.parseInt(data.getStringExtra("result"));
                array = new int[N * N];
                Random r = new Random();
                int p = r.nextInt(N * N - 1);
                array[p] = 2;
                p1.setTextColor(Color.RED);

                GridView grid = (GridView) findViewById(R.id.grid);
                grid.setNumColumns(N);
                tiles = new TilesAdapter(Game.this, array, p, p1, p2);
                grid.setAdapter(tiles);
            }
        }
    }

    @Override
    public void onClick(View view) {
        this.recreate();
    }
}
