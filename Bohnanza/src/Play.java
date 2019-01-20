import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

/*
 * @author Aalea
 * 
 * this class will receive the player and virtually control the player's actions.
 * it will consult the user and perform planting and harvesting
 * 
 */
public class Play {

	/*
	 * the class will receive the player to play and it will be held
	 * in this variable. After the play is over, this variable will be 
	 * sent back to where it was called from
	 */
	private static Player player; 
	
	private static int playerNum;

	private Scanner input;

	private Trade trade;

	/*
	 * not constructor. will receive the current player, start keyboard input, 
	 * call necessary methods then return the player back after it has played
	 */
	public Player start(Player player, int playerNum) {

		this.player = player;
		this.playerNum = playerNum;
		input = new Scanner(System.in);

		plant();
		
		BohnanzaGui tempGui = Bohnanza.getGui().setPlayerCards(playerNum);
		Bohnanza.setGui(tempGui);

		trade = new Trade();
		this.player = trade.trading(player, playerNum);

		//at the end of turn, draw 2 cards
		ArrayList<Card> temp = new ArrayList<Card>();

		for(int i = 0; i <= 2; i++)
			temp.add(Deck.takeCardFromDeck());

		player.setHand(temp);

		return player;

	}

	/*
	 * this method performs all steps of the turn
	 */
	static void plant() {

		int field = 0; //variable to hold which field player wishes to plant/harvest in
		ArrayList<Card> temp = new ArrayList<Card>();

		System.out.println("The card dealt least recently to you is " + player.getHand().get(0));
		System.out.println("You have two fields " + player.getField(1) + " and " + player.getField(2));
		//check if fields occupied
		if (player.getField(1).getQuantity() > 0 || player.getField(2).getQuantity() > 0) {	
			System.out.println("Do you wish to harvest? (y/n)");
			if (JOptionPane.showInputDialog("Do you wish to harvest? (y/n)").equals("y")) {
				//if both fields can be harvested, give them a choice
				if (player.getField(1).getQuantity() > 0 && player.getField(2).getQuantity() > 0) {
					System.out.println("Which field? (1 or 2)");
					harvest(Integer.parseInt(JOptionPane.showInputDialog("Which field? (1 or 2)")));
				}
				//if only field 1 can be harvested
				else if (player.getField(1).getQuantity() > 0)
					harvest(1);
				//if only field 2 can be harvested
				else if (player.getField(2).getQuantity() > 0)
					harvest(2);
			}
		}
		
		

		//if type of both field 1 and 2 are the same as the card, or both fields are empty
		if (player.getField(1).getType().equals(player.getHand().get(0).getType())
				&& player.getField(2).getType().equals(player.getHand().get(0).getType())
				|| player.getField(1).getType().equals("new")
				&& player.getField(2).getType().equals("new")) { //TO DO add condition for crop amount 1 or less

			System.out.println("Your card can be planted in both of your fields.");
			System.out.println("Do you wish to plant it in the field with " + 
					player.getField(1).getQuantity() + " crops or the field with " + 
					player.getField(1).getQuantity() + " crops? (1 or 2)");
			

			field = Integer.parseInt(JOptionPane.showInputDialog("Do you wish to plant it in the field with " + 
					player.getField(1).getQuantity() + " crops or the field with " + 
					player.getField(1).getQuantity() + " crops? (1 or 2)"));

		}
		//if field 1 is same type as card or is empty
		else if(player.getField(1).getType().equals(player.getHand().get(0).getType())
				|| player.getField(1).getType().equals("new")) {

			System.out.println("Currently, you can only plant in field 1, "
					+ "do you wish to harvest and plant the card in the other field? (y/n)");

			if (JOptionPane.showInputDialog("Currently, you can only plant in field 1, "
					+ "do you wish to harvest and plant the card in the other field? (y/n)").equals("y")) {

				field = 2;
				harvest(field);

			}
			else {
				field = 1;
				System.out.println("You will now plant in field 1");
			}

		}
		//if field 2 is the same type as the card or is empty
		else if(player.getField(2).getType().equals(player.getHand().get(0).getType())
				|| player.getField(2).getType().equals("new")) {

			System.out.println("Currently, you can only plant in field 2, "
					+ "do you wish to harvest and plant the card in the other field? (y/n)");

			if (JOptionPane.showInputDialog("Currently, you can only plant in field 2, "
					+ "do you wish to harvest and plant the card in the other field? (y/n)").equals("y")) {

				field = 1;
				harvest(field);

			}
			else {
				field = 2;
				System.out.println("You will now plant in field 2");
			}

		}
		else { //both fields have different types

			System.out.println("Both cannot be planted unless you harvest.");
			System.out.println("Field 1: " + player.getField(1));
			System.out.println("Field 2: " + player.getField(2));

			System.out.println("Which field do you wish to harvest and plant in? (1 or 2)");
			field = Integer.parseInt(JOptionPane.showInputDialog("Which field do you wish to harvest and plant in? (1 or 2)"));

			harvest(field);

		}

		player.getField(field).addToField(player.getHand().get(0)); //add to field will 
		Bohnanza.setGui(Bohnanza.getGui().playerFieldLabel(playerNum, field));
		
		//be able to detect if field 
		//is empty or not and follow
		//the correct procedure
		temp = player.getHand();
		Bohnanza.getDiscard().addCardToDiscard(temp.get(0));
		temp.remove(0);
		player.setHand(temp); //send card to discard
		

	}

