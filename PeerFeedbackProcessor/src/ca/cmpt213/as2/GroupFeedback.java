package ca.cmpt213.as2;

import java.util.ArrayList;
import java.util.List;

public class GroupFeedback {
    public List<Member> members = new ArrayList<>();
    public String confidentialComment;

    public GroupFeedback(String confidentialComment) {
        this.confidentialComment = confidentialComment;
    }

    public void add(Member member) {
        this.members.add(member);
    }

    public Member getGroup(int index) {
        return members.get(index);
    }
}
