To compile and execute the assignment, follow these steps - 

Step 1: Login to csgrads1.utdallas.edu server and go to the following path

Copy the files from the folder called Source code into your workspace, and paste all the java files. 
Or 
go to /home/013/n/nb/nbv140130/Homework 3     to get the files.

Step 2: Type the following command on the csgrads1 machine to set the class path for Lemmatizer.

source /usr/local/corenlp341/classpath.sh

Step 3: Complie all java Files

javac -classpath $CLASSPATH *.java

Step 4: Run Assignment file using below command (java filename data-path stopwords-path)
		It runs both versions of Index.
		
java Assignment /people/cs/s/sanda/cs6322/Cranfield/ /people/cs/s/sanda/cs6322/hw3.queries /people/cs/s/sanda/cs6322/resourcesIR/stopwords


The output and Report is stored in Output and Report folder.
