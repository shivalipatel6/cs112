package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	StringTokenizer str = new StringTokenizer(expr,delims, true);
    	String curr = "";
    	String temp = ""; 
    	while(str.hasMoreTokens()) {
    		curr = str.nextToken();
    		if (Character.isLetter(curr.charAt(0)) == true){
    			
    			if(str.hasMoreTokens()) {
    				temp = curr; 
        			curr = str.nextToken();
    			if(curr.contentEquals("[")) {
    				Array tempA = new Array(temp);
    				if(arrays.contains(tempA)) {
    					continue; 
    				}// for if there is a duplicate
    				else {
    					arrays.add(new Array(temp));
    				}// if no duplicate create new var
    			}// checks for array
    			else {
    				Variable tempV = new Variable(temp);
    				if(vars.contains(tempV)) {
    					continue;
    				}// for if there is a duplicate
    				else {
    					vars.add(new Variable(temp));
    				}// if no duplicate create new var
    				
    			}// creates variable		
    			}//  end if it is the last token check
    			else {
    		Variable tempV = new Variable(curr);
    				if(vars.contains(tempV)) {
    				}// for if there is a duplicate
    				else {
    					vars.add(new Variable(curr));
    				}// if no duplicate create new var
    			}
    		}// end variable or array if
    		
    	}// end while loop
    	
  
    }// end  makeVariableLists
    
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	Stack<Float> stk = new Stack<Float>(); 
    	Stack<Character> stkchr = new Stack<Character>(); 
    	expr = expr.replaceAll("\\s+","");
    	StringTokenizer str = new StringTokenizer(expr,delims, true);
    	
    	StringTokenizer arrayliststr = new StringTokenizer(expr,delims, true);
    	ArrayList<String> arryLstExpr = new ArrayList<String>();
    	String filler = "";
    	while(arrayliststr.hasMoreTokens()) {
    		filler = arrayliststr.nextToken();
    		arryLstExpr.add(new String(filler));
    	}// filling arraylist
    	
    	String curr = "";
    	String temparray = ""; 
    	while(str.hasMoreTokens()) {
    		curr = str.nextToken();

    		if(Character.isDigit(curr.charAt(0)) == true  ) {
    		stk.push(Float.parseFloat(curr));		
    		arryLstExpr.remove(0);
    		}// if statement for digits
    		else if(curr.charAt(0) == '+'|| curr.charAt(0) == '-') {
    			stkchr.push(curr.charAt(0) );
    			arryLstExpr.remove(0);
    		}// if for catching operators + - 
    		else if(curr.charAt(0) == '*'|| curr.charAt(0) == '/') {
    			if(curr.charAt(0) == '*') {
    	    		curr = str.nextToken();
    	    		arryLstExpr.remove(0);
    	    		if (Character.isLetter(curr.charAt(0)) == true) {
    	    			
    	    			if(str.hasMoreTokens()) {
    	    				 temparray = arryLstExpr.get(1);// issue is HERE
    	         			if(temparray.contentEquals("[")) {
    	         				int i = 2; 
    	             			int temp = 0;
    	             			int index = 0;
    	             			boolean nestedBraket = false; 
    	            			while(i< arryLstExpr.size() ) {// arryLstExpr.get(i).equals(")")
    	            				
    	            				if(arryLstExpr.get(i).equals("]")) {
    	            					temp = i;
    	            				}// comparison for close bracket
    	            				else if (temp== 0 && arryLstExpr.get(i).equals("[")) {
    	            					nestedBraket = true;
    	            				}// 
    	            				if(temp!= 0 && arryLstExpr.get(i).equals("[") && nestedBraket == false) {
    	            					i = arryLstExpr.size();
    	            				}// comparison for close bracket
    	            				
    	            				i++;
    	            				
    	            			}// finding the end parenthesis
    	             		String sendover = ""; 
    	             		 int j = 2; 
    	             		 while(j< temp) {
    	             			 sendover= sendover + arryLstExpr.get(j);
    	             			 j++;
    	             		 }// end creating send off string 
    	             			index =(int)evaluate(sendover,vars,arrays);
    	             	  
    	             	     Array tempA = new Array(curr); 	
    	          			stk.push(stk.pop()*(float)((arrays.get(arrays.indexOf(tempA)).values[index])));
    	          		   j = 0;	
    	          			while(j< temp) {
    	             	    	 curr = str.nextToken();
    	             	    	 arryLstExpr.remove(0);
    	                 	   j++;
    	             	     }// end creating send off string 		
    	            		arryLstExpr.remove(0);

    	        				
    	        			}// for array
    	        			else {
    	        				Variable tempV = new Variable(temparray);
    	        			stk.push(stk.pop()*(float)((vars.get(vars.indexOf(tempV)).value)));
    	        			arryLstExpr.remove(0);
    	        			}// for variable
    	    		}// for arrays and variables that are not the last one
    	    		else {
    	    			Variable tempV = new Variable(curr);
    	   				stk.push(stk.pop()*(float)((vars.get(vars.indexOf(tempV)).value)));
    	   				arryLstExpr.remove(0);
    	    			}// for last variables
    	    				
    	    		}// for variables or for arrays
    	    		else if( curr.charAt(0) == '(') {	
    	    			int i = 1; 
    	    			int temp = 0; 
    	    			boolean nestedBraket = false; 
            			while(i< arryLstExpr.size() ) {// arryLstExpr.get(i).equals(")")
            				
            				if(arryLstExpr.get(i).equals(")")) {
            					temp = i;
            				}// comparison for close bracket
            				else if (temp== 0 && arryLstExpr.get(i).equals("(")) {
            					nestedBraket = true;
            				}// 
            				if(temp!= 0 && arryLstExpr.get(i).equals("(") && nestedBraket == false) {
            					i = arryLstExpr.size();
            				}// comparison for close bracket
            				
            				i++;
            				
            			}// finding the end parenthesis
    	    		String sendover = ""; 
    	    		 int j = 1; 
    	    		 while(j< temp) {
    	    			 sendover= sendover + arryLstExpr.get(j);
    	    			 j++;
    	    		 }// end creating send off string 
    	    			stk.push(stk.pop()*evaluate(sendover,vars,arrays));
    	    	     j = 0;		
    	    	     while(j< temp) {
    	    	    	 curr = str.nextToken();
    	    	    	 arryLstExpr.remove(0);
    	        	   j++;
    	    	     }// end creating send off string
	    	    	 arryLstExpr.remove(0);

    	    		}// when paranetisis comes first
    	    		else {
    	    			stk.push(stk.pop()* Float.parseFloat(curr));
    	    			arryLstExpr.remove(0);
    	    		}// for plain old multiplication
    		
    			}// for multiplication
    			else if(curr.charAt(0) == '/') {
    				curr = str.nextToken();
            		arryLstExpr.remove(0);
    				if (Character.isLetter(curr.charAt(0)) == true) {
    	    			
    	    			if(str.hasMoreTokens()) {
    	    				 temparray = arryLstExpr.get(1);// issue is HERE
    	         			if(temparray.contentEquals("[")) {
    	         				int i = 2; 
    	             			int temp = 0;
    	             			int index = 0;
    	             			boolean nestedBraket = false; 
    	            			while(i< arryLstExpr.size() ) {// arryLstExpr.get(i).equals(")")
    	            				
    	            				if(arryLstExpr.get(i).equals("]")) {
    	            					temp = i;
    	            				}// comparison for close bracket
    	            				else if (temp== 0 && arryLstExpr.get(i).equals("[")) {
    	            					nestedBraket = true;
    	            				}// 
    	            				if(temp!= 0 && arryLstExpr.get(i).equals("[") && nestedBraket == false) {
    	            					i = arryLstExpr.size();
    	            				}// comparison for close bracket
    	            				
    	            				i++;
    	            				
    	            			}// finding the end parenthesis
    	             		String sendover = ""; 
    	             		 int j = 2; 
    	             		 while(j< temp) {
    	             			 sendover= sendover + arryLstExpr.get(j);
    	             			 j++;
    	             		 }// end creating send off string 
    	             			index =(int)evaluate(sendover,vars,arrays);
    	             	  
    	             	     Array tempA = new Array(curr); 	
    	          			stk.push(stk.pop()/(float)((arrays.get(arrays.indexOf(tempA)).values[index])));
    	          		   j = 0;	
    	          			while(j< temp) {
    	             	    	 curr = str.nextToken();
    	             	    	 arryLstExpr.remove(0);
    	                 	   j++;
    	             	     }// end creating send off string 		

    	        			}// for array
    	        			else {
    	        				Variable tempV = new Variable(temparray);
    	       				stk.push(stk.pop()/(float)((vars.get(vars.indexOf(tempV)).value)));
    	        			arryLstExpr.remove(0);
    	        			}// for variable
    	    		}// for arrays and variables that are not the last one
    	    		else {
    	    			Variable tempV = new Variable(curr);
    	   				stk.push(stk.pop()/(float)((vars.get(vars.indexOf(tempV)).value)));
            			arryLstExpr.remove(0);
    	    			}// for last variables
    	    				
    	    		}// for variables or for arrays
    	    		else if (curr.charAt(0) == '(') {	
    	    			int i = 1; 
    	    			int temp = 0; 
    	    			boolean nestedBraket = false; 
            			while(i< arryLstExpr.size() ) {// arryLstExpr.get(i).equals(")")
            				
            				if(arryLstExpr.get(i).equals(")")) {
            					temp = i;
            				}// comparison for close bracket
            				else if (temp== 0 && arryLstExpr.get(i).equals("(")) {
            					nestedBraket = true;
            				}// 
            				if(temp!= 0 && arryLstExpr.get(i).equals("(") && nestedBraket == false) {
            					i = arryLstExpr.size();
            				}// comparison for close bracket
            				
            				i++;
            				
            			}// finding the end parenthesis
    	    		String sendover = ""; 
    	    		 int j = 1; 
    	    		 while(j< temp) {
    	    			 sendover= sendover + arryLstExpr.get(j);
    	    			 j++;
    	    		 }// end creating send off string 
    	    			stk.push(stk.pop()/evaluate(sendover,vars,arrays));
    	    	     j = 0;		
    	    	     while(j< temp) {
    	    	    	 curr = str.nextToken();
    	    	    	 arryLstExpr.remove(0);
    	        	   j++;
    	    	     }// end creating send off string
	    	    	 arryLstExpr.remove(0);

    	    			
    	    		}// for division with paranetesis
    	    		else {
    	    			stk.push(stk.pop()/ Float.parseFloat(curr));
    	    			arryLstExpr.remove(0);
    	    		}// for plain old division
        			
    			}// divide
    		}// if for catching operators * / 
    		else if (curr.charAt(0) == '(') {
    			int i = 1; 
    			int temp = 0; 
    			boolean nestedBraket = false; 
    			while(i< arryLstExpr.size() ) {// arryLstExpr.get(i).equals(")")
    				
    				if(arryLstExpr.get(i).equals(")")) {
    					temp = i;
    				}// comparison for close bracket
    				else if (temp== 0 && arryLstExpr.get(i).equals("(")) {
    					nestedBraket = true;
    				}// 
    				if(temp!= 0 && arryLstExpr.get(i).equals("(") && nestedBraket == false) {
    					i = arryLstExpr.size();
    				}// comparison for close bracket
    				
    				i++;
    				
    			}// finding the end parenthesis
    		String sendover = ""; 
    		 int j = 1; 
    		 while(j< temp) {
    			 sendover= sendover + arryLstExpr.get(j);
    			 j++;
    		 }// end creating send off string 
    			stk.push( evaluate(sendover,vars,arrays));
    	     j = 0;		
    	     while(j< temp) {
    	    	 curr = str.nextToken();
    	    	 arryLstExpr.remove(0);
        	   j++;
    	     }// end creating send off string
	    	 arryLstExpr.remove(0);

    	     
    		}// for parenthsis
    		else if (Character.isLetter(curr.charAt(0)) == true) {
    			
    			if(str.hasMoreTokens()) {
    			
    				 temparray = arryLstExpr.get(1);// issue is HERE
        			if(temparray.contentEquals("[")) {
        				int i = 2; 
            			int temp = 0;
            			int index = 0;
            			boolean nestedBraket = false; 
            			while(i< arryLstExpr.size() ) {// arryLstExpr.get(i).equals(")")
            				
            				if(arryLstExpr.get(i).equals("]")) {
            					temp = i;
            				}// comparison for close bracket
            				else if (temp== 0 && arryLstExpr.get(i).equals("[")) {
            					nestedBraket = true;
            				}// 
            				if(temp!= 0 && arryLstExpr.get(i).equals("[") && nestedBraket == false) {
            					i = arryLstExpr.size();
            				}// comparison for close bracket
            				
            				i++;
            				
            			}// finding the end parenthesis
            		String sendover = ""; 
            		 int j = 2; 
            		 while(j< temp) {
            			 sendover= sendover + arryLstExpr.get(j);
            			 j++;
            		 }// end creating send off string 
            			index =(int)evaluate(sendover,vars,arrays);
            	  
            	     Array tempA = new Array(curr); 	
         			stk.push((float)((arrays.get(arrays.indexOf(tempA)).values[index])));
         		   j = 0;	
         			while(j< temp) {
            	    	 curr = str.nextToken();
            	    	 arryLstExpr.remove(0);
                	   j++;
            	     }// end creating send off string 		
            		arryLstExpr.remove(0);

        				
        					
        				
        			}// for array
        			else {
        				Variable tempV = new Variable(curr);
       				stk.push((float)((vars.get(vars.indexOf(tempV)).value)));
       	    	 arryLstExpr.remove(0);
        			}// for variable
    		}// for arrays and variables that are not the last one
    		else {
    			Variable tempV = new Variable(curr);
   				stk.push((float)((vars.get(vars.indexOf(tempV)).value)));
   	    	 arryLstExpr.remove(0);
    			}// for last variables
    			
    			
    		}// for variables or for arrays
    	
    		
    	}// while string tokenizer has more tokens
    	
    	
    	
    	stk = flipStackFloat(stk); 
    	stkchr = flipStackChar(stkchr);
    	while(!stkchr.isEmpty()) {
    	
    		if(stkchr.peek()== '-') {
    			float temp1 = stk.pop();
    			float temp2 = stk.pop();
    			stk.push(temp1-temp2); 
    			stkchr.pop();
    		}// for minus
    		else if(stkchr.peek()== '+'){
    			float temp1 = stk.pop();
    			float temp2 = stk.pop();
    			stk.push(temp2 + temp1); 
    			stkchr.pop();
    		}// for plus
    		
    	}// addition and subtraction evalutation work
    	
    	
    	// following line just a placeholder for compilation
    	return stk.pop();
    }// end of evlauator
    
    private static Stack<Float> flipStackFloat(Stack<Float> stk) {
    	Stack<Float> temp = new Stack<Float>(); 
    	
       while(!stk.isEmpty()) {
    	
    		temp.push(stk.pop());
    		
    	}//
  
    	return temp; // temp solution
    }// end flipStackFloat
    
    private static Stack<Character> flipStackChar(Stack<Character> stkchr) {
    	Stack<Character> temp = new Stack<Character>(); 

       while(!stkchr.isEmpty()) {
    	
    		temp.push(stkchr.pop());
    		
    	}//
  
    	return temp; // temp solution
    }// end flipStackFloat
    
}// end of CLASSSS Expresion
