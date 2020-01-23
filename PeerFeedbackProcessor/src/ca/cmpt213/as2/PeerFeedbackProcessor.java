package ca.cmpt213.as2;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PeerFeedbackProcessor {
    public static void main(String[] args) {
        final int FAILURE = -1;
        if (args.length != 2) {
            System.exit(FAILURE);
        }

        String jsonDir = args[0];
        String csvDir = args[1];
        File jsonPath = new File(jsonDir);
        File csvPath = new File(csvDir);
        if (!jsonPath.exists() || !csvPath.exists()) {
            System.err.println("Error: Directory not found!");
            System.exit(FAILURE);
        }

        List<File> jsonFiles = new ArrayList<>();
        getJsonFiles(jsonPath, jsonFiles);
        if (jsonFiles.isEmpty()) {
            System.exit(FAILURE);
        }
        try {
            FileWriter outputFile = new FileWriter(csvDir + "group_feedback.csv");
            PrintWriter printer = new PrintWriter(outputFile);
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
                    if (score < 0) {
                        System.exit(FAILURE);
                    }
                    String comment = contribution.get("comment").getAsString();

                    StudentFeedback studentFeedback = new StudentFeedback(name, sfuEmail, score, comment);
                    feedback.add(studentFeedback);
                }
                groupOfFeedbacks.add(feedback);
            }
            List<GroupFeedback> dupe = new ArrayList<>(groupOfFeedbacks);
            // creating a group
            List<GroupFeedback> groupedStudents = new ArrayList<>();
            int numGroupsMade = 0;
            printer.println("Group#,Source Student,Target Student,Score,Comment,,Private");
            for (GroupFeedback target : groupOfFeedbacks) {
                String targetEmail = target.getStudentFeedback(0).sfuEmail.trim();
                for (Iterator<GroupFeedback> iterateForSources = dupe.iterator();
                     iterateForSources.hasNext();) {
                    GroupFeedback source = iterateForSources.next();
                    for (StudentFeedback sourceFeedback : source) {
                        String sourceEmail = sourceFeedback.sfuEmail.trim();
                        if (sourceEmail.equalsIgnoreCase(targetEmail)) {
                            groupedStudents.add(source);
                            iterateForSources.remove();
                        }
                    }
                }
                if (groupedStudents.isEmpty()) {
                    continue;
                }
                Group newGroup = new Group(groupedStudents);
                numGroupsMade++;
                printer.println("Group " + numGroupsMade);
                newGroup.sortMemberFeedbackAndOutput(printer);
                printer.print("\n");
                groupedStudents.clear();
                if (dupe.isEmpty()) {
                    break;
                }
            }
            printer.close();
            outputFile.close();
        } catch (FileNotFoundException e) {
            System.exit(FAILURE);
        } catch (IOException io) {
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
