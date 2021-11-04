package textadventure;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class TextAdventure {

    int row;
    int col;

    Room[][] map;

    Scanner input;

    public void runGame() {
        System.out.println("+-------------------------------------------+");
        System.out.println("|                                           |");
        System.out.println("|       Welcome to my text adventure        |");
        System.out.println("|                                           |");
        System.out.println("+-------------------------------------------+\n");

        boolean running = true;

        // Starting the game
        while(running) {

            System.out.println(map[row][col].toString());               // Print the current room
            // Read user command
            String[] commandParts = readUserInput();
            String command = commandParts[0];

            // Check what command the user input
            if(command.equalsIgnoreCase("go")) {
                // Make sure something got written after "go"
                if(commandParts.length == 2) {
                    updatePlayerPosition(commandParts[1]);
                    System.out.println("Going " + commandParts[1]);
                }
                else {
                    System.out.println("You need to add a direction.");
                    System.out.println("For example go north\"");
                }
            }
            else if(command.equalsIgnoreCase("look")) {
                String itemDescription = map[row][col].getItemDescription();
                System.out.println(itemDescription);
            }
            else if(command.equalsIgnoreCase("save")) {
                save(row, col);
            }
            else if(command.equalsIgnoreCase("load")) {
                LoadSaveGame();
            }
            else if(command.equalsIgnoreCase("quit")) {
                running = false;
            }
        }
    }

    public static void save(int row, int col) {
        File file = new File("./save/saved_game.txt");
        try {
            FileWriter fileWriter = new FileWriter(file);
            String position = String.format("%d, %d", row, col);
            fileWriter.write(position);
            fileWriter.close();
            System.out.println("The game is saved");
        } catch (IOException e) {
            System.out.println("Could not save the game");
        }
    }

    public static String load() {
        File file = new File("./save/saved_game.txt");
        try {
            Scanner fileScanner = new Scanner(file);
            String position = fileScanner.nextLine();
            fileScanner.close();
            return position;
        } catch (FileNotFoundException e) {
            System.out.println("Could not load a saved game");
        }
        return null;
    }

    private void updatePlayerPosition(String direction) {
        // Look for direction
        if(direction.equalsIgnoreCase("north")) {
            row--;
            // Make sure we stay on the map
            if(row < 0) {
                row = 0;
            }
        }
        else if(direction.equalsIgnoreCase("south")) {
            row++;
            if(row >= map.length) {
                row--;
            }
        }
        else if(direction.equalsIgnoreCase("east")) {
            col++;
            if(col >= map[row].length) {
                col--;
            }
        }
        else if(direction.equalsIgnoreCase("west")) {
            col--;
            if(col < 0) {
                col = 0;
            }
        }
    }

    private String[] readUserInput() {
        System.out.print("> ");
        String command = input.nextLine();

        String[] commandParts = command.split(" ");
        return commandParts;
    }



    public void initialization() {
        // Init scanner input for reading user input
        input = new Scanner(System.in);

        // Initializing
        Room aHallway = new Room("A hallway\n", "A dark hallway\n");
        Room smallOffice = new Room("Small office\n", "A small office with a desk in the middle in front of a door.\n");
        Room theEntrance = new Room("The entrance\n", "An entrance to the office.\n");
        Room bossOffice = new Room("The boss's office\n", "A big and fancy office with a safe in the corner.\n");

        // Creating a knife and putting it in a room
        Item knife = new Item("Knife", "A rusty knife.");
        aHallway.setItem(knife);

        // Chest with items
        Chest chest = new Chest("Chest", "A chest with items in it.");
        Item key = new Item("Key", "A key");
        Item note = new Item("Note", "A note with numbers on it: 462 982");
        chest.addItemsToChest(key);
        chest.addItemsToChest(note);
        smallOffice.setItem(chest);

        map = new Room[][] {
                {aHallway, smallOffice},
                {theEntrance, bossOffice}};

        row = 1;
        col = 0;
    }


    private void LoadSaveGame() {
        String position = load();
        if(position != null) {
            String[] pos = position.split(", ");
            int oldRow = row;
            int oldCol = col;
            row = Integer.parseInt(pos[0]);
            col = Integer.parseInt(pos[1]);
            if(row >= map.length) {
                System.out.println("Error reading row coordinates from file. Are you cheating?");
                row = oldRow;
                col = oldCol;
            }
            else {
                if(col >= map[row].length) {
                    System.out.println("Error reading row coordinates from file. Are you cheating?");
                    row = oldRow;
                    col = oldCol;
                }
            }
        }
    }

    public void quit() {
        System.out.println("You have quit the game, thank you for playing.");
    }
}
