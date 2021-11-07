package mypackege;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;
//import java.util.Random;
import java.util.Scanner;

import mypackege.file_dll.Node;



public class editor {
	
	/**The main takes the argument from the user 
	 * it change's it into a string
	 * and gives it to the editor
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		editor edit = new editor();

		String textfile;
		textfile = args[0];
		edit.Editor(textfile);
	}
	
	public final int maxlinelength = 80; //maximum line length
	public final int maxwordlength = 20; //maximum word length
	public final int minwordlength = 5; //minimum word length
	public final int buffersize = 128; //page size in bytes
	public final int lineVisualizer = 0; //the toggle parameter to visualize the line number
	
	public int bufferlength ;
	
		
	private  ReadFromAsciiFile rfaf = new ReadFromAsciiFile();
	private  WriteToAsciiFile wtaf = new WriteToAsciiFile();
	private  file_dll dll = new file_dll();
	private  index_file index = new index_file();
	
	public   Scanner input = new Scanner(System.in);
	
	/**The editor contains the body of the program
	 * @param textfile
	 * @throws IOException 
	 */
	public void Editor(String textfile) throws IOException{
		
		int lineVisualizer = 0;
		int valueoflines = 0 ;
		int currentline = 1;
		String command = null;
		String insert = null;
		Node c_node = dll.head;
		RandomAccessFile MyFile = null;		
		/*takes the imported file name
		 * and if it exists it sends to the double linked list its line to import the in it
		 * or else it creates an empty file with the imported name 
		 */
		try {
		MyFile = new RandomAccessFile(textfile, "rw");
		String line;
		while ((line = MyFile.readLine()) != null) {
			//System.out.println(line);
			valueoflines++ ;
			c_node = dll.append(line);
		}
		} catch (IOException e) {

			   e.printStackTrace();
		} finally {

			   try {
			    if (MyFile != null)
			    	MyFile.close();
			   } catch (IOException ex) {
			    ex.printStackTrace();
			   }
			  }

		Node node_head = dll.head;
		c_node = node_head;
		
		
		while(command !="x" | command !="q" ){
			

			//int n = new Random().nextInt(20) + 1;
			
			
			command = input.nextLine();
			
			switch (command) {
			case "^": //Go to the first line
				currentline = 1;
				c_node = node_head;
				System.out.println("ok");
				System.out.println("");
				break;
			case "$": //Go to the last line
				currentline = valueoflines;
				c_node = dll.getlast();
				System.out.println("ok");
				System.out.println("");
				break;
			case "-": //Go up one line
				if(currentline > 1){
					currentline--;
					c_node = dll.getprev(c_node);
					System.out.println("ok");
					System.out.println("");}
				else System.out.println("You are alredy at the first line.");
				break;
			case "+": //Go down one line
				if(currentline < valueoflines){
					currentline++;
					c_node = dll.getnext(c_node);
					System.out.println("ok");
					System.out.println("");}
				else System.out.println("You are alredy at the last line.");
				break;
			case "a": //Add new line after current line (the user is asked to type in the text for the new line)
				System.out.println("Type text for new line:");
				insert = input.nextLine();
				if( c_node!= null) {
					c_node=dll.InsertAfter(c_node, insert);
					currentline++;}
				else {c_node = dll.append(insert);
					node_head = c_node;
				}
				valueoflines++ ;
				System.out.println("");
				
				break ;
			case "t": //Add new line before current line (the user is asked to type in the text for the new line)
				System.out.println("Type text for new line:");
				insert = input.nextLine();
				if(c_node == null ){c_node = dll.append(insert);
					node_head = c_node;
				}
				else if(c_node.prev != null) c_node=dll.InsertBefore(c_node, insert);
				else node_head = dll.push(insert);
				valueoflines++ ;
				System.out.println("");
				break ;
			case "d": //Delete current line, c_node points at the node after the deleted one
				if(c_node != null){ c_node = dll.delete(c_node);
					if(currentline == 1) node_head = c_node;
					if (currentline == valueoflines)currentline--;
					valueoflines-- ;
					System.out.println("ok");}
				else System.out.println("The file is already empty");
				System.out.println("");
				break;
			case "l": //Print all lines
				Node c_node_p = node_head;
				for(int i=1; i<=valueoflines; i++){
					rfaf.ReadCurrLine(i, c_node_p, lineVisualizer);
					c_node_p= dll.getnext(c_node_p);
					}
				if(valueoflines == 0 ) System.out.println("EMPTY FILE");
				System.out.println("");
				break;
			case "n": //Toggle whether line numbers are displayed when printing  (l command)
				if(lineVisualizer == 1 ) {
					lineVisualizer = 0;
					System.out.println("Toggle off");
				}
				else {
					lineVisualizer = 1;
					System.out.println("Toggle on");
				}
				System.out.println("");
				break;
			case "p": //Print current line
				rfaf.ReadCurrLine(currentline, c_node, lineVisualizer);
				System.out.println("");
				break;
			case "q": // end with no save
				System.out.println("Programm exit with no save");
				System.out.println("");
				index.deletindex(bufferlength);
				System.exit(0);
				break;
			case "w": //Write file to disk
				wtaf.save(node_head ,textfile);
				System.out.println("Save Successful");
				System.out.println("");
				break;
			case "x": // save and end
				wtaf.save(node_head, textfile);
				System.out.println("Save Successful");
				System.out.println("Programm exit");
				System.out.println("");
				index.deletindex(bufferlength);
				System.exit(0);
				break;
			case "=": //Print current line number
				System.out.println("We are at line " + currentline  );
				System.out.println("");
				break;
			case "#": //Print number of lines and characters
				Node Pr_node = dll.head;
				int sumofchar=0;
				if(valueoflines != 0){
					while( Pr_node.next != null ){
						sumofchar +=  rfaf.ReadLinesAndChars( Pr_node);
						Pr_node = dll.getnext(Pr_node);
					}
					if(Pr_node != null){
						sumofchar +=  rfaf.ReadLinesAndChars( Pr_node);
						Pr_node = dll.getnext(Pr_node);
					}
				}
				System.out.println("\nline/lines:" + valueoflines + ", characters:" + sumofchar );
				System.out.println("");
				break;
			case "c": // Create the index file (*.txt.ndx) and print the number of pages that has
				Node index_node = node_head;
				int pages;
								
				bufferlength = index.CreateIndexAndReturnPages(index_node, valueoflines);
				
				if((bufferlength % buffersize)>0) pages = ((bufferlength / buffersize)+1);
				else pages = bufferlength / buffersize;
				
				System.out.println("OK. Data pages of size 128 bytes:" + pages);
				System.out.println("");
				break;
			case "v": //Print index file
				if (bufferlength == 0) System.out.println("Cant print the file index .ERROR INDEX NOT FOUND.");
				else index.printall(bufferlength);
				System.out.println("");
				break;
			case "s": //Print lines of given word using serial search
				if (bufferlength == 0) System.out.println("Cant do a serial search in the file index .ERROR INDEX NOT FOUND.");
				else {
					System.out.println("Type word for search:");
					insert = input.nextLine();
					int serialdiskaccesses = index.ssearch(insert,bufferlength);

					if((bufferlength % buffersize)>0) serialdiskaccesses ++;
					
					System.out.println("disk accesses: " + serialdiskaccesses);
					
					//Code for the successful and unsuccessful search for the experiment
					/*String [] insert2 = {"Rectors", "laboratories", "Technical", "Venetian"};
					int  amountcorrect[] ={0, 0, 0, 0};
					for(int i = 0; i < insert2.length; i++ ){
			    		
				    	int serialdiskaccesses1 = index.ssearch(insert2[i],bufferlength);
				    	
						if((bufferlength % buffersize)>0) serialdiskaccesses1 ++;
						
						amountcorrect[i] = serialdiskaccesses1;
			    	}
					for(int i = 0; i < insert2.length; i++ ){
						System.out.println("random disk accesses: " + amountcorrect[i] );
					}

					int amount = 0;
			    	
			    	for(int i = 0; i < 30; i++ ){
			    		
				    	String insert1 = getAlphaNumericString();
				    	int serialdiskaccesses1 = index.ssearch(insert1,bufferlength);
				    	
						if((bufferlength % buffersize)>0) serialdiskaccesses1 ++;
						
						amount += serialdiskaccesses1;
			    	}
					System.out.println("random disk accesses: " + amount /30);*/
				}
				System.out.println("");
				break;
			case "b"://Print lines of given word using binary search
				if (bufferlength == 0) System.out.println("Cant do a binary search in the file index .ERROR INDEX NOT FOUND.");
				else {
					System.out.println("Type word for search:");
					insert = input.nextLine();
				
					String string_of_lines = index.bsearch1(insert, 0, bufferlength);
					

					if(!string_of_lines.isEmpty()) System.out.println("'" + insert + "'" + " is on lines " + string_of_lines.replaceFirst(",", ""));
					else{
						System.out.println("Fail to find " + "'" + insert + "'" );
						System.out.println("Try again with an other word." );
						System.out.println("NoSuchElementException");
					}
				}
				System.out.println("");
				break;
			default: //Print Bad command if the given command doesn't match with an appropriate command          
				System.out.println("Bad command");
				System.out.println("");
				break;
			}
		} 
	}
	
