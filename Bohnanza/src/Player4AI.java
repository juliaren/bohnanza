import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

/*
 * @author Julia
 * 
 * Object that represents the decision making of Player 4
 * 
 */
public class Player4AI implements AIInterface{

	private Player player;
	private OtherPlayersTrading opt;

	//object to generate the rest of a card just from user's input of type
	private FindCard cardFinder = new FindCard();

	//hold number of cards of one type in discard and planted in other player's fields
	private int used = 0;

	//hold rare cards
	private Card[] rare = new Card[3];

	//method to start off each turn
	public Player start(Player player) {
		this.player = player;

		//define rare cards
		rare[0] = cardFinder.start("gardenbean");
		rare[1] = cardFinder.start("redbean");
		rare[2] = cardFinder.start("black-eyedbean");

		//draw 2 cards from deck
		ArrayList<Card> temp = new ArrayList<Card>();

		for(int i = 0; i <= 2; i++)
			temp.add(Deck.takeCardFromDeck());

		//add card newly drew to hand
		player.getHand().addAll(temp);

		//1. plant 1st(dealt earliest) card in hand
		plant();

		//2. decide whether to plant a 2nd card(by now 2nd card becomes the 1st card in hand)

		//2.1. if 2nd card is the same type as existing cards in field or the field is empty, plant to that field
		if(player.getHand().get(0).getType() == player.getField(0).getType() || 
				player.getField(0).getQuantity() == 0)
			plant(0);
		else if (player.getHand().get(0).getType() == player.getField(1).getType() || 
				player.getField(1).getQuantity() == 0)
			plant(1);

		//2.2. if both fields are already occupied
		else if(player.getField(0).getQuantity() != 0 && player.getField(1).getQuantity() != 0){

			//2.2.1. if there is another card of the 2nd card type in hand (if last index is not the same as first)
			//2nd card should be planted but need to decide which field to harvest
			if(player.getHand().lastIndexOf(player.getHand().get(0)) != 0){

				//2.2.2. decide which field to harvest
				//2.2.2.1. if card types in a field does not exist in hand
				if (player.getHand().contains(player.getField(0).getType()) == false ||
						player.getHand().contains(player.getField(1).getType()) == false){

					//2.2.2.1.1. if card is rare and game is NOT during the beginning phase
					if(Deck.getDepletionCounter() >= 2){
						//harvest field with most rare card
						//identifies 3 types as rare cards with scarcity level from most rare to least:
						//most rare - garden bean

						for(int f = 0; f <= player.getField().length; f++){

							for (int i = 0; i <= rare.length; i ++){
								if (player.getField(f).getType() == rare[i].getType()){
									harvest(f);
									plant(f);
								}
							}
						}
					}

					//if non of the above conditions are met, do not plant 2nd card
					//start trading process
					trade();

					//draw 3 cards at end of turn
					for(int i = 0; i <= 3; i++)
						temp.add(Deck.takeCardFromDeck());

					//add card newly drew to hand
					player.getHand().addAll(temp);

				}
			}
		}

		return player;
	}

