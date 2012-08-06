package test;

import junit.framework.TestCase;
import junit.framework.Assert;
import org.brbonline.aiwars.MathUtils;

public class TestFuzzyEquals extends TestCase {

	public TestFuzzyEquals(String name) {
		super(name);
		double tolerance = 0.0001;
		Assert.assertTrue(MathUtils.fuzzyEquals(0, 0, 0.0001));
		Assert.assertTrue(MathUtils.fuzzyEquals(Double.MAX_VALUE, Double.MAX_VALUE, 0.0001));
		Assert.assertTrue(MathUtils.fuzzyEquals(Double.MIN_VALUE, Double.MIN_VALUE, 0.0001));
		Assert.assertTrue(MathUtils.fuzzyEquals(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 0.0001));
		Assert.assertTrue(MathUtils.fuzzyEquals(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 0.0001));
		Assert.assertTrue(MathUtils.fuzzyEquals(0.00001, 0.00002, 0.0001));
	}

}
