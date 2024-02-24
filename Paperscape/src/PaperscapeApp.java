import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import processing.core.PApplet;

public class PaperscapeApp extends PApplet {

	// Created for the #WCCChallenge - Topic: Improbable Architecture
	//
	// I used two-point perspective to draw this sketch - so this is made of up MANY
	// calculations of lines and point intersections.
	// I initially chose random hues just to distinguish each block during
	// development, but decided to keep them as it reminded me of stacks of
	// post-its. :)
	//
	// See other submissions here: https://openprocessing.org/curation/78544
	// Join the Birb's Nest Discord community! https://discord.gg/S8c7qcjw2b

	Vector1 gLeftPt;
	Vector1 gRightPt;

	ArrayList<BuildingBlock> gAllBlocks = new ArrayList<BuildingBlock>(); // [];
	ArrayList<Vector1> gBgGround = new ArrayList<Vector1>(); // [];
	ArrayList<Vector1> gBgSky = new ArrayList<Vector1>(); // [];
//
	float gDistFromPointMin;
	float gWallHeightInc;
	float gWallDistMin;

	float gWindowLength;
	float gWindowSpacing;

	float gWindowHeight;
	float gWindowSpacingX;
	float gWindowSpacingY;

	float gWindowColor;

	// boolean to keep track if the building block section has been reversed.
	boolean reversed = false;

	// when the picture gets created the first time, the sky is purple. Remove the
	// purpole color.
	int firstTimeBg = 1;

	int winWidth = 1000;
	int winHeight = 1000;

	Color[] COLOR55 = { Color.BLUE, Color.RED, Color.CYAN, Color.LIGHT_GRAY, Color.ORANGE}; //, Color.PINK, Color.YELLOW };
	
	// orange, red, maroon, blue, blue2
	String[] colors77 = { "#6fbf80", "#b75446", "#6f9bc0", "#b2a05e", "#dcafc9" };
	
	String[] skyColors67 = {"#add7eb", "#adbaeb", "#adcdeb", "#d4dcea", "#e1ebfc"};

	public void settings() {
		size(winWidth, winHeight);

	}

	/**
	 * class Line
	 */
	class Line {
		public float slope;
		public Vector1 p0;
		public Vector1 p1;
		public float yIntercept;

		public Line(Vector1 p0, Vector1 p1) {
//	    Object.assign(this, { p0, p1 });
			this.p0 = p0;
			this.p1 = p1;

			this.slope = p0.x == p1.x ? 0 : (p1.y - p0.y) / (p1.x - p0.x);
			this.yIntercept = this.p0.y - this.slope * this.p0.x;
		}

		public void draw() {
			line(this.p0.x, this.p0.y, this.p1.x, this.p1.y);
		}
	} // Line

	@Override
	public void setup() {
//	  createCanvas(windowWidth, windowHeight);
		colorMode(RGB); // HSL

		gWindowColor = color((float) 255, (float) 0.25);

		// set points for two point perspective calculations
		gLeftPt = new Vector1(0, (float) 0.6 * winWidth); // createVector(0, 0.6 * widWidth);
		gRightPt = new Vector1(winHeight, (float) 0.6 * winWidth); // createVector(winHeight, 0.6 * widWidth);
		gDistFromPointMin = (float) (winHeight * 0.2);
		gWallDistMin = (float) (winHeight * 0.1);

		createBuilding();
	}

	public static void main(String[] args) {
		String[] processingArgs = { "PaperscapeApp" };
		PApplet mySketch = new PaperscapeApp();
		PApplet.runSketch(processingArgs, mySketch);
	}

	@Override
	public void draw() {
//		background(160);

		// draw building blocks
		for (BuildingBlock block : gAllBlocks) {
			block.draw();
		}

//		print("draw block341: ");
//		for (BuildingBlock block : gAllBlocks) {
//			print(" " + block.indx);
//		}
//		println("end");
	}

