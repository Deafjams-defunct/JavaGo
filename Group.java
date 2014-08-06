import java.io.Serializable;
import java.util.ArrayList;

/**
    Class representing a group of pieces. Groups are formed when pieces of the same color
    are placed adjacent to each other.
    
    Groups of pieces extend the liberties of the pieces to that of all of the connected pieces.
    
    Whenever a piece is placed on the board, it becomes a group of its own, or is added to an group
    that is adjacent to the piece.
    
    @author Dylan Foster
    @version 5/14/11
*/
public class Group implements Serializable
{
    
    public static final boolean INITIAL_CHECK = true, NOT_INITIAL_CHECK = false;

    private ArrayList<BoardLocation> locationsOfGroup;
    private ArrayList<BoardLocation> liberties;
    private Player.Color color;
    
    /**
        Combines two groups into a single group.
        @param Group firstGroup
        @param Group secondGroup
        @return static Group
    */
    public static Group combine ( Group inGroup1, Group inGroup2 )
    {
        
        //Getting all locations contained in the given groups.
        ArrayList<BoardLocation> newGroupLocations = inGroup1.getLocations();
        newGroupLocations.addAll( inGroup2.getLocations() );
        
        //Returns a new group with the locations of the given groups.
        return new Group( newGroupLocations );
        
    }
    
    /**
        Constructor. Creates a group with a single location.
        @param BoardLocation Location to add to group.
    */
    public Group ( BoardLocation inBoardLocation )
    {
        
        locationsOfGroup = new ArrayList<BoardLocation>();
        liberties = new ArrayList<BoardLocation>();
        
        //Setting color of Group.
        this.color = inBoardLocation.getPiece().getColor();
        
        //Adding given BoardLocation to Group.
        this.add( inBoardLocation );
        
    }
    
    /**
        Constructor. Creates a group with multiple locations.
        @param ArrayList<BoardLocation> Locations to add to group.
    */
    public Group ( ArrayList<BoardLocation> inBoardLocations )
    {
        
        locationsOfGroup = new ArrayList<BoardLocation>();
        liberties = new ArrayList<BoardLocation>();
        
        //Setting color of Group.
        this.color = inBoardLocations.get( 0 ).getPiece().getColor();
        
        //Adding given locations to Group.
        this.add( inBoardLocations );
        
    }
    
    /**
        Adds a single <code>BoardLocation</code> to a <code>Group</code>.
        @param BoardLocation Location to add to group.
        @return void
    */
    public void add ( BoardLocation inBoardLocation )
    {
        
        //Adding given BoardLocation to locations of this group.
        locationsOfGroup.add( inBoardLocation );
        
        //Determining the liberties of the group.
        this.calculateLiberties( INITIAL_CHECK );
        
    }
    
    /**
        Adds multiple <code>BoardLocation</code>s to a <code>Group</code>
        @param ArrayList<BoardLocation> locations
        @return void
    */
    public void add ( ArrayList<BoardLocation> inBoardLocations )
    {
        
        //Adding given BoardLocations to locations of this group.
        locationsOfGroup.addAll( inBoardLocations );
        
        //Determining the liberties of the group.
        this.calculateLiberties( INITIAL_CHECK );
        
    }
    
    /**
        Returns if a <code>BoardLocation</code> is within a <code>Group</code>.
        @param BoardLocation location
        @return boolean
    */
    public boolean contains ( BoardLocation inBoardLocation )
    {
        
        if ( this.locationsOfGroup.contains( inBoardLocation ) ) { return true; }
        else { return false; }
        
    }
    
