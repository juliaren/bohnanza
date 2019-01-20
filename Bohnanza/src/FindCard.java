import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FindCard {

	public Card start(String cardType) {
		int quantity = 0;
		String type = "";
		Scanner input;
		int[] values = new int[4];
		Card match = new Card("cannot be found", 0, values);
		
		cardType = cardType.toLowerCase();
		int indexLastB = cardType.lastIndexOf("b");
		cardType = cardType.substring(0, indexLastB) + " " + cardType.substring(indexLastB, cardType.length());

		try {
			input = new Scanner(new File("cardInfo.txt"));
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
				
				//if the card being read is the query
				if (type.equalsIgnoreCase("\"" + cardType + "\"")) {
					match = new Card(type, quantity, values);
					break;
				}
				
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("File not found, check directory");
		}
		
		//input.close();
		
		return match;
		
		
	}

}
