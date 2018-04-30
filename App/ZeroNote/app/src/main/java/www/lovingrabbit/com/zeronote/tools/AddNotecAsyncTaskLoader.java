package www.lovingrabbit.com.zeronote.tools;

import android.content.AsyncTaskLoader;
import android.content.Context;

import org.json.JSONException;

import java.io.IOException;

public class AddNotecAsyncTaskLoader extends AsyncTaskLoader<String> {
    String mobile,title,url,result;
    public AddNotecAsyncTaskLoader(Context context,String mobile,String title,String url) {
        super(context);
        this.mobile = mobile;
        this.title = title;
        this.url = url;
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
            user = httpUtils.addNotec(mobile,title,title);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            result = httpUtils.connect_post(url, user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
