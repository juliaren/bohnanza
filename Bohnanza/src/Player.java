import java.util.ArrayList;
import java.util.Arrays;

/*
 * @author Julia
 * 
 * this class holds all the attributes of a player
 */
public class Player {
	
	/*
	 * the trading area of a player
	 */
	private ArrayList<Card> tradingArea;
	
	/*
	 * the coins a player has
	 */
	private int treasuryArea;
	
	/*
	 * the fields a player has
	 */
	private Field[] field;
	
	/*
	 * the player's cards in their hand
	 */
	private ArrayList<Card>hand;

	/*
	 * this constructor initializes the treasury, fields, and hand
	 */
	public Player() {
		
		//tradingArea = new TradingArea();
		treasuryArea = 0;
		
		field = new Field[2];
		field[0] = new Field();
		field[1] = new Field();
		hand = new ArrayList<Card>();
		tradingArea = new ArrayList<Card>();

	}

	@Override
	public String toString() {
		return "Player [tradingArea=" + tradingArea
				+ ", treasuryArea=" + treasuryArea + ", field="
				+ Arrays.toString(field) + "]";
	}

	public ArrayList<Card> getTradingArea() {
		return tradingArea;
	}

	public void setTradingArea(ArrayList<Card> tradingArea) {
		this.tradingArea = tradingArea;
	}

	public int getTreasuryArea() {
		return treasuryArea;
	}

	public Field[] getField() {
		return field;
	}

	public void setTreasuryArea(int treasuryArea) {
		this.treasuryArea = treasuryArea;
	}

	public Field getField(int fieldNum) {
		return field[fieldNum - 1];
	}

	public void setField(Field field, int fieldNum) {
		this.field[fieldNum-1] = field;
	}

	public ArrayList<Card> getHand() {
		return hand;
	}

	public void setHand(ArrayList<Card> hand) {
		this.hand = hand;
	}

}
