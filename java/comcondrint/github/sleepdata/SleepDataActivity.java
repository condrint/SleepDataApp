package comcondrint.github.sleepdata;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;

import java.util.Map;

import static comcondrint.github.sleepdata.R.id.sleepdatadisplay;

public class SleepDataActivity extends AppCompatActivity {

    public static TextView sleepdata;
    EditText DeleteText;
    AlertDialog formatAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_data);
        sleepdata = (TextView)findViewById(sleepdatadisplay);
        SleepDataActivity.sleepdata.setText(MainActivity.pref.getAll().toString());
        DeleteText = (EditText)findViewById(R.id.DeleteIndex);

        formatAlert = new AlertDialog.Builder(SleepDataActivity.this).create();
        formatAlert.setMessage("Format must be an integer that exists in the data");

    }

    public void delete (View view)
    {
        if (checkFormat(view))
        {
            Integer index = Integer.parseInt(DeleteText.getText().toString());

        }
        else
        {
            formatAlert.show();
        }
    }

    public boolean checkFormat(View view)
    {
        return true;
    }
}
