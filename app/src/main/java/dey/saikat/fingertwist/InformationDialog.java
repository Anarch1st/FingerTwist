package dey.saikat.fingertwist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InformationDialog extends Activity implements View.OnClickListener{

    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_dialog);


        Button but =(Button)findViewById(R.id.button3);
        but.setOnClickListener(this);
        tv=(TextView)findViewById(R.id.textView6);
        Intent i=getIntent();
        tv.setText(i.getStringExtra("text"));
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
