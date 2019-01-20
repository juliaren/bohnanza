import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JOptionPane;

/*
 * @author Aalea
 * 
 * Object that represents the decision making of Player 2
 */
public class Player2AI implements AIInterface{

	/*
	 * parallel arrays to hold data of cards
	 */
	private String[] cardType;
	private int[] cardTypeQuantity;
	private double[] cardValues;

	Card[] mustCollect;
	int[] indexOfMustCollect;
	Card[] worstCards;
	int[] indexWorstCards;

	Player player;
	OtherPlayersTrading opt;

	private int deckDepletionTracker;

	ArrayList<Card> cards;

	public Player2AI() {

		setupCardData();
		setupCardValues();

	}

	private void setupCardValues() {

		int[] values = new int[4];

		for (int c = 0; c < cardType.length; c++) {

			values = cards.get(c).getValues();

			cardValues[c] = 
					(1 / values[0] + 2 / values[1] + 3 / values[2] + 4 / values[3]) / 4;

		}

	}

	private void setupCardData() {

		//initialize deck array
		cards = new ArrayList<Card>();

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

				//add cards 
				cards.add(new Card(type, quantity, values));


			}
			input.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("File not found, check directory");
		}

		for (int i = 0; i < cards.size(); i++) {

			cardType[i] = cards.get(i).getType();
			cardTypeQuantity[i] = cards.get(i).getQuantity();

		}

		deckDepletionTracker = Bohnanza.getDeck().getDepletionCounter();

	}

	public Player start(Player player) {

		this.player = player;

		updateCardData();
		determineCardToCollect();

		plant();

		BohnanzaGui tempGui = Bohnanza.getGui().setPlayerCards(1);
		Bohnanza.setGui(tempGui);

		trade();

		tempGui.setPlayerCards(1);
		tempGui.tradingFieldSetup(1);
		Bohnanza.setGui(tempGui);

		//at the end of turn, draw 2 cards
		ArrayList<Card> temp = new ArrayList<Card>();

		for(int i = 0; i <= 2; i++)
			temp.add(Deck.takeCardFromDeck());

		player.setHand(temp);

		tempGui.setPlayerCards(1);
		Bohnanza.setGui(tempGui);

		return player;
	}

	/*
	 * 2. Append to card data
	 * 2.1. Take count of each player's field
	 * 	2.1.1. Get card type
		2.1.2. Get card quantity
		2.1.3. Add to data
	 */
	private void updateCardData() {

		if (deckDepletionTracker != Bohnanza.getDeck().getDepletionCounter()) {
			cardTypeQuantity = new int[cardType.length];

			for (int i = 0; i < cards.size(); i++) {

				cardType[i] = cards.get(i).getType();
				cardTypeQuantity[i] = cards.get(i).getQuantity();

			}

			deckDepletionTracker = Bohnanza.getDeck().getDepletionCounter();

		}

		Player[] players = Bohnanza.getPlayers();

		for (int j = 0; j < cards.size(); j++) {

			for (int n = 0; n < players.length; n++) {

				if (players[n].getField(1).getType().equals(cardType[j])) {

					cardTypeQuantity[j] = cardTypeQuantity[j] - players[n].getField(1).getQuantity();

				}
				else if (players[n].getField(2).getType().equals(cardType[j])) {

					cardTypeQuantity[j] = cardTypeQuantity[j] - players[n].getField(2).getQuantity();

				}

			}

		}

	}

	@Override
	public void plant() {

		int field = 0;

		//1. Plants least recently received card
		//1.1. If both fields empty, plant in field 1
		if (player.getField(1).getType().equals(player.getHand().get(0).getType())
				&& player.getField(2).getType().equals(player.getHand().get(0).getType())
				|| player.getField(1).getType().equals("new")
				&& player.getField(2).getType().equals("new")) {
			field = 1;
		}
		//1.2. If one field of same kind, plant in that field
		else if(player.getField(1).getType().equals(player.getHand().get(0).getType())
				|| player.getField(1).getType().equals("new")) {	
			field = 1;
		}
		else if(player.getField(2).getType().equals(player.getHand().get(0).getType())
				|| player.getField(2).getType().equals("new")) {
			field = 2;
		}
		//1.3. If no fields of same kind
		else {
			int[] differenceFromCardData = new int[2];
			//1.3.1. Search types of both fields in card data
			for (int i = 0; i < cardType.length; i++) {
				if (cardType[i].equals(player.getField(1).getType()))
					differenceFromCardData[0] = cardTypeQuantity[i] - player.getField(1).getQuantity();
				else if (cardType[i].equals(player.getField(2).getType()))
					differenceFromCardData[1] = cardTypeQuantity[i] - player.getField(2).getQuantity();

			}
			//1.3.2. Plant in field with less occurences in data
			if (differenceFromCardData[0] > differenceFromCardData[1])
				field = 1;
			else
				field = 2;

			harvest(field);

		}

		player.getField(field).addToField(player.getHand().get(0)); //add to field

		ArrayList<Card> temp = player.getHand();
		Bohnanza.getDiscard().addCardToDiscard(temp.get(0));
		temp.remove(0);
		player.setHand(temp); //send card to discard

		Bohnanza.setGui(Bohnanza.getGui().setField(player.getField(field)));
		Bohnanza.setGui(Bohnanza.getGui().cardQuantityLabels(1));
	}

	@Override
	public void harvest(int field) {

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
		Bohnanza.setGui(Bohnanza.getGui().setField(player.getField(field)));

	}

	@Override
	public void trade() {

		opt = new OtherPlayersTrading();

		//object to generate the rest of a card just from user's input of type
		FindCard cardFinder = new FindCard();
		//create temporary array to hold the cards the active player wish to trade for
		ArrayList<Card> wishCards = new ArrayList<Card>();
		//create temporary array to hold the cards the active player is currently trading with
		ArrayList<Card> currentTradeCard = new ArrayList<Card>();
		//to hold how many times for loop should repeat asking player for current trade card
		int loop;
		boolean exitTrade = false;
		//check if other player agree to trade
		boolean[] yes = new boolean[Bohnanza.getPlayers().length];
		int yesCounter = 0;

		//to hold type of card current active player wishes to receive
		String wishType;

		//draw 2 card from deck and add to trading area
		for(int i = 0; i < 2; i++)
			player.getTradingArea().add(Deck.takeCardFromDeck());

		BohnanzaGui tempGui = Bohnanza.getGui().tradingFieldSetup(1);
		Bohnanza.setGui(tempGui);

		//if one of the cards in trading area are part of the most valuable list, just plant them both
		for (int x = 0; x < mustCollect.length; x++) {
			if(player.getTradingArea().get(0).getType().equals(mustCollect[x].getType())) {
				Bohnanza.getPlay().plant(player.getTradingArea());
				exitTrade = true;
			}
		}
		//if player wants to trade, start trading process
		while (exitTrade = true) {

			//add card player selected to current trade card, remove from trading area
			for (int index = 0; index < player.getTradingArea().size(); index++) {
				currentTradeCard.add(player.getTradingArea().get(index));
			}

			for (int index = 0; index < player.getHand().size(); index++) {
				if (player.getHand().get(0).getType().equals(worstCards[0].getType())) {
					currentTradeCard.add(player.getHand().get(index));
					player.getHand().remove(index);
				}
				if (player.getHand().get(0).getType().equals(worstCards[1].getType())) {
					currentTradeCard.add(player.getHand().get(index));
					player.getHand().remove(index);
				}
				if (player.getHand().get(0).getType().equals(worstCards[2].getType())) {
					currentTradeCard.add(player.getHand().get(index));
					player.getHand().remove(index);
				}
			}	

			//loop through all other players to ask if they want to trade with active player
			for (int i = 1; i <= Bohnanza.getPlayers().length; i++){
				if (i == 1) {
					System.out.println("PLAYER " + i);
					System.out.println("the cards you have in hand are " +
							Arrays.toString(Bohnanza.players[i-1].getHand().toArray()));
					System.out.println("Do you wish to trade " + Arrays.toString(wishCards.toArray()) + 
							" with the current active player? (y/n)");

					if (JOptionPane.showInputDialog("Enter reply").equals("y")){
						yes[i - 1] = true;
						yesCounter++;
					}
				}
				else if (i == 3) {
					yes[i - 1] = Bohnanza.getP3().tradeConsent(Arrays.toString(mustCollect));
					if (yes[i-1])
						yesCounter++;
				}
				else if (i == 4) {
					yes[i - 1] = Bohnanza.getP4().tradeConsent(Arrays.toString(mustCollect));
					if (yes[i-1])
						yesCounter++;
				}
			}

			//if all other player do not agree to trade
			//ask current active player to end trade 
			if (yesCounter == 0){

				player.getTradingArea().addAll(currentTradeCard);
				currentTradeCard = new ArrayList<Card>();
				break;
			}//if only one player wants to trade, trade
			else if (yesCounter == 1){

				for (int tradingPlayerNum = 1; tradingPlayerNum <= Bohnanza.getPlayers().length; tradingPlayerNum++){
					if(yes[tradingPlayerNum - 1] == true){
						System.out.println("PLAYER " + tradingPlayerNum);
						swapCards(tradingPlayerNum - 1, currentTradeCard, mustCollect, player);
					}
				}
			} else if (yesCounter > 1){	
				//if more than 1 player want to trade with active player
				//ask active player who they want to trade with
				for (int tradingPlayerNum = 1; tradingPlayerNum <= Bohnanza.getPlayers().length; tradingPlayerNum++){

					if(yes[tradingPlayerNum - 1] == true)
						swapCards(tradingPlayerNum-1, currentTradeCard, mustCollect, player);
					
				}

			}

				break;

	}

	//if trading has ended, plant everything in trading area
	//including all cards received from trading and original 2 cards from deck
	//if any of the 2 cards have not been traded
	Bohnanza.getPlay().plant(player.getTradingArea());

}

