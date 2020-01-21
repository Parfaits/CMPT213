package ca.cmpt213.as2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GroupFeedback implements Iterable<StudentFeedback>{
    private List<StudentFeedback> feedbacks = new ArrayList<>();
    public String confidentialComments;

    public GroupFeedback(String confidentialComments) {
        this.confidentialComments = confidentialComments;
    }

    public void add(StudentFeedback student) {
        feedbacks.add(student);
    }

    public StudentFeedback getStudentFeedback(int i) {
        return feedbacks.get(i);
    }

    @Override
    public Iterator<StudentFeedback> iterator() {
        return feedbacks.iterator();
    }
}