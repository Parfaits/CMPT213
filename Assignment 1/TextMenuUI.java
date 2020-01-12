/*
 * Johnny Doan
 * */

import java.util.ArrayList;
import java.util.List;

public class TextMenuUI {
    private String title;
    private List<String> menuOptions = new ArrayList<>();

    public TextMenuUI(String title) {
        this.title = title;
        this.menuOptions.add("List minions");
        this.menuOptions.add("Add a new minion");
        this.menuOptions.add("Remove a minion");
        this.menuOptions.add("Attribute evil deed to a minion");
        this.menuOptions.add("DEBUG: Dump objects (toString)");
        this.menuOptions.add("Exit");
    }

    public void printMenu() {
        int len = this.title.length();
        int arrSize = menuOptions.size();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < len+4; j++) {
                if (i == 1 && j == 1) {
                    System.out.printf(" %s ", this.title);
                    j += len + 1;
                } else {
                    System.out.print("*");
                }
            }
            System.out.print("\n");
        }
        System.out.print("\n");
        for (int i = 0; i < arrSize; i++) {
            System.out.println((i+1) + ". " + menuOptions.get(i));
        }
    }
}
