package my.edu.taruc.lab22profile;

/**
 * Created by asus A555l on 16/1/2018.
 */

public class User {
    String Name;
    String Password;
    int score_addition;
    int score_subtraction;
    int score_multipication;
    int score_division;
    int score_mix;
    String Standard;

    public User() {
    }

    public User(String name, String password, int score_addition, int score_subtraction, int score_multipication,
                int score_division,int score_mix) {
        Name = name;
        Password = password;
        this.score_addition = score_addition;
        this.score_subtraction = score_subtraction;
        this.score_multipication = score_multipication;
        this.score_division = score_division;
        this.score_mix = score_mix;
    }

    public int getScore_mix() {
        return score_mix;
    }

    public void setScore_mix(int score_mix) {
        this.score_mix = score_mix;
    }

    public int getScore_addition() {
        return score_addition;
    }

    public void setScore_addition(int score_addition) {
        this.score_addition = score_addition;
    }

    public int getScore_subtraction() {
        return score_subtraction;
    }

    public void setScore_subtraction(int score_subtraction) {
        this.score_subtraction = score_subtraction;
    }

    public int getScore_multipication() {
        return score_multipication;
    }

    public void setScore_multipication(int score_multipication) {
        this.score_multipication = score_multipication;
    }

    public int getScore_division() {
        return score_division;
    }

    public void setScore_division(int score_division) {
        this.score_division = score_division;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
    public String getStandard() {
        return Standard;
    }

    public void setStandard(String standard) {
        Standard = standard;
    }

}
