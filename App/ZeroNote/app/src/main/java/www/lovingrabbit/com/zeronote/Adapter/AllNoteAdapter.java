package www.lovingrabbit.com.zeronote.Adapter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import www.lovingrabbit.com.zeronote.AllNoteFragment;
import www.lovingrabbit.com.zeronote.EditNoteActivity;
import www.lovingrabbit.com.zeronote.MainActivity;
import www.lovingrabbit.com.zeronote.R;

public class AllNoteAdapter extends RecyclerView.Adapter<AllNoteAdapter.ViewHolder> {
    List<AllNote> allNotes;

    private static final int MYLIVE_MODE_CHECK = 0;
    int mEditMode = MYLIVE_MODE_CHECK;
    int checkSize=0;
    Handler handler;
    Set<Integer> list = new HashSet<Integer>();
    public void setEditMode(int editMode) {
        mEditMode = editMode;
        notifyDataSetChanged();
    }
    public AllNoteAdapter(List<AllNote> allNoteList, Handler handler){
        allNotes = allNoteList;
        this.handler = handler;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_note_list,parent,false);
        final ViewHolder holder = new ViewHolder(view);
            holder.allNoteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mEditMode == MYLIVE_MODE_CHECK) {
                        int position = holder.getAdapterPosition();
                        Intent intent = new Intent(parent.getContext(), EditNoteActivity.class);
                        intent.putExtra("note_id", allNotes.get(position).getId());
                        parent.getContext().startActivity(intent);
                    }else {
                        int position = holder.getAdapterPosition();
                        AllNote allNote = allNotes.get(position);
                        boolean isSelect = allNote.isSelect();
                        if (isSelect){
                            allNote.setSelect(false);
                            list.remove(allNote.getId());
                            checkSize--;
                        }else {
                            allNote.setSelect(true);
                            list.add(allNote.getId());
                            checkSize++;
                        }
                        notifyDataSetChanged();
                        Message message = Message.obtain(handler);
                        Bundle bundle = new Bundle();
                        bundle.putInt("checkSize",checkSize);
                        bundle.putSerializable("list", (Serializable) list);
                        message.setData(bundle);
                        message.sendToTarget();
                    }
                }
            });
        return holder;
    }
    public void removeAll(){
        for (int i =0;i<allNotes.size();i++){
            AllNote allNote = allNotes.get(i);
            allNote.setSelect(false);
        }
        list.clear();
        checkSize =0;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        AllNote allNote = allNotes.get(position);
        holder.title.setText(allNote.getTitle());
        holder.article.setText(Html.fromHtml("<font color=\'#89434e\'>"+allNote.getUpdatetime()+" | "+"</font><font color=\'#858585\'>"+allNote.getArticle()+"</font>"));
        if (mEditMode == MYLIVE_MODE_CHECK) {
            holder.checkbox.setVisibility(View.GONE);
        } else {
            holder.checkbox.setVisibility(View.VISIBLE);
            if(allNote.isSelect()) {
                holder.checkbox.setImageResource(R.drawable.circle_check);
            }else{
                holder.checkbox.setImageResource(R.drawable.circle);
            }
        }
    }

    @Override
    public int getItemCount() {
        return allNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,article;
        View allNoteView;
        ImageView checkbox;
        public ViewHolder(View itemView) {
            super(itemView);
            allNoteView = itemView;
            title = itemView.findViewById(R.id.note_title);
            article = itemView.findViewById(R.id.note_article);
            checkbox = itemView.findViewById(R.id.note_ischeck);
        }
    }
    public void notifyAdapter(List<AllNote> allNotes,boolean isAdd){
        if (!isAdd){
            this.allNotes=allNotes;
        }else {
            this.allNotes.addAll(allNotes);
        }
        notifyDataSetChanged();
    }
    public List<AllNote> getMyLiveList(){
        if (allNotes == null)  {
            allNotes =new ArrayList<>();
        }
        return  allNotes;
    }
    public interface OnSwipeListener {
        void onItemClickListener(int pos,List<AllNote> notes);
    }
}
