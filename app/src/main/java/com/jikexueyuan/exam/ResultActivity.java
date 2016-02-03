package com.jikexueyuan.exam;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ResultActivity extends Activity {

    //    private String [] result = new String[21];
    private String[] result;
    private String[] resultx;
    private List<Question> list;
    private ListView listView;
    private SQLiteDatabase db;
    private Button btnXLS;
    //    private String [] questions = new String [21];
    private String[] questions;
    private String[] edits;
    private Button btn_back;
    private SharedPreferences sharedPreferences;
    /**
     * 题目集的数量
     */
    private int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(i);
                ResultActivity.this.finish();
            }
        });

        listView = (ListView) findViewById(R.id.list);

        DBService dbService = new DBService(0);
        list = dbService.getQuestions();

        count = list.size();
        result = new String[count];
        questions = new String[count];

        resultx = getIntent().getStringArrayExtra("result");
        edits = getIntent().getStringArrayExtra("edits");
        for (int i = 0; i < resultx.length; i++) {
            questions[i] = list.get(i).question;
            result[i] = GetAnswer(resultx[i], i, list.get(i).duoxuan);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.itemlayout, result);
        listView.setAdapter(adapter);

        SaveInDb();

        btnXLS = (Button) findViewById(R.id.btnXLS);
        btnXLS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hos = getSharedPreferences("Configs",Activity.MODE_PRIVATE).getString("NAME","t");
                String sql = "select * from result WHERE hos='"+hos+"'";
                Cursor cursor = db.rawQuery(sql, null);
                cursor.moveToFirst();

                String[][] results = new String[cursor.getCount()][count + 4 + 1];
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    for (int j = 0; j < count + 4 + 1; j++) {
                        if (j == 0) {
                            results[i][0] = cursor.getInt(cursor.getColumnIndex("ID")) + "";
                        } else {
                            results[i][j] = getCursor(j, cursor);
                        }
                    }
                }


                String[] title = {"ID", "第1题", "第2题", "第3题", "第4题", "第5题", "第6题", "第7.1题", "第7.2题", "第7.3题", "第7.4题",
                        "第7.5题", "第8题", "第9题", "第10题", "第11题", "第12题", "第13题", "第14.1题", "第14.2题", "第14.3题", "第14.4题"
                        , "第14.5题", "第14.6题", "第14.7题", "第15.1题", "第15.2题", "第15.3题", "第15.4题", "第15.5题", "第15.6题", "第15.7题", "第15.8题", "第15.9题"
                        , "第15.10题", "第15.11题", "第16.1题", "第16.2题", "第16.3题", "第17题", "第18题"
                        , "对医院的意见", "就诊科室", "调查员签名", "调查日期"};
                File f = Environment.getExternalStorageDirectory();
                try {
                    sharedPreferences = getSharedPreferences("Configs",Activity.MODE_PRIVATE);
                    String name="t1";
                    if (!sharedPreferences.getString("NAME","NULL").equals("NULL")) {
                        name = sharedPreferences.getString("NAME","t1");
                    }
                    //t.xls为要新建的文件名

                    WritableWorkbook book = Workbook.createWorkbook(new File(f.getPath() + "/MyQuestions/"+name+"_menzhen.xls"));
                    WritableSheet sheet = book.createSheet("第一页", 0);
                    for (int i = 0; i < count + 4 + 1; i++) {
                        sheet.addCell(new Label(i, 0, title[i]));
                    }

                    for (int i = 0; i < cursor.getCount(); i++) {
                        for (int j = 0; j < count + 4 + 1; j++) {
                            sheet.addCell(new Label(j, i + 1, results[i][j]));
                        }
                    }

                    //写入数据
                    book.write();
                    //关闭文件
                    book.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    /**
     * 将多选分割成单选
     *
     * @param s
     * @return
     */
    private String AddSpace(String s) {
        int index = Integer.parseInt(s);
        String result = s;
        if (index > 10) {
            result = "";
            int currentnum = 0;
            while (index > 0) {
                currentnum = index % 10;
                result = result + currentnum + ",";
                index = index / 10;
            }
        }

        return result;
    }

    /**
     * 根据题目标号和数据库查询结果返回所选结果
     *
     * @param i 题目标号
     * @param c 数据库查询结果
     * @return 被选择的结果
     */
    private String getCursor(int i, Cursor c) {
        switch (i) {
            case 1:
                return AddSpace(c.getString(c.getColumnIndex("ques1")));
            case 2:
                return AddSpace(c.getString(c.getColumnIndex("ques2")));
            case 3:
                return AddSpace(c.getString(c.getColumnIndex("ques3")));
            case 4:
                return AddSpace(c.getString(c.getColumnIndex("ques4")));
            case 5:
                return AddSpace(c.getString(c.getColumnIndex("ques5")));
            case 6:
                return AddSpace(c.getString(c.getColumnIndex("ques6")));
            case 7:
                return AddSpace(c.getString(c.getColumnIndex("ques7")));
            case 8:
                return AddSpace(c.getString(c.getColumnIndex("ques8")));
            case 9:
                return AddSpace(c.getString(c.getColumnIndex("ques9")));
            case 10:
                return AddSpace(c.getString(c.getColumnIndex("ques10")));

            case 11:
                return AddSpace(c.getString(c.getColumnIndex("ques11")));
            case 12:
                return AddSpace(c.getString(c.getColumnIndex("ques12")));
            case 13:
                return AddSpace(c.getString(c.getColumnIndex("ques13")));
            case 14:
                return AddSpace(c.getString(c.getColumnIndex("ques14")));
            case 15:
                return AddSpace(c.getString(c.getColumnIndex("ques15")));
            case 16:
                return AddSpace(c.getString(c.getColumnIndex("ques16")));
            case 17:
                return AddSpace(c.getString(c.getColumnIndex("ques17")));
            case 18:
                return AddSpace(c.getString(c.getColumnIndex("ques18")));
            case 19:
                return AddSpace(c.getString(c.getColumnIndex("ques19")));
            case 20:
                return AddSpace(c.getString(c.getColumnIndex("ques20")));
            case 21:
                return AddSpace(c.getString(c.getColumnIndex("ques21")));
            case 22:
                return AddSpace(c.getString(c.getColumnIndex("ques22")));
            case 23:
                return AddSpace(c.getString(c.getColumnIndex("ques23")));
            case 24:
                return AddSpace(c.getString(c.getColumnIndex("ques24")));
            case 25:
                return AddSpace(c.getString(c.getColumnIndex("ques25")));
            case 26:
                return AddSpace(c.getString(c.getColumnIndex("ques26")));
            case 27:
                return AddSpace(c.getString(c.getColumnIndex("ques27")));
            case 28:
                return AddSpace(c.getString(c.getColumnIndex("ques28")));
            case 29:
                return AddSpace(c.getString(c.getColumnIndex("ques29")));
            case 30:
                return AddSpace(c.getString(c.getColumnIndex("ques30")));
            case 31:
                return AddSpace(c.getString(c.getColumnIndex("ques31")));
            case 32:
                return AddSpace(c.getString(c.getColumnIndex("ques32")));
            case 33:
                return AddSpace(c.getString(c.getColumnIndex("ques33")));
            case 34:
                return AddSpace(c.getString(c.getColumnIndex("ques34")));
            case 35:
                return AddSpace(c.getString(c.getColumnIndex("ques35")));
            case 36:
                return AddSpace(c.getString(c.getColumnIndex("ques36")));
            case 37:
                return AddSpace(c.getString(c.getColumnIndex("ques37")));
            case 38:
                return AddSpace(c.getString(c.getColumnIndex("ques38")));
            case 39:
                return AddSpace(c.getString(c.getColumnIndex("ques39")));
            case 40:
                return AddSpace(c.getString(c.getColumnIndex("ques40")));

            case 41:
                return c.getString(c.getColumnIndex("a"));
            case 42:
                return c.getString(c.getColumnIndex("b"));
            case 43:
                return c.getString(c.getColumnIndex("c"));
            case 44:
                return c.getString(c.getColumnIndex("d"));

        }
        return "error";
    }


    /**
     * 根据选择的选项获得选项内容
     *
     * @param s    选项
     * @param i    第几道题目
     * @param mode 0-单选  1-多选
     * @return 结果
     */
    private String GetAnswer(String s, int i, int mode) {
        if (mode == 0) {
            int index = Integer.parseInt(s);
            switch (index) {
                case 0:
                    return GetQuesNum(i + 1) + "、" + questions[i] + "\n" + "(" + (index + 1) + ")" + list.get(i).ans0;
                case 1:
                    return GetQuesNum(i + 1) + "、" + questions[i] + "\n" + "(" + (index + 1) + ")" + list.get(i).ans1;
                case 2:
                    return GetQuesNum(i + 1) + "、" + questions[i] + "\n" + "(" + (index + 1) + ")" + list.get(i).ans2;
                case 3:
                    return GetQuesNum(i + 1) + "、" + questions[i] + "\n" + "(" + (index + 1) + ")" + list.get(i).ans3;
                case 4:
                    return GetQuesNum(i + 1) + "、" + questions[i] + "\n" + "(" + (index + 1) + ")" + list.get(i).ans4;
                case 5:
                    return GetQuesNum(i + 1) + "、" + questions[i] + "\n" + "(" + (index + 1) + ")" + list.get(i).ans5;
                case 6:
                    return GetQuesNum(i + 1) + "、" + questions[i] + "\n" + "(" + (index + 1) + ")" + list.get(i).ans6;
                case 7:
                    return GetQuesNum(i + 1) + "、" + questions[i] + "\n" + "(" + (index + 1) + ")" + list.get(i).ans7;
                case 8:
                    return GetQuesNum(i + 1) + "、" + questions[i] + "\n" + "(" + (index + 1) + ")" + list.get(i).ans8;
                case 9:
                    return GetQuesNum(i + 1) + "、" + questions[i] + "\n" + "(" + (index + 1) + ")" + list.get(i).ans9;

            }
        }
        if (mode == 1) {
            char c;
            int currentnum = -1;
            String result = "";
            for (int l = 0; l < s.length(); l++) {
                c = s.charAt(l);
                currentnum = Integer.parseInt(c + "");
                switch (currentnum) {
                    case 0:
                        result = result + "(" + (currentnum + 1) + ")" + list.get(i).ans0 + "\n";
                        break;
                    case 1:
                        result = result + "(" + (currentnum + 1) + ")" + list.get(i).ans1 + "\n";
                        break;
                    case 2:
                        result = result + "(" + (currentnum + 1) + ")" + list.get(i).ans2 + "\n";
                        break;
                    case 3:
                        result = result + "(" + (currentnum + 1) + ")" + list.get(i).ans3 + "\n";
                        break;
                    case 4:
                        result = result + "(" + (currentnum + 1) + ")" + list.get(i).ans4 + "\n";
                        break;
                    case 5:
                        result = result + "(" + (currentnum + 1) + ")" + list.get(i).ans5 + "\n";
                        break;
                    case 6:
                        result = result + "(" + (currentnum + 1) + ")" + list.get(i).ans6 + "\n";
                        break;
                    case 7:
                        result = result + "(" + (currentnum + 1) + ")" + list.get(i).ans7 + "\n";
                        break;
                    case 8:
                        result = result + "(" + (currentnum + 1) + ")" + list.get(i).ans8 + "\n";
                        break;
                    case 9:
                        result = result + "(" + (currentnum + 1) + ")" + list.get(i).ans9 + "\n";
                }
            }
            return GetQuesNum(i + 1) + "、" + questions[i] + "\n" + result;
        }
        return "未作答";
    }

    /**
     * 输入当前的题目序号，返回我们需要的真正题号
     *
     * @param i
     * @return
     */
    private String GetQuesNum(int i) {
        int quesNum = -1;
        switch (i) {
            case 7:
                return "7.1";
            case 8:
                return "7.2";
            case 9:
                return "7.3";
            case 10:
                return "7.4";
            case 11:
                return "7.5";
            case 18:
                return "14.1";
            case 19:
                return "14.2";
            case 20:
                return "14.3";
            case 21:
                return "14.4";
            case 22:
                return "14.5";
            case 23:
                return "14.6";
            case 24:
                return "14.7";
            case 25:
                return "15.1";
            case 26:
                return "15.2";
            case 27:
                return "15.3";
            case 28:
                return "15.4";
            case 29:
                return "15.5";
            case 30:
                return "15.6";
            case 31:
                return "15.7";
            case 32:
                return "15.8";
            case 33:
                return "15.9";
            case 34:
                return "15.10";
            case 35:
                return "15.11";
            case 36:
                return "16.1";
            case 37:
                return "16.2";
            case 38:
                return "16.3";
            default:
                if (i < 7) {
                    return i + "";
                }
                if (i > 38) {return (i - 22) + "";}
                if (i>11&&i<18){
                    return  (i-4)+"";
                }
        }
        return "ERROR";
    }




    /**
     * 存入数据库
     */
    private void SaveInDb(){
        File f = Environment.getExternalStorageDirectory();
        db = SQLiteDatabase.openDatabase(f.getPath()+"/MyQuestions/question.db", null, SQLiteDatabase.OPEN_READWRITE);


        String [] resulty = new String[count+4+1];

        for (int i=0;i<count;i++){
            resulty[i] = (Integer.parseInt(resultx[i])+1)+"";
            Log.d("resulty", resulty[i]);
        }

        resulty[count+4] = getSharedPreferences("Configs",Activity.MODE_PRIVATE).getString("NAME","t1");

        resulty[count] = edits [0];
        resulty[count+1] = edits [1];
        resulty[count+2] = edits [2];
        resulty[count+3] = edits [3];

        //第一个null对应自增id，count+4个占位符表示题目+建议
        db.execSQL("insert into result values(null,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",resulty);

        Cursor cursor = db.rawQuery("select * from result", null);
        Log.d("cursor",cursor.getCount()+"");
    }
}
