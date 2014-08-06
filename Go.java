import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.URI;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import java.util.ArrayList;

/**
    Main window of program. Contains current playing Board, handler of UI and IO of program.
    @author Dylan Foster
    @version 5/16/11
*/
public class Go extends JFrame
{

    public static final int BORDER_BUFFER = 25;
    private static String OS_NAME;
    private Board gameBoard;
    private JPanel mainPanel;
    private int boardSize;
    private final Tutorial tutorial;

    /**
        Main method. Runs on execution of program.
    */
    public static void main ( String[] args )
    {

        //Setting constant for user's operating system.
        Go.OS_NAME = System.getProperty( "os.name" ).substring( 0, 3 );

        //If user is on a Mac, place menu bar at top of screen, instead of in window.
        if ( Go.OS_NAME.equals( "Mac" ) ) { System.setProperty( "apple.laf.useScreenMenuBar", "true" ); }
        
        

        //Creates a new game.
        Go game = new Go();

    }

    /**
        Constructor, creates a new game frame with board of size <code>boardSize</code>
        @param int Board size, from <code>Board</code>'s constants
    */
    public Go ()
    {

        //Using super's constructor to set title of frame.
        super( "Go" );
        
        //Creating tutorial.
        this.tutorial = new Tutorial();
        
        //Getting board size given by user.
        this.boardSize = NewGameDialog.showNewGameDialog();
    
        //User selected a board size when making new game.
        if ( this.boardSize != -1 )
        {
            
            //Main window constants
            final int FRAME_WIDTH = BORDER_BUFFER + ( Board.SIZE_OF_SQUARE * this.boardSize ) - 3;
            final int FRAME_HEIGHT = BORDER_BUFFER + ( Board.SIZE_OF_SQUARE * this.boardSize ) + 18;

            //Creating a board for new game.
            this.gameBoard = new Board( this.boardSize );

            //Creating panel for frame, adding board. Adding panel to frame.
            mainPanel = new JPanel( null );
            mainPanel.add( this.gameBoard );
            this.add( mainPanel );

            //Assigning one frame size if user is on a Mac, another if user is not on a Mac.
            if ( Go.OS_NAME.equals( "Mac" ) ) { this.setSize( FRAME_WIDTH, FRAME_HEIGHT ); }
            else { this.setSize( FRAME_WIDTH, FRAME_HEIGHT + BORDER_BUFFER ); }

            //Centering window using default toolkit.
            Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
            this.setLocation( ( int ) ( resolution.getWidth() / 2 ) - ( this.getWidth() / 2 ), ( int ) ( resolution.getHeight() / 2 ) - ( this.getHeight() / 2 ) );

            //Configuring frame.
            this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            this.setResizable( false );

            //Creating menu bar and items.
            JMenuBar menuBar = new JMenuBar();
            JMenu fileMenu = new JMenu( "File" );
            JMenuItem newGameItem = new JMenuItem( "New", KeyEvent.VK_N );
            JMenuItem saveGameItem = new JMenuItem( "Save", KeyEvent.VK_S );
            JMenuItem loadGameItem = new JMenuItem( "Open", KeyEvent.VK_L );
            
            //If user is on a Mac.
            if ( OS_NAME.equals( "Mac" ) )
            {
                
                //Set hotkeys to command-( key ).
                newGameItem.setAccelerator( KeyStroke.getKeyStroke( 'N', InputEvent.META_DOWN_MASK ) );
                saveGameItem.setAccelerator( KeyStroke.getKeyStroke( 'S', InputEvent.META_DOWN_MASK ) );
                loadGameItem.setAccelerator( KeyStroke.getKeyStroke( 'O', InputEvent.META_DOWN_MASK ) );
                
            }
            //Assume user is on Windows.
            else
            {
                
                //Set hotkeys to control-( key ).
                newGameItem.setAccelerator( KeyStroke.getKeyStroke( 'N', InputEvent.CTRL_DOWN_MASK ) );
                saveGameItem.setAccelerator( KeyStroke.getKeyStroke( 'S', InputEvent.CTRL_DOWN_MASK ) );
                loadGameItem.setAccelerator( KeyStroke.getKeyStroke( 'O', InputEvent.CTRL_DOWN_MASK ) );
                
            }
            
            //Adding anonymous action listener to file menu items.
            newGameItem.addActionListener( new ActionListener () {
                
                public void actionPerformed ( ActionEvent e ) {
                    
                    //Getting a new board size.
                    int boardSize = NewGameDialog.showNewGameDialog();
                    
                    //If user selected a board size.
                    if ( boardSize != -1 )
                    {
                        
                        //Replace the board with a new one.
                        Go.this.replaceBoard( new Board( boardSize ) );
                        
                    }
                    
                }
                
            });
            
            saveGameItem.addActionListener( new ActionListener () {

                public void actionPerformed ( ActionEvent e ) { Go.this.saveGame(); }

            });

            loadGameItem.addActionListener( new ActionListener () {

                public void actionPerformed ( ActionEvent e ) { Go.this.loadGame(); }

            });

            //Adding menu items to the "File" menu.
            fileMenu.add( newGameItem );
            fileMenu.add( loadGameItem );
            fileMenu.add( new JSeparator() );
            fileMenu.add( saveGameItem );

            //If user is not on a Mac ( assuming user is on Windows ).
            if ( !Go.OS_NAME.equals( "Mac" ) )
            {

                //Creating seperator and Close menu item to replicate Window's common menu structure.
                JMenuItem closeItem = new JMenuItem( "Close", KeyEvent.VK_C );

                //Anonymous listener for Close menu item.
                closeItem.addActionListener ( new ActionListener () {

                    public void actionPerformed ( ActionEvent e ) { System.exit( 0 ); }

                });

                //Adding seperator and Close item to "File" menu.
                fileMenu.add( new JSeparator() );
                fileMenu.add( closeItem );

            }
            
            //Creating help menu.
            JMenu helpMenu = new JMenu( "Help" );
            JMenuItem rulesItem = new JMenuItem( "Rules..." );
            JMenuItem tutorialItem = new JMenuItem( "Tutorial" );
            
            //Adding action listeners to help menu buttons.
            rulesItem.addActionListener( new ActionListener () {
                
                public void actionPerformed ( ActionEvent e )
                {
                    
                    try
                    {
                        
                        //Get user's desktop information.
                        Desktop desktop = Desktop.getDesktop();
                
                        //Open URL in user's default browser.
                        desktop.browse( new URI( "http://en.wikipedia.org/wiki/Go_(game)#Basic_rules" ) );
                        
                    }
                    catch ( Exception ex )
                    {
                        
                        //URI is not malformed.
                        
                    }
                    
                    
                }
                
            });
            
            tutorialItem.addActionListener( new ActionListener () {
                
                public void actionPerformed ( ActionEvent e )
                {
                    
                    //Shows tutorial frame.
                    Go.this.tutorial.setVisible( true );
                    
                }
                
            });
            
            helpMenu.add( rulesItem );
            helpMenu.add( tutorialItem );

            //Adding "File" menu to menu bar. Setting this menu bar to this frame's menu bar.
            menuBar.add( fileMenu );
            menuBar.add( helpMenu );
            this.setJMenuBar( menuBar );

            //Making constructed frame visible.
            this.setVisible( true );
        
        }
        //User selected close button when asking for board size.
        else
        {
            
            //Exit.
            System.exit( 0 );
            
        }

    }
    
