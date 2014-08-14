package util;

public class Angles {
	
	public  float toRotmgAngle(float angle) {
		return (float) ((angle - 90) / (360d / Math.PI / 2));
	}

	public  float toNormalAngle(float angle) {
		return (float) (angle * (360d / Math.PI / 2)) + 90;
	}
}
