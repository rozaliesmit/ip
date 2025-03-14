# Daisy 🌼User Guide

Hello! I'm Daisy 🌼 a flowery chatbot eager to help you bloom into a more organized and productive you! From tracking tasks to keeping you on top of your game, I'm here to make your life a little more fragrant and a lot more colourful. Let's get started!

## Help: shows command formats

Displays all possible commands and their correct format

Format: `help`
Example: `help`
Example output:

```
Available commands:
  list - Show all tasks
  todo [task] - Add a to-do
  deadline [task] /by [date] - Add a deadline
  event [task] /from [start] /to [end] - Add an event
  mark [index] - Mark task as done
  unmark [index] - Unmark task
  delete [index] - Delete task
  find [keyword] - Find task
  bye - Exit program
```

## List: shows all tasks

Displays all tasks currently in the list

Format: `help`
Example: `list`
Example output:

```
1. [D] [ ] ip (by: midnight)
2. [E] [ ] birthday (from: monday to: tuesday)
3. [T] [ ] sleep
4. [E] [X] wedding (from: 6 to: 7)
5. [D] [ ] assignment 1 (by: august 5)
```

## Todo: adding a to-do task

Adds a to-do task to the list

Format: `todo [task]`
Example: `todo laundry`
Example output:

```
Added: [T] [ ] laundry
```

## Deadline: adding a deadline

Adds a deadline task to the list

Format: `deadline [task] /by [date]`
Example: `deadline project1 /by friday 23:59`

Example output:

```
Added: [D] [ ] project1 (by: friday 23:59)
```

## Event: adding an event

Adds an event task to the list

Format: `event [task] /from [start] /to [end]`
Example: `event conference /from monday 1PM /to monday 7PM`

Example output:

```
Added: [E] [ ] conference (from: monday 1PM to: monday 7PM)
```

## Mark: marking a task as done

Marks a task as done

Format: `mark [index]`
Example: `mark 5`

Example output:

```
Marked as done: [E] [X] wedding (from: 6 to: 7)
```

## Unmark: unmarking a task as done

Unmarks a task, if a task turns out to be incomplete

Format: `unmark [index]`
Example: `unmark 5`

Example output:

```
Unmarked: [E] [] wedding (from: 6 to: 7)
```

## Delete: delete a task

Deletes a task from the list

Format: `delete [index]`
Example: `delete 5`

Example output:

```
Removed: [E] [ ] wedding (from: 6 to: 7)
```

## find: find a task

Searches for tasks that have a given keyword in their description

Format: `find [keyword]`
Example: `find day`

Example output:

```
Here are the matching tasks in your list:
1. [E] [ ] birthday (from: monday to: tuesday)
2. [E] [ ] holiday (from: today. to: tomorrow)
```

## bye: exit program

Closes the program

Format: `bye`
Example: `bye`

Example output:

```
 Bye! Hope to see you again soon. 🌸
```


