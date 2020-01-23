package ca.cmpt213.as2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Group {
    public List<GroupFeedback> groupMembers;
    public GroupFeedback target;
    public List<StudentFeedback> sources = new ArrayList<>();

    public Group(List<GroupFeedback> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public void add(GroupFeedback student) {
        groupMembers.add(student);
    }

    public void sortMemberFeedbackAndOutput() {
        Collections.sort(groupMembers, new Comparator<GroupFeedback>() {
            @Override
            public int compare(GroupFeedback target1, GroupFeedback target2) {
                return target1.getStudentFeedback(0).sfuEmail.trim().compareToIgnoreCase(
                        target2.getStudentFeedback(0).sfuEmail.trim());
            }
        });
        for (GroupFeedback theTarget : groupMembers){
            target = theTarget;
            for (GroupFeedback theSource : groupMembers) {
                String sourceEmail = theSource.getStudentFeedback(0).sfuEmail.trim();
                String targetEmail = target.getStudentFeedback(0).sfuEmail.trim();
                if (!sourceEmail.equalsIgnoreCase(targetEmail)) {
                    for (StudentFeedback sourceFeedback : theSource) {
                        if (sourceFeedback.sfuEmail.trim().equalsIgnoreCase(targetEmail)) {
                            StudentFeedback source = new StudentFeedback("",
                                    theSource.getStudentFeedback(0).sfuEmail,
                                    sourceFeedback.score, sourceFeedback.comment);
                            sources.add(source);
                        }
                    }
                }
            }
            Collections.sort(sources, new Comparator<StudentFeedback>() {
                @Override
                public int compare(StudentFeedback source1, StudentFeedback source2) {
                    return source1.sfuEmail.trim().compareToIgnoreCase(source2.sfuEmail.trim());
                }
            });
            GroupFeedback feedbackAboutTarget = new GroupFeedback(target.confidentialComments);
//            feedbackAboutTarget.add(target.getStudentFeedback(0));
            for (StudentFeedback targetFeedback : sources) {
                feedbackAboutTarget.add(targetFeedback);
            }
            output(target.getStudentFeedback(0), feedbackAboutTarget);
            sources.clear();
        }
    }

    private void output(StudentFeedback target, GroupFeedback feedbackAboutTarget) {
        int numberOfFeedbacks = 0;
        Double partialAverage = 0.0;
        for (StudentFeedback feedback : feedbackAboutTarget) {
            String enhancedCommentAboutTarget = feedback.comment.replaceAll("\"", "'");
            System.out.printf(",%s,%s,%.1f,\"%s\",,\n", feedback.sfuEmail.trim(),
                    target.sfuEmail.trim(), feedback.score, enhancedCommentAboutTarget);
            numberOfFeedbacks++;
            partialAverage += feedback.score;
        }
        Double targetAvgScore = partialAverage / numberOfFeedbacks;
        String enhancedCommentToSelf = target.comment.replaceAll("\"", "'");
        String enhancedPrivateCommentToSelf = feedbackAboutTarget.confidentialComments.replaceAll(
                "\"", "'");
        System.out.printf(",-->,%s,%.1f,\"%s\",,\n", target.sfuEmail.trim(), target.score, enhancedCommentToSelf);
        System.out.printf(",-->,%s,avg %.1f /%d,,\"%s\",\n", target.sfuEmail.trim(),
                targetAvgScore, numberOfFeedbacks, enhancedPrivateCommentToSelf);
    }
}
