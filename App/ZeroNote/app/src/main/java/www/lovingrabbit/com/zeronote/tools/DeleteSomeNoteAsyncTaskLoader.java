package www.lovingrabbit.com.zeronote.tools;

import android.content.AsyncTaskLoader;
import android.content.Context;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class DeleteSomeNoteAsyncTaskLoader extends AsyncTaskLoader<String> {
    Set<Integer> set = new HashSet<Integer>();
    String url ,result;
    public DeleteSomeNoteAsyncTaskLoader(Context context, Set<Integer> set,String url) {
        super(context);
        this.set = set;
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
            user = httpUtils.deleteNote(set);
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
