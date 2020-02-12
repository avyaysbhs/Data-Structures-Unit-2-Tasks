package swingtasks;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

public class JuliaSetProgram {

    public static void anchorComponent(Component component, Container container, GridBagLayout layout, GridBagConstraints gbc, int x, int y, int width, int height) {
        gbc.gridx = x;
        gbc.gridy = y;

        gbc.gridwidth = width;
        gbc.gridheight = height;

        layout.setConstraints(component, gbc);
        container.add(component);
    }

    public static void main(String[] args)
    {
        AtomicReference<Float> filterSaturation = new AtomicReference<>(.8f);
        AtomicReference<Float> filterBrightness = new AtomicReference<>(.8f);

        JFrame frame = new JFrame();
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setResizable(true);

        float limit = 100;

        JuliaSetGraphPanel jsPanel = new JuliaSetGraphPanel(frame.getWidth(), frame.getHeight(), c ->
        {
            float[] comp = new float[3];
            Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), comp);
            return Color.HSBtoRGB(comp[0], comp[1]* filterSaturation.get(), comp[2]* filterBrightness.get());
        });

        Dimension baseSize =  jsPanel.getSize();
        jsPanel.setPreferredSize(baseSize);
        JScrollPane scrollPane = new JScrollPane(jsPanel);
        Dimension scSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(scSize.width/2, scSize.height/2);
        scrollPane.setSize(frame.getWidth(), frame.getHeight()-200);

        JPanel totalLayout = new JPanel(new GridLayout(2, 1));
//        GridBagLayout bagLayout = new GridBagLayout();
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.weightx = 1;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel barsLayout = new JPanel(new GridLayout(6, 1));
        JPanel labelsLayout = new JPanel(new GridLayout(6, 1));
        JPanel labeledBarsLayout = new JPanel(new BorderLayout());

        JScrollBar a = new JScrollBar(JScrollBar.HORIZONTAL, 100, 0, 0, 400);
        a.addAdjustmentListener((adjustment) ->
        {
            jsPanel.setA(adjustment.getValue()*.01);
            if (jsPanel.getMaxIterations() < limit) jsPanel.repaint();
        });

        JScrollBar b = new JScrollBar(JScrollBar.HORIZONTAL, 100, 0, 0, 400);
        b.addAdjustmentListener((adjustment) ->
        {
            jsPanel.setB(adjustment.getValue()*.01);
            if (jsPanel.getMaxIterations() < limit) jsPanel.repaint();
        });

        JScrollBar i = new JScrollBar(JScrollBar.HORIZONTAL, 100, 0, 0, 255);
        i.addAdjustmentListener((adjustment) ->
        {
            jsPanel.setMaxIterations(adjustment.getValue());
            if (jsPanel.getMaxIterations() < limit) jsPanel.repaint();
        });

        JScrollBar z = new JScrollBar(JScrollBar.HORIZONTAL, 100, 0, 0, 800);
        z.addAdjustmentListener((adjustment) ->
        {
            jsPanel.setZoom((float) (adjustment.getValue() * .01));
            jsPanel.setPreferredSize(new Dimension((int) (baseSize.getWidth() * jsPanel.getZoom()), (int) (baseSize.getHeight() * jsPanel.getZoom())));
            if (jsPanel.getMaxIterations() < limit) jsPanel.repaint();
        });

        JScrollBar st = new JScrollBar(JScrollBar.HORIZONTAL, 80, 0, 0, 100);
        st.addAdjustmentListener((adjustment) ->
        {
            filterSaturation.set(adjustment.getValue() * .01f);
            if (jsPanel.getMaxIterations() < limit) jsPanel.repaint();
        });

        JScrollBar br = new JScrollBar(JScrollBar.HORIZONTAL, 80, 0, 0, 100);
        br.addAdjustmentListener((adjustment) ->
        {
            filterBrightness.set(adjustment.getValue() * .01f);
            if (jsPanel.getMaxIterations() < limit) jsPanel.repaint();
        });

//        gbc.anchor = GridBagConstraints.LINE_START;
//        anchorComponent(new JLabel("A"), barsLayout, bagLayout, gbc, 0, 0, 2, 1);
//        anchorComponent(new JLabel("B"), barsLayout, bagLayout, gbc, 0, 1, 2, 1);
//        anchorComponent(new JLabel("Iterations"), barsLayout, bagLayout, gbc, 0, 2, 2, 1);
//        anchorComponent(new JLabel("Zoom"), barsLayout, bagLayout, gbc, 0, 3, 2, 1);
//        anchorComponent(new JLabel("Saturation"), barsLayout, bagLayout, gbc, 0, 4, 2, 1);
//        anchorComponent(new JLabel("Brightness"), barsLayout, bagLayout, gbc, 0, 5, 2, 1);
//
//        anchorComponent(a, barsLayout, bagLayout, gbc, 1, 0, 8, 1);
//        anchorComponent(b, barsLayout, bagLayout, gbc, 1, 1, 8, 1);
//        anchorComponent(i, barsLayout, bagLayout, gbc, 1, 2, 8, 1);
//        anchorComponent(z, barsLayout, bagLayout, gbc, 1, 3, 8, 1);
//        anchorComponent(st, barsLayout, bagLayout, gbc, 1, 4, 8, 1);
//        anchorComponent(br, barsLayout, bagLayout, gbc, 1, 5, 8, 1);

        labelsLayout.add(new JLabel("A")); barsLayout.add(a);
        labelsLayout.add(new JLabel("B")); barsLayout.add(b);
        labelsLayout.add(new JLabel("Iterations")); barsLayout.add(i);
        labelsLayout.add(new JLabel("Zoom")); barsLayout.add(z);
        labelsLayout.add(new JLabel("Saturation")); barsLayout.add(st);
        labelsLayout.add(new JLabel("Brightness")); barsLayout.add(br);

        labeledBarsLayout.add(labelsLayout, BorderLayout.WEST);
        labeledBarsLayout.add(barsLayout, BorderLayout.CENTER);

        totalLayout.add(labeledBarsLayout);
        JButton repaint = new JButton("REPAINT");
        totalLayout.add(repaint);

        JPanel totalerLayout = new JPanel(new BorderLayout());
        totalerLayout.add(scrollPane, BorderLayout.CENTER);
        totalerLayout.add(totalLayout, BorderLayout.SOUTH);

        repaint.addActionListener(e -> jsPanel.repaint());

        frame.add(totalerLayout);
        jsPanel.setVisible(true);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
