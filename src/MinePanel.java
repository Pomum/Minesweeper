import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

/** To do: Make it easier to set the amount of rows, columns and bombs */
public class MinePanel extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private int width = 1500; 
	private int height = 900; 
	private int tileWidth = 18;
	private int tileHeight = 14;
	private int amountRowsColumns = 30; // Amount of rows and columns 
	private int amountOfBombs = (amountRowsColumns * amountRowsColumns) / 4;
	private boolean leftClick = false;
	private boolean rightClick = false;
	private boolean gameOver = false;
	private List<Tile> tileList = new ArrayList<Tile>(); // Basically a list of all the tiles in the game.
	Point2D bombLocation = new Point2D.Double();

	public MinePanel()
	{
		width = tileWidth * amountRowsColumns + 1;
		height = tileHeight * amountRowsColumns + 1;
		
		setBackground(new Color(200, 200, 200));
		setPreferredSize(new Dimension(width,height));
		setNewGame(null);
		mouseListener();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		paint(g2);
	}
	
	public void paint(Graphics2D g2)
	{
		for(Tile t : tileList)
		{
			t.paint(g2);
			if(t.isClicked())
			{
				t.drawNumber(g2);
			}
		}
		repaint();
	}

	/** Makes all the tiles and adds it to the tileList */
	public void initTiles()
	{
		for (int i = 0; i < amountRowsColumns; i++)
		{
			for (int j = 0; j < amountRowsColumns; j++)
			{
				tileList.add(new Tile(i * tileWidth, j * tileHeight, tileWidth, tileHeight, i, j));
			}
		}
	}
	
	public void mouseListener()
	{
		addMouseListener(new MouseListener() 
		{
			
			@Override
			public void mouseReleased(MouseEvent e)
			{
				onClick(e); // Deals with the mouse event.
				leftClick = false;
				rightClick = false;
			}
			
			@Override
			public void mousePressed(MouseEvent e) 
			{
				if(e.getButton() == MouseEvent.BUTTON1)
				{
					leftClick = true;
				}
				if(e.getButton() == MouseEvent.BUTTON3)
				{
					rightClick = true;
				}
				
			}
			
			@Override
			public void mouseExited(MouseEvent e){}
			
			@Override
			public void mouseEntered(MouseEvent e){}
			
			@Override
			public void mouseClicked(MouseEvent e){}
		});
	}
	
	/** When given a Point2D point on the board it will tell you which tile it is or return null if that point does not contain a tile */
	public Tile findTile(Point2D p2d)
	{
		for(Tile t : tileList)
		{
			if(t.containsPoint(p2d))
			{
				return t;
			}
		}
		return null;
	}	
	
	public Tile findTile(int x, int y)
	{
		for(Tile tile : tileList)
		{
			if(x == tile.getNumberX() && y == tile.getNumberY())
			{
				return tile;
			}
		}
		return null;
	}
	
	public void setBombs(Tile t)
	{
		for(int i =  0; i <= amountOfBombs; i++)
		{
			Tile tile = findTile((int)(Math.random() * amountRowsColumns), (int)(Math.random() * amountRowsColumns));
			if (!tile.equals(t))
			{
				tile.setBomb();
			}
			else 
			{
				i--;
			}
		}
	}
	
	
	/** Figures out how many bombs are surrounding each tile and sets the bombcount of the tiles */
	public void setBombCount()
	{
		for(Tile t : tileList)
		{
			int count = 0;
			ArrayList<Tile> surroundingTiles = getSurroundingTiles(t);
			for(Tile t2 : surroundingTiles)
			{
				if(t2.isBomb())
				{
					count++;
				}
			}
			t.setBombCount(count);
		}
	}
	
	/** This basically gives you all the tiles around the specific tile. It returns an arrayList with these tiles. */
	public ArrayList<Tile> getSurroundingTiles(Tile t)
	{
		Point2D tileLocation = t.getLocation();
		ArrayList<Tile> surroundingTiles = new ArrayList<Tile>();

		ArrayList<Point2D.Double> points = new ArrayList<Point2D.Double>();
		points.add(new Point2D.Double(tileLocation.getX() - tileWidth, tileLocation.getY() - tileHeight));
		points.add(new Point2D.Double(tileLocation.getX(), tileLocation.getY() - tileHeight ));
		points.add(new Point2D.Double(tileLocation.getX() + tileWidth, tileLocation.getY() - tileHeight));
		points.add(new Point2D.Double(tileLocation.getX() + tileWidth , tileLocation.getY()));
		points.add(new Point2D.Double(tileLocation.getX() + tileWidth, tileLocation.getY() + tileHeight));
		points.add(new Point2D.Double(tileLocation.getX(), tileLocation.getY() + tileHeight));
		points.add(new Point2D.Double(tileLocation.getX() - tileWidth, tileLocation.getY() + tileHeight));
		points.add(new Point2D.Double(tileLocation.getX() - tileWidth, tileLocation.getY()));
		
		for (Point2D.Double p : points)
		{
			if (findTile(p) != null)
			{
				surroundingTiles.add(findTile(p));
			}
		}		
		return surroundingTiles;
	}
	
	
	/** Deals with the click events */
	public void onClick(MouseEvent e)
	{
		Tile t = findTile(e.getPoint());
		if(leftClick && rightClick) // There is something going wrong with this.
		{
			doBoth(t);
		}
		else if(leftClick)
		{
			doLeftClick(t);
		}
		else if(rightClick)
		{
			doRightClick(t);
		}
	}
	
	public void doLeftClick(Tile t)
	{
		if(gameOver)
		{
			setNewGame(t); 
		}
		else if (t != null)
		{
			if (t.isSafe())
			{
				t.setReverseSafe();
			}
			else if (t.isBomb())
			{
				setGameOver();
			}
			else 
			{
				t.setClicked();
				if(t.getBombCount() == 0)
				{
					setZeroTiles(getSurroundingTiles(t));
				}
			}
		}
	}
	
	public void doRightClick(Tile t)
	{
		if(!gameOver)
		{
			if (t.isSafe())
			{
				t.setReverseSafe();
			}
			else if (!t.isClicked())
			{
				t.setSafe();
			}
		}
	}
	
	public void doBoth(Tile t)
	{ 
		if(t.isClicked() && !gameOver)
		{ 
			if (checkMiddleClick(t) == 0)
			{
				setGameOver(); 
			}
			else if (checkMiddleClick(t) == 2)
			{	
				for(Tile tile : getSurroundingTiles(t))
				{
					if(!tile.isBomb())
					{
						tile.setClicked();
						if(tile.getBombCount() == 0)
						{
							setZeroTiles(getSurroundingTiles(tile));
						}
					}
				}
			}
		}
	}
	
	public void setGameOver()
	{
		for(Tile tile : tileList)
		{
			if(tile.isBomb() && !tile.isSafe())
			{
				tile.setBoom();
			}
			if(!tile.isBomb() && tile.isSafe())
			{
				tile.setWrong();
			}
			
		}
		gameOver = true;
	}
	
	public void setNewGame(Tile t)
	{
		tileList.clear(); // Restarting the game clearing the old tiles
		initTiles();      // Adding in new tiles
		setBombs(t);	  // Making random bombs, excluding the one you just clicked on
		setBombCount();   // Adding the numbers aka all the bombcounts
		gameOver = false;
	}
	
	public int checkMiddleClick(Tile t)
	{
		for(Tile tile : getSurroundingTiles(t))
		{
			if(tile.isBomb() && !tile.isSafe())
			{
				return 0;
			}			
		}
		for(Tile tile : getSurroundingTiles(t))
		{
			if(!tile.isBomb() && tile.isSafe())
			{
				return 1;
			}			
		}
		
		return 2;
	}
	
	
	/** When given an arraylist it will find all the tiles with a zero and open all the tiles around it untill there is no zero left. */
	public void setZeroTiles(ArrayList<Tile> tileList)
	{
		ArrayList<Tile> tempTiles = new ArrayList<Tile>();
		ArrayList<Tile> tempTiles2 = new ArrayList<Tile>();
		
		tempTiles.clear();
		tempTiles2.clear();
		
		for (Tile tile : tileList)
		{
			
			if (tile.getBombCount() == 0 && !tile.isClicked() && !tile.isBomb())
			{
				tile.setClicked();
				tempTiles.addAll(getSurroundingTiles(tile));
			}
			else if (!tile.isClicked() && !tile.isBomb())
			{
				tile.setClicked();
			}
			
		}
		
		for (Tile tile : tempTiles)
		{
			
			if (tile.getBombCount() == 0 && !tile.isClicked() && !tile.isBomb())
			{
				tile.setClicked();
				tempTiles2.addAll(getSurroundingTiles(tile));
			}
			else if (!tile.isClicked() && !tile.isBomb())
			{
				tile.setClicked();
			}
		}
		
		if (!tempTiles2.isEmpty())
		{
			setZeroTiles(tempTiles2);
		}
	}
}
