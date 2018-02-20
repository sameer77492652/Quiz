package helpadya.com.quiz.model;

import java.util.ArrayList;

/**
 * Created by sud on 17/12/17.
 */

public class Question {
    int id, question_id, question_points, language_id;
    String question;
    ArrayList<Option> questionOptionList = new ArrayList<>();

    public Question(int id, int question_id, int question_points, int language_id, String question) {
        this.id = id;
        this.question_id = question_id;
        this.question_points = question_points;
        this.language_id = language_id;
        this.question = question;
    }

    public Question() {

    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public int getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(int language_id) {
        this.language_id = language_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestion_points() {
        return question_points;
    }

    public void setQuestion_points(int question_points) {
        this.question_points = question_points;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<Option> getQuestionOptionList () {
        return questionOptionList;
    }

    public void setQuestionOptionList (ArrayList<Option> questionOptionList) {
        this.questionOptionList = questionOptionList;
    }

    public void addQuestionOption (Option option) {
        this.questionOptionList.add(option);
    }
}
