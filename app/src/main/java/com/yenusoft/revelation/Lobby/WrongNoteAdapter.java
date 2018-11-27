package com.yenusoft.revelation.Lobby;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yenusoft.revelation.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by SofKaints on 2017. 7. 29..
 */

class WrongNoteAdapter extends BaseAdapter
{
    private Context mContext;
    private ArrayList<WrongNote> wrongNotes;
    WrongNoteAdapter(Context mContext, ArrayList<WrongNote> wrongNotes)
    {
        this.mContext = mContext;
        this.wrongNotes = wrongNotes;
    }
    @Override
    public int getCount() {
        return wrongNotes.size();
    }

    @Override
    public WrongNote getItem(int i) {
        return wrongNotes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        Context context = viewGroup.getContext();

        if (view == null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.wn_item, viewGroup, false);
        }

        TextView tv_cv = view.findViewById(R.id.tv_title);
        TextView tv_wrongCount = view.findViewById(R.id.tv_wrongCount);
        String s = String.format(Locale.US,"%s %d:%d",
                mContext.getString(R.string.revel),
                wrongNotes.get(i).getChapter() + 1,
                wrongNotes.get(i).getVerse() + 1);
        tv_cv.setText(s);
        String t = String.format("%s",
                Integer.toString(wrongNotes.get(i).getCount()));
        tv_wrongCount.setText(t);

        return view;
    }
}
