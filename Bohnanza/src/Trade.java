import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JOptionPane;

/*
 * @author Julia
 * 
 * This class prompts the players through the trading process and calls up other classes such as
 * the Play class or Other Players' Trading class to plant and harvest during the trading process		
 */

public class Trade {

	private OtherPlayersTrading opt;

	public Player trading(Player player, int playerNum) {

		//FindCard findCard = new FindCard();
		Scanner input = new Scanner(System.in);
		opt = new OtherPlayersTrading();

		//object to generate the rest of a card just from user's input of type
		FindCard cardFinder = new FindCard();
		//create temporary array to hold the cards the active player wish to trade for
		ArrayList<Card> wishCards = new ArrayList<Card>();
		//create temporary array to hold the cards the active player is currently trading with
		ArrayList<Card> currentTradeCard = new ArrayList<Card>();
		//to hold the index of the card player selects from hand or trading area
		int index;
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
			
		BohnanzaGui tempGui = Bohnanza.getGui().tradingFieldSetup(playerNum);
		Bohnanza.setGui(tempGui);
		
		//show player the 2 cards drawn from deck to trading area
		System.out.println("in your trading area you have" 
				+ Arrays.toString(player.getTradingArea().toArray()));
		//ask player if they want to trade
		System.out.println("Do you wish to trade? (y/n)");

		//if player don't trade, plant the 2 cards
		if(JOptionPane.showInputDialog("Enter reply").equals("n")) {
			Bohnanza.getPlay().plant(player.getTradingArea());
		}
		//if player wants to trade, start trading process
		else {

			do{
				//ask if player want to trade cards in trading area
				System.out.println("do you wish to trade with the cards in your trading area? (y/n)");

				//if player want to trade card in trading area
				String userTradeChoice = JOptionPane.showInputDialog("Enter reply");
				if (userTradeChoice.equals("y")){

					//ask how many cards in trading area they wish to trade
					System.out.println("how many of the cards in your trading area do you wish to trade?");
					loop = Integer.parseInt(JOptionPane.showInputDialog("Enter reply"));

					//ask which cards they wish to trade
					for(int i = 0; i < loop; i++){
						System.out.print("which card do you wish to trade? (");
						for (int c = 1; c <= player.getTradingArea().size(); c++){
							System.out.print(c);

							if(c != player.getTradingArea().size())
								System.out.print(" or ");
						}
						System.out.print(")");
						System.out.println();

						//add card player selected to current trade card, remove from trading area
						index = Integer.parseInt(JOptionPane.showInputDialog("Enter reply")) - 1;
						currentTradeCard.add(player.getTradingArea().get(index));
						player.getTradingArea().remove(index);

					}

					//ask if player wants to trade any cards in hand
					System.out.println("the cards you have in hand are " + 
							Arrays.toString(player.getHand().toArray()));
					System.out.println("do you wish to trade cards in hand? (y/n)");

					//if player wants to trade card in hand, ask which cards
					if(JOptionPane.showInputDialog("Enter reply").equals("y")){

						System.out.println("how many of the cards in hand do you wish to trade?");
						loop = Integer.parseInt(JOptionPane.showInputDialog("Enter reply"));

						for(int i = 0; i < loop; i++){
							System.out.print("which card do you wish to trade? (");
							for (int c = 1; c <= player.getHand().size(); c++){
								System.out.print(c);

								if(c != player.getHand().size())
									System.out.print(" or ");
							}
							System.out.print(")");
							System.out.println();

							//add the card the player chose to array of cards to be traded
							index = Integer.parseInt(JOptionPane.showInputDialog("Enter reply")) - 1;
							currentTradeCard.add(player.getHand().get(index));
							player.getHand().remove(index);
						}

					}

					//ask player what cards they want to trade for
					System.out.println("how many cards do you wish to receive from other players?");
					loop = Integer.parseInt(JOptionPane.showInputDialog("Enter reply"));

					for(int i = 0; i < loop; i++){
						System.out.println("please enter the type of cards you wish to trade for? (no spaces allowed)");
						wishType = JOptionPane.showInputDialog("Enter reply");

						//add the card they wish to trade for in the wish card array list
						wishCards.add(cardFinder.start(wishType)); 
					}

					System.out.println(Arrays.toString(wishCards.toArray()));

					//loop through all other players to ask if they want to trade with active player
					for (int i = 1; i <= Bohnanza.getPlayers().length; i++){
						if (i != playerNum) {
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

					}

					//if all other player do not agree to trade
					//ask current active player to end trade or up their deal
					if (yesCounter == 0){
						System.out.println("no player wants to trade with you, do you wish to "
								+ "end trading or up your deal (1 or 2)");

						if(Integer.parseInt(JOptionPane.showInputDialog("Enter reply")) == 1) {
							player.getTradingArea().addAll(currentTradeCard);
							currentTradeCard = new ArrayList<Card>();
							break;
						}
						else
							continue;
					}//if only one player wants to trade, trade
					else if (yesCounter == 1){

						for (int tradingPlayerNum = 1; tradingPlayerNum <= Bohnanza.getPlayers().length; tradingPlayerNum++){
							if(yes[tradingPlayerNum - 1] == true){
								System.out.println("PLAYER " + tradingPlayerNum);
								swapCards(tradingPlayerNum - 1, currentTradeCard, wishCards, player);
							}
						}
					}else if (yesCounter > 1){	
						//if more than 1 player want to trade with active player
						//ask active player who they want to trade with
						for (int tradingPlayerNum = 1; tradingPlayerNum <= Bohnanza.getPlayers().length; tradingPlayerNum++){

							if(yes[tradingPlayerNum - 1] == true)
								System.out.println("Player " + tradingPlayerNum + ", ");
						}

						System.out.println("all wants to trade with you, who do you want to "
								+ "trade with? (enter player number)");

						swapCards(Integer.parseInt(JOptionPane.showInputDialog("Enter reply")) - 1, currentTradeCard, wishCards, player);
					}

					//after one whole process of trading finishes, ask active player
					//if they want to trade more or end trading
					System.out.println("do you wish to trade more cards? (y/n)");

					if(JOptionPane.showInputDialog("Enter reply").equals("n"))
						break;
					else
						continue;
				}
				else if (userTradeChoice.equals("n"))
					System.out.println("heh, we won't let you trade from hand only cause the programmers are too lazy");
				break;

			}while(Integer.parseInt(JOptionPane.showInputDialog("Enter reply")) != -1);

			//if trading has ended, plant everything in trading area
			//including all cards received from trading and original 2 cards from deck
			//if any of the 2 cards have not been traded
			Bohnanza.getPlay().plant(player.getTradingArea());

		}
		return player;

	}

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

}