	public void createBuilding() {
		noLoop();
		background(160);

		// reset arrays
		gBgGround = new ArrayList<Vector1>();// [];
		gBgSky = new ArrayList<Vector1>();// [];
		gAllBlocks = new ArrayList<BuildingBlock>();// [];

		// create background elements
		for (int i = 0; i < 50; i++) {
			// createVector(random(-width, 2 * width), random(gLeftPt.y, height))
			gBgGround.add(new Vector1(random(-width, 2 * width), random(gLeftPt.y, height)));
			// gBgSky.add(createVector(random(-width, 2 * width), random( (float)(-0.2 *
			// height) , gLeftPt.y))
			gBgSky.add(new Vector1(random(-width, 2 * width), random((float) (-0.2 * height), gLeftPt.y)));
		}

		// draw background
		noStroke();
		for (Vector1 ground1 : gBgGround) {

			float[] hsColor5 = { random(30, 35), 0, 30 };
			int color66 = PaperscapeApp.hslToRgb(hsColor5);

			fill(color66); // , (float) 100 0.05
			triangle(ground1.x, ground1.y, gLeftPt.x, gLeftPt.y, gRightPt.x, gRightPt.y);
		} // for

		if (firstTimeBg != 1) {
			for (Vector1 sky1 : this.gBgSky) {
				float[] hsColor5 = { 250, 60, random(87, 93) };
				int color65 = PaperscapeApp.hslToRgb(hsColor5);
				
				float colorItem = (float) random((float) 0, (float) 4);
				int intColItem = Math.round(colorItem);
				int intColItem2 = intColItem+1;
				if (intColItem2 ==5) {
					intColItem2 = 0;
				}
				
				Color colorVal = null;
				String strColor77 = skyColors67[intColItem];
				colorVal = Color.decode(strColor77);

				String strColor76 = skyColors67[intColItem2];
				Color colorVal2 = Color.decode(strColor76);

				float amt2 = random(0, 1);
				int nextColor = this.lerpColor((int)colorVal.getRGB(), (int)colorVal2.getRGB(), amt2);
				
				//fill(random(140, 150), 228, random(180, 242));
				fill(nextColor);
				triangle(sky1.x, sky1.y, gLeftPt.x, gLeftPt.y, gRightPt.x, gRightPt.y);

			}
			firstTimeBg = 2;
		} else {
			firstTimeBg = 2;
		}

		// perspective lines
		strokeWeight(1); // draw horizon line
		line(0, gLeftPt.y, width, gRightPt.y);

		// create and stack building blocks
		float yp = (float) (0.95 * height);

		// let [upper, mid, lower] = [[], [], []];
		ArrayList<BuildingBlock> upper1 = new ArrayList<BuildingBlock>();
		ArrayList<BuildingBlock> mid1 = new ArrayList<BuildingBlock>();
		ArrayList<BuildingBlock> lower1 = new ArrayList<BuildingBlock>();

		float count = random(18, 30);
		gWallHeightInc = (float) ((0.7 * height) / count);
		gWindowHeight = (float) (0.8 * gWallHeightInc);
		gWindowLength = (float) (0.5 * gWallHeightInc);
		gWindowSpacingX = (float) (0.2 * gWindowLength);
		gWindowSpacingY = (float) (0.1 * gWindowHeight);

		float colorItem = 0;
		for (int i = 0; i < count; i++) {
			// colorItem++;
			colorItem = (float) random((float) 0, (float) 4);
			int intColItem = Math.round(colorItem);
			int intColItem2 = -1;
			Color colorVal = null;
			//colorVal = COLOR55[(int) colorItem];
			colorVal = Color.decode(colors77[intColItem]);
			
			intColItem2++;
			
			if (intColItem2 == 5) {
				intColItem2 = 0;
			}
			Color colorVal2 = Color.decode(colors77[intColItem2]);
//			if (colorItem == 6) {
//				colorItem = 0;
//			}

			BuildingBlock block = new BuildingBlock(yp, i, colorVal, colorVal2);

//		    block.level > 0 ? upper.push(block) : block.level < 0 ? lower.push(block) : mid.push(block);
			if (block.level > 0) {
				upper1.add(block);
			} else if (block.level < 0) {
				lower1.add(block);

			} else {
				mid1.add(block);
			}

			yp -= random((float) 0.8, (float) 1.2) * gWallHeightInc;
		} // for

//		print("up31:");
//		for (BuildingBlock block : upper1) {
//			print (block.indx + " " );
//		}
//		println(" end");

//		  gAllBlocks = [...upper, ...lower.reverse(), ...mid.reverse()];
		if (reversed) {
			Collections.reverse(lower1);
			Collections.reverse(mid1);
			reversed = false;
		} else {
			reversed = true;
		}

//		print("upper32:");
//		for (BuildingBlock block : upper1) {
//			print (block.indx + " " );
//		}		
//		println(" end");

		gAllBlocks.addAll(upper1);
		gAllBlocks.addAll(lower1);
		gAllBlocks.addAll(mid1);

		loop();

	} // createBuilding

