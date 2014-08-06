import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.ImageIcon;

/**
    Class used to show a custom JOptionPane for starting a new game.
    @author Dylan Foster
    @version 5/14/11
*/
public class NewGameDialog extends JOptionPane
{
    
    /**
        Shows a dialog for selecting a board size.
        @return int size of board.
    */
    public static int showNewGameDialog ()
    {
        
        //Making image label, adding to frame.
        JLabel logoLabel = new JLabel( new ImageIcon( "logo.png" ) );
        
        //Creating Board size buttons.
        JButton[] buttons = new JButton[ 3 ];
        buttons[ 0 ] = new JButton( "19 x 19" );
        buttons[ 1 ] = new JButton( "13 x 13" );
        buttons[ 2 ] = new JButton( "9 x 9" );

        
        //Creating JOptionPane
        final JOptionPane newGameDialog = new JOptionPane( logoLabel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, buttons, buttons[ 0 ] );
        
        //Adding anonymous listener to 19 x 19 button.
        buttons[ 0 ].addActionListener( new ActionListener () {
           
           public void actionPerformed ( ActionEvent e ) { newGameDialog.setValue( Board.LARGE_BOARD ); } 
            
        });
        
        //Adding anonymous listener to 13 x 13 button.
        buttons[ 1 ].addActionListener( new ActionListener () {
            
           public void actionPerformed ( ActionEvent e ) { newGameDialog.setValue( Board.MEDIUM_BOARD ); }
            
        });
        
        //Adding anonymous listener to 9 x 9 button.
        buttons[ 2 ].addActionListener( new ActionListener () {
            
            public void actionPerformed ( ActionEvent e ) { newGameDialog.setValue( Board.SMALL_BOARD ); }
            
        });
         
        //Adding text to button area of frame.
        ( ( JPanel ) ( newGameDialog.getComponents() )[ 1 ] ).add( new JLabel( "Select a board size: " ) );
        
        //Creates visible dialog frame from option pane, sets to visible.
        JDialog displayDialog = newGameDialog.createDialog( null, "New Game" );
        displayDialog.setVisible( true );

        //Returns selected value of option pane as a File.
        if ( newGameDialog.getValue() instanceof Integer ) { return ( Integer ) newGameDialog.getValue(); }
        else { return -1; }
        
    }
    
}