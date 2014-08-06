import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.awt.Toolkit;
import java.awt.Dimension;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
    Class that represents the actual game board, as well as controls the logic ( rules ) of the game.
    @author Dylan Foster
    @version 5/16/11
*/
public class Board extends JComponent implements Serializable
{

    public static final int SIZE_OF_SQUARE = 30, LARGE_BOARD = 19, MEDIUM_BOARD = 13, SMALL_BOARD = 9;
    public static final boolean TURN_PASSED = true, TURN_NOT_PASSED = false;

    private BoardLocation[][] board;
    private Player.Color turn;
    private ArrayList<Group> groups;
    private transient BufferedImage boardImage;
    private boolean lastTurnPassed;
    private int whiteScore;
    private int blackScore;
    private BoardLocation previousWhiteMove;
    private BoardLocation previousBlackMove;
    private String saveName;
    
    /**
        Constructor. Creates new instance of a Board at the specified size.
        @param int Size of board.
    */
    public Board ( int boardSize )
    {
        
        //Sets first turn to be Black's. Black always goes first in Go.
        this.turn = Player.Color.BLACK;
        
        //Creates new array of BoardLocations to serve as a Goban.
        this.board = new BoardLocation[ boardSize ][ boardSize ];
        
        //Sets size of component.
        this.setSize( boardSize * SIZE_OF_SQUARE + Go.BORDER_BUFFER, boardSize * SIZE_OF_SQUARE + Go.BORDER_BUFFER );
        
        //Creates a blank ArrayList of groups, as Board does not currently have any Groups on it.
        this.groups = new ArrayList<Group>();
        
        //Cycling through all columns of array.
        for ( int i = 0; i < this.board.length; i++ )
        {
            
            //Cycling through all rows of array.
           for ( int j = 0; j < this.board[ i ].length; j++ )
           {
               
               BoardLocation.Type type;
               
               //Determines the Type of this BoardLocation. This effects the BoardLocation's liberties.
               if ( i == 0 && j == 0 ) { type = BoardLocation.Type.TOP_LEFT; }
               else if ( i == boardSize - 1 && j == 0 ) { type = BoardLocation.Type.TOP_RIGHT; }
               else if ( i == boardSize - 1 && j == boardSize - 1 ) { type = BoardLocation.Type.BOTTOM_RIGHT; }
               else if ( i == 0 && j == boardSize - 1 ) { type = BoardLocation.Type.BOTTOM_LEFT; }
               else if ( i == 0 ) { type = BoardLocation.Type.LEFT; }
               else if ( j == 0 ) { type = BoardLocation.Type.TOP; }
               else if ( i == boardSize - 1 ) { type = BoardLocation.Type.RIGHT; }
               else if ( j == boardSize - 1 ) { type = BoardLocation.Type.BOTTOM; }
               else { type = BoardLocation.Type.MAIN; }
               
               //Creates a new BoardLocation at the correct location, size and type.
               this.board[ i ][ j ] = new BoardLocation( i * SIZE_OF_SQUARE + Go.BORDER_BUFFER, j * SIZE_OF_SQUARE + Go.BORDER_BUFFER, SIZE_OF_SQUARE, type );
               
               //Adds BoardLocation to this Board component.
               this.add( this.board[ i ][ j ] );

           }

        }
        
        File goban;
        
        //Sets file to read to correct image, based on given board size.
        if ( this.board.length == 19 ) { goban = new File( "19x19goban.png" ); }
        else if ( this.board.length == 13 ) { goban = new File( "13x13goban.png" ); }
        else { goban = new File( "9x9goban.png" ); }
        
        //Reads image of board and stores it in an instance variable.
        try { this.boardImage = ImageIO.read( goban ); }
        catch ( IOException e )
        {
            
            //Cannot find image of the board. Cannot continue.
            System.out.println( "Board image not found." );
            System.exit( 1 );
            
        }

    }

