package dallaspolicecalls;
/*
 * DallasCalls.java
 * 
 * Copyright 2018 Hal <hal@hal-MS-7793>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 * 
 * 
 */

import java.lang.Thread;
import java.lang.Runnable;
import java.io.Console;
import javax.swing.JFrame;

public class DallasCalls {
	
	static JFrame frame;
	static MapGen map;
	static int W_HEIGHT = 512;
	static int W_WIDTH = 512;
	
	
	public static void main (String args[])  {
		
		frame = new JFrame();
		frame.setTitle("Map");
		frame.setSize(W_WIDTH,W_HEIGHT);
		frame.setVisible(false);
		
		map = new MapGen();
		frame.add(map);
		
		PrintData pd = new PrintData(args);
		Thread dataThread = new Thread(pd);
		dataThread.start();
		
		Console console = System.console();
		
		while(true)
		{
			if (console == null)
				System.exit(666);
			String s = console.readLine();
			if(s.equals("quit"))
				System.exit(1);
			else if (s.equals("-map"))
			{
				frame.setVisible(true);
			}
			else
			{
				pd.checkArgs(s.split(" "));
				dataThread.interrupt();
			}
		}
	}
}



