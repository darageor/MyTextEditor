package mypackege;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;


import java.io.*;
import mypackege.file_dll.Node;

public class index_file {
	
	public CopyOnWriteArrayList<String> word = new CopyOnWriteArrayList<String>();
	public CopyOnWriteArrayList<Integer> line = new CopyOnWriteArrayList<Integer>();
	
	private final String filename ="byte_file";
	private int bsearch_DiskAccess = 0;

	/**Creates and saves the byte array 
	 * @param cur_node
	 * @param maxlines
	 * @return
	 * @throws IOException
	 */
	public int CreateIndexAndReturnPages(Node cur_node, int maxlines) throws IOException {
		
		file_dll dll = new file_dll();
		editor edit = new editor();
		
		
		int cur_line = 0;
		String word_input = null;
		String readed_line = null;
		 
		ByteArrayOutputStream baos = new ByteArrayOutputStream(edit.buffersize) ;
		DataOutputStream dos = new DataOutputStream(baos);
		
		// checks if the current line is between the file borders
		while(cur_line < maxlines){
			readed_line = dll.getpage(cur_node); // take the line from the current node
			cur_line++; //make the current line pointer +1
			
			String delims ="\\P{Alpha}+" ;
			String [] tokens = readed_line.split(delims); // split the line in words 
			
			for(int j=0;j<tokens.length;j++){
				
				
				if(tokens[j].length() >= edit.minwordlength ){ // check if the word is bigger than the minimum length we wont 
					if(tokens[j].length() > edit.maxwordlength){ // check if the word is bigger than we wont and if so keep the part until the max line
						String [] token = tokens[j].split("(?!^)");
							   
						for(int k=0;k<edit.maxwordlength;k++){
								   
							if(k == 0) word_input = token[k] ; // if you are at the first letter insert it at the string
							else word_input += token[k]; // or else add the next letter to the string
						}
					}else word_input = tokens[j]; // or else take the word as it is
					
	
					//insert the word and the line number in the array list
					word.add(word_input);
					line.add(cur_line);
					}
											
				} //or else reject it
			
			//get the next node (text line)
			cur_node = dll.getnext(cur_node);
		}
		//create two object array lists from the two CopyOnWriteArrayLists
		Object[] a = word.toArray();
		//make all letters lowercase to be easier to sort them
		/* for(int i=0;i<a.length;i++){
		        if(!a[i].equals(((String) a[i]).toLowerCase()))
		        	a[i] = ((String) a[i]).toLowerCase();
		    }*/
		Object[] b = line.toArray();
		
		//sort the two array lists
		 for (int n = 0; n < a.length; n++) {
		        for (int m = 0; m < (a.length - 1) - n; m++) {
		            if ((( (String) a[m]).compareTo( (String) a[m + 1])) > 0) {
		                Object swapString = a[m];
		                a[m] = a[m + 1];
		                a[m + 1] = swapString;
		                Object swapInt = b[m];
		                b[m] = b[m + 1];
		                b[m + 1] = swapInt;
		            }
		        }
		    }
		 for (int i = 0; i < a.length; i++) {
		        word.set(i, (String) a[i]);
		        line.set(i, (Integer) b[i]);
		    }
		 
		Iterator<String> itr = word.iterator();
	 	Iterator<Integer> itr1 = line.iterator();
	 	
	 	//insert the sets of words and lines in the buffer
	 	while (itr.hasNext()){
			String w_str = itr.next();
			byte dest[] = new byte[edit.maxwordlength];
			byte[] src = w_str.getBytes();
					
			int l_int = itr1.next();
			System.arraycopy(src, 0, dest, 0, src.length);
			dos.write(dest, 0, edit.maxwordlength);  	// Write fixed size string (20 bytes)
			dos.writeInt(l_int);  		// Write Int (4 bytes)
	 	}
		
		// Get the bytes of the serialized object
		byte[] buffer = baos.toByteArray(); // Creates a newly allocated byte array.
		//System.out.println("\nbuf size: " + buffer.length + " bytes");
		byte[] DataPage = new byte[edit.buffersize];
		int buf_length =buffer.length;
		int i = 0;
		int seek_counter = 0;
		int start_offset = 0;
		int end_offset = 0;
		int offset_counter = 0;
		byte[] buffer_part = null;
		
		while(buf_length >=(-(edit.maxwordlength+4))){
			
			//find the offsets of the buffer that leads to one index page
			if (offset_counter <= edit.buffersize){ 
				offset_counter = ((edit.maxwordlength+4)*(i+1));
				end_offset = ((edit.maxwordlength+4)*(i+1));
				buf_length -=(edit.maxwordlength+4);
				i++;
			}
			else {//use the offsets that found before to take a part of the buffer and write it at the index file
				end_offset -= (edit.maxwordlength+4);
					
				buffer_part =  Arrays.copyOfRange(buffer, start_offset ,end_offset);
									
				System.arraycopy(buffer_part, 0, DataPage, 0, buffer_part.length); // Copy buffer data to DataPage of DataPageSize
					
				// write to the file
				RandomAccessFile MyFile = new RandomAccessFile (filename, "rw");
				MyFile.seek(edit.buffersize*seek_counter);
				MyFile.write(DataPage);		
				MyFile.close();
				
				seek_counter++;
				Arrays.fill(buffer_part, (byte)0);
				start_offset = end_offset; 
				offset_counter = 0;
				
			}
			
		}
		baos.close();
		return buffer.length ;
	}
	