    /**
        Paints the Board.
        @param Graphics Graphics to write to.
        @return void
    */
    public void paintComponent ( Graphics g )
    {
        
        //Draws the image of this Board.
        g.drawImage( this.boardImage, Go.BORDER_BUFFER - 1, Go.BORDER_BUFFER - 1, null );

    }
    
    /**
        Changes the current turn to opposite player.
        @param boolean If the turn was passed or not.
    */
    public void switchTurns ( boolean inTurnPassed )
    {
        
        //Checks if the last turn was passed, and this turn was passed.
        if ( this.lastTurnPassed && inTurnPassed )
        {
            
            //Two consecutive turns passed ( one from each player ) marks the end of the game.
            
            //Determines the score, and ends the game.
            this.determineScore();
            this.end();
            
            //Exit method.
            return;
            
        }
        
        //Sets if the last turn was passed to the given value.
        this.lastTurnPassed = inTurnPassed;
        
        //Changes the turn to other player.
        if ( this.turn == Player.Color.BLACK ) { this.turn = Player.Color.WHITE; }
        else { this.turn = Player.Color.BLACK; }
        
        //Refreshes all piece's liberties.
        //This ensures any pieces that were captured by a move during the previous turn are removed from the board
        //before the next turn.
        for ( int i = 0; i < this.groups.size(); i++ ) { this.groups.get( i ).calculateLiberties( Group.NOT_INITIAL_CHECK ); }

    }
    
    /**
        Adds a <code>Piece</code> to the board at the given location.
        @param BoardLocation Location to add a peice.
        @return void
    */
    public void addPieceAt ( BoardLocation inBoardLocation )
    {
        
        //Sets the last move to the given location.
        if ( this.turn == Player.Color.BLACK ) { this.previousBlackMove = inBoardLocation; }
        else { this.previousWhiteMove = inBoardLocation; }
        
        ArrayList<Group> neighbouringGroups = new ArrayList<Group>();
        
        //Getting all friendly groups that contain a piece that is in one of the four adjacent BoardLocations.
        for ( int i = 0; i < this.groups.size(); i++ )
        {
            
            if ( this.groups.get( i ).libertiesContain( inBoardLocation ) && this.groups.get( i ).getColor() == inBoardLocation.getPiece().getColor() )
            {
                
                neighbouringGroups.add( this.groups.get( i ) );
                
            }
            
        }
        
        //Checking if no groups ( and therefore, no pieces ) are adjacent to the given location.
        if ( neighbouringGroups.size() == 0 )
        {
            
            //Creating a new Group with this Location, as it cannot be added to another group.
            this.groups.add( new Group( inBoardLocation ) );
            
        }
        //Checking if one group is neighbouring.
        else if ( neighbouringGroups.size() == 1 )
        {
            
            //Add piece to friendly adjacent group.
            neighbouringGroups.get( 0 ).add( inBoardLocation );
            
        }
        //More than one neighbouring friendly groups...
        else
        {
            
            //Combining first two groups.
            Group newGroup = Group.combine( neighbouringGroups.get( 0 ), neighbouringGroups.get( 1 ) );
            
            //Combining all subsequent groups into a single group.
            for ( int i = 2; i < neighbouringGroups.size(); i++ )
            {
                
                newGroup = Group.combine( newGroup, neighbouringGroups.get( i ) );
                
            }
            
            //Adding new move to newly formed group.
            newGroup.add( inBoardLocation );
            
            //Removing all previous groups, as they have been combined into a new, single, group.
            for ( int i = 0; i < neighbouringGroups.size(); i++ )
            {
                
                this.groups.remove( neighbouringGroups.get( i ) );
                
            }
            
            //Add new group to Board.
            this.groups.add( newGroup );
            
        }
        
    }
    
