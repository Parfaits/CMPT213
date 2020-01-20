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
            for (File f : jsonFiles) {
                JsonElement fileElement = JsonParser.parseReader(new FileReader(f.getPath()));
                JsonObject fileObject = fileElement.getAsJsonObject();

                JsonArray jsonArrayOfGroupMembers = fileObject.get("group").getAsJsonArray();
                // A group is made up of feedback that matches each member's name
                List<ArrayList<GroupFeedback>> group = new ArrayList<>();


                System.out.println(f.getName());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
