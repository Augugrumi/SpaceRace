/**
* Copyright 2017 Davide Polonio <poloniodavide@gmail.com>, Federico Tavella
* <fede.fox16@gmail.com> and Marco Zanella <zanna0150@gmail.com>
* 
* This file is part of SpaceRace.
* 
* SpaceRace is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* SpaceRace is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with SpaceRace.  If not, see <http://www.gnu.org/licenses/>.
*/

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
    public void setHintData() {
        nextHintText.setText(QuestionAnswerManager.getHint(actualPoi));
        placeImage.setImageDrawable(getActivity().getDrawable(QuestionAnswerManager.getImage(actualPoi)));
    }
}
