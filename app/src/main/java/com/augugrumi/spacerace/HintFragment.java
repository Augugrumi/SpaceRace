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

        return mainView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void setHint(int hintId) {
        nextHintText.setText(hintId);
    }

    public void setHint(String hintString) {
        nextHintText.setText(hintString);
    }

    @OnClick(R.id.to_quiz_btn)
    public void onClickStartQuiz() {
        showView(question1View);
    }

    @OnClick({R.id.quiz1_rx1_btn, R.id.quiz1_rx2_btn, R.id.quiz1_rx3_btn})
    public void onClickRxQuestion1() {
        showView(question2View);
    }

    @OnClick({R.id.quiz2_rx1_btn, R.id.quiz2_rx2_btn, R.id.quiz2_rx3_btn})
    public void onClickRxQuestion2() {
        showView(question3View);
    }

    @OnClick({R.id.quiz3_rx1_btn, R.id.quiz3_rx2_btn, R.id.quiz3_rx3_btn})
    public void onClickRxQuestion3() {
        showView(quizResultView);
    }

    @OnClick({R.id.skip_quiz_btn, R.id.to_next_hint_btn})
    public void onClickSkipOrFinishedQuiz() {
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
}
