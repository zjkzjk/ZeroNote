package www.lovingrabbit.com.zeronote;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.lovingrabbit.com.zeronote.Adapter.Notec;
import www.lovingrabbit.com.zeronote.Adapter.NotecAdapter;
import www.lovingrabbit.com.zeronote.Adapter.SelectNotecAdapter;
import www.lovingrabbit.com.zeronote.tools.GetAsyncTaskLoader;

public class SelectNotecActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private static final String TAG = "SelectNotecActivity";
    @BindView(R.id.select_note_toolbar)
    Toolbar toolbar;
    @BindView(R.id.select_notec_back)
    LinearLayout back;
    @BindView(R.id.select_notec_rcy)
    RecyclerView recyclerView;
    @BindView(R.id.remove)
    Button remove;
    List<Notec> notecList = new ArrayList<>();
    String get_url = null;
    SelectNotecAdapter adapter;
    String GET_NOTEC_URL = "http://47.93.222.179/ZeroNote/api/Notec/getNotec";
    LoaderManager loaderManager;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_notec);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new SelectNotecAdapter(notecList);
        recyclerView.setAdapter(adapter);

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String userID = sharedPreferences.getString("mobile", "");
        get_url = GET_NOTEC_URL + "?mobile=" + userID;
        loaderManager = getLoaderManager();
        loaderManager.initLoader(0, null, SelectNotecActivity.this);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent(SelectNotecActivity.this,AddNoteActivity.class);
                returnIntent.putExtra("select_id",notecList.get(adapter.getSelectedPos()).getId());
                returnIntent.putExtra("select_name",notecList.get(adapter.getSelectedPos()).getTitle());
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });
    }

    private void parseJson(String data) throws JSONException {
        JSONObject jsonObject = new JSONObject(data);

        notecList.clear();
        JSONArray jsonArray = jsonObject.getJSONArray("search");
        int allCount = jsonObject.getInt("allCount");
        for (int i = 0; i < allCount; i++) {
            JSONObject get = jsonArray.getJSONObject(i);
            String title = get.getString("notec_name");
            String count = get.getString("sum");
            String updateTime = get.getString("updatetime");
            int notec_id = get.getInt("notec_id");
            Notec notec;
            if (id == notec_id) {
                notec = new Notec(title, count, notec_id, updateTime, true);
                adapter.setmSelectedPos(i);
            }else {
                notec = new Notec(title, count, notec_id, updateTime, false);
            }
            notecList.add(notec);
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new GetAsyncTaskLoader(this, get_url);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.d(TAG, "onLoadFinished: " + data);
        try {
            parseJson(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
