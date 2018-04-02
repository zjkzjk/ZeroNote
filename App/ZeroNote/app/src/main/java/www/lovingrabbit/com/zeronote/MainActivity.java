package www.lovingrabbit.com.zeronote;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
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

    boolean isMenuShuffle = true;
    Menu nMenu;
    Intent intent;
    EditText editText;
    MenuItem addNotec, compose;
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

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
        fabMenu.setClosedOnTouchOutside(true);

        fab_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"test",Toast.LENGTH_SHORT).show();
                intent = new Intent(MainActivity.this,AddNoteActivity.class);
                startActivity(intent);
                fabMenu.close(true);
            }
        });
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
                        fragmentChange(new NotecFragment());
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

    }

    public void alertDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialog = inflater.inflate(R.layout.dialog, (ViewGroup) findViewById(R.id.dialog));
        editText = (EditText) dialog.findViewById(R.id.et);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("新建笔记本");
        builder.setView(dialog);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this,editText.getText(),Toast.LENGTH_SHORT).show();
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

}
