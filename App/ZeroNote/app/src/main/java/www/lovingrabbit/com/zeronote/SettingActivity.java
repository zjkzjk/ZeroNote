package www.lovingrabbit.com.zeronote;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import www.lovingrabbit.com.zeronote.tools.GetAsyncTaskLoader;

public class SettingActivity extends AppCompatActivity{
    private static final String TAG = "SettingActivity";
    @BindView(R.id.setting_back)
    LinearLayout back;
    @BindView(R.id.setting_mobile)
    TextView setting_mobile;
    @BindView(R.id.setting_username)
    TextView setting_username;
    @BindView(R.id.setting_user)
    CircleImageView setting_user;
    @BindView(R.id.setting_user_infro)
    LinearLayout user_infro;
    final String IMG = "http://47.93.222.179/ZeroNote/upload/";
//
//    String get_url ,mobile;
//    final String GET_USER_URL = "http://47.93.222.179/ZeroNote/api/Log/getUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SharedPreferences sharedPreferences =getSharedPreferences("userInfo",MODE_PRIVATE);
        String mobile = sharedPreferences.getString("mobile","");
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String pic = intent.getStringExtra("pic");
        Glide.with(this).load(IMG+pic).into(setting_user);
        setting_mobile.setText(mobile);
        setting_username.setText(username);
        user_infro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(SettingActivity.this,EditUserInfroActivity.class);
                startActivityForResult(intent1,110);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==110){
            String pic = data.getStringExtra("pic");
            String name = data.getStringExtra("username");
            Log.d(TAG, "onActivityResult: "+pic);
            Log.d(TAG, "onActivityResult: "+name);
            Glide.with(this).load(IMG+pic).into(setting_user);
            setting_username.setText(name);
        }
    }
}