    /**
        Determines the liberties of a <code>Group</code>.
        @param boolean If this is an initial check of liberties or not.
        @return void
    */
    public void calculateLiberties ( boolean initialCheck )
    {
        
        //Removing all liberties from this group.
        this.liberties.clear();
        
        //Getting Board object this Group is located within.
        Board myBoard = ( Board ) this.locationsOfGroup.get( 0 ).getParent();
        
        //Cycling through locations of Group.
        for ( BoardLocation element : this.locationsOfGroup )
        {
            
            //Getting X and Y coordinates of location.
            int elementX = element.getX();
            int elementY = element.getY();
            
            //Getting surrounding locations.
            BoardLocation locationAbove = myBoard.getLocationAt( elementX, elementY - Board.SIZE_OF_SQUARE );
            BoardLocation locationRight = myBoard.getLocationAt( elementX + Board.SIZE_OF_SQUARE, elementY );
            BoardLocation locationBelow = myBoard.getLocationAt( elementX, elementY + Board.SIZE_OF_SQUARE );
            BoardLocation locationLeft = myBoard.getLocationAt( elementX - Board.SIZE_OF_SQUARE, elementY );
            
            //If location is not already in liberties, and location does not contain a piece, then, add location to liberties of group.
            if ( locationAbove instanceof BoardLocation && !this.liberties.contains( locationAbove ) && !locationAbove.containsPiece() ) { this.liberties.add( locationAbove ); }
            if ( locationRight instanceof BoardLocation && !this.liberties.contains( locationRight ) && !locationRight.containsPiece() ) { this.liberties.add( locationRight ); }
            if ( locationBelow instanceof BoardLocation && !this.liberties.contains( locationBelow ) && !locationBelow.containsPiece() ) { this.liberties.add( locationBelow ); }
            if ( locationLeft instanceof BoardLocation && !this.liberties.contains( locationLeft ) && !locationLeft.containsPiece() ) { this.liberties.add( locationLeft ); }
            
        }
        
        //If this is not an initial check of liberties, and this group has no more liberties, remove the group from the board.
        if ( !initialCheck && this.liberties.size() == 0 ) { myBoard.removeGroup( this ); }
        
    }
    
    /**
        Returns if this <code>Group</code>'s liberties contains the given <code>BoardLocation</code>.
        @param BoardLocation location
        @return boolean
    */
    public boolean libertiesContain ( BoardLocation inBoardLocation )
    {
        
        //Cycling through liberties of group.
        for ( BoardLocation element : this.liberties )
        {
            
            //Getting X and Y location of location.
            int elementX = element.getX();
            int elementY = element.getY();
            
            //If X and Y coordinates of given location matches current location, this group contains given location.
            if ( elementX == inBoardLocation.getX() && elementY == inBoardLocation.getY() ) { return true; }
            
        }
        
        //Group does not contain given location.
        return false;
        
    }
    
    /**
        Returns all <code>BoardLocation</code>s contained in this <code>Group</code>.
        @return ArrayList<BoardLocation>
    */
    public ArrayList<BoardLocation> getLocations () { return this.locationsOfGroup; }
    
    /**
        Returns all <code>BoardLocation</code>s representing the liberties of this group.
        @return ArrayList<BoardLocation>
    */
    public ArrayList<BoardLocation> getLiberties () { return this.liberties; }
    
    /**
        Returns the color of the <code>Piece</code>s within this <code>Group</code>.
        @return Player.Color
    */
    public Player.Color getColor () { return this.color; }
    
    /**
        Returns the number of liberties this <code>Group</code> has.
        @return int
    */
    public int getLibertyCount () { return this.liberties.size(); }
    
    /**
        Returns if this <code>Group</code> would be captured by a move at the given <code>BoardLocation</code>
        @param BoardLocation Location to check.
        @return boolean
    */
    public boolean wouldBeCapturedByMoveAt ( BoardLocation inLocation )
    {
        
        //Checks if liberties contain the given location, and if this group only has one liberty remaining.
        //If both are true, this group would not be captured by a move at the given location.
        if ( this.libertiesContain( inLocation ) && this.getLibertyCount() == 1 ) { return true; }
        
        //Group would not be captured by a move at the given location.
        return false;
        
    }
    
}