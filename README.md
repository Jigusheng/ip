# Lumi project

This is a project template for a greenfield Java project called _Lumi_. Given below are instructions on how to use it.

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/lumi/Lumi.java` file, right-click it, and choose `Run Lumi.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see something like the below as the output:
   ```
   o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o
    _      _   _ __  __ ___ 
   | |    | | | |  \/  |_ _|
   | |    | | | | |\/| || | 
   | |___ | |_| | |  | || | 
   |_____| \___/|_|  |_|___|
   Hi there! I'm Lumi, your bright and bubbly chat buddy!
   I'm popping in to sprinkle a little cheer your way.
   o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o
   That's all for now - thanks for stopping by!
   Stay sparkly, and come chat again soon!
   o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o
   ```

**Warning:** Keep `src/main/java` as the source root for all Java packages. Do not move the `lumi` package outside this folder, because build tools such as Gradle expect this standard layout.
