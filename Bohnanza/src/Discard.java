import java.util.ArrayList;

public class Discard {
	
	private ArrayList<Card> discard;
	
	public Discard() {
		
		discard = new ArrayList<Card>();
		
	}
	
	public void addCardToDiscard(Card card) {
		discard.add(card);
	}

	@Override
	public String toString() {
		return "Discard=" + discard;
	}

	public ArrayList<Card> getDiscard() {
		return discard;
	}

	public void setDiscard(ArrayList<Card> discard) {
		this.discard = discard;
	}

}
