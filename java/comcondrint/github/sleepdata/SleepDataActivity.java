package comcondrint.github.sleepdata;

import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import org.apache.commons.math3.stat.regression.SimpleRegression;



import java.util.Map;

import static comcondrint.github.sleepdata.MainActivity.dataEditor;


public class SleepDataActivity extends AppCompatActivity {

    EditText sleepdata;
    EditText DeleteText;
    AlertDialog formatAlert;
    AlertDialog deleteAlert;
    AlertDialog sizeAlert;
    AlertDialog modelAlert;

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
        sizeAlert = new AlertDialog.Builder(SleepDataActivity.this).create();
        sizeAlert.setMessage("To get more accurate results, enter more data");
        modelAlert = new AlertDialog.Builder(SleepDataActivity.this).create();



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

    public void model(View view)
    {
        int preflength = MainActivity.pref.getAll().keySet().size(); //number of entries
        if (preflength < 30)
        {
          sizeAlert.show();
        }

        SimpleRegression model = new SimpleRegression(true);
        Map <String,?> prefs = MainActivity.pref.getAll();

        for(Map.Entry<String,?> entry : prefs.entrySet()){
            String[] data = entry.getValue().toString().split(" / "); // sleep time will be y ([1]) and rating x ([0])
            model.addData(Double.parseDouble(data[1]), Double.parseDouble((data[0])));
        }

        modelAlert.setMessage("Based on the data entered and a linear model, the ideal quantity of sleep is " + Math.round(model.predict(5.0)*100.0)/100.0 + " hours.");
        modelAlert.show();
    }
}
