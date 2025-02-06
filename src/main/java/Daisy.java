import java.util.Scanner;

public class Daisy {
    private static final int MAX_ITEMS = 100;
    private static String[] items = new String[MAX_ITEMS];
    private static int itemCount = 0;

    public static void main(String[] args) {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";

        System.out.println("Hello from\n" + logo);
        System.out.println("____________________________________________________________");
        System.out.println(" Hello! I'm Daisy");
        System.out.println(" What can I do for you?");
        System.out.println("____________________________________________________________");

        Scanner scanner = new Scanner(System.in);
        String input = "";
        while (true){
            input = scanner.nextLine();
            if (input.equals("bye")){
                break;
            } else if (input.equals("list")){
                listItems();
            } else {
                addItem(input);
            }
            System.out.println(input);
        }

        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println("____________________________________________________________");
    }

    private static void listItems() {
        System.out.println("____________________________________________________________");
        for (int i = 0; i < itemCount; i++) {
            System.out.println((i + 1) + ". " + items[i]);
        }
    }

    private static void addItem(String item) {
        if (itemCount < MAX_ITEMS) {
            items[itemCount] = item;
            itemCount++;
            System.out.println("____________________________________________________________");
            System.out.println(" added: " + item);
            System.out.println("____________________________________________________________");
        }
        else {
            System.out.println("____________________________________________________________");
            System.out.println("The list is full. Cannot add any more items.");
            System.out.println("____________________________________________________________");
        }
    }
}

