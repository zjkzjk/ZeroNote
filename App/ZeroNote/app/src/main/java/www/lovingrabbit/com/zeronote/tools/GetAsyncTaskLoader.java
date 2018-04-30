package www.lovingrabbit.com.zeronote.tools;

import android.content.AsyncTaskLoader;
import android.content.Context;

import org.json.JSONException;

import java.io.IOException;

public class GetAsyncTaskLoader extends AsyncTaskLoader<String> {
    String url,result;
    public GetAsyncTaskLoader(Context context,String url) {
        super(context);
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
        try {
            result = httpUtils.connect_get(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
