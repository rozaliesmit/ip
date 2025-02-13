import java.util.Scanner;

public class Daisy {
    // Maximum number of tasks
    private static final int MAX_ITEMS = 100;
    private static Task[] tasks = new Task[MAX_ITEMS];
    private static int taskCount = 0;

    public static void main(String[] args) {
        // Introduction message
        System.out.println("Hello from\n");
        System.out.println("____________________________________________________________");
        System.out.println(" Hello! I'm Daisy");
        System.out.println(" What can I do for you?");
        System.out.println("____________________________________________________________");

        Scanner scanner = new Scanner(System.in);
        String input = "";

        // Respond to user commands
        while (true) {
            input = scanner.nextLine();
            // End conversation
            if (input.equals("bye")) {
                break;
              // Display list
            } else if (input.equals("list")) {
                listTasks();
              // Mark task as done
            } else if (input.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(input.split(" ")[1]);
                markTask(taskNumber);
              // Unmark task
            } else if (input.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(input.split(" ")[1]);
                unmarkTask(taskNumber);
              // Add task to list
            } else {
                addTask(input);
            }
        }

        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println("____________________________________________________________");

        scanner.close();
    }

    // Add task
    private static void addTask(String description) {
        if (taskCount < MAX_ITEMS) {
            tasks[taskCount] = new Task(description);
            taskCount++;
            System.out.println("____________________________________________________________");
            System.out.println(" added: " + description);
            System.out.println("____________________________________________________________");
        } else {
            System.out.println("____________________________________________________________");
            System.out.println(" List is full, cannot add more tasks.");
            System.out.println("____________________________________________________________");
        }
    }

    // List task
    private static void listTasks() {
        System.out.println("____________________________________________________________");
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + ".[" + tasks[i].getStatusIcon() + "] " + tasks[i].getDescription());
        }
        System.out.println("____________________________________________________________");
    }

    // Mark task as done
    private static void markTask(int taskNumber) {
        if (taskNumber > 0 && taskNumber <= taskCount) {
            tasks[taskNumber - 1].markAsDone();
            System.out.println("____________________________________________________________");
            System.out.println(" Nice! I've marked this task as done:");
            System.out.println("   [X] " + tasks[taskNumber - 1].getDescription());
            System.out.println("____________________________________________________________");
        } else {
            System.out.println("____________________________________________________________");
            System.out.println(" Invalid task number.");
            System.out.println("____________________________________________________________");
        }
    }

    // Unmark task
    private static void unmarkTask(int taskNumber) {
        if (taskNumber > 0 && taskNumber <= taskCount) {
            tasks[taskNumber - 1].markAsNotDone();
            System.out.println("____________________________________________________________");
            System.out.println(" OK, I've marked this task as not done yet:");
            System.out.println("   [ ] " + tasks[taskNumber - 1].getDescription());
            System.out.println("____________________________________________________________");
        } else {
            System.out.println("____________________________________________________________");
            System.out.println(" Invalid task number.");
            System.out.println("____________________________________________________________");
        }
    }
}

class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    public String getDescription() {
        return description;
    }

    public void markAsDone() {
        isDone = true;
    }

    public void markAsNotDone() {
        isDone = false;
    }
}
