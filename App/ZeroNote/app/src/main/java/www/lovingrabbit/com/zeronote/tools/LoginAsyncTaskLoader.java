package www.lovingrabbit.com.zeronote.tools;

import android.content.AsyncTaskLoader;
import android.content.Context;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by 17922 on 2018/3/24.
 */

public class LoginAsyncTaskLoader extends AsyncTaskLoader<String>{
    String mUrl,mMobile,mPass,result;
    public LoginAsyncTaskLoader(Context context,String url,String mobile,String pass) {
        super(context);
        mUrl = url;
        mMobile = mobile;
        mPass = pass;
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
            user = httpUtils.login(mMobile,mPass);
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
