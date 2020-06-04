import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

public class FunctionDrawer {
    private int height;
    private int width;
    private int maxValue;
    private double dx;
    private HashMap<Integer, InequalityPerimeterGetter> inequalityBehaviors;

    public FunctionDrawer(int width, int height, int maxValue, double dx) {
        this.height = height;
        this.width = width;
        this.dx = dx;
        this.maxValue = maxValue;
        intializeInequalityBehaviors();
    }

    private void intializeInequalityBehaviors() {
        inequalityBehaviors = new HashMap<>();
        inequalityBehaviors.put(Inequality.GREATERTHAN, (f) -> {
            Point[] corners = getNormalizedCorners();

            Point bottomRight = corners[2];
            Point bottomLeft = corners[3];

            Polygon funcPath = getFunctionPolygon(f);

            funcPath.addPoint(bottomLeft.x, bottomLeft.y);
            funcPath.addPoint(bottomRight.x, bottomRight.y);


            return funcPath;
        });
        inequalityBehaviors.put(Inequality.LESSTHAN, (f) -> {
            Point[] corners = getNormalizedCorners();

            Point topLeft = corners[0];
            Point topRight = corners[1];

            Polygon funcPath = getFunctionPolygon(f);

            funcPath.addPoint(topRight.x, topRight.y);
            funcPath.addPoint(topLeft.x, topLeft.y);

            return funcPath;
        });
    }

    //scala e mette al centro
    private Point fromNormalToGraphCoordinates(Point2D.Double point) {
        double scalex = getWidth()/(2*maxValue);
        double scaley = getHeight()/(2*maxValue);
        int safeGuard = 10;

        int sx;
        int sy;

        try {
            sx = Math.toIntExact(Math.round(point.x * scalex));
            sy = Math.toIntExact(Math.round(point.y * scaley));
        }catch (ArithmeticException exc) {
            sx = getWidth()/2 + safeGuard;
            sy = getHeight()/2 + safeGuard;
        }

        if(sy > getHeight())
            sy = getHeight()/2 + safeGuard;
        if(sx > getWidth())
            sx = getWidth()/2 + safeGuard;

        sx = getWidth()/2 + sx;
        sy = getHeight()/2 - sy;

        return new Point(sx, sy);
    }

    private Point fromNormalToGraphCoordinates(double x, double y) {
        return fromNormalToGraphCoordinates(new Point2D.Double(x, y));
    }

    public ArrayList<Line2D.Double> getFunctionGraph(RealFunction function) {
        ArrayList<Line2D.Double> curve = new ArrayList<>();
        for(double i = -maxValue; i < maxValue; i+=dx) {
            double x = i;
            double y = function.calculateIn(x);
            double xp = (i-dx);
            double yp = function.calculateIn(xp);

            Point graphEndingPoint = fromNormalToGraphCoordinates(xp, yp);
            Point graphStartingPoint = fromNormalToGraphCoordinates(x, y);
            curve.add(new Line2D.Double(graphStartingPoint.x, graphStartingPoint.y,
                    graphEndingPoint.x, graphEndingPoint.y));
        }

        return curve;
    }

    public ArrayList<Line2D.Double> getFunctionsGraph(RealFunction[] functions) {
        ArrayList<Line2D.Double> componentLines = new ArrayList<>();

        for(RealFunction function : functions)
            componentLines.addAll(getFunctionGraph(function));

        return componentLines;
    }

    private Point[] getNormalizedCorners() {
        Point bottomLeft = new Point(0, height);
        Point bottomRight = new Point(width, height);
        Point topLeft = new Point(0, 0);
        Point topRight = new Point(width, 0);

        return new Point[]{topLeft, topRight, bottomLeft, bottomRight};
    }

    public Polygon getFunctionPolygon(RealFunction f) {
        Polygon polygon = new Polygon();
        ArrayList<Line2D.Double> curve = getFunctionGraph(f);


        for(Line2D.Double componentLine : curve) {
            //son gia normalizzate a w/2 e a h/2
            int x1 = Math.toIntExact(Math.round(componentLine.x1));
            int x2 = Math.toIntExact(Math.round(componentLine.x2));
            int y1 = Math.toIntExact(Math.round(componentLine.y1));
            int y2 = Math.toIntExact(Math.round(componentLine.y2));
            polygon.addPoint(x1, y1);
            polygon.addPoint(x2, y2);
        }

        return polygon;
    }

    public Area getSystemArea(Inequality[] inequalities) {
        int w = getWidth();
        int h = getHeight();

        Area systemArea = new Area();

        if(inequalities.length > 0) {
            systemArea = new Area(new Rectangle(0, 0, w, h));

            for(Inequality inequality : inequalities) {
                RealFunction function = inequality.getFunction();
                Polygon funcPath = inequalityBehaviors.get(inequality.getState())
                        .getPerimeter(function);

                systemArea.intersect(new Area(funcPath));
            }
        }

        return systemArea;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }
}