	void plant(Player player) {

		this.player = player;

		int field = 0; //variable to hold which field player wishes to plant/harvest in
		ArrayList<Card> temp = new ArrayList<Card>();

		System.out.println("The card dealt least recently to you is " + player.getHand().get(0));
		System.out.println("You have two fields " + player.getField(1) + " and " + player.getField(2));
		//check if fields occupied
		if (player.getField(1).getQuantity() > 0 || player.getField(2).getQuantity() > 0) {	
			System.out.println("Do you wish to harvest? (y/n)");

			if (JOptionPane.showInputDialog("Do you wish to harvest? (y/n)").equals("y")) {
				//if both fields can be harvested, give them a choice
				if (player.getField(1).getQuantity() > 0 && player.getField(2).getQuantity() > 0) {
					System.out.println("Which field? (1 or 2)");
					harvest(Integer.parseInt(JOptionPane.showInputDialog("Which field? (1 or 2)")));
				}
				//if only field 1 can be harvested
				else if (player.getField(1).getQuantity() > 0)
					harvest(1);
				//if only field 2 can be harvested
				else if (player.getField(2).getQuantity() > 0)
					harvest(2);
			}
		}
		//if type of both field 1 and 2 are the same as the card, or both fields are empty
		if (player.getField(1).getType().equals(player.getHand().get(0).getType())
				&& player.getField(2).getType().equals(player.getHand().get(0).getType())
				|| player.getField(1).getType().equals("new")
				&& player.getField(2).getType().equals("new")) { //TO DO add condition for crop amount 1 or less

			System.out.println("Your card can be planted in both of your fields.");
			System.out.println("Do you wish to plant it in the field with " + 
					player.getField(1).getQuantity() + " crops or the field with " + 
					player.getField(1).getQuantity() + " crops? (1 or 2)");

			field = Integer.parseInt(JOptionPane.showInputDialog("Do you wish to plant it in the field with " + 
					player.getField(1).getQuantity() + " crops or the field with " + 
					player.getField(1).getQuantity() + " crops? (1 or 2)"));

		}
		//if field 1 is same type as card or is empty
		else if(player.getField(1).getType().equals(player.getHand().get(0).getType())
				|| player.getField(1).getType().equals("new")) {

			System.out.println("Currently, you can only plant in field 1, "
					+ "do you wish to harvest and plant the card in the other field? (y/n)");

			if (JOptionPane.showInputDialog("Currently, you can only plant in field 1, "
					+ "do you wish to harvest and plant the card in the other field? (y/n)").equals("y")) {

				field = 2;
				harvest(field);

			}
			else {
				field = 1;
				System.out.println("You will now plant in field 1");
			}

		}
		//if field 2 is the same type as the card or is empty
		else if(player.getField(2).getType().equals(player.getHand().get(0).getType())
				|| player.getField(2).getType().equals("new")) {

			System.out.println("Currently, you can only plant in field 2, "
					+ "do you wish to harvest and plant the card in the other field? (y/n)");

			if (JOptionPane.showInputDialog("Currently, you can only plant in field 2, "
					+ "do you wish to harvest and plant the card in the other field? (y/n)").equals("y")) {

				field = 1;
				harvest(field);

			}
			else {
				field = 2;
				System.out.println("You will now plant in field 2");
			}

		}
		else { //both fields have different types

			System.out.println("Both cannot be planted unless you harvest.");
			System.out.println("Field 1: " + player.getField(1));
			System.out.println("Field 2: " + player.getField(2));

			System.out.println("Which field do you wish to harvest and plant in? (1 or 2)");
			field = Integer.parseInt(JOptionPane.showInputDialog("Which field do you wish to harvest and plant in? (1 or 2)"));

			harvest(field);

		}

		player.getField(field).addToField(player.getHand().get(0)); //add to field will 
		//be able to detect if field 
		//is empty or not and follow
		//the correct procedure
		Bohnanza.setGui(Bohnanza.getGui().setField(player.getField(field)));;
		temp = player.getHand();
		Bohnanza.getDiscard().addCardToDiscard(temp.get(0));
		temp.remove(0);
		player.setHand(temp); //send card to discard

	}

