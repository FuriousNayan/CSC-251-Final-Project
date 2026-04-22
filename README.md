# The WECIB Card Game

A Pokémon-style **turn-based card game** built for Java 2, featuring a cast of WECIB students and teachers. You **build a deck through a triple draft**, then **battle** using abilities, attacks, and type interactions. The goal is to **defeat your opponent and become the WECIB Master**.

The game is a **desktop app** (Java and JavaFX) with music, a title screen, draft and battle flow, and in-game help.

## How to run the game (Windows)

These steps assume you are on **Windows** and have a **Java 21+** runtime installed (`java` on your `PATH`).

1. **Open the repository on GitHub** and click the **Code** button in the top-right of the file list.

2. **Download the repository as a ZIP** — in the menu that appears, choose **Download ZIP** (at the bottom of the popup).

3. **Find the ZIP** on your computer, then **Extract All** to a folder you can find easily.

4. **Open the project folder in a terminal:** right-click the extracted project folder, then choose **Open in Terminal** (or *Open in Windows Terminal* / *Open in Command Prompt*, depending on your system).

5. In that terminal, run:

   ```bat
   .\run.bat
   ```

6. **Play** when the WECIB Card Game window opens.

`run.bat` starts the packaged game JAR and uses the **JavaFX SDK** included in this project, so you do not need a separate JavaFX install.

## Building from source (optional)

If you have **Apache Maven** and Java 21 installed, you can rebuild the JAR with:

```bash
mvn clean package
```

The shaded JAR is written to `target/wecib-card-game-1.0-SNAPSHOT.jar`, which `run.bat` already points to by default.
