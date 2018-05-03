package www.lovingrabbit.com.zeronote;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.lovingrabbit.com.zeronote.tools.AddNoteAsyncTaskLoader;
import www.lovingrabbit.com.zeronote.tools.GetAsyncTaskLoader;

public class AddNoteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private static final String TAG = "AddNoteActivity";
    @BindView(R.id.add_note_toolbar)
    Toolbar toolbar;
    @BindView(R.id.select_notec)
    Button btn;
    @BindView(R.id.add_note_drawerlayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.add_note_nav)
    NavigationView navigationView;
    @BindView(R.id.add_note_title)
    EditText add_title;
    @BindView(R.id.add_note_artical)
    EditText add_artical;

    String location = "23.03,113.75";
    LoaderManager loaderManager;
    Intent intent;
    long update_time = 0;
    int notec_id;
    int isShare = 0,note_type = 1;
    String notec_name;
    String note_tag = "测试";
    String get_url=null;
    String GET_NOTEC = "http://47.93.222.179/ZeroNote/api/Notec/getNotec";
    String ADD_NOTE = "http://47.93.222.179/ZeroNote/api/Note/addNote";
    
    List<String> list = new ArrayList<String>(){};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.yes);
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(AddNoteActivity.this,SelectNotecActivity.class);
                intent.putExtra("id",notec_id);
                startActivityForResult(intent,111);
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String userID = sharedPreferences.getString("mobile","");
        get_url = GET_NOTEC +"?mobile="+userID;

        loaderManager = getLoaderManager();
        loaderManager.initLoader(0,null,AddNoteActivity.this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(add_title.getText().toString().equals("")&&add_artical.getText().toString().equals("")){
                    Toast.makeText(this,"标题和内容不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    loaderManager = getLoaderManager();
                    loaderManager.initLoader(1, null, AddNoteActivity.this);
                }
                finish();
                break;
            case R.id.add_note_open:
                drawerLayout.openDrawer(GravityCompat.END);
                Toast.makeText(AddNoteActivity.this,"open",Toast.LENGTH_SHORT).show();
                break;
            default:

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_note_actionbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String mobile = sharedPreferences.getString("mobile","");
        if (id == 0 ){
            return new GetAsyncTaskLoader(AddNoteActivity.this,get_url);
        }else if (id == 1){
            return new AddNoteAsyncTaskLoader(AddNoteActivity.this,mobile,notec_id, add_title.getText().toString(),
                    add_artical.getText().toString(), location,note_tag,note_type,isShare,ADD_NOTE);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        int log_result = 100;
        try {
            log_result = parseJson(data);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (log_result ==100){
            if (btn.getText().equals("")) {
                btn.setText(notec_name);

            }
        }else if (log_result == 0){
            Toast.makeText(AddNoteActivity.this,"发布笔记失败",Toast.LENGTH_SHORT).show();
        }else if (log_result == 1){
            Toast.makeText(AddNoteActivity.this,"新建笔记成功",Toast.LENGTH_SHORT).show();
        }else if (log_result == 1) {
            Toast.makeText(AddNoteActivity.this, "笔记本不存在", Toast.LENGTH_SHORT).show();
        }Log.d(TAG, "onLoadFinished: "+notec_id);

    }

    private int parseJson(String data) throws JSONException, ParseException {
        int result = 100;
        JSONObject jsonObject = new JSONObject(data);
        if (jsonObject.has("result")) {
            result = jsonObject.getInt("result");
        }else {
            JSONArray search = jsonObject.getJSONArray("search");
            int allCount = jsonObject.getInt("allCount");
            for (int i = 0; i < allCount; i++) {
                JSONObject get = search.getJSONObject(i);
                String updateTime = get.getString("updatetime");
                long time = getIntTime(updateTime);
                int nId = get.getInt("notec_id");
                String nName = get.getString("notec_name");
                if (update_time < time) {
                    update_time = time;
                    notec_id = nId;
                    notec_name = nName;
                }
            }
        }
        return result;
    }
    public long getIntTime(String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long time = simpleDateFormat.parse(date).getTime();
        return time;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK) {
            int selectid = data.getIntExtra("select_id",0);
            String name = data.getStringExtra("select_name");
            Log.d(TAG, "onActivityResult: "+name);
            Log.d(TAG, "onActivityResult: "+selectid);
            btn.setText(name);
            notec_id = selectid;
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
