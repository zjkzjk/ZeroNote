package www.lovingrabbit.com.zeronote.tools;

import android.content.AsyncTaskLoader;
import android.content.Context;

import org.json.JSONException;

import java.io.IOException;

public class AddNoteAsyncTaskLoader extends AsyncTaskLoader<String> {
    String result,url,mobile,note_title,note_body,note_tag,location;
    int isShare,note_type,notec_id;
    public AddNoteAsyncTaskLoader(Context context,String mobile,int notec_id, String note_title,String note_body,
                                  String location, String note_tag,int note_type,int isShare,String url) {
        super(context);
        this.mobile = mobile;
        this.notec_id = notec_id;
        this.note_body = note_body;
        this.location = location;
        this.note_tag = note_tag;
        this.note_title = note_title;
        this.note_type = note_type;
        this.isShare = isShare;
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
            user = httpUtils.addNote(mobile,notec_id,note_title,note_body,note_tag,location,isShare,note_type);
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
