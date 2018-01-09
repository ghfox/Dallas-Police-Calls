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
	//int M_ZOOM = 11;
	final String API = "https://maps.googleapis.com/maps/api/staticmap?";
	String [][] MAP_DATA;
	Image image;
	
    //https://maps.googleapis.com/maps/api/staticmap?size=512x512&zoom=10&center=Dallas&markers=color:red%7Clabel:A%7CPreston+Rd+Dallas+Texas
    
    public MapGen(){
        try {
            BufferedImage img = ImageIO.read(new URL("https://maps.googleapis.com/maps/api/staticmap?size=512x512&zoom=10&center=Dallas"));
            image = img;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public MapGen(String [][] map_data) {
		MAP_DATA = map_data; 
		String url = "" + API;
		url = url + "size=" + M_WIDTH + "x" + M_HEIGHT;
		//url = url + "&zoom=" + M_ZOOM + "&center=Dallas";
		for(int i = 0; i < 15 /*map_data.length*/; i++) //Limit with geocoding
		{
			url = url + buildMarker(i);
		}
		try {
            BufferedImage img = ImageIO.read(new URL(url));
            image = img;
        }
        catch (Exception e){
            e.printStackTrace();
        }
		
	}
	
	public String buildMarker(int a)
	{
		String label = "&markers=color:";
		String color = "red";
		label = label + color + "%7Clabel:" + MAP_DATA[a][0].trim();
		label = label + "%7C" + MAP_DATA[a][1] + "+" + MAP_DATA[a][2].replaceAll(" ","+") + "+Dallas+Texas";
		return label;
	}
		
    
    public void paintComponent (Graphics g){
        if(image == null) return;
        int imageWidth = image.getWidth(this);
        int imageHeight = image.getHeight(this);

        g.drawImage(image, 0, 0, this);
    }

}
