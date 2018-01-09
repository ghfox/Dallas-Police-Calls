package dallaspolicecalls;

import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.net.URL;
import java.net.MalformedURLException;
import java.lang.Thread;
import java.lang.InterruptedException;
import java.lang.Runnable;

public class PrintData implements Runnable
{
    boolean dbg = false;
    String S_URL = "https://www.dallasopendata.com/resource/are8-xahz.csv?";
    int [] PF_INTS;
    String [] PRINT_FIELDS = {"priority","date_time","division","block","location","nature_of_call"};
    int UPDATE_FREQUENCY = 600000;
    int I_LOC = -1, I_PRI = -1, I_BLO = -1;
    final String S_PRI = "priority", S_BLO = "block", S_LOC = "location";
    ArrayList<String> DATA;
    String [][] MAP_DATA;
    String [] FL_TOKENS;
    
    String maxChar = "25";
    
    public PrintData(String [] args)
    {
        checkArgs(args);
    }
    
    public void run() {
        try {
            updateData();
            printData();
            Thread.sleep(UPDATE_FREQUENCY);
        } catch (Exception e) {
            System.out.println("MSG :: Thread interrupted, refresh called");
            run();
        }
        
        System.out.println("MSG :: Refresh");
        run();
    }
    public String [][] getMapData()
    {
        MAP_DATA = new String[DATA.size()][3];
        for(int i = 0; i < DATA.size(); i++)
        {
            //if(dbg){System.out.println("DGB :: WORKING IN LINE "+ i + " " + DATA.get(i));}
            String[] tokens = DATA.get(i).split(",");
            MAP_DATA[i][0] = tokens[I_PRI];
            MAP_DATA[i][1] = tokens[I_BLO];
            MAP_DATA[i][2] = tokens[I_LOC];
        }
        return MAP_DATA;
    }
    
    public void updateData() throws MalformedURLException, IOException
    {
        DATA = new ArrayList<String>();
        URL url = new URL(S_URL);
        Scanner s = new Scanner(url.openStream());
        String firstLine = s.nextLine();
        firstLine = firstLine.replaceAll("\"","");
        if(dbg) {System.out.println("DBG:: FIRSTLINE " + firstLine);}
        FL_TOKENS = firstLine.split(",");   
        findTheFields();
        while(s.hasNext())
            DATA.add(s.nextLine().replaceAll("\"",""));
    }
    
    public void printData()
    {
        for(int i = 0; i < PRINT_FIELDS.length; i++)
        {
            if(PF_INTS[i] != -1)
                System.out.printf("%-25." + maxChar + "s",PRINT_FIELDS[i]);
        }
        System.out.println();
    
        for(int i = 0; i < DATA.size(); i++)
        {
            System.out.print(" " + (char)(65+i) + " ");
            String [] line = DATA.get(i).split(",");
            processPrintLine(line);
        }
        System.out.println("\n\nReady for input, enter new flags, quit, or wait for refresh.");
        
    }
    
    public void findTheFields()
    {
        PF_INTS = new int [PRINT_FIELDS.length];
        for(int i = 0; i < PRINT_FIELDS.length; i++)
            PF_INTS[i] = -1;
            
        for(int i = 0; i < FL_TOKENS.length; i++)
        {
            switch(FL_TOKENS[i])
            {
                case S_PRI : {I_PRI = i; break; }
                case S_LOC : {I_LOC = i; break; }
                case S_BLO : {I_BLO = i; break; }
            }
            for(int j = 0; j < PF_INTS.length; j++)
            {
                if(PRINT_FIELDS[j].equals(FL_TOKENS[i]))
                    PF_INTS[j] = i;
            }
        }
        for(int i = 0; i < PF_INTS.length; i++)
        {
            if(PF_INTS[i] == -1)
                System.out.println("ERROR :: Field \"" + PRINT_FIELDS[i] + "\" was not found. Will be excluded");
        }
        if(I_LOC == -1 || I_BLO == -1 || I_PRI == -1)
        {
                System.out.println("ERROR :: REQUIRED FIELDS NOT FOUND \t LOC,BLO,PRI " + I_LOC + "," + I_BLO + "," + I_PRI);
                System.exit(2);
        }
        if(dbg){System.out.println("DBG :: REQUIRED FIELDS \t LOC,BLO,PRI " + I_LOC + "," + I_BLO + "," + I_PRI);}
    }
    
    public void processPrintLine(String [] line){
        for(int i = 0; i < PF_INTS.length; i++)
            if(PF_INTS[i] != -1)
                System.out.printf("%-25." + maxChar + "s", line[PF_INTS[i]].replaceAll("\"",""));
        System.out.println();
    }
    
    public void checkArgs(String [] args){
        try{
            for(int i = 0; i < args.length; i++)
            {
                //if(args[i].equals("-r"))
                    //printData();
                if(args[i].equals("-d"))
                    dbg = true;
                else if(args[i].equals("-url"))
                {
                    i++;
                    S_URL = args[i];
                    System.out.println("MSG :: Custom User URL " + S_URL);
                }
                else if(args[i].equals("-fields"))
                {
                    i++;
                    PRINT_FIELDS = args[i].split(",");
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
                        UPDATE_FREQUENCY = 1000 * Integer.parseInt(args[i]);
                    } 
                    catch (Exception e) 
                    {
                        System.out.println("ERROR :: Bad update frequency number, only integers of seconds please.");
                    }
                    if(UPDATE_FREQUENCY >= 1800000)
                        System.out.println("MSG :: Update frquency set " + (UPDATE_FREQUENCY/1000) + " seconds");
                    else
                    {
                        UPDATE_FREQUENCY = 1800000;
                        System.out.println("MSG :: Update frequency too low, set to 1800 seconds (30 mins)");
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
            System.out.println("ERROR :: Invalid argument; check help with -h");
        }       
    }
    
    public void printHelp()
    {
        System.out.println("--Help--\n-d \tDisplay debug lines. \n-url [new url] \tUrl for a csv, in theory this could be any csv file (include http://).");
        System.out.println("-fields [field1,field2,etc]\tWill change the default fields to the ones listed, do not use spaces, order will be as entered.");
        System.out.println("-uf [seconds]\tWill change the rate at which the data is refreshed or updated. Will not accept less than 180 seconds.");
        System.out.println("-mc [max character in col]\tWill change max character in entries, may cause some misalignment.");
        System.out.println("-map\tGenerate static map. Will not work with other flags");
        System.out.println("-h\tDisplays this helpful blurb.\n\n");
        System.out.println("Contains information from Dallas OpenData which is made available under the ODC Attribution License.");
        System.out.println("https://www.dallasopendata.com/");
        System.exit(1);
    }
}
