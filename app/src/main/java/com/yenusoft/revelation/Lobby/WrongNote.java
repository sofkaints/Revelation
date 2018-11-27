package com.yenusoft.revelation.Lobby;

/**
 * Created by SofKaints on 2017. 7. 29..
 */

public class WrongNote
{
    private int id;
    private int abb_num;
    private int chapter;
    private int verse;
    private int count;

    public WrongNote(int id, int abb_num, int chapter, int verse, int count)
    {
        this.id = id;
        this.abb_num = abb_num;
        this.chapter = chapter;
        this.verse = verse;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAbb_num() {
        return abb_num;
    }

    public void setAbb_num(int abb_num) {
        this.abb_num = abb_num;
    }

    public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public int getVerse() {
        return verse;
    }

    public void setVerse(int verse) {
        this.verse = verse;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
