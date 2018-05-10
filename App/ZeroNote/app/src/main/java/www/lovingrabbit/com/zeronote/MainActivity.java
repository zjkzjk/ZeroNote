package www.lovingrabbit.com.zeronote;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import www.lovingrabbit.com.zeronote.tools.AddNotecAsyncTaskLoader;
import www.lovingrabbit.com.zeronote.tools.GetAsyncTaskLoader;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{
    private static final String TAG = "MainActivity";
    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.fab_menu)
    FloatingActionMenu fabMenu;
    @BindView(R.id.fab_text)
    FloatingActionButton fab_text;
    @BindView(R.id.main_setting)
    LinearLayout main_setting;

    TextView nav_username,nav_userMobile;
    CircleImageView nav_userPic;
    View draw ;
    boolean isMenuShuffle = true;
    String mobile;
    int user_id;
    Intent intent;
    EditText add_notec;
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };
    LoaderManager loaderManager;
    String ADD_NOTEC = "http://47.93.222.179/ZeroNote/api/Notec/addNotec";
    final String IMG = "http://47.93.222.179/ZeroNote/upload/";
    String get_url ,username,pic;
    final String GET_USER_URL = "http://47.93.222.179/ZeroNote/api/Log/getUser";
    NotecFragment notecFragment;

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), R.string.exit,
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fragmentChange(new AllNoteFragment());
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.menu);
        }
        navigationView.setCheckedItem(R.id.all_note);
        draw = navigationView.inflateHeaderView(R.layout.nav_head);

        int[][] states = new int[][] {

                new int[] { -android.R.attr.state_checked},  // pressed
                new int[] { android.R.attr.state_checked } // selected
        };

        int[] colors = new int[] {
                Color.rgb(10, 0, 0),
                Color.rgb(137, 67, 78)

        };

        ColorStateList myList = new ColorStateList(states, colors);
        navigationView.setItemTextColor(myList);
        navigationView.setItemIconTintList(myList);

        nav_username = draw.findViewById(R.id.user_name);
        nav_userMobile = draw.findViewById(R.id.user_mobile);
        nav_userPic = draw.findViewById(R.id.user_pic);

        fabMenu.setClosedOnTouchOutside(true);

        fab_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this,AddNoteActivity.class);
                startActivity(intent);
                fabMenu.close(true);
            }
        });
        notecFragment = new NotecFragment();
//        fabMenu.addMenuButton(fab_text);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.all_note:
                        isMenuShuffle = true;
                        invalidateOptionsMenu();
                        fragmentChange(new AllNoteFragment());
                        drawerLayout.closeDrawer(navigationView);
                        setTitle(R.string.all_note);
                        break;
                    case R.id.note_book:
                        isMenuShuffle = false;
                        invalidateOptionsMenu();
                        fragmentChange(notecFragment);
                        drawerLayout.closeDrawer(navigationView);
                        setTitle(R.string.note_book);
                        break;
                    case R.id.share_note:
                        Toast.makeText(MainActivity.this, R.string.share_note, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rubbish:
                        Toast.makeText(MainActivity.this, R.string.rubbish, Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        main_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSetting();
            }
        });
        loaderManager = getLoaderManager();
        loaderManager.initLoader(7,null,MainActivity.this);
    }
    public void startSetting(){
        Intent intent = new Intent(MainActivity.this,SettingActivity.class);
        intent.putExtra("pic",pic);
        intent.putExtra("username",username);
        startActivity(intent);
    }

    public void alertDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialog = inflater.inflate(R.layout.dialog, (ViewGroup) findViewById(R.id.dialog));
        add_notec = (EditText) dialog.findViewById(R.id.et);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("新建笔记本");
        builder.setView(dialog);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                add_notec.setText("");
            }

        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!add_notec.getText().toString().equals("")){
                    loaderManager = getLoaderManager();
                    loaderManager.initLoader(0,null,MainActivity.this);
                }
            }
        });
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.action_add_notec:
                alertDialog();
                break;
            case R.id.setting:
                startSetting();
                break;
            default:

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isMenuShuffle) {
            menu.findItem(R.id.action_add_notec).setVisible(false);
            menu.findItem(R.id.action_compose).setVisible(true);
        } else {
            menu.findItem(R.id.action_add_notec).setVisible(true);
            menu.findItem(R.id.action_compose).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //fragment切换
    public void fragmentChange(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_fragment, fragment);
        transaction.commit();
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        mobile = sharedPreferences.getString("mobile","");
        if (id == 0){
            Log.d(TAG, "onCreateLoader: "+add_notec.getText().toString());
            if (!add_notec.getText().toString().equals("")) {
                return new AddNotecAsyncTaskLoader(MainActivity.this, mobile, add_notec.getText().toString()
                        , ADD_NOTEC);
            }
        }else if (id == 7){
            get_url = GET_USER_URL +"?mobile="+mobile;
            return new GetAsyncTaskLoader(MainActivity.this,get_url);
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
        int result = 100;
        JSONObject jsonObject = new JSONObject(data);
        if (jsonObject.has("result")) {
            result = jsonObject.getInt("result");
            if (result == 0){
                Toast.makeText(MainActivity.this,"失败，网络错误",Toast.LENGTH_SHORT).show();
            }else if (result == 1){
                Toast.makeText(MainActivity.this,"创建成功",Toast.LENGTH_SHORT).show();
                notecFragment.reload();
            }else if (result == 2){
                Toast.makeText(MainActivity.this,"失败，笔记本已经存在",Toast.LENGTH_SHORT).show();
            }
            add_notec.setText("");
            loaderManager.destroyLoader(0);
        }else{
            user_id = jsonObject.getInt("id");
            username = jsonObject.getString("username");
            pic = jsonObject.getString("pic");
            Glide.with(this).load(IMG+pic).into(nav_userPic);
            nav_username.setText(username);
            nav_userMobile.setText(mobile);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        Log.d(TAG, "onLoaderReset: ");
    }
}
