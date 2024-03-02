import java.awt.Color;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * fireworks app 3
 */
public class FireworksApp extends PApplet {

	static final int winWidth = 1000;
	static final int winHeight = 1000;

	ArrayList<Particle> particles = new ArrayList<Particle>();
	ArrayList<Particle> explosions = new ArrayList<Particle>();
	int endColor;
	float gravity = (float) 0.15; // 0.25
	String redColor = ""; // 'red';
	String yellowColor = "";// 'yellow';

	// ['red', 'orange', 'yellow', 'lime', 'cyan', 'magenta', 'white'];
//	String[] colors = { "red", "orange", "yellow", "lime", "cyan", "magenta", "white" };
	Color[] COLORS45 = { Color.RED, Color.ORANGE, Color.PINK, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.WHITE };
	PGraphics houses;

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

	@Override
	public void settings() {
		size(winWidth, winHeight);

		super.settings();
	}

	@Override
	public void setup() {
		pixelDensity(1);
		endColor = color(64, 0);
		makeHouses();

		super.setup();
	}

	public static void main(String[] args) {
		String[] processingArgs = { "FireworksApp" };
		PApplet mySketch = new FireworksApp();
		PApplet.runSketch(processingArgs, mySketch);
	}

	public void makeHouses() {
		houses = createGraphics(winWidth, winHeight);

		// must call beginDraw before using strokeWeight
		houses.beginDraw();
		houses.strokeWeight(2);
		int houseCount = 10;
		float houseWidth = winWidth / houseCount;
		int houseWindowWidth = 10;
		int houseWindowHeight = 15;

		for (int i = 0; i < houseCount; i++) {
			float houseHeight = random(35, 100);
			houses.fill(128);
			houses.rect(houseWidth * i, height - houseHeight, houseWidth, houseHeight * 2);

			// fill building with windows. Randomly light the windows
			for (float windowY = height - houseHeight + 10; windowY < height - houseWindowHeight
					- 5; windowY += houseWindowHeight + 5) {
				houses.fill(random(0, 1) < 0.25 ? Color.YELLOW.getRGB() : 64);
				houses.rect(houseWidth * i + 12, windowY, houseWindowWidth, houseWindowHeight);

				houses.fill(random(0, 1) < 0.25 ? Color.YELLOW.getRGB() : 64);
				houses.rect(houseWidth * (i + 1) - 12 - houseWindowWidth, windowY, houseWindowWidth, houseWindowHeight);

			}
		} // for

		// always match endDraw with beginDraw
		houses.endDraw();
	}

	@Override
	public void mouseClicked() {

		super.mouseClicked();

		int colorVal = (int) random(0, 6);
//		String color343 = colors[colorVal];
//		Color color23 = Color.decode(color343);
		Color color23 = COLORS45[colorVal];

		Firework fireWk = new Firework(mouseX, winHeight, color23.getRGB()); // add random color
		println("firework speed: " + (int) (fireWk.xSpeed) + " " + (int) (fireWk.ySpeed));
//		particles.push(fireWk);
		particles.add(fireWk);

	}

	@Override
	public void draw() {
		background(64);

		for (Particle p : particles) {
			p.step();
			p.draw();
		} // for

		for (Particle ex : explosions) {
			ex.step();
			ex.draw();
		} // for

		// remove dead particles
//		particles = particles.filter((p) => p.isAlive);
//
		ArrayList<Particle> deadParticles2 = new ArrayList<Particle>();
		for (Particle p : particles) {
			if (!p.isAlive) {
				deadParticles2.add(p);
			}
		} // for

		particles.removeAll(deadParticles2);

		// delete the explosions separate, java concurrency problems due to adding
		// explosions particles while iterating
		// through the firework
		ArrayList<Particle> deadExplosions = new ArrayList<Particle>();
		for (Particle ex : explosions) {
			if (!ex.isAlive) {
				deadExplosions.add(ex);
			}
		} // for

		explosions.removeAll(deadExplosions);

		image(houses, 0, 0);

	}

	class Particle {
		float x;
		float y;
		float xSpeed;
		float ySpeed;
		int color1;
		int size;
		boolean isAlive;
		ArrayList<Vector1> trail3 = new ArrayList<Vector1>();
		int trailIndex;
		int partNum;
		private boolean isParticle = true;

