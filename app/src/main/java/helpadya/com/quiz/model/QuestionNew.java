package helpadya.com.quiz.model;

import java.util.ArrayList;

/**
 * Created by sud on 19/2/18.
 */

public class QuestionNew {
    int id, correct_id;
    String question_english, question_hindi, points;
    ArrayList<OptionNew> questionOptionListNew = new ArrayList<>();

    public QuestionNew(int id, int correct_id, String question_english, String question_hindi, String points) {
        this.id = id;
        this.correct_id = correct_id;
        this.question_english = question_english;
        this.question_hindi = question_hindi;
        this.points = points;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public void setQuestionOptionListNew(ArrayList<OptionNew> questionOptionListNew) {
        this.questionOptionListNew = questionOptionListNew;
    }

    public QuestionNew() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCorrect_id() {
        return correct_id;
    }

    public void setCorrect_id(int correct_id) {
        this.correct_id = correct_id;
    }

    public String getQuestion_english() {
        return question_english;
    }

    public void setQuestion_english(String question_english) {
        this.question_english = question_english;
    }

    public String getQuestion_hindi() {
        return question_hindi;
    }

    public void setQuestion_hindi(String question_hindi) {
        this.question_hindi = question_hindi;
    }

    public void addQuestionOptionNew (OptionNew optionNew) {
        this.questionOptionListNew.add(optionNew);
    }

    public ArrayList<OptionNew> getQuestionOptionListNew () {
        return questionOptionListNew;
    }
}