	/**print all the words and the lines that we found them from the byte array
	 * @param bufferlength 
	 * @throws IOException
	 */
	public void printall(int bufferlength) throws IOException {
		
		editor edit = new editor();
		
		int buf_length = bufferlength;
		int i = 0;
		
		
		while(buf_length >=(-(edit.maxwordlength + 4))){
			
			RandomAccessFile MyFile = new RandomAccessFile (filename, "r");
			byte[] ReadDataPage = new byte[edit.buffersize];
			MyFile.seek(edit.buffersize*i);
			MyFile.read(ReadDataPage);
			ByteArrayInputStream bis= new ByteArrayInputStream(ReadDataPage);
			DataInputStream dis= new DataInputStream(bis);
			byte bb[] = new byte[edit.maxwordlength];
			dis.read(bb);
			byte[] bb1=new String(bb).replaceAll("\0", " ").getBytes();
			String string_word = new String(bb1);
			int string_line = dis.readInt();
			
			if (string_line != 0)System.out.println( string_word + " " + string_line);
				
			MyFile.close();
			
			buf_length -=edit.maxwordlength + 4;
			i++;
		}
		
	}

	
	/**It takes as inputs the amount of the bytes existing in the index file and the word that the user wants to find
	 * it makes the word of the user all lower case letter 
	 * and the by doing a serial search it find if the word existing in the file or not
	 * if it finds the word it returns on witch line the word is in the txt file given by the user
	 * @param insert
	 * @param bufferlength
	 * @return
	 * @throws IOException
	 */	
public int ssearch (String insert, int bufferlength) throws IOException {
		
		//make the inserted string into lowercase to make the search easier
		//String search_insert = insert.toLowerCase();
		String search_insert = insert;
		
		editor edit = new editor();
		
		int buf_length = bufferlength;
		int i = 0;
		String string_of_lines = "";
		int DiskAccess = 0;
		
		//start searching from the start of the index
		while(buf_length >=(-(edit.maxwordlength + 4))){
			
			//read the index file
			RandomAccessFile MyFile = new RandomAccessFile (filename, "r");
			byte[] ReadDataPage = new byte[edit.buffersize];
			MyFile.seek(edit.buffersize*i);
			DiskAccess++;
			
			MyFile.read(ReadDataPage);
			ByteArrayInputStream bis= new ByteArrayInputStream(ReadDataPage);
			DataInputStream dis= new DataInputStream(bis);
			byte bb[] = new byte[edit.maxwordlength];
			dis.read(bb);
			//throw the null bytes at the end of the byte array and return them to a new byte array
			byte[] bb1=new String(bb).replaceAll("\0", "").getBytes();
			String string_word = new String(bb1);
			int string_line = dis.readInt();
			
			//if the word is found add the line in the search line array list 
			if ( stringCompare(search_insert, string_word) == 0 ){
				string_of_lines += "," + string_line ;
			//if the word read from the index file is "bigger" than the inserted one break the loop
			}else if (stringCompare(search_insert, string_word) < 0) break;
				
			MyFile.close();
			
			buf_length -=edit.maxwordlength + 4;
			i++;
		}
		
		if(!string_of_lines.isEmpty()) {
			System.out.println("'" + insert + "'" + " is on lines " + string_of_lines.replaceFirst(",", ""));
		}else{
			System.out.println("Fail to find " + "'" + insert + "'" );
			System.out.println("Try again with an other word." );
			System.out.println("NoSuchElementException");
		}
		
		int DiskAccess_Return = DiskAccess*(edit.maxwordlength+4)/edit.buffersize;
		
		return DiskAccess_Return;
	}
	
