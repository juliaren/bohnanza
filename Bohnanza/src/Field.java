import java.util.ArrayList;

/*
 * @author Julia
 * 
 * this class holds all attributes of a field
 * has methods to check if field is occupied and add crops to field
 */
public class Field{
	
	private String type;
	private int quantity;
	private ArrayList<Card> crops;

	public Field() {
		
		this.type = "new";
		this.quantity = 0;
		crops = new ArrayList<Card>();

	}

	@Override
	public String toString() {
		return "Field [type=" + type + ", quantity=" + quantity + "]";
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
	
	private boolean checkOccupied(){
		
		if (quantity == 0)
			return false;
		else
			return true;
	}
	
	public void addToField(Card card){
		
		if (type == card.getType()){
			crops.add(card);
			quantity++;
		}
		else if(checkOccupied() == false){
			crops.add(card);
			type = card.getType();
			quantity++;
		}
	}

	public ArrayList<Card> getCrops() {
		return crops;
	}

	public void setCrops(ArrayList<Card> crops) {
		this.crops = crops;
	}

}
