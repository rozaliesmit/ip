import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

// Main Daisy class
public class Daisy {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Constructor for Daisy
     * Initializes the user interface (Ui), storage (Storage) and loads saved tasks
     *
     * @param filePath the file path to load and save tasks.
     */
    public Daisy(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (Exception e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /**
     * Displays the welcome message and handles user input until the user chooses to exit ("bye")
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine();
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (Exception e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }

    public static void main(String[] args) {
        new Daisy("data/daisy.txt").run();
    }
}

// Ui for dealing with user input and output
class Ui {
    private final Scanner scanner;

    /**
     * Initializes the scanner for reading user input
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Displays the welcome message
     */
    public void showWelcome() {
        System.out.println("____________________________________________________________");
        System.out.println(" Hello! I'm Daisy 🌼");
        System.out.println(" What can I do for you?");
        System.out.println(" Type 'help' to see available commands.");
        System.out.println("____________________________________________________________");
    }

    /**
     * Displays the goodbye message
     */
    public void showGoodbye() {
        System.out.println(" Bye! Hope to see you again soon. 🌸");
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    /**
     * Displays an error message when loading tasks doesn't work
     */
    public void showLoadingError() {
        System.out.println("Error loading saved tasks.");
    }

    /**
     * Displays an error message
     *
     * @param message the error message to display
     */
    public void showError(String message) {
        System.out.println("Error: " + message);
    }
}

class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the storage file
     *
     * @return list of tasks from the storage file
     * @throws IOException if an I/O error occurs
     */
    public ArrayList<Task> load() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return tasks;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                tasks.add(Task.fromFileString(scanner.nextLine()));
            }
        }
        return tasks;
    }

    /**
     * Saves the tasks to the storage file
     *
     * @param tasks the list of tasks to save
     * @throws IOException if an I/O error occurs
     */
    public void save(ArrayList<Task> tasks) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Task task : tasks) {
                writer.write(task.toFileString() + "\n");
            }
        }
    }
}

// Manages the task list, including adding, deleting, marking, and listing tasks
class TaskList {
    private ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the task list
     *
     * @param task the task to add
     */
    public void addTask(Task task) {
        tasks.add(task);
        System.out.println("Added: " + task);
    }

    /**
     * Finds and displays tasks that match the provided keyword
     *
     * @param keyword the keyword to search for in tasks
     */
    public void findTask(String keyword) {
        System.out.println("Here are the matching tasks in your list:");
        int count = 0;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).description.contains(keyword)) {
                System.out.println((count + 1) + ". " + tasks.get(i));
                count++;
            }
        }
        if (count == 0) {
            System.out.println("No matching tasks found.");
        }
    }

    public void deleteTask(int index) {
        if (index < 1 || index > tasks.size()) {
            System.out.println("Invalid task number!");
            return;
        }
        Task removed = tasks.remove(index - 1);
        System.out.println("Removed: " + removed);
    }

    public void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("Your task list is empty!");
            return;
        }
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    public void markTask(int index) {
        tasks.get(index - 1).markAsDone();
        System.out.println("Marked as done: " + tasks.get(index - 1));
    }

    public void unmarkTask(int index) {
        tasks.get(index - 1).markAsNotDone();
        System.out.println("Unmarked: " + tasks.get(index - 1));
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }
}

// Parses ui and returns command
class Parser {
    public static Command parse(String input) {
        String[] parts = input.split(" ", 2);
        String command = parts[0];
        String args = parts.length > 1 ? parts[1] : "";

        switch (command) {
        case "list":
            return new ListCommand();
        case "mark":
            return new MarkCommand(Integer.parseInt(args));
        case "unmark":
            return new UnmarkCommand(Integer.parseInt(args));
        case "todo":
            return new AddCommand(new TodoTask(args));
        case "deadline":
            String[] deadlineParts = args.split(" /by ");
            return new AddCommand(new DeadlineTask(deadlineParts[0], deadlineParts[1]));
        case "event":
            String[] eventParts = args.split(" /from | /to ");
            return new AddCommand(new EventTask(eventParts[0], eventParts[1], eventParts[2]));
        case "delete":
            return new DeleteCommand(Integer.parseInt(args));
        case "bye":
            return new ExitCommand();
        case "help":
            return new HelpCommand();
        case "find":
            return new FindCommand(args);
        default:
            throw new IllegalArgumentException("Invalid command! Type 'help' to see available commands.");
        }
    }
}

