package com.augugrumi.spacerace;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.augugrumi.spacerace.utility.QuestionAnswerManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class FirstHintFragment extends AbsHintFragment {

    //viewgroups
    @BindView(R.id.next_hint_layout) ViewGroup nextHintView;
    @BindView(R.id.next_hint_image_layout) ViewGroup imageHintView;


    //next hint view
    @BindView(R.id.next_hint_text)
    TextView nextHintText;
    @BindView(R.id.to_image_hint)
    Button toImageHintBtn;


    //next hint image view
    @BindView(R.id.to_text_hint) Button toHintBtn;
    @BindView(R.id.hide_btn) Button hideBtn;

    //place image
    @BindView(R.id.place_image) ImageView placeImage;


    public FirstHintFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        parent = (MapActivity)getActivity();
        View v = inflater.inflate(R.layout.fragment_first_hint, container, false);
        ButterKnife.bind(this, v);

        nextHintText.setMovementMethod(new ScrollingMovementMethod());
        layouts = new ArrayList<>();
        layouts.add(nextHintView);
        layouts.add(imageHintView);

        // Inflate the layout for this fragment
        return v;
    }

    @OnClick(R.id.to_image_hint)
    public void onClickShowImage(View v) {
        showView(imageHintView);
    }

    @OnClick(R.id.hide_btn)
    public void onClickHide() {
        parent.hideHintAndShowMap();
    }

    @OnClick(R.id.to_text_hint)
    public void onClickSkipOrFinishedQuiz(View v) {
        showView(nextHintView);
    }

    @Override
    protected void setHintData() {
        nextHintText.setText(QuestionAnswerManager.getHint(actualPoi));
        placeImage.setImageDrawable(getActivity().getDrawable(QuestionAnswerManager.getImage(actualPoi)));
    }
}
