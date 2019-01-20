/**
 * BohnanzaGui - Author: Nithiiyan Skhanthan
 * <p>
 * This GUI is comprised of various array's of JPanels and JLabels all arranged to 
 * support the main game and act as visual aid as the game progresses with popup's 
 * to alert the player of game developments and allow them to input answers with 
 * their moves. 
 * <p>
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.*;


public class BohnanzaGui extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	public static final int CARD_WIDTH = 80;
	public static final int CARD_HEIGHT = 100;

	int numberOfFields =2;

	JTextPane gameOutput = new JTextPane(); // create new textPane

	JLabel gameLabel = new JLabel(); //gamePanel

	//various JLabel and JPanel array creation's
	
	JLabel[] playerPlantingFields = new JLabel[2];

	JPanel[] playerCardPanel = new JPanel[4];

	JPanel tradingAreaPanel = new JPanel();

	JLabel[] coins  = new JLabel[4];

	JLabel[] decks = new JLabel[2];

	JLabel[] playerCards = new JLabel[5];

	ImageIcon[] cardArray = new ImageIcon[5];

	JPanel[] playerPanel = new JPanel[4]; 

	JLabel[] tradingCard = new JLabel[2];

	JLabel[] cardQuantities = new JLabel[3];

	//Creation of the BufferedImage for scaling 
	private BufferedImage img;

	/**
	 * BohnanzaGui Method - Author: Nithiiyan Skhanthan
	 * <p>
	 * This method performs the setup of the main frame where the other frames/labels are set on top of. 
	 * <p>
	 */
	public BohnanzaGui(){
		//call setup method

		gameLabelSetup();

		//setup
		getContentPane().setBackground(Color.WHITE); // set border color (for background testing)
		
		setLayout(null); // set layout to null
		
		setSize(1800, 1000); // setup the size 
		
		setDefaultCloseOperation(EXIT_ON_CLOSE); // have program reset on exit
		
		setResizable(false); // make the program a constant size
		
		setVisible(true); // make the program visible to user

	}

	/**
	 * playerPanel Method - Author: Nithiiyan Skhanthan
	 * <p>
	 * This method performs the setup of the main player panels, "playerPanel", and adds them to GUI.
	 * <p>
	 * 
	 */
	private void playerPanel(int index) {

		playerPanel[index] = new JPanel();

		this.add(playerPanel[index]);

		playerPanel[index].setLayout(null); // use our own layout

		if(index == 0)
			playerPanel[index].setBounds(50, 50, 650, 400); // setup the size and location

		else if (index ==1)
			playerPanel[index].setBounds(1100, 50, 650, 400); // setup the size and location

		else if (index ==2)
			playerPanel[index].setBounds(1100, 525, 650, 400); // setup the size and location

		else if (index ==3)
			playerPanel[index].setBounds(50, 525, 650, 400); // setup the size and location


		playerPanel[index].setVisible(true); // make the panel visible
		
		playerPanel[index].setBackground(Color.BLUE); // choose the color


		playerCardPanel(index);


		for(int fieldIndex = 0; fieldIndex < playerPlantingFields.length; fieldIndex++) { 
			coins(index);	
		}
		
	}

	/**
	 * cardQuantityLabels Method - Author: Nithiiyan Skhanthan
	 * <p>
	 * This method performs the setup of the quantity labels, "cardQuantityLabels", which display's how many cards are in each pile/deck.
	 * <p>
	 * 
	 */
	private void cardQuantityLabels(int index) {

		Player[] players = Bohnanza.getPlayers();

		for (int cardLabelIndex = 0; cardLabelIndex < cardQuantities.length; cardLabelIndex++) {


			cardQuantities[cardLabelIndex] = new JLabel();

			if(cardLabelIndex == 0) {
				cardQuantities[cardLabelIndex].setBounds(100, 370, 40,25); // setup the size and location
				
				cardQuantities[cardLabelIndex].setText(""+players[index].getField(1).getQuantity());
			}	
			else if (cardLabelIndex ==1) {
				cardQuantities[cardLabelIndex].setBounds(300, 370, 40,25); // setup the size and location
				
				cardQuantities[cardLabelIndex].setText(""+players[index].getField(2).getQuantity());
			}
			else if (cardLabelIndex ==2) {
				cardQuantities[cardLabelIndex].setBounds(550, 370, 40,25); // setup the size and location
				
				cardQuantities[cardLabelIndex].setText(""+players[index].getTreasuryArea());
			}
			cardQuantities[cardLabelIndex].setOpaque(true);
			
			cardQuantities[cardLabelIndex].setVisible(true);
			
			playerPanel[index].add(cardQuantities[cardLabelIndex]);

			playerPanel[index].revalidate();
			
			playerPanel[index].repaint();
		}
		
	}
	/**
	 * coins Method - Author: Nithiiyan Skhanthan
	 * <p>
	 * This method performs the setup of the coin image labels, and adds them to the playerPanel
	 * <p>
	 * 
	 */
	private void coins(int index) {


		coins[index] = new JLabel(new ImageIcon("images/back.png"));

		playerPanel[index].add(coins[index]);

		coins[index].setBounds(475, 150, 165, 200); // setup the size and location
		
		coins[index].setVisible(true); // make the panel visible
		
		coins[index].setOpaque(true); // choose the color



	}

	/**
	 * playerCardPanel Method - Author: Nithiiyan Skhanthan
	 * <p>
	 * This method performs the setup and addition of the "playerCardPanel" to the "playerPanel", which will then store the players cards. 
	 * <p>
	 * 
	 */
	private void playerCardPanel(int index) {

		playerCardPanel[index] = new JPanel();

		playerPanel[index].add(playerCardPanel[index]);
		
		playerCardPanel[index].setLayout(null); // use our own layout
		
		playerCardPanel[index].setBounds(10, 10, 630, 115);// setup the size and location
		
		playerCardPanel[index].setVisible(true); // make the panel visible
		
		playerCardPanel[index].setBackground(Color.GRAY); // choose the color

		playerCardLabelsSetup(index);

	}

	/**
	 * playerCardLabelsSetup Method - Author: Nithiiyan Skhanthan
	 * <p>
	 * This method performs the setup and addition of the "playerCards" labels to the "playerCardPanel", which 
	 * will store the players cards and display them overlapping each other. 
	 * <p>
	 * 
	 */
	public void playerCardLabelsSetup(int index) {
		for (int cardIndex = 0; cardIndex < playerCards.length; cardIndex++) {

			playerCards[cardIndex] = new JLabel();

			playerCardPanel[index].add(playerCards[cardIndex]);

			if(cardIndex == 0) 
				playerCards[cardIndex].setBounds(10, 10, CARD_WIDTH, CARD_HEIGHT); // setup the size and location

			else if (cardIndex ==1) 
				playerCards[cardIndex].setBounds(80,10, CARD_WIDTH, CARD_HEIGHT); // setup the size and location

			else if (cardIndex ==2)
				playerCards[cardIndex].setBounds(150,10, CARD_WIDTH, CARD_HEIGHT); // setup the size and location

			else if (cardIndex ==3)
				playerCards[cardIndex].setBounds(220, 10, CARD_WIDTH, CARD_HEIGHT); // setup the size and location

			else if (cardIndex ==4)
				playerCards[cardIndex].setBounds(290, 10, CARD_WIDTH, CARD_HEIGHT); // setup the size and location

			playerCards[cardIndex].setOpaque(true);
		}

	}

	/**
	 * playerFieldLabel Method - Author: Nithiiyan Skhanthan, Aalea Ally
	 * <p>
	 * This method performs the setup of the "playerFieldLabel", which takes each card and scales it to an appropriate size, then formats and adds them to the panel.
	 * <p>
	 * 
	 */
	BohnanzaGui playerFieldLabel(int playerNum, int fieldIndex) {

		Player[] players = Bohnanza.getPlayers();
		
		String type = players[playerNum].getField(fieldIndex).getType();
		
		type = type.replace("\"", "");

		ImageIcon cardImage = new ImageIcon("images/" + type + ".png"); // load the image to a imageIcon
		
		Image image = cardImage.getImage(); // transform it 
		
		Image newimg = image.getScaledInstance(185, 235,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		
		cardImage = new ImageIcon(newimg);  // transform it back

		playerPlantingFields[fieldIndex-1] = new JLabel(cardImage);

		playerPlantingFields[fieldIndex-1].setLayout(null); // use our own layout
		
		playerPlantingFields[fieldIndex-1].setBounds(10 + (fieldIndex-1) * 250, 130, 220 ,230); // setup the size and location

		playerPlantingFields[fieldIndex-1].setVisible(true); // make the panel visible

		playerPlantingFields[fieldIndex-1].setOpaque(false);

		playerPanel[playerNum].add(playerPlantingFields[fieldIndex-1]);
		
		playerPlantingFields[fieldIndex-1].revalidate();
		
		playerPlantingFields[fieldIndex-1].repaint();
		
		playerPanel[playerNum].revalidate();
		
		playerPanel[playerNum].repaint();

		cardQuantityLabels(playerNum);

		return this;

	}
	/**
	 * gameLabelSetup Method - Author: Nithiiyan Skhanthan
	 * <p>
	 * This method performs the setup of the gameLabel, which sits in front of the main frame and contains the rest of the elements for the GUI.
	 * <p>
	 * 
	 */

	private void gameLabelSetup() {

		playerPanelSetup();

		for(int deckIndex = 0; deckIndex < decks.length; deckIndex++){
			
			deckSetup(deckIndex);
	
		}

		this.add(gameLabel);

		gameLabel.setLayout(null); // use our own layout
		
		gameLabel.setBounds(10, 10, 1770, 950); // setup the size and location
		
		gameLabel.setVisible(true); // make the panel visible
		
		gameLabel.setOpaque(true); // choose the color


		//title code
		JLabel gamePanelTitle = new JLabel("Bohnanza!"); //title

		gameLabel.add(gamePanelTitle);
		
		gamePanelTitle.setBounds(865, 1, 150, 50);
		
		ImageIcon cardImage = new ImageIcon("images/background.jpeg"); // load the image to a imageIcon
		
		Image image = cardImage.getImage(); // transform it 
		
		Image newimg = image.getScaledInstance(1770, 950,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		
		cardImage = new ImageIcon(newimg);  // transform it back

		gameLabel.setIcon(cardImage);
		
		gameLabel.setHorizontalAlignment(JLabel.CENTER);
		
		gameLabel.setVerticalAlignment(JLabel.CENTER);
	}
	/**
	 * tradingFieldSetup Method - Author: Nithiiyan Skhanthan
	 * <p>
	 * This method performs the setup of the trading field, which cards are placed in for the other players to view for trading. 
	 * <p>
	 * 
	 */
	BohnanzaGui tradingFieldSetup(int playerNum) {
		Player[] players = Bohnanza.getPlayers();

		tradingAreaPanel.setLayout(null); // use our own layout
		
		tradingAreaPanel.setBounds(700, 50, 380, 325); // setup the size and location
		
		tradingAreaPanel.setVisible(true); // make the panel visible

		for (int tradingCardIndex = 0; tradingCardIndex < tradingCard.length; tradingCardIndex++) {

			tradingCard[tradingCardIndex] = new JLabel();

			tradingCard[tradingCardIndex].setBounds(25 +  tradingCardIndex* 185, 10, 150 , 225); // setup the size and location

			String type = players[playerNum].getTradingArea().get(tradingCardIndex).getType();
			
			type = type.replace("\"", "");
			
			System.out.println(type + " in the process of adding to the trading area");

			ImageIcon cardImage = new ImageIcon("images/" + type + ".png"); // load the image to a imageIcon
			
			Image image = cardImage.getImage(); // transform it 
			
			Image newimg = image.getScaledInstance(CARD_WIDTH+90, CARD_HEIGHT+145,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			
			cardImage = new ImageIcon(newimg);  // transform it back

			tradingCard[tradingCardIndex].setIcon(cardImage);
			
			tradingCard[tradingCardIndex].setHorizontalAlignment(JLabel.CENTER);
			
			tradingCard[tradingCardIndex].setVerticalAlignment(JLabel.CENTER);

			tradingCard[tradingCardIndex].setVisible(true); // make the panel visible
			
			tradingCard[tradingCardIndex].setOpaque(true);

			tradingAreaPanel.add(tradingCard[tradingCardIndex]);
			
			gameLabel.add(tradingAreaPanel);

		}

		tradingAreaPanel.revalidate();
		
		tradingAreaPanel.repaint();
		
		gameLabel.revalidate();
		
		return this;
	}

	/**
	 * deckSetup Method - Author: Nithiiyan Skhanthan
	 * <p>
	 * This method performs the setup of the decks at the bottom, which store static card labels to simulate decks. 
	 * <p>
	 * 
	 */
	private void deckSetup(int deckIndex) {


		ImageIcon back = new ImageIcon("images/Back.png"); //make new ImageIcon
		
		Image image = back.getImage(); // make to editable image
		
		Image newimg = image.getScaledInstance(140, 200,  java.awt.Image.SCALE_SMOOTH); // scale image to better fit size
		
		back = new ImageIcon(newimg);  // make back to placeable image
		
		decks[deckIndex] = new JLabel(back); //add image

		gameLabel.add(decks[deckIndex]);

		decks[deckIndex].setLayout(null); // use our own layout
		
		decks[deckIndex].setVisible(true); // make the panel visible
		
		decks[deckIndex].setOpaque(true); // choose the color

		if (deckIndex == 1) 
			decks[deckIndex].setBounds(700, 600, 175 , 275); // setup the size and location
		
		else if (deckIndex == 0)
			decks[deckIndex].setBounds(905, 600, 175 , 275); // setup the size and location


	}

	public BohnanzaGui setField(Field field) {

		return this;
	}
	public int getNumberOfFields() {
		return numberOfFields;
	}


	public void setNumberOfFields(int numberOfFields) {
		this.numberOfFields = numberOfFields;
	}


	public JLabel getGamePanel() {
		return gameLabel;
	}


	public void setGamePanel(JLabel gamePanel) {
		this.gameLabel = gamePanel;
	}


	public JLabel[] getPlayerPlantingFields() {
		return playerPlantingFields;
	}


	public void setPlayerPlantingFields(JLabel[] playerPlantingFields) {
		this.playerPlantingFields = playerPlantingFields;
	}


	public JPanel[] getPlayerCardPanel() {
		return playerCardPanel;
	}


	public void setPlayerCardPanel(JPanel[] playerCardPanel) {
		this.playerCardPanel = playerCardPanel;
	}


	public JPanel getTradingAreaPanel() {
		return tradingAreaPanel;
	}


	public void setTradingAreaPanel(JPanel tradingAreaPanel) {
		this.tradingAreaPanel = tradingAreaPanel;
	}


	public JLabel[] getCoins() {
		return coins;
	}


	public void setCoins(JLabel[] coins) {
		this.coins = coins;
	}


	public JLabel[] getDecks() {
		return decks;
	}


	public void setDecks(JLabel[] decks) {
		this.decks = decks;
	}


	public ImageIcon[] getCardArray() {
		return cardArray;
	}


	public void setCardArray(ImageIcon[] cardArray) {
		this.cardArray = cardArray;
	}


	public JPanel[] getPlayerPanel() {
		return playerPanel;
	}


	public void setPlayerPanel(JPanel[] playerPanel) {
		this.playerPanel = playerPanel;
	}


	public JLabel[] getTradingCard() {
		return tradingCard;
	}


	public void setTradingCard(JLabel[] tradingCard) {
		this.tradingCard = tradingCard;
	}



	private void playerPanelSetup() {

		for(int index = 0; index < playerPanel.length; index++)
			playerPanel(index);


	}

	public JLabel[] getPlayerCards() {
		return playerCards;
	}

	/**
	 * deckSetup Method - Author: Aalea Ally
	 * <p>
	 * This method performs the setup of the player cards and sets the player card labels to the respective players card images
	 * <p>
	 * 
	 */
	public BohnanzaGui setPlayerCards(int playerNum) {

		playerCardPanel[playerNum].removeAll();

		Player[] players = Bohnanza.getPlayers();

		playerCards = new JLabel[players[playerNum].getHand().size()];

		for (int cardIndex = 0; cardIndex < playerCards.length; cardIndex++) {

			String type = players[playerNum].getHand().get(cardIndex).getType();
			
			type = type.replace("\"", "");
		
			playerCards[cardIndex] = new JLabel();
			
			ImageIcon cardImage = new ImageIcon("images/" + type + ".png"); // load the image to a imageIcon
			
			Image image = cardImage.getImage(); // transform it 
			
			Image newimg = image.getScaledInstance(CARD_WIDTH, CARD_HEIGHT,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
		
			cardImage = new ImageIcon(newimg);  // transform it back

			playerCards[cardIndex].setIcon(cardImage);


			playerCardPanel[playerNum].add(playerCards[cardIndex]);

			playerCards[cardIndex].setBounds(10 + (70 * (cardIndex)), 10, CARD_WIDTH, CARD_HEIGHT); // setup the size and location

			playerCards[cardIndex].setOpaque(true);
		}

	

		playerCardPanel[playerNum].revalidate();
		
		playerCardPanel[playerNum].repaint();
		
		return this;

	}


	public JTextPane getGameOutput() {
		return gameOutput;
	}

	public void setGameOutput(JTextPane gameOutput) {
		this.gameOutput = gameOutput;
	}

	public static int getCardWidth() {
		return CARD_WIDTH;
	}

	public static int getCardHeight() {
		return CARD_HEIGHT;
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
