package www.lovingrabbit.com.zeronote.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

import www.lovingrabbit.com.zeronote.R;

public class NotecAdapter extends RecyclerView.Adapter<NotecAdapter.ViewHolder> {
    List<Notec> notecs;
    public NotecAdapter(List<Notec> notecList){
        notecs = notecList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notec_list,parent,false);
        ViewHolder holder = new ViewHolder(view);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Notec notec = notecs.get(position);
        holder.title.setText(notec.getTitle());
        holder.count.setText("一共有"+notec.getCount()+"条笔记");
    }

    @Override
    public int getItemCount() {
        return notecs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,count;
        View view;
        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            title = itemView.findViewById(R.id.notec_title);
            count = itemView.findViewById(R.id.notec_count);
        }
    }
}
