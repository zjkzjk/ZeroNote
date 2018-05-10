package www.lovingrabbit.com.zeronote;


import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.lovingrabbit.com.zeronote.Adapter.AllNote;
import www.lovingrabbit.com.zeronote.Adapter.AllNoteAdapter;
import www.lovingrabbit.com.zeronote.R;
import www.lovingrabbit.com.zeronote.tools.DeleteSomeNoteAsyncTaskLoader;
import www.lovingrabbit.com.zeronote.tools.GetAsyncTaskLoader;
import www.lovingrabbit.com.zeronote.tools.ItemClickSupport;

/**
 * A simple {@link Fragment} subclass.
 */

public class AllNoteFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {
    private static final String TAG = "AllNoteFragment";
    @BindView(R.id.all_note_rcy)
    RecyclerView recyclerView;
    @BindView(R.id.time_title)
    TextView time_title;
    @BindView(R.id.long_press_show)
    LinearLayout long_show;
    TextView select_num;
    Button deleteAll,selectAll;
    AllNoteAdapter adapter;
    List<AllNote> allNotes = new ArrayList<AllNote>();
    Context context;
    Activity activity;
    int checkSize;
    LoaderManager loaderManager;
    String get_url = null;
    String GET_NOTE_URL = "http://47.93.222.179/ZeroNote/api/Note/getNote";
    String DELETE_NOTE_URL= "http://47.93.222.179/ZeroNote/api/Note/deleteNote";
    int position=0;


    public AllNoteFragment() {
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
    Set<Integer> list;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            checkSize = bundle.getInt("checkSize");
            list = (Set<Integer>) bundle.getSerializable("list");
            for (int i :list){
                Log.d(TAG, "handleMessage: "+i);
            }
            select_num.setText("已选择："+checkSize);
            return false;
        }
    });

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences sharedPreferences = activity.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String mobile = sharedPreferences.getString("mobile","");
        get_url = GET_NOTE_URL + "?mobile="+mobile;
        loaderManager = getLoaderManager();
        loaderManager.initLoader(0,null,AllNoteFragment.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new AllNoteAdapter(allNotes,handler);
        recyclerView.setAdapter(adapter);

        //获取当前屏幕第一可见item的position
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    position = manager.findFirstCompletelyVisibleItemPosition();
                }
            }
        });
        //长按选项
        ItemClickSupport.addTo(recyclerView).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                int mEditMode = 1;
                long_show.setVisibility(View.VISIBLE);
                deleteAll = activity.findViewById(R.id.all_delete);
                selectAll = activity.findViewById(R.id.all_select);
                select_num = activity.findViewById(R.id.select_num);
                adapter.setEditMode(mEditMode);
                selectAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hide();
                    }
                });
                deleteAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loaderManager = getLoaderManager();
                        loaderManager.initLoader(1,null,AllNoteFragment.this);
                    }
                });

                return false;
            }
        });

    }
    public void hide(){
        adapter.setEditMode(0);
        long_show.setVisibility(View.GONE);
        adapter.removeAll();
        select_num.setText("已选择：0");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_note, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        if (id ==0) {
            return new GetAsyncTaskLoader(context, get_url);
        }else if (id == 1){
            return new DeleteSomeNoteAsyncTaskLoader(context,list,DELETE_NOTE_URL);
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
        } catch (ParseException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

    private void parseJson(String data) throws JSONException, ParseException {
        JSONObject jsonObject = new JSONObject(data);
        if (jsonObject.has("result")){
            int log_result = jsonObject.getInt("result");
            if (log_result == 1){
                Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
                for (int id :list){
                    for (int i = 0;i<allNotes.size();i++){
                        if (id == allNotes.get(i).getId()){
                            allNotes.remove(i);
                            break;
                        }
                    }
                }
                hide();
                adapter.notifyDataSetChanged();
            }else {
                Toast.makeText(context,"删除成功失败",Toast.LENGTH_SHORT).show();
            }
        }
        else {
            allNotes.clear();
            JSONArray jsonArray = jsonObject.getJSONArray("search");
            int allCount = jsonObject.getInt("allCount");
            for (int i = allCount - 1; i >= 0; i--) {
                JSONObject get = jsonArray.getJSONObject(i);
                String title = get.getString("note_title");
                String airtical = get.getString("note_body");
                String updateTime = get.getString("updatetime");
                Date date = StringToDate(updateTime);
                String showtime = dateToString(date);
                int note_id = get.getInt("note_id");
                AllNote allNote = new AllNote(title, airtical, note_id, showtime);
                allNotes.add(allNote);
            }
        }
        time_title.setText(allNotes.get(position).getUpdatetime());
    }
    public static Date StringToDate(String time) throws ParseException {
        DateFormat format =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = format.parse(time);
        return date;
    }
    public static String dateToString(Date time){
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat ("yyyy-MM-dd");
        String ctime = formatter.format(time);
        return ctime;
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        allNotes.clear();
        adapter.notifyDataSetChanged();
    }

}
