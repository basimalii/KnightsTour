//package KnightsTour;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

/*
 *  The main window of the gui.
 *  Notice that it extends JFrame - so we can add our own components.
 *  Notice that it implements ActionListener - so we can handle user input.
 *  This version also implements MouseListener to show equivalent functionality (compare with the other demo).
 *  @author mhatcher
 */
public class CheckerBoard extends JFrame implements ActionListener, MouseListener
{
	// gui components that are contained in this frame:
	private JPanel topPanel, bottomPanel;	// top and bottom panels in the main window
	private JLabel instructionLabel;		// a text label to tell the user what to do
	private JLabel infoLabel;            // a text label to show the coordinate of the selected square
    private JButton topButton;				// a 'reset' button to appear in the top panel
	private GridSquare [][] gridSquares;	// squares to appear in grid formation in the bottom panel
	private int rows,columns;				// the size of the grid
	
	private ArrayList<Integer> currentBox = new ArrayList<>();
	private ArrayList<Integer> previousBox = new ArrayList<>();
	
	private int numOfMoves = 0;
	
	/*
	 *  constructor method takes as input how many rows and columns of gridsquares to create
	 *  it then creates the panels, their subcomponents and puts them all together in the main frame
	 *  it makes sure that action listeners are added to selectable items
	 *  it makes sure that the gui will be visible
	 */
	public CheckerBoard(int rows, int columns)
	{
		this.rows = rows;
		this.columns = columns;
		this.setSize(600,600);
		
		// first create the panels
		topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout());
		
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(rows, columns, 6, 6));
		bottomPanel.setSize(500,500);
		
		// then create the components for each panel and add them to it
		
		// for the top panel:
		instructionLabel = new JLabel("Sir Lancelot, visit every square once!");
        //infoLabel = new JLabel("No square clicked yet.");
		topButton = new JButton("New Game");
		topButton.addActionListener(this);			// IMPORTANT! Without this, clicking the square does nothing.
		
		topPanel.add(instructionLabel);
		topPanel.add (topButton);
        //topPanel.add(infoLabel);
		
	
		// for the bottom panel:	
		// create the squares and add them to the grid
		gridSquares = new GridSquare[rows][columns];
		for ( int x = 0; x < columns; x ++)
		{
			for ( int y = 0; y < rows; y ++)
			{
				gridSquares[x][y] = new GridSquare(x, y);
				gridSquares[x][y].setSize(20, 20);
				gridSquares[x][y].setColor(x + y);
				
				gridSquares[x][y].addMouseListener(this);		// AGAIN, don't forget this line!
				
				bottomPanel.add(gridSquares[x][y]);
			}
		}
		
		// now add the top and bottom panels to the main frame
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(topPanel, BorderLayout.NORTH);
		getContentPane().add(bottomPanel, BorderLayout.CENTER);		// needs to be center or will draw too small
		
		// housekeeping : behaviour
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}
	
	
	/*
	 *  handles actions performed in the gui
	 *  this method must be present to correctly implement the ActionListener interface
	 */
	public void actionPerformed(ActionEvent aevt)
	{
		// get the object that was selected in the gui
		Object selected = aevt.getSource();
				
		// if resetting the squares' colours is requested then do so
		if ( selected.equals(topButton) )
		{
			clearCurrentAndPrevious();
			instructionLabel.setText("Sir Lancelot, visit every square once!");
			numOfMoves = 0;
			for ( int x = 0; x < columns; x ++)
			{
				for ( int y = 0; y < rows; y ++)
				{
					gridSquares [x][y].setColor(x + y);
				}
			}
		}
	}
	
	private boolean setCurrentBox(int x, int y) 
	{
		if (previousBox.isEmpty())
		{
			currentBox.add(0, x);
			currentBox.add(1, y);
			setPreviousBox(x , y);
			return false;
		}
		setPreviousBox(currentBox.get(0), currentBox.get(1));
		currentBox.add(0, x);
		currentBox.add(1, y);
		return true;
	}
	
	private void setPreviousBox(int x, int y) 
	{
		previousBox.add(0, x);
		previousBox.add(1, y);
	}
	
	private int getCurrentBoxXCoordinate() { return currentBox.get(0); }
	private int getCurrentBoxYCoordinate() { return currentBox.get(1); }
	
	private GridSquare getCurrentBox() { return gridSquares[getCurrentBoxXCoordinate()][getCurrentBoxYCoordinate()]; }
	
	private int getPreviousBoxXCoordinate() { return previousBox.get(0); }
	private int getPreviousBoxYCoordinate() { return previousBox.get(1); }
	
	private void clearCurrentAndPrevious() 
	{
		currentBox.clear();
		previousBox.clear();
	}
	
	private boolean isValid(GridSquare square) 
	{
        int x = square.getXcoord();
        int y = square.getYcoord();
        
        int currX = getCurrentBoxXCoordinate();
        int currY = getCurrentBoxYCoordinate();
        
        if ((square.equals(getCurrentBox())) || (square.getBackground().equals(Color.blue))) { return false; }
        // right && up || right && down
        else if(((currX - 1 == x) && (currY + 2 == y)) || ((currX + 1 == x) && (currY + 2 == y))) { return true; }
		// left && up || left && down
		else if(((currX - 1 == x) && (currY - 2 == y)) || ((currX + 1 == x) && (currY - 2 == y))) { return true; }
		// up && right || up && left
		else if(((currX - 2 == x) && (currY + 1 == y)) || ((currX - 2 == x) && (currY - 1 == y))) { return true; }
		// down && right || down && left
		else if(((currX + 2 == x) && (currY + 1 == y)) || ((currX + 2 == x) && (currY - 1 == y))) { return true; }
		
		return false;
	}
	
	private void incrementMoves(GridSquare square) 
	{
		if(!currentBox.isEmpty())
		{
//			if ((square == getCurrentBox()) || (square.getBackground().equals(Color.blue)))
//			{
//				instructionLabel.setText("You can't go there!");
//			}
			if(isValid(square))
			{
				numOfMoves ++;
				instructionLabel.setText("Moves: " + numOfMoves);
	            int x = square.getXcoord();
	            int y = square.getYcoord();
				setCurrentBox(x, y);
				gridSquares[getPreviousBoxXCoordinate()][getPreviousBoxYCoordinate()].setBackground(Color.blue);
				square.switchColor();
			}
			else 
			{
		        instructionLabel.setText("You can't go there!");
	        }
		}
		else 
		{
			numOfMoves ++;
			instructionLabel.setText("Moves: " + numOfMoves);
            int x = square.getXcoord();
            int y = square.getYcoord();
			setCurrentBox(x, y);
			gridSquares[getPreviousBoxXCoordinate()][getPreviousBoxYCoordinate()].setBackground(Color.blue);
			square.switchColor();
		}
	}
	
	// Mouse Listener events
	public void mouseClicked(MouseEvent mevt)
	{
		// get the object that was selected in the gui
		Object selected = mevt.getSource();
		
		/*
		 * I'm using instanceof here so that I can easily cover the selection of any of the gridsquares
		 * with just one piece of code.
		 * In a real system you'll probably have one piece of action code per selectable item.
		 * Later in the course we'll see that the Command Holder pattern is a much smarter way to handle actions.
		 */
		
		// if a gridsquare is selected then switch its color
		if (selected instanceof GridSquare)
		{
            GridSquare square = (GridSquare) selected;
            incrementMoves(square);
            if (numOfMoves == rows * columns) {instructionLabel.setText("You did it!");}
            //infoLabel.setText("("+x+","+y+") last selected.");
            
    		}
		}
	
	// not used but must be present to fulfil MouseListener contract
	public void mouseEntered(MouseEvent arg0){}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
}
