import java.util.Arrays;

/*
 * @author Aalea
 * 
 * This class holds the attributes of a card
 */
public class Card{

	/*
	 * This holds the type of the card
	 */
	private String type;
	
	/*
	 * This holds the number of cards of this type present in the entire game
	 */
	private int quantity;
	
	/*
	 * The index is the reward value and the variable at that index is the crops needed
	 */
	private int[] values;
	
	/*
	 * Constructor method that stores attributes to card
	 */
	public Card(String type, int quantity, int[] values) {

		this.type = type;
		this.quantity = quantity;
		this.values = values;
		
	}
	
	//another contructor to hold "imaginary" card that player wish to trade for
	public Card(String type, int quantity) {

		this.type = type;
		this.quantity = quantity;
		
	}

	@Override
	public String toString() {
		return type + ", quantity=" + quantity + ", values=" + Arrays.toString(values) + "]";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int[] getValues() {
		return values;
	}

	public void setValues(int[] values) {
		this.values = values;
	}

}
