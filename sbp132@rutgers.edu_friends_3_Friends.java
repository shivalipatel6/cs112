package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		ArrayList<String> friendChain = new ArrayList<String>();
		int cntr = g.map.size(); 
		boolean p1Included = false;
		boolean p2Included = false;
		int startName = 0;
		int endName = 0;
		int [] parent = new int[cntr];
		for (int i = 0; i< cntr; i++) {
			if( g.members[i].name.equals(p1) == true) {
				p1Included = true;
				startName = i;
			}// if the first person is in the graph
			else if( g.members[i].name.equals(p2) == true) {
				endName = i;
				p2Included = true;
			}// if the first person is in the graph
			
		}// while loop to see if students are in the graph
		
		if(p1Included == false || p2Included == false) {
			return null;
		}// end case where one of the people or both are not in the graph
		boolean [] visted = new boolean[cntr];
		Queue<Integer> cue = new Queue<Integer>(); 
		visted[startName] = true;
		cue.enqueue(startName);
		parent[startName] = startName; 
		while(cue.size() > 0 ) {
			int curr;
		curr = cue.dequeue();
		
		for (Friend nbr = g.members[curr].first; nbr != null; nbr=nbr.next ) {	
			
			if(visted[nbr.fnum] == false ) {
				visted[nbr.fnum] = true;	
				cue.enqueue(nbr.fnum);
				parent[nbr.fnum] = curr; 
			if(nbr.fnum == endName ) {
				
				return getPath(g, parent, startName, endName);
				
			}// if we find the end vertex
				
			}// if the vertex has not been visited
			
			
		}// end for loop
			
		
		}// end while loop
		
		
		return friendChain;
	}// end shortestChain
	
	public static ArrayList<String> getPath (Graph g, int[] parent, int startName, int endName ){
		Stack<Integer> path = new Stack<Integer>(); 
		int currIndex = endName; 
		path.push(endName);
		
		while(parent[currIndex] != startName) {
			path.push(parent[currIndex]);
			currIndex = parent[currIndex]; 
			
		}// while loop to situate stack and pull out path
		path.push(startName);

		 ArrayList<String> shortestPath = new ArrayList<String>();
		
		 while(!path.isEmpty()) {
			 shortestPath.add(g.members[path.pop()].name );
			 
		 }// end while loop that is emptying stack and filling up the arrayList
		
		
		return shortestPath; 
	}// end get path
	

	
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		ArrayList<ArrayList<String>> masterList = new ArrayList<ArrayList<String>>();
		int cntr = g.map.size(); 
		boolean [] visted = new boolean[cntr];
		for(int i = 0; i< cntr; i++) {

			if(g.members[i].student == false) {
				visted[i] = true; 
				continue;
			}//the friend is not a student we need to mark as visited and move on
			else if(g.members[i].school.compareTo(school) != 0 ) {
				visted[i] = true; 
				continue;	
			}// the friend does not go to the same school, mark as visted and move on
			else if (visted[i]== false) {
			//	ArrayList<String> temp = new ArrayList<String>();
				masterList.add(dsfcliques(i, visted, g, school));
				
			}// recursive portion call DSF and pass the vertex, the boolean array, and array list of that clique
				
		}// end while loop that goes through each vertex in the graph

		return masterList;
		
	}// end cliques
	
	
	public static ArrayList<String> dsfcliques(int v, boolean[] visted, Graph g, String school  ){
		ArrayList<String> tempList = new ArrayList<String>();
		visted[v] = true;
		tempList.add(g.members[v].name); 
		for (Friend nbr = g.members[v].first; nbr != null; nbr=nbr.next ) {
			if(g.members[nbr.fnum].student == false) {
				visted[nbr.fnum] = true; 
				continue;
			}//the friend is not a student we need to mark as visited and move on
			else if(g.members[nbr.fnum].school.compareTo(school) != 0 ) {
				visted[nbr.fnum] = true; 
				continue;	
			}// if the friend is not in the same school
			else if(visted[nbr.fnum] == false ) {
				tempList.addAll(dsfcliques(nbr.fnum, visted, g, school ));
				
			}// end else if for if the friend is in the same school and has noe been visited	
			
		}// for loop iterating through friend links
	
		
		return tempList; 
	}// end dsf
	

	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		ArrayList<String> fullList = new ArrayList<String>();
		int cntr = g.map.size(); 
		boolean [] visted = new boolean[cntr];
		int [] dsfNum = new int[cntr];
		int [] back = new int[cntr];
		for(int i = 0; i< cntr; i++) {
			int count = 1;
			int vertChecked2c = 0;
			fullList.addAll(dsfConnectors(i,vertChecked2c, g, visted, count, dsfNum, back) );
			
		}// dsf is happening here!!!!
	
		
		return fullList;
		
	}// end connectors
	
	public static ArrayList<String> dsfConnectors(int vertex, int vertChecked2c, Graph g, boolean[] visted, int count,int [] dsfNum, int [] back ){
		ArrayList<String> temp = new ArrayList<String>();
		visted[vertex] = true; 
		dsfNum[vertex]= count;
		back[vertex] = count;
		count++;
		int vertCkedtwce = vertChecked2c;
		for (Friend nbr = g.members[vertex].first; nbr != null; nbr=nbr.next ) {
			
			
			if(visted[nbr.fnum] == false ) {
				temp.addAll(dsfConnectors(nbr.fnum,vertCkedtwce, g, visted, count, dsfNum, back));
				if(dsfNum[vertex] != 1  ) {
					
					if(dsfNum[vertex] > back[nbr.fnum] ) {
					back[vertex] = Math.min(back[vertex], back[nbr.fnum]);	
					
					}// end if else statement
					if( temp.contains(g.members[vertex].name)== false && dsfNum[vertex] <= back[nbr.fnum]  ) {
						
						temp.add( g.members[vertex].name);
						
					}// there is a connector
					
				}// case for checking for connectorss if we are not looking at the first vertex.		
				else if(dsfNum[vertex] == 1 ){
					if( temp.contains(g.members[vertex].name)== false && dsfNum[vertex] <= back[nbr.fnum] && vertCkedtwce == 0 ) {
						vertCkedtwce++;
						
					}// the first vertex may be a connector but it passed once
					else if(temp.contains(g.members[vertex].name)== false && dsfNum[vertex] <= back[nbr.fnum] && vertCkedtwce == 1 ) {
						temp.add( g.members[vertex].name);
						
					}//the first vertex is a connector cause it passed twice
					
				}// if we are looking at the first vertex
				
			}//// if we have not visited
			else {
			back[vertex] = Math.min(back[vertex], dsfNum[nbr.fnum] );
				
			}// we have visited!!!!!
			
		}// end for loop
	
	
	return temp; 	
	}// end dsfConnectors
	
	
	
}// freinds class

