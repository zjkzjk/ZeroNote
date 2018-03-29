package www.lovingrabbit.com.zeronote;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.lovingrabbit.com.zeronote.Adapter.AllNote;
import www.lovingrabbit.com.zeronote.Adapter.AllNoteAdapter;
import www.lovingrabbit.com.zeronote.R;

/**
 * A simple {@link Fragment} subclass.
 */

public class AllNoteFragment extends Fragment {
    @BindView(R.id.all_note_rcy)
    RecyclerView recyclerView;
    AllNoteAdapter adapter;
    List<AllNote> allNotes = new ArrayList<AllNote>();

    public AllNoteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initList();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new AllNoteAdapter(allNotes);
        recyclerView.setAdapter(adapter);
    }

    private void initList() {
        for (int i= 0 ;i<20;i++) {
            AllNote allNote = new AllNote("这是一个测试标题" + i, "18:30 | 2 kb  写了些什么呢~" + i);
            allNotes.add(allNote);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_note, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

}
