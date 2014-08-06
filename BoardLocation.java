import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Graphics;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
    Class representing a single point on a Go board.
    @author Dylan Foster
    @version 5/9/11
*/
public class BoardLocation extends JComponent implements MouseListener, Serializable
{

    //Enumerated type for each type of point. Used to easily determine liberties of a location.
    public enum Type { TOP_LEFT, TOP, TOP_RIGHT, RIGHT, BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT, LEFT, MAIN };

    private static BoardLocation lastLocation = null;
    private BoardLocation.Type type;
    private JPopupMenu popupMenu;

    /**
        Constructor. Creates a location of the given <code>size</code>, at the given ( <code>x</code>, <code>y</code> ), of the given type.
        @param int X coordinate of location in pixels.
        @param int Y coordinate of location in pixels.
        @param int Size of this location, in pixels.
        @param BoardLocation.Type Type of location.
    */
    public BoardLocation ( int x, int y, int size, BoardLocation.Type inType )
    {

        //Assigning type of location.
        this.type = inType;

        //Adding mouse listener.
        this.addMouseListener( this );
        
        //Instantiating popup menu
        this.popupMenu = new JPopupMenu();
        
        //Creating menu item for player to pass their turn. Adding anonymous listener to menu item.
        JMenuItem menuItem = new JMenuItem( "Pass Turn" );
        menuItem.addActionListener( new ActionListener () {

            public void actionPerformed ( ActionEvent e )
            {
                
                //Getting parent of this location.
                Object parent = BoardLocation.this.getParent();

                if ( parent instanceof Board )
                {
                    
                    //Passing turn of current player.
                    ( ( Board ) parent ).switchTurns( Board.TURN_PASSED );
              
                }

            }

        });
        
        //Adding menu item to popup menu.
        this.popupMenu.add( menuItem );

        //Setting location and size of location.
        this.setLocation( x - Board.SIZE_OF_SQUARE / 2, y - Board.SIZE_OF_SQUARE / 2 );
        this.setSize( size, size );

    }
    
    /**
        Used to paint any <code>Piece</code> objects that may be on this location.
        @param Graphics Graphics object
        @return void
    */
    public void paintComponent ( Graphics g )
    {
        
        //For each component on this component.
        for ( Component element : this.getComponents() )
        {
            
            //If current component is a Piece.
            if ( element instanceof Piece )
            {
                
                //Paints Piece.
                ( ( Piece ) element ).paintComponent( g );
                
            }
            
        }

    }
    
    /**
        Used to return this <code>BoardLocation</code> to it's original state.
        @return void
    */
    public void reset ()
    {
        
        //Removes all components from this BoardLocation.
        this.removeAll();
        
        //Paints emptied component.
        this.repaint();
        
        //Sets the lastLocation
        BoardLocation.lastLocation = null;

    }
    
    /**
        Used to add a <code>Piece</code> to this <code>BoardLocation</code>.
        @param Piece Piece to add.
        @return void
    */
    public void add ( Piece inPiece )
    {
        
        //Adds Piece component to this component.
        super.add( inPiece );
        
        //Repaints this component.
        this.repaint();
        
        //Checking if this piece is a "solid" piece.
        //If Piece is not solid, it is not added to Board, as it is temporary.
        if ( !inPiece.isTranslucent() )
        {
            
            //Gets this Location's parent component.
            Board myBoard = ( Board ) this.getParent();
            
            //Adds solid piece to board.
            myBoard.addPieceAt( this );

        }

    }

    /**
        Used to remove the current solid <code>Piece</code> from this location.
        @return void
    */
    public void removePiece ()
    {
        
        //Cycling through all components on this component;
        for ( Component element : this.getComponents() )
        {
            
            //Checking that current component is a Piece and is not translucent.
            if ( element instanceof Piece && !( ( Piece ) element ).isTranslucent() )
            {
                
                //Removing Piece from this component.
                super.remove( element );
                
            }
            
        }
        
        //Repainting this component.
        this.repaint();

    }
    
    /**
        Used to remove temporary, translucent pieces.
        @return void
    */
    public void removeTranslucentPieces ()
    {
        
        //Cycling through all components on this component.
        for ( Component element : this.getComponents() )
        {
            
            //Checking that current component is a Piece and is translucent.
            if ( element instanceof Piece && ( ( Piece ) element ).isTranslucent() )
            {
                
                //Removing Piece from this component.
                super.remove( element );
                
            }
            
        }
        
        //Repainting this component.
        this.repaint();

    }
    
    /**
        Used to check if this <code>BoardLocation</code> contains a non-translucent piece.
        @return boolean
    */
    public boolean containsPiece ()
    {
        
        //Cycling through all components on this component.
        for ( Component element : this.getComponents() )
        {
            
            //Checking if current component is a Piece, and is not translucent.
            if ( element instanceof Piece && !( ( Piece ) element ).isTranslucent() )
            {
                
                //Yes, component does contain a non-translucent piece.
                return true;
                
            }
            
        }
        
        //Could not find a non-translucent, Piece component.
        //No, component does not contain a non-translucent piece.
        return false;

    }
    
    /**
        Used to check if this <code>BoardLocation</code> contains a translucent piece.
        @return boolean
    */
    public boolean containsTranslucentPiece ()
    {

        //Cycling through all components on this component.
        for ( Component element : this.getComponents() )
        {
            
            //Checking if current component is a Piece, and is not translucent.
            if ( element instanceof Piece && ( ( Piece ) element ).isTranslucent() )
            {
                
                //Yes, component does contain a translucent piece.
                return true;
                
            }
            
        }
        
        //Could not find a translucent, Piece component.
        //No, component does not contain a translucent piece.
        return false;

    }
    