	// function to generate a random string of length n 
    static String getAlphaNumericString() 
    { 
    	editor edit = new editor(); 
  
    	// lower limit for LowerCase Letters 
        int lowerLimit = 97; 
  
        // lower limit for LowerCase Letters 
        int upperLimit = 122; 
  
        Random random = new Random(); 
        
        int randomNum = random.nextInt((edit.maxwordlength - edit.minwordlength) + 1) + edit.minwordlength;
  
        // Create a StringBuffer to store the result 
        StringBuffer r = new StringBuffer(randomNum); 
        
        // take a random value between 97 and 122 
        int nextRandomChar = lowerLimit  + (int)(random.nextFloat() * (upperLimit - lowerLimit + 1)); 

        // take a random value between 65 and 90 
        int nextRandomUpperChar = 65  + (int)(random.nextFloat() * (90 - 65 + 1)); 
        
        // take a random value between 0 and 9 
        int Randompeacker = (0)  + (int)(random.nextFloat() * (9 - (0) + 1)); 
       
        // append a character at the end of bs 
        if(Randompeacker > 4) r.append((char)nextRandomChar); 
        else r.append((char)nextRandomUpperChar); 
  
        for (int i = 1; i < randomNum; i++) { 
  
            // take a random value between 97 and 122 
            nextRandomChar = lowerLimit 
                                 + (int)(random.nextFloat() 
                                         * (upperLimit - lowerLimit + 1)); 
  
            // append a character at the end of bs 
            r.append((char)nextRandomChar); 
        } 
  
        // return the resultant string 
        return r.toString(); 
    } 

   
}


