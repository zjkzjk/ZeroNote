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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    LoaderManager loaderManager;
    Intent intent;
    String GET_NOTEC = "http://47.93.222.179/ZeroNote/api/Notec/getNotec";

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
                startActivity(intent);
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String userID = sharedPreferences.getString("mobile","");
        if (!userID.equals("")){
            GET_NOTEC = GET_NOTEC +"?mobile="+userID;
        }
        loaderManager = getLoaderManager();
        loaderManager.initLoader(0,null,AddNoteActivity.this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(add_title.getText().toString().equals("")&&add_artical.getText().toString().equals("")){
                    Toast.makeText(this,"标题和内容不能为空",Toast.LENGTH_SHORT).show();
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
        if (id == 0 ){
            return new GetAsyncTaskLoader(AddNoteActivity.this,GET_NOTEC);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.d(TAG, data);

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