    /**
        Used to get the piece currently on this <code>BoardLocation</code>.
        @return Piece Piece currently on this BoardLocation.
    */
    public Piece getPiece ()
    {
        
        //Cycling through all components on this component.
        for ( Component element : this.getComponents() )
        {
            
            //Checking if current component is a Piece component.
            if ( element instanceof Piece )
            {
                
                //Returning Piece component on this BoardLocation.
                return ( Piece ) element;
                
            }
            
        }
        
        //No Piece component found.
        return null;

    }
    
    /**
        Used to get the Type of this <code>BoardLocation</code>.
        @return BoardLocation.Type Type of this <code>BoardLocation</code>.
    */
    public BoardLocation.Type getType () { return this.type; }

    /**
        Returns a repesentation of this <code>BoardLocation</code> as a <code>String</code>
        @return String
    */
    public String toString () { return "BoardLocation: x=" + this.getX() + ", y=" + this.getY() + ", width=" + this.getWidth() + ", contains piece: " + this.containsPiece(); }

    //Methods required by MouseListener interface
    public void mouseClicked ( MouseEvent event ) {}
    public void mouseReleased ( MouseEvent event ) {}
    
    /**
        Listener called when user's mouse enters this BoardLocation's area.
        Adds a translucent piece to this location, if a move here would be valid.
        @param MouseEvent Data about the user's actions.
        @return void
    */
    public void mouseEntered ( MouseEvent event )
    {
        
        //Getting parent of this BoardLocation.
        Board myBoard = ( Board ) BoardLocation.this.getParent();
        
        //Code used to display a translucent piece of the current player.
        
        //Checking if this BoardLocation does not contain a piece, the move to this location is valid, and that
        //the player's last move was not to this BoardLocation ( a violation of the Ko Rule ).
        if ( !this.containsPiece() && !myBoard.isMoveIllegal( this ) && !this.equals( BoardLocation.lastLocation ) )
        {

            Piece piece;
            
            //Creating a piece based on the color of the current player.
            if ( myBoard.getTurn() == Player.Color.BLACK ) { piece = new Piece( Player.Color.BLACK, Piece.IS_TRANSPARENT ); }
            else { piece = new Piece( Player.Color.WHITE, Piece.IS_TRANSPARENT ); }

            //Adding this temporary piece to this BoardLocation.
            this.add( piece );
            
            //Ensuring this event will only be used once.
            event.consume();

        }

    }

    /**
        Listener called when user's mouse exits this <code>BoardLocation</code>'s area.
        @param MouseEvent Data about the user's actions.
        @return void
    */
    public void mouseExited ( MouseEvent event )
    {
        
        //Removing temporary pieces from this BoardLocation.
        this.removeTranslucentPieces();

        //Ensuring this event will only be used once.
        event.consume();

    }
    
    /**
        Listener called when user presses down on mouse button within <code>BoardLocation</code>'s area.
        @param MouseEvent Data abotu the user's actions.
        @return void
    */
    public void mousePressed ( MouseEvent event )
    {
        
        //Checking if user left clicked.
        if ( event.getButton() == MouseEvent.BUTTON1 )
        {
            
            //Getting parent component.
            Board myBoard = ( Board ) this.getParent();
            
            //Checking that there is no solid piece, or is a translucent piece within this BoardLocation. Checking to make sure this move is valid,
            //and does not violate the Ko Rule.
            if ( ( !this.containsPiece() || this.containsTranslucentPiece() ) && !this.equals( BoardLocation.lastLocation ) && !myBoard.isMoveIllegal( this ) )
            {

                Piece piece;
                
                //Creating a piece based on the current player's color.
                if ( myBoard.getTurn() == Player.Color.BLACK ) { piece = new Piece( Player.Color.BLACK, Piece.IS_NOT_TRANSPARENT ); }
                else { piece = new Piece( Player.Color.WHITE, Piece.IS_NOT_TRANSPARENT ); }
                
                //Adding piece to this BoardLocation.
                this.add( piece );
                
                //Switching turn to other player, without passing turn.
                myBoard.switchTurns( Board.TURN_NOT_PASSED );
                
                //Setting previous move to this location.
                BoardLocation.lastLocation = this;
                
                //Ensuring event will only be used once.
                event.consume();

            }

        }
        //Checking if user triggered a popup window.
        else if ( event.isPopupTrigger() )
        {
            
            //Displaying popup menu at location user clicked.
            this.popupMenu.show( this, event.getX(), event.getY() );

        }

    }
    
    /**
        Reads object in after being serialized.
        @param ObjectInputStream File to read object from.
        @return void
    */
    private void readObject ( ObjectInputStream in ) throws IOException, ClassNotFoundException
    {
     
        //Calling default read object method.
        in.defaultReadObject();
        
        //Cycling through components of popup menu.
        for ( Component element : this.popupMenu.getComponents() )
        {
            
            //Checking for JMenuItems.
            if ( element instanceof JMenuItem )
            {
                
                //Re-adding anonymous listener to JMenuItem.
                ( ( JMenuItem ) element ).addActionListener( new ActionListener () {

                    public void actionPerformed ( ActionEvent e )
                    {

                        //Getting parent of this location.
                        Object parent = BoardLocation.this.getParent();

                        if ( parent instanceof Board )
                        {

                            //Passing turn of current player.
                            ( ( Board ) parent ).switchTurns( Board.TURN_PASSED );

                        }

                    }

                });
                
            }
            
        }
        
    }

}