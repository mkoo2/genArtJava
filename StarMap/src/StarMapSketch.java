import java.awt.Color;
import java.awt.Font;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.data.Table;

/**
 * star map
 */
public class StarMapSketch extends PApplet {

	static final int winWidth = 1200;
	static final int winHeight = 1200; // 600 - for laptop only

	// For the #WCCChallenge, theme: "connecting the dots."
	// This sketch maps the 9000+ stars logged in the Yale Bright Star Catalog.
	// These data are conveniently available in spreadsheet format here:
	// https://docs.google.com/spreadsheets/d/1zVSoeZ8gV-v5v9OSsMm02E6QPcNxc-XvGrGT6YhyOtI/edit#gid=1426262721
	// About half of the stars are in the Northern Hemisphere, and half are in the
	// Southern Hemisphere.
	// Zodiac constellations (plus ursas major and minor) can be color-coded for
	// enhanced visibility.
	// With color coding off, star colors are based on spectral data from the Bright
	// Star Catalog.
	// Connecting the dots of each constellation is DIY (as it is in real life)!

//	let cabbrevs = ['ARI', 'TAU', 'GEM', 'CAN', 'LEO', 'VIR', 'LIB', 'SCO', 'CAP', 'AQR', 'PSC', 'SGR', 'UMI', 'UMA'];
//	let cnames = ['Aries', 'Taurus', 'Gemini', 'Cancer', 'Leo', 'Virgo', 'Libra', 'Scorpio', 'Capricorn', 'Aquarius', 'Pisces', 'Saggitarius', 'Ursa Minor', 'Ursa Major'];
//	let colors = ['rgb(168,244,155)', 'rgb(235,26,3)', 'rgb(239,147,109)', 'rgb(254,199,7)', 'rgb(244,239,114)', 'rgb(7,17,248)', 'rgb(48,239,4)', 'rgb(5,212,218)', 'rgb(117,244,238)', 'rgb(165,159,252)', 'rgb(238,8,246)', 'rgb(235,188,237)', 'rgb(104,239,228)', 'rgb(40,229,1)'];
//	let spectypes = [
//	             	'#cad7ff',
//	             	'#aabfff',
//	             	'#f8f7ff',
//	             	'#fff4ea',
//	             	'#ffd2a1',
//	             	'#ffcc6f',
//	             	'#9bb0ff',
//	             	'#ffffff'
//	             ];

	String[] spectypes = { "#cad7ff", "#aabfff", "#f8f7ff", "#fff4ea", "#ffd2a1", "#ffcc6f", "#9bb0ff", "#ffffff" };

	String[] cabbrevs = { "ARI", "TAU", "GEM", "CAN", "LEO", "VIR", "LIB", "SCO", "CAP", "AQR", "PSC", "SGR", "UMI",
			"UMA" };
	String[] cnames = { "Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo", "Libra", "Scorpio", "Capricorn",
			"Aquarius", "Pisces", "Saggitarius", "Ursa Minor", "Ursa Major" };
	Color[] colors = { new Color(168, 244, 155), new Color(235, 26, 3), new Color(239, 147, 109),
			new Color(254, 199, 7), new Color(244, 239, 114), new Color(7, 17, 248), new Color(48, 239, 4),
			new Color(5, 212, 218), new Color(117, 244, 238), new Color(165, 159, 252), new Color(238, 8, 246),
			new Color(235, 188, 237), new Color(104, 239, 228), new Color(40, 229, 1) };
	float zoom = (float) 0.25;
	float ztarget = 1;
	float orient = 0;
	float torient = 0;
	float hemi2 = 1;
	float time = 0;
	Vector1 center, tcenter;
	float oheight, owidth;

	// add global variables explicitly; implicit in javascript
	// sky
	PGraphics cg0;
	// stars
	PGraphics cg1;
	// axis
	PGraphics cg2;
	PGraphics cg3;

	// if contain "ALL" all constellations are selected
	private String selectedConstellation = "";

	private boolean colorConstellations = true;
	
	private boolean autoRotation = false;
	private boolean showAxis = true;
	private boolean northHemisphere = true;
	private boolean alignStars = false;

	Table stars;

	/**
	 * preLoad need to be called manually in java. automatic in js
	 */
	public void preload() {
		stars = loadTable("BSC.csv", "header");
	}

