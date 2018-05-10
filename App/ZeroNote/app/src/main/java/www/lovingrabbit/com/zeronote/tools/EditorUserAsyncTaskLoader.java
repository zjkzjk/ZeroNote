package www.lovingrabbit.com.zeronote.tools;

import android.content.AsyncTaskLoader;
import android.content.Context;

import org.json.JSONException;

import java.io.IOException;

public class EditorUserAsyncTaskLoader extends AsyncTaskLoader<String> {
    String mobile,username,pic,url,result;
    public EditorUserAsyncTaskLoader(Context context,String mobile,String username,String pic,String url) {
        super(context);
        this.mobile = mobile;
        this.username = username;
        this.pic = pic;
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        HttpUtils httpUtils = new HttpUtils();
        String user = null;
        try {
            user = httpUtils.editUser(mobile,username,pic);
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
