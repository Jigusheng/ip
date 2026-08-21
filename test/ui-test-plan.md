# Console UI test plan

Each test case runs in a fresh Lumi process. Expected response blocks correspond to the input commands in order and are separated by a line containing only `---`. The test runner checks the surrounding divider lines separately.

## TC-01: Mixed task types and status changes

Aim: Verify that to-dos, deadlines, and events are added polymorphically and retain their type-specific display when marked and unmarked.

### Inputs

```text
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
mark 1
list
unmark 1
list
bye
```

### Expected outputs

```text
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
---
 Got it. I've added this task:
   [D][ ] return book (by: Sunday)
 Now you have 2 tasks in the list.
---
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 3 tasks in the list.
---
 Nice! I've marked this task as done:
   [T][X] borrow book
---
 Here are the tasks in your list:
 1.[T][X] borrow book
 2.[D][ ] return book (by: Sunday)
 3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
---
 OK, I've marked this task as not done yet:
   [T][ ] borrow book
---
 Here are the tasks in your list:
 1.[T][ ] borrow book
 2.[D][ ] return book (by: Sunday)
 3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
---
 Bye for now! Keep shining, and I hope to see you again soon!
```

## TC-02: Free-form deadline text

Aim: Verify that deadline date and time details are retained as plain strings without date parsing.

### Inputs

```text
deadline do homework /by no idea :-p
list
bye
```

### Expected outputs

```text
 Got it. I've added this task:
   [D][ ] do homework (by: no idea :-p)
 Now you have 1 tasks in the list.
---
 Here are the tasks in your list:
 1.[D][ ] do homework (by: no idea :-p)
---
 Bye for now! Keep shining, and I hope to see you again soon!
```

## TC-03: Invalid task creation commands preserve state

Aim: Verify specific explanations for malformed or unknown inputs and confirm that rejected task commands do not add tasks.

### Inputs

```text
<EMPTY>
todo
blah
list later
bye now
deadline return book
deadline /by Sunday
deadline return book /by
deadline return book /bypass
event project meeting
event project meeting /from Mon /today
event /from Mon /to Tue
event project meeting /from /to Tue
event project meeting /from Mon /to
todo valid task
list
bye
```

### Expected outputs

```text
 Hmm, please enter a command.
---
 Hmm, a todo needs a description. Try: todo <description>
---
 Hmm, I don't recognize that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.
---
 Hmm, I don't recognize that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.
---
 Hmm, I don't recognize that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.
---
 Hmm, a deadline needs a due date. Try: deadline <description> /by <when>
---
 Hmm, a deadline needs a description before /by.
---
 Hmm, the /by value cannot be empty.
---
 Hmm, a deadline needs a due date. Try: deadline <description> /by <when>
---
 Hmm, an event needs start and end details. Try: event <description> /from <start> /to <end>
---
 Hmm, an event needs start and end details. Try: event <description> /from <start> /to <end>
---
 Hmm, an event needs a description before /from.
---
 Hmm, the /from value cannot be empty.
---
 Hmm, the /to value cannot be empty.
---
 Got it. I've added this task:
   [T][ ] valid task
 Now you have 1 tasks in the list.
---
 Here are the tasks in your list:
 1.[T][ ] valid task
---
 Bye for now! Keep shining, and I hope to see you again soon!
```

## TC-04: Delete tasks and renumber the list

Aim: Verify deleting a task returns the removed task, decreases the count, and shifts later task numbers without changing their details or statuses.

### Inputs

```text
todo read book
deadline return book /by June 6th
event project meeting /from Aug 6th 2pm /to 4pm
mark 1
mark 2
list
delete 3
list
delete 1
list
bye
```

### Expected outputs

```text
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
---
 Got it. I've added this task:
   [D][ ] return book (by: June 6th)
 Now you have 2 tasks in the list.
---
 Got it. I've added this task:
   [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
 Now you have 3 tasks in the list.
---
 Nice! I've marked this task as done:
   [T][X] read book
---
 Nice! I've marked this task as done:
   [D][X] return book (by: June 6th)
---
 Here are the tasks in your list:
 1.[T][X] read book
 2.[D][X] return book (by: June 6th)
 3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
---
 Noted. I've removed this task:
   [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
 Now you have 2 tasks in the list.
---
 Here are the tasks in your list:
 1.[T][X] read book
 2.[D][X] return book (by: June 6th)
---
 Noted. I've removed this task:
   [T][X] read book
 Now you have 1 tasks in the list.
---
 Here are the tasks in your list:
 1.[D][X] return book (by: June 6th)
---
 Bye for now! Keep shining, and I hope to see you again soon!
```

## TC-05: Invalid delete commands preserve the list

Aim: Verify empty-list, missing, non-numeric, and out-of-range delete commands are rejected without removing or modifying tasks.

### Inputs

```text
delete 1
todo keep this task
deadline remove this task /by tomorrow
delete
delete first
delete 0
delete 3
list
delete 2
delete 2
list
delete 1
list
bye
```

### Expected outputs

```text
 Hmm, there are no tasks to delete yet.
---
 Got it. I've added this task:
   [T][ ] keep this task
 Now you have 1 tasks in the list.
---
 Got it. I've added this task:
   [D][ ] remove this task (by: tomorrow)
 Now you have 2 tasks in the list.
---
 Hmm, tell me which task to delete. Try: delete <task number>
---
 Hmm, the task number must be a whole number.
---
 Hmm, choose a task number from 1 to 2.
---
 Hmm, choose a task number from 1 to 2.
---
 Here are the tasks in your list:
 1.[T][ ] keep this task
 2.[D][ ] remove this task (by: tomorrow)
---
 Noted. I've removed this task:
   [D][ ] remove this task (by: tomorrow)
 Now you have 1 tasks in the list.
---
 Hmm, choose a task number from 1 to 1.
---
 Here are the tasks in your list:
 1.[T][ ] keep this task
---
 Noted. I've removed this task:
   [T][ ] keep this task
 Now you have 0 tasks in the list.
---
 Here are the tasks in your list:
---
 Bye for now! Keep shining, and I hope to see you again soon!
```

## TC-06: Invalid mark commands preserve status

Aim: Verify missing, non-numeric, and out-of-range task numbers are handled without changing the selected task's status.

### Inputs

```text
mark 1
todo read book
mark
mark first
mark 2
list
mark 1
unmark 0
unmark
list
unmark 1
list
bye
```

### Expected outputs

```text
 Hmm, there are no tasks to mark yet.
---
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
---
 Hmm, tell me which task to mark. Try: mark <task number>
---
 Hmm, the task number must be a whole number.
---
 Hmm, choose a task number from 1 to 1.
---
 Here are the tasks in your list:
 1.[T][ ] read book
---
 Nice! I've marked this task as done:
   [T][X] read book
---
 Hmm, choose a task number from 1 to 1.
---
 Hmm, tell me which task to unmark. Try: unmark <task number>
---
 Here are the tasks in your list:
 1.[T][X] read book
---
 OK, I've marked this task as not done yet:
   [T][ ] read book
---
 Here are the tasks in your list:
 1.[T][ ] read book
---
 Bye for now! Keep shining, and I hope to see you again soon!
```
