import java.awt.Color;

import processing.core.PApplet;

/**
 * 
 * Vancouver, Canada
 * 
 * Everyone says it always rains here....
 * 
 * @cartocopia 2023-04-04
 * 
 *             #WCCChallenge - Where I Live
 * 
 *             Left click regenerates the scene Right Click saves the image
 * 
 *             image colour palette is (mostly) from historic paint colours used
 *             on Vancouver houses....
 * 
 *             Colour palettes from
 *             https://www.vancouverheritagefoundation.org/wp-content/uploads/2022/09/
 *             VHF001_Brochure_v9_Final.pdf The True Colours paint palette for
 *             Western Canada© is the product of more than 30 years of research
 *             by Vancouver Heritage Foundation.
 * 
 */
public class VancouverApp extends PApplet {

	// drawing name: 220811

	static final int winWidth = 1400;
	static final int winHeight = 1000;

	// ------------------------------------------
	// Set rain to false to have a nice day!
	// ------------------------------------------
	boolean rain = true;

	int start; // timer for rain
	int fc; // my framecounter to allow restarts
	int ar = 1; // aspect ratio
	int MAXINT = 65535; // convenience
	// building colours
//	String[] bc = ['#3d3b33', '#c08755', '#b19b83', '#6d1f12', '#4d5b67', '#8f7157', '#9d9e9b', '#f6d292', '#727d87', '#0b0002'];
	String[] bc = { "#3d3b33", "#c08755", "#b19b83", "#6d1f12", "#4d5b67", "#8f7157", "#9d9e9b", "#f6d292", "#727d87",
			"#0b0002" };

	float[] dnoff = { 0, 0 }; // used for difference noise offset in clouds

	public static void main(String[] args) {
		String[] processingArgs = { "VancouverApp" };
		PApplet mySketch = new VancouverApp();
		PApplet.runSketch(processingArgs, mySketch);
	}

	public void setup() {
//		let canvas = createCanvas(1112, 834);
//		canvas.elt.addEventListener("contextmenu", (e) => e.preventDefault()); // disable right click context menu
		noiseDetail(6, 0.5f); // terrain noise, noiseDetail(6, 0.5) works well
		ar = width / height;
		smooth();
		strokeJoin(ROUND);
		startmeup();
	}

	public void startmeup() {
		start = millis(); // restart rain timer
		fc = 0; // resetr framecounter
		background(255);
		noiseDetail(6, 0.5f); // terrain noise, noiseDetail(6, 0.5) works well for mountains
		dnoff[0] = random(99);
		dnoff[1] = random(99);

	}

