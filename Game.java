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
    public static Room currentRoom;
    public String[] items = {ANSI_YELLOW + "Energy Drink" + ANSI_RESET, ANSI_GREEN + "Green Apple" + ANSI_RESET , ANSI_RED + "Pistol from the police officer" + ANSI_RESET};
    public String[] inventoryItems = new String[4];
    private int itemNumber = 0;
    public int coins = 0;
    public int roomNumber = getRandomNumberInRange(1,6);
    private boolean searchTask = true;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";

    boolean allRoomsTask = false;
    boolean policeTask = false;

    boolean outsideTask = false;
    boolean marktcenterTask = false;
    boolean gymTask = false;
    boolean pubTask = false;
    boolean theatreTask = false;
    boolean policeofficeTask = false;

    boolean thiefTask = false;
    boolean actorTask = false;
    boolean dumbelTask = false;
    boolean everyRoomTask = false;
    boolean energyTask = false;
    boolean drinkTask = false;

    boolean energyBuyed = false;

    Room actorLocation; 
    Room thiefLocation;
    Room dumbelLocation;

    String thiefLocationName;



    public static Room outside, theatre, pub, gym, policeoffice, marktcenter,townhall;
        
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
        outside = new Room("outside the main entrance of the city🏙️", "Go to every location on the map!"); //working
        marktcenter = new Room("in the marktcenter🛒","Buy an energy drink to increase your strength! To buy things at the store type in 'shop'!"); //working
        theatre = new Room("in the theatre🎭", "Find the actors in the city!"); //working
        pub = new Room("in the city pub🍾 Type in drinks to see all drinks you can buy", "Buy a drink!"); //working
        gym = new Room("in a gym🏃", "Find the lost dumbbell in the city!");  //working
        policeoffice = new Room("in the police office👮‍♂️", "Help the police officers to find the thief!"); //working
        townhall = new Room("in the wonderful townhall.🏢 There's only one exit right! Try it out!", "Defeat the boss after completing all the other Tasks!");
        
        // initialise room exits
        outside.setExits(pub, theatre, gym, marktcenter);
        theatre.setExits(null, marktcenter, null, outside);
        pub.setExits(townhall, null, outside, null);
        gym.setExits(marktcenter, null, null, policeoffice);
        policeoffice.setExits(null, pub, null, marktcenter);
        marktcenter.setExits(outside, policeoffice, gym, theatre);
        townhall.setExits(townhall, townhall, pub, townhall);


        thiefLocation = marktcenter;
        
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


        //Random Spawn for actor
        if(roomNumber == 1) {
        actorLocation = marktcenter; 
        }
        if(roomNumber == 2) {
        actorLocation = outside;
        }
        if(roomNumber == 3) {
        actorLocation = pub;
        }
        if(roomNumber == 4) {
        actorLocation = theatre;
        }
        if(roomNumber == 5) {
        actorLocation = policeoffice;
        }
        if(roomNumber == 6) {
        actorLocation = gym;
        }

        //Random dumbel location
        if(roomNumber == 1) {
        dumbelLocation = pub; 
        }
        if(roomNumber == 2) {
        dumbelLocation = theatre;
        }
        if(roomNumber == 3) {
        dumbelLocation = marktcenter;
        }
        if(roomNumber == 4) {
        dumbelLocation = outside;
        }
        if(roomNumber == 5) {
        dumbelLocation = gym;
        }
        if(roomNumber == 6) {
        dumbelLocation = policeoffice;
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

            if(allRoomsTask == false) {
            checkTasks();
            }
            endMessage();
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
        System.out.println("");
        System.out.println("Your Task: " + currentRoom.getTask());
        System.out.println();
    }

    private void checkTasks() {

        if(currentRoom == outside) {outsideTask = true;}
        if(currentRoom == marktcenter) {marktcenterTask = true;}
        if(currentRoom == gym) {gymTask = true;}
        if(currentRoom == pub) {pubTask = true;}
        if(currentRoom == policeoffice) {policeofficeTask = true;}
        if(currentRoom == theatre) {theatreTask = true;}

        if(outsideTask == true && marktcenterTask == true && gymTask == true && pubTask == true && policeofficeTask == true && theatreTask == true) {
            allRoomsTask = true;
            everyRoomTask = true;
            coins = coins + 10;

            System.out.println("Congratualtions!🎉 You've visited every location on the map!");
            System.out.println(ANSI_RED + "You've got: 10 Coins" + ANSI_RESET);
        }


    }
    

    private void look()
    {
        System.out.println("You're " + currentRoom.description);
        if(dumbelLocation == currentRoom) {System.out.println("You've found the lost dumbel!");System.out.println(ANSI_RED + "You've got: 10 coins" + ANSI_RESET); coins += 10; dumbelTask = true;}
        if(actorLocation == currentRoom) {System.out.println("You've found the actors from the theater!");System.out.println(ANSI_RED + "You've got: 10 coins" + ANSI_RESET); coins += 10; actorTask = true;}
        if(currentRoom == marktcenter) {

        if(roomNumber == 1) {
            thiefLocation = townhall; 
            thiefLocationName = " townhall";
        }
        if(roomNumber == 2) {
            thiefLocation = outside;
            thiefLocationName = " outside";
        }
        if(roomNumber == 3) {
            thiefLocation = pub;
            thiefLocationName = " pub";
        }
        if(roomNumber == 4) {
            thiefLocation = theatre;
            thiefLocationName = " theatre";
        }
        if(roomNumber == 5) {
            thiefLocation = policeoffice;
            thiefLocationName = " policeoffice";
        }
        if(roomNumber == 6) {
            thiefLocation = gym;
            thiefLocationName = " gym";
        }

            System.out.println("There is the thief! Run and get him!"); 
            System.out.println("The thief flew to:" + thiefLocationName);
        }

        if(thiefLocation == currentRoom && currentRoom != marktcenter) {System.out.println("There's the thief! You've catched him this time!"); System.out.println(ANSI_RED + "You've got: 20 coins" + ANSI_RESET); coins+= 20;thiefTask = true;}
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
        System.out.println(ANSI_YELLOW + "1 Energy Drink:");
        System.out.println("    10 coins" + ANSI_RESET);
        System.out.println("");
        System.out.println(ANSI_GREEN + "2 Green Apple");
        System.out.println("    6 coins" + ANSI_RESET);
        System.out.println("");
        System.out.println("3 Pistol from the police officer");
        System.out.println("    50 coins");
        System.out.println("");
        System.out.println("Use the number of the product to buy it!");
    }

    private void buy(Command icommand) {

        if(!icommand.hasSecondWord()) {
            System.out.println("Whatcha wanna buy? Select a product!");
            return;
        }

        String item = icommand.getSecondWord();

        if(item.equals("1") && coins >= 10) {inventoryItems[1] = items[0];coins-=10; energyBuyed = true; if(energyBuyed) {coins+=10; energyTask =true;} 
            System.out.println("Congratualtions!🎉 You've got an energy drink!");
            System.out.println(ANSI_RED + "You've got: 10 Coins" + ANSI_RESET);
        }
        if(item.equals("2") && coins >= 6) {inventoryItems[2] = items[1];coins-=6;}
        if(item.equals("3") && coins >= 50) {inventoryItems[3] = items[2];coins-=50;}
    }

    private void endMessage() {
        if(dumbelTask && actorTask && thiefTask&& everyRoomTask && energyTask && drinkTask) {System.out.println("You've completed every Task!");System.out.println(ANSI_RED + "You've unlocked the boss! You can fight him in the townhall to finish the Game!" + ANSI_RESET); System.out.println("tip: A weapon from the marktcenter could help you to win!");}
    }

    private void drinks() {
        if(currentRoom == pub) {
        System.out.println(ANSI_RED + "0,5l Beer" + ANSI_RESET);
        System.out.println("    6 coins");
        System.out.println("");
        System.out.println(ANSI_RED+"2cl Whiskey"+ANSI_RESET);
        System.out.println("    10 coins");
        System.out.println("");
        System.out.println(ANSI_RED+"2,5cl Vodka"+ANSI_RESET);
        System.out.println("    10 coins");
        System.out.println("");
        System.out.println(ANSI_RED + "0,4l Cola" + ANSI_RESET);
        System.out.println("    8 coins");
        }
    }

    private void drink(Command iDrink) {
        if(!iDrink.hasSecondWord()) {
            System.out.println("You wanna drink something?🍻");
            return;
        }

        String drink = iDrink.getSecondWord();

        if(drink.equals("Beer") && coins >= 6) {coins-=6; System.out.println("Congrats!🎉 You've completed the task!"); System.out.println(ANSI_RED + "You've got 10 coins"+  ANSI_RESET); coins += 10;System.out.println("You've drunk your " + drink);}
        if(drink.equals("Whiskey") && coins >= 10) {coins-=10; System.out.println("Congrats!🎉 You've completed the task!"); System.out.println(ANSI_RED + "You've got 10 coins"+  ANSI_RESET); coins += 10;System.out.println("You've drunk your " + drink);}
        if(drink.equals("Vodka") && coins >= 10) {coins-=10; System.out.println("Congrats!🎉 You've completed the task!"); System.out.println(ANSI_RED + "You've got 10 coins"+  ANSI_RESET); coins += 10;System.out.println("You've drunk your " + drink);}
        if(drink.equals("Cola") && coins >= 8) {coins-=8; System.out.println("Congrats!🎉 You've completed the task!"); System.out.println(ANSI_RED + "You've got 10 coins"+  ANSI_RESET); coins += 10;System.out.println("You've drunk your " + drink);}
        if(drink.equals("Energy Drink")) {System.out.println("You've drunk your " + drink);}
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
        else if (commandWord.equals("coins"))
            printCoins();
        else if(commandWord.equals("shop") && currentRoom == marktcenter)
            shop();
        else if(commandWord.equals("buy") && currentRoom == marktcenter)
            buy(command);
        else if(commandWord.equals("offer") && currentRoom == marktcenter)
            offer();
        else if(commandWord.equals("inventory"))
            showInventory();
        else if(commandWord.equals("drinks"))
            drinks();
        else if(commandWord.equals("drink"))
            drink(command);
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
        System.out.println("   go quit help look coins donate shop offer inventory buy drinks drink  ");
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
