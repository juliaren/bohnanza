import java.util.ArrayList;
/*
 * @author Nithiiyan Skhanthan
 * 
 * MAIN CLASS
 * - calls classes
 * - creates player array
 * - deals 5 cards to player
 * - manages win's 
 */
import java.util.Arrays;

public class Bohnanza {
	
	private static BohnanzaGui gui;
	
	/*
	 * holds the array for all players
	 */
	static Player[] players;
	
	/*
	 * creates an instance of the play class to use
	 */
	private static Play play;
	
	/*
	 * object that represents player 2's decision making
	 */
	private static Player2AI p2;
	/*
	 * object that represents player 3's decision making
	 */
	private static Player3AI p3;
	/*
	 * object that represents player 4's decision making
	 */
	private static Player4AI p4;
	
	/*
	 * holds the deck
	 */
	private Deck deck;
	
	/*
	 * hold the discard pile
	 */
	private Discard discard;
	
	private ArrayList<Card> temp;

	/*
	 * this constructor 
	 */
	public Bohnanza() {
		
		gui = new BohnanzaGui();
	
		play = new Play();
		p2 = new Player2AI(); //Aalea
		p3 = new Player3AI(); //Nithiiyan
		p4 = new Player4AI(); //Julia
		
		players =  new Player[4];	//creates 4 players
		players[0] = new Player();
		players[1] = new Player();
		players[2] = new Player();
		players[3] = new Player();
		
		deck = new Deck();
		discard = new Discard();
		
		//creates a temp array to hold cards being transported
		temp = new ArrayList<Card>();
		
		//iterates through each player, 0-3
		for(int c = 0; c < 4; c++) {
			
			//players[c] = new Player();
			
			//adds five cards from deck to each player's hand
			for(int i = 0; i < 5; i++){
			
				temp.add(deck.takeCardFromDeck());
		
			}
			//System.out.println(Arrays.toString(temp.toArray()));
			//System.out.println(c);
			players[c].setHand(temp);
			gui.playerCardLabelsSetup(c);
			gui.setPlayerCards(c);
			
			
			temp = new ArrayList<Card>(); //resets temp arraylist
		}


		while (deck.getDepletionCounter() !=3) {
			System.out.println("PLAYER 1");
			players[0] = play.start(players[0], 0);
			System.out.println("PLAYER 2");
			//players[1] = p2.start(players[1]);
			players[1] = play.start(players[1], 1);
			System.out.println("PLAYER 3");
			//players[2] = p3.start(players[2]);
			players[2] = play.start(players[2], 2);
			System.out.println("PLAYER 4");
			//players[3] = p4.start(players[3]);
			players[3] = play.start(players[3], 3);

		}
		deck.checkWin(players);
	}
	public static BohnanzaGui getGui() {
		return gui;
	}
	public static void setGui(BohnanzaGui newGui) {
		gui = newGui;
	}
	public static Player2AI getP2() {
		return p2;
	}
	public static void setP2(Player2AI p2) {
		Bohnanza.p2 = p2;
	}
	public static Player3AI getP3() {
		return p3;
	}
	public static void setP3(Player3AI p3) {
		Bohnanza.p3 = p3;
	}
	public static Player4AI getP4() {
		return p4;
	}
	public static void setP4(Player4AI p4) {
		Bohnanza.p4 = p4;
	}
	public ArrayList<Card> getTemp() {
		return temp;
	}
	public void setTemp(ArrayList<Card> temp) {
		this.temp = temp;
	}
 
	public static Player[] getPlayers() {
		return players;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}

	public static Play getPlay() {
		return play;
	}

	public void setPlay(Play play) {
		this.play = play;
	}

	public Deck getDeck() {
		return deck;
	}

	public void setDeck(Deck deck) {
		this.deck = deck;
	}

	public static Discard getDiscard() {
		return discard;
	}

	public void setDiscard(Discard discard) {
		this.discard = discard;
	}

}