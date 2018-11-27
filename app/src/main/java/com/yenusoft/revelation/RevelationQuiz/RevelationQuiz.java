package com.yenusoft.revelation.RevelationQuiz;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yenusoft.revelation.Dict;
import com.yenusoft.revelation.Dictionary.AllRandomData;
import com.yenusoft.revelation.R;
import com.yenusoft.revelation.enums;
import com.yenusoft.revelation.myDB;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

/**
 * Created by SofKaints on 2017. 7. 11..
 */

public class RevelationQuiz extends Activity
{
    private TextView tv_title;
    private EditText et_content;
    private enums.startType startType;
    private ArrayList<Integer> overlapVerse;
    private ArrayList<AllRandomData> overlapARD;
    private int startVerse, maxVerse;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nv_bar);

        ImageButton ib_left = findViewById(R.id.ib_left);
        ib_left.setBackgroundResource(R.drawable.icon_left);
        ib_left.setOnClickListener(quizExit);
        ImageButton ib_right = findViewById(R.id.ib_right);
        ib_right.setBackgroundResource(R.drawable.icon_menu);
        ib_right.setVisibility(View.INVISIBLE);
        FrameLayout fl_content = findViewById(R.id.fl_content);
        this.fcSetView(View.inflate(this, R.layout.quiz, fl_content));
    }

    private void fcSetView(View v)
    {
        tv_title = v.findViewById(R.id.tv_title);
        Button but_clear = v.findViewById(R.id.but_clear);
        Button but_showAnswer = v.findViewById(R.id.but_showAnswer);
        ImageButton but_next = v.findViewById(R.id.but_next);
        ImageButton but_prev = v.findViewById(R.id.but_prev);

        startType = (enums.startType)getIntent().getSerializableExtra("startType");
        overlapARD = new ArrayList<>();
        overlapVerse = new ArrayList<>();
        int curChapter = Dict.i().getCurChapter();
        int curVerse = startVerse = Dict.i().getCurVerse(); //현재 구절
        maxVerse = Dict.i().getChapters().get(curChapter).getVers().size(); //현재 장의 최대 절

        if (startType == enums.startType.random) //if random
        {
            curVerse = fcGetRandomNumber();
            Dict.i().setCurVerse(curVerse);
            but_prev.setVisibility(View.INVISIBLE);
        }
        else if (startType == enums.startType.all_random)
        {
            if (fcIsEndAllRandom())
                finish();

            curChapter = overlapARD.get(overlapARD.size() - 1).getChapter();
            curVerse = overlapARD.get(overlapARD.size() - 1).getVerse();
            Dict.i().setCurChapter(curChapter);
            Dict.i().setCurVerse(curVerse);
            but_prev.setVisibility(View.INVISIBLE);
        }

        tv_title.setText(String.format(Locale.US,"%s %d:%d", Dict.i().getChapters().get(curChapter).getAbbreviation(), curChapter + 1, curVerse + 1));
        et_content = v.findViewById(R.id.et_content);

        but_showAnswer.setOnClickListener(showAnswer);
        but_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contentClear();
            }
        });
        but_next.setOnClickListener(nextEvent());
        but_prev.setOnClickListener(prevEvent);
    }

    private void contentClear()
    {
        et_content.setText("");
    }

    private View.OnClickListener quizExit = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            fcExit();
        }
    };

    private void fcExit()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(RevelationQuiz.this);
        alert.setMessage(R.string.quiz_exit_message);

        alert.setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                fcInit();
                finish();
            }
        });
        alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog ad = alert.create();
        ad.show();
    }

    private View.OnClickListener showAnswer = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder alert = new AlertDialog.Builder(RevelationQuiz.this);
            int curChapter = Dict.i().getCurChapter();
            int curVerse = Dict.i().getCurVerse();
            alert.setTitle(String.format(Locale.US,"%s %d:%d",
                    Dict.i().getChapters().get(curChapter).getAbbreviation(),
                    Dict.i().getCurChapter() + 1,
                    Dict.i().getCurVerse() + 1));
            alert.setMessage(Dict.i().getChapters().get(curChapter).getVers().get(curVerse).getContent());

            alert.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            AlertDialog ad = alert.create();
            ad.show();
        }
    };

    private View.OnClickListener nextEvent()
    {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //1. 문제가 맞는지 확인
                int curChapter = Dict.i().getCurChapter();
                int curVerse = Dict.i().getCurVerse();
                String answer = Dict.i().getChapters().get(curChapter).getVers().get(curVerse).getContent();
                String trimContent = et_content.getText().toString().replace(" ", "").trim();
                String trimAnswer = answer.replace(" ", "").trim();
                boolean isCorrect = trimContent.equals(trimAnswer);
                if (!isCorrect)
                    fcWrong(trimContent, trimAnswer, answer, et_content.getText().toString());
                else //정답일경우
                    fcCorrect(false);
            }
        };
    }

    private View.OnClickListener prevEvent = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int curChapter;
            int curVerse = Dict.i().getCurVerse();

            if (curVerse != 0)
            {
                Dict.i().setCurVerse(curVerse - 1);
                curChapter = Dict.i().getCurChapter();
                curVerse = Dict.i().getCurVerse();
                tv_title.setText(String.format(Locale.US,"%s %d:%d", Dict.i().getChapters().get(curChapter).getAbbreviation(), curChapter + 1, curVerse + 1));
                contentClear();
            }
        }
    };

    private void fcCorrect(boolean isForceNext)
    {
        if (!isForceNext)
            fcSetWrongCount(true);
        int curChapter = Dict.i().getCurChapter();
        int curVerse = Dict.i().getCurVerse();

        switch (startType)
        {
            case order:
                //다풀었을 경우
                if (curVerse + 1 == Dict.i().getChapters().get(curChapter).getVers().size())
                    fcAllClear();
                else
                {
                    if (!isForceNext)
                        Toast.makeText(this, R.string.corret, Toast.LENGTH_SHORT).show();

                    Dict.i().setCurVerse(Dict.i().getCurVerse() + 1);
                    curChapter = Dict.i().getCurChapter();
                    curVerse = Dict.i().getCurVerse();
                    tv_title.setText(String.format(Locale.US,"%s %d:%d", Dict.i().getChapters().get(curChapter).getAbbreviation(), curChapter + 1, curVerse + 1));
                    this.contentClear();
                }
                break;
            case random:
                int endRandom = fcGetRandomNumber();
                //다풀었을 경우
                if (endRandom == -1)
                    fcAllClear();
                else
                {
                    if (!isForceNext)
                        Toast.makeText(this, R.string.corret, Toast.LENGTH_SHORT).show();

                    Dict.i().setCurVerse(endRandom);
                    curChapter = Dict.i().getCurChapter();
                    curVerse = Dict.i().getCurVerse();
                    tv_title.setText(String.format(Locale.US,"%s %d:%d", Dict.i().getChapters().get(curChapter).getAbbreviation(), curChapter + 1, curVerse + 1));
                    this.contentClear();
                }
                break;
            case all_random:
                if (fcIsEndAllRandom())
                    fcAllClear();
                else
                {
                    curChapter = overlapARD.get(overlapARD.size() - 1).getChapter();
                    curVerse = overlapARD.get(overlapARD.size() - 1).getVerse();
                    Dict.i().setCurChapter(curChapter);
                    Dict.i().setCurVerse(curVerse);

                    if (!isForceNext)
                        Toast.makeText(this, R.string.corret, Toast.LENGTH_SHORT).show();

                    tv_title.setText(String.format(Locale.US,"%s %d:%d", Dict.i().getChapters().get(curChapter).getAbbreviation(), curChapter + 1, curVerse + 1));
                    this.contentClear();
                }

                break;
            default:
                break;
        }
    }

    private void fcAllClear()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(RevelationQuiz.this);
        alert.setTitle(R.string.quiz_clear);
        alert.setCancelable(false);
        alert.setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                fcInit();
                finish();
            }
        });

        AlertDialog ad = alert.create();
        ad.show();
    }

    private void fcWrong(String trimContent, String trimAnswer, String originAnswer, String originDraw)
    {
        //여기에 오답을 올리던지 내리던지 해야함
        fcSetWrongCount(false);
        String result = getString(R.string.wrong);

        ArrayList<Integer> wrongs = fcCompare(trimContent, trimAnswer);
        ArrayList<Integer> spaces = fcFindSpace(originDraw);

        AlertDialog.Builder alert = new AlertDialog.Builder(RevelationQuiz.this);
        alert.setTitle(result);
//        alert.setMessage(R.string.alert_addnote);
        alert.setCancelable(false);
        @SuppressLint("InflateParams") View alertView = getLayoutInflater().inflate(R.layout.dialog_check_answer, null);
        final TextView tv_answer = alertView.findViewById(R.id.tv_answer);
        TextView tv_drawup = alertView.findViewById(R.id.tv_drawup);
        tv_answer.setText(originAnswer);

        if (wrongs.size() == 0)
        {
            SpannableStringBuilder sp = new SpannableStringBuilder(trimContent);
            for (int j = 0; j < spaces.size(); j++)
                sp.insert(spaces.get(j) - 1, " ");

            tv_drawup.setText(sp);
        }
        else
        {
            for (int i = 0; i < wrongs.size(); i++)
            {
                SpannableStringBuilder sp;
                if (i == 0)
                    sp = new SpannableStringBuilder(trimContent);
                else
                    sp = new SpannableStringBuilder(tv_drawup.getText());

                ForegroundColorSpan fcs = new ForegroundColorSpan(Color.RED);
                sp.setSpan(fcs, wrongs.get(i) - 1, wrongs.get(i), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_drawup.setText(sp);
                if (i == wrongs.size() - 1)
                {
                    for (int j = 0; j < spaces.size(); j++)
                        sp.insert(spaces.get(j) - 1, " ");

                    tv_drawup.setText(sp);
                }
            }
        }
        alert.setView(alertView);

        alert.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alert.setNeutralButton(R.string.next, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                fcCorrect(true);
            }
        });

        AlertDialog ad = alert.create();
        ad.show();
    }

    private void fcSetWrongCount(boolean isCorrect)
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) //권한이 없을 경우
            return;

        int curChapter = Dict.i().getCurChapter();
        int curVerse = Dict.i().getCurVerse();
        //해당 절에 대한 데이터가 일단 있는지 부터
        myDB my = new myDB(this);
        //정답이든 아니든 찾아봐야 함
        String s = String.format(Locale.US,"SELECT * FROM wrong_note WHERE chapter = %d AND verse = %d;",
                curChapter,
                curVerse);
        Cursor row = my.funcSelect(s);

        if (row == null) //없으면 만들어야 함
        {
            if (isCorrect) //없지만 정답이면 나감
                return;

            //정답이 아니면 추가
            String insert = String.format(Locale.US,"INSERT INTO wrong_note (abb_num, chapter, verse, count) VALUES(%d, %d, %d, %d);",
                    66,
                    curChapter,
                    curVerse,
                    1);
            my.funcUseQuery(insert);
            return;
        }

        int count = row.getInt(row.getColumnIndex("count"));
        if (isCorrect) //있는데 정답이면
        {
            count--;
            if (count == 0) //지워야 함
            {
                String del = String.format(Locale.US,"DELETE FROM wrong_note WHERE chapter = %d AND verse = %d;",
                        curChapter,
                        curVerse);
                my.funcUseQuery(del);
                return;
            }

            String down = String.format(Locale.US,"UPDATE wrong_note SET count = %d WHERE chapter = %d AND verse = %d;",
                    count,
                    curChapter,
                    curVerse);
            my.funcUseQuery(down);
        }
        else
        {
            count++;

            String up = String.format(Locale.US,"UPDATE wrong_note SET count = %d WHERE chapter = %d AND verse = %d;",
                    count,
                    curChapter,
                    curVerse);
            my.funcUseQuery(up);
        }
    }

    private ArrayList<Integer> fcCompare(String write, String answer)
    {
        if (write.length() == 0)
            return new ArrayList<>();

        String[] ws = write.split("");
        String[] an = answer.split("");
        ArrayList<Integer> wrongs = new ArrayList<>();
        for (int i = 0; i < an.length; i++)
        {
            if (i == ws.length)
                break;
            if (!an[i].equals(ws[i]))
            {
                wrongs.add(i);
            }
        }

        return wrongs;
    }

    private ArrayList<Integer> fcFindSpace(String content)
    {
        if (content.length() == 0)
            return new ArrayList<>();

        String s = content.trim();
        String[] cs = s.split("");
        ArrayList<Integer> spaces = new ArrayList<>();
        for (int i = 0; i < cs.length; i++)
        {
            if (cs[i].equals(" "))
                spaces.add(i);


        }
        return spaces;
    }

    //랜덤용
    private int fcGetRandomNumber()
    {
        if (overlapVerse.size() == maxVerse - startVerse) {
            return -1;
        }

        Random r = new Random();
        int max = maxVerse - startVerse;
        int ranNum = startVerse + r.nextInt(max);

        for (int i = 0; i < overlapVerse.size(); i++)
        {
            if (ranNum == overlapVerse.get(i))
            {
                ranNum = startVerse + r.nextInt(max);
                i = -1;
            }
        }
        overlapVerse.add(ranNum);

        return ranNum;
    }

    private boolean fcIsEndAllRandom()
    {
        if (overlapARD.size() == Dict.i().getRevMaxVerse())
            return true;

        Random r = new Random();
        int chapter = r.nextInt(Dict.i().getChapters().size());

        Random r1 = new Random();
        int verse = r1.nextInt(Dict.i().getChapters().get(chapter).getVers().size());

        for (int i = 0; i < overlapARD.size(); i++)
        {
            if (overlapARD.get(i).getChapter() == chapter &&
                overlapARD.get(i).getVerse() == verse)
            {
                chapter = r.nextInt(Dict.i().getChapters().size());
                verse = r1.nextInt(Dict.i().getChapters().get(chapter).getVers().size());
                i = -1;
            }
        }

        overlapARD.add(new AllRandomData(chapter, verse));
        return false;
    }

    private void fcInit()
    {
        Intent i = new Intent();
        i.putExtra("chapter", Dict.i().getCurChapter());
        i.putExtra("verse", Dict.i().getCurVerse());
        setResult(0, i);
    }

    //올랜덤용 초기화도 있어야 할듯

    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
        fcExit();
    }

}























