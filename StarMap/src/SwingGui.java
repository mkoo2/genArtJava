import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SwingGui {

	private JFrame frame;

	public SwingGui(StarMapSketch sketch) {
		frame = new JFrame("Controls");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();

		JCheckBox colorConstel2 = new JCheckBox("Color constellation?", true);
		colorConstel2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JCheckBox cb = (JCheckBox) e.getSource();
				if (cb.isSelected()) {
					// do something if check box is selected
					sketch.setColorConstellations(true);
				} else {
					// check box is unselected, do something else
					sketch.setColorConstellations(false);
				}

				sketch.recolorMap();
			}
		});
		panel.add(colorConstel2);

		JCheckBox autoRotateCheck1 = new JCheckBox("Auto-rotate?", sketch.isAutoRotation());
		autoRotateCheck1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JCheckBox cb = (JCheckBox) e.getSource();
				if (cb.isSelected()) {
					// do something if check box is selected
					sketch.setAutoRotation(true);
				} else {
					// check box is unselected, do something else
					sketch.setAutoRotation(false);
				}

			}
		});
		panel.add(autoRotateCheck1);

		JCheckBox showAxis1 = new JCheckBox("Show Axis?", sketch.isShowAxis());
		showAxis1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JCheckBox cb = (JCheckBox) e.getSource();
				if (cb.isSelected()) {
					// do something if check box is selected
					sketch.setShowAxis(true);
				} else {
					// check box is unselected, do something else
					sketch.setShowAxis(false);
				}

			}
		});
		panel.add(showAxis1);

		JButton recenterBtn2 = new JButton("Re-center");
		recenterBtn2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sketch.recenterMap();
			}
		});

		panel.add(recenterBtn2);

		JButton zoominBtn1 = new JButton("Zoom in");
		zoominBtn1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sketch.zoomIn();
			}
		});

		panel.add(zoominBtn1);

		JButton zoominBtn3 = new JButton("Zoom out");
		zoominBtn3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sketch.zoomOut();
			}
		});

		panel.add(zoominBtn3);
		
		JButton hemiBtn1 = new JButton("North/South");
		hemiBtn1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (sketch.isNorthHemisphere()) {
					sketch.setNorthHemisphere(false);
				} else {
					sketch.setNorthHemisphere(true);
				}
				
				sketch.switchHemisphere();
			}
		});
		
		panel.add(hemiBtn1);

		JButton alignStarsBtn2 = new JButton("Align stars");
		alignStarsBtn2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (sketch.isAlignStars()) {
					sketch.setAlignStars(false);
				} else {
					sketch.setAlignStars(true);
				}
				
				sketch.recolorMap();
			}
		});
		
		panel.add(alignStarsBtn2);
		
		String[] constellMenu = {  "ALL", "ARI", "TAU", "GEM", "CAN", "LEO", "VIR", "LIB", "SCO", "CAP", "AQR", "PSC", "SGR", "UMI",
				"UMA"};

	    final JComboBox<String> cb = new JComboBox<String>(constellMenu);

	    cb.setVisible(true);
	    panel.add(cb);
	    
		JButton goConstellBtn1 = new JButton("Go Constel");
		goConstellBtn1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String constellb = (String)cb.getSelectedItem();
				sketch.setSelectedConstellation(constellb);
				
				sketch.recolorMap();
			}
		});
		
		panel.add(goConstellBtn1);
		
		frame.add(panel);

		frame.setSize(300, 300);
	}

	public void show() {
		frame.setVisible(true);
	}
}