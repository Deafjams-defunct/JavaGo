import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.ImageIcon;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
    Class representing a tutorial window.
    
    For future version: create tutorial section object, instantiate with title, image or text.
    @author Dylan Foster
    @version 5/16/11
*/
public class Tutorial extends JFrame
{
    
    private JPanel[] tutorialSections;
    private JPanel mainPanel;
    private int currentSection;
    private JButton nextButton;
    private JButton backButton;

    /**
        Constructor. Builds tutorial window.
    */
    public Tutorial ()
    {
        
        //Setting title of window through super's constructor.
        super( "Tutorial" );
        
        //Setting current tutorial section to 0 and creating sections array.
        this.currentSection = 0;
        this.tutorialSections = new JPanel[ 15 ];
        
        //Creating each individual panel.
        this.createFirstSection();
        this.createSecondSection();
        this.createThirdSection();
        this.createFourthSection();
        this.createFifthSection();
        this.createSixthSection();
        this.createSeventhSection();
        this.createEighthSection();
        this.createNinthSection();
        this.createTenthSection();
        this.createEleventhSection();
        this.createTwelfthSection();
        this.createThirteenthSection();
        this.createFourteenthSection();
        this.createFifteenthSection();
        
        //Creating main panel of window ( tutorial information ), adding first tutorial.
        this.mainPanel = new JPanel( new BorderLayout() );
        this.mainPanel.add( this.tutorialSections[ this.currentSection ], BorderLayout.CENTER );
        
        //Creating and configuring button panel and it's subpanel.
        JPanel buttonPanel = new JPanel( new BorderLayout() );
        buttonPanel.setName( "Buttons" );
        JPanel buttonSubPanel = new JPanel();
        
        //Creating buttons, disabling back button ( cannot go back from 0! ).
        this.nextButton = new JButton( "Next" );
        this.backButton = new JButton( "Back" );
        this.backButton.setEnabled( false );
        
        //Adding anonymous action listeners to buttons.
        nextButton.addActionListener( new ActionListener () {
            
            public void actionPerformed ( ActionEvent e )
            {
                
                //Showing next tutorial panel.
                Tutorial.this.next();
                
            }
            
        });
        
        backButton.addActionListener( new ActionListener () {
            
            public void actionPerformed ( ActionEvent e )
            {
                
                //Showing previous tutorial panel.
                Tutorial.this.back();
                
            }
            
        });
        
        //Adding buttons to button subpanel.
        buttonSubPanel.add( backButton );       
        buttonSubPanel.add( nextButton );
        
        //Adding button subpanel to east section of button panel 
        //Adding button panel to main panel's south section ( keeps buttons in bottom right corner ).
        buttonPanel.add( buttonSubPanel, BorderLayout.EAST );
        this.mainPanel.add( buttonPanel, BorderLayout.SOUTH );
        
        //Adding main panel to frame.
        this.add( mainPanel );
        
        //Configuring frame.
        this.setSize( 500, 400 );
        this.setResizable( false );
        Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation( ( int ) ( resolution.getWidth() / 2 ) - ( this.getWidth() / 2 ), ( int ) ( resolution.getHeight() / 2 ) - ( this.getHeight() / 2 ) );
        
    }
    
    /**
        Changes current tutorial section to the next tutorial section.
        @return void
    */
    private final void next ()
    {
        
        //Cycling through main panel's elements.
        for ( Component element : this.mainPanel.getComponents() )
        {
            
            //If element is not the buttons panel.
            if ( !( element.getName().equals( "Buttons" ) ) )
            {
                
                //Remove the panel.
                this.mainPanel.remove( element );
                
            }
            
        }
        
        //Adding next tutorial panel to main panel. Revalidating and drawing main panel.
        this.mainPanel.add( this.tutorialSections[ ++currentSection ], BorderLayout.CENTER );
        this.mainPanel.revalidate();
        this.mainPanel.repaint();
        
        //If current panel is last panel, disable next button.
        if ( currentSection == tutorialSections.length - 1 ) { this.nextButton.setEnabled( false ); }
        
        //Enable back button ( if you just went forward, you can go back! ).
        this.backButton.setEnabled( true );
        
    }
    
