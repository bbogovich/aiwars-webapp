package org.brbonline.aiwars;

public class MathUtils {
	public static boolean fuzzyEquals(double a, double b, double tolerance){
		boolean result=false;
		if( a==b || //handle POSITIVE_INFINITY, NEGATIVE_INFINITY
				(tolerance==Double.POSITIVE_INFINITY&&a!=Double.NaN&&b!=Double.NaN )){
			result=true; 
		} else if(!(
					a==Double.NEGATIVE_INFINITY||b==Double.NEGATIVE_INFINITY || //an infinite number and a non infinite number cannot by definition be equal
					a==Double.POSITIVE_INFINITY||b==Double.POSITIVE_INFINITY ||
					a==Double.NaN||b==Double.NaN
				)) {
			double delta = Math.abs(a-b);
			if(a*b==0){ //if either value is zero, the delta is unreliable
				result = delta < tolerance*tolerance;
			}else {
				result = delta / (Math.abs(a)+Math.abs(b)) < tolerance;
			}
		}
		return result;
	}
}
