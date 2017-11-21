package com.augugrumi.spacerace;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Marco Zanella
 * @version 0.01
 *          date 09/11/17
 */

public class HintFragment extends Fragment {

    private ArrayList<ViewGroup> layouts;

    //views
    @BindView(R.id.explanation_layout) ViewGroup explanationView;
    @BindView(R.id.question1_layout) ViewGroup question1View;
    @BindView(R.id.question2_layout) ViewGroup question2View;
    @BindView(R.id.question3_layout) ViewGroup question3View;
    @BindView(R.id.quiz_result_layout) ViewGroup quizResultView;
    @BindView(R.id.next_hint_layout) ViewGroup nextHintView;

    //content explanation view
    @BindView(R.id.place_explanation_title) TextView explanationTitleText;
    @BindView(R.id.place_explanation_text) TextView explanationContentText;
    @BindView(R.id.skip_quiz_btn) Button skipBtn;
    @BindView(R.id.to_quiz_btn) Button quizBtn;

    //question1 view
    @BindView(R.id.question1_text) TextView question1Text;
    @BindView(R.id.quiz1_rx1_btn) Button quiz1Rx1Btn;
    @BindView(R.id.quiz1_rx2_btn) Button quiz1Rx2Btn;
    @BindView(R.id.quiz1_rx3_btn) Button quiz1Rx3Btn;

    //question2 view
    @BindView(R.id.question2_text) TextView question2Text;
    @BindView(R.id.quiz2_rx1_btn) Button quiz2Rx1Btn;
    @BindView(R.id.quiz2_rx2_btn) Button quiz2Rx2Btn;
    @BindView(R.id.quiz2_rx3_btn) Button quiz2Rx3Btn;

    //question3 view
    @BindView(R.id.question3_text) TextView question3Text;
    @BindView(R.id.quiz3_rx1_btn) Button quiz3Rx1Btn;
    @BindView(R.id.quiz3_rx2_btn) Button quiz3Rx2Btn;
    @BindView(R.id.quiz3_rx3_btn) Button quiz3Rx3Btn;

    //score view
    @BindView(R.id.score_text) TextView scoreText;
    @BindView(R.id.to_next_hint_btn) Button nextHintBtn;

    //next hint view
    @BindView(R.id.next_hint_text) TextView nextHintText;
    @BindView(R.id.hide_btn) Button hideBtn;

    private MapActivity parent;

    private int idNum;
    private String idName;
    private int correctAnswer1;
    private int correctAnswer2;
    private int correctAnswer3;
    private int questionNum;
    private int score;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainView = inflater.inflate(R.layout.fragment_hint, container, false);
        ButterKnife.bind(this, mainView);

        parent = (MapActivity)getActivity();

        explanationContentText.setMovementMethod(new ScrollingMovementMethod());
        nextHintText.setMovementMethod(new ScrollingMovementMethod());
        layouts = new ArrayList<>();
        layouts.add(explanationView);
        layouts.add(question1View);
        layouts.add(question2View);
        layouts.add(question3View);
        layouts.add(quizResultView);
        layouts.add(nextHintView);
        showView(explanationView);

        questionNum = 0;
        score = 0;
        correctAnswer1 = 0;
        correctAnswer2 = 0;
        correctAnswer3 = 0;

