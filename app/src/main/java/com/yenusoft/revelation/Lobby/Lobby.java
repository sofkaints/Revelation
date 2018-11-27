package com.yenusoft.revelation.Lobby;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.yenusoft.revelation.Dict;
import com.yenusoft.revelation.Dictionary.ChapterAdapter;
import com.yenusoft.revelation.Dictionary.VerseAdapter;
import com.yenusoft.revelation.Logo;
import com.yenusoft.revelation.R;
import com.yenusoft.revelation.RevelationQuiz.RevelationQuiz;
import com.yenusoft.revelation.enums;

import java.util.Locale;

/**
 * Created by SofKaints on 2017. 7. 11..
 */

public class Lobby extends Activity
{

    private ResultHandler rh;
    private PopupWindow popupWindowDogs;
    private Button but_chapter, but_verse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        rh = new ResultHandler();
        popupWindowDogs = popupWindowDogs();
        setContentView(R.layout.nv_bar);

        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.lobby_revelation);
        ImageButton ib_left = findViewById(R.id.ib_left);
        ib_left.setBackgroundResource(R.drawable.icon_left);
        ib_left.setOnClickListener(exitEvent);
        ImageButton ib_right = findViewById(R.id.ib_right);
        ib_right.setBackgroundResource(R.drawable.icon_menu);
        ib_right.setOnClickListener(showMenu);
        FrameLayout fl_content = findViewById(R.id.fl_content);
        this.fcSetView(View.inflate(this, R.layout.lobby, fl_content));
    }


    private void fcSetView(View v)
    {
        //fcSetButVerse(5);
        but_chapter = v.findViewById(R.id.but_chapter);
        but_verse = v.findViewById(R.id.but_verse);
        but_chapter.setOnClickListener(chapterEvent(but_verse));
        but_verse.setOnClickListener(verseEvent);

        Button but_order = v.findViewById(R.id.but_order);
        but_order.setOnClickListener(startQuiz);
    }

    private View.OnClickListener showMenu = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            popupWindowDogs.showAsDropDown(view, -5, 0);
        }
    };

    private View.OnClickListener chapterEvent(final Button but_verse)
    {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                but_verse.setText(R.string.verse_1);
                Dict.i().setCurVerse(0);
                AlertDialog.Builder alert = new AlertDialog.Builder(Lobby.this);
                @SuppressLint("InflateParams") View alertView = getLayoutInflater().inflate(R.layout.dialog_cv, null);
                ((TextView)alertView.findViewById(R.id.tv_title)).setText(R.string.chapter);
                ListView lv_cvs =  alertView.findViewById(R.id.lv_cvs);
                alert.setView(alertView);
                alert.setCancelable(false);

                alert.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog ad = alert.create();

                ChapterAdapter adapter = new ChapterAdapter(Dict.i().getChapters());
                lv_cvs.setAdapter(adapter);
                lv_cvs.setOnItemClickListener(fcChapter_ClickListener(ad));
                ad.show();
            }
        };
    }

    private AdapterView.OnItemClickListener fcChapter_ClickListener(final AlertDialog ad)
    {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Dict.i().setCurChapter(i);
                fcSetButChapter(i);
                ad.dismiss();
            }
        };
    }

    private View.OnClickListener verseEvent = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            AlertDialog.Builder alert = new AlertDialog.Builder(Lobby.this);
            @SuppressLint("InflateParams") View alertView = getLayoutInflater().inflate(R.layout.dialog_cv, null);
            ((TextView)alertView.findViewById(R.id.tv_title)).setText(R.string.verse);
            ListView lv_cvs = alertView.findViewById(R.id.lv_cvs);
            alert.setView(alertView);
            alert.setCancelable(false);

            alert.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog ad = alert.create();
            VerseAdapter adapter = new VerseAdapter(Dict.i().getChapters().get(Dict.i().getCurChapter()).getVers());
            lv_cvs.setAdapter(adapter);
            lv_cvs.setOnItemClickListener(fcVerse_ClickListener(ad));
            ad.show();
        }
    };

    private AdapterView.OnItemClickListener fcVerse_ClickListener(final AlertDialog ad)
    {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Dict.i().setCurVerse(i);
                fcSetButVerse(i);
                ad.dismiss();
            }
        };
    }

    private void fcSetButChapter(int chapter)
    {
        if (Dict.i().getLang() == enums.lang.kor)
            but_chapter.setText(String.format(Locale.US,"%d %s", chapter + 1, getString(R.string.chapter)));
        else
            but_chapter.setText(String.format(Locale.US,"%s %d", getString(R.string.chapter), chapter + 1));
    }

    private void fcSetButVerse(int verse)
    {
        if (Dict.i().getLang() == enums.lang.kor)
            but_verse.setText(String.format(Locale.US,"%d %s", verse + 1, getString(R.string.verse)));
        else
            but_verse.setText(String.format(Locale.US,"%s %d", getString(R.string.verse), verse + 1));
    }

    private View.OnClickListener startQuiz = new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder alert = new AlertDialog.Builder(Lobby.this);

            alert.setTitle(R.string.choice);
            CharSequence[] cs = new CharSequence[3];
            cs[0] = getString(R.string.order);
            cs[1] = getString(R.string.random);
            cs[2] = getString(R.string.allRandom);
            alert.setItems(cs, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(Lobby.this, RevelationQuiz.class);
                    switch (which)
                    {
                        case 0:
                            i.putExtra("startType", enums.startType.order);
                            break;
                        case 1:
                            i.putExtra("startType", enums.startType.random);
                            break;
                        case 2:
                            i.putExtra("startType", enums.startType.all_random);
                            break;
                        default:
                            break;
                    }
                    startActivityForResult(i, 0);
                }
            });
            alert.create().show();
        }
    };

    //from revQuiz
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int chapter = data.getIntExtra("chapter", 0);
        int verse = data.getIntExtra("verse", 0);

        Dict.i().setCurChapter(chapter);
        Dict.i().setCurVerse(verse);

        fcSetButChapter(Dict.i().getCurChapter());
        fcSetButVerse(Dict.i().getCurVerse());

        //db 갱신
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            Logo.fcSetWrongNote(Lobby.this);
    }

    private View.OnClickListener wrongNote = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder alert = new AlertDialog.Builder(Lobby.this);
            @SuppressLint("InflateParams") View alertView = getLayoutInflater().inflate(R.layout.dialog_wrongnote, null);
            ListView lv_cvs = alertView.findViewById(R.id.lv_cvs);
            alert.setView(alertView);
            alert.setCancelable(false);

            alert.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog ad = alert.create();

            WrongNoteAdapter adapter = new WrongNoteAdapter(Lobby.this, Dict.i().getWrongNotes());
            lv_cvs.setAdapter(adapter);
            lv_cvs.setOnItemClickListener(fcWrongNoteItemEvent(ad));
            ad.show();
            popupWindowDogs.dismiss();
        }
    };

    private View.OnClickListener devInfo = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder alert = new AlertDialog.Builder(Lobby.this);
            alert.setTitle(R.string.dev);
            String t = String.format("♥%s\n버그나 추가되었으면 하는 기능을 구체적으로 아래 이메일로 보내주세요.\nE-Mail: sofkaints@naver.com",
                    getString(R.string.developer));
            alert.setMessage(t);
            alert.setCancelable(false);

            alert.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alert.setNeutralButton(R.string.send_now, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    funcSendMail();
                }
            });

            AlertDialog ad = alert.create();
            //ad.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            ad.show();
            popupWindowDogs.dismiss();
        }
    };

    private void funcSendMail()
    {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"sofkaints@naver.com"});
        //i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
        @SuppressLint("DefaultLocale") String format = String.format("Device Model : %s\n API Level : %d\n\n", Build.MODEL, Build.VERSION.SDK_INT);
        i.putExtra(Intent.EXTRA_TEXT, format);
        try
        {
            startActivity(Intent.createChooser(i, getString(R.string.dev)));
        }
        catch (android.content.ActivityNotFoundException ex)
        {
            Toast.makeText(Lobby.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private AdapterView.OnItemClickListener fcWrongNoteItemEvent(final AlertDialog ad)
    {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Dict.i().setCurChapter(Dict.i().getWrongNotes().get(i).getChapter());
                Dict.i().setCurVerse(Dict.i().getWrongNotes().get(i).getVerse());
                ad.dismiss();
                Intent intent = new Intent(Lobby.this, RevelationQuiz.class);
                intent.putExtra("startType", enums.startType.order);
                startActivityForResult(intent, 0);
            }
        };
    }

    private PopupWindow popupWindowDogs()
    {
        PopupWindow popupWindow = new PopupWindow(Lobby.this);

        View v = View.inflate(this, R.layout.lobby_menu, null);
        v.findViewById(R.id.tv_wrongNote).setOnClickListener(wrongNote);
        v.findViewById(R.id.tv_dev).setOnClickListener(devInfo);

        popupWindow.setFocusable(true);
        popupWindow.setWidth(500);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        popupWindow.setContentView(v);

        return popupWindow;
    }

    private View.OnClickListener exitEvent = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    @Override
    public void finish() {
        //연속해서 터치하면 종료하게끔
        if (rh != null)
        {
            if (!rh.finishFlag)
            {
                Toast.makeText(this, R.string.appExit, Toast.LENGTH_SHORT).show();
                rh.finishFlag = true;
                rh.sendEmptyMessageDelayed(0, 2000);
                return;
            }
        }

        super.finish();
        System.exit(0);
    }

    private static class ResultHandler extends Handler {
        boolean finishFlag = false;

        ResultHandler() {
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0)
                finishFlag = false;
        }
    }
}

























