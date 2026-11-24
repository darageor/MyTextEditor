# MyTextEditor

## Part One

The program's `main` function receives the file name as an argument and
passes it to the `editor` function. Before the `editor` function begins,
it initializes the following variables as `public final`:
`maxlinelength` (the maximum number of characters allowed in each line
of the file), `maxwordlength` (the maximum number of characters allowed
in each word of the file), `minwordlength` (the minimum number of
characters allowed in each word of the file), `buffersize` (the maximum
size, in bytes, of the buffer that can be used in the second part of the
assignment), and `LineVisualizer` (the variable indicating whether line
numbers are displayed when printing). In addition, it initializes a
variable named `bufferlength`, which will hold the total size of the
buffer created in the second part of the assignment.

At the start of the `editor` function, we initialize the variables
`LineVisualizer` and `valueoflines` (the number of lines in the file) to
zero, and the variable `currentline` (which stores the number of the
line we are currently on) to one. We then pass each line of the file as
a separate node to the `double linked list` (`dll`) class, using the
`append` function of the `dll`, which adds a node to the end of the
list. If a file with the specified name does not exist, we create one.
We store the head of the list in two variables of type `node`: one
allows us to move directly to the beginning of the list, while the other
keeps track of the node we are currently on. Finally, we read the
command the user wants to execute into the `command` variable and
execute it in a loop, whenever possible.

If the user enters the command `^` (go to the first line), we set
`currentline` to one and `c_node` (the node we are currently on) to
`node_head`.

If the user enters the command `$` (go to the last line), we set
`currentline` to `valueoflines` and `c_node` to the last node of the
`dll`, as returned by the `getlast` function of the `dll`.

If the user enters the command `-` (go up one line), the program checks
whether the current line is the first line of the file. If it is not, it
decrements `currentline` by one and sets `c_node` to the previous node,
as returned by the `getprev` function of the `dll`. Otherwise, it
informs the user that they are already on the first line of the file.

If the user enters the command `+` (go down one line), the program
checks whether the current line is the last line of the file. If it is
not, it increments `currentline` by one and sets `c_node` to the next
node, as returned by the `getnext` function of the `dll`. Otherwise, it
informs the user that they are already on the last line of the file.

If the user enters the command `a` (add one line after the current
line), the program asks the user for the string of the line they want to
add. It then checks whether the current position is at the end of the
file or whether the file is empty. If neither is the case, it uses the
`InsertAfter` function of the `dll` (which adds a new node after the
current node). Otherwise, it uses the `append` function of the `dll`. It
updates `c_node` to point to the node added by the user and, finally,
updates `valueoflines` and `currentline` with their correct values.

If the user enters the command `t` (add one line before the current
line), the program asks the user for the string of the line they want to
add. It then checks whether the file is empty and, if so, uses the
`append` function. Otherwise, it checks whether the current position is
at the beginning of the file. If it is not, it uses the `InsertBefore`
function of the `dll` (which adds a new node before the current node).
Otherwise, it uses the `push` function of the `dll` (which adds a node
at the beginning of the file). It updates `c_node` to point to the node
added by the user and, finally, updates `valueoflines` and `currentline`
with their correct values.

If the user enters the command `d` (delete current line), the program
checks whether the line the user wants to delete exists. If it does, it
deletes it using the `delete` function of the `dll`, updates `c_node` to
the node following the deleted one, updates `node_head` and
`currentline` if necessary, and decrements `valueoflines` by one. If the
line does not exist, this means that the file is empty, and the program
prints an appropriate message.

If the user enters the command `l` (print all lines), the program prints
all the lines of the file by repeatedly printing each line using the
`ReadCurrLine` function of the `ReadFromAsciiFile` (`rfaf`) class. If
the file is empty, it prints an appropriate message.

If the user enters the command `n` (toggle whether line numbers are
displayed when printing), the program enables or disables the display of
line numbers when printing by setting the variable to zero or one,
respectively, for disabled and enabled. It then prints an appropriate
message.

If the user enters the command `p` (print current line), the program
prints the line of the file on which the user is currently positioned,
using the `ReadCurrLine` function of `rfaf`.

