package com.jikexueyuan.exam;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by antdlx 2015-7-27
 */
public class DBService {
    private SQLiteDatabase db;
    private int mode;
    private Cursor cursor;

    public DBService(int mode)
    {
        this.mode = mode;
        File f = Environment.getExternalStorageDirectory();
        db = SQLiteDatabase.openDatabase(f.getPath()+"/MyQuestions/question.db", null, SQLiteDatabase.OPEN_READWRITE);

    }

    public List<Question> getQuestions()
    {
        List<Question> list = new ArrayList<Question>();
        if (mode==0){
            cursor = db.rawQuery("select * from exam1", null);
        }else {
            cursor = db.rawQuery("select * from exam2", null);
        }

        if(cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            int count = cursor.getCount();
            for(int i = 0; i < count; i++)
            {
                cursor.moveToPosition(i);
                Question question = new Question();
                question.question = cursor.getString(cursor.getColumnIndex("question"));
                question.ans0 = cursor.getString(cursor.getColumnIndex("ans0"));
                question.ans1 = cursor.getString(cursor.getColumnIndex("ans1"));
                question.ans2 = cursor.getString(cursor.getColumnIndex("ans2"));
                question.ans3 = cursor.getString(cursor.getColumnIndex("ans3"));
                question.ans4 = cursor.getString(cursor.getColumnIndex("ans4"));
                question.ans5 = cursor.getString(cursor.getColumnIndex("ans5"));
                question.ans6 = cursor.getString(cursor.getColumnIndex("ans6"));
                question.ans7 = cursor.getString(cursor.getColumnIndex("ans7"));
                question.ans8 = cursor.getString(cursor.getColumnIndex("ans8"));
                question.ans9 = cursor.getString(cursor.getColumnIndex("ans9"));
                question.duoxuan = cursor.getInt(cursor.getColumnIndex("duoxuan"));

                question.ID = cursor.getInt(cursor.getColumnIndex("ID"));
                question.selectedAns = "-1";
                list.add(question);

            }
        }
        return list;
    }

}
