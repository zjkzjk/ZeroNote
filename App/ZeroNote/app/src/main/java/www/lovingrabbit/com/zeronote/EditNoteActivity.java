package www.lovingrabbit.com.zeronote;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.lovingrabbit.com.zeronote.tools.EditNoteAsyncTaskLoader;
import www.lovingrabbit.com.zeronote.tools.GetAsyncTaskLoader;

public class EditNoteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{
    private static final String TAG = "EditNoteActivity";
    @BindView(R.id.edit_note_toolbar)
    Toolbar toolbar;
    @BindView(R.id.select_notec)
    Button btn;
    @BindView(R.id.edit_note_drawerlayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.edit_note_nav)
    NavigationView navigationView;
    @BindView(R.id.edit_note_title)
    EditText edit_title;
    @BindView(R.id.edit_note_artical)
    EditText edit_artical;
    @BindView(R.id.edit_fab)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.edit_back)
    LinearLayout back;
    @BindView(R.id.edit_save)
    Button save;
    Intent intentResult;
    int isShare = 0,note_type = 1;
    String location = "23.03,113.75";
    String note_tag = "测试";
    String get_url;
    String body="",title = "";
//    String note_title,note_body,notec_name;
    private int notec_id = -1;
    int note_id;
    final String GET_ONE_NOTE_URL = "http://47.93.222.179/ZeroNote/api/Note/getOneNote";
    final String EDIT_NOTE_URL = "http://47.93.222.179/ZeroNote/api/Note/editNote";
    LoaderManager loaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        SharedPreferences sharedPreferences = getSharedPreferences("user_infro",MODE_PRIVATE);
        String mobile = sharedPreferences.getString("mobile","");
        Intent intent = getIntent();
        note_id = intent.getIntExtra("note_id",0);
        get_url = GET_ONE_NOTE_URL + "?mobile="+mobile+"&note_id=" + note_id;
        loaderManager = getLoaderManager();
        loaderManager.initLoader(0,null,EditNoteActivity.this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_strat();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loaderManager = getLoaderManager();
                loaderManager.initLoader(1,null,EditNoteActivity.this);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_strat();
                body = edit_artical.getText().toString();
                title = edit_title.getText().toString();
                intentResult = new Intent(EditNoteActivity.this,SelectNotecActivity.class);
                intentResult.putExtra("id",notec_id);
                startActivityForResult(intentResult,111);
            }
        });
    }
    public void edit_strat(){
        save.setVisibility(View.VISIBLE);
        edit_title.setFocusable(true);
        edit_title.setFocusableInTouchMode(true);
        edit_title.requestFocus();
        edit_artical.setFocusable(true);
        edit_artical.setFocusableInTouchMode(true);
        edit_artical.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        floatingActionButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        if (id == 0) {
            return new GetAsyncTaskLoader(this, get_url);
        }else if (id == 1){
            return new EditNoteAsyncTaskLoader(this,note_id ,notec_id, edit_title.getText().toString(),
                    edit_artical.getText().toString(), location,note_tag,note_type,isShare,EDIT_NOTE_URL);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        try {
            parseJson(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseJson(String data) throws JSONException {
        JSONObject jsonObject = new JSONObject(data);
        if (jsonObject.has("result")){
            Log.d(TAG, "parseJson: "+1);
            int result = jsonObject.getInt("result");
            if (result==1){
                Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(this,"修改失败",Toast.LENGTH_SHORT).show();
            }
        }else {
            Log.d(TAG, "parseJson: "+2);
            String note_title = jsonObject.getString("note_title");
            String note_body = jsonObject.getString("note_body");
            if (notec_id == -1) {
                notec_id = jsonObject.getInt("notec_id");
            }
            String notec_name = jsonObject.getString("notec_name");
            if (btn.getText().equals("")) {
                btn.setText(notec_name);
            }
            if (body.equals("")){
                body = note_body;
            }
            if (title.equals("")){
                title = note_title;
            }
            if (body.equals("")){
                body = note_body;
            }
            edit_title.setText(title);
            edit_artical.setText(body);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

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
            Log.d(TAG, "notec_id: "+notec_id);
        }
    }
}
