import java.util.Scanner;

public class Daisy {
    // Maximum number of tasks
    private static final int MAX_TASKS = 100;
    private static Task[] tasks = new Task[MAX_TASKS];
    private static int taskCount = 0;

    public static void main(String[] args) {
        // Introduction message
        printIntroduction();

        Scanner scanner = new Scanner(System.in);
        String input;

        // Respond to user commands
        while (true) {
            input = scanner.nextLine();
            // End conversation
            if (input.equals("bye")) {
                break;
            } else if (input.equals("list")) {
                listTasks();
            } else if (input.startsWith("mark ")) {
                invalidMarkTask(input);
            } else if (input.startsWith("unmark ")) {
                invalidUnmarkTask(input);
            } else if (input.startsWith("todo ")) {
                invalidTodoTask(input);
            } else if (input.startsWith("deadline ")) {
                invalidDeadlineTask(input);
            } else if (input.startsWith("event ")) {
                invalidEventTask(input);
            } else {
                addTask(new Task(input));
            }
        }

        // Goodbye message
        printGoodbye();

        scanner.close();
    }

    private static void printIntroduction() {
        System.out.println("____________________________________________________________");
        System.out.println(" Hello! I'm Daisy");
        System.out.println(" What can I do for you?");
        System.out.println("____________________________________________________________");
    }

    private static void printGoodbye() {
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println("____________________________________________________________");
    }

    private static void invalidMarkTask(String input) {
        try {
            int taskNumber = Integer.parseInt(input.split(" ")[1]);
            markTask(taskNumber);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Oh no! Seems like the command format was invalid. Please use the format 'mark <task_number>' instead.");
        }
    }

    private static void invalidUnmarkTask(String input) {
        try {
            int taskNumber = Integer.parseInt(input.split(" ")[1]);
            unmarkTask(taskNumber);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Oh no! Seems like the command format was invalid. Please use the format 'unmark <task_number>' instead.");
        }
    }

    private static void invalidTodoTask(String input) {
        addTask(new TodoTask(input.substring(5)));
    }

    private static void invalidDeadlineTask(String input) {
        String[] parts = input.substring(9).split(" /by ");
        if (parts.length == 2) {
            addTask(new DeadlineTask(parts[0], parts[1]));
        } else {
            System.out.println("Oh no! Seems like the command format was invalid. Please use the format 'deadline <description> /by <time>' instead.");
        }
    }

    private static void invalidEventTask(String input) {
        String[] parts = input.substring(6).split(" /from | /to ");
        if (parts.length == 3) {
            addTask(new EventTask(parts[0], parts[1], parts[2]));
        } else {
            System.out.println("Oh no! Seems like the command format was invalid. Please use the format 'event <description> /from <start_time> /to <end_time>' instead.");
        }
    }

    // Add task
    private static void addTask(Task task) {
        if (taskCount < MAX_TASKS) {
            tasks[taskCount] = task;
            taskCount++;
            printTaskAdded(task);
        } else {
            System.out.println("____________________________________________________________");
            System.out.println("Oh no! The list is full, I am unable to add more tasks :(");
            System.out.println("____________________________________________________________");
        }
    }

    private static void printTaskAdded(Task task) {
        System.out.println("____________________________________________________________");
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }

    // List tasks
    private static void listTasks() {
        System.out.println("____________________________________________________________");
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + "." + tasks[i]);
        }
        System.out.println("____________________________________________________________");
    }

    // Mark task as done
    private static void markTask(int taskNumber) {
        if (taskNumber > 0 && taskNumber <= taskCount) {
            tasks[taskNumber - 1].markAsDone();
            printTaskMarkedDone(tasks[taskNumber - 1]);
        } else {
            System.out.println("____________________________________________________________");
            System.out.println(" Invalid task number.");
            System.out.println("____________________________________________________________");
        }
    }

    private static void printTaskMarkedDone(Task task) {
        System.out.println("____________________________________________________________");
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task);
        System.out.println("____________________________________________________________");
    }

    // Unmark task
    private static void unmarkTask(int taskNumber) {
        if (taskNumber > 0 && taskNumber <= taskCount) {
            tasks[taskNumber - 1].markAsNotDone();
            printTaskUnmarked(tasks[taskNumber - 1]);
        } else {
            System.out.println("____________________________________________________________");
            System.out.println(" Invalid task number.");
            System.out.println("____________________________________________________________");
        }
    }

    private static void printTaskUnmarked(Task task) {
        System.out.println("____________________________________________________________");
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
        System.out.println("____________________________________________________________");
    }
}

// Base Task class
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

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}

// Todo task
class TodoTask extends Task {
    public TodoTask(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}

// Deadline task
class DeadlineTask extends Task {
    protected String by;

    public DeadlineTask(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}

// Event task
class EventTask extends Task {
    protected String from;
    protected String to;

    public EventTask(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