    /**
        Changes current tutorial section to the previous tutorial section.
    */
    private final void back ()
    {
        
        //Cycling through main panel's elements.
        for ( Component element : this.mainPanel.getComponents() )
        {
            
            //If element is not the buttons panel.
            if ( !( element.getName().equals( "Buttons" ) ) )
            {
                
                //Remove the panel.
                this.mainPanel.remove( element );
                
            }
            
        }
        
        //Adding previous tutorial panel to main panel. Revalidating and drawing main panel.
        this.mainPanel.add( this.tutorialSections[ --this.currentSection ], BorderLayout.CENTER );
        this.mainPanel.revalidate();
        this.mainPanel.repaint();
        
        //If current section is the first section, disable back button.
        if ( currentSection == 0 ) { this.backButton.setEnabled( false ); }
        
        //Enable next button ( you just went back, so, you can go forward ).
        this.nextButton.setEnabled( true );
        
    }
    
    /**
        Creates first tutorial section.
        @return void
    */
    private void createFirstSection ()
    {
        
        //Creates and configures panel.
        JPanel firstSection = new JPanel( null );
        firstSection.setName( "Tutorial 1" );
        
        //Creating piece images.
        JLabel whitePiece = new JLabel( new ImageIcon( "whitepiece.png" ) );
        JLabel blackPiece = new JLabel( new ImageIcon( "blackpiece.png" ) );
        blackPiece.setBounds( 152, 150, 29, 29 );
        whitePiece.setBounds( 319, 150, 29, 29 );
        
        //Adding piece images to panel.
        firstSection.add( whitePiece );
        firstSection.add( blackPiece );
        
        //Creating and configuring title.
        JLabel title = new JLabel( "The Basics" );
        title.setFont( new Font( Font.SERIF, Font.BOLD, 36 ) );
        title.setBounds( 165, 10, 170, 36 );
        
        //Adding title to panel.
        firstSection.add( title );
        
        //Creating and configuring text.
        JTextArea text = new JTextArea( "Go is a game between two opposing players.\nOne player uses black pieces, the other white." );
        text.setEditable( false );
        text.setBackground( null );
        text.setBounds ( 112, 290, 350, 36 );
        
        //Adding text to panel.
        firstSection.add( text );
        
        //Placing panel in sections array.
        this.tutorialSections[ 0 ] = firstSection;
        
    }
    
    /**
        Creates second tutorial section.
        @return void
    */
    private void createSecondSection ()
    {
        
        //Creates and configures panel.
        JPanel secondSection = new JPanel( null );
        secondSection.setName( "Tutorial 2" );
        
        //Creating and configuring image.
        JLabel image = new JLabel( new ImageIcon( "Tutorial Images/blackgoesfirst.png" ) );
        image.setBounds( 125, 25, 250, 250 );
        
        //Adding image to panel.
        secondSection.add( image );
        
        //Creating and configuring text.
        JLabel text = new JLabel( "Black always goes first." );
        text.setBounds( 175, 300, 150, 16 );
        
        //Adding text to panel.
        secondSection.add( text );
        
        //Placing panel in sections array.
        this.tutorialSections[ 1 ] = secondSection;
        
    }
    
    /**
        Creates third tutorial section.
        @return void
    */
    private void createThirdSection ()
    {
        
        //Creates and configures panel.
        JPanel thirdSection = new JPanel( null );
        thirdSection.setName( "Tutorial 3" );
        
        //Creating and configuring image.
        JLabel image = new JLabel( new ImageIcon( "Tutorial Images/alternatingTurns.png" ) );
        image.setBounds( 126, 26, 250, 250 );
        
        //Adding image to panel.
        thirdSection.add( image );
        
        //Creating and configuring text.
        JLabel text = new JLabel( "The players then alternate turns." );
        text.setBounds( 145, 300, 210, 16 );
        
        //Adding text to panel.
        thirdSection.add( text );
        
        //Placing panel in sections array.
        this.tutorialSections[ 2 ] = thirdSection;
        
    }
    
