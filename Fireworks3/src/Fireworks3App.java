import processing.core.*;
import java.util.*;
import java.awt.*;

public class Fireworks3App extends PApplet {
  ArrayList<particle> p = new ArrayList<particle>();
  float hu = 0;
  boolean allow = false;

  Color[] COLORS45 = { Color.RED, Color.ORANGE, Color.PINK, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.WHITE };

	static final int winWidth = 1600;
	static final int winHeight = 1000;
  int index3 = 0;
  Color color7;

  public void setup() {
//    size(1112, 834);
    hu = random(360);
//    colorMode(HSB);
    background(0);
    frameRate(60);
    allow = true;
  }

	public void settings() {
		size(winWidth, winHeight);
	}


	public static void main(String[] args) {
		String[] processingArgs = { "Fireworks3App" };
		PApplet mySketch = new Fireworks3App();
		PApplet.runSketch(processingArgs, mySketch);
	}

  public void draw() {
//    MousePressed();
//     background(0);
    fade();
    purge();
    allow = true;

    for (int i = 0; i < p.size(); i++) {
      p.get(i).step();
      p.get(i).display();
    }
  }

  public void MousePressed() {
    if (mousePressed) {
      for (int i = 0; i < 200; i++) {
        p.add(new particle(index3++));
      }
    }
  }

	@Override
	public void mouseClicked() {
//    System.out.println("click135");

    int colorVal = (int) random(0, 6);
    Color color23 = COLORS45[colorVal];
    color7 = color23;

    for (int i = 0; i < 200; i++) {
      p.add(new particle(index3++));
    }
}

  void fade() {
    fill(0, 0, 0, 20);
    noStroke();
    rect(0, 0, width, height);
  }

  void purge() {
//    System.out.println("purge363:");
    for (int i = 0; i < p.size(); i++) {
      particle part3 = p.get(i);

//        System.out.println("part313:" + dist(0, 0, part3.xv, part3.yv) + " " +  part3.y + " " + height);
      if (dist(0, 0, part3.xv, part3.yv) < 0.2 || 
          part3.y > height * 0.99 - part3.size) {
//        System.out.println("part343 removal:" + part3.getIndex());
        p.remove(i);
      }
    }
    // if (p.size() > 0) {
    //   System.out.println("part373:" + p.size() );
    // }
  }

  class particle {

    float x = mouseX;
    float px = x;
    float y = mouseY;
    float py = y;
    float dir = random(0, TWO_PI);
    float mag = 0; // (5 * (((width / 1920f) * random(0, 5)) + 0.5f));  // 0.1f
    float xv = 0; //mag * cos(dir);
    float yv = 0; //mag * sin(dir);
    float size = random(1, 10);
    float friction = 0.1f;
    float gravity = 0.13f; // 0.2f
    float color5 = color(hu, random(150, 255), 255);
    Color color6;
    int index3;

    public particle (int indx) {
      this.index3 = indx;
      
      color6 = color7;
      
      float rand34 = random(0, 3);//5
      mag = 5 * (((width / 1920f) * rand34) + 0.1f);  // 0.1f
      //System.out.println("mag347:" + mag + " " + rand34);
      xv = mag * cos(dir);
      yv = mag * sin(dir);
        
    }

    void display() {
      strokeWeight(size);
//       stroke(this.color5);
      //Color colr = Color.CYAN;
       stroke(color6.getRGB());
      // System.out.println("line343:" + index3 + " " + px + " " + py + " " + x + " " + y);
      line(px, py, x, y);
    }

    void step() {
      px = x;
      py = y;
      x += xv;
      y += yv;
      yv += gravity;

    }

    public int getIndex() {
      return this.index3;
    }
  }
}