package swingtasks;

import sun.java2d.SunGraphics2D;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

@SuppressWarnings("SpellCheckingInspection")
public class JuliaSetGraphPanel extends JPanel {

    private final BufferedImage bufferedImage;
    private ColorFilter filter;

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    public double getZoom() {
        return zoom;
    }

    private double zoom;

    public static interface ColorFilter
    {
        int filter(Color in);
    }

    public float getMaxIterations() {
        return maxIterations;
    }

    public void setMaxIterations(float maxIterations) {
        this.maxIterations = maxIterations;
    }

    private float maxIterations;
    private double A, B;

    public JuliaSetGraphPanel(int width, int height, int maxIterations, ColorFilter filter)
    {
       // super();
        this.setSize(width, height);
        System.out.println(width+" "+height);
        this.bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.maxIterations = maxIterations;
        this.filter = filter;
        this.zoom = 1;
    }

    public JuliaSetGraphPanel(int width, int height) { this(width, height, 50, null); }
    public JuliaSetGraphPanel(int width, int height, ColorFilter filter) { this(width, height, 50, filter); }

    private static double sqr_sum(double a, double b)
    {
        return (a * a) + (b * b);
    }

    private static double sqr_diff(double a, double b)
    {
        return (a * a) - (b * b);
    }

    private void redraw()
    {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        final double zoom = this.zoom;

        double zx, zy;

        for (int x=0;x<width;x++)
            for (int y=0;y<height;y++)
            {
                zx = 1.5 * ((x - .5 * width)/(.5 * zoom * width));
                zy = ((y - .5 * height)/(.5 * zoom * height));
                float iter = maxIterations;

                while (iter > 0 && sqr_sum(zx, zy) < 6)
                {
                    double q = sqr_diff(zx, zy) + A;
                    zy = 2*zx*zy + B;
                    zx = q;
                    iter--;
                }

                int c;
                if (iter > 0)
                    c = Color.HSBtoRGB((maxIterations / iter) % 1, 1, 1);
                else c = Color.HSBtoRGB(maxIterations / iter, 1, 0);

                bufferedImage.setRGB(x, y, filter == null ? c : filter.filter(new Color(c)));
            }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        redraw();
        g.drawImage(bufferedImage, 0, 0, (img, infoflags, x, y, width1, height1) -> true);
    }

    public void setA(double a) {
        A = a;
    }

    public void setB(double b) {
        B = b;
    }

    public String toString()
    {
        return String.format("A=%f, B=%f, I=%f", A, B, maxIterations);
    }
}
