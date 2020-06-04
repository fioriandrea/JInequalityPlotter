import org.mariuszgromada.math.mxparser.Function;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;

public class JGraphPlotter extends JPanel {

    /**private HashMap<String, PropertyChangeListener> modelListeners;**/
    private GraphPlotterModel model;
    private FunctionDrawer drawer;

    public static final Color DEFAULT_CONSTRAINT_COLOR = new Color(255,175,175, 100);
    public static final int DEFAULT_MAXVALUE = 10;
    public static final double DEFAULT_DX = 0.01;

    private void setModelListeners() {
        model.addChangeListener((e) -> repaint());
    }

    private ArrayList<Shape> fetchCurvesFromModel() {
        ArrayList<Shape> shapesOnScreen = new ArrayList<>();
        //adds functions
        shapesOnScreen.addAll(drawer.getFunctionsGraph(model.getFunctions()
                .toArray(new RealFunction[0])));
        //adds constraint functions
        for(Inequality inequality : model.getInequalities())
            shapesOnScreen.addAll(drawer.getFunctionGraph(inequality.getFunction()));

        return shapesOnScreen;
    }

    private void drawAxis(Graphics2D g2) {
        g2.drawLine(getWidth()/2, 0, getWidth()/2, getHeight());
        g2.drawLine(0, getHeight()/2, getWidth(), getHeight()/2);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(drawer != null) {
            drawer.setWidth(getWidth());
            drawer.setHeight(getHeight());
        }
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        drawAxis(g2);

        //volendo si può spezzare in piu' metodi

        ArrayList<Shape> shapesOnScreen = fetchCurvesFromModel();
        //gets constraint area
        Area constraint = drawer.getSystemArea(model.getInequalities()
                .toArray(new Inequality[0]));

        //draws everything
        plotShapes(g2, shapesOnScreen);

        g2.setColor(DEFAULT_CONSTRAINT_COLOR);

        g2.fill(constraint);
    }

    private void plotShapes(Graphics2D g2, ArrayList<Shape> shapes) {
        for(Shape shape : shapes)
            g2.draw(shape);
    }

    public JGraphPlotter(GraphPlotterModel model) {
        super();
        this.model = model;
        setModelListeners();
        drawer = new FunctionDrawer(getWidth(), getHeight(), DEFAULT_MAXVALUE, DEFAULT_DX);
    }

    public void setDx(double dx) {
        drawer.setDx(dx);
        repaint();
    }

    public void setMaxValue(int maxValue) {
        drawer.setMaxValue(maxValue);
        repaint();
    }

    public double getDx() {
        return drawer.getDx();
    }

    public int getMaxValue() {
        return drawer.getMaxValue();
    }
}
