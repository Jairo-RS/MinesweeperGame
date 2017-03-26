
public class GridCells { //This class stores the information of each cell

	private int xPos;
	private int yPos;
	public int neighboringBombs = 0;
	private boolean Visible = false;
	private boolean Bomb;
	
	GridCells(int x, int y) {
		this.xPos = x;
		this.yPos = y;
	}
	
	public int getxPos() {
		return this.xPos;
	}
	public int getyPos() {
		return this.yPos;
	}
	
	public void setxPos(int x) {
		this.xPos = x;
	}
	public void setyPos(int y) {
		this.yPos = y;
	}
	
	public boolean isVisible() {
		return this.Visible;
	}
	
	public void setVisible(boolean value) {
		this.Visible = value;
	}	
	
	public boolean isBomb() {
		return this.Bomb;
	}
	
	public void setBomb(boolean value) {
		this.Bomb = value;
	}
	
	public int getNeighboringBombs() {
		return this.neighboringBombs;
	}
	
	public void bumpBombs() {
		this.neighboringBombs += 1;
	}
}