	@Override
	public void settings() {
		size(winWidth, winHeight);

//		createCanvas(winWidth, winHeight);

	}

	public void setup() {
		preload();

		cg0 = createGraphics((int) (2.25 * height), (int) (2.25 * height));
		cg1 = createGraphics((int) (2.25 * height), (int) (2.25 * height));
		cg2 = createGraphics((int) (2.25 * height), (int) (2.25 * height));
		cg3 = createGraphics((int) (2.25 * height), (int) (2.25 * height));

		cg0.beginDraw();
		cg1.beginDraw();
		cg2.beginDraw();
		cg3.beginDraw();

		oheight = winHeight;
		owidth = winWidth;
		imageMode(CENTER);

		makeSky();

		// moved to draw method
//		makeLabels();
		
		makeAxes();
		makeStars();
		if (hemi2 == 1) {
			windowTitle("NORTHERN HEMISPHERE");
		}

		cg0.endDraw();
		cg1.endDraw();
		cg2.endDraw();
		cg3.endDraw();

//		center = createVector(0, 0);
//		tcenter = createVector(0, 0);

		center = new Vector1(0, 0);
		tcenter = new Vector1(0, 0);

		orient = PI;
		torient = PI;
	}

	public void draw() {
		background(0);
		
		// java does not have lerp on vectors
		// center.lerp(tcenter, 0.1);
		float xx = lerp(center.x, tcenter.x, (float) 0.1);
		float yy = lerp(center.y, tcenter.y, (float) 0.1);
		center = new Vector1(xx, yy);
		translate(width / 2, height / 2);
		scale(zoom);

		translate(center.x, center.y);

		makeLabels(center);
		
		
		if (frameCount > 30) {
			zoom = lerp(zoom, ztarget, (float) 0.05);
		}

		// java does not have javascript mousePressed, movedX, movedY
		if (this.mousePressed) { // mouseIsPressed) {
			tcenter.x += zoom * (mouseX - dmouseX); // movedX;
			tcenter.y += zoom * (mouseY - dmouseY); // movedY;
		} else {
			if (isAutoRotation()) {
				torient += (Math.signum(center.y - (mouseY - height / 2)) * (mouseX - dmouseX) / 200)
						- (Math.signum(center.x - (mouseX - width / 2)) * (mouseY - dmouseY) / 200);
			}
		}

		orient = lerp(orient, torient, (float) 0.1);
		rotate(orient + time);

		if (this.isAutoRotation()) {
			time -= hemi2 / 2000;
		}
		
		image(cg0, 0, 0);
		image(cg1, 0, 0);

		if (this.isShowAxis()) {
			image(cg2, 0, 0);
		}
		image(cg3, 0, 0);
		
	}