	//plant first card in hand
	@Override
	public void plant() {

		//if card is the same type as existing cards in field, plant to that field
		if(player.getHand().get(0).getType() == player.getField(0).getType()){
			//plant card to field
			player.getField(0).addToField(player.getHand().get(0));
			//remove card from hand
			player.getHand().remove(0);
		}
		else if (player.getHand().get(0).getType() == player.getField(1).getType()){
			//plant card to field
			player.getField(1).addToField(player.getHand().get(0));
			//remove card from hand
			player.getHand().remove(0);
		}
		//if first field is empty, plant in first field
		else if(player.getField(0).getQuantity() == 0){
			//plant card to field
			player.getField(0).addToField(player.getHand().get(0));
			//remove card from hand
			player.getHand().remove(0);
		}

		//if second field is empty, plant in second field
		else if(player.getField(1).getQuantity() == 0){
			//plant card to field
			player.getField(1).addToField(player.getHand().get(0));
			//remove card from hand
			player.getHand().remove(0);
		}

		//if both fields are occupied
		else{

			//if both fields and card that needs to be planted are different types
			//if card is rare and game is during the final stages
			if(Deck.getDepletionCounter() >= 2){
				//harvest field with most rare card
				//identifies 3 types as rare cards with scarcity level from most rare to least:
				//most rare - garden bean

				for(int f = 0; f <= player.getField().length; f++){

					for (int i = 0; i <= rare.length; i ++){
						if (player.getField(f).getType() == rare[i].getType()){
							harvest(f);
							plant(f);
						}
					}
				}
			}
			//if game is NOT during final stages
			else{
				//harvest and plant in field with less cards(when both fields have more than one card)
				if(player.getField(0).getQuantity() > 1 && player.getField(1).getQuantity() > 1){
					if(player.getField(0).getQuantity() > player.getField(1).getQuantity()){
						harvest(1);
						//plant card to field
						player.getField(1).addToField(player.getHand().get(0));
						//remove card from hand
						player.getHand().remove(0);
					}else{
						harvest(0);
						//plant card to field
						player.getField(0).addToField(player.getHand().get(0));
						//remove card from hand
						player.getHand().remove(0);
					}
				}
				//if one field only has one card, harvest and plant in other field
				else if (player.getField(0).getQuantity() > 1){
					harvest(0);
					//plant card to field
					player.getField(0).addToField(player.getHand().get(0));
					//remove card from hand
					player.getHand().remove(0);
				}else if(player.getField(1).getQuantity() > 1){
					harvest(1);
					//plant card to field
					player.getField(1).addToField(player.getHand().get(0));
					//remove card from hand
					player.getHand().remove(0);
				}
				//if both fields contain same number of cards
				else if (player.getField(0).getQuantity() == player.getField(1).getQuantity()){
					//harvest the type that already occurred in other player's fields
					for(int i = 0; i <= 4; i ++){
						if(player.getField(0).getType() == Bohnanza.players[i].getField(0).getType()
								|| player.getField(0).getType() == Bohnanza.players[i].getField(1).getType()){
							harvest(0);
							//plant card to field
							player.getField(0).addToField(player.getHand().get(0));
							//remove card from hand
							player.getHand().remove(0);
						}else if(player.getField(1).getType() == Bohnanza.players[i].getField(0).getType()
								|| player.getField(1).getType() == Bohnanza.players[i].getField(1).getType()){
							harvest(1);
							//plant card to field
							player.getField(1).addToField(player.getHand().get(0));
							//remove card from hand
							player.getHand().remove(0);
						}
					}
					//if both fields contain card types that no other player has planted
				}else{
					//harvest the field with greater coin value
					//greater coin value means the type is going to become more rare(player already
					//has many of the cards)
					int cropAmount = player.getField(0).getQuantity();
					int[] values = player.getField(0).getCrops().get(0).getValues();
					int f1Coins = 0;

					for(int i = 3; i >= 0; i--) {
						if (values[i]==cropAmount) {
							f1Coins = i + 1;
						}
						else if (values[i] != 0 && cropAmount % values[i] == 0) {
							f1Coins = (i+1) *(cropAmount / values[i]);
						}
					}
					cropAmount = player.getField(1).getQuantity();
					values = player.getField(1).getCrops().get(0).getValues();
					int f2Coins = 0;
					for(int i = 3; i >= 0; i--) {
						if (values[i]==cropAmount) {
							f2Coins = i + 1;
						}
						else if (values[i] != 0 && cropAmount % values[i] == 0) {
							f2Coins = (i+1) *(cropAmount / values[i]);
						}
					}

					//if field 1 has greater coin value, harvest and plant in field 1
					//if equal coin value, then just plant in field 1 blindly
					if(f1Coins > f2Coins || f1Coins == f2Coins){
						harvest(0);
						//plant card to field
						player.getField(0).addToField(player.getHand().get(0));
						//remove card from hand
						player.getHand().remove(0);

						//if field 2 has greater coin value, harvest and plant in field 2
					}else if(f1Coins < f2Coins){
						harvest(1);
						//plant card to field
						player.getField(1).addToField(player.getHand().get(0));
						//remove card from hand
						player.getHand().remove(0);
					}
				}
			}
		}
	}

	//plant when specified which field to plant to
	public void plant(int field){
		//plant card to field
		player.getField(field).addToField(player.getHand().get(0));
		//remove card from hand
		player.getHand().remove(0);
	}

