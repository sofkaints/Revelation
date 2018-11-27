package com.yenusoft.revelation.Dictionary;

import java.util.ArrayList;

/**
 * Created by SofKaints on 2017. 7. 11..
 */

public class Chapter
{
    private int chapter;
    private String content; //요한계시록
    private String abbreviation; //줄임말
    private ArrayList<Verse> vers;

    public Chapter(int chapter, String content, String abb)
    {
        this.chapter = chapter;
        this.content = content;
        this.abbreviation = abb;
        this.vers = new ArrayList<>();
    }

    public int getChapter() {
        return chapter;
    }

    public String getContent() {
        return content;
    }

    public String getAbbreviation() {
        return abbreviation;
    }


    public ArrayList<Verse> getVers() {
        return vers;
    }
}
