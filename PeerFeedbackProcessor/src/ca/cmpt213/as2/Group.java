package ca.cmpt213.as2;

import java.util.ArrayList;

public class Group {
    public ArrayList<GroupFeedback> sources;

    public Group(ArrayList<GroupFeedback> sources) {
        this.sources = sources;
    }

    public void add(GroupFeedback student) {
        sources.add(student);
    }
}
