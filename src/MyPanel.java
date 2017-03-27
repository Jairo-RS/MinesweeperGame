import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Random;
import java.awt.Font;

import javax.swing.JPanel;

public class MyPanel extends JPanel {
	private static final long serialVersionUID = 3426940946811133635L;
	private static final int GRID_X = 25;
	private static final int GRID_Y = 25;
	private static final int INNER_CELL_SIZE = 29;
	private static final int TOTAL_COLUMNS = 9;
	private static final int TOTAL_ROWS = 10;   //Last row has only one cell
	
	public int x = -1;
	public int y = -1;
	public int mouseDownGridX = 0;
	public int mouseDownGridY = 0;
	
	public static int bombCount = 10;
	public int exposedCells;

	public Color[][] colorCoveredSquare = new Color[TOTAL_COLUMNS][TOTAL_ROWS+1]; 
	public Color[][] colorUncoveredSquare = new Color[TOTAL_COLUMNS][TOTAL_ROWS];  //este array determina si la celda es una mina o no lo es
	
	public GridCells[][] Cells = new GridCells[TOTAL_COLUMNS][TOTAL_ROWS];
	public boolean revealAllTheNumbers = false;
	
	public MyPanel() {   //This is the constructor... this code runs first to initialize
		if (INNER_CELL_SIZE + (new Random()).nextInt(1) < 1) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("INNER_CELL_SIZE must be positive!");
		}
		if (TOTAL_COLUMNS + (new Random()).nextInt(1) < 2) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_COLUMNS must be at least 2!");
		}
		if (TOTAL_ROWS + (new Random()).nextInt(1) < 3) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_ROWS must be at least 3!");
		}
		for (int x = 0; x < TOTAL_COLUMNS; x++) {   //The rest of the grid
			for (int y = 0; y < TOTAL_ROWS; y++) {
				colorCoveredSquare[x][y] = Color.WHITE;
				Cells[x][y] = new GridCells(x, y);  //Calls GridCells class to get information
			}
		}
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//Compute interior coordinates
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		int x2 = getWidth() - myInsets.right - 1;
		int y2 = getHeight() - myInsets.bottom - 1;
		int width = x2 - x1;
		int height = y2 - y1;

		//Paint the background
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(x1, y1, width + 1, height + 1);

		//By default, the grid will be 9x9 (see above: TOTAL_COLUMNS and TOTAL_ROWS) 
		g.setColor(Color.BLACK);
		for (int y = 0; y <= TOTAL_ROWS - 1; y++) {
			g.drawLine(x1 + GRID_X, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)), x1 + GRID_X + ((INNER_CELL_SIZE + 1) * TOTAL_COLUMNS), y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)));
		}
		for (int x = 0; x <= TOTAL_COLUMNS; x++) {
			g.drawLine(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y, x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y + ((INNER_CELL_SIZE + 1) * (TOTAL_ROWS - 1)));
		}

		//Paint cell(square) colors
		for (int x = 0; x < TOTAL_COLUMNS; x++) {
			for (int y = 0; y < TOTAL_ROWS - 1; y++) {
				if ((x == 0) || (y != TOTAL_ROWS - 1)) {
					Color c = colorCoveredSquare[x][y];
					g.setColor(c);
					g.fillRect(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)) + 1, INNER_CELL_SIZE, INNER_CELL_SIZE);
				}
			}
		}
		
		Font arial = new Font("Arial", Font.BOLD, 20);
		g.setFont(arial);
		
		for (int x = 0; x < TOTAL_COLUMNS; x++)
		{
			for (int y = 0; y < TOTAL_ROWS - 1; y++)
			{
				if(Cells[x][y].isVisible() && Cells[x][y].getNeighboringBombs() > 0)
				{
					revealNumbers(g, x, y);
					repaint();
				}
				
				else if(Cells[x][y].isVisible() && Cells[x][y].getNeighboringBombs() <= 0 && !Cells[x][y].isBomb())
				{
					if (x!=0)
					{
						if(y!=8){Cells[x - 1][y + 1].setVisible(true); colorCoveredSquare[x - 1][y + 1] = Color.GRAY;}
						if(y!=0){Cells[x - 1][y - 1].setVisible(true); colorCoveredSquare[x - 1][y - 1] = Color.GRAY;}
						Cells[x - 1][y].setVisible(true); colorCoveredSquare[x - 1][y] = Color.GRAY;
					}
					if(x!=8)
					{
						if(y!=8){Cells[x + 1][y + 1].setVisible(true); colorCoveredSquare[x + 1][y + 1] = Color.GRAY;}
						if(y!=0){Cells[x + 1][y - 1].setVisible(true); colorCoveredSquare[x + 1][y - 1] = Color.GRAY;}
						Cells[x + 1][y].setVisible(true); colorCoveredSquare[x + 1][y] = Color.GRAY;
					}
					if(y!=8){Cells[x][y + 1].setVisible(true); colorCoveredSquare[x][y + 1] = Color.GRAY;}
					if(y!=0){Cells[x][y - 1].setVisible(true); colorCoveredSquare[x][y - 1] = Color.GRAY;}
				}
			}
		}
		
		if (revealAllTheNumbers)
		{
			revealAllTheNumbers(g);
		}
		
		for (int x = 0; x < TOTAL_COLUMNS; x++)
		{
			for (int y = 0; y < TOTAL_ROWS - 1; y++)
			{
				if(Cells[x][y].isVisible() && !Cells[x][y].isBomb() && Cells[x][y].getNeighboringBombs() > 0)
				{
					g.setColor(Cells[x][y].getColorOfNumber());
					g.drawString(Integer.toString(Cells[x][y].getNeighboringBombs()), GRID_X + x*(INNER_CELL_SIZE+1) + 10, GRID_Y + y*(INNER_CELL_SIZE+1) + 20);
				}
			}
		}
		
		exposedCells = 0;
		for (int x = 0; x < TOTAL_COLUMNS; x++) {
			for (int y = 0; y < TOTAL_ROWS - 1; y++) {
				if(Cells[x][y].isVisible() && !Cells[x][y].isBomb()) {
					exposedCells +=1;
				}
			}
		}
	}
	public int getGridX(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		if (x == 0 && y == TOTAL_ROWS - 1) {    //The lower left extra cell
			return x;
		}
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 2) {   //Outside the rest of the grid
			return -1;
		}
		return x;
	}
	public int getGridY(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		if (x == 0 && y == TOTAL_ROWS - 1) {    //The lower left extra cell
			return y;
		}
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 2) {   //Outside the rest of the grid
			return -1;
		}
		return y;
	}
	
	public void generateBombs()
	{
		Random randX = new Random();
		Random randY = new Random();
		int generatedBombs = 0;
		while (generatedBombs < bombCount)
		{
			int x = randX.nextInt(9);
			int y = randY.nextInt(9);
			if (!Cells[x][y].isBomb())
			{
				Cells[x][y].setBomb(true);
				generatedBombs++;
			}
		}
	}
	
	public void revealTheBombs()
	{
		for (int x = 0; x < TOTAL_COLUMNS; x++)
		{
			for (int y = 0; y < TOTAL_ROWS; y++) 
			{
				if(Cells[x][y].isBomb())
				{
					Cells[x][y].setVisible(true);
					colorCoveredSquare[x][y] = Color.BLACK;
				}
			}
		}
	}
	
	public void generateNumbers()
	{
		for (int x = 0; x < TOTAL_COLUMNS; x++)
		{
			for (int y = 0; y < TOTAL_ROWS; y++)
			{
				if(Cells[x][y].isBomb())
				{
					if (x!=0)
					{
						if(y!=8){Cells[x - 1][y + 1].bumpBombs();}
						if(y!=0){Cells[x - 1][y - 1].bumpBombs();}
						Cells[x - 1][y].bumpBombs();
					}
					if(x!=8)
					{
						if(y!=8){Cells[x + 1][y + 1].bumpBombs();}
						if(y!=0){Cells[x + 1][y - 1].bumpBombs();}
						Cells[x + 1][y].bumpBombs();
					}
					if(y!=8){Cells[x][y + 1].bumpBombs();}
					if(y!=0){Cells[x][y - 1].bumpBombs();}
				}
			}
		}
	}
	
	public void revealAllTheNumbers(Graphics g)
	{
		for (int x = 0; x < TOTAL_COLUMNS; x++)
		{
			for (int y = 0; y < TOTAL_ROWS; y++) 
			{
				if(!Cells[x][y].isBomb() && Cells[x][y].getNeighboringBombs() > 0)
				{
					g.setColor(Cells[x][y].getColorOfNumber());
					g.drawString(Integer.toString(Cells[x][y].getNeighboringBombs()), GRID_X + x*(INNER_CELL_SIZE+1) + 10, GRID_Y + y*(INNER_CELL_SIZE+1) + 20);
				}
			}
		}
	}
	
	public void revealNumbers(Graphics g, int xPos, int yPos)
	{
		g.setColor(Cells[xPos][yPos].getColorOfNumber());
		g.drawString(Integer.toString(Cells[xPos][yPos].getNeighboringBombs()), GRID_X + x*(INNER_CELL_SIZE+1) + 10, GRID_Y + y*(INNER_CELL_SIZE+1) + 20);
		return;
	}
	
}
