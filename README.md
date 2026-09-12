## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

## Database Setup

The application connects to MySQL database `gamedb` on `localhost:3306` using the
MySQL connector in `lib/mysql-connector-j-9.7.0.jar`.

By default it uses user `root` with an empty password. Set these environment
variables when your local MySQL installation uses different credentials:

```powershell
$env:SUDOKU_DB_USER = "your_mysql_user"
$env:SUDOKU_DB_PASSWORD = "your_mysql_password"
java -cp "bin;lib/mysql-connector-j-9.7.0.jar" MainFrame
```

When compiling from a newer JDK than the JDK selected by VS Code, target Java
21 so the generated classes remain compatible with the VS Code runtime:

```powershell
javac --release 21 -cp "lib/mysql-connector-j-9.7.0.jar" -d bin src/*.java
```

You can also override the connection settings with Java properties:

```powershell
java -Dsudoku.db.url="jdbc:mysql://localhost:3306/gamedb?serverTimezone=UTC" `
	-Dsudoku.db.user="your_mysql_user" `
	-Dsudoku.db.password="your_mysql_password" `
	-cp "bin;lib/mysql-connector-j-9.7.0.jar" MainFrame
```

Users authenticate against `User`. On the login screen, enter a new username
and password and click **Register**. If the username is not already saved, the
new user is inserted into `User`; that username and password can then be used
with **Login** to open the game menu. Admins authenticate against `Admin` and
see the **Show status** button in the menu. The status window lists every row
in `User` with its password, total match count, and total score. A completed
Tiger Trap match adds its score to the logged-in user's existing `Game_Stat` row
and increments `matches_played`; the first match creates that row.

The game is single-player: the logged-in player controls the blockers. The
Tiger is controlled by a bot that automatically chooses a random available
point whenever its turn begins.

The `User.user_id` column must generate unique IDs, and `User.username` should
have a unique constraint so duplicate registrations are rejected by MySQL.