private void swapCards(int pNum, ArrayList<Card> currentTradeCard, 
		Card[] mustCollect, Player player){

	//add active player's card to other player's trading area
	Bohnanza.players[pNum].getTradingArea().addAll(currentTradeCard);

	//let other player plant the received card from trading
	//need to plant in other player's field
	opt.plantOtherPlayer(pNum, Bohnanza.players[pNum].getTradingArea());	

	//add wish card to active player's trading area
	for (int i = 0; i < Bohnanza.players[pNum].getHand().size(); i++) {
		//remove wish card from other player's hands if available
		for (int g = 0; g < mustCollect.length; g++) {
			if (Bohnanza.players[pNum].getHand().get(i).getType().equals(mustCollect[g].getType())) {
				Bohnanza.players[pNum].getHand().remove(i);
				player.getTradingArea().add(mustCollect[g]);
			}
		}
	}

}

private void determineCardToCollect() {

	//3. Determine which card to collect
	mustCollect = new Card[3];
	indexOfMustCollect = new int[] {0, 0, 0};
	//3.1. Compare card ratios chart with card quantity data
	for (int c = 0; c < cardType.length; c++) {
		//if current card has a higher value than the card in 1st place
		if (cardValues[c] > cardValues[indexOfMustCollect[0]]) {
			mustCollect[0] = cards.get(c);
			indexOfMustCollect[0] = c;
		}
		//if current card has a higher value than the card in 2nd place
		else if (cardValues[c] > cardValues[indexOfMustCollect[1]]) {
			mustCollect[1] = cards.get(c);
			indexOfMustCollect[1] = c;
		}
		//if current card has a higher value than the card in 3rd place
		else if (cardValues[c] > cardValues[indexOfMustCollect[2]]) {
			mustCollect[2] = cards.get(c);
			indexOfMustCollect[2] = c;
		}
	}
	//3.2. Determine the 3 best cards to collect
	//decide between top two by seeing which one has highest quantity
	if (cardTypeQuantity[indexOfMustCollect[1]] > cardTypeQuantity[indexOfMustCollect[0]]) {

		int tempIndex = indexOfMustCollect[0];
		indexOfMustCollect[0] = indexOfMustCollect[1];
		indexOfMustCollect[1] = tempIndex;

		Card tempCard = mustCollect[0];
		mustCollect[0] = mustCollect[1];
		mustCollect[1] = tempCard;

	}

	//3.3. Determine 3 worst cards to collect
	worstCards = new Card[3];
	indexWorstCards = new int[] {0, 0, 0};
	//3.1. Compare card ratios chart with card quantity data
	for (int c = 0; c < cardType.length; c++) {
		//if current card has a lower value than the card in 1st place
		if (cardValues[c] < cardValues[indexWorstCards[0]]) {
			worstCards[0] = cards.get(c);
			indexWorstCards[0] = c;
		}
		//if current card has a lower value than the card in 2nd place
		else if (cardValues[c] < cardValues[indexWorstCards[1]]) {
			worstCards[1] = cards.get(c);
			indexWorstCards[1] = c;
		}
		//if current card has a lower value than the card in 3rd place
		else if (cardValues[c] < cardValues[indexWorstCards[2]]) {
			worstCards[2] = cards.get(c);
			indexWorstCards[2] = c;
		}
	}

}

@Override
public static boolean tradeConsent(ArrayList<Card> wishCards) {

	boolean answer = true;

	for (int i = 0; i < cards.size(); i++) {
		if (wishCards.getType().equals(worstCards[i].getType()) ) {
			answer = false;
		}

	}

	return answer;

}

}
