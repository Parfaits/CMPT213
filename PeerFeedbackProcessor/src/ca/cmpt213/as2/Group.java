package ca.cmpt213.as2;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Group {
    private final int FAILURE = -1;
    private List<GroupFeedback> groupMembers;
    private List<StudentFeedback> sources = new ArrayList<>();

    public Group(List<GroupFeedback> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public void sortMemberFeedbackAndOutput(PrintWriter printer) {
        checkOnlyOneSubmission();
        checkEmailTyposAndMissingMembers();
        checkSumOfScores();
        Collections.sort(groupMembers, new Comparator<GroupFeedback>() {
            @Override
            public int compare(GroupFeedback target1, GroupFeedback target2) {
                return target1.getStudentFeedback(0).sfuEmail.trim().compareToIgnoreCase(
                        target2.getStudentFeedback(0).sfuEmail.trim());
            }
        });
        for (GroupFeedback theTarget : groupMembers){
            for (GroupFeedback theSource : groupMembers) {
                String sourceEmail = theSource.getStudentFeedback(0).sfuEmail.trim();
                String targetEmail = theTarget.getStudentFeedback(0).sfuEmail.trim();
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
            GroupFeedback feedbackAboutTarget = new GroupFeedback(theTarget.confidentialComments);
            for (StudentFeedback targetFeedback : sources) {
                feedbackAboutTarget.add(targetFeedback);
            }
            output(theTarget.getStudentFeedback(0), feedbackAboutTarget, printer);
            sources.clear();
        }
    }

    private void checkEmailTyposAndMissingMembers() {
        List<GroupFeedback> listToCheck = new ArrayList<>(groupMembers);
        boolean foundMatch = false;
        for (GroupFeedback g : groupMembers) {
            for (GroupFeedback l : listToCheck) {
                if (l.equals(g)) {
                    continue;
                }
                for (StudentFeedback s : l) {
                    String emailToCheck = g.getStudentFeedback(0).sfuEmail.trim();
                    String emailToCheckWith = s.sfuEmail.trim();
                    if (emailToCheck.equalsIgnoreCase(emailToCheckWith)) {
                        foundMatch = true;
                    }
                }
                if (foundMatch) {
                    foundMatch = false;
                } else {
                    System.err.println("Error: Found typo or missing member in file containing: \n\n" + l);
                    System.exit(FAILURE);
                }
            }
        }
    }

    private void checkOnlyOneSubmission() {
        if (groupMembers.size() == 1 && groupMembers.get(0).feedbackSize() != 1) {
            System.err.println("Error: Only one submission found for this group:\n\n" + groupMembers.get(0).toString());
            System.exit(FAILURE);
        }
    }

    private void checkSumOfScores() {
        Double sum = 0.0;
        int numberOfFeedbacks = 0;
        for (GroupFeedback g: groupMembers) {
            for (StudentFeedback s : g) {
                sum += s.score;
                numberOfFeedbacks++;
            }
            if (!(Math.abs(sum - (20 * numberOfFeedbacks)) < 0.1)) {
                System.err.println("Error: Sum of scores in the group is not " +
                        "(20 * number of group members), with a tolerance of 0.1" +
                        " In file containing:\n\n" + g.toString());
                System.exit(FAILURE);
            }
            sum = 0.0;
            numberOfFeedbacks = 0;
        }
    }

    private void output(StudentFeedback target, GroupFeedback feedbackAboutTarget, PrintWriter printer) {
        int numberOfFeedbacks = 0;
        Double sum = 0.0;
        for (StudentFeedback feedback : feedbackAboutTarget) {
            String enhancedCommentAboutTarget = feedback.comment.replaceAll("\"", "'");
            printer.printf(",%s,%s,%.1f,\"%s\",,\n", feedback.sfuEmail.trim(),
                    target.sfuEmail.trim(), feedback.score, enhancedCommentAboutTarget);
            numberOfFeedbacks++;
            sum += feedback.score;
        }
        Double targetAvgScore = sum / numberOfFeedbacks;
        String enhancedCommentToSelf = target.comment.replaceAll("\"", "'");
        String enhancedPrivateCommentToSelf = feedbackAboutTarget.confidentialComments.replaceAll(
                "\"", "'");
        printer.printf(",-->,%s,%.1f,\"%s\"\n", target.sfuEmail.trim(), target.score, enhancedCommentToSelf);
        printer.printf(",-->,%s,avg %.1f /%d,,,\"%s\"\n", target.sfuEmail.trim(),
                targetAvgScore, numberOfFeedbacks, enhancedPrivateCommentToSelf);
    }
}