	@Override
	public void mouseClicked() {
		createBuilding();
	}

	public Vector1 getIntersectionPtWithConstant(float constant, Line line) {
		float x = constant;
		float y = line.p0.y + ((constant - line.p0.x) * (line.p1.y - line.p0.y)) / (line.p1.x - line.p0.x);

		return new Vector1(x, y); // createVector(x, y);
	}

	class BuildingBlock {
		public int level;

		// add indx to keep track of which building block will be drawn as magenta
		public int indx;
		public ArrayList<Wall> allWalls = new ArrayList<Wall>();
		public Color buildingColor2;
		public Color nextBuildingColor;
		

		public BuildingBlock(float yp, int i, Color color6, Color color7) {

			indx = i;
			colorMode(HSB); // HSL
			this.buildingColor2 = color6;
			this.nextBuildingColor = color7;

			// center corner
			// createVector(random(2 * gDistFromPointMin, width - 2 * gDistFromPointMin),
			// yp)
			Vector1 centerUpperPt = new Vector1(random(2 * gDistFromPointMin, width - 2 * gDistFromPointMin), yp);
			// createVector
			Vector1 centerLowerPt = new Vector1(centerUpperPt.x,
					centerUpperPt.y - random((float) 0.5, (float) 2.5) * gWallHeightInc);

			Line centerLine = new Line(centerUpperPt, centerLowerPt);
			float centerLineHeight = centerUpperPt.y - centerLowerPt.y;

			// left side
			float leftPosX = random(gLeftPt.x + gDistFromPointMin, centerUpperPt.x - gWallDistMin);
			Line leftCornerLine = this.getCornerLine(leftPosX, centerLine, gLeftPt);

			// right side
			float rightPosX = floor(random(centerUpperPt.x + gWallDistMin, gRightPt.x - gDistFromPointMin));
			Line rightCornerLine = this.getCornerLine(rightPosX, centerLine, gRightPt);

			// ceiling
			Vector1 ceilingPt = this.getIntersectionPt(new Line(leftCornerLine.p1, gRightPt),
					new Line(rightCornerLine.p1, gLeftPt));
			Vector1 floorPt = this.getIntersectionPt(new Line(leftCornerLine.p0, gRightPt),
					new Line(rightCornerLine.p0, gLeftPt));
//
//	    // pick a color
			float cHue = random(0, 360);
			float cSat = random(40, 50); // 30 , 40;
			float cLig = random(40, 50); // 70, 80;

			// create walls
//	    const createWall = (points, colorMultiplier, hasWindows) => new Wall(points, color(cHue, cSat, cLig * colorMultiplier), hasWindows);

			Vector1[] floorWallPts = { leftCornerLine.p0, centerLine.p0, rightCornerLine.p0, floorPt };
			float color1 = color(cHue, cSat, (float) (cLig * 1.0));
			
			Wall floorWall = createWall2(floorWallPts, buildingColor2, false);

			Vector1[] leftWallPts = { leftCornerLine.p1, leftCornerLine.p0, centerLine.p0, centerLine.p1 };
			float color3 = color(cHue, cSat, (float) (cLig * 1.0));
			
			int lftWallColor7 = lerpColor(buildingColor2.getRGB(), this.nextBuildingColor.getRGB(), (float)0.10);
			Color lftWallColor8 = new Color(lftWallColor7);
			
			Wall leftWall = createWall2(leftWallPts, lftWallColor8, true);
			
			int ceilWallColor3 = lerpColor(buildingColor2.getRGB(), this.nextBuildingColor.getRGB(), (float)0.20);
			Color ceilWallColor4 = new Color(ceilWallColor3);

			Vector1[] ceilingPts = { leftCornerLine.p1, centerLine.p1, rightCornerLine.p1, ceilingPt };
			float color4 = color(cHue, cSat, (float) (cLig * 1.0));
			Wall ceilingWall = createWall2(ceilingPts, ceilWallColor4, false);

			Vector1[] rightWallPts = { centerLine.p1, centerLine.p0, rightCornerLine.p0, rightCornerLine.p1 };
			float color2 = color(cHue, cSat, (float) (cLig * 1.0));
			
			int rgtWallColor5 = lerpColor(buildingColor2.getRGB(), this.nextBuildingColor.getRGB(), (float)0.30);
			Color rgtWallColor6 = new Color(rgtWallColor5);
			
			Wall rightWall = createWall2(rightWallPts, rgtWallColor6, true);

//			if (this.indx == 10) {
//				for (Vect1 ve : rightWall.allPoints) {
//					println("wall dim:" + ve.x + " " +ve.y);
//				}
//				println("end wall dim:");
//			}


			// order walls for rendering purposes
			boolean isAboveLeftPt = centerUpperPt.y > gLeftPt.y && centerLowerPt.y > gLeftPt.y;
			boolean isBelowLeftPt = centerUpperPt.y < gLeftPt.y && centerLowerPt.y < gLeftPt.y;

			if (isAboveLeftPt) {
				Wall[] walls3 = { ceilingWall, leftWall, rightWall };
				this.allWalls = new ArrayList<Wall>(Arrays.asList(walls3));
				this.level = 1;
			} else if (isBelowLeftPt) {
//	      		this.allWalls = [leftWall, rightWall, floorWall];
				Wall[] walls3 = { leftWall, rightWall, floorWall };
				this.allWalls = new ArrayList<Wall>(Arrays.asList(walls3));
				this.level = -1;
			} else {
//	      		this.allWalls = [leftWall, rightWall];
				Wall[] walls3 = { leftWall, rightWall };
				this.allWalls = new ArrayList<Wall>(Arrays.asList(walls3));
				this.level = 0;
			}
		} // constructor BuildingBlock

