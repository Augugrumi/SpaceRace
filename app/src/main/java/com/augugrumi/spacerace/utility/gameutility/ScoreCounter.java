package com.augugrumi.spacerace.utility.gameutility;

import android.support.annotation.NonNull;
import android.util.SparseBooleanArray;

import com.augugrumi.spacerace.utility.QuestionAnswerManager;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dpolonio on 24/11/17.
 */

public class ScoreCounter {

    private int score;
    private Map<LatLng, List<Score>> pointMap;

    private ScoreCounter(@NonNull Map<LatLng, List<Score>> pointMap, int score) {
        this.score = score;
        this.pointMap = pointMap;
    }

    public int getScore() {
        return score;
    }

    @NonNull
    public Map<LatLng, SparseBooleanArray> getAnswerCorrectnessMap () {
        Map<LatLng, SparseBooleanArray> res = new HashMap<>();

        for (LatLng id : pointMap.keySet()) {
            SparseBooleanArray isCorrectList = new SparseBooleanArray();

            for (Score score : pointMap.get(id)) {
                isCorrectList.put(score.getQuestionId(), score.isCorrect());
            }
        }
        return res;
    }

    private static class Score {

        private boolean isCorrect;
        private int questionId;
        private LatLng latLng;

        private Score (LatLng latLng, int questionId) {
            this.latLng = latLng;
            this.questionId = questionId;
        }

        void setAnswer(String answer) {
            isCorrect = answer.equals(
            QuestionAnswerManager.getRightAnswer(latLng, questionId));
        }

        boolean isCorrect () {
            return isCorrect;
        }

        int getQuestionId () {
            return questionId;
        }

        int getScore() {

            // 1 point for a correct answer
            // 0 points for a wrong answer
            return isCorrect ? 1 : 0;
        }
    }

    public static class Builder {

        private Map<LatLng, List<Score>> pointMap;
        private int score;

        public Builder() {
            pointMap = new HashMap<>();
        }

        @NonNull
        public Builder appendPOIQuestions(@NonNull LatLng POIId) {
            List<Score> questionsWithScore = new ArrayList<>();

            for (int i = 1; i <= 3; i++) {
                questionsWithScore.add(
                        new Score(POIId, i)
                );
            }

            pointMap.put(POIId, questionsWithScore);
            return this;
        }

        @NonNull
        public Builder appendAnswer(@NonNull LatLng POIId, int questionId, @NonNull String answer) {
            pointMap.get(POIId).get(questionId-1).setAnswer(answer);
            score += pointMap.get(POIId).get(questionId-1).getScore();

            return this;
        }

        @NonNull
        public ScoreCounter build () {
            return new ScoreCounter(pointMap, score);
        }
    }
}
