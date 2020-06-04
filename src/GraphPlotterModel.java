import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;

public interface GraphPlotterModel {
    ArrayList<Inequality> getInequalities();
    ArrayList<RealFunction> getFunctions();
    void addFunction(RealFunction function);
    void removeFunction(RealFunction function);
    void addInequality(Inequality inequality);
    void removeInequality(Inequality inequality);

    void removeChangeListener(ChangeListener listener);
    void addChangeListener(ChangeListener listener);
    void fireChangeEvent(ChangeEvent e);
    // newVal is an arrayList
}
