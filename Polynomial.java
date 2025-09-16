public class Polynomial {
	double [] coefficient;
	
	public Polynomial() {
		double[] coef = new double[1];
		coef[0] = 0;
		coefficient = coef;
	}
	public Polynomial(double [] c) {
		coefficient = c;
	}
	
	Polynomial add(Polynomial poly) {
        	int maxLength = Math.max(coefficient.length, poly.coefficient.length);
        	double[] sum = new double[maxLength];
        	for (int i = 0; i < maxLength; i++) {
            		double coefA = (i < coefficient.length) ? coefficient[i] : 0;
            		double coefB = (i < poly.coefficient.length) ? poly.coefficient[i] : 0;
            		sum[i] = coefA + coefB;
        	}
		Polynomial sumPoly = new Polynomial(sum);
        	return sumPoly;
	}

	double evaluate(double x) {
		double value = 0;
		for (int i = 0; i < coefficient.length; i++) {
			value += coefficient[i] * Math.pow(x, i);
		}
		return value;
	}

	boolean hasRoot(double x) {
		return (evaluate(x) == 0);
	}
}