    /**
        Gets input from user and saves the current game state.
        @return void
    */
    private final void saveGame ()
    {
        
        String saveGameName = null;
        
        //If game has already been saved
        if ( this.gameBoard.hasBeenSaved() )
        {
            
            //Set name to board's name.
            saveGameName = this.gameBoard.getSaveName();
            
        }
        //Game has not already been saved.
        else
        {
            
            boolean done = false;

            while ( !done )
            {

                //Get save game name from user.
                saveGameName = JOptionPane.showInputDialog( null, "Name your save: ", "Save Game", JOptionPane.PLAIN_MESSAGE );

                //Checking if user gave the saved game a name.
                if ( saveGameName instanceof String && saveGameName.equals( "" ) ) { JOptionPane.showMessageDialog( null, "Please give your save a name.", "Error", JOptionPane.ERROR_MESSAGE ); }
                else { done = true; }
                
                //User closed dialog.
                if ( !( saveGameName instanceof String ) ) { return; }

            }
            
        }
        
        try
        {

            //Writing Board of current game to save file.
            FileOutputStream fileOut = new FileOutputStream( new File( "Saved Games/" + saveGameName + ".gosave" ) );
            ObjectOutputStream objectOut = new ObjectOutputStream( fileOut );

            objectOut.writeObject( this.gameBoard );

            objectOut.close();
        

        }
        catch ( FileNotFoundException e )
        {

            System.out.println( "Encountered a problem while saving." );

        }
        catch ( IOException e )
        {

            System.out.println( "Encountered a problem while saving." );

        }
        
        //Setting board's save name.
        this.gameBoard.setSaveName( saveGameName );
        
    }