    /**
        Removes a group from the <code>Board</code>.
        @param Group Group to remove.
        @return void
    */
    public void removeGroup ( Group inGroup )
    {
        
        //Removes the given Group from the board.
        this.groups.remove( inGroup );
        
        //Gets all BoardLocations containing Pieces within the group. ( All locations in a Group inherently contain a piece. )
        ArrayList<BoardLocation> locations = inGroup.getLocations();
        
        //Remove all Pieces within given Group from Board.
        for ( int i = 0; i < locations.size(); i++ ) { locations.get( i ).removePiece(); }
        
    }
    
    /**
        Sets the save name of this <code>Board</code>
        @param String name
        @return void
    */
    public void setSaveName ( String inName )
    {
        
        this.saveName = inName;
        
    }
    
    /**
        Determines if a move is illegal, as defined by the rules of Go.
        @param BoardLocation
    */
    public boolean isMoveIllegal ( BoardLocation inBoardLocation )
    {
        
        //Checking if player making move is moving to the same location they moved to last game.
        //If they are, it is a violation of the Ko rule, and, therefore, illegal.
        if ( this.turn == Player.Color.BLACK )
        {
            
            if ( inBoardLocation.equals( previousBlackMove ) ) { return true; }
            
        }
        else
        {
            
            if ( inBoardLocation.equals( previousWhiteMove ) ) { return true; }
            
        }
        
        ArrayList<Group> neighbouringGroups = new ArrayList<Group>();
        
        //Getting all neighbouring group, both enemy and friendly.
        for ( int i = 0; i < this.groups.size(); i++ )
        {
            
            if ( this.groups.get( i ).libertiesContain( inBoardLocation ) )
            {
                
                neighbouringGroups.add( this.groups.get( i ) );
                
            }
            
        }
        
        //If there are no neighbouring groups, move it legal.
        if ( neighbouringGroups.size() == 0 ) { return false; }
        
        //If location is not surrounded by pieces, the move is legal.
        if ( !this.locationIsSurrounded( inBoardLocation ) ) { return false; }
        
        //Past this point, all rules apply to a surrounded piece.
        
        ArrayList<Group> enemyGroups = new ArrayList<Group>();
        ArrayList<Group> friendlyGroups = new ArrayList<Group>();
        
        //Splitting all groups into friendly and enemy groups.
        for ( int i = 0; i < neighbouringGroups.size(); i++ )
        {
            
            Group currentGroup = neighbouringGroups.get( i );
            
            if ( currentGroup.getColor() == this.turn ) { friendlyGroups.add( currentGroup ); }
            else { enemyGroups.add( currentGroup ); }
            
        }
        
        boolean wouldCaptureAnEnemyGroup = false;
        
        //Cycling through enemy groups.
        for ( Group element : enemyGroups )
        {
            
            //If enemy group would be captured, set wouldCaptureAnEnemyGroup to true.
            if ( element.wouldBeCapturedByMoveAt( inBoardLocation ) ) { wouldCaptureAnEnemyGroup = true; }
            
        }
        
        boolean allFriendlyGroupsHaveOneLiberty = true;
        
        //Cycling through friendly groups.
        for ( Group element : friendlyGroups )
        {
            
            //If any group has more than one liberty, set allFriendlyGroupsHaveOneLiberty to false.
            if ( element.getLibertyCount() > 1 ) { allFriendlyGroupsHaveOneLiberty = false; }
            
        }
        
        //If any friendly group has more than one liberty, move is legal.
        if ( !allFriendlyGroupsHaveOneLiberty ) { return false; }
        
        //If there are no enemy groups, and would not capture an enemy group, move is illegal.
        if ( enemyGroups.size() == 0 && !wouldCaptureAnEnemyGroup ) { return true; }
        
        //If there is more than 0 enemy groups, all friendly groups have one liberty, and would not capture an enemy group,
        //move is illegal.
        if ( enemyGroups.size() > 0 && allFriendlyGroupsHaveOneLiberty && !wouldCaptureAnEnemyGroup ) { return true; }
        
        //If all friendly groups have one liberty, and there are no enemy groups, move is illegal.
        if ( allFriendlyGroupsHaveOneLiberty && enemyGroups.size() == 0 ) { return true; }
        
        //If all friendly groups have one liberty, and would not capture an enemy groups, move is illegal.
        if ( allFriendlyGroupsHaveOneLiberty && !wouldCaptureAnEnemyGroup ) { return true; }
        
        //If move does not meet any of the above conditions, it is legal.
        return false;
        
    }
        