	public void makeStars() {
		cg1.clear();
		cg1.push();
		cg1.translate(cg1.width / 2, cg1.height / 2);
		cg1.stroke(255);
		int count = 0;
//		println("hemi34:" + hemi2);
		for (int r = stars.getRowCount() - 1; r >= 0; r--) {
			cg1.push();
			// need to convert from integer calc to float calc to not align to grid
			float aval = 0;
			if (isAlignStars()) {
				int srad = Integer.parseInt(stars.getString(r, 2)); // RA hours
				int sram = Integer.parseInt(stars.getString(r, 3)); // RA minutes
				int sras = Integer.parseInt(stars.getString(r, 4)); // RA seconds
				aval = (srad + (sram / 60) + (sras / 3600)); // Right ascension
			} else {
				float srad = Integer.parseInt(stars.getString(r, 2)); // RA hours
				float sram = Integer.parseInt(stars.getString(r, 3)); // RA minutes
				float sras = Integer.parseInt(stars.getString(r, 4)); // RA seconds
				aval = (srad + (sram / 60) + (sras / 3600)); // Right ascension
			}

			float ang = map(aval, 0, 24, 0, TAU);
			float decd = Integer.parseInt(stars.getString(r, 5)); // DEC degrees
			float decm = Integer.parseInt(stars.getString(r, 6)); // DEC minutes
			float decs = Integer.parseInt(stars.getString(r, 7)); // DEC seconds
			float dval = (decd + (decm / 60) + (decs / 3600)); // Declination
			float rad = map(hemi2 * dval, 0, 90, oheight, 0);
			float vmag = Float.parseFloat(stars.getString(r, 10)); // Brightness value
			int spec = Integer.parseInt(stars.getString(r, 13)); // Spectral color code (M, K, G, F, A, K, O)
			Color speccol2 = Color.decode(spectypes[spec]);
			
//			int speccol = color(spectypes[spec]);
			Color speccol3 = speccol2;
			
//			speccol.setAlpha(random(150, 220));
			float radius = map(vmag, (float) -1.47, 5, 8, 2); // Scale
			if (hemi2 * decd > 0) {
				String name2 = stars.getString(r, 1).toUpperCase();
				String name = "";
				if (name2.length() > 0) {
					name = name2.substring(name2.length() - 3); // Constellation code is last three characters
//					println("name:'" +name + "' " +colors[0]);
					if (name.equalsIgnoreCase("UMI")) {
//						println("name:'" +name + "' " +colors[0]);
					}
				}

				if (name.equalsIgnoreCase("UMI")) {
//					println(name + ":" + aval + ", " + ang + ", " + rad + "," + decd);
					cg1.rotate(hemi2 * ang);
					cg1.translate(0, rad);
					cg1.noStroke();
				} else {
					cg1.rotate(hemi2 * ang);
					cg1.translate(0, rad);
					cg1.noStroke();
				}

				if (radius < 2) {
					if (this.isColorConstellations())
						cg1.fill(255, random(150, 220));
					else {
						cg1.fill(speccol3.getRGB());
					}
					cg1.ellipse(0, 0, radius * oheight / 566, radius * oheight / 566);
				} else {
					colorTheConstellation(name, speccol3);
					starburst(0, 0, radius * oheight / 566);
					cg1.noStroke();
					cg1.ellipse(0, 0, radius * oheight / 566, radius * oheight / 566);
				}
			} // if hemi
			cg1.pop();
		} // for
		cg1.pop();
	}

	/**
	 * color the constellations
	 * 
	 * @param speccol3
	 * @param constellName
	 */
	public void colorTheConstellation(String constellName, Color speccol3) {
		// println("name:" +name + " " );
		// javascript == does not translate to java for string
		cg1.fill(255, 220);

		String constell = "";
		if (selectedConstellation != "") {
			constell = selectedConstellation;
			if (selectedConstellation == "ALL") {
				constell = "";
			}
		}

		updateConstellationIndex(constellName, 1, constell);
		updateConstellationIndex(constellName, 2, constell);
		updateConstellationIndex(constellName, 3, constell);
		updateConstellationIndex(constellName, 4, constell);
		updateConstellationIndex(constellName, 5, constell);
		updateConstellationIndex(constellName, 6, constell);
		updateConstellationIndex(constellName, 7, constell);
		updateConstellationIndex(constellName, 8, constell);
		updateConstellationIndex(constellName, 9, constell);
		updateConstellationIndex(constellName, 10, constell);
		updateConstellationIndex(constellName, 11, constell);
		updateConstellationIndex(constellName, 12, constell);
		updateConstellationIndex(constellName, 13, constell);

//		if (name.equalsIgnoreCase(cabbrevs[0])) {
//			cg1.fill(colors[0].getRGB());
//		} else if (name.equalsIgnoreCase(cabbrevs[1])) {
//			cg1.fill(colors[1].getRGB());
//		} else if (name.equalsIgnoreCase(cabbrevs[2])) {
//			cg1.fill(colors[2].getRGB());
//		} else if (name.equalsIgnoreCase(cabbrevs[3])) {
//			cg1.fill(colors[3].getRGB());
//		} else if (name.equalsIgnoreCase(cabbrevs[4])) {
//			cg1.fill(colors[4].getRGB());
//		} else if (name.equalsIgnoreCase(cabbrevs[5])) {
//			cg1.fill(colors[5].getRGB());
//		} else if (name.equalsIgnoreCase(cabbrevs[6])) {
//			cg1.fill(colors[6].getRGB());
//		} else if (name.equalsIgnoreCase(cabbrevs[7])) {
//			cg1.fill(colors[7].getRGB());
//		} else if (name.equalsIgnoreCase(cabbrevs[8])) {
//			cg1.fill(colors[8].getRGB());
//		} else if (name.equalsIgnoreCase(cabbrevs[9])) {
//			cg1.fill(colors[9].getRGB());
//		} else if (name.equalsIgnoreCase(cabbrevs[10])) {
//			cg1.fill(colors[10].getRGB());
//		} else if (name.equalsIgnoreCase(cabbrevs[11])) {
//			cg1.fill(colors[11].getRGB());
//		} else if (name.equalsIgnoreCase(cabbrevs[12])) { // Ursa Minor
//			cg1.fill(colors[12].getRGB());
//		} else if (name.equalsIgnoreCase(cabbrevs[13])) {
//			cg1.fill(colors[13].getRGB());
//		} else {
//			cg1.fill(255, 220);
//		}

		if ( this.isColorConstellations() == false) {
			cg1.fill(speccol3.getRGB());
		}
	}

