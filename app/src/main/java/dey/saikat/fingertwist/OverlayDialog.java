package dey.saikat.fingertwist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class OverlayDialog extends Activity implements View.OnClickListener{

    private Button but;
    private EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overlay_dialog);
        but=(Button)findViewById(R.id.button2);
        but.setOnClickListener(this);
        edit=(EditText)findViewById(R.id.editText);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

    }

    @Override
    public void onClick(View view) {
        Log.d("MyApp",String.valueOf(edit.getText()));
        Intent i=new Intent();
        i.putExtra("result",String.valueOf(edit.getText()));
        setResult(Activity.RESULT_OK,i);
        finish();
    }
}
