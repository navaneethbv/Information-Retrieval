import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
/*
 * Name:Navaneeth Rao
 * NetId: nbv140130
 * Subject:Information Retrieval
 * ClassNo.:CS6322.501
*/
public class tokenization
{
	static HashMap<String, Integer> token=new HashMap<String, Integer>();//Used for storing the Tokens
	static HashMap<String, Integer> stem=new HashMap<String, Integer>();//Used for storing the values after stemming
	static int countTotalWords;
	static int StemCount;
	static int length;
	public static void main(String args[])
	{
		long starttime= System.currentTimeMillis();//Start Time of Program
		File folder=new File(args[0]);//Path of the Folder Where the input files are present.
		try 
		{
			readingFiles(folder);//Function to process the files,Checks weather it is file or directory.
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		System.out.println("-----------------------------------------------");
		System.out.println("Tokenization");
		System.out.println("-----------------------------------------------");
		System.out.println("Total Number of the Documents:"+length);
		System.out.println("1: Total number of tokens:"+countTotalWords);
		System.out.println("2: Average number of tokens per document:"+countTotalWords / length);
		System.out.println("3: Number of unique words:"+ token.size());
		System.out.println("4: Number of words that occur only once:"+  oneFrequency(token));
		topFrequency30(token);
		long endtime=System.currentTimeMillis();//End Time for Tokenization
		System.out.println("-----------------------------------------------");
		long runningtime=(long)endtime-starttime;//Total Time taken to Tokenization.
		System.out.println("Time taken to processes the token is:"+runningtime);
		System.out.println("-----------------------------------------------");
		stem(token);
		System.out.println("Stemming");
		System.out.println("-----------------------------------------------");
		System.out.println("Total Number of the Documents:"+length);
		System.out.println("1: Total number of tokens:"+StemCount);
		System.out.println("2: Average number of tokens per document:"+StemCount / length);
		System.out.println("3: Number of unique words:"+ stem.size());
		System.out.println("4: Number of words that occur only once:"+  oneFrequency(stem));
		topFrequency30(stem);
		System.out.println("-----------------------------------------------");
	}
	public static void readingFiles(File file) throws FileNotFoundException//Function used to find weather a file is Directory or not
	{																	  
		for(File entry:file.listFiles())
		{
			if(entry.isDirectory())//To Find weather a entry is directory or not
			{
				readingFiles(entry);//If the file is a Directory then make a recursive call with the new entry as the directory path.
			}
			else   //If Entry is File then it sent to read the contents.
			{
				try
				{
					readfile(entry);//Function to read the File Contents with the File entry as a Parameter
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			length++;
		}
	}
	private static void readfile(File entry)throws IOException //Function to Read the Contents of the file DO the Tokenization
	{
		if(entry==null)//If The File Contents are null/empty then it is returned a null value.
		{
			return;
		}
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(entry));
			for(String s;(s = br.readLine()) != null;)//Reading Each Line for the contents,If Line is null then it is exited
			{
				s=s.replaceAll("\\<.*?>","");//Removing the SGML tags and replacing it with space.
				s=s.replaceAll("[\\d+]","");//Removing the Digits and Replacing them with null.
				s=s.replaceAll("[+^:,?;=%#&~`$!@*_)/(}{]","");//Remove the special characters except "." and replace them with null.
				s=s.replaceAll("'s"," ");//Remove word/lines containing "'s" with null value.
				s=s.replaceAll("\\'"," ");//Remove "'" with a space.
				s=s.replaceAll("-"," ");//Remove the words containing "-" with a space making them as two separate values.
				s=s.replaceAll("\\.","");//Remove the words containing "." with a null value.
				if(s != null)
				{
					String [] words = s.split(" ");//Splitting the line into words using the space as delimiter.
					for(String word:words)//Processing each word 
					{
						word = word.trim();//Trimming spaces if any.
						word = word.toLowerCase();//Converting all the words into lowercase
						if(word.length()<1)//If the word was a space then we are not processing.
						{
							continue;
						}
						Integer count = token.get(word);//Get the count value of the word.
						if(count == null)//First occurrence of the token
						{
							token.put(word, 1);
						}
						else//Multiple occurrence of the token
						{	
							count++;
							token.put(word, count);
						}
						//System.out.println(word);
						countTotalWords++;
					}
					
			}
				
		}
			br.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	private static  Integer oneFrequency(HashMap<String,Integer> token)//Function to count the number of words that occur only once.
	{
		int cnt = 0;
		Iterator<Entry<String, Integer>> it = token.entrySet().iterator();
	    while (it.hasNext())
	    {
	        Map.Entry<String,Integer> one = it.next();
	        if(one.getValue() == 1)
	        	cnt++;
	    }
	    return cnt;
	}
	private static void topFrequency30(HashMap<String,Integer> tokens)//Function to Print the Top 30 tokens and Stems
	{
		value vc = new value(tokens);//Sorting of the values
		TreeMap<String, Integer> tokensTree = new TreeMap<String, Integer>(vc);
		int count = 0;
		tokensTree.putAll(tokens);
		System.out.println("5:\n\tS.no\tToken\tFrequency");
		System.out.println("\t--------------------------");
		for(String key : tokensTree.descendingMap().keySet())
		{
			count++;
			if (count > 30) break;
			int frequency = tokens.get(key);
		
			System.out.println("\t"+count+"\t"+key+"\t"+frequency);
		}
	}
	private static void stem(HashMap<String, Integer> tokens)//Function to perform stemming on the tokens
	{
		Stemmer stm = new Stemmer();
		for ( Entry<String, Integer> token : tokens.entrySet())
		{
			String temporary = token.getKey();
			for(int j=0; j <temporary.toCharArray().length; j++)
			{
				stm.add(temporary.toCharArray()[j]);
			}
			stm.stem();
			String stemWord = stm.toString();
			if (stem.containsKey(stemWord))
			{
				int countStem = stem.get(stemWord);
				countStem++;
				stem.put(stemWord, countStem);
			} 
			else
			{
				stem.put(stemWord, 1);
			}
			StemCount++;
		}
		
	}
	static class value implements Comparator<String>//Class to perform sorting of the values
	{

	    Map<String, Integer> b;

	    value(Map<String, Integer> b)
	    {
	        this.b = b;
	    }

	    @Override
	    public int compare(String sone, String stwo)
	    {
	        if (b.get(sone) >= b.get(stwo))
	        {
	            return 1;
	        } 
	        else
	        {
	            return -1;
	        }
	    }	
	 }

}
