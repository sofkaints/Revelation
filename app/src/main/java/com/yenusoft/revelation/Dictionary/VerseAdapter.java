package com.yenusoft.revelation.Dictionary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yenusoft.revelation.R;

import java.util.ArrayList;

/**
 * Created by SofKaints on 2017. 7. 11..
 */

public class VerseAdapter extends BaseAdapter
{
    ArrayList<Verse> verses;
    public VerseAdapter(ArrayList<Verse> verses)
    {
        this.verses = verses;
    }

    @Override
    public int getCount() {
        return verses.size();
    }

    @Override
    public Object getItem(int i) {
        return verses.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        Context context = viewGroup.getContext();

        if (view == null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.cv_item, viewGroup, false);
        }

        TextView tv_cv = (TextView) view.findViewById(R.id.tv_cv);
        tv_cv.setText(Integer.toString(verses.get(i).getVerse()));
        return view;
    }
}