    /**
        Creates fourth tutorial section.
        @return void
    */
    private void createFourthSection ()
    {
        
        //Creates and configures panel.
        JPanel fourthSection = new JPanel( null );
        fourthSection.setName( "Tutorial 4" );
        
        //Creating and configuring title.
        JLabel title = new JLabel( "Liberties" );
        title.setFont( new Font( Font.SERIF, Font.BOLD, 36 ) );
        title.setBounds( 180, 10, 140, 36 );
        
        //Adding title to panel.
        fourthSection.add( title );
        
        //Creating and configuring image.
        JLabel image = new JLabel( new ImageIcon( "Tutorial Images/liberties.png" ) );
        image.setBounds( 126, 50, 250, 250 );
        
        //Adding image to panel.
        fourthSection.add( image );
        
        //Placing panel in sections array.
        this.tutorialSections[ 3 ] = fourthSection;
        
    }
    
    /**
        Creates fifth tutorial section.
        @return void
    */
    private void createFifthSection ()
    {
        
        //Creates and configures panel.
        JPanel fifthSection = new JPanel( null );
        fifthSection.setName( "Tutorial 5" );
        
        //Creating and configuring image.
        JLabel image = new JLabel( new ImageIcon( "Tutorial Images/libertyNumbers.png" ) );
        image.setBounds( 126, 26, 250, 250 );
        
        //Adding image to panel.
        fifthSection.add( image );
        
        //Creating and configuring text.
        JTextArea text = new JTextArea( "Each piece has liberties, which are open spaces that are adjacent to the" + 
        "\npiece. Liberties can be taken from a piece by the opponent, by placing a piece in \nanother piece's liberties. Liberties are shown above for their respective pieces." );
        text.setEditable( false );
        text.setBackground( null );
        text.setBounds( 5, 275, 490, 48 );
        
        //Adding text to panel.
        fifthSection.add( text );
        
        //Placing panel in sections array.
        this.tutorialSections[ 4 ] = fifthSection;
        
    }

    /**
        Creates sixth tutorial section.
        @return void
    */
    private void createSixthSection ()
    {
        
        //Creates and configures panel.
        JPanel sixthSection = new JPanel( null );
        sixthSection.setName( "Tutorial 6" );
        
        //Creating and configuring image.
        JLabel image = new JLabel( new ImageIcon( "Tutorial Images/capturing.png" ) );
        image.setBounds( 126, 27, 250, 250 );
        
        //Adding image to panel.
        sixthSection.add( image );
        
        //Creating and configuring text.
        JTextArea text = new JTextArea( "When a piece has no more liberties, that piece is considered\n" +
        "to be captured, and is removed from the board. The black\npiece above no longer has any liberties, and would be captured." );
        text.setEditable( false );
        text.setBackground( null );
        text.setBounds( 50, 275, 400, 48 );
        
        //Adding text to panel.
        sixthSection.add( text );
        
        //Placing panel in sections array.
        this.tutorialSections[ 5 ] = sixthSection;
        
    }
    
    /**
        Creates seventh tutorial section.
        @return void
    */
    private void createSeventhSection ()
    {
        
        //Creates and configures panel.
        JPanel seventhSection = new JPanel( null );
        seventhSection.setName( "Tutorial 7" );
        
        //Creating and configuring image.
        JLabel image = new JLabel( new ImageIcon( "Tutorial Images/units.png" ) );
        image.setBounds( 125, 25, 250, 250 );
        
        //Adding image to panel.
        seventhSection.add( image );
        
        //Creating and configuring text.
        JTextArea text = new JTextArea( "Pieces of the same color can be placed next to each other to\nform a unit. Units have the liberties of all of the pieces they\n contain. " +
        "The above unit contains 3 pieces, with 8 liberties." );
        text.setEditable( false );
        text.setBackground( null );
        text.setBounds( 50, 275, 400, 48 );
        
        //Adding text to panel.
        seventhSection.add( text );
        
        //Placing panel in sections array.
        this.tutorialSections[ 6 ] = seventhSection;
        
    }
    
