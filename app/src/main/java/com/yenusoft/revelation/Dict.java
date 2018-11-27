package com.yenusoft.revelation;

import com.yenusoft.revelation.Dictionary.Chapter;
import com.yenusoft.revelation.Lobby.WrongNote;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by SofKaints on 2017. 7. 11..
 */

public class Dict
{
    static Dict dict = null;
    private ArrayList<Chapter> chapters;
    private ArrayList<WrongNote> wrongNotes;
    private enums.lang lang;
    private int curChapter;
    private int curVerse;
    private int revMaxVerse;
    Dict()
    {
        if (Locale.getDefault().toString().equals("ko") || Locale.getDefault().toString().equals("ko_KR"))
            this.lang = enums.lang.kor;
        else
            this.lang = enums.lang.eng;
        this.chapters = new ArrayList<>();
        this.wrongNotes = new ArrayList<>();
        this.curChapter = 0;
        this.curVerse = 0;
        this.revMaxVerse = 0;
    }
    public static Dict i()
    {
        return dict;
    }

    public ArrayList<Chapter> getChapters() {
        return chapters;
    }

    public int getCurChapter() {
        return curChapter;
    }

    public void setCurChapter(int curChapter) {
        this.curChapter = curChapter;
    }

    public int getCurVerse() {
        return curVerse;
    }

    public void setCurVerse(int curVerse) {
        this.curVerse = curVerse;
    }

    public enums.lang getLang() {
        return lang;
    }

    public int getRevMaxVerse() {
        return revMaxVerse;
    }

    public void setRevMaxVerse(int revMaxVerse) {
        this.revMaxVerse = revMaxVerse;
    }

    public ArrayList<WrongNote> getWrongNotes() {
        return wrongNotes;
    }
}
