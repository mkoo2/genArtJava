
public class StarMapMain {

	public static void main(String[] args){
		StarMapSketch sketch = new StarMapSketch();
		SwingGui swingGui = new SwingGui(sketch);

		sketch.run();
		swingGui.show();
	}	
	
}
