package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		TrieNode root = new TrieNode(null, null, null);
		Indexes temp;
		TrieNode fstchild= null;
		TrieNode ptr = null;
		TrieNode prev = root;
		
	for (int i =0; i < allWords.length; i++) {
	if (i == 0) {
		short endindx = (short) (allWords[0].length()-1);
		 temp = new Indexes(0,(short)0,endindx);
		 fstchild = new TrieNode(temp, null, null);
		 ptr = fstchild;
	   	root.firstChild = fstchild; 
	   	continue;
	}// if for the first index of the array
	else {
	boolean match = false;
	 prev = root; 
	 ptr = root.firstChild;
	while(match == false) {
	int ndIndx = ptr.substr.wordIndex;
	if(returnIndex(allWords[i], allWords[ndIndx],(int)ptr.substr.startIndex,(int)ptr.substr.endIndex)== 0) {
		if(ptr.sibling != null) {
			ptr= ptr.sibling; 
			continue; 
		}// if there is another sibling in the tree
		if(ptr.sibling == null) {
			short endindx = (short) (allWords[i].length()-1);
			temp = new Indexes(i,(short)0,endindx);
			TrieNode tempnde = new TrieNode(temp, null, null);
			ptr.sibling = tempnde; 
			ptr = tempnde; 
			match = true;
			continue; 
		}//the case where there are no root  prefixes at all in the array at that point.
		
	}// no match condition
	else {
		if((returnIndex(allWords[i], (allWords[ndIndx]),(int)ptr.substr.startIndex,(int)ptr.substr.endIndex)-1) == (int)ptr.substr.endIndex) {
			prev = ptr; 
			ptr = ptr.firstChild; 
			ndIndx = ptr.substr.wordIndex;
			continue; 	
		}// for prefixes like do -> donutaa
			else if(returnIndex(allWords[i], allWords[ndIndx],(int)ptr.substr.startIndex,(int)ptr.substr.endIndex) == ptr.substr.startIndex) {
				if(ptr.sibling != null) {
					ptr= ptr.sibling; 
					continue; 
				}// if there is another sibling in the tree
				if(ptr.sibling == null) {			
					
					short endindx = (short) (allWords[i].length()-1);
					temp = new Indexes(i,ptr.substr.startIndex,endindx);
					TrieNode tempnde = new TrieNode(temp, null, null);
		            ptr.sibling = tempnde; 
					match = true;
					continue; 
				}//the case where there are no common prefixes left for the current word.
				
			}// for a inserted word that has the same prefix and another sib
			
		else {
			
			if(ptr.firstChild != null ) {
				
				short strtIndx = (short)(returnIndex(allWords[i], allWords[ndIndx],(int)ptr.substr.startIndex,(int)ptr.substr.endIndex));
				short smlrPrefixEnd = (short)(returnIndex(allWords[i], allWords[ndIndx],(int)ptr.substr.startIndex,(int)ptr.substr.endIndex)-1);
				temp = new Indexes(ptr.substr.wordIndex,ptr.substr.startIndex,smlrPrefixEnd);
				TrieNode smlrPrefix = new TrieNode(temp,ptr,ptr.sibling);
				prev.firstChild = smlrPrefix;
				ptr.substr.startIndex =strtIndx;
				
				short endindx = (short) (allWords[i].length()-1);
				temp = new Indexes(i,strtIndx ,endindx);
				TrieNode tempnde = new TrieNode(temp, null, null);
				ptr.sibling = tempnde;
				match = true;
				continue; 
				
			}// for if there was already a larger prefix in the works and you need t/o input a smaller one
			else if (ptr.firstChild == null) {
			
			short strtIndx = (short)(returnIndex(allWords[i], allWords[ndIndx],(int)ptr.substr.startIndex,(int)ptr.substr.endIndex));
			// insterted word
			short endindx = (short) (allWords[i].length()-1);
			temp = new Indexes(i,strtIndx,endindx);
			TrieNode tempnde2 = new TrieNode(temp, null, null);
			// old word
			temp = new Indexes(ptr.substr.wordIndex,strtIndx ,ptr.substr.endIndex);
			TrieNode tempnde = new TrieNode(temp, null, tempnde2);
			ptr.substr.endIndex = (short)(returnIndex(allWords[i], allWords[ndIndx],(int)ptr.substr.startIndex,(int)ptr.substr.endIndex)-1);
			ptr.firstChild = tempnde; 
			match = true;
			continue; 
			}
			
		}// for making new prefixes for a word like p -> pop and passionfruit
		
		
	}// for some sort of match at all second most inner else statement
	
	
	}// end tree tracing loop WHILE loop
	}// end if else	for the comparisons most outer else
	
	
	}// end for loop
		
		return root;
	}// end TrieNode
	
	
	public static int returnIndex(String word1, String word2, int beginIdx, int endIndex) {
		if(endIndex == (word2.length()-1) && beginIdx == 0) {
			// don't do anything this is for the whole word
		}// end if statement
		else if(endIndex < (word2.length()-1)) {
			word2 = word2.substring(0,(endIndex+1));
		}// for words that have prefixes already
		
		int i = 0;
		int indexcntr = 0;
		while(i< word1.length() && i< word2.length()) {
			if(word1.charAt(i)==word2.charAt(i)) {
				indexcntr++;
			}// end comparision index
			else if(word1.charAt(i)!=word2.charAt(i)){
				break;
			}
				i++;
		}// end while loop
		
		return indexcntr; 
	}// end returnIndex
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) {
		if(root.firstChild == null) {
			return null;
		}// for if an empty tree is given in
		ArrayList<TrieNode> arrylst = new ArrayList<TrieNode>();
		TrieNode ptr = root.firstChild;
		boolean endOfTheLine = false;
		
		while(endOfTheLine == false) {
			int ndIndx = ptr.substr.wordIndex;
			if(returnIndex(prefix, allWords[ndIndx],(int)ptr.substr.startIndex,(int)ptr.substr.endIndex) == 0 ) {
				if(ptr.sibling != null) {
				ptr= ptr.sibling; 
				continue; 
				}// end if else
				if(ptr.sibling == null) {
					endOfTheLine = true;
					continue;
				}// end if else
				
				
			}// if statement for if there is no match between the prefix and the word being checked
			if(returnIndex(prefix, allWords[ndIndx],(int)ptr.substr.startIndex,(int)ptr.substr.endIndex)> 0 && returnIndex(prefix, allWords[ndIndx],(int)ptr.substr.startIndex,(int)ptr.substr.endIndex) != prefix.length()) {
				if((returnIndex(prefix, allWords[ndIndx],(int)ptr.substr.startIndex,(int)ptr.substr.endIndex)-1) == (int)ptr.substr.endIndex ) {
					if(ptr.firstChild != null) {
						ptr = ptr.firstChild;
						continue; 
					}// end if else for another child
					
				}// for the case where prefix = bean and you are comparing to be
				else if (ptr.sibling != null){
					ptr = ptr.sibling; 
					continue;
				}// end case where prefix = bull and you are comparing be and need the bu prefix
				else if (ptr.sibling == null) {
					endOfTheLine = true;
					continue;
				}// end if 
				
			}// for the base where you are tracing down the right path but do not have a match yet like be with prefix bean
	 if(returnIndex(prefix, allWords[ndIndx],(int)ptr.substr.startIndex,(int)ptr.substr.endIndex) == prefix.length()) {
		if(ptr.firstChild != null && ptr.sibling != null) {
					TrieNode sib = ptr.sibling;
					int sibIndx = sib.substr.wordIndex;
		     if(returnIndex(prefix, allWords[sibIndx],(int)sib.substr.startIndex,(int)sib.substr.endIndex) == 0){
					ptr = ptr.firstChild;
					continue;
			  }// for if the match node's sibling does not match with prefix
		      while(returnIndex(prefix, allWords[sibIndx],(int)sib.substr.startIndex,(int)sib.substr.endIndex) == prefix.length()) {	
					if(sib.firstChild != null && sib.sibling != null) {
						ArrayList<TrieNode> temp = completionList(sib,allWords,prefix); 
						
						if(temp == null) {
							sib = sib.sibling; 
							   sibIndx = sib.substr.wordIndex;
							   continue;
						}// end if else statement for an empty return
						else {
						for(int i = 0; i < temp.size(); i ++) {
							 arrylst.add(temp.get(i));
							
						}// end for loop	
							
						}// end else statement
						sib = sib.sibling; 
						   sibIndx = sib.substr.wordIndex;
						   continue;
					}// recursive point????
					   if(sib.firstChild == null && sib.sibling != null) {
						   arrylst.add(sib);
						   sib = sib.sibling; 
						   sibIndx = sib.substr.wordIndex;
						   continue;
					   }// for the case where there is a sibling for sib but sib also fits the prefix
					   if(sib.firstChild != null && sib.sibling == null) {
						   sib = sib.firstChild;
						   sibIndx = sib.substr.wordIndex;
						   continue; 
					   }// for if there is a kid but no more siblings
					   if(sib.firstChild == null && sib.sibling == null) {
						   arrylst.add(sib);
						   break;
					   }// for if there is no kids and no more siblings
					   
		       }// for if a node with children has siblings that also work with the prefix			
					ptr= ptr.firstChild;
					continue;
		}// in the case that ptr has a child which means its not a leaf node and it is an prefix in itself.
		if(ptr.firstChild == null && ptr.sibling != null) {
			arrylst.add(ptr);
			   ptr = ptr.sibling; 
			   continue;
		}// if statement for if there are no more children for that node but it has siblings.
		if(ptr.firstChild != null && ptr.sibling == null) {
			ptr = ptr.firstChild;
			   continue; 
			
		   }// for if there is a kid but no more siblings	
		 if(ptr.firstChild == null && ptr.sibling == null) {
			   arrylst.add(ptr);
			   endOfTheLine = true;
			   break;
			   // may be a problem with the way the while loop is structured may want to switch to a boolean t-f condition
		   }// for if there is no kids and no more siblings	 
			
	}// for a prefix match
	 else {
		 if(ptr.firstChild == null && ptr.sibling == null) {
			   endOfTheLine = true;
			   break;
			   // may be a problem with the way the while loop is structured may want to switch to a boolean t-f condition
		   }// for if there is no kids and no more siblings	 
	 }// end else
			
		}// main while loop
		
		if(arrylst.isEmpty()) {
			return null;
		}// for is there are no matches
		
		return arrylst;
	}// end Array List
	
	
	
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");

		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