	public void draw() {
		// coordinate system is y-up for drawing. This also makes rotations go
		// counterclockwise....
		scale(1, -1);
		translate(0, -height);
		float s;

		switch (fc) { // frame counter These are split apart because of the mountain gradients taking
						// so much time....
		case 0: // a blank canvas
			background(255);
			break;
		case 1: // paint the sky
			sky();
			break;
		case 2: // mountains
			s = random(MAXINT);
			mtn2(0.91f, 0.80f, 10, "#2c0500", s);
			mtn2(0.91f, 0.80f, 10, "#ECE8E8", (float) (s - .01));
			mtn2(0.88f, 0.81f, 10, "#2c0500", s);
			break;
		case 3: // more mountains
			s = random(MAXINT);
			mtn2((float) 0.85, (float) 0.75f, 6, "#440a1f", s);
			mtn2((float) 0.85f, (float) 0.75f, 6, "#CCBCC2", (float) (s - .01));
			mtn2((float) 0.83f, (float) 0.76f, 6, "#440a1f", s);
			break;
		case 4: // even more mountains
			s = random(MAXINT);
			mtn2((float) 0.80f, (float) 0.72f, 4, "#D3C0BF", (float) (s - 0.02f));
			mtn2((float) 0.80f, (float) 0.72f, 4, "#5f1815", s);
			break;
		case 5: // small mountains
			mtn2((float) 0.71f, (float) 0.68f, 2, "#67594e", random(MAXINT));
			break;
		case 6: // tree covered mountains
			tree_mtn(0.68f, 0.62f, 5, "#98979e", random(MAXINT), 5);
			tree_mtn(0.62f, 0.55f, 3, "#94a182", random(MAXINT), 10);
			tree_mtn(0.58f, 0.5f, 2, "#7a8c81", random(MAXINT), 20);
			tree_mtn(0.5f, 0.4f, 5, "#425243", random(MAXINT), 30);
			break;
		case 7: // city
			int count = floor(width / 30); // number of building
			float[] bx = new float[count];
			for (int i = 0; i < count; i++) { // evenly spaced with randomness
				// bx.push(i * (width / count) + random(-20, 20));
				bx[i] = (float) (i * (width / count) + random(-20, 20));
			}
			// TODO
//			shuffle(bx, true); //shuffle order
			for (int i = 0; i < count; i++) {
				float x = bx[i];
				float h = random(80, 80 + sin(PI * x / width) * 450); // higher in the middle
				float w = map(h, 80, 530, 120, 40) * random((float) 0.8, (float) 1.2); // the higher, the skinnier
				building(x, height * 0.15f, w, h, bc[floor(random(bc.length))]); // draw the building
			}
			break;
		case 8: // landmarks. No representations are made to geographical accuracy
			noStroke();
			fill(Color.decode("#425243").getRGB());
			rect(0, 0, width, (height * 0.15f)); // some green by the water
			harbour_centre(width * 0.75f + random(-width / 20, width / 20), height * 0.15f);
			bc_place(width * 0.45f + random(-width / 20, width / 20), height * 0.14f);
			science_world(width * 0.2f + random(-width / 20, width / 20), height * 0.14f);
			canada_place(width * 0.75f + random(-width / 20, width / 20), height * 0.14f);
			break;
		case 9: // waterfront walk and water
			noStroke();
			fill(Color.decode("#4d5b97").getRGB());
			rect(0, 0, width, (float) (height * 0.13));
			stroke(Color.decode("#0b0002").getRGB());
			strokeWeight(2);
			line(0, (float) (height * 0.14), width, (float) (height * 0.14));
			line(0, (float) (height * 0.135), width, (float) (height * 0.135));
			float pcount = width / 10;
			for (int i = 0; i < pcount; i++) {
				line(i * 10 + random(-2, 2), (float) (height * 0.132), i * 10 + random(-1, 1),
						(float) (height * 0.141));
			}
			break;
		default:
			break;
		}

		// rain
		if (rain) {

			// subtly animated clouds
			if ((millis() - start > 3000) && (millis() - start < 6500)) { // then the clouds
				noiseDetail(8, 0.85f);
				float noiseScale = 0.0001f; // 0.0001f
				noStroke();
				for (int i = 0; i < 2000; i++) {
					float rx = random(width);
					float ry = random(height);
					float v = noise(rx * noiseScale, ry * noiseScale * 2);
					v = abs(v - noise(rx * noiseScale + dnoff[0] - fc / 1000, ry * noiseScale + fc / 2000 + dnoff[1])); // difference
																														// noise
					float z = map(v, 0, 1, 180, 245); // 180
					float a = map(v, 0, 1, 8, 32) * sq(sq(map(ry, 0, height, 0, 1)));
					fill(z, z, z, a);
					ellipse(rx, ry, random(20, 50), random(2, 20));
				}
			}

			float rd = random(160, 230);

			if (millis() - start > 8000 && millis() - start < 14000) { // then the rain. increase rain until you
																		// barely
																		// see buildings
				strokeWeight(1);
				stroke(rd, rd, rd, map(millis() - start, 0, 80000, 0, 255)); // decrease opacity
				for (int i = 0; i < 1000; i++) {
					float rx = random(-10, width);
					float ry = (1 - pow(random(0, 1), 3)) * (height + 20); // heavier rain up higher
					line(rx, ry, rx + 5 + random(2), ry - 20);
				}
			}

		}

		fc++; // increment the framecounter
	}

