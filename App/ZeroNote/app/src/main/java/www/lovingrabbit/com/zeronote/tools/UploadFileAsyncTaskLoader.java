package www.lovingrabbit.com.zeronote.tools;

import android.content.AsyncTaskLoader;
import android.content.Context;

public class UploadFileAsyncTaskLoader extends AsyncTaskLoader<String> {
    String url,path;
    public UploadFileAsyncTaskLoader(Context context,String mPath,String mUrl) {
        super(context);
        url = mUrl;
        path = mPath;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        UploadUtil uploadUtil = new UploadUtil();
        String result = uploadUtil.formUpload(url,path);
        return result;
    }
}
