# discord-select-menu-bug

Bot to assist with reproducing a visual bug in the Discord Android client.

## Bug

On Android, when a bot edits a message with a new select menu, instead of displaying the placeholder value, the last selected value
from the previous select menu is displayed.

To reproduce:

1. Run the bot per instructions in the section below.
2. Type `test`. The bot will send a select menu.
3. Choose any option in the select menu. The bot will replace the select menu with a button.
4. Click the button. The bot will replace the button with a new select menu.
5. Observe that the new select menu displays the selection from the last select menu instead of the placeholder, even though
the selection from the last select menu is not an option in the new select menu.

Actual behavior: The last selected option from the previous select menu is displayed.

Expected behavior: The placeholder is displayed in the new select menu.

## Running

### Prereqs

- Java 11 or above.
- `application.properties` in project root:

    ```
    bot.token=<bot token>
    ```

### Using script

```
./run
```

### Manually

```
./mvnw clean install
./mvnw exec:java -Dexec.mainClass="org.gerbil.discord.bug.Main" -Dconfig.file=application.properties
```

### On Windows

```
mvnw.cmd clean install
mvnw.cmd exec:java -Dexec.mainClass="org.gerbil.discord.bug.Main" -Dconfig.file=application.properties
```