    /**
        Creates eighth tutorial section.
        @return void
    */
    private void createEighthSection ()
    {
        
        //Creates and configures panel.
        JPanel eighthSection = new JPanel( null );
        eighthSection.setName( "Tutorial 8" );
        
        //Creating and configuring title.
        JLabel title = new JLabel( "Ending the Game" );
        title.setFont( new Font( Font.SERIF, Font.BOLD, 36 ) );
        title.setBounds( 112, 10, 275, 36 );
        
        //Adding title to panel.
        eighthSection.add( title );
        
        //Creating and configuring image.
        JLabel image = new JLabel( new ImageIcon( "Tutorial Images/end.png" ) );
        image.setBounds( 110, 50, 280, 280 );
        
        //Adding image to panel.
        eighthSection.add( image );
        
        //Placing panel in sections array.
        this.tutorialSections[ 7 ] = eighthSection;
        
    }
    
    /**
        Creates ninth tutorial section.
        @return void
    */
    private void createNinthSection ()
    {
        
        //Creates and configures panel.
        JPanel ninthSection = new JPanel( null );
        ninthSection.setName( "Tutorial 9" );
        
        //Creating and configuring image.
        JLabel image = new JLabel( new ImageIcon( "Tutorial Images/pass.png" ) );
        image.setBounds( 110, 0, 280, 280 );
        
        //Adding image to panel.
        ninthSection.add( image );
        
        //Creating and configuring text.
        JTextArea text = new JTextArea( "In order to end a game, both players must pass their turns\nconsecutively. When passing your turn, you do not play a piece,\nand your turns ends." +
        "To pass your turn at any time, right click\non the board and select \"Pass Turn\"." );
        text.setEditable( false );
        text.setBackground( null );
        text.setBounds( 50, 275, 400, 64 );
        
        //Adding text to panel.
        ninthSection.add( text );
        
        //Placing panel in sections array.
        this.tutorialSections[ 8 ] = ninthSection;
        
    }
    
    /**
        Creates tenth tutorial section.
        @return void
    */
    private void createTenthSection ()
    {
        
        //Creates and configures panel.
        JPanel tenthSection = new JPanel( null );
        tenthSection.setName( "Tutorial 10" );
        
        //Creating and configuring image.
        JLabel image = new JLabel( new ImageIcon( "Tutorial Images/scoring.png" ) );
        image.setBounds( 130, 10, 241, 204 );
        
        //Adding image to panel.
        tenthSection.add( image );
        
        //Creating and configuring text.
        JTextArea text = new JTextArea( "Scoring in this version of Go is based on territory. Territory is a\nliberty of a group that is not" + 
        " contested. A contested liberty is a\nliberty that is both a liberty of a black piece and a white piece.\n" + 
        "The player with the most territory at the end of the game is the\nwinner. Above, black has 6 territory, and white has 9." + 
        " The four\ncontested territories are marked with red Xs and do not count\ntowards either player's territory. White is the winner." );
        text.setEditable( false );
        text.setBackground( null );
        text.setBounds( 50, 225, 400, 120 );
        
        //Adding text to panel.
        tenthSection.add( text );
        
        //Placing panel in sections array.
        this.tutorialSections[ 9 ] = tenthSection;
        
    }
    
    /**
        Creates eleventh tutorial section.
        @return void
    */
    private void createEleventhSection ()
    {
        
        //Creates and configures panel.
        JPanel eleventhSection = new JPanel( null );
        eleventhSection.setName( "Tutorial 11" );
        
        //Creating and configuring title.
        JLabel title = new JLabel( "Advanced Rules" );
        title.setFont( new Font( Font.SERIF, Font.BOLD, 36 ) );
        title.setBounds( 125, 10, 250, 36 );
        
        //Adding title to panel.
        eleventhSection.add( title );
        
        //Creating and configuring image.
        JLabel image = new JLabel( new ImageIcon( "Tutorial Images/advanced.png" ) );
        image.setBounds( 110, 50, 280, 280 );
        
        //Adding image to panel.
        eleventhSection.add( image );
        
        //Placing panel in sections array.
        this.tutorialSections[ 10 ] = eleventhSection;
        
    }
    