	//plant when specified which cards to plant(from trading)
	public void plant(Card temp){

		//if first field is empty, plant in first field
		if(player.getField(0).getQuantity() == 0){
			//plant card to field
			player.getField(0).addToField(temp);
			//remove card from hand
			player.getTradingArea().remove(temp);
		}

		//if second field is empty, plant in second field
		else if(player.getField(1).getQuantity() == 0){
			//plant card to field
			player.getField(1).addToField(temp);
			//remove card from hand
			player.getTradingArea().remove(temp);
		}

		//if both fields are occupied
		else{

			//if card is the same type as existing cards in field, plant to that field
			if(player.getHand().get(0).getType() == player.getField(0).getType()){
				//plant card to field
				player.getField(0).addToField(temp);
				//remove card from hand
				player.getTradingArea().remove(temp);
			}
			else if (player.getHand().get(0).getType() == player.getField(1).getType()){
				//plant card to field
				player.getField(1).addToField(temp);
				//remove card from hand
				player.getTradingArea().remove(temp);
			}
			//if both fields and card that needs to be planted are different types
			else{
				//if card is rare and game is during the final stages
				//2.2.2.1.1. if card is rare and game is NOT during the beginning phase
				if(Deck.getDepletionCounter() >= 2){
					//harvest field with most rare card
					//identifies 3 types as rare cards with scarcity level from most rare to least:
					//most rare - garden bean

					for(int f = 0; f <= player.getField().length; f++){

						for (int i = 0; i <= rare.length; i ++){
							if (player.getField(f).getType() == rare[i].getType()){
								harvest(f);
								plant(f);
							}
						}
					}
				}
				//if game is NOT during final stages
				else{
					//harvest and plant in field with less cards(when both fields have more than one card)
					if(player.getField(0).getQuantity() > 1 && player.getField(1).getQuantity() > 1){
						if(player.getField(0).getQuantity() > player.getField(1).getQuantity()){
							harvest(1);
							//plant card to field
							player.getField(1).addToField(temp);
							//remove card from hand
							player.getTradingArea().remove(temp);
						}else{
							harvest(0);
							//plant card to field
							player.getField(0).addToField(temp);
							//remove card from hand
							player.getTradingArea().remove(temp);
						}
					}
					//if one field only has one card, harvest and plant in other field
					else if (player.getField(0).getQuantity() > 1){
						harvest(0);
						//plant card to field
						player.getField(0).addToField(temp);
						//remove card from hand
						player.getTradingArea().remove(temp);
					}else if(player.getField(1).getQuantity() > 1){
						harvest(1);
						//plant card to field
						player.getField(1).addToField(temp);
						//remove card from hand
						player.getTradingArea().remove(temp);
					}
					//if both fields contain same number of cards
					else if (player.getField(0).getQuantity() == player.getField(1).getQuantity()){
						//harvest the type that already occured in other player's fields
						for(int i = 0; i <= 4; i ++){
							if(player.getField(0).getType() == Bohnanza.players[i].getField(0).getType()
									|| player.getField(0).getType() == Bohnanza.players[i].getField(1).getType()){
								harvest(0);
								//plant card to field
								player.getField(0).addToField(temp);
								//remove card from hand
								player.getTradingArea().remove(temp);
							}else if(player.getField(1).getType() == Bohnanza.players[i].getField(0).getType()
									|| player.getField(1).getType() == Bohnanza.players[i].getField(1).getType()){
								harvest(1);
								//plant card to field
								player.getField(1).addToField(temp);
								//remove card from hand
								player.getTradingArea().remove(temp);
							}
						}
						//if both fields contain card types that no other player has planted
					}else{
						//harvest the field with greater coin value
						//greater coin value means the type is going to become more rare(player already
						//has many of the cards)
						int cropAmount = player.getField(0).getQuantity();
						int[] values = player.getField(0).getCrops().get(0).getValues();
						int f1Coins = 0;

						for(int i = 3; i >= 0; i--) {
							if (values[i]==cropAmount) {
								f1Coins = i + 1;
							}
							else if (values[i] != 0 && cropAmount % values[i] == 0) {
								f1Coins = (i+1) *(cropAmount / values[i]);
							}
						}
						cropAmount = player.getField(1).getQuantity();
						values = player.getField(1).getCrops().get(0).getValues();
						int f2Coins = 0;
						for(int i = 3; i >= 0; i--) {
							if (values[i]==cropAmount) {
								f2Coins = i + 1;
							}
							else if (values[i] != 0 && cropAmount % values[i] == 0) {
								f2Coins = (i+1) *(cropAmount / values[i]);
							}
						}

						//if field 1 has greater coin value, harvest and plant in field 1
						//if equal coin value, then just plant in field 1 blindly
						if(f1Coins > f2Coins || f1Coins == f2Coins){
							harvest(0);
							//plant card to field
							player.getField(0).addToField(temp);
							//remove card from hand
							player.getTradingArea().remove(temp);

							//if field 2 has greater coin value, harvest and plant in field 2
						}else if(f1Coins < f2Coins){
							harvest(1);
							//plant card to field
							player.getField(1).addToField(temp);
							//remove card from hand
							player.getTradingArea().remove(temp);
						}
					}
				}
			}
		}
	}

