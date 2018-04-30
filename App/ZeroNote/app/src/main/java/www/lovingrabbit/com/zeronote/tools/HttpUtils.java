package www.lovingrabbit.com.zeronote.tools;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 17922 on 2018/3/24.
 */

public class HttpUtils {
    OkHttpClient okHttpClient;
    public HttpUtils()  {
        okHttpClient = new OkHttpClient();
    }

    public static final MediaType JSON
            = MediaType.parse("application/json;charset=utf-8");

    public String connect_post(String url,String json) throws IOException {
        //把请求的内容字符串转换为json
        RequestBody body = RequestBody.create(JSON, json);
        //RequestBody formBody = new FormEncodingBuilder()

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = okHttpClient.newCall(request).execute();
        String result = response.body().string();
        return result;
    }
    public String connect_get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        String result = response.body().string();
        return result;
    }
    public String login(String mobile,String password) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mobile",mobile);
        jsonObject.put("password",password);
        jsonObject.put("logtype",1);
//        Log.e("test", jsonObject.toString());
        return jsonObject.toString();
    }
    public String register(String mobile,String password,String username) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mobile",mobile);
        jsonObject.put("password",password);
        jsonObject.put("username",username);
        jsonObject.put("registertype",1);
        Log.e("test", jsonObject.toString());
        return jsonObject.toString();
    }
    public String addNotec(String mobile,String notec_name,String notec_desc) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mobile",mobile);
        jsonObject.put("notec_name",notec_name);
        jsonObject.put("notec_desc",notec_desc);
        return jsonObject.toString();
    }


}