    /**
        Creates twelfth tutorial section.
        @return void
    */
    private void createTwelfthSection ()
    {
        
        //Creates and configures panel.
        JPanel twelfthSection = new JPanel( null );
        twelfthSection.setName( "Tutorial 12" );
        
        //Creating and configuring image.
        JLabel image = new JLabel( new ImageIcon( "Tutorial Images/ko.png" ) );
        image.setBounds( 125, 15, 250, 250 );
        
        //Adding image to panel.
        twelfthSection.add( image );
        
        //Creating and configuring text.
        JTextArea text = new JTextArea( "The Ko rule prevents a player from placing the board in a state\nthat has already occured." + 
        " Above, if white had just taken a black\npiece at X, then black could not place a piece at X during his\nnext turn, as it would replicate a previous board state." );
        text.setEditable( false );
        text.setBackground( null );
        text.setBounds( 50, 270, 400, 120 );
        
        //Adding text to panel.
        twelfthSection.add( text );
        
        //Placing panel in sections array.
        this.tutorialSections[ 11 ] = twelfthSection;
        
    }
    
    /**
        Creates thirteenth tutorial section.
        @return void
    */
    private void createThirteenthSection ()
    {
        
        //Creates and configures panel.
        JPanel thirteenthSection = new JPanel( null );
        thirteenthSection.setName( "Tutorial 13" );
        
        //Creating and configuring image.
        JLabel image = new JLabel( new ImageIcon( "Tutorial Images/selfcapture.png" ) );
        image.setBounds( 112, -14, 275, 275 );
        
        //Adding image to panel.
        thirteenthSection.add( image );
        
        //Creating and configuring text.
        JTextArea text = new JTextArea( "In Go, capturing your opponent's pieces is always done before\ncapturing your own pieces." +
        " Above, if white places a piece at the\nX location, the black pieces would be captured, and the white\ngroup would have 2 liberties." +
        "If black placed a piece at the X,\nthe white pieces would be captured." );
        text.setEditable( false );
        text.setBackground( null );
        text.setBounds( 50, 260, 400, 130 );
        
        //Adding text to panel.
        thirteenthSection.add( text );
        
        //Placing panel in sections array.
        this.tutorialSections[ 12 ] = thirteenthSection;
        
    }
    
    /**
        Creates fourteenth tutorial section.
        @return void
    */
    private void createFourteenthSection ()
    {
        
        //Creates and configures panel.
        JPanel fourteenthSection = new JPanel( null );
        fourteenthSection.setName( "Tutorial 14" );
        
        //Creating and configuring image.
        JLabel image = new JLabel( new ImageIcon( "Tutorial Images/eyes.png" ) );
        image.setBounds( 125, 0, 250, 275 );
        
        //Adding image to panel.
        fourteenthSection.add( image );
        
        //Creating and configuring text.
        JTextArea text = new JTextArea( "Eyes are configurations of pieces that players can form, that make a\ngroup of pieces very difficult to capture." + 
        " Above, the white group has a\nsingle eye. It is possible to capture this group." + 
        " The black group,\nhowever, has two eyes and can never be captured." );
        text.setEditable( false );
        text.setBackground( null );
        text.setBounds( 25, 275, 450, 140 );
        
        //Adding text to panel.
        fourteenthSection.add( text );
        
        //Placing panel in sections array.
        this.tutorialSections[ 13 ] = fourteenthSection;
        
    }
    
    /**
        Creates fifteenth tutorial section.
        @return void
    */
    private void createFifteenthSection ()
    {
        
        //Creates and configures panel.
        JPanel fifteenthSection = new JPanel( null );
        fifteenthSection.setName( "Tutorial 15" );
        
        //Creating and configuring image.
        JLabel image = new JLabel( new ImageIcon( "Tutorial Images/congratulations.png" ) );
        image.setBounds( 0, 0, 500, 200 );
        
        //Adding image to panel.
        fifteenthSection.add( image );
        
        //Creating and configuring text.
        JLabel text = new JLabel( "You are ready to start a game! Have fun!" );
        text.setBounds( 120, 250, 260, 16 );
        
        //Adding text to panel.
        fifteenthSection.add( text );
        
        //Placing panel in sections array.
        this.tutorialSections[ 14 ] = fifteenthSection;
        
    }

}