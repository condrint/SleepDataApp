package comcondrint.github.sleepdata;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.apache.commons.math3.stat.regression.SimpleRegression;



import java.util.Map;

import static comcondrint.github.sleepdata.MainActivity.dataEditor;


public class SleepDataActivity extends AppCompatActivity {


    TextView sleepdatatext;

    EditText DeleteText;
    AlertDialog formatAlert;
    AlertDialog deleteAlert;
    AlertDialog sizeAlert;
    AlertDialog modelAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_data);

        sleepdatatext = (TextView) findViewById(R.id.sleepdatatext);
        refresh();



        sleepdatatext.setMovementMethod(new ScrollingMovementMethod());
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

            refresh();

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

        modelAlert.setMessage("Based on the data entered and a linear model, the ideal quantity of sleep is " + Math.round(model.predict(5.0)*100.0)/100.0 + " hours");
        modelAlert.show();
    }

    public void refresh()
    {
        sleepdatatext.setText("");
        Map <String,?> prefs = MainActivity.pref.getAll();
        String alldata[][] = new String[prefs.size()][3];
        int counter = 0;
        for(Map.Entry<String,?> entry : prefs.entrySet()){
            String[] data = entry.getValue().toString().split(" / "); // sleep time will be y ([1]) and rating x ([0])
            alldata[counter][0] = entry.getKey();
            alldata[counter][1] = data[0];
            alldata[counter][2] = data[1];
            counter += 1;
        }

        //sort by original index
        java.util.Arrays.sort(alldata, new java.util.Comparator<String[]>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public int compare(String[] a, String[] b) {
                int a1 = Integer.parseInt(a[0]);
                int b1 = Integer.parseInt(b[0]);
                return Integer.compare(a1, b1);
            }
        });

        for(int i = 0; i < alldata.length; i++){
            sleepdatatext.append("#" + alldata[i][0] + ": " + alldata[i][1] + " hours of sleep rated " + Math.round(Double.parseDouble(alldata[i][2])) + " out of 5 \n\n");
        }

    }
}
