package mypackege;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import mypackege.file_dll.Node;

public class WriteToAsciiFile {
	
	/**It does the save job for the text editor
	 * and in case the given file name dose not exist it creates it and then it writes in it
	 * @param node
	 * @param textfile
	 */
	public void save(Node node, String textfile){
		
		file_dll dll = new file_dll();
		
				// If the file doesn't exists, create and write to it
				// If the file exists,remove all content and write to it
		        try (FileWriter writer = new FileWriter(textfile);
		             BufferedWriter bw = new BufferedWriter(writer)) {

		        	while( node != null ){
		        		bw.write(dll.getpage(node));
		        		bw.newLine();
		        		node = dll.getnext(node);
		        	}

		        } catch (IOException e) {
		            System.err.format("IOException: %s%n", e);
		        }
	}
}
