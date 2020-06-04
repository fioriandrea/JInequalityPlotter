import java.awt.*;

@FunctionalInterface
public interface InequalityPerimeterGetter {
    Polygon getPerimeter(RealFunction function);
}