    public BoardLocation getLocationAt ( int x, int y )
    {
        
        Component component = this.getComponentAt( x, y );
        
        if ( component instanceof BoardLocation ) { return ( BoardLocation ) component; }
        
        return null;
        
    }
    
    /**
        Returns the color of the player, who's turn it currently is.
        @return Player.Color
    */
    public Player.Color getTurn () { return this.turn; }
    
    /**
        Returns this <code>Board</code>'s save name.
    */
    public String getSaveName () { return this.saveName; }
    
    /**
        Returns the size of the board.
        @return int
    */
    public int getBoardSize () { return this.board.length; }
    
    /**
        Returns if this <code>Board</code> has been saved.
        @return boolean
    */
    public boolean hasBeenSaved ()
    {
        
        if ( this.saveName instanceof String ) { return true; }
        else { return false; }
        
    }
    
    /**
        Displays a new window, showing final score of game.
        @return void
    */
    private void end ()
    {
        
        //Creates a new frame.
        final JFrame gameOverFrame = new JFrame( "Game Over" );
        
        //Configuring frame.
        gameOverFrame.setSize( 400, 200 );
        gameOverFrame.setResizable( false );
        
        //Centering frame.
        Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
        gameOverFrame.setLocation( ( int ) ( resolution.getWidth() / 2 ) - ( gameOverFrame.getWidth() / 2 ), ( int ) ( resolution.getHeight() / 2 ) - ( gameOverFrame.getWidth() / 2 ) );
        
        //Creating a JPanel with absolute positioning.
        JPanel panel = new JPanel( null );
        
        //Creating labels with score, new game button and winner image.
        JLabel winner = null;
        final JLabel whiteLabel = new JLabel( "White Score: " + this.whiteScore );
        final JLabel blackLabel = new JLabel( "Black Score: " + this.blackScore );
        JButton newGameButton = new JButton( "New Game" );
        
        //Setting location and sizes of components.
        whiteLabel.setBounds( 10, 37, 100, 15 );
        blackLabel.setBounds( 10, 82, 100, 15 );
        newGameButton.setBounds( 20, 137, 360, 25 );
        
        //Setting winner image to winner's color.
        if ( this.whiteScore > this.blackScore ) { winner = new JLabel( new ImageIcon( "whiteWins.png" ) ); }
        else if ( this.blackScore > this.whiteScore ) { winner = new JLabel( new ImageIcon( "blackWins.png" ) ); }
        else { winner = new JLabel( new ImageIcon( "draw.png" ) ); }
        
        //Setting bounds of winner image.
        winner.setBounds( 190, 15, 190, 120 );
        
        //Listener for New Game button.
        class newGameButtonListener implements ActionListener
        {
            
            //Called when button is pressed.
            public void actionPerformed ( ActionEvent event )
            {
                
                //Hides game over frame.
                gameOverFrame.setVisible( false );
                
                //Resets the board.
                Board.this.reset( NewGameDialog.showNewGameDialog() );
                
                //Resets labels.
                whiteLabel.setText( "White Score: " );
                blackLabel.setText( "Black Score: " );
                
            }
            
        }
        
        //Adds listener to new game button.
        newGameButton.addActionListener( new newGameButtonListener() );
        
        //Add labels and buttons to frame.
        panel.add( whiteLabel );
        panel.add( blackLabel );
        panel.add( newGameButton );
        panel.add( winner );
        
        //Add panel to frame.
        gameOverFrame.add( panel );
        
        //Setting newGameButton to default button of frame.
        gameOverFrame.getRootPane().setDefaultButton( newGameButton );
        
        //Make frame visible.
        gameOverFrame.setVisible( true );
        
    }
    