		public Wall createWall2(Vector1[] pts, Color color, boolean hasWindows) {
			return new Wall(pts, color, hasWindows);
		}

		public Line getCornerLine(float ptX, Line line, Vector1 perspectivePt) {
			Line[] boundLines = { new Line(line.p0, perspectivePt), new Line(line.p1, perspectivePt) };

//		    let intersections = boundLines.map( (boundLine) => getIntersectionPtWithConstant(ptX, boundLine) );
//		    return new Line(...intersections);

			ArrayList<Vector1> intersct = new ArrayList<Vector1>();

			for (Line bound1 : boundLines) {
				Vector1 v2 = getIntersectionPtWithConstant(ptX, bound1);
				intersct.add(v2);
			}

			return new Line(intersct.get(0), intersct.get(1));
		}

		public Vector1 getIntersectionPt(Line line0, Line line1) {
			float x = (line1.yIntercept - line0.yIntercept) / (line0.slope - line1.slope);
			float y = line0.slope * x + line0.yIntercept;
			// createVector
			return new Vector1(x, y);
		}

		public void draw() {

//			println("building ind:" + this.indx);
			stroke(255, 0, 0);
			strokeWeight((float) 0.5);

			for (Wall wall : this.allWalls) {
				stroke(0);
				wall.draw(this);
			}
		} // draw

