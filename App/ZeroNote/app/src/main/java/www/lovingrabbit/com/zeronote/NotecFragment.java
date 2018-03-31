package www.lovingrabbit.com.zeronote;


import android.app.LoaderManager;
import android.content.DialogInterface;
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
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.lovingrabbit.com.zeronote.Adapter.Notec;
import www.lovingrabbit.com.zeronote.Adapter.NotecAdapter;
import www.lovingrabbit.com.zeronote.tools.ItemClickSupport;


public class NotecFragment extends Fragment {
    @BindView(R.id.notec_rcy)
    RecyclerView recyclerView;
    NotecAdapter adapter;
    LoaderManager loaderManager;
    GestureDetector mGestureDetector;
    List<Notec> notecList =  new ArrayList<Notec>();

    public NotecFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
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

    private void init() {
        for (int i = 0;i<20;i++){
            Notec notec = new Notec("给笔记本取个名字吧~"+i,"0 条 笔 记"+i);
            notecList.add(notec);
        }
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


}
