package dallaspolicecalls;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.net.URL;
import javax.swing.JComponent;

public class MapGen extends JComponent{
	
	int M_HEIGHT = 512;
	int M_WIDTH = 512;
	int M_ZOOM = 11;
	
	Image image;
    
    public MapGen(){
        try {
            BufferedImage img = ImageIO.read(new URL("https://maps.googleapis.com/maps/api/staticmap?size=512x512&zoom=10&center=Dallas&markers=color:red%7Clabel:A%7CPreston+Rd+Dallas+Texas"));
            image = img;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void paintComponent (Graphics g){
        if(image == null) return;
        int imageWidth = image.getWidth(this);
        int imageHeight = image.getHeight(this);

        g.drawImage(image, 0, 0, this);
    }

}
