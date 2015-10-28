import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Point2D;


public class Tile 
{
	private int tileWidth;
	private int tileHeight;
	private int bombCount;
	private boolean bomb;
	private boolean clicked = false;
	private Shape r;
	private Color color;
	private Point2D location = new Point2D.Double();
	private boolean safe = false;
	private int numberX;
	private int numberY;
	
	public Tile(int x, int y, int tileWidth, int tileHeight, int numberX,int numberY)
	{
		this.numberX = numberX;
		this.numberY = numberY;
		location.setLocation(x, y);
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		r = new Rectangle(x, y, tileWidth, tileHeight);
		color = Color.gray;
		bomb = false;
	}
	
	public void paint(Graphics2D g2)
	{
			g2.setPaint(color);
			g2.fill(r);
			g2.setPaint(Color.black);
			g2.draw(r);
	}
	
	public boolean containsPoint(Point2D p2d)
	{
		if (r.contains(p2d))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public void setBackground(Color c) 
	{
		this.color = c;
	}
	
	public void setBombCount(int bombCount)
	{
		this.bombCount = bombCount;
	}
	
	public int getBombCount()
	{
		return bombCount;
	}
	
	public boolean isBomb()
	{
		return bomb;
	}
	
	public Point2D getLocation()
	{
		return location;
	}
	
	
	public void drawNumber(Graphics2D g2)
	{
		String s = "" + bombCount;
		double sWidth = g2.getFont().getStringBounds(s, g2.getFontRenderContext()).getWidth();
		double sHeight = g2.getFont().getStringBounds(s, g2.getFontRenderContext()).getHeight();
		
		g2.drawString(s, (int)(location.getX() + tileWidth/2 - sWidth/2), (int)(location.getY() + tileHeight/2 + sHeight/2 - 2));
	}
	
	public void setClicked()
	{
		clicked = true;
		color = Color.white;
	}
	
	public void setBoom()
	{
		color = Color.red;
	}
	
	public void setSafe()
	{
		color = Color.cyan;
		safe = true;
	}
	
	public void setReverseSafe()
	{
		color = Color.gray;
		safe = false;
	}
	
	public boolean isSafe()
	{
		return safe;
	}
	
	public void setBomb()
	{
		bomb = true;
	}
	
 	public boolean isClicked()
	{
		return clicked;
	}
 	
 	public int getNumberX()
 	{
 		return numberX;
 	}
 	
 	public int getNumberY()
 	{
 		return numberY;
 	}
 	
 	public void setWrong()
 	{
 		color = Color.yellow;
 	}
 	
}