	// -----------------------------
	// Nice random builing function
	//
	public void building(float _x, float _y, float _w, float _h, String _c) {
		float x = _x;
		float y = _y;
		float w = _w;
		float h = _h;

		int c = Color.decode(_c).getRGB();

		float fh = random(20, 30); // floor height
		float floors = floor(h / fh); // number of floors
		fh = h / floors; // back calculate exact floor size

		float wh = fh * random(0.5f, 1); // window height
		float windows = floor(random(3, 10)); // number of windows
		float wr = random(0.2f, 1); // window ratio
		float ww = w / windows; // window width

//		float squig = floor(abs(randomGaussian(0, (float) 0.9))) * random(2, 5); // building squiggle
		float squig = floor(abs(randomGaussian())) * random(2, 5); // building squiggle
		float cycles = random(0.5f, 5); // number of squiggle cycles

		float x_off = x - w / 2 + (ww * (1 - wr)) / 2;
		float y_off = y + (fh - wh) / 2;
		for (int j = 0; j < floors - 1; j++) {
			stroke(c);
			fill(c);
			rect(squig * sin(cycles * TWO_PI * j / floors) + x - w / 2, y + j * h / floors, w, fh); // floor
			if (brightness(c) > 50) { // windows light or dark depends on building colour
				fill(50);
				stroke(50);
			} else {
				fill(230);
				stroke(230);
			}
			if (random(1) < 0.9 || squig > 3) { // skip the occasional floor or if the building is real squiggly
				for (int i = 0; i < windows; i++) {
					strokeWeight(2);
					rect(squig * sin(cycles * TWO_PI * j / floors) + x_off + i * ww, y_off + j * h / floors, ww * wr,
							wh); // windows
				}
			} else { // replace with horizontal lines instead of windows
				strokeWeight(2);
				float l = floor(random(4));
				for (int i = 0; i < l; i++) {
					line(squig * sin(cycles * TWO_PI * j / floors) + x - w / 2, y + j * fh + (i + 1) * fh / (l + 1),
							squig * sin(cycles * TWO_PI * j / floors) + x + w / 2, y + j * fh + (i + 1) * fh / (l + 1));
				}
			}
		}
		// roof options
		strokeWeight(2);
		fill(c);
		stroke(c);
		int roof = floor(random(1, 6));
		switch (roof) {
		case 1: // slope right
			triangle(x - w / 2, y + h, x - w / 2, y + h - fh, x + w / 2, y + h - fh);
			break;
		case 2: // slope left
			triangle(x - w / 2, y + h, x + w / 2, y + h - fh, x - w / 2, y + h - fh);
			break;
		case 3: // peak
			triangle(x - w / 2, y + h - fh, x + w / 2, y + h - fh, x, y + h + fh * random((float) -0.5, (float) 0.5));
			break;
		case 4: // dome
			float ds = random((float) 0.2, (float) 0.8);
			arc(x, y + h - fh, ds * w, ds * w, 0, PI);
		default: // flat
			rect(x - w / 2, y + h - fh, w, fh * random((float) 0.1, (float) 0.5)); // flat
			break;
		}
		// roof greebles
		for (int i = 0; i < random(w / 10); i++) {
			rect(x - w / 2 + w * random(0.9f), y + h - fh, w * random(0.1f), fh * random(0.7f, 1.2f));
		}
	}

