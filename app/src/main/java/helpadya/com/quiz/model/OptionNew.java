package helpadya.com.quiz.model;

/**
 * Created by sud on 19/2/18.
 */

public class OptionNew {
    int id, question_id;
    String option;

    public OptionNew(int id, int question_id, String option) {
        this.id = id;
        this.question_id = question_id;
        this.option = option;
    }

    public OptionNew() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }
}