	//to harvest the cards in a specified field
	//harvest specified field
	//includes awarding corresponding coins and flush card to discard
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

	//to trade with other player
	//determines what to trade
	@Override
	public void trade() {

		opt = new OtherPlayersTrading();

		//create temporary array to hold the cards the active player wish to trade for
		ArrayList<Card> wishCards = new ArrayList<Card>();
		//create temporary array to hold the cards the active player is currently trading with
		ArrayList<Card> currentTradeCard = new ArrayList<Card>();
		//to hold how many times for loop should repeat asking player for current trade card
		int loop;
		//check if other player agree to trade
		boolean[] yes = new boolean[Bohnanza.getPlayers().length];
		int yesCounter = 0;
		//to hold type of card current active player wishes to receive
		String wishType;

		//draw 2 card from deck and add to trading area
		for(int i = 0; i < 2; i++)
			player.getTradingArea().add(Deck.takeCardFromDeck());

		BohnanzaGui tempGui = Bohnanza.getGui().tradingFieldSetup(4);
		Bohnanza.setGui(tempGui);


		//trade from trading area
		for(int i = 0; i < 2; i++){

			//count how many cards of one type is in discard pile
			for(int d = 0; d <= Bohnanza.getDiscard().getDiscard().size(); d++){
				if(player.getTradingArea().get(i).getType() == Bohnanza.getDiscard().getDiscard().get(d).getType())
					used++;
			}

			//count how many cards of one type is already planted in other players' fields
			for(int d = 0; d <= Bohnanza.getPlayers().length; d++){
				//loop each players' field
				for(int f = 0; f <= Bohnanza.players[d].getField().length; f++){
					if(player.getTradingArea().get(i).getType() == Bohnanza.players[d].getField(f).getType())
						used++;
				}
			}

			//if more than half of this card type is already discarded or planted in other player's fields, trade
			if(used >= (player.getTradingArea().get(i).getQuantity() / 2)){
				currentTradeCard.add(player.getTradingArea().get(i));
			}

			//if game is NOT during beginning phase and player has a rare card, trade it out
			else if(Deck.getDepletionCounter() >= 2){
				//trade rare card
				//identifies 3 types as rare cards with scarcity level from most rare to least:

				for(int r = 0; r <= rare.length; r++){
					if(player.getTradingArea().get(i).getType() == rare[r].getType())
						currentTradeCard.add(player.getTradingArea().get(i));

				}
			}

			//if the 2 cards drawn is a type that already exist in field or hand, don't trade and plant directly
			else if(player.getTradingArea().get(i).getType() == player.getField(0).getType() ||
					player.getTradingArea().get(i).getType() == player.getField(1).getType() ||
					player.getHand().contains(player.getTradingArea().get(i).getType()) == true){

				plant(player.getTradingArea().get(i));
				player.getTradingArea().remove(i);
			}

		}
		//start trading process if trading area still has cards
		if(player.getTradingArea().size() != 0){

			//trade cards that still remains in trading area
			currentTradeCard.addAll(player.getTradingArea());

			for(int i = 0; i <= player.getTradingArea().size(); i ++)
				player.getTradingArea().remove(i);


			//trade cards in hand
			for(int i = 0; i <= player.getHand().size(); i ++){

				//trade cards in hand that only has one card of that type and is not a type planted in field
				if(player.getHand().get(i).getType() != player.getField(0).getType() ||
						player.getHand().get(i).getType() != player.getField(1).getType() ||
						player.getHand().indexOf(player.getHand().get(i).getType()) == 
						player.getHand().lastIndexOf(player.getHand().get(i).getType())){

					currentTradeCard.add(player.getHand().get(i));
					player.getHand().remove(i);

				}
			}

			//select the card player wishes to trade for
			//add the card they wish to trade for in the wish card array list

			//look for card types already planted in field or exist in hand
			for (int i = 0; i <= player.getField().length; i++)
				wishCards.add(cardFinder.start(player.getField(i).getType()));

			for(int i = 0; i <= player.getHand().size(); i ++){
				wishCards.add(cardFinder.start(player.getHand().get(i).getType())); 
			}

			//if game is not towards the end, look for rare cards
			if(Deck.getDepletionCounter() <= 2){
				for(int r = 0; r <= rare.length; r++)
					wishCards.add(cardFinder.start(rare[r].getType()));
			}

			//loop through all other players to ask if they want to trade with active player
			for (int i = 1; i <= Bohnanza.getPlayers().length; i++){
				if (i == 1) {
					System.out.println("PLAYER 1");
					System.out.println("the cards you have in hand are " +
							Arrays.toString(Bohnanza.players[i-1].getHand().toArray()));
					System.out.println("Do you wish to trade " + Arrays.toString(wishCards.toArray()) + 
							" with the current active player? (y/n)");

					if (JOptionPane.showInputDialog("Enter reply").equals("y")){
						yes[i - 1] = true;
						yesCounter++;
					}
				}

				if (i == 2){
					if(Player2AI.tradeConsent(wishCards) == true)
						yesCounter++;
				}

				if (i == 3){
					if(Player3AI.tradeConsent(wishCards) == true)
						yesCounter++;
				}

			}

			//if all other player do not agree to trade
			//end trade

			//if only one player wants to trade, trade
			if (yesCounter == 1){

				for (int tradingPlayerNum = 1; tradingPlayerNum <= Bohnanza.getPlayers().length; tradingPlayerNum++){
					if(yes[tradingPlayerNum - 1] == true){
						swapCards(tradingPlayerNum - 1, currentTradeCard, wishCards, player);
					}
				}
			}else if (yesCounter > 1){	
				//if more than 1 player want to trade with active player
				//trade with player with least coin value(least likely to win)
				int leastCoins = 1000000;
				int leastCoinsPlayer = 0;
				for (int tradingPlayerNum = 1; tradingPlayerNum <= Bohnanza.getPlayers().length; tradingPlayerNum++){

					if(yes[tradingPlayerNum - 1] == true){
						if(leastCoins > Bohnanza.players[tradingPlayerNum - 1].getTreasuryArea())
							leastCoinsPlayer = tradingPlayerNum - 1;
					}
				}
				swapCards(leastCoinsPlayer, currentTradeCard, wishCards, player);
			}
		}

		//once trading has ended, plant everything in trading area
		//including all cards received from trading and original 2 cards from deck
		//if any of the 2 cards have not been traded
		for(int i = 0; i <= player.getTradingArea().size(); i ++)
			plant(player.getTradingArea().get(i));

	}

