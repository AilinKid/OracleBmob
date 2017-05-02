package com.example.arena.oracle.fargment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arena.oracle.R;
import com.example.arena.oracle.Utils.Action;
import com.example.arena.oracle.Utils.BmobUtils;
import com.example.arena.oracle.activity.Main2Activity;
import com.example.arena.oracle.bean.Paper;
import com.example.arena.oracle.bean.Question;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddPaperFragment extends Fragment implements View.OnClickListener{

    private Context context;

    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private Paper paper = new Paper();
    private LinearLayout addPaperDialog;
    private EditText inputPaperId;
    private EditText inputPaperName;
    private Button addQuestionBtn;
    private Button finishBtn;
    private List<Question> questions = new ArrayList<Question>();

    private Button dialogSureBtn;
    private TextView paperTime;
    private TextView id;
    private EditText ETquestion, EToptionA, EToptionB, EToptionC, EToptionD;
    private CheckBox CBoptionA, CBoptionB, CBoptionC, CBoptionD;
    private int tag=1;


    private ProgressDialog waitDialog=null;





    public AddPaperFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_paper, container, false);
        //fragment中注册，必须用this
        EventBus.getDefault().register(this);
        addPaperDialog = (LinearLayout) view.findViewById(R.id.add_paper_dialog);
        inputPaperId = (EditText) view.findViewById(R.id.et_paper_id);
        inputPaperName = (EditText) view.findViewById(R.id.et_paper_name);
        addQuestionBtn = (Button)view.findViewById(R.id.button_add_question);
        finishBtn = (Button) view.findViewById(R.id.button_add_paper);

        dialogSureBtn = (Button)view.findViewById(R.id.dialog_sure);
        paperTime = (TextView)view.findViewById(R.id.tv_paper_date);

        //提醒题目编号的
        id = (TextView)view.findViewById(R.id.tv_id);
        ETquestion = (EditText)view.findViewById(R.id.et_question);
        EToptionA = (EditText)view.findViewById(R.id.et_optionA);
        EToptionB = (EditText)view.findViewById(R.id.et_optionB);
        EToptionC = (EditText)view.findViewById(R.id.et_optionC);
        EToptionD = (EditText)view.findViewById(R.id.et_optionD);

        CBoptionA = (CheckBox)view.findViewById(R.id.checkbox_A);
        CBoptionB = (CheckBox)view.findViewById(R.id.checkbox_B);
        CBoptionC = (CheckBox)view.findViewById(R.id.checkbox_C);
        CBoptionD = (CheckBox)view.findViewById(R.id.checkbox_D);


        //试题添加从1开始
        id.setText(tag+".");
        Log.d("papertime",df.format(new Date()));
        String temptime = df.format(new Date());
        paperTime.setText(temptime);
        dialogSureBtn.setOnClickListener(this);

        //dialog聚焦
        return view;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_sure:
                if(!inputPaperId.getText().toString().equals("")
                        &&!inputPaperName.getText().toString().equals("")){
                    paper.setPaperId(inputPaperId.getText().toString());
                    paper.setPaperName(inputPaperName.getText().toString());
                    paper.setJoinTime(df.format(new Date()));
                    //dialog消隐
                    addPaperDialog.setVisibility(View.GONE);
                    addQuestionBtn.setOnClickListener(this);
                    finishBtn.setOnClickListener(this);
                }
                else{
                    //Snackbar.make(this,"请补全信息", Snackbar.LENGTH_SHORT).show();
                    Toast.makeText(context,"请补全信息", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.button_add_paper:
                finishBtn.setOnClickListener(null);
                savePaper();
                waitDialog = new ProgressDialog(context);
                waitDialog.setMessage("登录中");
                waitDialog.setCancelable(false);
                waitDialog.show();
                break;
            case R.id.button_add_question:
                addQuestion();
                break;
        }
    }

    private void addQuestion(){
        int res = checkInformation();
        switch (res){
            case -1:
                Toast.makeText(context, "请补全信息", Toast.LENGTH_SHORT).show();
                break;
            case 0:
                Toast.makeText(context, "请选择答案", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                CBoptionA.setChecked(false);
                saveQuesiton("A");
                break;
            case 2:
                CBoptionB.setChecked(false);
                saveQuesiton("B");
                break;
            case 3:
                CBoptionC.setChecked(false);
                saveQuesiton("C");
                break;
            case 4:
                CBoptionD.setChecked(false);
                saveQuesiton("D");
                break;
        }
    }

    private int checkInformation(){
        if(!ETquestion.getText().toString().equals("")&&!EToptionA.getText().toString().equals("")&&
                !EToptionB.getText().toString().equals("")&&!EToptionC.getText().toString().equals("") &&
                !EToptionD.getText().toString().equals("")){
            //信息齐全
            if(CBoptionA.isChecked()){
                return 1;
            }
            if(CBoptionB.isChecked()){
                return 2;
            }
            if(CBoptionC.isChecked()){
                return 3;
            }
            if(CBoptionD.isChecked()){
                return 4;
            }
            return 0;
        }
        else{
            return -1;
        }
    }

    private void saveQuesiton(String answer){
        Question q = new Question();
        q.setQuestionId(paper.getPaperName()+tag);
        q.setPaperName(paper.getPaperName());
        q.setQuestion(ETquestion.getText().toString());
        q.setOptionA(EToptionA.getText().toString());
        q.setOptionB(EToptionB.getText().toString());
        q.setOptionC(EToptionC.getText().toString());
        q.setOptionD(EToptionD.getText().toString());
        q.setAnswer(answer);

        questions.add(q);
        tag++;
        id.setText(tag+".");
        ETquestion.setText("");
        EToptionA.setText("");
        EToptionB.setText("");
        EToptionC.setText("");
        EToptionD.setText("");

        Log.d("问题添加", "问题"+tag+"-1添加成功");
    }

    private boolean savePaper(){
        addQuestion();
        BmobUtils.saveNewPaper(context, paper, questions);
        return false;
    }

    @Subscriber
    private void onReceiveActionEvent(Action action){
        Main2Activity paentActivity = (Main2Activity) getActivity();
        switch (action){
            case SAVE_PAPER_SUCCESS:
                waitDialog.dismiss();
                Toast.makeText(context, "保存试卷成功", Toast.LENGTH_SHORT).show();
                //不是activity不能用finish 这边做好事切换fragment
                paentActivity.refreshTolist();
                break;
            case SAVE_PAPER_ERROR:
                waitDialog.dismiss();
                Toast.makeText(context, "保存试卷失败", Toast.LENGTH_SHORT).show();
                paentActivity.refreshTolist();
                break;
        }
    }


}
