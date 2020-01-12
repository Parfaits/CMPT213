/*
 * Johnny Doan
 * */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        List<Minion> minions = new ArrayList<>();
        TextMenuUI ui = new TextMenuUI("Main Menu");
        Scanner input = new Scanner(System.in);
        boolean isDone = false;

        while (!isDone) {
            ui.printMenu();
            int choice = handleUserInput(input, 1, 6);
            int numMinions = minions.size();

            switch (choice) {
                case 1:
                    listMinions(minions, numMinions);
                    break;
                case 2:
                    addMinion(input, minions);
                    break;
                case 3:
                    listMinions(minions, numMinions);
                    System.out.println("(Enter 0 to cancel)");
                    int index = handleUserInput(input,0, numMinions);
                    if (index == 0) {
                        break;
                    } else {
                        minions.remove(index-1);
                    }
                    break;
                case 4:
                    listMinions(minions, numMinions);
                    System.out.println("(Enter 0 to cancel)");
                    int value = handleUserInput(input, 0, numMinions);
                    if (value == 0) {
                        break;
                    } else {
                        attributeED(minions, value-1);
                    }
                    break;
                case 5:
                    System.out.println("All minion objects:");
                    for (int i = 0; i < numMinions; i++) {
                        System.out.println((i+1) + ". " + minions.get(i).toString());
                    }
                    break;
                case 6:
                    isDone = true;
                    break;
                default:
                    handleUserInput(input,1, 6);
            }
        }
    }

    public static int handleUserInput(Scanner scan, int lower, int upper) {
        System.out.print("> ");
        int choice = scan.nextInt();
        while (lower > choice || choice > upper) {
            System.out.println("Error: Please enter a selection between " + lower +
                                " and " + upper);
            System.out.print("> ");
            choice = scan.nextInt();
        }
        scan.nextLine(); // Consume '\n' because nextInt() did not consume '\n'
                         // So we can use nextLine() without problem
        return choice;
    }

    public static void attributeED(List<Minion> minions, int index) {
        int nofED = minions.get(index).getNumberOfEvilDeeds();
        minions.get(index).setNumberOfEvilDeeds(nofED + 1);
        System.out.println(minions.get(index).getName() + " has now done " +
                           minions.get(index).getNumberOfEvilDeeds() + " evil deed(s)");
    }

    public static void addMinion(Scanner scan, List<Minion> minions) {
        System.out.print("Enter minion's name: ");
        String name = scan.nextLine();
        System.out.print("Enter minion's height: ");
        double height = scan.nextDouble();
        while (height < 0) {
            System.out.println("ERROR: Height must be >= 0.");
            System.out.print("Enter minion's height: ");
            height = scan.nextDouble();
        }
        Minion newMinion = new Minion(name, height);
        minions.add(newMinion);
    }

    public static void listMinions(List<Minion> minions, int numMinions) {
        System.out.println("List of Minions:");
        if (numMinions == 0) {
            System.out.println("No minions found.");
            return;
        }
        for (int i = 0; i < numMinions; i++) {
            System.out.println((i+1) + ". " + minions.get(i).getName() + ", " +
                                minions.get(i).getHeight() + "m, " +
                                minions.get(i).getNumberOfEvilDeeds() + " evil deed(s)");
        }
    }
}
