package ca.cmpt213.as2;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PeerFeedbackProcessor {
    public static void main(String[] args) {
        final int FAILURE = -1;
//        if (args.length != 2) {
//            System.exit(FAILURE);
//        }

        String jsonDir = args[0];
//        String csvDir = args[1];
        File dir = new File(jsonDir);
        if (!dir.exists()) {
            System.exit(FAILURE);
        }

        List<File> jsonFiles = new ArrayList<>();
        getJsonFiles(dir, jsonFiles);
        if (jsonFiles.isEmpty()) {
            System.exit(FAILURE);
        }
        try {
//            List<ArrayList<GroupFeedback>> groups = new ArrayList<>();
            List<GroupFeedback> groupOfFeedbacks = new ArrayList<>(); // Basically the json data
            for (File f : jsonFiles) {
                JsonElement fileElement = JsonParser.parseReader(new FileReader(f.getPath()));
                JsonObject fileObject = fileElement.getAsJsonObject();

                GroupFeedback feedback = new GroupFeedback(fileObject.get("confidential_comments").getAsString());

                JsonArray jsonArrayOfGroupMembers = fileObject.get("group").getAsJsonArray();
                for (JsonElement memberElement : jsonArrayOfGroupMembers) {
                    JsonObject memberObject = memberElement.getAsJsonObject();
                    JsonObject contribution = memberObject.get("contribution").getAsJsonObject();

                    String name = memberObject.get("name").getAsString();
                    String sfuEmail = memberObject.get("sfu_email").getAsString();
                    Double score = contribution.get("score").getAsDouble();
                    String comment = contribution.get("comment").getAsString();

                    StudentFeedback studentFeedback = new StudentFeedback(name, sfuEmail, score, comment);
                    feedback.add(studentFeedback);
                }
                groupOfFeedbacks.add(feedback);
            }
            List<GroupFeedback> dupe = new ArrayList<>();
            for (GroupFeedback g : groupOfFeedbacks) {
                dupe.add(g);
            }
            // creating a group
            GroupFeedback targetStudent;
            List<GroupFeedback> sourceStudent = new ArrayList<>();
//            List<Group> groups = new ArrayList<>();
            int numGroupsMade = 0;
            System.out.println("Group#,Source Student,Target Student,Score,Comment,,Private");
            for (GroupFeedback target : dupe) {
                targetStudent = target;
//                for (GroupFeedback source : duplicateGOF) {
                for (Iterator<GroupFeedback> iterateForSources = groupOfFeedbacks.iterator();
                     iterateForSources.hasNext();) {
                    GroupFeedback source = iterateForSources.next();
                    for (StudentFeedback memberInTarget : targetStudent) {
                        String targetEmail = memberInTarget.sfuEmail.trim();
                        String sourceEmail = source.getStudentFeedback(0).sfuEmail.trim();
                        if (source != target && sourceEmail.equalsIgnoreCase(targetEmail)) {
                            sourceStudent.add(source);
//                            groupOfFeedbacks.remove(source);
                            iterateForSources.remove();
                        }
                    }
                }
                if (groupOfFeedbacks.isEmpty()) {
                    break;
                }
                Group newGroup = new Group(sourceStudent);
                newGroup.add(targetStudent);
                numGroupsMade++;
                System.out.println("Group " + numGroupsMade);
                newGroup.sortMemberFeedbackAndOutput();
                System.out.print("\n");
            }

        } catch (FileNotFoundException e) {
            System.exit(FAILURE);
        }
    }

    private static void getJsonFiles(File dir, List<File> jsonFiles) {
        if (dir.isFile() || dir.list() == null) {
            return;
        }
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".json") || pathname.isDirectory();
            }
        };
        File[] files = dir.listFiles(filter);
        assert files != null;
        for (File c : files) {
            if (c.isFile()) {
                jsonFiles.add(c);
            }
            getJsonFiles(c, jsonFiles);
        }
    }
} //java -jar out\artifacts\ProcessPeerFeedback_jar\ProcessPeerFeedback.jar InputTestDataSets\2-OneGroup
//java -jar out/artifacts/PeerFeedbackProcessor_jar/PeerFeedbackProcessor.jar InputTestDataSets/2-OneGroup/
