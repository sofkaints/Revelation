package com.yenusoft.revelation.Dictionary;

import android.annotation.SuppressLint;
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

public class ChapterAdapter extends BaseAdapter
{
    private ArrayList<Chapter> chapters;
    public ChapterAdapter(ArrayList<Chapter> chapters)
    {
        this.chapters = chapters;
    }
    @Override
    public int getCount() {
        return chapters.size();
    }

    @Override
    public Object getItem(int i) {
        return chapters.get(i);
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
            view = inflater.inflate(R.layout.cv_item, viewGroup, false);
        }

        TextView tv_cv = (TextView) view.findViewById(R.id.tv_cv);
        tv_cv.setText(Integer.toString(chapters.get(i).getChapter()));
        return view;
    }
}
