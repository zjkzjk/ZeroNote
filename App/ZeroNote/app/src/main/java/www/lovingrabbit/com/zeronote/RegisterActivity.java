package www.lovingrabbit.com.zeronote;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.lovingrabbit.com.zeronote.tools.RegisterAsyncTaskLoder;

public class RegisterActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    @BindView(R.id.register_back)
    ImageView back;
    @BindView(R.id.register_button)
    Button register;
    @BindView(R.id.register_mobile)
    EditText register_mobile;
    @BindView(R.id.register_password)
    EditText register_password;
    @BindView(R.id.register_username)
    EditText register_username;
    @BindView(R.id.register_password_again)
    EditText register_password_again;
    String mobile,password,password_again,username;
    LoaderManager loaderManager;
    String POST_REGISTER_URL = "http://47.93.222.179/ZeroNote/api/Log/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile = "";
                password = "";
                password_again = "";
                username = "";
                username = register_username.getText().toString();
                mobile = register_mobile.getText().toString();
                password = register_password.getText().toString();
                password_again = register_password_again.getText().toString();
                Log.e("password", password );
                Log.e("password_again", password_again );

                if (mobile.equals("")||password.equals("")||password_again.equals("")){
                    Toast.makeText(RegisterActivity.this,"账户或密码不能为空",Toast.LENGTH_SHORT).show();
                }else if (username.equals("")){
                    username = mobile;
                }else if (!password.equals(password_again)){
                    Toast.makeText(RegisterActivity.this,"两次的密码不一致",Toast.LENGTH_SHORT).show();
                }else if (register_mobile.equals("")){
                    loaderManager = getLoaderManager();
                    loaderManager.initLoader(0,null,RegisterActivity.this);
                }else {
                    loaderManager = getLoaderManager();
                    loaderManager.restartLoader(0,null,RegisterActivity.this);
                }
            }
        });
    }
    private int parseJson(String JSONData) {
        int results = 3;
        try {
            Log.e("zjkzjk:",JSONData);
            JSONObject jsonObject = new JSONObject(JSONData);
            results = jsonObject.getInt("result");
            Log.e("result:",results+"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }
    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new RegisterAsyncTaskLoder(RegisterActivity.this,POST_REGISTER_URL,mobile,password,username);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        int log_results = parseJson(data);

        if (log_results == 0) {
            Toast.makeText(RegisterActivity.this, "服务器出错", Toast.LENGTH_SHORT).show();
        } else if (log_results == 1) {
            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("mobile", mobile);
            editor.commit();
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (log_results == 2) {
            Toast.makeText(RegisterActivity.this, "用户已存在", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

}