    /**
        Resets the board to it's original state.
        @return void
    */
    private final void reset ( int boardSize )
    {
        
        //Getting game.
        Go game = ( Go ) this.getParent().getParent().getParent().getParent().getParent();
        
        game.replaceBoard( new Board( boardSize ) );
        
    }
    
    /**
        Determines if a location is surrounded, with no regard to piece color.
        @param BoardLocation Location to check if surrounded.
        @return boolean
    */
    private boolean locationIsSurrounded ( BoardLocation inBoardLocation )
    {
        
        boolean locationIsSurrounded = false;
        
        //Getting surrounding locations.
        BoardLocation aboveLocation = this.getLocationAt( inBoardLocation.getX(), inBoardLocation.getY() - SIZE_OF_SQUARE );
        BoardLocation rightLocation = this.getLocationAt( inBoardLocation.getX() + SIZE_OF_SQUARE, inBoardLocation.getY() );
        BoardLocation belowLocation = this.getLocationAt( inBoardLocation.getX(), inBoardLocation.getY() + SIZE_OF_SQUARE );
        BoardLocation leftLocation = this.getLocationAt( inBoardLocation.getX() - SIZE_OF_SQUARE, inBoardLocation.getY() );
        
        boolean containsAboveLocation = false;
        boolean containsRightLocation = false;
        boolean containsBelowLocation = false;
        boolean containsLeftLocation = false;
        
        //Determining if each location contains a piece.
        if ( aboveLocation instanceof BoardLocation && aboveLocation.containsPiece() ) { containsAboveLocation = true; }
        if ( rightLocation instanceof BoardLocation && rightLocation.containsPiece() ) { containsRightLocation = true; }
        if ( belowLocation instanceof BoardLocation && belowLocation.containsPiece() ) { containsBelowLocation = true; }
        if ( leftLocation instanceof BoardLocation && leftLocation.containsPiece() ) { containsLeftLocation = true; }
        
        //Getting type of given BoardLocation.
        BoardLocation.Type inBoardLocationType = inBoardLocation.getType();
        
        //Determining if given BoardLocation's on board liberties are filled by pieces at surrounding location.
        if ( inBoardLocationType == BoardLocation.Type.TOP_LEFT )
        {
            
            //Location is in top left corner.
        
            if ( containsRightLocation && containsBelowLocation )
            {

                locationIsSurrounded = true;
        
            }
        
        }
        else if ( inBoardLocationType == BoardLocation.Type.TOP )
        {
            
            //Location is in top row.
        
            if ( containsRightLocation && containsBelowLocation && containsLeftLocation )
            {
            
                locationIsSurrounded = true;
                     
            }
        
        }
        else if ( inBoardLocationType == BoardLocation.Type.TOP_RIGHT )
        {
            
            //Location is in top right corner.
        
            if ( containsBelowLocation && containsLeftLocation )
            {
            
                locationIsSurrounded = true;
            
            }
        
        }
        else if ( inBoardLocationType == BoardLocation.Type.RIGHT )
        {
            
            //Location is in right column.
        
            if ( containsAboveLocation && containsBelowLocation && containsLeftLocation )
            {
            
                locationIsSurrounded = true;           
            }
        
        }
        else if ( inBoardLocationType == BoardLocation.Type.BOTTOM_RIGHT )
        {
            
            //Location is in bottom right corner.
        
            if ( containsAboveLocation && containsLeftLocation )
            {
            
                locationIsSurrounded = true;
            
            }
        
        }
        else if ( inBoardLocationType == BoardLocation.Type.BOTTOM )
        {
            
            //Location is in bottom row.
        
            if ( containsAboveLocation && containsRightLocation && containsLeftLocation )
            {
            
                locationIsSurrounded = true;
            
            }
        
        }
        else if ( inBoardLocationType == BoardLocation.Type.BOTTOM_LEFT )
        {
            
            //Location is in bottom left corner.
        
            if ( containsAboveLocation && containsRightLocation )
            {
            
                locationIsSurrounded = true;
            
            }
        
        }
        else if ( inBoardLocationType == BoardLocation.Type.LEFT )
        {
            
            //Location is in left column.
        
            if ( containsAboveLocation && containsBelowLocation && containsRightLocation )
            {
            
                locationIsSurrounded = true;
            
            }
        
        }
        else
        {
            
            //Location is in middle of board.
        
            if ( containsAboveLocation && containsRightLocation && containsBelowLocation && containsLeftLocation )
            {

                locationIsSurrounded = true;

            }
        
        }
        
        return locationIsSurrounded;
        
    }
    
