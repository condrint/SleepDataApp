package comcondrint.github.sleepdata;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Map;

import static comcondrint.github.sleepdata.R.id.sleepdatadisplay;

public class SleepDataActivity extends AppCompatActivity {

    public static TextView sleepdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_data);
        sleepdata = (TextView)findViewById(sleepdatadisplay);
        SleepDataActivity.sleepdata.setText(MainActivity.pref.getAll().toString());

    }

    public void refresh (View view)
    {
        SleepDataActivity.sleepdata.setText(MainActivity.pref.getAll().toString());
    }
}