abstract class Command {
    /**
     * Excecutes the command
     *
     * @param tasks the task list
     * @param ui the user interface
     * @param storage the storage to save the tasks
     * @throws IOException if an error occurs during I/O operations
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws IOException;

    public boolean isExit() {
        return false;
    }
}

class ListCommand extends Command {
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.listTasks();
    }
}

class MarkCommand extends Command {
    private final int index;

    public MarkCommand(int index) {
        this.index = index;
    }

    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.markTask(index);
    }
}

class UnmarkCommand extends Command {
    private final int index;

    public UnmarkCommand(int index) {
        this.index = index;
    }

    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.unmarkTask(index);
    }
}

class AddCommand extends Command {
    private final Task task;

    public AddCommand(Task task) {
        this.task = task;
    }

    public void execute(TaskList tasks, Ui ui, Storage storage) throws IOException {
        tasks.addTask(task);
        storage.save(tasks.getTasks());
    }
}

class DeleteCommand extends Command {
    private final int index;

    public DeleteCommand(int index) {
        this.index = index;
    }

    public void execute(TaskList tasks, Ui ui, Storage storage) throws IOException {
        tasks.deleteTask(index);
        storage.save(tasks.getTasks());
    }
}

class ExitCommand extends Command {
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    public boolean isExit() {
        return true;
    }
}

class HelpCommand extends Command {
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        System.out.println("Available commands:");
        System.out.println("  list - Show all tasks");
        System.out.println("  todo [task] - Add a to-do");
        System.out.println("  deadline [task] /by [date] - Add a deadline");
        System.out.println("  event [task] /from [start] /to [end] - Add an event");
        System.out.println("  mark [index] - Mark task as done");
        System.out.println("  unmark [index] - Unmark task");
        System.out.println("  delete [index] - Delete task");
        System.out.println("  find [index] - Find task");
        System.out.println("  bye - Exit program");
    }
}

class FindCommand extends Command {
    private String keyword;

    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.findTask(keyword);
    }

    @Override
    public boolean isExit() {
        return false;
    }
}

abstract class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns the status of the task
     *
     * @return "[X]" if done, "[ ]" if not done
     */
    public String getStatusIcon() {
        return (isDone ? "[X]" : "[ ]");
    }

    /**
     * Saving the task to a file
     */
    public abstract String toFileString();

    /**
     * Creating a task from a file.
     */
    public static Task fromFileString(String line) {
        String[] parts = line.split(" \\| ");
        switch (parts[0]) {
        case "T":
            TodoTask todo = new TodoTask(parts[2]);
            if (parts[1].equals("1")) todo.markAsDone();
            return todo;
        case "D":
            DeadlineTask deadline = new DeadlineTask(parts[2], parts[3]);
            if (parts[1].equals("1")) deadline.markAsDone();
            return deadline;
        case "E":
            EventTask event = new EventTask(parts[2], parts[3], parts[4]);
            if (parts[1].equals("1")) event.markAsDone();
            return event;
        default:
            throw new IllegalArgumentException("Invalid task type in file.");
        }
    }

    @Override
    public String toString() {
        return getStatusIcon() + " " + description;
    }
}

class TodoTask extends Task {
    public TodoTask(String description) {
        super(description);
    }

    @Override
    public String toFileString() {
        return "T | " + (isDone ? "1" : "0") + " | " + description;
    }

    @Override
    public String toString() {
        return "[T] " + super.toString();
    }
}

class DeadlineTask extends Task {
    private String by;

    public DeadlineTask(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toFileString() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + by;
    }

    @Override
    public String toString() {
        return "[D] " + super.toString() + " (by: " + by + ")";
    }
}

class EventTask extends Task {
    private String from;
    private String to;

    public EventTask(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toFileString() {
        return "E | " + (isDone ? "1" : "0") + " | " + description + " | " + from + " | " + to;
    }

    @Override
    public String toString() {
        return "[E] " + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
