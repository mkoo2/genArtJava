import java.awt.Color;
import java.util.ArrayList;
import processing.core.*;

/**
 * Explosion app from openprocessing
 */
public class ExplosionApp extends PApplet {

  ArrayList<particle> p = new ArrayList<particle>();

  public void settings() {
    size(1000, 800);
  }

  public static void main(String[] args) {
    String[] processingArgs = { "ExplosionApp" };
    PApplet mySketch = new ExplosionApp();
    PApplet.runSketch(processingArgs, mySketch);
  }

  public void setup() {
    background(0);
    frameRate(120); // 120
    // fullScreen();
    // size(480, 360);

  }

  public void draw() {
    MousePressed();
    // background(0);
    fade();
    purge();

    for (int i = 0; i < p.size(); i++) {
      p.get(i).step();
      p.get(i).display();
    }
  }

  void MousePressed() {
    if (mousePressed) {
      for (int i = 0; i < 200; i++) {
        p.add(new particle());
      }
    }
  }

  void fade() {
    fill(0, 0, 0, 64);
    noStroke();
    rect(0, 0, width, height);
  }

  void purge() {
    for (int i = 0; i < p.size(); i++) {
      if (dist(0, 0, p.get(i).xv, p.get(i).yv) < 0.2 && p.get(i).y > height * 0.99 - p.get(i).size) {
        // System.out.println("remove 4342");
        p.remove(i);
      }
    }
  }

  class particle {

    float x = mouseX;
    float px = x;
    float y = mouseY;
    float py = y;
    float dir = random(0, TWO_PI);
    float mag = (5f * (((width / 1920f) * random(0, 5)) + 0.1f));
    float xv = mag * cos(dir);
    float yv = mag * sin(dir);
    float size = random(1, 10);
    float friction = 0.5f;
    float gravity = 0.2f;

    void display() {
      strokeWeight(size);
      // stroke(Color.CYAN.getRGB()); // 255
      stroke(Color.GREEN.getRGB()); // 255
      line(px, py, x, y);
    }

    void step() {
      px = x;
      py = y;
      x += xv;
      y += yv;
      yv += gravity;
      // right
      if (x > width - (size / 2)) {
        x -= xv;
        xv = -friction * abs(xv);
        yv *= friction;
        while (x > width - (size / 2)) {
          x -= 0.1;
        }
      }
      // left
      if (x < 0 + (size / 2)) {
        x -= xv;
        xv = friction * abs(xv);
        yv *= friction;
        while (x < 0 + (size / 2)) {
          x += 0.1;
        }
      }
      // bottom
      if (y > height - (size / 2)) {
        y -= yv;
        yv = -friction * abs(yv);
        xv *= friction;
        while (y > height - (size / 2)) {
          y -= 0.1;
        }
      }
      // top
      if (y < 0 + (size / 2)) {
        y -= yv;
        yv = friction * abs(yv);
        xv *= friction;
        while (y < 0 + (size / 2)) {
          y += 0.1;
        }
      }
    }
  } // class
}