        return mainView;
    }

    //informazioni sul luogo -> domande -> next hint


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @OnClick(R.id.to_quiz_btn)
    public void onClickStartQuiz() {

        questionNum++;

        int questionID = getQuestionID(questionNum);

        question1Text.setText(questionID);

        //todo: sort answers
        int aNumber = 1;
        int answerID = getAnswerID(questionNum, aNumber);

        quiz1Rx1Btn.setText(answerID);

        aNumber++;
        answerID = getAnswerID(questionNum, aNumber);

        quiz1Rx2Btn.setText(answerID);

        aNumber++;
        answerID = getAnswerID(questionNum, aNumber);

        quiz1Rx3Btn.setText(answerID);

        showView(question1View);
    }

    @OnClick({R.id.quiz1_rx1_btn, R.id.quiz1_rx2_btn, R.id.quiz1_rx3_btn})
    public void onClickRxQuestion1(View view) {

        int givenAnswerNum = 0;
        switch (view.getId()){
            case R.id.quiz1_rx1_btn:
                givenAnswerNum = 1;
                break;
            case R.id.quiz1_rx2_btn:
                givenAnswerNum = 2;
                break;
            case R.id.quiz1_rx3_btn:
                givenAnswerNum = 3;
                break;
        }

        if(correctAnswer1 == givenAnswerNum)
            score++;

        questionNum++;

        int questionID = getQuestionID(questionNum);

        question2Text.setText(questionID);

        //todo: sort answers
        int aNumber = 1;
        int answerID = getAnswerID(questionNum, aNumber);

        quiz2Rx1Btn.setText(answerID);

        aNumber++;
        answerID = getAnswerID(questionNum, aNumber);

        quiz2Rx2Btn.setText(answerID);

        aNumber++;
        answerID = getAnswerID(questionNum, aNumber);

        quiz2Rx3Btn.setText(answerID);

        showView(question2View);
    }

    @OnClick({R.id.quiz2_rx1_btn, R.id.quiz2_rx2_btn, R.id.quiz2_rx3_btn})
    public void onClickRxQuestion2(View view) {

        int givenAnswerNum = 0;
        switch (view.getId()){
            case R.id.quiz1_rx1_btn:
                givenAnswerNum = 1;
                break;
            case R.id.quiz1_rx2_btn:
                givenAnswerNum = 2;
                break;
            case R.id.quiz1_rx3_btn:
                givenAnswerNum = 3;
                break;
        }

        if(correctAnswer2 == givenAnswerNum)
            score++;

        questionNum++;

        int questionID = getQuestionID(questionNum);

        question3Text.setText(questionID);

        //todo: sort answers
        int aNumber = 1;
        int answerID = getAnswerID(questionNum, aNumber);

        quiz3Rx1Btn.setText(answerID);

        aNumber++;
        answerID = getAnswerID(questionNum, aNumber);

        quiz3Rx2Btn.setText(answerID);

        aNumber++;
        answerID = getAnswerID(questionNum, aNumber);

        quiz3Rx3Btn.setText(answerID);

        showView(question3View);
    }

    @OnClick({R.id.quiz3_rx1_btn, R.id.quiz3_rx2_btn, R.id.quiz3_rx3_btn})
    public void onClickRxQuestion3(View view) {

        int givenAnswerNum = 0;
        switch (view.getId()){
            case R.id.quiz1_rx1_btn:
                givenAnswerNum = 1;
                break;
            case R.id.quiz1_rx2_btn:
                givenAnswerNum = 2;
                break;
            case R.id.quiz1_rx3_btn:
                givenAnswerNum = 3;
                break;
        }

        if(correctAnswer3 == givenAnswerNum)
            score++;

        questionNum = 0;

        scoreText.setText(score+"/3");

        //todo: save the score somewhere over the rainbow

        score = 0;
        correctAnswer3 = 0;
        correctAnswer2 = 0;
        correctAnswer1 = 0;

        showView(quizResultView);
    }

    @OnClick({R.id.skip_quiz_btn, R.id.to_next_hint_btn})
    public void onClickSkipOrFinishedQuiz(View v) {

        idNum++;

                

        showView(nextHintView);
    }

    @OnClick(R.id.hide_btn)
    public void onClickHide() {
        parent.hideHintAndShowMap();
    }

    private void showView(ViewGroup view) {
        for (ViewGroup v : layouts) {
            v.setVisibility(View.GONE);
        }
        view.setVisibility(View.VISIBLE);
    }

    private int getAnswerID(int qNumber, int aNumber){
        return getResources().getIdentifier("answer_"+idNum+"_"+qNumber+"_"+aNumber,
                "string", "com.augugrumi.spacerace");
    }

    private int getQuestionID(int qNumber){
        return getResources().getIdentifier("question_"+idNum+"_"+qNumber,
                "string", "com.augugrumi.spacerace");
    }

}