	/**It takes the word inserted from the user 
	 * it makes the word of the user's word all lower case letter 
	 * and the by doing a binary search it find if the word existing in the file or not
	 * if it finds the word it returns on witch line the word is in the txt file given by the user
	 * @param insert
	 * @param minbufferlength
	 * @param maxbufferlength
	 * @return
	 * @throws IOException
	 */
	public String bsearch(String insert, int minbufferlength, int maxbufferlength) throws IOException {
		//make the inserted string into lowercase to make the search easier
		//String search_insert = insert.toLowerCase();
		String search_insert = insert;
				
		editor edit = new editor();
		
		String string_of_lines = "";
		
		int DiskAccess=0;
		
		while(maxbufferlength >= minbufferlength){
			int buf_length = minbufferlength + (maxbufferlength-1) / 2;
	
			//read the index file
			RandomAccessFile MyFile = new RandomAccessFile (filename, "r");
			byte[] ReadDataPage = new byte[edit.buffersize];
			
			//start searching from the start of the index
			
				
				MyFile.seek(buf_length);
				DiskAccess++;
				
				MyFile.read(ReadDataPage);
				ByteArrayInputStream bis= new ByteArrayInputStream(ReadDataPage);
				DataInputStream dis= new DataInputStream(bis);
				byte bb[] = new byte[edit.maxwordlength];
				dis.read(bb);
				//throw the null bytes at the end of the byte array and return them to a new byte array
				byte[] bb1=new String(bb).replaceAll("\0", "").getBytes();
				String ss = new String(bb1);
				System.out.println(ss);
				//if the word is found add the line in the search line array list 
				if ( stringCompare(search_insert, ss) < 0) {
					while(stringCompare(search_insert, ss) == 0){
						buf_length -=edit.maxwordlength + 4;
						MyFile.seek(edit.buffersize);
						MyFile.read(ReadDataPage);
						 bis= new ByteArrayInputStream(ReadDataPage);
						dis= new DataInputStream(bis);
						bb = new byte[edit.maxwordlength];
						dis.read(bb);
						//throw the null bytes at the end of the byte array and return them to a new byte array
						bb1=new String(bb).replaceAll("\0", "").getBytes();
						ss = new String(bb1);
						string_of_lines += "," + dis.readInt() ;
					}
					//string_of_lines += "," + dis.readInt() ;
				}else if (stringCompare(search_insert, ss) < 0) {
					DiskAccess++;
					minbufferlength = buf_length+1;
				}
				else {
					DiskAccess++;
					maxbufferlength =  buf_length-1;
				}
				
											
			
			MyFile.close();
			
			}
		
		
		int DiskAccess_Return = DiskAccess;//*(edit.maxwordlength+4)/edit.buffersize;
		
		//if((edit.bufferlength % edit.buffersize)>0) DisckAccess_Return ++;
		
		System.out.println("disk accesses: " + DiskAccess_Return);
		
		return string_of_lines;
		
	}
	
	
	/**It takes the word inserted from the user 
	 * it makes the word of the user's word all lower case letter 
	 * and the by doing a binary search it find if the word existing in the file or not
	 * if it finds the word it returns on witch line the word is in the txt file given by the user 
	 * @param insert
	 * @param min
	 * @param max
	 * @return
	 * @throws IOException
	 */
	public String bsearch1(String insert, int min, int max) throws IOException {
		//make the inserted string into lowercase to make the search easier
		//		String search_insert = insert.toLowerCase();
		String search_insert = insert;		
				
		editor edit = new editor();
				
		String string_of_lines = "";
		
				
		if ((max - min) < edit.buffersize){
			int search_parameter = min;
			while( search_parameter < max){
				//read the index file
				RandomAccessFile MyFile = new RandomAccessFile (filename, "r");
				byte[] ReadDataPage = new byte[edit.buffersize];
				if(search_parameter<0 ) MyFile.seek(0);
				else MyFile.seek(search_parameter);
				bsearch_DiskAccess++;
				
				MyFile.read(ReadDataPage);
				ByteArrayInputStream bis= new ByteArrayInputStream(ReadDataPage);
				DataInputStream dis= new DataInputStream(bis);
				byte bb[] = new byte[edit.maxwordlength];
				dis.read(bb);
				//throw the null bytes at the end of the byte array and return them to a new byte array
				byte[] bb1=new String(bb).replaceAll("\0", "").getBytes();
				String ss = new String(bb1);
				if(stringCompare(search_insert, ss) < 0) search_parameter += (edit.maxlinelength+4);
				else if(stringCompare(search_insert, ss) > 0) {
					MyFile.close();
					return string_of_lines;
				}else {
					string_of_lines +=  "," + dis.readInt() ;
					MyFile.close();
					return  string_of_lines;
				}
				MyFile.close();
			}
			return  string_of_lines;
		}else {
			int search_parameter1 = max +((max-min)/2);
			//read the index file
			RandomAccessFile MyFile = new RandomAccessFile (filename, "r");
			byte[] ReadDataPage = new byte[edit.buffersize];
			if(search_parameter1<0 ) MyFile.seek(0);
			else MyFile.seek(search_parameter1);
			bsearch_DiskAccess++;
			
			MyFile.read(ReadDataPage);
			ByteArrayInputStream bis= new ByteArrayInputStream(ReadDataPage);
			DataInputStream dis= new DataInputStream(bis);
			byte bb[] = new byte[edit.maxwordlength];
			dis.read(bb);
			//throw the null bytes at the end of the byte array and return them to a new byte array
			byte[] bb1=new String(bb).replaceAll("\0", "").getBytes();
			String ss = new String(bb1);
			if(stringCompare(search_insert, ss) < 0) {
				string_of_lines +=  bsearch1(insert, search_parameter1, max);
				MyFile.close();
				return  string_of_lines;
			}else if(stringCompare(search_insert, ss) > 0){
				string_of_lines +=  bsearch1(insert, min, search_parameter1);	
				MyFile.close();
				return string_of_lines;
			}else {
				string_of_lines +=  "," + dis.readInt() ;
				MyFile.close();
				return  string_of_lines;
			}
			
		}
	//	System.out.println("disk accesses: " + bsearch_DiskAccess);
	}
	
	
	/** This method compares two strings 
     * lexicographically without using 
     * library functions      * 
     * @param str1
     * @param str2
     * @return
     */
    public static int stringCompare(String str1, String str2) 
    { 
  
        int l1 = str1.length(); 
        int l2 = str2.length(); 
        int lmin = Math.min(l1, l2); 
  
        for (int i = 0; i < lmin; i++) { 
            int str1_ch = (int)str1.charAt(i); 
            int str2_ch = (int)str2.charAt(i); 
  
            if (str1_ch != str2_ch) return str1_ch - str2_ch;  
        } 
  
        // Edge case for strings like 
        // String 1="student" and String 2="students" 
        if (l1 != l2) { 
            return l1 - l2; 
        } 
  
        // If none of the above conditions is true, 
        // it implies both the strings are equal 
        else { 
            return 0; 
        } 
    } 

	/**delete the index files before the program terminates
	 * and print an appropriate message whether or nod the file deleted
	 * @param bufferlength
	 * @throws FileNotFoundException
	 */
	public void deletindex(int bufferlength) throws FileNotFoundException  {
		
		int buf_length = bufferlength;
		if (buf_length > 0){
			File MyFile = new File (filename);
			if(MyFile.delete()) 
			        { 
			            System.out.println(filename+" deleted successfully"); 
			        } 
			        else
			        { 
			            System.out.println("Failed to delete "+filename); 
			        }  
		}else return;
		
	}
	
}
