package com.jikexueyuan.exam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class ExamActivity extends Activity implements CompoundButton.OnCheckedChangeListener {

    private int count;
    private int current;
    private CheckBox[] checkBoxes = new CheckBox[20];
    private TextView[] textViews = new TextView[2];
    private List<Question> list ;
    private  TextView tv_head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        DBService dbService = new DBService(0);
        tv_head = (TextView) findViewById(R.id.tv_head);
        tv_head.setVisibility(View.VISIBLE);

        list = dbService.getQuestions();

        count = list.size();
        current = 0;


        //初始化20个checkBox
        checkBoxes[0] = (CheckBox) findViewById(R.id.ans10);
        checkBoxes[1] = (CheckBox) findViewById(R.id.ans11);
        checkBoxes[2] = (CheckBox) findViewById(R.id.ans12);
        checkBoxes[3] = (CheckBox) findViewById(R.id.ans13);
        checkBoxes[4] = (CheckBox) findViewById(R.id.ans14);
        checkBoxes[5] = (CheckBox) findViewById(R.id.ans15);
        checkBoxes[6] = (CheckBox) findViewById(R.id.ans16);
        checkBoxes[7] = (CheckBox) findViewById(R.id.ans17);
        checkBoxes[8] = (CheckBox) findViewById(R.id.ans18);
        checkBoxes[9] = (CheckBox) findViewById(R.id.ans19);
        checkBoxes[10] = (CheckBox) findViewById(R.id.ans20);
        checkBoxes[11] = (CheckBox) findViewById(R.id.ans21);
        checkBoxes[12] = (CheckBox) findViewById(R.id.ans22);
        checkBoxes[13] = (CheckBox) findViewById(R.id.ans23);
        checkBoxes[14] = (CheckBox) findViewById(R.id.ans24);
        checkBoxes[15] = (CheckBox) findViewById(R.id.ans25);
        checkBoxes[16] = (CheckBox) findViewById(R.id.ans26);
        checkBoxes[17] = (CheckBox) findViewById(R.id.ans27);
        checkBoxes[18] = (CheckBox) findViewById(R.id.ans28);
        checkBoxes[19] = (CheckBox) findViewById(R.id.ans29);

        //注册监听器
        for (int i=0;i<20;i++){
            checkBoxes[i].setOnCheckedChangeListener(this);
        }


        //初始化2个题干TextView
        textViews[0] = (TextView) findViewById(R.id.ques1);
        textViews[1] = (TextView) findViewById(R.id.ques2);

        Button btn_next = (Button)findViewById(R.id.btn_next);
        Button btn_previous = (Button)findViewById(R.id.btn_previous);

        //初始化第一页的内容
        Question q = list.get(0);
        textViews[0].setText("一、基本资料：\n 1、您的性别");
        checkBoxes[0].setVisibility(View.VISIBLE);
        checkBoxes[0].setText("(1)" + q.ans0);
        checkBoxes[1].setVisibility(View.VISIBLE);
        checkBoxes[1].setText("(2)"+q.ans1);

        q = list.get(1);
        textViews[1].setText("2、"+q.question);
        checkBoxes[10].setVisibility(View.VISIBLE);
        checkBoxes[10].setText("(1)"+q.ans0);
        checkBoxes[11].setVisibility(View.VISIBLE);
        checkBoxes[11].setText("(2)"+q.ans1);
        checkBoxes[12].setVisibility(View.VISIBLE);
        checkBoxes[12].setText("(3)"+q.ans2);
        checkBoxes[13].setVisibility(View.VISIBLE);
        checkBoxes[13].setText("(4)"+q.ans3);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (IsSelected(current)) {
                    //保存当页结果，然后再进入下一页
                    String answers = "";
                    for (int i = 0; i < 10; i++) {

                        if (checkBoxes[i].isChecked()) {
                            answers = answers + i;
                        }
                    }
                    list.get(current).selectedAns = answers;
                    CheckSpecial(current);
                    answers = "";
                    for (int i = 10; i < 20; i++) {
                        if (checkBoxes[i].isChecked()) {
                            answers = answers + (i - 10);
                        }
                    }
                    list.get(current+1).selectedAns = answers;

                    CheckSpecial(current+1);

                    //全部回答完成之后
                    if (current==count-2){
                        String [] selected = new String [count];
                        for (int i=0;i<count;i++){
                            selected[i]= list.get(i).selectedAns;
                        }
                        Intent i = new Intent(ExamActivity.this,LastActivity.class);
                        i.putExtra("result",selected);
                        startActivity(i);
                    }

                    //执行翻页，多-2是最后一页的特殊处理
                    if (current < count - 2) {

                        tv_head.setVisibility(View.GONE);

                        ResetBtn();
                        current += 2;
                        Question q1 = list.get(current);
                        Question q2 = list.get(current + 1);
                        initBoxs(q1, q2);
//                        textViews[0].setText((current + 1) + "、" + q1.question);
//                        textViews[1].setText((current + 2)+"、"+q2.question);
                        initQues((current+1),0,q1);
                        initQues((current+2),1,q2);

                        //记录当页的答案,-1为没有选择,分为单选多选
                        if (q1.duoxuan == 0) {
                            if (Integer.parseInt(q1.selectedAns) != -1) {
                                checkBoxes[Integer.parseInt(q1.selectedAns)].setChecked(true);
                            }
                        } else {
                            int selectednum = Integer.parseInt(q1.selectedAns);
                            int currentnum;
                            while (selectednum > 0) {
                                currentnum = selectednum % 10;
                                selectednum = selectednum / 10;
                                checkBoxes[currentnum].setChecked(true);
                            }
                        }

                        if (q2.duoxuan == 0) {
                            if (Integer.parseInt(q2.selectedAns) != -1) {
                                checkBoxes[Integer.parseInt(q2.selectedAns) + 10].setChecked(true);
                            }
                        } else {
                            int selectednum = Integer.parseInt(q2.selectedAns);
                            int currentnum;
                            while (selectednum > 0) {
                                currentnum = selectednum % 10;
                                selectednum = selectednum / 10;
                                checkBoxes[currentnum + 10].setChecked(true);
                            }
                        }
                    }
                } else {
                    Toast.makeText(ExamActivity.this, "你还有没有作答的题目哦~", Toast.LENGTH_LONG).show();
                }
            }

        });

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (current > 0) {
                    ResetBtn();
                    current -= 2 ;

                    if (current==0){
                        tv_head.setVisibility(View.VISIBLE);
                    }

                    Question q1 = list.get(current);
                    Question q2 = list.get(current+1);
                    initBoxs(q1, q2);
//                    textViews[0].setText((current + 1) + "、" + q1.question);
//                    textViews[1].setText((current + 2)+"、"+q2.question);
                    initQues((current+1),0,q1);
                    initQues((current+2),1,q2);

                    //记录当页的答案,-1为没有选择,分为单选多选
                    if (q1.duoxuan==0){
                        if (Integer.parseInt(q1.selectedAns) != -1) {
                            checkBoxes[Integer.parseInt(q1.selectedAns)].setChecked(true);
                        }
                    }else {
                        int selectednum = Integer.parseInt(q1.selectedAns);
                        int currentnum ;
                        while(selectednum > 0){
                            currentnum = selectednum % 10 ;
                            selectednum = selectednum / 10 ;
                            checkBoxes[currentnum].setChecked(true);
                        }
                    }
                    if (q2.duoxuan==0){
                        if (Integer.parseInt(q2.selectedAns) != -1) {
                            checkBoxes[Integer.parseInt(q2.selectedAns)+10].setChecked(true);
                        }
                    }else {
                        int selectednum = Integer.parseInt(q2.selectedAns);
                        int currentnum;
                        while (selectednum > 0) {
                            currentnum = selectednum % 10;
                            selectednum = selectednum / 10;
                            checkBoxes[currentnum+10].setChecked(true);
                        }
                    }
                }
            }
        });
    }

    /**
     * 特殊情况的检测
     */
    private void CheckSpecial(int i) {
        if (list.get(i).selectedAns.equals("")){
            switch (i){
                case 6:
                    list.get(i).selectedAns="-1";
                    break;
                case 7:
                    list.get(i).selectedAns="-1";
                    break;
                case 8:
                    list.get(i).selectedAns="-1";
                    break;
                case 9:
                    list.get(i).selectedAns="-1";
                    break;
                case 10:
                    list.get(i).selectedAns="-1";
                    break;
                case 17:
                    list.get(i).selectedAns="-1";
                    break;
                case 18:
                    list.get(i).selectedAns="-1";
                    break;
                case 19:
                    list.get(i).selectedAns="-1";
                    break;
                case 20:
                    list.get(i).selectedAns="-1";
                    break;
                case 21:
                    list.get(i).selectedAns="-1";
                    break;
                case 22:
                    list.get(i).selectedAns="-1";
                    break;
                case 23:
                    list.get(i).selectedAns="-1";
                    break;
            }
        }
    }

    /**
     * 修改题目的输出，用于题号的特殊设置和题干的特殊设置
     * @param quesNum  题号
     * @param textNum  修改第一个还是第二个textView 0-第一个，1-第二个
     * @param ques     题目
     */
    private void initQues(int quesNum,int textNum,Question ques){
        switch (quesNum){
            case 7:
                textViews[textNum].setText(Html.fromHtml("7.1、就诊过程中<font color='#ff0000'><big>挂号</big></font>让您等待的时间是"));
                break;
            case 8:
                textViews[textNum].setText(Html.fromHtml("7.2、就诊过程中<font color='#ff0000'><big>候诊</big></font>让您等待的时间是"));
                break;
            case 9:
                textViews[textNum].setText(Html.fromHtml("7.3、就诊过程中<font color='#ff0000'><big>等候检查</big></font>让您等待的时间是"));
                break;
            case 10:
                textViews[textNum].setText(Html.fromHtml("7.4、就诊过程中<font color='#ff0000'><big>交费</big></font>让您等待的时间是"));
                break;
            case 11:
                textViews[textNum].setText(Html.fromHtml("7.5、就诊过程中<font color='#ff0000'><big>取药</big></font>让您等待的时间是"));
                break;
            case 12:
                textViews[textNum].setText(Html.fromHtml("二、医生工作评价<br /> 8、您对医生的<font color='#ff0000'><big>医疗技术水平</big></font>的总体评价"));
                break;
            case 13:
                textViews[textNum].setText(Html.fromHtml("9、您对医生的<font color='#ff0000'><big>服务质量</big></font>的总体评价"));
                break;
            case 14:
                textViews[textNum].setText(Html.fromHtml("10、对于<font color='#ff0000'><big>医生</big></font>的工作，您认为哪些方面需要改进（可多选）"));
                break;
            case 15:
                textViews[textNum].setText(Html.fromHtml("三、护士工作评价<br /> 11、您对护士<font color='#ff0000'><big>技术水平</big></font>的总体评价"));
                break;
            case 16:
                textViews[textNum].setText(Html.fromHtml("12、您对护士<font color='#ff0000'><big>服务质量</big></font>的总体评价为"));
                break;
            case 17:
                textViews[textNum].setText(Html.fromHtml("13、对于护士的工作，您认为哪些方面需要<font color='#ff0000'><big>改进</big></font>（可多选）"));
                break;
            case 18:
                textViews[textNum].setText(Html.fromHtml("四、医技科室工作评价<br /> 14.1、本次就诊过程中，您对<font color='#ff0000'><big>药房</big></font>的评价为"));
                break;
            case 19:
                textViews[textNum].setText(Html.fromHtml("14.2、本次就诊过程中，您对<font color='#ff0000'><big>化验</big></font>的评价为"));
                break;
            case 20:
                textViews[textNum].setText(Html.fromHtml("14.3、本次就诊过程中，您对<font color='#ff0000'><big>B超</big></font>的评价为"));
                break;
            case 21:
                textViews[textNum].setText(Html.fromHtml("14.4、本次就诊过程中，您对<font color='#ff0000'><big>心电图</big></font>的评价为"));
                break;
            case 22:
                textViews[textNum].setText(Html.fromHtml("14.5、本次就诊过程中，您对<font color='#ff0000'><big>CT</big></font>的评价为"));
                break;
            case 23:
                textViews[textNum].setText(Html.fromHtml("14.6、本次就诊过程中，您对<font color='#ff0000'><big>X线</big></font>的评价为"));
                break;
            case 24:
                textViews[textNum].setText(Html.fromHtml("14.7、本次就诊过程中，您对<font color='#ff0000'><big>磁共振</big></font>的评价为"));
                break;
            case 25:
                textViews[textNum].setText(Html.fromHtml("五、就医环境及后勤保障评价<br /> 15.1、您对<font color='#ff0000'><big>就医环境及后勤保障</big></font>的总体评价"));
                break;
            case 26:
                textViews[textNum].setText("15.2、" +ques.question);
                break;
            case 27:
                textViews[textNum].setText("15.3、" +ques.question);
                break;
            case 28:
                textViews[textNum].setText("15.4、" +ques.question);
                break;
            case 29:
                textViews[textNum].setText("15.5、" +ques.question);
                break;
            case 30:
                textViews[textNum].setText("15.6、" +ques.question);
                break;
            case 31:
                textViews[textNum].setText("15.7、" +ques.question);
                break;
            case 32:
                textViews[textNum].setText("15.8、" +ques.question);
                break;
            case 33:
                textViews[textNum].setText("15.9、" +ques.question);
                break;
            case 34:
                textViews[textNum].setText("15.10、" +ques.question);
                break;
            case 35:
                textViews[textNum].setText("15.11、" +ques.question);
                break;
            case 36:
                textViews[textNum].setText(Html.fromHtml("六、医德医风评价<br /> 16.1、您对医院<font color='#ff0000'><big>“廉洁行医，医德医风”</big></font>的总体印象"));
                break;
            case 37:
                textViews[textNum].setText("16.2、"+ques.question);
                break;
            case 38:
                textViews[textNum].setText("16.3、"+ques.question);
                break;
            case 39:
                textViews[textNum].setText(Html.fromHtml("七、医疗费用评价<br /> 17、您本次就医的<font color='#ff0000'><big>医疗费用明细</big></font>是否清晰"));
                break;
            case 40:
                textViews[textNum].setText(Html.fromHtml("八、患者忠诚度评价<br /> 18、如果您的家人和朋友需要医疗保健服务，您是否愿意<font color='#ff0000'><big>推荐</big></font>本院"));
                break;
            default:
                textViews[textNum].setText(quesNum + "、" + ques.question);
        }
    }

    /**
     * 使全部按钮重新enable
     */
    private void ResetBtn(){
        for (int i=0;i<20;i++){
            checkBoxes[i].setEnabled(true);
        }
    }

    /**
     * 处理单选checkBox的监听和禁用
     * mode-0 第一题  mode-1第二题  flag不被选中的
     */
    private void BuildSingle(int mode,int flag){
        if (mode==0){
            for (int i=0;i<10;i++){
                if (i==flag){
                    continue;
                }
//                checkBoxes[i].setEnabled(false);
                checkBoxes[i].setChecked(false);
            }
        }
        if (mode==1){
            for (int i=10;i<20;i++){
                if (i==flag){
                    continue;
                }
//                checkBoxes[i].setEnabled(false);
                checkBoxes[i].setChecked(false);
            }
        }
    }


    /**
     * 判断当前页是否都作答了
     * @return
     */
    private boolean IsSelected(int index){
        Log.d("index", index + "");
        boolean q1=false,q2=false;
        for (int i=0;i<10;i++){
            //current>=7-1
            if ((current>=6&&current<=10)||(current>=17&&current<=23)){
                q1=true;
                break;
            }
            if (checkBoxes[i].isChecked()){
                q1=true;
                break;
            }
        }
        for (int i=10;i<20;i++){
            if ((current+1>=6&&current+1<=10)||(current+1>=17&&current+1<=23)){
                q2=true;
                break;
            }
            if (checkBoxes[i].isChecked()){
                q2=true;
                break;
            }
        }

        return (q1&&q2);
    }




    /**
     * 根据结果展现出来checkBox
     */
    private void initBoxs(Question q1 ,Question q2 ){
        //清除全部状态
        for(int i = 0 ; i < 20 ; i++) {
            checkBoxes[i].setChecked(false);
            checkBoxes[i].setVisibility(View.VISIBLE);
        }

        if ((q1.ans0).equals("NULL")){
            checkBoxes[0].setVisibility(View.GONE);
        }else{
            checkBoxes[0].setText("(1)"+q1.ans0);
        }

        if ((q1.ans1).equals("NULL")){
            checkBoxes[1].setVisibility(View.GONE);
        }else{
            checkBoxes[1].setText("(2)"+q1.ans1);
        }

        if ((q1.ans2).equals("NULL")){
            checkBoxes[2].setVisibility(View.GONE);
        }else{
            checkBoxes[2].setText("(3)"+q1.ans2);
        }

        if ((q1.ans3).equals("NULL")){
            checkBoxes[3].setVisibility(View.GONE);
        }else{
            checkBoxes[3].setText("(4)"+q1.ans3);
        }

        if ((q1.ans4).equals("NULL")){
            checkBoxes[4].setVisibility(View.GONE);
        }else{
            checkBoxes[4].setText("(5)"+q1.ans4);
        }

        if ((q1.ans5).equals("NULL")){
            checkBoxes[5].setVisibility(View.GONE);
        }else{
            checkBoxes[5].setText("(6)"+q1.ans5);
        }

        if ((q1.ans6).equals("NULL")){
            checkBoxes[6].setVisibility(View.GONE);
        }else{
            checkBoxes[6].setText("(7)"+q1.ans6);
        }

        if ((q1.ans7).equals("NULL")){
            checkBoxes[7].setVisibility(View.GONE);
        }else{
            checkBoxes[7].setText("(8)"+q1.ans7);
        }

        if ((q1.ans8).equals("NULL")){
            checkBoxes[8].setVisibility(View.GONE);
        }else{
            checkBoxes[8].setText("(9)"+q1.ans8);
        }

        if ((q1.ans9).equals("NULL")){
            checkBoxes[9].setVisibility(View.GONE);
        }else{
            checkBoxes[9].setText("(10)"+q1.ans9);
        }

        //************************************************

        if ((q2.ans0).equals("NULL")){
            checkBoxes[10].setVisibility(View.GONE);
        }else{
            checkBoxes[10].setText("(1)"+q2.ans0);
        }

        if ((q2.ans1).equals("NULL")){
            checkBoxes[11].setVisibility(View.GONE);
        }else{
            checkBoxes[11].setText("(2)"+q2.ans1);
        }

        if ((q2.ans2).equals("NULL")){
            checkBoxes[12].setVisibility(View.GONE);
        }else{
            checkBoxes[12].setText("(3)"+q2.ans2);
        }

        if ((q2.ans3).equals("NULL")){
            checkBoxes[13].setVisibility(View.GONE);
        }else{
            checkBoxes[13].setText("(4)"+q2.ans3);
        }

        if ((q2.ans4).equals("NULL")){
            checkBoxes[14].setVisibility(View.GONE);
        }else{
            checkBoxes[14].setText("(5)"+q2.ans4);
        }

        if ((q2.ans5).equals("NULL")){
            checkBoxes[15].setVisibility(View.GONE);
        }else{
            checkBoxes[15].setText("(6)"+q2.ans5);
        }

        if ((q2.ans6).equals("NULL")){
            checkBoxes[16].setVisibility(View.GONE);
        }else{
            checkBoxes[16].setText("(7)"+q2.ans6);
        }

        if ((q2.ans7).equals("NULL")){
            checkBoxes[17].setVisibility(View.GONE);
        }else{
            checkBoxes[17].setText("(8)"+q2.ans7);
        }

        if ((q2.ans8).equals("NULL")){
            checkBoxes[18].setVisibility(View.GONE);
        }else{
            checkBoxes[18].setText("(9)"+q2.ans8);
        }

        if ((q2.ans9).equals("NULL")){
            checkBoxes[19].setVisibility(View.GONE);
        }else{
            checkBoxes[19].setText("(10)"+q2.ans9);
        }
    }

    private int checkedBtn = -1;

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        //判定当前点击的是第几的按钮
        switch (compoundButton.getId()){
            case R.id.ans10:
                checkedBtn=0;
                break;
            case R.id.ans11:
                checkedBtn=1;
                break;
            case R.id.ans12:
                checkedBtn=2;
                break;
            case R.id.ans13:
                checkedBtn=3;
                break;
            case R.id.ans14:
                checkedBtn=4;
                break;
            case R.id.ans15:
                checkedBtn=5;
                break;
            case R.id.ans16:
                checkedBtn=6;
                break;
            case R.id.ans17:
                checkedBtn=7;
                break;
            case R.id.ans18:
                checkedBtn=8;
                break;
            case R.id.ans19:
                checkedBtn=9;
                break;
            case R.id.ans20:
                checkedBtn=10;
                break;
            case R.id.ans21:
                checkedBtn=11;
                break;
            case R.id.ans22:
                checkedBtn=12;
                break;
            case R.id.ans23:
                checkedBtn=13;
                break;
            case R.id.ans24:
                checkedBtn=14;
                break;
            case R.id.ans25:
                checkedBtn=15;
                break;
            case R.id.ans26:
                checkedBtn=16;
                break;
            case R.id.ans27:
                checkedBtn=17;
                break;
            case R.id.ans28:
                checkedBtn=18;
                break;
            case R.id.ans29:
                checkedBtn=19;
                break;
        }


        if ((list.get(current).duoxuan==0)){
            if (compoundButton.isChecked()==false&&checkedBtn<10){
                for (int i=0;i<10;i++){
                    checkBoxes[i].setEnabled(true);
                }
            }
            if (compoundButton.isChecked()){
                switch (compoundButton.getId()){
                    case R.id.ans10:
                        BuildSingle(0,0);
                        break;
                    case R.id.ans11:
                        BuildSingle(0,1);
                        break;
                    case R.id.ans12:
                        BuildSingle(0,2);
                        break;
                    case R.id.ans13:
                        BuildSingle(0,3);
                        break;
                    case R.id.ans14:
                        BuildSingle(0,4);
                        break;
                    case R.id.ans15:
                        BuildSingle(0,5);
                        break;
                    case R.id.ans16:
                        BuildSingle(0,6);
                        break;
                    case R.id.ans17:
                        BuildSingle(0,7);
                        break;
                    case R.id.ans18:
                        BuildSingle(0,8);
                        break;
                    case R.id.ans19:
                        BuildSingle(0,9);
                        break;
                }
            }
        }
        if ((list.get(current+1).duoxuan==0)){
            if (compoundButton.isChecked()==false&&checkedBtn>=10){
                for (int i=10;i<20;i++){
                    checkBoxes[i].setEnabled(true);
                }
            }
            if (compoundButton.isChecked()){
                switch (compoundButton.getId()){
                    case R.id.ans20:
                        BuildSingle(1,10);
                        break;
                    case R.id.ans21:
                        BuildSingle(1,11);
                        break;
                    case R.id.ans22:
                        BuildSingle(1,12);
                        break;
                    case R.id.ans23:
                        BuildSingle(1,13);
                        break;
                    case R.id.ans24:
                        BuildSingle(1,14);
                        break;
                    case R.id.ans25:
                        BuildSingle(1,15);
                        break;
                    case R.id.ans26:
                        BuildSingle(1,16);
                        break;
                    case R.id.ans27:
                        BuildSingle(1,17);
                        break;
                    case R.id.ans28:
                        BuildSingle(1,18);
                        break;
                    case R.id.ans29:
                        BuildSingle(1,19);
                        break;
                }
            }
        }
    }
}
