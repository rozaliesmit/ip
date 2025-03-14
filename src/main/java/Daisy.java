import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class Daisy {
    private static final String FILE_PATH = "./data/daisy.txt";
    // Store tasks in arrayList
    private static final ArrayList<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        Storage.loadTasks(tasks, FILE_PATH);
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
            if (taskNumber < 1 || taskNumber > tasks.size()) {
                System.out.println("Invalid task number! Please enter a valid task number!");
                return;
            }
            tasks.get(taskNumber - 1).markAsDone();
            Storage.saveTasks(tasks, FILE_PATH);
            System.out.println("Marked task as done: " + tasks.get(taskNumber - 1));
        } catch (Exception e) {
            System.out.println("Invalid format! Use: mark <task_number>");
        }
    }

    private static void invalidUnmarkTask(String input) {
        try {
            int taskNumber = Integer.parseInt(input.split(" ")[1]);
            if (taskNumber < 1 || taskNumber > tasks.size()) {
                System.out.println("Invalid task number! Please enter a valid task number!");
                return;
            }
            tasks.get(taskNumber - 1).markAsNotDone();
            Storage.saveTasks(tasks, FILE_PATH);
            System.out.println("Unmarked task as done: " + tasks.get(taskNumber - 1));
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
        String[] parts = input.substring(6).split(" /from | /to ", 3);
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
        Storage.saveTasks(tasks, FILE_PATH);
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
        if (taskNumber > 1 || taskNumber > tasks.size()) {
            System.out.println("Invalid task number! Please enter a valid task number!");
            return;
        }
            Task removedTask = tasks.remove(taskNumber - 1);
            Storage.saveTasks(tasks, FILE_PATH);
            System.out.println("____________________________________________________________");
            System.out.println("Removed: " + removedTask);
            System.out.println("Now you have " + tasks.size() + " tasks left.");
            System.out.println("____________________________________________________________");
        }
}

class Storage {
    public static void saveTasks(ArrayList<Task> tasks, String filePath) {
        try {
            File file = new File(filePath);
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            PrintWriter writer = new PrintWriter(file);

            for (Task task : tasks) {
                writer.println(task.toFileString());
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Something went wrong while saving tasks! " + e.getMessage());
        }
    }

    public static void loadTasks(ArrayList<Task> tasks, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return; // No file to load
        } try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Task task = Task.fromFileString(line);
                if (task != null) {
                    tasks.add(task);
                }
            } scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Something went wrong while loading tasks! " + e.getMessage());
        }
    }
}

// Base Task class
abstract class Task {
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

    public abstract String toFileString();

    public static Task fromFileString(String fileString) {
        String[] parts = fileString.split(" \\| ");
        if (parts.length < 3) return null;

        boolean isDone = parts[1].equals("1");
        String description = parts[2];
        Task task;

        switch (parts[0]) {
        case "T":
            task = new TodoTask(description);
            break;
        case "D":
            if (parts.length < 4) return null;
            task = new DeadlineTask(description, parts[3]);
            break;
        case "E":
            if (parts.length < 5) return null;
            task = new EventTask(description, parts[3], parts[4]);
            break;
        default:
            return null;
        }
        if (isDone) task.markAsDone();
        return task;
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
    public String toFileString() {
        return "T | " + (isDone ? "1" : "0") + " | " + description;
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
    public String toFileString() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + by;
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
    public String toFileString() {
        return "E | " + (isDone ? "1" : "0") + " | " + description + " | " + from + " | " + to;
    }
}
