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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.lovingrabbit.com.zeronote.tools.LoginAsyncTaskLoader;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{
    @BindView(R.id.login_create_account)
    TextView creat_account;
    @BindView(R.id.login_mobile)
    EditText login_mobile;
    @BindView(R.id.login_password)
    EditText login_password;
    @BindView(R.id.login_button)
    Button login_button;

    LoaderManager loaderManager;
    String mobile,password;
    String POST_LOGIN_URL = "http://47.93.222.179/ZeroNote/api/Log/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        creat_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile = "";
                password = "";
                mobile = login_mobile.getText().toString();
                password = login_password.getText().toString();
                if (mobile.equals("")||password.equals("")){
                    Toast.makeText(LoginActivity.this,"账户或密码不能为空",Toast.LENGTH_SHORT).show();
                }else if (login_mobile.equals("")){
                    loaderManager = getLoaderManager();
                    loaderManager.initLoader(0,null,LoginActivity.this);
                }else{
                    loaderManager = getLoaderManager();
                    loaderManager.restartLoader(0,null,LoginActivity.this);
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
        return new LoginAsyncTaskLoader(LoginActivity.this,POST_LOGIN_URL,mobile,password);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        int log_results = parseJson(data);

        if (log_results == 0 ){
            Toast.makeText(LoginActivity.this,"用户不存在",Toast.LENGTH_SHORT).show();
        }else if (log_results == 1 ){
            Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("mobile",mobile);
            editor.commit();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else if (log_results == 2 ){
            Toast.makeText(LoginActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}

