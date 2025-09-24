import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Polynomial {
	double[] coefficient;
	int[] exponent;
	
	public Polynomial() {
		coefficient = new double[]{0};
		exponent = new int[]{0};
	}

	// this constructor takes the same argument as in lab1, e.g. [1,2,0,3]
	public Polynomial(double[] c) {
		coefficient = new double[c.length];
		int index = 0;
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 0)
				continue;
			else
				index++;
			coefficient[index] = c[i];
			exponent[index] = i;
		}
	}

	// this constructor takes two arguments
	public Polynomial(double[] c, int[] e) {
		coefficient = c;
		exponent = e;
	}

	public Polynomial(File f) {
		try (BufferedReader input = new BufferedReader(new FileReader(f))) {
			String poly = input.readLine();
			String[] splitPoly = poly.split("(?=[+-])");

			double[] coefficient = new double[splitPoly.length];
			int[] exponent = new int[splitPoly.length];

			for (int i = 0; i < splitPoly.length; i++) {
				String element = splitPoly[i];
				String[] splitElement = element.split("x");
				if (splitElement.length == 1) {
					coefficient[i] = Double.parseDouble(splitElement[0]);
					exponent[i] = 0;
				} else {
					coefficient[i] = Double.parseDouble(splitElement[0]);
					exponent[i] = Integer.parseInt(splitElement[1]);
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + f.getAbsolutePath());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error reading or closing file: " + f.getAbsolutePath());
			e.printStackTrace();
		}
	}

	Polynomial add(Polynomial poly) {
		// temp: temporary arrays of size maxLength to store the sum
		int maxLength = coefficient.length + poly.coefficient.length;
		double[] tempCoef = new double[maxLength];
		int[] tempExpo = new int[maxLength];
		
		// copy original array to temp
		System.arraycopy(coefficient, 0, tempCoef, 0, coefficient.length);
		System.arraycopy(exponent, 0, tempExpo, 0, exponent.length);
		int nonZeroLength = coefficient.length;

		// add new array to temp
		outer:
		for (int i = 0; i < poly.coefficient.length; i++) {
			for (int j = 0; j < nonZeroLength; j++) {
				if (tempExpo[j] == poly.exponent[i]) {
					tempCoef[j] += poly.coefficient[i];
					continue outer;
				}
			}
			tempCoef[nonZeroLength] = poly.coefficient[i];
			tempExpo[nonZeroLength] = poly.exponent[i];
			nonZeroLength++;
		}

		double[] sumCoef = new double[nonZeroLength];
		int[] sumExpo = new int[nonZeroLength];
		System.arraycopy(tempCoef, 0, sumCoef, 0, nonZeroLength);
		System.arraycopy(tempExpo, 0, sumExpo, 0, nonZeroLength);

		return(new Polynomial(sumCoef, sumExpo));
	}

	Polynomial multiply(Polynomial poly) {
		// find maxLength
		int maxA = exponent[0];
		int maxB = poly.exponent[0];
		for (int i = 0; i < exponent.length; i++) {
			if (exponent[i] > maxA)
				maxA = exponent[i];
		}
		for (int i = 0; i < poly.exponent.length; i++) {
			if (poly.exponent[i] > maxB)
				maxB = poly.exponent[i];
		}
		int maxLength = maxA + maxB;

		// temp: temporary arrays of size maxLength to store the product
		double[] tempCoef = new double[maxLength + 1];
		int[] tempExpo = new int[maxLength + 1];

		for (int i = 0; i <= maxLength; i++) {
			tempExpo[i] = i;
		}
		for (int i = 0; i < exponent.length; i++) {
			for (int j = 0; j < poly.exponent.length; j++) {
				int newExpo = exponent[i] + poly.exponent[j];
				tempCoef[newExpo] += coefficient[i] * poly.coefficient[j];
			}
		}

		// filter non-zero term
		int numNonZero = 0;
		for (int i = 0; i <= maxLength; i++) {
			if (tempCoef[i] != 0)
				numNonZero++;
		}
		double[] productCoef = new double[numNonZero];
		int[] productExpo = new int[numNonZero];
		int index = 0;
		for (int i = 0; i <= maxLength; i++) {
			if (tempCoef[i] != 0) {
				productCoef[index] = tempCoef[i];
				productExpo[index] = tempExpo[i];
				index++;
			}
		}
		return (new Polynomial(productCoef, productExpo));
	}

	double evaluate(double x) {
		double value = 0;
		for (int i = 0; i < coefficient.length; i++) {
			value += coefficient[i] * Math.pow(x, exponent[i]);
		}
		return value;
	}

	boolean hasRoot(double x) {
		return (Math.abs(evaluate(x)) <= 1e-10);
	}

	void saveToFile(String s) {
		try (PrintStream output = new PrintStream(s)) {
			for (int i = 0; i < coefficient.length; i++) {
				if (i > 0 && coefficient[i] > 0)
					output.append("+");
				String coefStr;
				if (coefficient[i] % 1 == 0)
					coefStr = Integer.toString((int) coefficient[i]); // clean integer coefficient
				else
					coefStr = Double.toString(coefficient[i]);
				if (exponent[i] == 0) {
					output.append(coefStr);
				} else {
					output.append(coefStr);
					output.append("x");
					output.append(Integer.toString(exponent[i]));
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("File not found or cannot be created: " + s);
			e.printStackTrace();
		}
	}
}