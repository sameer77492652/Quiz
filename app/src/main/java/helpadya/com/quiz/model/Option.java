package helpadya.com.quiz.model;

/**
 * Created by sud on 17/12/17.
 */

public class Option {
    int id, question_id, language_id, option_id, status;
    String option;

    public Option(int id, int question_id, int language_id, int option_id, int status, String option) {
        this.id = id;
        this.question_id = question_id;
        this.language_id = language_id;
        this.option_id = option_id;
        this.status = status;
        this.option = option;
    }

    public Option() {

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

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public int getOption_id() {
        return option_id;
    }

    public void setOption_id(int option_id) {
        this.option_id = option_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }
}
