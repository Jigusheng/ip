# Console UI test plan

Each test case runs in a fresh Lumi process and an isolated temporary working folder. Expected response blocks correspond to the input commands in order and are separated by a line containing only `---`. The test runner checks the surrounding divider lines separately. A case can provide `Initial data` to prepare its data file and `Expected data` to check the final saved records.

## TC-01: Mixed task types and status changes

Aim: Verify that to-dos, deadlines, and events are added polymorphically and retain their type-specific display when marked and unmarked.

### Inputs

```text
todo borrow book
deadline return book /by 2019-10-15
event project meeting /from 2019-10-16 1400 /to 2019-10-16 1600
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
   [D][ ] return book (by: Oct 15 2019)
 Now you have 2 tasks in the list.
---
 Got it. I've added this task:
   [E][ ] project meeting (from: Oct 16 2019, 2:00PM to: Oct 16 2019, 4:00PM)
 Now you have 3 tasks in the list.
---
 Nice! I've marked this task as done:
   [T][X] borrow book
---
 Here are the tasks in your list:
 1.[T][X] borrow book
 2.[D][ ] return book (by: Oct 15 2019)
 3.[E][ ] project meeting (from: Oct 16 2019, 2:00PM to: Oct 16 2019, 4:00PM)
---
 OK, I've marked this task as not done yet:
   [T][ ] borrow book
---
 Here are the tasks in your list:
 1.[T][ ] borrow book
 2.[D][ ] return book (by: Oct 15 2019)
 3.[E][ ] project meeting (from: Oct 16 2019, 2:00PM to: Oct 16 2019, 4:00PM)
---
 Bye for now! Keep shining, and I hope to see you again soon!
```

## TC-02: Parsed deadline dates and times

Aim: Verify strict calendar validation, ISO and day/month/year inputs, friendly display formatting, state preservation after invalid dates, and canonical date-time storage.

### Inputs

```text
deadline impossible date /by 2019-02-29
deadline wrong separator /by 2019/10/15
deadline invalid time /by 2019-10-15 2400
deadline do homework /by 2019-10-15
deadline return book /by 2/12/2019 1800
list
bye
```

### Expected outputs

```text
 Hmm, use a date like 2019-10-15 or 2/12/2019, optionally followed by a 24-hour time such as 1800.
---
 Hmm, use a date like 2019-10-15 or 2/12/2019, optionally followed by a 24-hour time such as 1800.
---
 Hmm, use a date like 2019-10-15 or 2/12/2019, optionally followed by a 24-hour time such as 1800.
---
 Got it. I've added this task:
   [D][ ] do homework (by: Oct 15 2019)
 Now you have 1 tasks in the list.
---
 Got it. I've added this task:
   [D][ ] return book (by: Dec 02 2019, 6:00PM)
 Now you have 2 tasks in the list.
---
 Here are the tasks in your list:
 1.[D][ ] do homework (by: Oct 15 2019)
 2.[D][ ] return book (by: Dec 02 2019, 6:00PM)
---
 Bye for now! Keep shining, and I hope to see you again soon!
```

### Expected data

```text
D | 0 | do homework | 2019-10-15T00:00:00
D | 0 | return book | 2019-12-02T18:00:00
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
deadline impossible date /by 2019-02-29
todo valid task
event bad event /from 2019-10-15 /to tomorrow
list
bye
```

### Expected outputs

```text
 Hmm, please enter a command.
---
 Hmm, a todo needs a description. Try: todo <description>
---
 Hmm, I don't recognize that command. Try todo, deadline, event, list, find, mark, unmark, delete, or bye.
---
 Hmm, I don't recognize that command. Try todo, deadline, event, list, find, mark, unmark, delete, or bye.
---
 Hmm, I don't recognize that command. Try todo, deadline, event, list, find, mark, unmark, delete, or bye.
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
 Hmm, use a date like 2019-10-15 or 2/12/2019, optionally followed by a 24-hour time such as 1800.
---
 Got it. I've added this task:
   [T][ ] valid task
 Now you have 1 tasks in the list.
---
 Hmm, use a date like 2019-10-15 or 2/12/2019, optionally followed by a 24-hour time such as 1800.
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
deadline return book /by 2019-06-06
event project meeting /from 2019-08-06 1400 /to 2019-08-06 1600
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
   [D][ ] return book (by: Jun 06 2019)
 Now you have 2 tasks in the list.
---
 Got it. I've added this task:
   [E][ ] project meeting (from: Aug 06 2019, 2:00PM to: Aug 06 2019, 4:00PM)
 Now you have 3 tasks in the list.
---
 Nice! I've marked this task as done:
   [T][X] read book
---
 Nice! I've marked this task as done:
   [D][X] return book (by: Jun 06 2019)
---
 Here are the tasks in your list:
 1.[T][X] read book
 2.[D][X] return book (by: Jun 06 2019)
 3.[E][ ] project meeting (from: Aug 06 2019, 2:00PM to: Aug 06 2019, 4:00PM)
---
 Noted. I've removed this task:
   [E][ ] project meeting (from: Aug 06 2019, 2:00PM to: Aug 06 2019, 4:00PM)
 Now you have 2 tasks in the list.
---
 Here are the tasks in your list:
 1.[T][X] read book
 2.[D][X] return book (by: Jun 06 2019)
---
 Noted. I've removed this task:
   [T][X] read book
 Now you have 1 tasks in the list.
---
 Here are the tasks in your list:
 1.[D][X] return book (by: Jun 06 2019)
---
 Bye for now! Keep shining, and I hope to see you again soon!
```