	public static void harvest(int field) {

		//Count the number of crops of bean
		int cropAmount = player.getField(field).getQuantity();
		int coins = 0;
		int[] values = player.getField(field).getCrops().get(0).getValues();
		ArrayList<Card> temp = new ArrayList<Card>();

		//Consider the gold coin award based on the number of crops
		for(int i = 3; i >= 0; i--) {
			if (values[i]==cropAmount) {
				coins = i + 1;
			}
			else if (values[i] != 0 && cropAmount % values[i] == 0) {
				coins = (i+1) *(cropAmount / values[i]);
			}
		}
		//Add coins to treasury (cards disguised as coins)
		player.setTreasuryArea(player.getTreasuryArea() + coins);
		System.out.println("You now have " + player.getTreasuryArea() + " gold coins in your treasury.");

		//Place remaining cards in discard pile
		for (int i = 0; i < cropAmount - coins; i++) {
			//add card to discard pile
			Bohnanza.getDiscard().addCardToDiscard(player.getField(field).getCrops().get(0));
			//remove card from field
			temp = player.getField(field).getCrops();
			temp.remove(0);
			player.getField(field).setCrops(temp);
		}

		player.setField(new Field(), field);
		Bohnanza.setGui(Bohnanza.getGui().setField(player.getField(field)));;

	}

	void plant(ArrayList<Card> temp) {

		for (int i = 0; i < temp.size(); i++) {
			playerNum++;
			System.out.println("PLAYER " + playerNum);
			playerNum--;
			System.out.println("Card: " + temp.get(i));
			int field = 0; //variable to hold which field player wishes to plant/harvest in

			System.out.println("You have two fields " + player.getField(1) + " and " + player.getField(2));
			//check if fields occupied
			if (player.getField(1).getQuantity() > 0 || player.getField(2).getQuantity() > 0) {	
				System.out.println("Do you wish to harvest? (y/n)");

				if (JOptionPane.showInputDialog("Do you wish to harvest? (y/n)").equals("y")) {
					//if both fields can be harvested, give them a choice
					if (player.getField(1).getQuantity() > 0 && player.getField(2).getQuantity() > 0) {
						System.out.println("Which field? (1 or 2)");
						harvest(Integer.parseInt(JOptionPane.showInputDialog("Which field? (1 or 2)")));
					}
					//if only field 1 can be harvested
					else if (player.getField(1).getQuantity() > 0)
						harvest(1);
					//if only field 2 can be harvested
					else if (player.getField(2).getQuantity() > 0)
						harvest(2);
				}
			}
			//if type of both field 1 and 2 are the same as the card, or both fields are empty
			if (player.getField(1).getType().equals(temp.get(0).getType())
					&& player.getField(2).getType().equals(temp.get(0).getType())
					|| player.getField(1).getType().equals("new")
					&& player.getField(2).getType().equals("new")) { //TO DO add condition for crop amount 1 or less

				System.out.println("Your card can be planted in both of your fields.");
				System.out.println("Do you wish to plant it in the field with " + 
						player.getField(1).getQuantity() + " crops or the field with " + 
						player.getField(1).getQuantity() + " crops? (1 or 2)");

				field = Integer.parseInt(JOptionPane.showInputDialog("Do you wish to plant it in the field with " + 
						player.getField(1).getQuantity() + " crops or the field with " + 
						player.getField(1).getQuantity() + " crops? (1 or 2)"));

			}
			//if field 1 is same type as card or is empty
			else if(player.getField(1).getType().equals(temp.get(0).getType())
					|| player.getField(1).getType().equals("new")) {

				System.out.println("Currently, you can only plant in field 1, "
						+ "do you wish to harvest and plant the card in the other field? (y/n)");

				if (JOptionPane.showInputDialog("Currently, you can only plant in field 1, "
						+ "do you wish to harvest and plant the card in the other field? (y/n)").equals("y")) {

					field = 2;
					harvest(field);

				}
				else {
					field = 1;
					System.out.println("You will now plant in field 1");
				}

			}
			//if field 2 is the same type as the card or is empty
			else if(player.getField(2).getType().equals(temp.get(0).getType())
					|| player.getField(2).getType().equals("new")) {

				System.out.println("Currently, you can only plant in field 2, "
						+ "do you wish to harvest and plant the card in the other field? (y/n)");

				if (JOptionPane.showInputDialog("Currently, you can only plant in field 2, "
						+ "do you wish to harvest and plant the card in the other field? (y/n)").equals("y")) {

					field = 1;
					harvest(field);

				}
				else {
					field = 2;
					System.out.println("You will now plant in field 2");
				}

			}
			else { //both fields have different types

				System.out.println("Both cannot be planted unless you harvest.");
				System.out.println("Field 1: " + player.getField(1));
				System.out.println("Field 2: " + player.getField(2));

				System.out.println("Which field do you wish to harvest and plant in? (1 or 2)");
				field = Integer.parseInt(JOptionPane.showInputDialog("Which field do you wish to harvest and plant in? (1 or 2)"));

				harvest(field);

			}

			player.getField(field).addToField(temp.get(0)); //add to field will 
			//be able to detect if field 
			//is empty or not and follow
			//the correct procedure
			Bohnanza.setGui(Bohnanza.getGui().setField(player.getField(field)));
			temp.remove(0);

		}

	}

}