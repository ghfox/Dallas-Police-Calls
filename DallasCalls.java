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

import java.io.IOException;
import java.util.Scanner;
import java.net.URL;
import java.net.MalformedURLException;
import java.lang.Thread;
import java.lang.InterruptedException;

public class DallasCalls {
	
	static int [] relevantThings;
	static String [] fields = {"priority","date_time","division","location","status","nature_of_call"};
	static boolean dbg = false;
	static String urlString = "https://www.dallasopendata.com/resource/are8-xahz.csv";
	static int updateFrequency = 90000;
	static String maxChar = "25";
	
	public static void main (String args[]) throws IOException, MalformedURLException, InterruptedException {
		checkArgs(args);
		URL url = new URL(urlString);
		Scanner s = new Scanner(url.openStream());
		String firstLine = s.nextLine();
		firstLine = firstLine.replaceAll("\"","");
		if(dbg) {System.out.println("DBG:: FIRSTLINE " + firstLine);}
		String [] flTokens = firstLine.split(",");	
		
		findTheFields(flTokens);
		
		for(int i = 0; i < fields.length; i++)
		{
			if(relevantThings[i] != -1)
				System.out.printf("%-25." + maxChar + "s",fields[i]);
		}
		System.out.println();
	
		while(s.hasNext())
		{
			String [] line = s.nextLine().split(",");
			processLine(line);
		}
		
		Thread.sleep(updateFrequency);
		System.out.println("\n\nMSG :: Refresh");
		main(args);
	}
	
	public static void findTheFields(String [] flTokens)
	{
		relevantThings = new int [fields.length];
		for(int i = 0; i < fields.length; i++)
		{
			relevantThings[i] = -1;
			for(int j = 0; j < flTokens.length; j++)
			{
				if(fields[i].equals(flTokens[j]))
					relevantThings[i] = j;
			}
			if(relevantThings[i] == -1)
				System.out.println("ERROR :: Field \"" + fields[i] + "\" was not found. Will be excluded");
		}
	}
	
	public static void processLine(String [] line){
		for(int i = 0; i < relevantThings.length; i++)
			if(relevantThings[i] != -1)
				System.out.printf("%-25." + maxChar + "s", line[relevantThings[i]].replaceAll("\"",""));
		System.out.println();
	}
	
	public static void checkArgs(String [] args){
		try{
			for(int i = 0; i < args.length; i++)
			{
				if(args[i].equals("-d"))
					dbg = true;
				else if(args[i].equals("-url"))
				{
					i++;
					urlString = args[i];
					System.out.println("MSG :: Custom User URL " + urlString);
				}
				else if(args[i].equals("-fields"))
				{
					i++;
					fields = args[i].split(",");
					System.out.println("MSG :: Custom User Fields " + args[i]);
				}
				else if(args[i].equals("-mc"))
				{	
					try
					{
						i++;
						Integer.parseInt(args[i]);
						maxChar = args[i];
					}
					catch (Exception e) { throw new IllegalArgumentException(); }
					System.out.println("MSG :: Custom User max characters " + maxChar);
				}
				else if(args[i].equals("-uf"))
				{
					try
					{
						i++;
						updateFrequency = 1000 * Integer.parseInt(args[i]);
					} 
					catch (Exception e) 
					{
						System.out.println("ERROR :: Bad update frequency number, only integers of seconds please.");
					}
					if(updateFrequency >= 30000)
						System.out.println("MSG :: Update frquency set " + (updateFrequency/1000) + " seconds");
					else
					{
						updateFrequency = 30000;
						System.out.println("MSG :: Update frequency too low, set to 30 seconds");
					}
				}
				else if(args[i].equals("-h"))
					printHelp();
				else
					throw new IllegalArgumentException();
			} 
		}
		catch (Exception e) 
		{
			System.out.println("ERROR :: Invalid argument");
			printHelp();
		}			
	}
	
	public static void printHelp()
	{
		System.out.println("--Help--\n-d \tDisplay debug lines. \n-url [new url] \tUrl for a csv, in theory this could be any csv file (include http://).");
		System.out.println("-fields [field1,field2,etc]\tWill change the default fields to the ones listed, do not use spaces, order will be as entered.");
		System.out.println("-uf [seconds]\tWill change the rate at which the data is refreshed or updated. Will not accept less than 30 seconds.");
		System.out.println("-mc [max character in col]\tWill change max character in entries, may cause some misalignment.");
		System.out.println("-h\tDisplays this helpful blurb.\n\n");
		System.out.println("Contains information from Dallas OpenData which is made available under the ODC Attribution License.");
		System.out.println("https://www.dallasopendata.com/");
		System.exit(1);
	}
}

