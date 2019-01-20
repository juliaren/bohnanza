import java.util.ArrayList;
import java.util.Scanner;

/*
 * @author Julia
 * 
 * This class plants and harvest other player's crops within the current active players turn due to trading
 * after other player trade with current active player, they have to plant the cards they traded with 
 * the current active player and this process may involve harvesting their current crops in field
 */

public class OtherPlayersTrading{

	Scanner input;

	void plantOtherPlayer(int playerNum, ArrayList<Card> tradingArea) {
		
		input = new Scanner(System.in);
		
		int field = 0; //variable to hold which field player wishes to plant/harvest in
		System.out.println("PLAYER " + playerNum + 1);
		System.out.println("You have two fields " + Bohnanza.players[playerNum].getField(1) + " and " 
							+ Bohnanza.players[playerNum].getField(2));
		//check if fields occupied
		if (Bohnanza.players[playerNum].getField(1).getQuantity() > 0 || Bohnanza.players[playerNum].getField(2).getQuantity() > 0) {	
			System.out.println("Do you wish to harvest? (y/n)");

			if (input.next().equals("y")) {
				//if both fields can be harvested, give them a choice
				if (Bohnanza.players[playerNum].getField(1).getQuantity() > 0 && Bohnanza.players[playerNum].getField(2).getQuantity() > 0) {
					System.out.println("Which field? (1 or 2)");
					harvestOtherPlayer(playerNum, input.nextInt());
				}
				//if only field 1 can be harvested
				else if (Bohnanza.players[playerNum].getField(1).getQuantity() > 0)
					harvestOtherPlayer(playerNum, 1);
				//if only field 2 can be harvested
				else if (Bohnanza.players[playerNum].getField(2).getQuantity() > 0)
					harvestOtherPlayer(playerNum, 2);
			}
		}
		//if type of both field 1 and 2 are the same as the card, or both fields are empty
		if (Bohnanza.players[playerNum].getField(1).getType().equals(Bohnanza.players[playerNum].getHand().get(0).getType())
				&& Bohnanza.players[playerNum].getField(2).getType().equals(Bohnanza.players[playerNum].getHand().get(0).getType())
				|| Bohnanza.players[playerNum].getField(1).getType().equals("new")
				&& Bohnanza.players[playerNum].getField(2).getType().equals("new")) { //TO DO add condition for crop amount 1 or less

			System.out.println("Your card can be planted in both of your fields.");
			System.out.println("Do you wish to plant it in the field with " + 
					Bohnanza.players[playerNum].getField(1).getQuantity() + " crops or the field with " + 
					Bohnanza.players[playerNum].getField(1).getQuantity() + " crops? (1 or 2)");

			field = input.nextInt();

		}
		//if field 1 is same type as card or is empty
		else if(Bohnanza.players[playerNum].getField(1).getType().equals(Bohnanza.players[playerNum].getHand().get(0).getType())
				|| Bohnanza.players[playerNum].getField(1).getType().equals("new")) {

			System.out.println("Currently, you can only plant in field 1, "
					+ "do you wish to harvest and plant the card in the other field? (y/n)");

			if (input.next().equals("y")) {

				field = 2;
				harvestOtherPlayer(playerNum, field);

			}
			else {
				field = 1;
				System.out.println("You will now plant in field 1");
			}

		}
		//if field 2 is the same type as the card or is empty
		else if(Bohnanza.players[playerNum].getField(2).getType().equals(Bohnanza.players[playerNum].getHand().get(0).getType())
				|| Bohnanza.players[playerNum].getField(2).getType().equals("new")) {

			System.out.println("Currently, you can only plant in field 2, "
					+ "do you wish to harvest and plant the card in the other field? (y/n)");

			if (input.next().equals("y")) {

				field = 1;
				harvestOtherPlayer(playerNum, field);

			}
			else {
				field = 2;
				System.out.println("You will now plant in field 2");
			}

		}
		else { //both fields have different types

			System.out.println("Both cannot be planted unless you harvest.");
			System.out.println("Field 1: " + Bohnanza.players[playerNum].getField(1));
			System.out.println("Field 2: " + Bohnanza.players[playerNum].getField(2));

			System.out.println("Which field do you wish to harvest and plant in? (1 or 2)");
			field = input.nextInt();

			harvestOtherPlayer(playerNum, field);

		}

		Bohnanza.players[playerNum].getField(field).addToField(Bohnanza.players[playerNum].getHand().get(0)); //add to field will 
		//be able to detect if field 
		//is empty or not and follow
		//the correct procedure

		
		Bohnanza.getGui().setField(Bohnanza.players[playerNum].getField(field));
		tradingArea = Bohnanza.players[playerNum].getHand();
		Bohnanza.getDiscard().addCardToDiscard(tradingArea.get(0));
		tradingArea.remove(0);
		Bohnanza.players[playerNum].setHand(tradingArea); //send card to discard

		
	}

	private void harvestOtherPlayer(int playerNum, int field) {

		input = new Scanner(System.in);
		
		//Count the number of crops of bean
		int cropAmount = Bohnanza.players[playerNum].getField(field).getQuantity();
		int coins = 0;
		int[] values = Bohnanza.players[playerNum].getField(field).getCrops().get(0).getValues();
		ArrayList<Card> temp = new ArrayList<Card>();

		//Consider the gold coin award based on the number of crops
		for(int i = 3; i >= 0; i++) {
			if (values[i]==cropAmount) {
				coins = i + 1;
			}
			else if (cropAmount % values[i] == 0) {
				coins = (i+1) *(cropAmount / values[i]);
			}
		}
		//Add coins to treasury (cards disguised as coins)
		Bohnanza.players[playerNum].setTreasuryArea(Bohnanza.players[playerNum].getTreasuryArea() + coins);
		System.out.println("You now have " + Bohnanza.players[playerNum].getTreasuryArea() + " gold coins in your treasury.");

		//Place remaining cards in discard pile
		for (int i = 0; i < cropAmount - coins; i++) {
			//add card to discard pile
			Bohnanza.getDiscard().addCardToDiscard(Bohnanza.players[playerNum].getField(field).getCrops().get(0));
			//remove card from field
			temp = Bohnanza.players[playerNum].getField(field).getCrops();
			temp.remove(0);
			Bohnanza.players[playerNum].getField(field).setCrops(temp);
		}

		Bohnanza.players[playerNum].setField(new Field(), field);
		Bohnanza.getGui().setField(Bohnanza.players[playerNum].getField(field));

	}

}
