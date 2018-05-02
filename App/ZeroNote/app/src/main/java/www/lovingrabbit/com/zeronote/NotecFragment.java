package www.lovingrabbit.com.zeronote;


import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Loader;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.lovingrabbit.com.zeronote.Adapter.AllNote;
import www.lovingrabbit.com.zeronote.Adapter.Notec;
import www.lovingrabbit.com.zeronote.Adapter.NotecAdapter;
import www.lovingrabbit.com.zeronote.tools.GetAsyncTaskLoader;
import www.lovingrabbit.com.zeronote.tools.ItemClickSupport;


public class NotecFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>{
    private static final String TAG = "NotecFragment";
    @BindView(R.id.notec_rcy)
    RecyclerView recyclerView;
    NotecAdapter adapter;
    LoaderManager loaderManager;
    GestureDetector mGestureDetector;
    List<Notec> notecList =  new ArrayList<Notec>();
    Context context;
    Activity activity;
    String GET_NOTEC_URL = "http://47.93.222.179/ZeroNote/api/Notec/getNotec";

    public NotecFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loaderManager = getLoaderManager();
        loaderManager.initLoader(0,null,NotecFragment.this);
        SharedPreferences sharedPreferences = activity.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String userID = sharedPreferences.getString("mobile","");
        if (!userID.equals("")){
            GET_NOTEC_URL = GET_NOTEC_URL +"?mobile="+userID;
        }
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        adapter = new NotecAdapter(notecList);
        recyclerView.setAdapter(adapter);
        ItemClickSupport.addTo(recyclerView).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                alertDialog();
                return false;
            }
        });
    }
    public void alertDialog() {

        final String[] items = new String[]{
                "共享",
                "离线保存",
                "重命名",
                "移至新的笔记本",
                "添加到快捷方式",
                "删除"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("笔记本选项");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(),which+"",Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notec, container, false);
        ButterKnife.bind(this,view);
//        textView.setText(Html.fromHtml("<font color=\'#858585\'>"+"欢迎"+"</font><font color=\'#f02387\'><U>"+"购物愉快"+"</U></font>"));
        return view;

    }


    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new GetAsyncTaskLoader(context,GET_NOTEC_URL);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.d(TAG, "onLoadFinished: "+data);
        try {
            parseJson(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

    private void parseJson(String data) throws JSONException {
        notecList.clear();
        JSONObject jsonObject = new JSONObject(data);
        JSONArray jsonArray = jsonObject.getJSONArray("search");
        int allCount = jsonObject.getInt("allCount");
        for (int i=0;i<allCount;i++){
            JSONObject get = jsonArray.getJSONObject(i);
            String title = get.getString("notec_name");
            String count = get.getString("sum");
            String updateTime = get.getString("updatetime");
            int notec_id = get.getInt("notec_id");
            Notec notec = new Notec(title,count,notec_id,updateTime);
            notecList.add(notec);

        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
