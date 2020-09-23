package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		HashMap<String,Occurrence> curDoc = new HashMap<String,Occurrence>(1000,2.0f);
		Scanner sc = new Scanner(new File(docFile));
		Occurrence temp = null; 
		while (sc.hasNext()) {
			String word = sc.next();
			
		if(getKeyword(word) == null) {
			continue;
		}// if the word is not a keyword
		else {
			if(curDoc.containsKey(getKeyword(word)) == true) {
				temp = curDoc.get(getKeyword(word));
				temp.frequency = temp.frequency +1;
				curDoc.replace(getKeyword(word), temp);
				
			}// if the keyword is already in the hashmap increment the frequency number
			else {
			temp = new Occurrence(docFile,1);	
				curDoc.put(getKeyword(word), temp);
				
			}// if it is a new keyword
			
		}// if the word is a keyword
			
		}// incrementing through each word in the doc 
		
	
		return curDoc;
	}// end loadKeywordsFromDocument
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		for (String e : kws.keySet()) {
			
			if(keywordsIndex.containsKey(e) ) {
				keywordsIndex.get(e).add(kws.get(e));
				insertLastOccurrence(keywordsIndex.get(e));
			}// true for if there is already a key in the master hash
			else {
				ArrayList<Occurrence> temp = new ArrayList<Occurrence>();
				temp.add(kws.get(e));
				keywordsIndex.put(e,temp);
		}// end else for if the key for that word has not been inserted yet
			
			
		}// for loop that traverses the hash map
		
		
	}// end mergeKeywords
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'k
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 * 
	 * See assignment description for examples  
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
	
		String rtrnWrd = word; 
		for(int i=0; i< word.length()-1; i++) {
			char currchar = word.charAt(i);
				
			if(Character.isLetter(currchar) == false  && word.charAt(i) != '.' && word.charAt(i) != ',' && word.charAt(i) != '?' &&word.charAt(i) != ':'
				     &&word.charAt(i) !=';'  &&word.charAt(i) !='!') {
				return null;
				
			}// if the word is not a punctuation and not a letter
			else if(word.charAt(i) == '.' ||word.charAt(i) == ','  ||word.charAt(i) == '?' ||word.charAt(i) == ':'
			     ||word.charAt(i) ==';'  ||word.charAt(i) =='!') {
				
				if(word.charAt(i+1) != '.' && word.charAt(i +1 ) != ',' && word.charAt(i +1) != '?' &&word.charAt(i +1) != ':'
					     &&word.charAt(i+1) !=';'  &&word.charAt(i+1) !='!') {
				
					return null;
				}// if the next char is a letter
				else {
				rtrnWrd = word.substring(0,i);
					i = word.length();
				}// if the next char is a character	
			}// hits a character		
			
			
		}// checking parameters for any charachters at beginning or end. 
		
		
		
		if(word.charAt(word.length()-1) == '.' ||word.charAt(word.length()-1) == ','  ||word.charAt(word.length()-1) == '?' ||word.charAt(word.length()-1) == ':'
			     ||word.charAt(word.length()-1) ==';'  ||word.charAt(word.length()-1) =='!' ) {
			rtrnWrd = word.substring(0,word.length()-1);
			
		}// end if for last char being a char
		else if(Character.isLetter(word.charAt(word.length()-1)) == false  && word.charAt(word.length()-1) != '.' && word.charAt(word.length()-1) != ',' && word.charAt(word.length()-1) != '?' &&
				word.charAt(word.length()-1) != ':'  &&word.charAt(word.length()-1) !=';'  &&word.charAt(word.length()-1) !='!') {
			return null;
			
		}// if the last char is NOT punctuation
		
		
		rtrnWrd = rtrnWrd.toLowerCase(); 
		
		if(noiseWords.contains(rtrnWrd) == true) {
			return null;
		}// the case where word is a noise word
		else if(rtrnWrd.compareTo(" ") == 0){
			return null;
		}
		else {
			return rtrnWrd;	
		}// else for if the word is not a noise word
			
	}// end getKeyword
	
	


	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 *  
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {

		ArrayList<Integer> midIndx =  new ArrayList<Integer>();
		int first = 0; 
		int last = occs.size()-2;
		int key = (occs.get(occs.size()-1)).frequency;
		boolean duplicate = false;
		
		while(last >= first) {
		int middle = (first +last)/2;
		midIndx.add(middle);
		if((occs.get(middle)).frequency == key ) {
		occs.add(middle, occs.get(occs.size()-1));
		occs.remove(occs.size()-1);
		duplicate = true;
			break;
		}// if there is a duplicate frequency values
		else if((occs.get(middle)).frequency > key ) {	
		first = middle + 1;
		}// end if for middle greater than key
		else {
			last = middle -1;	
		}// end if for middle less than key
		}// binary search while loop
		if(duplicate == false) {
			occs.add(last + 1, occs.get(occs.size()-1));
			occs.remove(occs.size()-1);
		}// to insert last link if not a duplicate
		
		return midIndx;
	}// end insertLastOccurrence
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. 
	 * 
	 * Note that a matching document will only appear once in the result. 
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. 
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
	 * frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, 
	 *         returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {

		ArrayList<String> myDocs = new ArrayList<String>();
		int first = 0; 
		if(keywordsIndex.containsKey(kw1)== false&& keywordsIndex.containsKey(kw2)==false) {
			return null;
		}// end condition if master hash does not have either word
		else if(keywordsIndex.containsKey(kw1) == true && keywordsIndex.containsKey(kw2)==false ) {
			ArrayList<Occurrence> temp = new ArrayList<Occurrence>();
			temp = keywordsIndex.get(kw1);
			int i =0;
			while(i < 5 && i < temp.size()) {
				myDocs.add(temp.get(i).document);
				i++;
			}// loading myDocs list
			
			return myDocs;
		}// end if for if kw2 has no entries
        else if(keywordsIndex.containsKey(kw1) == false && keywordsIndex.containsKey(kw2)==true ) {
			
        	ArrayList<Occurrence> temp = new ArrayList<Occurrence>();
			temp = keywordsIndex.get(kw2);
			int i =0;
			while(i < 5 && i < temp.size()) {
				myDocs.add(temp.get(i).document);
				i++;
			}// loading myDocs list
			return myDocs; 	
        	
		}// end if for if kw1 has no entries
        else {
        	ArrayList<Occurrence> tempkw1 = new ArrayList<Occurrence>();
			tempkw1 = keywordsIndex.get(kw1);	
			ArrayList<Occurrence> tempkw2 = new ArrayList<Occurrence>();
			tempkw2 = keywordsIndex.get(kw2);	
        	int kw1Cntr = 0;
        	int kw2Cntr = 0; 
			while(myDocs.size()<5 && kw1Cntr < tempkw1.size() &&  kw2Cntr < tempkw2.size()) {
				
				if(tempkw1.get(kw1Cntr).frequency > tempkw2.get(kw2Cntr).frequency) {
					if(isDuplicate(tempkw1.get(kw1Cntr).document, myDocs) == true ) {
						kw1Cntr++;
					}// there is a duplicate
					else {
						myDocs.add(tempkw1.get(kw1Cntr).document); 
					}// if there is no duplicate					
					continue;

				}// if kw1 has a higher frequency than kw2
				else if(tempkw2.get(kw2Cntr).frequency > tempkw1.get(kw1Cntr).frequency) {
					if(isDuplicate(tempkw2.get(kw2Cntr).document, myDocs) == true ) {
						kw2Cntr++;
					}// there is a duplicate
					else {
						myDocs.add(tempkw2.get(kw2Cntr).document); 
					}// if there is no duplicate					
					continue;	
					
				}// if kw2 has a higher frequency than kw1
				else if(tempkw2.get(kw2Cntr).frequency == tempkw1.get(kw1Cntr).frequency) {
					if(isDuplicate(tempkw1.get(kw1Cntr).document, myDocs) == true ) {
						kw1Cntr++;
					}// there is a duplicate
					else {
						myDocs.add(tempkw1.get(kw1Cntr).document); 
					}// if there is no duplicate					
					continue;		
					
				}// if kw2 has the same frequency as kw1
			
			}// loading my docs while loop 
			
			
        	if( myDocs.size()<5 ) {
        		if(kw1Cntr == tempkw1.size() && kw2Cntr < tempkw2.size() ) {
        			while(myDocs.size()<5 && kw2Cntr < tempkw2.size()) {
        				if(isDuplicate(tempkw2.get(kw2Cntr).document, myDocs) == true ) {
    						kw2Cntr++;
    					}// there is a duplicate  
        				else {
        					myDocs.add(tempkw2.get(kw2Cntr).document); 
    						kw2Cntr++;
        				}// end else
        				
        			}// loading myDocs list
        			return myDocs;
        		}// end if 
        		else if(kw2Cntr == tempkw2.size() && kw1Cntr < tempkw1.size() ) {
        			while(myDocs.size()<5 && kw1Cntr < tempkw1.size()) {
        				if(isDuplicate(tempkw1.get(kw1Cntr).document, myDocs) == true ) {
    						kw1Cntr++;
    					}// there is a duplicate  
        				else {
            				myDocs.add(tempkw1.get(kw1Cntr).document); 
    						kw1Cntr++;
        				}// end else
        				
        			}// loading myDocs list
        			return myDocs;
        		}// end if 
        		
        	}// if when we exit the loop the array list does not have 5 elements it means that
        	// one or both of the occurrance lists maxed out
        	
        }/// for is they both are true!!!!
		
		return myDocs;
	
	}// end top5search
	
	public boolean isDuplicate(String newDoc, ArrayList<String> myDocs ) {
		
		boolean isDup = false;
		if(myDocs.isEmpty() ) {
			return false;

		}// if the arraylist is empty
		else {
			int i = 0;
			while(i< myDocs.size() ) {
				
				if(myDocs.get(i).compareTo(newDoc)==0 ) {
					isDup = true;
					break;
				}// theres a match!!
				i++;
			}//checking 
		}// array list has an elemnt
	
		
		return isDup;
	}// end isDuplicate

	
	
	
}// end the LittleSearchEngine class
