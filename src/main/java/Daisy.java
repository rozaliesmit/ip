import java.util.ArrayList;
import java.util.Scanner;

public class Daisy {
    // Store tasks in arrayList
    private static final ArrayList<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        // Introduction message
        printIntroduction();
        Scanner scanner = new Scanner(System.in);

        // Respond to user commands
        while (true) {
            String input = scanner.nextLine();
            // End conversation
            if (input.equals("bye")) {
                break;
            } else if (input.equals("help")) {
                printHelp();
            } else if (input.equals("list")) {
                listTasks();
            } else if (input.startsWith("mark")) {
                invalidMarkTask(input);
            } else if (input.startsWith("unmark")) {
                invalidUnmarkTask(input);
            } else if (input.startsWith("todo")) {
                invalidTodoTask(input);
            } else if (input.startsWith("deadline")) {
                invalidDeadlineTask(input);
            } else if (input.startsWith("event")) {
                invalidEventTask(input);
            } else if (input.startsWith("delete")) {
                invalidDeleteTask(input);
            } else {
                System.out.println("____________________________________________________________");
                System.out.println("Oh no! Invalid command. Type 'help' for a list of commands.");
                System.out.println("____________________________________________________________");
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

    private static void printHelp() {
        System.out.println("____________________________________________________________");
        System.out.println("Commands:");
        System.out.println(" - list: Show all tasks");
        System.out.println(" - mark <task_number>: Mark a task as done");
        System.out.println(" - unmark <task_number>: Unmark a task");
        System.out.println(" - todo <description>: Add a new todo task");
        System.out.println(" - deadline <description> /by <time>: Add a deadline task");
        System.out.println(" - event <description> /from <start_time> /to <end_time>: Add an event task");
        System.out.println(" - delete <task_number>: Remove a task");
        System.out.println(" - bye: Exit the program");
        System.out.println("____________________________________________________________");
    }

    // List task
    private static void listTasks() {
        System.out.println("____________________________________________________________");
        if (tasks.isEmpty()) {
            System.out.println("Your task list is empty!");
        } else {
            System.out.println("Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + ". " + tasks.get(i));
            }
        }
        System.out.println("____________________________________________________________");
    }

    private static void invalidMarkTask(String input) {
        try {
            int taskNumber = Integer.parseInt(input.split(" ")[1]);
            markTask(taskNumber);
        } catch (Exception e) {
            System.out.println("Invalid format! Use: mark <task_number>");
        }
    }

    private static void invalidUnmarkTask(String input) {
        try {
            int taskNumber = Integer.parseInt(input.split(" ")[1]);
            unmarkTask(taskNumber);
        } catch (Exception e) {
            System.out.println("Invalid format! Use: unmark <task_number>");
        }
    }

    private static void invalidTodoTask(String input) {
        String description = input.substring(5).trim();
        if (description.isEmpty()) {
            System.out.println("Todo description cannot be empty!");
        } else {
            addTask(new TodoTask(description));
        }
    }

    private static void invalidDeadlineTask(String input) {
        String[] parts = input.substring(9).split(" /by ");
        if (parts.length == 2) {
            addTask(new DeadlineTask(parts[0], parts[1]));
        } else {
            System.out.println("Invalid format! Use: deadline <description> /by <time>");
        }
    }

    private static void invalidEventTask(String input) {
        String[] parts = input.substring(6).split(" /from | /to ");
        if (parts.length == 3) {
            addTask(new EventTask(parts[0], parts[1], parts[2]));
        } else {
            System.out.println("Invalid format! Use: event <description> /from <start_time> /to <end_time>");
        }
    }

    private static void invalidDeleteTask(String input) {
        try {
            int taskNumber = Integer.parseInt(input.split(" ")[1]);
            deleteTask(taskNumber);
        } catch (Exception e) {
            System.out.println("Invalid format! Use: delete <task_number>");
        }
    }

    // Add task
    private static void addTask(Task task) {
        tasks.add(task);
        System.out.println("____________________________________________________________");
        System.out.println("Added: " + task);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }

    // Mark task as done
    private static void markTask(int taskNumber) {
        if (taskNumber > 0 && taskNumber <= tasks.size()) {
            tasks.get(taskNumber - 1).markAsDone();
            System.out.println("____________________________________________________________");
            System.out.println("Marked as done: " + tasks.get(taskNumber - 1));
            System.out.println("____________________________________________________________");
        } else {
            System.out.println("Invalid task number!");
        }
    }

    // Unmark task
    private static void unmarkTask(int taskNumber) {
        if (taskNumber > 0 && taskNumber <= tasks.size()) {
            tasks.get(taskNumber - 1).markAsNotDone();
            System.out.println("____________________________________________________________");
            System.out.println("Unmarked: " + tasks.get(taskNumber - 1));
            System.out.println("____________________________________________________________");
        } else {
            System.out.println("Invalid task number!");
        }
    }

    // Delete task
    private static void deleteTask(int taskNumber) {
        if (taskNumber > 0 && taskNumber <= tasks.size()) {
            Task removedTask = tasks.remove(taskNumber - 1);
            System.out.println("____________________________________________________________");
            System.out.println("Removed: " + removedTask);
            System.out.println("Now you have " + tasks.size() + " tasks left.");
            System.out.println("____________________________________________________________");
        } else {
            System.out.println("Invalid task number!");
        }
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

    public void markAsDone() {
        isDone = true;
    }

    public void markAsNotDone() {
        isDone = false;
    }

    @Override
    public String toString() {
        return "[" + (isDone ? "X" : " ") + "] " + description;
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
    private final String by;

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
    private final String from, to;

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
