package com.yenusoft.revelation.Dictionary;

/**
 * Created by SofKaints on 2017. 7. 11..
 */

public class Verse
{
    private int verse = 0;
    private String content = "";

    public Verse(int verse, String content)
    {
        this.verse = verse;
        this.content = content;
    }

    public int getVerse() {
        return verse;
    }

    public String getContent() {
        return content;
    }
}
