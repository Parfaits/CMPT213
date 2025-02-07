package ca.cmpt213.as2;

public class StudentFeedback {
    public String name;
    public String sfuEmail;
    public Double score;
    public String comment;

    public StudentFeedback(String name, String sfuEmail, Double score, String comment) {
        this.name = name;
        this.sfuEmail = sfuEmail;
        this.score = score;
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "StudentFeedback{" +
                "name='" + name + '\'' +
                ", sfuEmail='" + sfuEmail + '\'' +
                ", score=" + score +
                ", comment='" + comment + '\'' +
                '}';
    }
}