	//to swap cards with other player once agreed to trade
	private void swapCards(int pNum, ArrayList<Card> currentTradeCard, 
			ArrayList<Card> wishCards, Player player){

		//add active player's card to other player's trading area
		Bohnanza.players[pNum].getTradingArea().addAll(currentTradeCard);

		//let other player plant the received card from trading
		//need to plant in other player's field
		opt.plantOtherPlayer(pNum, Bohnanza.players[pNum].getTradingArea());	

		//add wish card to active player's trading area
		player.getTradingArea().addAll(wishCards);

		//remove wish card from other player's hands
		Bohnanza.players[pNum].getHand().remove(wishCards);

		//current active players card are already removed when taking the card into array

	}

	//method that gets called by other player and returns an answer that states whether player wants to trade
	@Override
	public boolean tradeConsent(Card tradeCard) {

		//if the trade card is a type that is planted in field or exist in hand, then agree to trade
		if(player.getField(0).getType() == tradeCard.getType() ||
				player.getField(1).getType() == tradeCard.getType() ||
				player.getHand().contains(tradeCard))
			return true;

		//if trade card is a rare card and game is NOT during ending phase, agree to trade
		if(Deck.getDepletionCounter() <= 2){
			for(int r = 0; r <= rare.length; r++){
				if(tradeCard.getType() == rare[r].getType())
					return true;
			}

		}
		//otherwise, do not agree to trade
		return false;
	}

}
