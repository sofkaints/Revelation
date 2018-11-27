package com.yenusoft.revelation.Dictionary;

/**
 * Created by SofKaints on 2017. 7. 29..
 */

public class AllRandomData
{
    private int chapter;
    private int verse;

    public AllRandomData(int chapter, int verse)
    {
        this.chapter = chapter;
        this.verse = verse;
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
}
