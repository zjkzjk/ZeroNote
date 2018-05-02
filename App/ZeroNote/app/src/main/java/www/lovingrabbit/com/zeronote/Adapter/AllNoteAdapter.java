package www.lovingrabbit.com.zeronote.Adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import www.lovingrabbit.com.zeronote.R;

public class AllNoteAdapter extends RecyclerView.Adapter<AllNoteAdapter.ViewHolder> {
    List<AllNote> allNotes;

    public AllNoteAdapter(List<AllNote> allNoteList){
        allNotes = allNoteList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_note_list,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AllNote allNote = allNotes.get(position);
        holder.title.setText(allNote.getTitle());
        holder.article.setText(Html.fromHtml("<font color=\'#89434e\'>"+allNote.getUpdatetime()+" | "+"</font><font color=\'#858585\'>"+allNote.getArticle()+"</font>"));
    }

    @Override
    public int getItemCount() {
        return allNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,article;
        View allNoteView;
        public ViewHolder(View itemView) {
            super(itemView);
            allNoteView = itemView;
            title = itemView.findViewById(R.id.note_title);
            article = itemView.findViewById(R.id.note_article);
        }
    }
}
