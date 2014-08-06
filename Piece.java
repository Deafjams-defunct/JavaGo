import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.ObjectInputStream;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
    Class representing a single piece in play, of either color.
    @author Dylan Foster
    @version 5/13/11
*/
public class Piece extends JComponent implements Serializable
{
    
    //Constants for transparency of piece.
    public static final boolean IS_TRANSPARENT = true;
    public static final boolean IS_NOT_TRANSPARENT = false;
    
    private Player.Color color;
    private boolean translucent;
    private transient BufferedImage pieceImage;
    
    /**
        Constructor. Creates a new piece with given color and transparency.
        @param Player.Color Color of the piece.
        @param boolean If piece is translucent or not.
    */
    public Piece ( Player.Color inColor, boolean inTranslucent )
    {
        
        //Assigning parameters to instance variables.
        this.color = inColor;
        this.translucent = inTranslucent;
        
        File pieceFile;
        
        //Assigning file the proper image, based on the given parameters.
        if ( inColor == Player.Color.BLACK && !inTranslucent ) { pieceFile = new File( "blackpiece.png" ); }
        else if ( inColor == Player.Color.BLACK && inTranslucent ) { pieceFile = new File( "blackpiecetranslucent.png" ); }
        else if ( inColor == Player.Color.WHITE && !inTranslucent ) { pieceFile = new File( "whitepiece.png" ); }
        else { pieceFile = new File( "whitepiecetranslucent.png" ); }
        
        //Reading image from file, assigning to instance variable.
        try { this.pieceImage = ImageIO.read( pieceFile ); }
        catch ( IOException e )
        {
            
            //Could not find piece's image file in program's folder, unable to proceed.
            System.out.println( "Could not find piece's image file." );
            System.exit( 1 );
            
        }

    }
    
    /**
        Method to paint the image within a <code>Piece</code> object.
        @param Graphics Graphics to write to.
        @return void
    */
    public void paintComponent ( Graphics g )
    {
        
        //Draws image of Piece at top left of component.
        g.drawImage( this.pieceImage, 0, 0, null );

    }
    
    /**
        Returns if this <code>Piece</code> is translucent or not.
        @return boolean
    */
    public boolean isTranslucent ()
    {

      if ( this.translucent ) { return true; }
      else { return false; }

    }
    
    /**
        Returns color of this <code>Piece</code>.
        @return Player.Color
    */
    public Player.Color getColor ()
    {
        
        if ( this.color == Player.Color.BLACK ) { return Player.Color.BLACK; }
        else { return Player.Color.WHITE; }
        
    }
    
    /**
        Recreates this <code>Piece</code> object after being serialized.
        @param ObjectInputStream Stream of Object to read.
        @return void
    */
    private void readObject ( ObjectInputStream in ) throws IOException, ClassNotFoundException
    {
        
        //Calling default readObject method.
        in.defaultReadObject();
        
        //As BufferImage is not serializable, we must recreate it when loading object back into program.
        File pieceFile;
        
        //Assigning file the proper image, based on instance variables.
        if ( this.color == Player.Color.BLACK && !this.translucent ) { pieceFile = new File( "blackpiece.png" ); }
        else if ( this.color == Player.Color.BLACK && this.translucent ) { pieceFile = new File( "blackpiecetranslucent.png" ); }
        else if ( this.color == Player.Color.WHITE && !this.translucent ) { pieceFile = new File( "whitepiece.png" ); }
        else { pieceFile = new File( "whitepiecetranslucent.png" ); }
        
        //Reading in image file, assigning to instance variable.
        try { this.pieceImage = ImageIO.read( pieceFile ); }
        catch ( IOException e )
        {
            
            //Could not find piece's image file in program's folder, unable to proceed.
            System.out.println( "Could not find piece's image file." );
            System.exit( 1 );
            
        }
        
    }
    
    /**
        Returns a string representation of this <code>Piece</code>.
        @return String
    */
    public String toString () { return "Piece: x=" + this.getX() + ", y=" + this.getY() +  ", Color=" + this.getColor(); }

}