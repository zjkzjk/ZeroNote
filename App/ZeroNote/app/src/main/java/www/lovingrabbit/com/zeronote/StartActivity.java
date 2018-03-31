package www.lovingrabbit.com.zeronote;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends AppCompatActivity {

    private String TAG = "StartActivity";
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_activity);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                String username = sharedPreferences.getString("mobile","");
                if (username.equals("")) {
                    intent = new Intent(StartActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else {
                    intent = new Intent(StartActivity.this, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task,2000);
    }


}