		class Wall {
			Vector1[] allPoints;
			ArrayList<Vector1[]> allWindows = new ArrayList<Vector1[]>();
			Color color;

			public Wall(Vector1[] points, Color c, boolean hasWindows) {
				this.allPoints = points;
				this.color = c;

				// reset new arrays
//			    this.allWindows = [];
				this.allWindows = new ArrayList<Vector1[]>();

				if (hasWindows) {
					// calculate window parameters (size is based on how close wall is to center)
					float heightL = abs(this.allPoints[1].y - this.allPoints[0].y);
					float heightR = abs(this.allPoints[2].y - this.allPoints[3].y);
					boolean isHeightLMax = heightL > heightR;
					float heightMax;
					float posXMax;
					if (isHeightLMax) {
						heightMax = heightL;
						posXMax = this.allPoints[0].x;
					} else {
						heightMax = heightR;
						posXMax = this.allPoints[2].x;
					}

					float windowSizeScalar = 1 - abs(posXMax - width / 2) / (width / 2);
					float windowHeight = windowSizeScalar * gWindowHeight;
					float windowLength = windowSizeScalar * gWindowLength;
					float windowSpacingX = windowSizeScalar * gWindowSpacingX;
					float windowSpacingY = windowSizeScalar * gWindowSpacingY;
					float ratioWindowToWall = windowHeight / heightMax;
					float windowCount = floor(heightMax / (windowHeight + windowSpacingY)) - 1;

					// Create Windows
					if (windowCount > 0) {
						float windowHeightL = isHeightLMax ? windowHeight : ratioWindowToWall * heightL;
						float windowHeightR = isHeightLMax ? ratioWindowToWall * heightR : windowHeight;

						float spaceAvailable = heightL - windowHeightL * windowCount;
						float extraInc = spaceAvailable / windowCount;

						float percentInc = (extraInc + windowHeightL) / heightL;
						float percent = (float) (0.5 * extraInc) / heightL;
						float wallDistance = this.allPoints[3].x - this.allPoints[0].x;
						float initPos = (float) (this.allPoints[3].x - windowSpacingY * 0.5);
						float wallLength = abs(wallDistance);
						float sign = wallDistance < 0 ? 1 : -1;

						float windowColCount = floor(wallLength / (windowLength + windowSpacingX)) - 2;
						float windowSpaceAvailable = wallLength - windowColCount * windowLength;
						float windowInc = windowSpaceAvailable / windowColCount + windowLength;

						for (int i = 0; i < windowCount; i++) {
							float adj1y = lerp(this.allPoints[1].y, this.allPoints[0].y, percent);
							float adj0y = adj1y - windowHeightL;
							float adj2y = lerp(this.allPoints[2].y, this.allPoints[3].y, percent);
							float adj3y = adj2y - windowHeightR;

							// new Line(createVector(this.allPoints[0].x, adj0y),
							// createVector(this.allPoints[3].x, adj3y));
							Line topWindowLine = new Line(new Vector1(this.allPoints[0].x, adj0y),
									new Vector1(this.allPoints[3].x, adj3y));

							// new Line(createVector(this.allPoints[2].x, adj2y),
							// createVector(this.allPoints[1].x, adj1y));
							Line bottomWindowLine = new Line(new Vector1(this.allPoints[2].x, adj2y),
									new Vector1(this.allPoints[1].x, adj1y));
							this.createWindow(topWindowLine, bottomWindowLine, windowLength, windowColCount, windowInc,
									sign, initPos);
							percent += percentInc;
						} // for
					} // if
				} // if
			} // constructor Wall

