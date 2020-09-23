package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author Shivali Patel sbp132 spb132@scarletmail.rutgers.edu 
 *
 */
public class Polynomial {

	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage
	 * format of the polynomial is:
	 * 
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * 
	 * with the guarantee that degrees will be in descending order. For example:
	 * 
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * 
	 * which represents the polynomial:
	 * 
	 * <pre>
	 * 4 * x ^ 5 - 2 * x ^ 3 + 2 * x + 3
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients
	 *         and degrees read from scanner
	 */
	public static Node read(Scanner sc) throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}

	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input
	 * polynomials. The returned polynomial MUST have all new nodes. In other words,
	 * none of the nodes of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the
	 *         returned node is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {
		Node poly3 = null;
		Node ptr3 = poly3;
		Node ptr1 = poly1;
		Node ptr2 = poly2;
		boolean emptyNode = true; 
		float tempCoeff = 0;
		int tempdegr = 0;
		
		if (poly1 == null && poly2 == null) {
			return poly3;
		} // end if statement

		while (ptr1 != null || ptr2 != null) {

			if (ptr1 == null) {
				while (ptr2 != null ) {
					tempCoeff = ptr2.term.coeff;
					tempdegr = ptr2.term.degree;
					if (ptr3 == null && emptyNode == true) {
						poly3 = new Node(tempCoeff, tempdegr, null);
						ptr3 = poly3;
						emptyNode = false;
					} // end if
					else {
						ptr3.next = new Node(tempCoeff, tempdegr, null);
						ptr3 = ptr3.next;
					}
					ptr2 = ptr2.next;
					
				} // end the append while
				return poly3;
			} // end if statement about poly1 = null

			if (ptr2 == null) {
				while (ptr1 != null) {
					tempCoeff = ptr1.term.coeff;
					tempdegr = ptr1.term.degree;
					if (ptr3 == null && emptyNode == true) {
						poly3 = new Node(tempCoeff, tempdegr, null);
						ptr3 = poly3;
						emptyNode = false;						
					} // end if
					else {
						ptr3.next = new Node(tempCoeff, tempdegr, null);
						ptr3 = ptr3.next;
					} // end if else
					ptr1 = ptr1.next; 
				} // end the append while
				return poly3;
			} // end if statement about poly2 = null

			if (ptr1.term.degree == ptr2.term.degree) {

				if (ptr1.term.coeff + ptr2.term.coeff == 0) {
					ptr2 = ptr2.next;
					ptr1 = ptr1.next;
				} // end cancel out condition
				else {
					tempCoeff = ptr1.term.coeff + ptr2.term.coeff;
					tempdegr = ptr1.term.degree;
					if (ptr3== null && emptyNode == true) {
						poly3 = new Node(tempCoeff, tempdegr, null);
						ptr3 = poly3;
						emptyNode = false;
					} // end if
					else {
						ptr3.next = new Node(tempCoeff, tempdegr,null);
						ptr3 = ptr3.next;
					} // end else for not null ptr3
					ptr2 = ptr2.next;
					ptr1 = ptr1.next;
				
				} // end else condition

			} // end degree comparison
			else if (ptr1.term.degree < ptr2.term.degree) {
				tempCoeff = ptr1.term.coeff;
				tempdegr = ptr1.term.degree;
				if (ptr3 == null && emptyNode == true) {
					poly3 = new Node(tempCoeff, tempdegr, null);
					ptr3 = poly3;
					emptyNode = false;
				} // end if
				else {
					ptr3.next = new Node(tempCoeff, tempdegr,null);
					ptr3= ptr3.next;
				}
				ptr1 = ptr1.next;
			} else if (ptr2.term.degree < ptr1.term.degree) {
				tempCoeff = ptr2.term.coeff;
				tempdegr = ptr2.term.degree;
				if (ptr3 == null && emptyNode == true) {
					poly3 = new Node(tempCoeff, tempdegr, null);
					ptr3 = poly3;
					emptyNode = false;
				} // end if
				else {
					ptr3.next = new Node(tempCoeff, tempdegr, null);
					ptr3= ptr3.next;
				}
				ptr2 = ptr2.next;
			}

		} // end while loop

		return poly3;
	}// end node add

	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input
	 * polynomials. The returned polynomial MUST have all new nodes. In other words,
	 * none of the nodes of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the
	 *         returned node is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		Node poly3 = null;
		Node ptr3 = poly3;
		Node ptr1 = poly1;
		Node ptr2 = poly2;
		Node fnlpoly = null;
		Node fnlptr = fnlpoly;
		float tempCoeff = 0;
		int tempdegr = 0;
		boolean emptyNode = true;
		
		if (poly1 == null || poly2 == null) {
			return poly3;
		} // edgecase 1

		while (ptr1 != null) {

			while (ptr2 != null) {
				tempCoeff = ptr1.term.coeff * ptr2.term.coeff;
				tempdegr = ptr1.term.degree + ptr2.term.degree;

				if (ptr3 == null && emptyNode == true) {
					poly3 = new Node(tempCoeff, tempdegr, null);
					ptr3 = poly3;
					emptyNode = false;
				} // end if
				else {
					ptr3.next = new Node(tempCoeff, tempdegr,null);
					ptr3= ptr3.next;
				}
				ptr2 = ptr2.next;
			} // end inner while loop
			
			fnlpoly = add(fnlpoly,poly3);
			
			poly3 = null;
			ptr3 = poly3;
			ptr2 = poly2;
			ptr1 = ptr1.next;
			emptyNode = true;
		} // end outer while loop

		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		return fnlpoly;
	}

	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x    Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {

		if (poly == null) {
			return 0;
		} // base case if poly == null

		return evaluate(poly.next, x) + (poly.term.coeff * (float) (Math.pow(x, poly.term.degree)));

	}

	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		}

		String retval = poly.term.toString();
		for (Node current = poly.next; current != null; current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}
}
