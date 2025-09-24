import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Driver {
	public static void main(String [] args) {

		// test: constructor and saveToFile
		// x^2 + 2x + 1
		double[] c1 = {1,2,1};
		int[] c2 = {2,1,0};
		Polynomial p1 = new Polynomial(c1, c2);
		p1.saveToFile("test1.txt");

		// test: addition and multiplication
		// x + 3x^3 + 5x^5
		double[] c3 = {1,3,5};
		int[] c4 = {1,3,5};
		Polynomial p2 = new Polynomial(c3, c4);
		Polynomial sum = p1.add(p2);
		Polynomial product = p1.multiply(p2);
		sum.saveToFile("sum.txt"); 			// expected: x2+3x1+1+3x3+5x5
		product.saveToFile("product.txt");	// expected: x+2x2+4x3+6x4+8x5+10x6+5x7

		// test: evaluate and hasRoot
		System.out.println("p1(2) = " + p1.evaluate(2));
		if(p1.hasRoot(-1))
			System.out.println("-1 is a root of p1");
		else
			System.out.println("-1 is not a root of p1");
	}
}