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

## Building the fat JAR

The Shadow plugin packages Lumi and all of its runtime dependencies into one executable file named `duke.jar`. Ensure that `java -version` reports Java 25 before building or running it.

From the project root, run the following command on Windows:

```powershell
.\gradlew.bat shadowJar
```

On macOS or Linux, run:

```shell
./gradlew shadowJar
```

The completed JAR is created at `build/libs/duke.jar`. The normal Gradle `build` task also creates this JAR because the Shadow task is connected to the build lifecycle.

To run Lumi from the project root on Windows:

```powershell
java -jar build\libs\duke.jar
```

On macOS or Linux:

```shell
java -jar build/libs/duke.jar
```

Lumi stores tasks in `data/lumi.txt` relative to the directory from which the command is run. Running the command from the project root therefore keeps the data file in the project's `data` folder.

In IntelliJ, you can build the same JAR by opening the Gradle tool window, finding the `shadowJar` task, and running it. Refresh the project view afterward if the `build` folder is not immediately visible.
