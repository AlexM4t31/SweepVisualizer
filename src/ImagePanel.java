import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

public class ImagePanel extends JPanel  {

    static final String placeholder = "";
    private JLabel imageLabel;
    public ImagePanel() {

    }

    public void setImage(String imagePath) {
        BufferedImage image = null;
        try
        {
            image = ImageIO.read(new File(imagePath));
        } catch ( IOException exc ) {
            // handle
        }

        if ( image != null )
        {
            if ( imageLabel != null )
            {
                remove(imageLabel);
            }

            imageLabel = new JLabel(new ImageIcon(image));

            add(imageLabel);
        }
    }

    public void setPlaceholder(){
        setImage( ImagePanel.placeholder );
    }

}
