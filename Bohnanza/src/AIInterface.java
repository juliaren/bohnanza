import java.util.ArrayList;

//interface that defines what each AI class must have
public interface AIInterface {

	//to start their turn that can call the following methods
	public Player start(Player player);
	
	//to hold codes that decide which cards to plant
	public void plant();
	
	//to hold codes that decide which cards to harvest
	public void harvest(int field);
	
	//to hold codes that decide which cards to trade
	public void trade();
	
	//to hold codes that decide whether player wishes to trade
	public boolean tradeConsent(Card tradeCard);

}
