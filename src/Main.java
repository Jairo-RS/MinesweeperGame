import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		JFrame myFrame = new JFrame("MINESWEEPER");
		myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		myFrame.setLocation(400, 150);
		myFrame.setSize(400, 400);
		MyPanel myPanel = new MyPanel();
		myFrame.add(myPanel);
		
		myPanel.generateBombs();
		myPanel.revealTheBombs(); //Method to show that bombs(mines) are working
		myPanel.generateNumbers();
		myPanel.revealTheNumbers = true; //Method to test that numbers are displayed

		MyMouseAdapter myMouseAdapter = new MyMouseAdapter();
		myFrame.addMouseListener(myMouseAdapter);

		myFrame.setVisible(true);
	}
}