	private void updateConstellationIndex(String name, int index, String constellb) {

		String constellation = cabbrevs[index];
		if ((name.equalsIgnoreCase(constellation) && constellb.equalsIgnoreCase(""))
				|| (name.equalsIgnoreCase(constellation) && constellb.equalsIgnoreCase(name))) {
			cg1.fill(colors[index].getRGB());
		}
	}

	/**
	 * figure out how to put labels in the upper right hand corner. this method is
	 * called in the draw function.
	 */
	public void makeLabels(Vector1 center) {

		cg3.beginDraw();
		cg3.clear();
		int initialX = 700;
		int initialY = 700;
//		int initialX = 1900;
//		int initialY = 700;
		float ydiv = 14 * height / 560;
		for (int i = 0; i < cnames.length; i++) {
			float y = initialY + (2 * ydiv + i * ydiv);
			String constell = cabbrevs[i];
			String constellb = this.selectedConstellation;
			if ((this.isColorConstellations() && constellb == "ALL")
					|| (this.isColorConstellations() && constellb == "")
					|| (this.isColorConstellations() && constell == constellb)) {
				cg3.fill(colors[i].getRGB());
			} else {
				cg3.fill(new Color(229, 227, 227).getRGB());
			}
			cg3.textSize(30);
			cg3.text(cnames[i], initialX, y);
		}
		cg3.endDraw();
	}

	/**
	 * put the hemisphere in the window title
	 */
	public void switchHemisphere() {
		hemi2 = -hemi2;
		if (hemi2 == 1)
			windowTitle("NORTHERN HEMISPHERE");
		else
			windowTitle("SOUTHERN HEMISPHERE");
		cg1.beginDraw();
		cg1.clear();
		makeAxes();
		makeStars();
		cg1.endDraw();
	}

	public void recenterMap() {
		tcenter.x = 0;
		tcenter.y = 0;
		ztarget = 1;
	}

	public void zoomIn() {
		ztarget += 0.1;
	}

	public void zoomOut() {
		ztarget = ztarget - (float) 0.1;
	}

	public void recolorMap() {
		cg1.beginDraw();
		cg1.clear();
		makeAxes();
		makeStars();
		cg1.endDraw();
	}

	public void makeAxes() {
		cg2.clear();
		cg2.push();
		cg2.translate(cg2.width / 2, cg2.height / 2);
		for (int i = 0; i < 10; i++) {
			cg2.noFill();
			cg2.stroke(255);
			cg2.strokeWeight((float) 0.2 * oheight / 566);
			cg2.ellipse(0, 0, 2 * i * oheight / 9, 2 * i * oheight / 9);
			cg2.textAlign(CENTER, BOTTOM);
			cg2.textFont(new PFont(new Font("monospace", 0, 12), true));
			cg2.textSize(12 * oheight / 566);
			cg2.fill(255, 150);
			cg2.noStroke();
			if (i > 0 && i < 9)
				cg2.text((hemi2 * i <= 0 ? "" : "+") + (90 - 10 * i) * hemi2 + '°', 0, -i * oheight / 9);
			if (i > 0 && i < 9) {
				cg2.rotate(PI);
				cg2.text((hemi2 * i <= 0 ? "" : "+") + (90 - 10 * i) * hemi2 + '°', 0, -i * oheight / 9);
				cg2.rotate(-PI);
			}
			cg2.stroke(255);

		}
		for (int i = 0; i < 24; i++) {
			cg2.push();
			cg2.rotate(HALF_PI + hemi2 * (i * TAU / 24));
			cg2.line(oheight / 9, 0, oheight, 0);
			cg2.translate(oheight, 0);
			cg2.rotate(HALF_PI);
			cg2.textAlign(CENTER, BOTTOM);
			cg2.textFont(new PFont(new Font("monospace", 0, 12), true));
			cg2.textSize(12 * oheight / 566);
			cg2.fill(255);
			cg2.noStroke();
			String intStr = Integer.valueOf(i).toString();
			String intStr2 = padStartStr(intStr, '0');
			cg2.text(intStr2 + 'h', 0, 0);
			cg2.pop();
		}
		cg2.pop();
	}

