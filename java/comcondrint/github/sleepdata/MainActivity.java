package comcondrint.github.sleepdata;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import java.text.*;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    SharedPreferences SleepData = this.getPreferences(Context.MODE_PRIVATE);
    SharedPreferences.Editor dataEditor = SleepData.edit();


    public float[] extractData() {

        //Get rating 1-5 to use as value for storage
        EditText RateText = (EditText)findViewById(R.id.RateText);
        float Rating = Float.parseFloat(RateText.getText().toString());

        //Extract difference between sleep/wake time to use as key for storage
        EditText WakeText = (EditText)findViewById(R.id.WakeText);
        EditText SleepText = (EditText)findViewById(R.id.SleepText);
        String WakeTime = WakeText.getText().toString();
        String SleepTime = SleepText.getText().toString();

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        float[] A = new float [2];
        try {
            Date date1 = format.parse(WakeTime);
            Date date2 = format.parse(SleepTime);
            float difference = (date2.getTime() - date1.getTime()) * 3600000;
            A[0] = difference;
            A[1] = Rating;

        } catch (ParseException e){

        };
        return A;
    }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToRatings(View view)
    {
        Intent intent = new Intent(MainActivity.this, RatingChart.class);
        startActivity(intent);
    }

    public void goToData(View view)
    {
        Intent intent = new Intent(MainActivity.this, SleepDataActivity.class);
        startActivity(intent);
    }

    public void SaveData(View view)
    {
        //amt of sleep is key, rating of ease of waking is value
        float[] pair = extractData();
        String amtSleep = Float.toString(pair[0]);
        dataEditor.putInt(amtSleep , Math.round(pair[1]));
        dataEditor.apply();
    }

}