If the user enters the command `q` (end with no save), the editor
terminates without updating the input file with the changes made.

If the user enters the command `w` (write file to disk), the program
updates the input file with the changes made up to that point, using the
`save` function of the `WriteToAsciiFile` (`wtaf`) class.

If the user enters the command `x` (save and exit), the program updates
the input file with the changes made, using the `save` function of
`wtaf`, and then terminates the editor.

If the user enters the command `=` (print current line number), the
program prints the number of the line on which the user is currently
positioned.

If the user enters the command `#` (print number of lines and
characters), the program creates a variable named `sumofchar` to store
the total number of characters in the text. By calling the
`ReadLinesAndChars` function of `rfaf`, it increments `sumofchar` by the
number of characters in each line. Finally, it prints the total number
of characters and the number of lines in the file.

### `file_dll` class

It contains the doubly linked list and all the functions required for
inserting a line, deleting a line, etc.

Before inserting each line, all line-insertion functions call the
`checksize` function. This function checks the length of the line and
returns a string, removing the last words if necessary so that the final
string does not exceed `maxlinelength`. However, it does not split any
word in order to force it to fit within the specified length.

During its execution, `checksize` calls the `getfullstring` function,
which returns a string of the length specified by `checksize` without
removing punctuation marks.

## Part Two

If the user enters the command `c` (create the index file and print the
number of pages it contains), the `CreateIndexAndReturnPages` function
of the `index_file` (`index`) class is called. This function stores in a
byte file the words in the text whose character count is at least
`minwordlength`. It also stores words whose character count exceeds
`maxwordlength`, but only up to the maximum number of characters
specified. Next to each word, it records the line of the file in which
the word was found. The function returns the size, in bytes, of the file
it created. This value is stored in the `bufferlength` variable, and by
dividing it by the `buffersize` variable, the program calculates and
prints the number of pages in that file.

If the user enters the command `v` (print index), the program checks
whether the index file has been created. If it has, it uses the
`printall` function of `index` to print all the data from the index
file.

If the user enters the command `s` (print lines of given word using
serial search), the `ssearch` function of `index` is called. It performs
a sequential search of the index file to check whether the word entered
by the user exists one or more times, and prints the line or lines in
the original file where it occurs. If the word does not exist, it prints
an appropriate message. Finally, the function returns the number of disk
accesses made before it terminates, and this number is also displayed to
the user.

Binary search does not work correctly because of a problem in its
implementation code. Therefore, below is a description of what should
happen.

If the user enters the command `b` (print lines of given word using
binary search), the `bsearch` function of `index` is called. It performs
a binary search of the index file to check whether the word entered by
the user exists one or more times, and prints the line or lines in the
original file where it occurs. If the word does not exist, it prints an
appropriate message. Finally, the function returns the number of disk
accesses made before it terminates, and this number is also displayed to
the user.

### `index_file` class

It contains all the functions required to create, print, and search the
byte file.

All search functions use the `stringCompare` function to check for
equality between the word entered by the user and the words in the index
file. This function returns zero if the two words are identical, a
positive number if the word entered by the user for searching is
"greater" (appears later alphabetically according to ASCII code, or has
a greater number of characters), or a negative number if the word
entered by the user for searching is "smaller" (appears earlier
alphabetically according to ASCII code, or has fewer characters).

Before the editor terminates, the `deleteindex` function of `index` is
called. It deletes the index file if it has been created and prints an
appropriate message indicating whether the file was successfully
deleted.

## EXPERIMENT

The experiment was performed for serial search only.

  ------------------------------------------------------------------------------------------
  Indexing file   testfile_x2.txt   testfile_x5.txt   testfile_x10.txt   testfile_x1000.txt
  -------------- ----------------- ----------------- ------------------ --------------------
  file size            31008             77520             155040             15504000
  (bytes)                                                               

  number of             243               606               1212               121125
  pages                                                                 

                                                                        

  Rectors               70                176               352                35249

  laboratories          172               429               859                85874

  Technical             85                213               426                42561

  Venetia               99                247               495                49499

  Unsuccessful          118               321               593                61548
  Search Average                                                        
  ------------------------------------------------------------------------------------------