		Particle() {

		}

		Particle(float x, float y, float xSpeed, float ySpeed, int pColor, int size, int partNum) {
			this.x = x;
			this.y = y;
			this.xSpeed = xSpeed;
			this.ySpeed = ySpeed;
			this.color1 = pColor;
			this.size = size;
			this.isAlive = true;
			this.trail3 = new ArrayList<Vector1>();
			this.trailIndex = 0;
			this.partNum = partNum;
		}

		public void step() {
//			this.trail[this.trailIndex] = createVector(this.x, this.y);
			this.trail3.add(new Vector1(this.x, this.y));
			this.trailIndex++;
//			if (this.trailIndex > 10) { // default 10
//				this.trailIndex = 0;
//			}
			this.x += this.xSpeed;
			this.y += this.ySpeed;

			this.ySpeed += gravity;

			if (this.y > height) {
				this.isAlive = false;
			}
		}

		public void draw() {
			this.drawTrail();
			fill(this.color1);
			noStroke();
			rect(this.x, this.y, this.size, this.size);

//	        if (this.fireWork && false) {
//	          print("firework: "+ Math.floor(this.x) + " " + Math.floor(this.y) + " " + Math.floor(this.countdown));
//	        }
//	      
//	        if (this.partNum == -2) {
//	          print("partNum-2: "+ Math.floor(this.x) + " " + Math.floor(this.y) + " " + Math.floor(this.ySpeed) );
//	        }

		}

		public void drawTrail() {
			float index = 1;

			for (int i = this.trailIndex - 1; i >= 0; i--) {
				int tColor = lerpColor(this.color1, endColor, index / this.trail3.size());
//				if (!this.isParticle()) {
//					println("lerp %:" + (float)(index / this.trail3.size()) + " " + index + " " + this.trail3.size());
//				}
				fill(tColor);
				noStroke();
				rect(this.trail3.get(i).x, this.trail3.get(i).y, this.size, this.size);
				index++;
			}

			index = 0;
			for (int i = this.trail3.size() - 1; i >= this.trailIndex; i--) {
				int tColor = lerpColor(this.color1, endColor, index / this.trail3.size());
//				if (!this.isParticle()) {
//					println("lerp %:" + (float) (index / this.trail3.size()) + " " + index + " " + this.trail3.size());
//				}
				fill(tColor);
				noStroke();
				rect(this.trail3.get(i).x, this.trail3.get(i).y, this.size, this.size);
				index++;
			} // for
		}

		boolean isParticle() {
			return true;
		}

	}

	class Firework extends Particle {
		float countdown = random(30, 60);
		boolean fireWork;
		boolean firstNegative;

		Firework() {
			super();
		}

		Firework(float x, float y, int color) {
			// random(colors)
			super(x, y, random(-2, 2), random(-10, -15), color, 10, -1);
			this.countdown = random(30, 60); // 30,60
			this.fireWork = true;
			this.firstNegative = false;
		}

		public boolean isParticle() {
			return false;
		}

		public void step() {

			// need to call the super.step
			super.step();

			this.countdown--;
			if (this.countdown <= 0) {
				float explosionSize = random(20, 50); // 20, 50
//				println("explosion num:" + Math.floor(explosionSize));
				for (int i = 0; i < explosionSize; i++) {

					float speed = random(5, 10);
					float angle = random(TWO_PI);
					float xSpeed = cos(angle) * speed;
					float ySpeed = sin(angle) * speed;

					int xColor = this.color1;
					int iNum = i;
					if (ySpeed < 0 && !this.firstNegative) {
						// if the firework color is yellow use the red color
						// to track my one particle with yellow color
						if (xColor == Color.YELLOW.getRGB()) {
							xColor = Color.RED.getRGB();
						} else {
							xColor = Color.YELLOW.getRGB();
						}

//						println("part speed343: " + iNum + ": " + Math.floor(xSpeed) + " " + Math.floor(ySpeed));
						this.firstNegative = true;
						iNum = -2;
					}

					Particle partX = new Particle(this.x, this.y, xSpeed, ySpeed, xColor, 5, iNum);

					explosions.add(partX);
//					particles.push(partX);
				} // for
				this.isAlive = false;
			} // if
		} // step
	} // class Firework

}
