import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.JOptionPane;

/*
 * @author Aalea
 * 
 * This class reads in a table of values and inserts them in an ArrayList of card objects,
 * holds the deck, shuffles the cards, counts how many times the deck is depleted, and 
 * performs actions needed to determine the winner, once the depletion counter reaches 3
 */
public class Deck {
	//public static void main (String[] args) {
	
	/*
	 * Array that holds the cards in the deck
	 */
	private static ArrayList<Card> deck;
	
	/*
	 * Counts the number of times the deck runs out
	 */
	private static int depletionCounter;
	
	/*
	 * Constructor method that 
	 */
	public Deck() {
		
		//initialize deck array
		deck = new ArrayList<Card>();
		
		//create temp variables to use when reading in card info
		String type = "";
		int quantity = 0;
		int[] values = new int[4];
		
		try {
			//open up card info file
			Scanner input = new Scanner (new File("cardInfo.txt"));
			input.useDelimiter(",");
			
			//loop that 
			for (int i = 1; i <= 7; i++) {
			//while (input.hasNext()) {
				
				//gets info from file and places in temp variable
				quantity = input.nextInt();
				type = input.next();

				//stores reward info for card harvesting
				values[0] = input.nextInt();
				values[1] = input.nextInt();
				values[2] = input.nextInt();
				values[3] = input.nextInt();
				
				//add cards to deck based on quantity
				for (int x = 0; x <= quantity; x++) {
					deck.add(new Card(type, quantity, values));
				}
				
			}
			input.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("File not found, check directory");
		}
		
		//System.out.println(Arrays.toString(deck.toArray()));
			
		shuffle();
		
	}

	@Override
	public String toString() {
		return "Deck [deck=" + deck + ", depletionCounter=" + depletionCounter + "]";
	}

	public ArrayList<Card> getDeck() {
		return deck;
	}

	public void setDeck(ArrayList<Card> deck) {
		this.deck = deck;
	}

	public static int getDepletionCounter() {
		return depletionCounter;
	}

	public void setDepletionCounter(int depletionCounter) {
		this.depletionCounter = depletionCounter;
	}
	
	/*
	 * this method adds a single card to the deck
	 */
	public void addCardToDeck(Card card) {
		deck.add(card);
	}
	
	/*
	 * this method draws a card from the deck and sends it off to whoever called it
	 */
	public static Card takeCardFromDeck() {
		if (deck.size() == 0) {
			checkWin(Bohnanza.getPlayers());
			//save card at index 0
			Card card = deck.get(0);
			//apparently the remove method not only removes an element, but also shifts down
			//the elements so there won't be an empty element at that index
			deck.remove(0);
			//send card to whoever called this method
			return card;
		}
		else {
			//save card at index 0
			Card card = deck.get(0);
			//apparently the remove method not only removes an element, but also shifts down
			//the elements so there won't be an empty element at that index
			deck.remove(0);
			//send card to whoever called this method
			return card;
		}
	}

	public void shuffle() {

		Collections.shuffle(deck);
		//System.out.println(Arrays.toString(deck.toArray()));
		
	}
	
	public static void checkWin(Player[] players) {
		
		if (depletionCounter==3)
			win(players);
		
	}
	
	/*
	* this method does the procedure to determine who wins
	*/
	private static void win (Player[] players) {
		
		Play play = new Play();
		//iterate through all players
		for (int g = 0; g <= 4; g++) {
			//make each player harvest everything remaining in their field
			for (int i = 0; i < players[g].getField().length; i++)
				Play.harvest(i);
		}
		
		Player winner = null;
		//check who has most in treasury
		for (int g = 0; g < 4; g++) {
			//if treasury of player is greater than last player, make them winner
			//or if same amount of coins, player who is farthest away from 1st player wins
			if (players[g].getTreasuryArea() >= winner.getTreasuryArea())
				winner = players[g];	
		}
		
		JOptionPane.showMessageDialog(null, "The winner is Player " + winner);
		
		
	}

}