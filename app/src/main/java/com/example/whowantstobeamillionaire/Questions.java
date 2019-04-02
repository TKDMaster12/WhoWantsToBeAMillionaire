package com.example.whowantstobeamillionaire;

import java.util.Map;

class Questions {
    private Map<String,String> answers;
    private String correctAnswer;
    private String askedQuestion;

    //constructor
    public Questions() {
    }

    //constructor setting variables
    public Questions(Map<String, String> answers, String correctAnswer, String askedQuestion) {
        this.answers = answers;
        this.correctAnswer = correctAnswer;
        this.askedQuestion = askedQuestion;
    }

    //gets and set of variables
    public Map<String, String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, String> answers) {
        this.answers = answers;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getAskedQuestion() {
        return askedQuestion;
    }

    public void setAskedQuestion(String askedQuestion) {
        this.askedQuestion = askedQuestion;
    }
}
