package dey.saikat.fingertwist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MaximumTouchSupported extends Activity implements View.OnClickListener {

    private MTView mtView;
    private FrameLayout frm;
    private Button but;
    private LinearLayout linear;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        mtView=new MTView(this);
        frm=new FrameLayout(this);
        frm.addView(mtView);
        but=new Button(this);
        but.setId(R.id.button);
        linear=new LinearLayout(this);
        but.setWidth(300);
        but.setText("Done");
        but.setOnClickListener(this);
        linear.addView(but);
        //frm.addView(mtView);
        FrameLayout.LayoutParams prams=new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
        frm.addView(linear,prams);
        setContentView(frm);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button:
                Intent i=new Intent(this, Game.class);
                startActivity(i);
                break;
        }
    }
}