    /**
        Determines the final score of the game through territory scoring.
        @return void
    */
    private void determineScore ()
    {
        
        int blackScore = 0;
        ArrayList<BoardLocation> territory = new ArrayList<BoardLocation>();
        
        //Cycling through groups.
        for ( Group element : this.groups )
        {
            
            //If color of group is black.
            if ( element.getColor() == Player.Color.BLACK )
            {
                
                //Add all liberties to territory.
                territory.addAll( element.getLiberties() );
                
            }
            
        }
        
        //Cycling through groups.
        for ( Group element : this.groups )
        {
            
            //If color of group is white.
            if ( element.getColor() == Player.Color.WHITE )
            {
                
                //Cycling through liberties of group.
                for ( BoardLocation locationToCheck : element.getLiberties() )
                {
                    
                    //If black territory contains white liberty, remove location from black territory, as it is contested.
                    if ( territory.contains( locationToCheck ) ) { territory.remove( locationToCheck ); }
                    
                }
                
            }
            
        }
        
        //Sets black score
        this.blackScore = territory.size();
        
        //Reset ArrayList containing territories
        territory.clear();
        
        //Cycling through groups.
        for ( Group element : this.groups )
        {
            
            //If group is white.
            if ( element.getColor() == Player.Color.WHITE )
            {
                
                //Add all liberties to territory.
                territory.addAll( element.getLiberties() );
                
            }
            
        }
        
        //Cycling through groups.
        for ( Group element : this.groups )
        {
            
            //If group is black.
            if ( element.getColor() == Player.Color.BLACK )
            {
                
                //Cycling through group's liberties.
                for ( BoardLocation locationToCheck : element.getLiberties() )
                {
                    
                    //If white territory contains black liberty, remove location from white territory, as it is contested.
                    if ( territory.contains( locationToCheck ) ) { territory.remove( locationToCheck ); }
                    
                }
                
            }
            
        }
        
        //Set white's score.
        this.whiteScore = territory.size();
        
    }
    
    /**
        Recreates this <code>Board</code> after being serialized.
        @param ObjectInputStream Stream of object to read.
        @return void
    */
    private void readObject ( ObjectInputStream in ) throws IOException, ClassNotFoundException
    {
        
        //Calls default readObject method.
        in.defaultReadObject();
        
        //As BufferImage is not serializable, we must recreate it when loading object back into program.
        File goban;
        
        //Assigning file the proper image, based on an instance variable.
        if ( this.board.length == 19 ) { goban = new File( "19x19goban.png" ); }
        else if ( this.board.length == 13 ) { goban = new File( "13x13goban.png" ); }
        else { goban = new File( "9x9goban.png" ); }
        
        //Reading in image and setting to instance variable.
        try { this.boardImage = ImageIO.read( goban ); }
        catch ( IOException e )
        {
            
            //Board image not found in program's folder. Unable to continue.
            System.out.println( "Board image not found." );
            System.exit( 1 );
            
        }
        
    }
    
}