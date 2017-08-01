package comcondrint.github.sleepdata;

import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;

import java.util.Map;

import static comcondrint.github.sleepdata.MainActivity.dataEditor;


public class SleepDataActivity extends AppCompatActivity {

    EditText sleepdata;
    EditText DeleteText;
    AlertDialog formatAlert;
    AlertDialog deleteAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_data);
        sleepdata = (EditText) findViewById(R.id.sleepdata);
        sleepdata.setText(MainActivity.pref.getAll().toString());
        DeleteText = (EditText)findViewById(R.id.DeleteIndex);

        dataEditor = MainActivity.pref.edit();

        formatAlert = new AlertDialog.Builder(SleepDataActivity.this).create();
        formatAlert.setMessage("Format must be an integer that exists in the data");
        deleteAlert = new AlertDialog.Builder(SleepDataActivity.this).create();

    }

    public void delete (View view)
    {
        String index = DeleteText.getText().toString();

        if (checkFormat(index))
        {
            MainActivity.delete(index.trim());
            DeleteText.setText("");

            deleteAlert.setMessage("Deleted entry #" + index);
            deleteAlert.show();

            //refresh data
            sleepdata.setText(MainActivity.pref.getAll().toString());

        }
        else
        {

            formatAlert.show();
        }
    }


    public boolean checkFormat(String index)
    {
        boolean hasKey = false;
        Map <String,?> prefs = MainActivity.pref.getAll();
        for (String key : prefs.keySet()) {
            if (key.trim().equals(index.trim())) {
                hasKey = true;
            }
        }
        return hasKey;
    }
}
