package mypackege;


import mypackege.file_dll.Node;


public class ReadFromAsciiFile {
	
	/**Prints the current line of the text
	 * with or without the line number depends of the toggle value 
	 * or it prints EMPTY FILE if you try too print a no written file
	 * @param cline
	 * @param node
	 * @param numTog
	 */
	public void ReadCurrLine(int cline, Node node, int numTog) {
		
		file_dll dll = new file_dll();
		
	     if(node != null) {
	    	 if(numTog == 1) System.out.println( + cline + ")" + dll.getpage(node));
	    	 else System.out.println(node.page);}
         else System.out.println("EMPTY FILE");

	}

	/**It splits it's line in words
	 * and then it returns the amount of characters its line has
	 * @param node
	 * @return
	 */
	public int ReadLinesAndChars(Node node) {
		
		file_dll dll = new file_dll();
		
        int sumofchar=0;
        String line = dll.getpage(node);
	    String delims = ",\\s+|\\s*\\\"\\s*|\\s+|\\.\\s*|\\s*\\:\\s*";
	    String [] tokens = line.split(delims);
	    for(int j=0;j<tokens.length;j++){
	    	
		     	sumofchar += tokens[j].length();
		     	
	    }
	    return sumofchar;
	}

}