			public void createWindow(Line topWindowLine, Line bottomWindowLine, float windowSize, float windowColCount,
					float windowInc, float sign, float initPos) {
				for (int j = 0; j < windowColCount; j++) {
					float position0 = sign * j * windowInc + initPos;
					float position1 = position0 + sign * windowSize;
					Vector1 wpt0 = getIntersectionPtWithConstant(position0, bottomWindowLine);
					Vector1 wpt1 = getIntersectionPtWithConstant(position0, topWindowLine);
					Vector1 wpt2 = getIntersectionPtWithConstant(position1, topWindowLine);
					Vector1 wpt3 = getIntersectionPtWithConstant(position1, bottomWindowLine);

//				      this.allWindows.push([wpt0, wpt1, wpt2, wpt3]);
					Vector1[] vp2 = { wpt0, wpt1, wpt2, wpt3 };
					this.allWindows.add(vp2);
				}
			} // createWindow

			public void draw(BuildingBlock blk) {

				Color col22 = Color.MAGENTA;

				if (blk.indx == 10) {
					Color col21 = new Color(179, 172, 210, 1);
					fill(col22.getRGB());
				} else {
					fill(this.color.getRGB());
				}
				noStroke();
				quad(this.allPoints[0].x, this.allPoints[0].y, this.allPoints[1].x, this.allPoints[1].y,
						this.allPoints[2].x, this.allPoints[2].y, this.allPoints[3].x, this.allPoints[3].y);

				// if blk.indx == 10 for debugging purposes for graphics
				if (blk.indx == 10) {
					Color col21 = new Color(80, 154, 127);
					fill(col21.getRGB());
					stroke(col21.getRGB());
//					fill(hue((int) this.color), saturation((int) this.color), (float) (brightness((int) this.color) ),
//							(float) 0.5);
//					stroke(hue((int) this.color), saturation((int) this.color),
//							(float) (brightness((int) this.color) * 0.8), (float) 0.5);

				} else {
					Color col21 = new Color(80, 154, 127);
					fill(col21.getRGB());
					stroke(col21.getRGB());
//					fill(hue((int) this.color), saturation((int) this.color), (float) (brightness((int) this.color) ),
//							(float) 0.5);
//					stroke(hue((int) this.color), saturation((int) this.color),
//							(float) (brightness((int) this.color) * 0.8), (float) 0.5);

				}

				for (Vector1[] window : this.allWindows) {
					quad(window[0].x, window[0].y, window[1].x, window[1].y, window[2].x, window[2].y, window[3].x,
							window[3].y);
				} // for

			} // draw

		} // Wall
	}
	/**
	 * converts from hsl color scheme to rgb. Works some what
	 * 
	 * @param hsl
	 * @return
	 */
	public static int hslToRgb(float[] hsl) {
		float h = hsl[0];
		float s = hsl[1];
		float l = hsl[2];

		float c = (1 - Math.abs(2.f * l - 1.f)) * s;
		float h_ = h / 60.f;
		float h_mod2 = h_;
		if (h_mod2 >= 4.f)
			h_mod2 -= 4.f;
		else if (h_mod2 >= 2.f)
			h_mod2 -= 2.f;

		float x = c * (1 - Math.abs(h_mod2 - 1));
		float r_, g_, b_;
		if (h_ < 1) {
			r_ = c;
			g_ = x;
			b_ = 0;
		} else if (h_ < 2) {
			r_ = x;
			g_ = c;
			b_ = 0;
		} else if (h_ < 3) {
			r_ = 0;
			g_ = c;
			b_ = x;
		} else if (h_ < 4) {
			r_ = 0;
			g_ = x;
			b_ = c;
		} else if (h_ < 5) {
			r_ = x;
			g_ = 0;
			b_ = c;
		} else {
			r_ = c;
			g_ = 0;
			b_ = x;
		}

		float m = l - (0.5f * c);
		int r = (int) ((r_ + m) * (255.f) + 0.5f);
		int g = (int) ((g_ + m) * (255.f) + 0.5f);
		int b = (int) ((b_ + m) * (255.f) + 0.5f);
		return r << 16 | g << 8 | b;
	}

}
