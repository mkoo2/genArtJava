import java.awt.Color;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

/**
 * Time land app how to slow down the horizontal line speed?
 * 
 */

public class TimeLandApp extends PApplet {

	static final int winWidth = 1000;
	static final int winHeight = 1000;

	// orange, red, maroon, blue, blue2
	static final String[] COLORS4 = { "#be7f73", "#b75446", "#94402b", "#57507e", "#363169" };

	// yellow
	static final String BG_COLOR = "#FFFFC7"; // #B3E5FC FFFFC7
//	static final String BG_COLOR = "#FFFFC7"; // #B3E5FC FFFFC7

	private static int X_VELOCITY = 4;

	Color bgColor;
	ArrayList<Dripper2> drippers = new ArrayList<Dripper2>();// =[];
	boolean next2 = false;
	int w2;
	ArrayList<Drip2> drips2 = new ArrayList<Drip2>();
	int dripperIdx = 1;

	PGraphics mainGraphics;
	PImage g2;

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

	} // Vect1

	public void settings() {
		size(winWidth, winHeight);

	}

	public static void main(String[] args) {
		String[] processingArgs = { "TimeLandApp" };
		PApplet mySketch = new TimeLandApp();
		PApplet.runSketch(processingArgs, mySketch);
	}

	/**
	 * add the last Vect1 to the current Vect
	 *
	 * @param last
	 * @return
	 */
	public Vector1 addToVector(Vector1 v1, Vector1 last) {
		float xx = last.x + v1.x;
		float yy = last.y + v1.y;

		v1.x = xx;
		v1.y = yy;

		return new Vector1(xx, yy);
	}
	
	/**
	 * add the last Vect1 to the current Vect
	 *
	 * @param last
	 * @return
	 */
	public PVector addToPVector(PVector v1, PVector last) {
		float xx = last.x + v1.x;
		float yy = last.y + v1.y;

		v1.x = xx;
		v1.y = yy;

		return new PVector(xx, yy);
	}
	
	public void setup() {
		// createCanvas(windowWidth, windowHeight);
		w2 = max(winWidth, winHeight);

		Color bgCol1 = Color.decode(BG_COLOR);
//		Color bgCol1 = new Color(31, 100, 100);
//		Color bgCol1 = new Color(221, 76, 13);
		
		mainGraphics = createGraphics(width, height);
		
//		Color colorOrange = new Color(31, 100, 100);
//		
//		background(colorOrange.getRGB());
//		fill(255);
//		noStroke();
//		circle(random(width), (height/2 * 1.5f), random(75, 200));
		

		Color bgCol2 = new Color(bgCol1.getRed(), bgCol1.getGreen(), bgCol1.getBlue(), 30);
		// bgColor= color('#FFFFC7');
		bgColor = bgCol2;
		background(bgCol2.getRGB());
		// bgColor.setAlpha(30);
		// drippers.push(new dripper());

		drippers.add(new Dripper2(0));
	}

	public void draw() {

		PImage g1 = get();

		if (g2 == null) {
			g2 = g1;
		}

		if (next2) {

			Color bgCol = Color.decode(BG_COLOR);
			background(bgCol.getRGB());

			image(g1, 0, -30);
			image(g1, 0, height - 30, width, 30, 0, height - 30, width, 30);

//			image(g2, 0, -30);
//			image(g2, 0, height - 30, width, 30, 0, height - 30, width, 30);
			
			tint(bgCol.getRed(), 216); // 216

//			PImage g2 = get();
//			push();
//			fill(Color.ORANGE.getRGB());
//			Color colorOrange = null;
//			// 221, 76, 13
//			colorOrange = new Color(color(232, 190, 100));
//			fill(colorOrange.getRGB());
//			noStroke();
//			circle(width/3, (height/4), 150);
//			
//			pop();
			
			drippers.add(new Dripper2(this.dripperIdx));

			this.dripperIdx++;
//			println(">>>>>>>>>>>> new dripper:" );
			next2 = false;
		}

//		mainGraphics.beginDraw();
//		mainGraphics.image(g1, 0, (float) 1);
		
		ArrayList<Dripper2> removeItems2 = new ArrayList<Dripper2>();
		for (Dripper2 dr : drippers) {
			dr.move3();
			dr.paint();
			dr.done(removeItems2);
		}

		drippers.removeAll(removeItems2);
		

		ArrayList<Drip2> removeDrips31 = new ArrayList<Drip2>();
		for (Drip2 d : drips2) {
			d.droop(this);
			d.dry(removeDrips31);
		}

//		mainGraphics.endDraw();
//		image(mainGraphics, 0, 0);
		
		drips2.removeAll(removeDrips31);

	}

	@Override
	public void mouseClicked() {
		
		background(bgColor.getRGB());
		
	}
	
	/**
	 * dripper class
	 */
	class Dripper2 {
		PVector pos;
		PVector vel;
		float colorGrad;
		int idx;

		// keep track of the y offset caused by noise.
		private float yDiff2 = 0;
		float priorY = 0;
		private PVector lowSpot = new PVector(0, 0);
		private PVector lowestSpot = new PVector(0, 0);
		private int lowSpotId = 0;
		private int lowSpotCount = 1;
		public int lowSpotComparator = (int) random(2, 4);

		public Dripper2(int i, float x, float y) {

			// this.pos= createVector(x, y);
			this.pos = new PVector(x, y);
			// this.acc= createVector(0.01, 0)
			this.vel = new PVector(X_VELOCITY, 0);

			this.colorGrad = random(100);
			this.idx = i;

		} // constructor

		public Dripper2(int i) {

			// this.pos= createVector(x, y);
			this.pos = new PVector(0, 0);
			// this.acc= createVector(0.01, 0)
			this.vel = new PVector(X_VELOCITY, 0);

			this.colorGrad = random(100);
			this.idx = i;

		} // constructor

		public void move3() {
			float yoff = 0;

			// decrease the frame count to smooth of the line: default 100
			// noise function creates noise which causes the curve to jump up or drop down
			// suddenly
			yoff = map(noise(this.pos.x / 500, frameCount / 100, this.colorGrad), (float) -0.1, 1, winHeight / 2,
					winHeight);

			// TODO
			addToPVector(this.pos, this.vel);

			this.pos.y = yoff;

			// calcJunction2();

		} // move

		/**
		 * calculates the difference between the prior drip and the current drip y
		 * location.
		 * 
		 * @param drp
		 */
		public void calcJunction2(Drip2 drp) {

			int DIFF_KEY = 30;
			float diff = (this.pos.y - this.priorY);

			boolean isDiffDrip = false;

			// ignore the drip that is in 4th x position
			// ignore any drip calc outside of the window
			if (this.pos.x > 4 && this.pos.y <= height && this.pos.x <= width) {
				if (diff < -DIFF_KEY) {
					this.setYDiff3(diff);

//					println("arc pos362 :" + " " + (int) drp.pos.x + " " + (int) drp.pos.y + " " + (int) diff);
					isDiffDrip = true;
					drp.pos.y = drp.pos.y - diff;

					this.priorY = this.pos.y;
//					println("arc pos362a:" + " " + (int) drp.pos.x + " " + (int) drp.pos.y + " "
//							+ (int) this.getyDiff() + " " + (int)this.priorY);

				} else if (diff > DIFF_KEY) {
					this.setYDiff3(diff);

//					println("arc pos364 :" + " " + (int) drp.pos.x + " " + (int) drp.pos.y + " " + (int) diff);
					isDiffDrip = true;
					drp.pos.y = drp.pos.y - diff;

					this.priorY = this.pos.y;
//					println("arc pos364a:" + " " + (int) this.pos.x + " " + (int) this.pos.y + " "
//							+ (int) this.getyDiff() + " " + (int)this.priorY);
				}
			} // if

			// indicate to the drip that there has been an offset.
			drp.setDripOffset(isDiffDrip);

			int lowSpotOffset = (int) random(100, 400);
			if (this.priorY > this.pos.y && this.pos.x > lowSpotOffset) {
				this.lowSpot = this.pos;

				if (this.lowestSpot.y == 0 || this.lowestSpot.y + 10 < this.lowSpot.y) {
//					println("low spot 3432:" + drp.dripr2.idx + " "+ (int)this.lowSpotX + " " + (int)this.lowSpotY + " " + 
//									(int)this.lowestSpotX + " " + (int)this.lowestSpotY);

					this.lowestSpot = this.pos;
					this.lowSpotCount++;

				}
			}

//			int lowSpotCountCompare = (int)random(3, 6);
			if (this.lowestSpot.y > 0 && this.lowSpotCount == this.lowSpotComparator && this.lowSpotId == 0) {
				this.lowSpotId = 1;
				this.pos.x = this.pos.x - X_VELOCITY + 7;
//				println("low spot 423:" + drp.dripr2.idx + " " + (int) this.lowSpot.x + " " + (int) this.lowSpot.y + " "
//						+ (int) this.lowestSpot.x + " " + (int) this.lowestSpot.y);
				drp.setLowSpot(true);
			}

			if (this.lowestSpot.y > 0 && this.lowSpotId == 1) {
//				println("low spot 3432:" + (int)this.lowSpotX + " " + (int)this.lowSpotY + " " + (int)this.lowestSpotX + " " + (int)this.lowestSpotY);
				this.lowSpotId = 2;
			}

			this.priorY = (this.pos.y);

		}

		public void paint() {
			float grad1 = this.colorGrad % 4;

			String color5 = COLORS4[floor(grad1)];
			Color bgCol4 = Color.decode(color5);
			// bgCol4 = Color.MAGENTA; //[floor(grad1)];

			int c1 = bgCol4.getRGB();

			String color6 = COLORS4[floor((grad1 + 1) % 4)];
			Color bgCol3 = Color.decode(color6);
			int c2 = bgCol3.getRGB();

			float amt = grad1 % 1;
			// amt = (float)0.5;
			int c = lerpColor( c1, c2, amt);

			// drips.push(new drip(this.pos.x, this.pos.y, c));

			int ix = drips2.size();

			// if the Dipper has a y offset add that offset to the y position of the drip
			Drip2 drp = new Drip2(this, ix, this.pos.x, this.pos.y - this.getYDiff3(), c);
			calcJunction2(drp);
			drips2.add(drp);

			// int i = drips.size();

			this.colorGrad += 0.007;

		} // paint

		/**
		 * remove the completed drippers
		 * 
		 * @param removeItems
		 */
		public void done(ArrayList<Dripper2> removeItems) {
			if (this.pos.x > w2 * 2.5) {
				int index = drippers.indexOf(this);
				// TODO
				removeItems.add(this);
				// drippers.splice(index, 1);
				next2 = true;
			}
		}// done

		float getYDiff3() {
			return yDiff2;
		}

		void setYDiff3(float diff) {

			// yDiff = -42 && diff = 97
			if (this.yDiff2 == 0) {
				this.yDiff2 = diff;
			} else {
				this.yDiff2 += diff;
			}

		} // setYDiff
	}

	/**
	 * class Drip
	 */
	class Drip2 {
		int idx;
		Vector1 pos;
		Vector1 acc;
		Vector1 vel;
		float color;
		float stroke;
		int alpha;
		int size;
		float sizeVel;
		float sizeD;
		float sizeAcc;
		float sNoise;
		int timer;
		double timerEnd;
		boolean lowSpot = false;;

		// keep track if the current drip has an offset in isDripOffset. If so, we can
		// set the color to cyan so it is easier to
		// keep track of
		private boolean isDripOffset2 = false;
		Dripper2 dripr2;

		public Drip2(Dripper2 dr, int i, float x, float y, float c) {
			this.dripr2 = dr;
			this.idx = i;
			this.pos = new Vector1(x, y);
			this.acc = new Vector1(0, 0);
			this.vel = new Vector1(0, 0);
			this.color = c;
			this.stroke = c;
			this.alpha = 20;
			this.size = 3;
			this.sizeVel = random((float) 0.15, (float) 0.3);
			this.sizeD = random(100, 200);
			this.sizeAcc = -this.sizeVel / this.sizeD;
			this.sNoise = random(1000);
			this.timerEnd = (height - this.pos.y) * 2.5;// 2.5; //2.5;
			this.timer = 0;

		}

		public void droop(TimeLandApp app) {
			this.sNoise += 0.01;
			this.timer += 1;
			if (this.timer < this.timerEnd / 2) {
				this.acc = new Vector1(0, (float) 0.04);
			} else {
				this.acc = new Vector1(0, (float) -0.009);
			}
			addToVector(this.vel, this.acc);
			addToVector(this.pos, this.vel);

			this.alpha -= 0.01;

			// TODO
			// this.stroke.setAlpha(this.alpha/2);
			// this.color.setAlpha(this.alpha);

			Color col33 = new Color((int) this.color);
			Color col34 = new Color(col33.getRed(), col33.getGreen(), col33.getBlue()); // , this.alpha);
			// fill((int)this.color);

			int WIDTH_KEY = 8; // set 5 for cyan

			// we can find the location of the drip offset by setting the color to cyan
			Color col22 = Color.CYAN;
			if (this.isDripOffset2) {
//				col34 = col22;
				WIDTH_KEY = 5;
			}

			if (this.lowSpot) {
				col34 = col22;
				WIDTH_KEY = 7;

			}

			fill(col34.getRGB());

			Color col36 = new Color(col33.getRed(), col33.getGreen(), col33.getBlue()); // , this.alpha*2);

			// stroke((int)this.stroke);
			stroke((int) col36.getRGB());

			this.sizeVel += this.sizeAcc;
			this.size += this.sizeVel;

			float sOff = 0;
			sOff = map(noise(frameCount / 100, this.sNoise), 0, 1, -this.size / 3, this.size / 3);
			float sizeX = this.size;
			if (this.lowSpot == true) {
				sizeX = 4 + sOff + WIDTH_KEY;
			}

//			println ("drip432:" + this.pos.x + " " + this.pos.y );

			arc(this.pos.x, this.pos.y, sizeX, this.size + sOff, 0, PI); // HALF_PI PI

		} // droop

		public void dry(ArrayList<Drip2> removeDrips34) {
			if (this.timer > this.timerEnd) {
//				int index = drips2.indexOf(this);

				// drips.splice(index, 1);
				removeDrips34.add(this);

			}

		} // dry

		public boolean isDripOffset1() {
			return isDripOffset2;
		}

		public void setDripOffset(boolean isDripOffset) {
			this.isDripOffset2 = isDripOffset;
		}

		public boolean isLowSpot() {
			return lowSpot;
		}

		public void setLowSpot(boolean lowSpot) {
			this.lowSpot = lowSpot;
		}
	} // drive

}
