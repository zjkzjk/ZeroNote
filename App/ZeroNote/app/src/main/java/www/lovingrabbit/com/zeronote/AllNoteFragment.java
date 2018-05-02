package www.lovingrabbit.com.zeronote;


import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.lovingrabbit.com.zeronote.Adapter.AllNote;
import www.lovingrabbit.com.zeronote.Adapter.AllNoteAdapter;
import www.lovingrabbit.com.zeronote.R;
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
    AllNoteAdapter adapter;
    List<AllNote> allNotes = new ArrayList<AllNote>();
    Context context;
    Activity activity;

    LoaderManager loaderManager;
    String GET_NOTE_URL = "http://47.93.222.179/ZeroNote/api/Note/getNote";
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences sharedPreferences = activity.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String mobile = sharedPreferences.getString("mobile","");
        GET_NOTE_URL = GET_NOTE_URL + "?mobile="+mobile;
        loaderManager = getLoaderManager();
        loaderManager.initLoader(0,null,AllNoteFragment.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new AllNoteAdapter(allNotes);
        recyclerView.setAdapter(adapter);
        //长按选项
        ItemClickSupport.addTo(recyclerView).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                return false;
            }
        });
        //获取当前屏幕第一可见item的position
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    position = manager.findFirstCompletelyVisibleItemPosition();
                    Log.d(TAG, "onScrollStateChanged: "+position);
                }
            }
        });

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

        return new GetAsyncTaskLoader(context,GET_NOTE_URL);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        try {
            parseJson(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

    private void parseJson(String data) throws JSONException {
        allNotes.clear();
        JSONObject jsonObject = new JSONObject(data);
        JSONArray jsonArray = jsonObject.getJSONArray("search");
        int allCount = jsonObject.getInt("allCount");
        for (int i=allCount-1;i>=0;i--){
            JSONObject get = jsonArray.getJSONObject(i);
            String title = get.getString("note_title");
            String airtical = get.getString("note_body");
            String updateTime = get.getString("updatetime");
            int note_id = get.getInt("note_id");
            AllNote allNote = new AllNote(title, airtical, note_id,updateTime);
            allNotes.add(allNote);

        }
        time_title.setText(allNotes.get(position).getUpdatetime());
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        allNotes.clear();
    }
}
