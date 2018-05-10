package www.lovingrabbit.com.zeronote;

import android.Manifest;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import www.lovingrabbit.com.zeronote.tools.EditorUserAsyncTaskLoader;
import www.lovingrabbit.com.zeronote.tools.GetAsyncTaskLoader;
import www.lovingrabbit.com.zeronote.tools.UploadFileAsyncTaskLoader;

public class EditUserInfroActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{
    private static final String TAG ="EditUserInfroActivity" ;
    @BindView(R.id.edit_user_infro_back)
    LinearLayout back;
    @BindView(R.id.edit_user_infro_pic)
    CircleImageView user_pic;
    @BindView(R.id.edit_user_infro_name)
    EditText editUsername;
    @BindView(R.id.edit_user_infro_username)
    TextView username;
    @BindView(R.id.edit_user_infro_mobile)
    TextView usermobile;
    @BindView(R.id.edit_user_infro_save)
    Button save;
    Uri originalUri;
    LoaderManager loaderManager;
    String path,pic;
    String get_url ,mobile,name;
    final String GET_USER_URL = "http://47.93.222.179/ZeroNote/api/Log/getUser";
    final String UPLOAD_URL = "http://47.93.222.179/ZeroNote/api/Log/uploadFile";
    final String IMG = "http://47.93.222.179/ZeroNote/upload/";
    final String EDIT_USER_URL = "http://47.93.222.179/ZeroNote/api/Log/editUser";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bm = null;
        // 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
        ContentResolver resolver = getContentResolver();
        if(data == null)
            return;

        try {
            originalUri = data.getData(); // 获得图片的uri

            bm = MediaStore.Images.Media.getBitmap(resolver, originalUri); // 显得到bitmap图片

            // 这里开始的第二部分，获取图片的路径：

            String[] proj = { MediaStore.Images.Media.DATA };

            // 好像是android多媒体数据库的封装接口，具体的看Android文档
            @SuppressWarnings("deprecation")
            Cursor cursor = managedQuery(originalUri, proj, null, null, null);
            // 按我个人理解 这个是获得用户选择的图片的索引值
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            // 将光标移至开头 ，这个很重要，不小心很容易引起越界
            cursor.moveToFirst();
            // 最后根据索引值获取图片路径

            path = cursor.getString(column_index);

            if (!path.equals("")) {
                loaderManager = getLoaderManager();
                loaderManager.initLoader(3, null, EditUserInfroActivity.this);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 103)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {

                //权限被授予
                choosePhoto();

            } else
            {
                // Permission Denied
                Toast.makeText(EditUserInfroActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void checkPerm() {
        /**1.在AndroidManifest文件中添加需要的权限。
         *
         * 2.检查权限
         *这里涉及到一个API，ContextCompat.checkSelfPermission，
         * 主要用于检测某个权限是否已经被授予，方法返回值为PackageManager.PERMISSION_DENIED
         * 或者PackageManager.PERMISSION_GRANTED。当返回DENIED就需要进行申请授权了。
         * */
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){ //权限没有被授予
            Log.d(TAG, "checkPerm: ");
            /**3.申请授权
             * @param
             *  @param activity The target activity.（Activity|Fragment、）
             * @param permissions The requested permissions.（权限字符串数组）
             * @param requestCode Application specific request code to match with a result（int型申请码）
             *    reported to {@link OnRequestPermissionsResultCallback#onRequestPermissionsResult(
             *    int, String[], int[])}.
             * */
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    103);


        }else{//权限被授予
            choosePhoto();

            //直接操作
        }

    }
    public void choosePhoto(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 103);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_infro);
        ButterKnife.bind(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishForResult();
                finish();
            }
        });
        user_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPerm();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loaderManager = getLoaderManager();
                loaderManager.initLoader(1,null,EditUserInfroActivity.this);
            }
        });
        loaderManager = getLoaderManager();
        loaderManager.initLoader(0,null,EditUserInfroActivity.this);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        mobile = sharedPreferences.getString("mobile","");
        get_url = GET_USER_URL +"?mobile="+mobile;
        Log.d(TAG, "onCreateLoader: "+get_url);
        if (id == 0){
            return new GetAsyncTaskLoader(this,get_url);
        }else if (id == 3){
            return new UploadFileAsyncTaskLoader(this,path,UPLOAD_URL);
        }else if (id == 1){
            return new EditorUserAsyncTaskLoader(this,mobile,editUsername.getText().toString(),pic,EDIT_USER_URL);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.d(TAG, "onLoadFinished: "+data);
        try {
            parseJson(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        finishForResult();
        finish();
    }

    public void finishForResult(){
        Intent intent = new Intent(EditUserInfroActivity.this,SettingActivity.class);
        intent.putExtra("username",editUsername.getText().toString());
        intent.putExtra("pic",pic);
        setResult(110,intent);
    }
    private void parseJson(String data) throws JSONException {
        JSONObject jsonObject = new JSONObject(data);
        if(jsonObject.has("editResult")){
            int log_result = jsonObject.getInt("editResult");
            if (log_result == 1){
                Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();
                finishForResult();
                finish();
            }else {
                Toast.makeText(this,"修改失败",Toast.LENGTH_SHORT).show();
            }
        }else if(jsonObject.has("upload")){
            pic = jsonObject.getString("upload");
            Toast.makeText(this,"上传成功",Toast.LENGTH_SHORT).show();
            Glide.with(this).load(IMG+pic).into(user_pic);
        }else {
            name = jsonObject.getString("username");
            pic = jsonObject.getString("pic");
            username.setText(name);
            usermobile.setText(mobile);
            editUsername.setText(name);
            if (!pic.equals("/pic")) {
                Glide.with(this).load(IMG + pic).into(user_pic);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