## TC-05: Invalid delete commands preserve the list

Aim: Verify empty-list, missing, non-numeric, and out-of-range delete commands are rejected without removing or modifying tasks.

### Inputs

```text
delete 1
todo keep this task
deadline remove this task /by 2019-10-16
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
   [D][ ] remove this task (by: Oct 16 2019)
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
 2.[D][ ] remove this task (by: Oct 16 2019)
---
 Noted. I've removed this task:
   [D][ ] remove this task (by: Oct 16 2019)
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

## TC-07: Load and automatically save tasks

Aim: Verify valid saved tasks and statuses load at startup, a malformed line is skipped, and each kind of successful list mutation is reflected in the final data file.

### Initial data

```text
T | 1 | read book
D | 0 | return book | 2019-06-06T00:00:00
this line is corrupted
E | 0 | project meeting | 2019-08-06T14:00:00 | 2019-08-06T16:00:00
```

### Inputs

```text
list
unmark 1
mark 2
delete 3
todo join sports club
list
bye
```

### Expected outputs

```text
 Here are the tasks in your list:
 1.[T][X] read book
 2.[D][ ] return book (by: Jun 06 2019)
 3.[E][ ] project meeting (from: Aug 06 2019, 2:00PM to: Aug 06 2019, 4:00PM)
---
 OK, I've marked this task as not done yet:
   [T][ ] read book
---
 Nice! I've marked this task as done:
   [D][X] return book (by: Jun 06 2019)
---
 Noted. I've removed this task:
   [E][ ] project meeting (from: Aug 06 2019, 2:00PM to: Aug 06 2019, 4:00PM)
 Now you have 2 tasks in the list.
---
 Got it. I've added this task:
   [T][ ] join sports club
 Now you have 3 tasks in the list.
---
 Here are the tasks in your list:
 1.[T][ ] read book
 2.[D][X] return book (by: Jun 06 2019)
 3.[T][ ] join sports club
---
 Bye for now! Keep shining, and I hope to see you again soon!
```

### Expected data

```text
T | 0 | read book
D | 1 | return book | 2019-06-06T00:00:00
T | 0 | join sports club
```

## TC-08: Find tasks by description

Aim: Verify that find requires a separate non-empty keyword, matches description substrings case-insensitively across task types, renumbers matches, shows no results cleanly, and does not change task state.

### Inputs

```text
find
findbook
todo read book
deadline return BOOK /by 2019-06-06
event book club /from 2019-08-06 1400 /to 2019-08-06 1600
todo write notes
mark 1
mark 2
find bOoK
find CLUB
find 2019
find missing
list
bye
```

### Expected outputs

```text
 Hmm, tell me what to find. Try: find <keyword>
---
 Hmm, I don't recognize that command. Try todo, deadline, event, list, find, mark, unmark, delete, or bye.
---
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
---
 Got it. I've added this task:
   [D][ ] return BOOK (by: Jun 06 2019)
 Now you have 2 tasks in the list.
---
 Got it. I've added this task:
   [E][ ] book club (from: Aug 06 2019, 2:00PM to: Aug 06 2019, 4:00PM)
 Now you have 3 tasks in the list.
---
 Got it. I've added this task:
   [T][ ] write notes
 Now you have 4 tasks in the list.
---
 Nice! I've marked this task as done:
   [T][X] read book
---
 Nice! I've marked this task as done:
   [D][X] return BOOK (by: Jun 06 2019)
---
 Here are the matching tasks in your list:
 1.[T][X] read book
 2.[D][X] return BOOK (by: Jun 06 2019)
 3.[E][ ] book club (from: Aug 06 2019, 2:00PM to: Aug 06 2019, 4:00PM)
---
 Here are the matching tasks in your list:
 1.[E][ ] book club (from: Aug 06 2019, 2:00PM to: Aug 06 2019, 4:00PM)
---
 Here are the matching tasks in your list:
---
 Here are the matching tasks in your list:
---
 Here are the tasks in your list:
 1.[T][X] read book
 2.[D][X] return BOOK (by: Jun 06 2019)
 3.[E][ ] book club (from: Aug 06 2019, 2:00PM to: Aug 06 2019, 4:00PM)
 4.[T][ ] write notes
---
 Bye for now! Keep shining, and I hope to see you again soon!
```
