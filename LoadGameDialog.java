import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Component;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import java.io.File;
import java.util.ArrayList;

/**
    Class used to show a custom JOptionPane for loading various files.
    @author Dylan Foster
    @version 5/9/11
*/
public class LoadGameDialog extends JOptionPane
{

    /**
        Frame that displays a <code>JList</code> of file names to user, receives input.
        @param ArrayList<File> files to display in list.
        @return File Selected <code>File</code>
    */
    public static File showLoadDialog ( final ArrayList<File> inSaveFiles )
    {

        if ( !( inSaveFiles instanceof ArrayList ) ) { return null; }

        //Creating array of file names to display in list.
        String [] saveFileNames = new String[ inSaveFiles.size() ];

        //For each file in the input ArrayList.
        for ( int i = 0; i < inSaveFiles.size(); i++ )
        {

            //Getting name of file.
            String fileName = inSaveFiles.get( i ).getName();

            //Removing extension from file.
            fileName = fileName.substring( 0, fileName.indexOf( "." ) );

            //Store file name in String array.
            saveFileNames[ i ] = fileName;

        }

        //Creating and configuring JList.
        final JList fileList = new JList( saveFileNames );
        fileList.setFixedCellHeight( 35 );

        //Creating scroll pane, so JList can contain more files that the frame height would normally allow.
        JScrollPane scrollPane = new JScrollPane( fileList );

        //Creating and configuring option pane containing scroll pane and no buttons.
        final JOptionPane loadDialog = new JOptionPane( scrollPane, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[ 0 ], null );
        fileList.setBackground( loadDialog.getBackground() );

        //Adding anonymous mouse listener to JList.
        fileList.addMouseListener( new MouseListener () {

            public void mousePressed ( MouseEvent e )
            {

                //If user double-clicks on list.
                if ( e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2 )
                {

                    //Gets list item at clicked location.
                    int selectionIndex = fileList.locationToIndex( e.getPoint() );

                    //If list item exists
                    if ( selectionIndex != -1 )
                    {

                        //Sets value of the dialog frame to selected file.
                        loadDialog.setValue( inSaveFiles.get( selectionIndex ) );

                    }

                }

            }

            //Required methods of MouseListener interface.
            public void mouseReleased ( MouseEvent e ) {}
            public void mouseClicked ( MouseEvent e ) {}
            public void mouseEntered ( MouseEvent e ) {}
            public void mouseExited ( MouseEvent e ) {}

        });

        //Creates visible dialog frame from option pane, sets to visible.
        JDialog displayDialog = loadDialog.createDialog( null, "Load Game" );
        displayDialog.setVisible( true );

        //Returns selected value of option pane as a File.
        return ( File ) loadDialog.getValue();

    }

}