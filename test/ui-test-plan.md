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
