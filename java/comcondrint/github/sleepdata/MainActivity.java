package comcondrint.github.sleepdata;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import java.text.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {


    SharedPreferences pref;
    SharedPreferences.Editor dataEditor;

    EditText RateText;
    EditText WakeText;
    EditText SleepText;

    AlertDialog saveAlert;
    AlertDialog formatAlert;

    public float[] extractData() {

        //Get rating 1-5 to use as value for storage
        float Rating = Float.parseFloat(RateText.getText().toString());

        //Extract difference between sleep/wake time to use as key for storage
        String WakeTime = WakeText.getText().toString();
        String SleepTime = SleepText.getText().toString();

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        float[] pair = new float [2];
        try {
            Date date1 = format.parse(WakeTime);
            Date date2 = format.parse(SleepTime);
            float difference = Math.abs((date2.getTime() - date1.getTime())) * 3600000;
            pair[0] = difference;
            pair[1] = Rating;

        } catch (ParseException e){
            ; //should never get here because I check the format before calling extractdata()
        };
        return pair;
    }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        dataEditor = pref.edit();

        SleepText = (EditText)findViewById(R.id.SleepText);
        WakeText = (EditText)findViewById(R.id.WakeText);
        RateText = (EditText)findViewById(R.id.RateText);

        saveAlert = new AlertDialog.Builder(MainActivity.this).create();
        saveAlert.setMessage("Data saved");

        formatAlert = new AlertDialog.Builder(MainActivity.this).create();
        formatAlert.setMessage("Wrong input format");
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
        if (checkFormat(view)) {
            float[] pair = extractData();
            String amtSleep = Float.toString(pair[0]);
            dataEditor.putInt(amtSleep , Math.round(pair[1]));
            dataEditor.apply();
            RateText.setText("");
            WakeText.setText("");
            SleepText.setText("");
            saveAlert.show();
        } else{
            //wrong format
            formatAlert.show();
        }



    }

    public boolean checkFormat(View view)
    {
        //check if inputs are empty before proceeding
        boolean notEmpty = (!WakeText.getText().toString().isEmpty()) && (!SleepText.getText().toString().isEmpty()) && (!RateText.getText().toString().isEmpty());

        if (notEmpty){
            //check 1-5 for rating, and 24 hr formatted times for sleep/wake
            int Rating = Integer.parseInt(RateText.getText().toString());
            boolean ratingFormat = (Rating >= 1 && Rating <= 5);

            //check 24hr format
            Pattern pattern;
            Matcher matcher;
            pattern = Pattern.compile("([01]?[0-9]|2[0-3]):[0-5][0-9]");
            String WakeTime = WakeText.getText().toString();
            String SleepTime = SleepText.getText().toString();

            return ratingFormat && pattern.matcher(WakeTime).matches() && pattern.matcher(SleepTime).matches();
        } else {
            return false;
        }

    }

}
