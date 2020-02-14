package swingtasks;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.concurrent.atomic.AtomicReference;

public class JuliaSetProgram extends JFrame {

    public JuliaSetProgram()
    {
        JFrame frame = this;
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setResizable(true);

        float limit = 100;

        JuliaSetGraphPanel jsPanel = new JuliaSetGraphPanel(frame.getWidth(), frame.getHeight());

        Dimension baseSize =  jsPanel.getSize();
        jsPanel.setPreferredSize(baseSize);
        JScrollPane scrollPane = new JScrollPane(jsPanel);

        Dimension scSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(scSize.width/2, scSize.height/2);
        scrollPane.setSize(frame.getWidth(), frame.getHeight()-200);

        int fields = 7;

        JPanel totalLayout = new JPanel(new GridLayout(2, 1));
        JPanel barsLayout = new JPanel(new GridLayout(fields, 1));
        JPanel labelsLayout = new JPanel(new GridLayout(fields, 2));
        JPanel labeledBarsLayout = new JPanel(new BorderLayout());

        JScrollBar a = new JScrollBar(JScrollBar.HORIZONTAL, 100, 0, 0, 400);
        JScrollBar b = new JScrollBar(JScrollBar.HORIZONTAL, 100, 0, 0, 400);
        JScrollBar i = new JScrollBar(JScrollBar.HORIZONTAL, 100, 0, 0, 255);
        JScrollBar z = new JScrollBar(JScrollBar.HORIZONTAL, 100, 0, 0, 800);

        AdjustmentListener abiz = adjustmentEvent -> {
            if (adjustmentEvent.getSource() == a)
                jsPanel.setA(adjustmentEvent.getValue()*.01);
            else if (adjustmentEvent.getSource() == b)
                jsPanel.setB(adjustmentEvent.getValue()*.01);
            else if (adjustmentEvent.getSource() == i)
                jsPanel.setMaxIterations(adjustmentEvent.getValue());
            else if (adjustmentEvent.getSource() == z)
            {
                jsPanel.setZoom((float) (adjustmentEvent.getValue() * .01));
                jsPanel.setPreferredSize(new Dimension((int) (baseSize.getWidth() * jsPanel.getZoom()), (int) (baseSize.getHeight() * jsPanel.getZoom())));
            }
            if (jsPanel.getMaxIterations() < limit) jsPanel.repaint();
        };

        a.addAdjustmentListener(abiz);
        b.addAdjustmentListener(abiz);
        i.addAdjustmentListener(abiz);
        z.addAdjustmentListener(abiz);

        JScrollBar hu = new JScrollBar(JScrollBar.HORIZONTAL, 50, 0, 0, 100);
        JScrollBar st = new JScrollBar(JScrollBar.HORIZONTAL, 80, 0, 0, 100);
        JScrollBar br = new JScrollBar(JScrollBar.HORIZONTAL, 80, 0, 0, 100);

        AdjustmentListener hsbListener = adjustmentEvent -> {
            if (adjustmentEvent.getSource() == hu)
                jsPanel.setHueMultiplier(adjustmentEvent.getValue()/100f);
            else if (adjustmentEvent.getSource() == st)
                jsPanel.setSaturationMultiplier(adjustmentEvent.getValue()/100.0f);
            else if (adjustmentEvent.getSource() == br)
                jsPanel.setBrightnessMultiplier(adjustmentEvent.getValue()/100.0f);
            if (jsPanel.getMaxIterations() < limit) jsPanel.repaint();
        };

        hu.addAdjustmentListener(hsbListener);
        hu.addAdjustmentListener(hsbListener);
        hu.addAdjustmentListener(hsbListener);

        labelsLayout.add(new JLabel("A")); labelsLayout.add(createSpinnerFrom(a)); barsLayout.add(a);
        labelsLayout.add(new JLabel("B")); labelsLayout.add(createSpinnerFrom(b));barsLayout.add(b);
        labelsLayout.add(new JLabel("Iterations")); labelsLayout.add(createSpinnerFrom(i)); barsLayout.add(i);
        labelsLayout.add(new JLabel("Zoom")); labelsLayout.add(createSpinnerFrom(z)); barsLayout.add(z);
        labelsLayout.add(new JLabel("Hue")); labelsLayout.add(createSpinnerFrom(hu)); barsLayout.add(hu);
        labelsLayout.add(new JLabel("Saturation")); labelsLayout.add(createSpinnerFrom(st)); barsLayout.add(st);
        labelsLayout.add(new JLabel("Brightness")); labelsLayout.add(createSpinnerFrom(br)); barsLayout.add(br);

        labeledBarsLayout.add(labelsLayout, BorderLayout.WEST);
        labeledBarsLayout.add(barsLayout, BorderLayout.CENTER);

        totalLayout.setLayout(new BorderLayout());
        totalLayout.add(labeledBarsLayout, BorderLayout.CENTER);
        JButton repaint = new JButton("REPAINT");
        totalLayout.add(repaint, BorderLayout.SOUTH);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                scrollPane.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()-160));
                totalLayout.setPreferredSize(new Dimension(frame.getWidth(), 160));
            }
        });

        JPanel totalerLayout = new JPanel(new BorderLayout());
        totalerLayout.add(scrollPane, BorderLayout.CENTER);
        totalerLayout.add(totalLayout, BorderLayout.SOUTH);

        repaint.addActionListener(e -> jsPanel.repaint());

        frame.add(totalerLayout);

        frame.getComponentListeners()[0].componentResized(null);
        for (Component component: barsLayout.getComponents())
            ((JScrollBar) component).setValue(((JScrollBar) component).getValue());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        repaint.doClick();
    }

    public static JSpinner createSpinnerFrom(JScrollBar scrollBar)
    {
        SpinnerModel model = new SpinnerNumberModel(
            scrollBar.getValue(),
            scrollBar.getMinimum(),
            scrollBar.getMaximum(),
    (scrollBar.getMaximum()-scrollBar.getMinimum())/300 + 1);
        JSpinner spinner = new JSpinner(model);

        spinner.addChangeListener(e -> scrollBar.setValue((int) model.getValue()));
        scrollBar.addAdjustmentListener(e -> model.setValue(e.getValue()));
        return spinner;
    }

    public static void main(String[] args)
    {
        new JuliaSetProgram();
    }
}
