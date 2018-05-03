package www.lovingrabbit.com.zeronote.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.List;

import www.lovingrabbit.com.zeronote.R;

public class SelectNotecAdapter extends RecyclerView.Adapter<SelectNotecAdapter.ViewHolder> {
    private static final String TAG = "SelectNotecAdapter";
    private int mSelectedPos=-1;
    List<Notec> notecs;
    public SelectNotecAdapter(List<Notec> notecList){
        notecs = notecList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_notec_list,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position, List<Object> payloads) {
        final ViewHolder contact=  holder;
        if(payloads.isEmpty()) {
            Notec notec = notecs.get(position);
            holder.title.setText(notec.getTitle());
            holder.count.setText("一共有" + notec.getCount() + "条笔记");
            holder.checkBox.setChecked(mSelectedPos==position);
        }else {
            holder.checkBox.setChecked(mSelectedPos==position);
        }
        contact.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectedPos!=position){//当前选中的position和上次选中不是同一个position 执行
                    contact.checkBox.setChecked(true);
                    if(mSelectedPos!=-1){//判断是否有效 -1是初始值 即无效 第二个参数是Object 随便传个int 这里只是起个标志位
                        notifyItemChanged(mSelectedPos,0);
                    }
                    mSelectedPos=position;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notecs.size();
    }
    public int getSelectedPos(){
        return mSelectedPos;
    }
    public void setmSelectedPos(int selectedPos){
        this.mSelectedPos = selectedPos;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,count;
        View view;
        CheckBox checkBox;
        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            title = itemView.findViewById(R.id.notec_title);
            count = itemView.findViewById(R.id.notec_count);
            checkBox = itemView.findViewById(R.id.ischeck);
        }
    }
}
