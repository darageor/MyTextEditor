package mypackege;

/**It contains the double linked list
 * and contains all the needed commands in the list
 * like (insert, delete, get next, get previous, get last)
 * @author User
 *
 */
public class file_dll {

	Node head; // head of list 
	  
    /* Doubly Linked list Node*/
    class Node { 
        String page; 
        Node prev; 
        Node next; 
  
        // Constructor to create a new node 
        // next and previous is by default initialized as null 
        Node(String d) { page = d; } 
    } 

    

    /**It inserts a node at the next node that the user is the time he calls it 
     * and returns a pointer at the new line
     * @param prev_Node
     * @param new_page
     * @return
     */
	/* Given a node as prev_node, insert a new node after the given node */
	public Node InsertAfter(Node prev_Node, String new_page) 
	{ 

		/*1. check if the given prev_node is NULL */
		if (prev_Node == null) { 
			System.out.println("The given previous node cannot be NULL "); 
			return head; 
		} 
		
		/* 2. allocate node 
		* 3. put in the data */
		String newpage = checksize(new_page);
		Node new_node = new Node(newpage); 

		/* 4. Make next of new node as next of prev_node */
		new_node.next = prev_Node.next; 

		/* 5. Make the next of prev_node as new_node */
		prev_Node.next = new_node; 

		/* 6. Make prev_node as previous of new_node */
		new_node.prev = prev_Node; 

		/* 7. Change previous of new_node's next node */
		if (new_node.next != null) 
			new_node.next.prev = new_node; 
		return new_node; //returns the new node
	} 
	
	
	/**It inserts a node at the previous node that the user is the time he calls it 
	  * and returns a pointer at the new line
	  * @param next_Node
	  * @param new_page
	  * @return
	  */
	/* Given a node as next_node, insert a new node before the given node */
	public Node InsertBefore(Node next_Node, String new_page) {
		/* 1. allocate node 
		* 2. put in the data */
		String newpage = checksize(new_page);
		Node new_node = new Node(newpage); 

		/* 3. Make next of new node as  next_Node */
		new_node.next = next_Node; 
		
		/* 4. Make next_Node as previous of new_node */
		new_node.prev = next_Node.prev;

		/* 5. Make the previous of next_Node as new_node */
		next_Node.prev.next = new_node; 
 

		/* 6. Change previous of new_node's next node */
		new_node.next.prev = new_node; 
		
		return new_node;//returns the new node
	}

	/**It inserts a node at the front of the list
	 * and returns a pointer at the new head of the list
	 * @param new_page
	 * @return
	 */
	// Adding a node at the front of the list 
	public Node push(String new_page) 
	{ 
	    /* 1. allocate node  
	    * 2. put in the data */
		String newpage = checksize(new_page);
		Node new_Node = new Node(newpage); 
	  
	    /* 3. Make next of new node as head and previous as NULL */
	    new_Node.next = head; 
	    new_Node.prev = null; 
	  
	    /* 4. change prev of head node to new node */
	    if (head != null) 
	        head.prev = new_Node; 
	  
	    /* 5. move the head to point to the new node */
	    head = new_Node; 
	    
	    return head;//returns the head of the list
	} 
	
	/**It inserts a node at the end of the list
	 * and returns a pointer at the new node of the list
	 * @param new_page
	 * @return
	 */
	// Add a node at the end of the list 
	public Node append(String new_page) 
	{ 
		/* 1. allocate node 
		* 2. put in the data */
		String newpage = checksize(new_page);
		Node new_node = new Node(newpage); 

		Node last = head; /* used in step 5*/

		/* 3. This new node is going to be the last node, so 
		* make next of it as NULL*/
		new_node.next = null; 

		/* 4. If the Linked List is empty, then make the new 
		* node as head */
		if (head == null) { 
			new_node.prev = null; 
			head = new_node; 
			return head; 
		} 

		/* 5. Else traverse till the last node */
		while (last.next != null) 
			last = last.next; 

		/* 6. Change the next of last node */
		last.next = new_node; 

		/* 7. Make last node as previous of new node */
		new_node.prev = last; 
		
		return new_node;
	} 
	
	/**It deletes the node the user is when he use this command
	 * and returns the next node from the deleted node if it is not at the end of the list 
	 * or else it returns the previous node than the deleted on
	 * @param node
	 * @return
	 */
	/*delete the current node*/
	public Node delete(Node node) {
		Node return_node;
		
		/* 1. Make previous of node next node as node previous 
		 * if you are not at the last node*/
		if (node.next != null)node.next.prev = node.prev ; 

		/* 2. Make previous of node next node as node previous */
		if (node.prev != null)node.prev.next = node.next; 
		
		/* 3. Make return_node as node next */
		if (node.next!= null) return_node = node.next; 
		else return_node = node.prev;
		
		/* 4. Delete the node, so 
		* make next of it as NULL and
		* make the previous of it as NULL*/
		node.next = null;
		node.prev = null;
		
		return return_node; //returns the next node from the deleted node if not at the end of file or else return prev node
	}

	/**It returns the last node of the list
	 * @return
	 */
	public Node getlast() {
	Node last = head; 

	/*traverse till the last node */
	while (last.next != null) last = last.next; 
		return last;
	}

	/**It returns the next node of the current_node
	 * @param current_node
	 * @return
	 */
	public Node getnext(Node current_node) {

		/* traverse till the next node */
		if (current_node.next != null) current_node = current_node.next; 
			return current_node;
		}
	
	/**return the previous node of the current_node
	 * @param current_node
	 * @return
	 */
	public Node getprev(Node current_node) {

		/* traverse till the previous node */
		if (current_node.prev != null) current_node = current_node.prev; 
			return current_node;
		}
	
	/**return the line of the current node
	 * @param current_node
	 * @return
	 */
	public String getpage(Node current_node) {

		return current_node.page;
		}
	

	 /**It checks if the given string its in the borders of a line
	  * and if it isn't, it returns a string as long as it can gets 
	  * without cutting any word or loosing any symbol
	  * and it succeeds it by using the method getfullstring 
	  * @param new_page
	  * @return
	  */
	 private String checksize(String new_page) {
		 editor editor = new editor();
		 String str = null ;
		 String str1 = null ;
		 int charsum=0;
			String delims = ",\\s+|\\s*\\\"\\s*|\\s+|\\.\\s*|\\s*\\:\\s*|\\,\\s*";
		    String [] tokens = new_page.split(delims);
		    for(int j=0;j<tokens.length;j++){
			    for(int i=0;i<tokens[j].length();i++){
			    	charsum++;
			    	if(charsum > editor.maxlinelength) {
			    		str1 = getfullstring(str, new_page ,j-1);
			    		return str1;}
			    }
			    str = tokens[j];
		    }
		    return new_page;
		}


	 /**It takes as input the full string that the user inserts 
	  * and the word and the line(the caller found the word) that the caller of the method wont to stop
	  * and returns the string until that point with every symbol existed and before in it
	  * @param str
	  * @param new_page
	  * @param i
	  * @return
	  */
	private String getfullstring(String str, String new_page, int i) {
		 int j = 0;
			String delims = "\\s+";
			String str1 = null ;
		    String [] tokens = new_page.split(delims);
		    while(tokens.toString() != str & j <= i){
		    	
		    	if(j == 0) str1 = tokens[j] + " ";
			    else str1 += tokens[j] + " ";
		    	j++;
		    }
		    return str1;
	}
}

