package www.lovingrabbit.com.zeronote.tools;

import android.content.AsyncTaskLoader;
import android.content.Context;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by 17922 on 2018/3/25.
 */

public class RegisterAsyncTaskLoder extends AsyncTaskLoader<String> {
    String mUrl,mMobile,mPass,result,mUsername;
    public RegisterAsyncTaskLoder(Context context,String url,String mobile,String password,String username) {
        super(context);
        mUrl = url;
        mMobile = mobile;
        mPass = password;
        mUsername = username;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        HttpUtils httpUtils = new HttpUtils();
        String user = null;
        try {
            user = httpUtils.register(mMobile,mPass,mUsername);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            result = httpUtils.connect_post(mUrl, user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