	/**
	 * code to pad a string.
	 * 
	 * @param inputString
	 * @param padChar
	 * @return
	 */
	public String padStartStr(String inputString, char padChar) {
		return String.format("%1$" + inputString.length() + "s", inputString).replace(' ', padChar);
	}

	public void starburst(int x, int y, float rad) { // , int col
		cg1.push();
		cg1.translate(x, y);
		cg1.scale(rad / 8);
		for (int i = 0; i < 50; i++) {
			cg1.push();
			cg1.rotate(random(TAU));
			cg1.stroke(random(200, 255), rad * 20);
			cg1.strokeWeight((float) 0.3);
			cg1.line(random((float) (-2.5 * rad), -rad), (float) 0, random(rad, (float) (2.5 * rad)), 0);
			cg1.pop();
		}
		cg1.pop();
	}

	public void makeSky() {
		int start = color(3, 18, 40);
		int end = color(0, 0, 0);
		cg0.clear();
		cg0.translate(cg0.width / 2, cg0.height / 2);
		for (int r = 2 * height; r > 0; r -= 3) {
			float a = map(r, 2 * height, 0, HALF_PI, 0);
			int col = lerpColor(start, end, cos(a));
			cg0.stroke(col);
			cg0.strokeWeight(3);
			cg0.ellipse(0, 0, r, r);
		}
	}

	public void keyPressed() {
		// ARROW KEYS zoom and rotate
		if (keyCode == UP) {
			zoomIn();
		}
		if (keyCode == DOWN) {
			zoomOut();
		}
		if (keyCode == RIGHT)
			torient += 0.1;
		if (keyCode == LEFT)
			torient -= 0.1;
//		//WASD KEYS move center of map up, down, left, and right
		if (keyCode == 87)
			tcenter.y += 20;
		if (keyCode == 83)
			tcenter.y -= 20;
		if (keyCode == 65)
			tcenter.x += 20;
		if (keyCode == 68)
			tcenter.x -= 20;
		if (keyCode == 32)
			recenterMap(); // SPACE recenters
		if (keyCode == 13 || keyCode == 10) {
			if (this.isAutoRotation()) {// ENTER toggles autorotation
				this.setAutoRotation(false);
			} else {
				this.setAutoRotation(true);
			}
		}
		if (keyCode == 16) {// SHIFT toggles axes
			if (this.isShowAxis()) {
				this.setShowAxis(false);
			} else {
				this.setShowAxis(true);
			}

		}
		if (keyCode == 67) { // C toggles star coloring
			if (this.isColorConstellations()) {
				this.setColorConstellations(false);
			} else {
				this.setColorConstellations(true);
			}

//			ccbox.checked(!ccbox.checked());
			recolorMap();
		}
//		if (keyCode == 70) goFull(); //F toggles fullscreen view
	}

	public void run() {
		String[] processingArgs = { "StarMapSketch" };
		PApplet.runSketch(processingArgs, this);
	}

	boolean isAutoRotation() {
		return autoRotation;
	}

	void setAutoRotation(boolean isAutoRotation) {
		this.autoRotation = isAutoRotation;
	}

	boolean isColorConstellations() {
		return colorConstellations;
	}

	void setColorConstellations(boolean isColorConstellation) {
		this.colorConstellations = isColorConstellation;
	}

	public boolean isShowAxis() {
		return showAxis;
	}

	public void setShowAxis(boolean isShowAxis) {
		this.showAxis = isShowAxis;
	}

	public boolean isNorthHemisphere() {
		return northHemisphere;
	}

	public void setNorthHemisphere(boolean northHemisphere) {
		this.northHemisphere = northHemisphere;
	}

	public boolean isAlignStars() {
		return alignStars;
	}

	public void setAlignStars(boolean alignStars) {
		this.alignStars = alignStars;
	}

	public String getSelectedConstellation() {
		return selectedConstellation;
	}

	public void setSelectedConstellation(String currentConstell) {
		this.selectedConstellation = currentConstell;
	}

}
