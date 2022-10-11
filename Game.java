/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Luca D'Aguanno
 * @version 1.0 (September 2022)
 */

import java.util.Random;

class Game 
{
    // ~~~ initialization ~~~
    public static void main(String args[]) {
        Game g = new Game();
        g.play();
    }

    private Parser parser;
    private Room currentRoom;
    public String[] items = {ANSI_YELLOW + "Energy Drink" + ANSI_RESET, ANSI_GREEN + "Green Apple" + ANSI_RESET ,ANSI_RED + "Pistol from the police officer" + ANSI_RESET};
    public String[] inventoryItems = new String[3];
    public int coins = 0;
    public int roomNumber = getRandomNumberInRange(1,6);
    private boolean searchTask = true;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "	\u001B[31m";

    Room outside, theatre, pub, gym, policeoffice, marktcenter;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();

        inventoryItems[0] = ANSI_BLUE + "Bottle of water" + ANSI_RESET;
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    { 
        // create the rooms
        outside = new Room("outside the main entrance of the city🏙️", "Go to every location on the map!");
        marktcenter = new Room("in the marktcenter🛒","Find some energy drinks to increase your strength! To buy things at the store type in 'shop'!");
        theatre = new Room("in the theatre🎭", "Find the actors in the city!");
        pub = new Room("in the city pub🍾", "Drink more shots as the buddergolem!!");
        gym = new Room("in a gym🏃", "Find the lost dumbbell in the city!");
        policeoffice = new Room("in the police office👮‍♂️", "Help the police officers to find the rascal!");
        
        // initialise room exits
        outside.setExits(null, theatre, gym, pub);
        theatre.setExits(null, marktcenter, null, outside);
        pub.setExits(null, outside, null, null);
        gym.setExits(outside, policeoffice, null, null);
        policeoffice.setExits(null, null, null, gym);
        marktcenter.setExits(null, null, gym, null);

        //startinglocation of the game
        
        if(roomNumber == 1) {
        currentRoom = outside; 
        }
            if(roomNumber == 2) {
        currentRoom = marktcenter;
        }
        if(roomNumber == 3) {
            currentRoom = theatre;
        }
        if(roomNumber == 4) {
        currentRoom = pub;
        }
        if(roomNumber == 5) {
        currentRoom = gym;
        }
        if(roomNumber == 6) {
        currentRoom = policeoffice;
        }

    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.🎉  Good bye.👋");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to this Adventure!");
        System.out.println("Adventure is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println("You are " + currentRoom.getDescription());
        System.out.print("Exits: ");
        if(currentRoom.northExit != null)
            System.out.print("north ");
        if(currentRoom.eastExit != null)
            System.out.print("east ");
        if(currentRoom.southExit != null)
            System.out.print("south ");
        if(currentRoom.westExit != null)
            System.out.print("west ");
        System.out.println();
    }


    private void look()
    {
        System.out.println("You're " + currentRoom.description);
    }

    private void donate() {
        coins = 0;
    }

    private void printCoins()
    {
        System.out.println("Your current balance are " + coins + " Coins");
    }

    private void showInventory() {

        for(int i=0; i<inventoryItems.length;i++)
            System.out.println(inventoryItems[i]);
    }

    private void shop() {
        System.out.println("Welcome in the marktcenter!");
        System.out.println("You can buy some powerups here!");
        System.out.println("Please type in the command 'offer' to see all offers!");
    }

    private void offer() {
        System.out.println("Here are all offers that we have here:");
        System.out.println("");
        System.out.println(ANSI_GREEN + "1 Energy Drink:");
        System.out.println("  3 coins" + ANSI_RESET);
        System.out.println("");
        System.out.println(ANSI_RED + "more coming soon..." + ANSI_RESET);
    }

    private static int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
    /**
     * Given a command, process (that is: execute) the command.
     * If this command ends the game, true is returned, otherwise false is
     * returned.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help"))
            printHelp();
        else if (commandWord.equals("go"))
            goRoom(command);
        else if (commandWord.equals("look"))
            look();
        else if (commandWord.equals("donate"))
            donate();
        else if (commandWord.equals("coins"))
            printCoins();
        else if(commandWord.equals("shop") && currentRoom == marktcenter)
            shop();
        else if(commandWord.equals("offer") && currentRoom == marktcenter)
            offer();
        else if(commandWord.equals("inventory"))
            showInventory();
        else if (commandWord.equals("quit"))
            wantToQuit = quit(command);

        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println("   go quit help look coins donate shop inventory ");
    }

    /** 
     * Try to go to one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = null;
        if(direction.equals("north"))
            nextRoom = currentRoom.northExit;
        if(direction.equals("east"))
            nextRoom = currentRoom.eastExit;
        if(direction.equals("south"))
            nextRoom = currentRoom.southExit;
        if(direction.equals("west"))
            nextRoom = currentRoom.westExit;

        if (nextRoom == null)
            System.out.println("There is no door!");
        else {
            currentRoom = nextRoom;
            System.out.println("You are " + currentRoom.getDescription());

            if(searchTask) {
            System.out.println("Your Task: " + currentRoom.getTask());
            }

            System.out.print("Exits: ");
            if(currentRoom.northExit != null)
                System.out.print("north ");
            if(currentRoom.eastExit != null)
                System.out.print("east ");
            if(currentRoom.southExit != null)
                System.out.print("south ");
            if(currentRoom.westExit != null)
                System.out.print("west ");
            System.out.println();
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game. Return true, if this command
     * quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else
            return true;  // signal that we want to quit
    }
}