    /**
        Gets input from user and loads inputted game.
        @return void
    */
    private final void loadGame ()
    {

        try
        {

            //Determining the saved games directory.
            File savesDirectory = new File( this.getSavesDirectory() );

            ArrayList<File> saveFiles = new ArrayList<File>();

            //Getting all save files from directory.
            for ( File element : savesDirectory.listFiles() )
            {

                String fileName = element.getName();

                String fileExtension = fileName.substring( fileName.lastIndexOf( "." ) );

                if ( fileExtension.equals( ".gosave" ) )
                {

                    saveFiles.add( element );

                }

            }

            //Displaying load game dialog to user, getting input.
            File gameToLoad = LoadGameDialog.showLoadDialog( saveFiles );

            //If user selected an valid file.
            if ( gameToLoad instanceof File )
            {

                //Reading file in, replacing current board with board saved in file.
                FileInputStream fileIn = new FileInputStream( gameToLoad );
                ObjectInputStream objectIn = new ObjectInputStream( fileIn );

                this.replaceBoard( ( Board ) objectIn.readObject() );

                objectIn.close();

            }

        }
        catch ( ClassNotFoundException e )
        {

            System.out.println( "Save file appears to be corrupt." );

        }
        catch ( FileNotFoundException e )
        {

            System.out.println( "Encountered a problem while loading game." );

        }
        catch ( IOException e )
        {

            System.out.println( "Encountered a problem while loading game." );

        }

    }

    /**
        Replaces board currently in this frame.
        @param Board <code>Board</code> to replace current one with.
        @return void
    */
    public void replaceBoard ( Board inBoard )
    {

        //Removing current board from the frame.
        this.mainPanel.remove( this.gameBoard );

        //Drawing an empty board ( removes pieces in play during previous game ).
        this.repaint();

        //Add new board to frame.
        this.mainPanel.add( inBoard );

        //Setting this game's current board to input board.
        this.gameBoard = inBoard;
        
        this.boardSize = this.gameBoard.getBoardSize();
        
        //Declaring new frame size.
        final int FRAME_WIDTH = BORDER_BUFFER + ( Board.SIZE_OF_SQUARE * this.boardSize ) - 3;
        final int FRAME_HEIGHT = BORDER_BUFFER + ( Board.SIZE_OF_SQUARE * this.boardSize ) + 18;
        
        //Assigning one frame size if user is on a Mac, another if user is not on a Mac.
        if ( Go.OS_NAME.equals( "Mac" ) ) { this.setSize( FRAME_WIDTH, FRAME_HEIGHT ); }
        else { this.setSize( FRAME_WIDTH, FRAME_HEIGHT + BORDER_BUFFER ); }

    }

    /**
        Gets current directory of this program.
        @return String
    */
    private String getSavesDirectory ()
    {

        //Making file in program's current directory to determine path to that directory.
        String currentDirectory = new File( "File Used To Get Current Directory" ).getAbsolutePath();

        //Returning string containing program's current directory.
        if ( Go.OS_NAME.equals( "Mac" ) )
        {
            
            //Returning path with forward slashes.
            return currentDirectory.substring( 0, currentDirectory.lastIndexOf( "/" ) + 1 ) + "Saved Games/";

        }
        else
        {
            
            //Returning path with back slashes.
            return currentDirectory.substring( 0, currentDirectory.lastIndexOf( "\\" ) + 1 ) + "\\Saved Games\\";

        }

    }

}