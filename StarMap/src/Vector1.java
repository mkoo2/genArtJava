
public class Vector1 {
	public float x;
	public float y;

	public Vector1(float xx, float yy) {
		x = xx;
		y = yy;
	}

	/**
	 * add the last Vect1 to the current Vect
	 *
	 * @param last
	 * @return
	 */
	public Vector1 addTo(Vector1 last) {
		float xx = last.x + this.x;
		float yy = last.y + this.y;
		this.x = xx;
		this.y = yy;
		return new Vector1(xx, yy);
	}
} // Vect1