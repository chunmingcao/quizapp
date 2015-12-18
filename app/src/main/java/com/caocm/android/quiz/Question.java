package com.caocm.android.quiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by caocm_000 on 12/9/2015.
 */
public class Question {
    private String text;
    private List<String> options = new ArrayList();
    private int answer;

    private static List<Question> questions = new ArrayList<>();

    static {
        for(int i = 0; i < 10; i ++) {
            Question q = new Question("Q" + i + ". What is your name?");
            q.addOption("China");
            q.addOption("Japan");
            q.addOption("United State");
            q.addOption("Moon");
            q.setAnswer(0);
            questions.add(q);
        }
    }
    static int index = 0;
    static public Question getNextQuestion(){
        if(index <= 0) index = 0;
        if(index >= questions.size()) index = questions.size()-1;
        return questions.get(index ++);
    }
    static public Question getPreQuestion(){
        if(index <= 0) index = 0;
        if(index >= questions.size()) index = questions.size()-1;
        return questions.get(index --);
    }
    public Question(String text){
        this.text = text;
    }
    public void addOption(String option){
        options.add(option);
    }

    public List getOptions() {
        return options;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString(){
        String ss = text;
        for(String option : options){
            ss += "|" + option;
        }
        ss += answer;
        return ss;
    }
}
