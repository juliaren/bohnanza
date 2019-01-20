import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.JOptionPane;


public class BohnanzaTest {

	public static void main(String[] args) {

		CustomPrintStream printStream = new CustomPrintStream();  
		System.setOut(printStream); 
		
		new Bohnanza();
		// set up a custom print stream   

		// from now on, the System.out.println() will shows a dialog message  

	}  
}  

class CustomPrintStream extends PrintStream { 
	public CustomPrintStream() { 
		super(new ByteArrayOutputStream()); } 
public void println(String msg) { JOptionPane.showMessageDialog(null, msg); }
}

	



