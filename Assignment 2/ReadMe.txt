NOTE : Cranfield Database is not uploaded to git as the file size is large. Please copy the same from Assignment 1.

Instructions to Execute the Program:
1) Copy all the files from the folder "Homework 2/src" into your workspace.

2) Copy all the files from folder "libs" into your workspace.

Note : We need commons-lang3-3.4.jar and commons-cli-1.3.1.jar to execute the code. So please paste the jar files in one folder structure above the current directory.
3) Open Command Prompt/Putty and navigate to the folder where the files are located

4) Compile using the command
	javac -cp "/usr/local/corenlp341/joda-time.jar:/usr/local/corenlp341/jollyday.jar:/usr/local/corenlp341/ejml-0.23.jar:/usr/local/corenlp341/xom.jar:/usr/local/corenlp341/javax.json.jar:/usr/local/corenlp341/stanford-corenlp-3.4.1.jar:/usr/local/corenlp341/stanford-corenlp-3.4.1-models.jar:./commons-cli-1.3.1.jar:./commons-lang3-3.4.jar:." *.java
	
5) Run using the command
   We need to supply input to the program, and we give it as->java <File> -path PATH_TO_CRANFIELD_DOCUMENTS -stop PATH_TO_STOPWORDS
   Run using the command
     -> java -cp "/usr/local/corenlp341/joda-time.jar:/usr/local/corenlp341/jollyday.jar:/usr/local/corenlp341/ejml-0.23.jar:/usr/local/corenlp341/xom.jar:/usr/local/corenlp341/javax.json.jar:/usr/local/corenlp341/stanford-corenlp-3.4.1.jar:/usr/local/corenlp341/stanford-corenlp-3.4.1-models.jar:./commons-cli-1.3.1.jar:./commons-lang3-3.4.jar:." Indexing -path /people/cs/s/sanda/cs6322/Cranfield -stop /people/cs/s/sanda/cs6322/resourcesIR/stopwords 
   If the location of the cranfield documents or stopwords is different, please specify it here.
   
6) The output displayed for a test run is stored in output.txt file. Redirect the standard output of the program to write the output to a file as:-
	java -cp "/usr/local/corenlp341/joda-time.jar:/usr/local/corenlp341/jollyday.jar:/usr/local/corenlp341/ejml-0.23.jar:/usr/local/corenlp341/xom.jar:/usr/local/corenlp341/javax.json.jar:/usr/local/corenlp341/stanford-corenlp-3.4.1.jar:/usr/local/corenlp341/stanford-corenlp-3.4.1-models.jar:./commons-cli-1.3.1.jar:./commons-lang3-3.4.jar:." Indexing -path /people/cs/s/sanda/cs6322/Cranfield -stop /people/cs/s/sanda/cs6322/resourcesIR/stopwords > output.txt
