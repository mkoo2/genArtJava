
/**
 * class Vect1 use to keep track of the xy points. used instead of createVector
 * in javascript
 */
public class Vector1 {
	public float x;
	public float y;

	public Vector1(float xx, float yy) {
		x = xx;
		y = yy;
	}

	/**
	 * add the vect32 Vect1 to the current Vector1
	 *
	 * @param vect32
	 * @return
	 */
	public Vector1 add(Vector1 vect32) {
		float xx = vect32.x + x;
		float yy = vect32.y + y;
		return new Vector1(xx, yy);
	}
} // Vect1