	public void canada_place(float _x, float _y) {
		float x = _x;
		float y = _y - 2;
		int w = 250;
		int h = 40;
		push();
		noStroke();
		rectMode(CENTER);
		fill(200);
		rect(x, y + h / 2, w + 20, h);
		fill(30);
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 3; j++) {
				rect(x - w / 2 + w * i / 19, y + 10 + h * j / 4, w / 24, h / 5);
			}
		}
		fill(220);
		stroke(220);
		strokeWeight(1);
		for (int i = 0; i < 5; i++) {
			triangle((float) (x - w / 2 + w * (i + 0.4) / 5), y + h, x - w / 2 + w * (i + 1) / 5, y + h,
					x - w / 2 + w / 12 + w * i / 5, (float) (y + 2.2 * h));
		}
		fill(250);
		stroke(250);
		strokeWeight(1);
		for (int i = 0; i < 5; i++) {
			triangle((float) (x - w / 2 + w * i / 5), y + h, (float) (x - w / 2 + w * (i + 0.4) / 5), y + h,
					x - w / 2 + w / 8 + w * i / 5, (float) (y + 2.5 * h));
		}
		pop();
	}

	public void science_world(float _x, float _y) {
		float x = _x;
		float y = _y - 2;
		int w = 100;
		int h = 20;
		push();
		noStroke();
		rectMode(CENTER);
		stroke(240);
		strokeWeight(2);
		fill(200);
		circle(x, (float) (y + h * 3.5), w);
		fill(240);
		rect(x, y + h, (float) (w * 0.6), h);
		fill(230);
		stroke(240);
		rect(x, y + h / 2, w, h);
		for (int i = 0; i < 8; i++) {
			line(x - w * sin(PI * i / 8) / 2, (float) (y + h * 3.5 + w * cos(PI * i / 8) / 2),
					x + w * sin(PI * i / 8) / 2, (float) (y + h * 3.5 + w * cos(PI * i / 8) / 2));
			for (int j = 0; j < 8; j++) {
				float x0 = x - w * cos(PI * j / 8) * sin(PI * i / 8) / 2;
				double y0 = y + h * 3.5 + w * cos(PI * i / 8) / 2;
				double x1 = x - w * cos(PI * (j + 1) / 8) * sin(PI * (i + 1) / 8) / 2;
				double y1 = y + h * 3.5 + w * cos(PI * (i + 1) / 8) / 2;
				line(x0, (float) y0, (float) x1, (float) y1);
				x1 = x - w * cos(PI * (j - 1) / 8) * sin(PI * (i + 1) / 8) / 2;
				y1 = y + h * 3.5 + w * cos(PI * (i + 1) / 8) / 2;
				line(x0, (float) y0, (float) x1, (float) y1);
			}
		}
		pop();
	}

	public void bc_place(float _x, float _y) {
		float x = _x;
		float y = _y - 2;
		int w = 300;
		int h = 50;
		push();
		noStroke();
		rectMode(CENTER);
		fill(230);
		rect(x, y + h / 2, w, h);
		fill(200);
		rect(x, y + h, w + 20, h / 2);
		fill(250);
		arc(x, (float) (y + h * 1.25), w, h, 0, PI);
		stroke(30);
		strokeWeight(2);
		line(x - w / 2, y + h, x + w / 2, y + h);
		line(x - w / 2, y + h + 5, x + w / 2, y + h + 5);
		line(x - w / 2, y + h - 5, x + w / 2, y + h - 5);
		fill(210);
		stroke(210);
		strokeWeight(1);
		for (int i = 0; i < 20; i++) {
			triangle((float) (x + 0.5 * (w + 10) * cos(PI * i / 20) - 2), (float) (y + h * 0.75),
					(float) (x + 0.5 * (w + 10) * cos(PI * i / 20) + 2), (float) (y + h * 0.75),
					(float) (x + 0.5 * (w + 60) * cos(PI * i / 20) + 2), y + 2 * h);
		}
		pop();
	}

	public void harbour_centre(float _x, float _y) {
		float x = _x;
		float y = _y;
		int w = 100;
		int h = 250;
		int c = Color.decode("#f0daad").getRGB();

		float fh = 15; // floor height
		float floors = floor(h / fh); // number of floors;
		fh = h / floors; // back calculate exact floor size

		float wh = (fh * 0.5f); // window height
		int windows = 6; // number of windows
		float wr = (float) 0.5; // window ratio
		float ww = w / windows; // window width

		float x_off = x - w / 2 + (ww * (1 - wr)) / 2;
		float y_off = y + (fh - wh) / 2;
		for (int j = 0; j < floors - 1; j++) {
			stroke(c);
			fill(c);
			rect(x - w / 2, y + j * h / floors, w, fh); // floor
			fill(50);
			stroke(50);
			for (int i = 0; i < windows; i++) {
				rect((x_off + i * ww), (y_off + j * h / floors), ww * wr, wh); // windows
			}
		}

		// roof
		strokeWeight(2);
		fill(c);
		stroke(c);
		rect(x - w / 2, y + h - fh, w, fh * random((float) 0.1, (float) 0.5)); // flat

		// platform
		push();
		rectMode(CENTER);
		stroke(c);
		rect(x, y + h + 50, 80, fh);
		rect(x, y + h + 50 + fh, 100, fh);
		rect(x, y + h + 50 + fh * 2, 110, fh);
		rect(x, y + h + 50 + fh * 3, 60, fh);
		rect(x, y + h + 50, 5, 190);
		rect(x, y + h + 50, 1, 240);
		rect(x, y + h + 130, 10, 8);
		fill(50);
		noStroke();
		rect(x, y + h + 50 + fh, 100, fh / 2);
		rect(x, y + h + 50 + fh * 2, 110, fh / 2);
		pop();

		// elevator
		strokeWeight(2);
		fill(c);
		stroke(c);
		rect((float) (x - ww), y, (float) ww * 2, h + 41);
		stroke(50);
		line(x - (float) ww / 2, y + fh / 2, x - (float) ww / 2, y + h + 40);
		line(x + (float) ww / 2, y + fh / 2, x + (float) ww / 2, y + h + 40);
		line(x, y + fh / 2, x, y + h + 40);
	}

	// a nice recursive tree function.
	public void tree(float _x, float _y, float _span) {
		quad(_x, _y, _x + _span, _y - _span / 2, _x, _y + _span, _x - _span, _y - _span / 2);
		if (_span > 2) {
			tree(_x + random(-_span, _span) / 8, _y + _span / 2, (float) (_span * 0.9));
		}
	}

	// gradient
	public void sky() {
		strokeWeight(2);
		// sky
		for (int i = 0; i < 0.15 * height; i++) {
			stroke(lerpColor(color(255, 255, 0), color(255, 255, 255), i / ((float) 0.15 * height)));
			line(0, i + (float) 0.7 * height, width, i + (float) 0.7 * height);
		}
		for (int i = 0; i < 0.15 * height; i++) {
			stroke(lerpColor(color(255, 255, 255), color(0, 255, 255), i / ((float) 0.15 * height)));
			line(0, i + (float) 0.85 * height, width, i + (float) 0.85 * height);
		}
	}

	// noise based mountain range
	public void mtn2(float _p, float _v, float _ns, String _c, float _r) {
		float p = _p; // max peax height
		float v = _v; // min valley low
		float ns = _ns * ar; // noise scale multiplied by the aspecy ratio so it looks OK on wide screens
		float r = _r; // noise offset

//		int c_r = Color.decode(_c).getRed(); // pixel blending for faster gradients
//		int c_g = Color.decode(_c).getGreen();
//		int c_b = Color.decode(_c).getBlue();

		// get min and max to normalize
		float lmin = 1;
		float lmax = 0;
		float y = 0;
		float noiseInput = 0;
		float nsCalc = 0;
		for (int i = 0; i < width; i++) {
			nsCalc = ns * i / width;
			noiseInput = nsCalc + r;
			y = noise(noiseInput);
			lmin = min(lmin, (float) y);
			lmax = max(lmax, (float) y);
		}

		stroke(Color.decode(_c).getRGB());
		fill(Color.decode(_c).getRGB());

//		loadPixels(); // pixeldensity is 1 //remember screen is inverted!
		for (int i = 0; i < width; i++) { // used noise plus a trig function to create more natural valleys
			nsCalc = ns * i / width;
			y = map(noise(nsCalc + r) * sqrt((1 + sin(ar * TWO_PI * i / width + r)) / 2), lmin, lmax, v, p) * height;
			// line(i, 0, i, y);
//            println("idx34:" + index2 +  " " + y + " " + lmin + " " + lmax);

			strokeWeight(2);
			line(i, 0, i, y);

//			for (int j = 0; j < y; j++) {
//				// TODO check (int)y with >>
//				int blend = (128 - (int) y + j) >> 1; // faster
//				// index = ((height-j) * width + i)<<2; //faster
//				int index = ((height - j) * width + i)  - (2000); //<< 2; // faster  - (2000);
//				pixels[index] = c_r + blend;
//				pixels[index + 1] = c_g + blend;
//				pixels[index + 2] = c_b + blend;
//				pixels[index + 3] = 255;
//			}
		}
//		updatePixels();
	}

	// similar to above but adds trees on the crest and does away with pixel
	// blending...
	public void tree_mtn(float _p, float _v, float _ns, String _c, float _r, float _ts) {
		float p = _p;
		float v = _v;
		float ns = _ns * ar;
		float r = _r;
		float ts = _ts; // tree size

		// get min and max to normalize
		float lmin = 1;
		float lmax = 0;
		float nsCalc = 0;
		for (int i = 0; i < width; i++) {
			nsCalc = ns * i / width;
			float y = noise(nsCalc + r);
			lmin = min(lmin, y);
			lmax = max(lmax, y);
		}

		stroke(Color.decode(_c).getRGB());
		fill(Color.decode(_c).getRGB());

		for (int i = 0; i < width; i++) {
			nsCalc = ns * i / width;
			float y = map(noise(nsCalc + r), lmin, lmax, v, p) * height;
			strokeWeight(2);
			line(i, 0, i, y);
			if (i % _ts == 0) {
				strokeWeight(1);
				tree(i, y + random(-_ts * 2, 0), _ts);
			}
		}
	}

	// Richard Bourne Special
	// save jpg

	public void mousePressed() {
//		int lapse = 0; // mouse timer
//		if (millis() - lapse > 400) {
//			lapse = millis();
//			save("img_" + month() + "-" + day() + "_" + hour() + "-" + minute() + "-" + second() + ".jpg");
//		}
//		return (false);
	}

	public void keyPressed() {
		startmeup();
	}

	@Override
	public void settings() {
		size(winWidth, winHeight);
		pixelDensity(1);
	}

}
