package helpadya.com.quiz.model;

/**
 * Created by sud on 28/1/18.
 */

public class ScoreList {
    int id, score;
    String name;

    public ScoreList(int id, int score, String name) {
        this.id = id;
        this.score = score;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
