import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;

public class DefaultGraphPlotterModel implements GraphPlotterModel{
    private ArrayList<Inequality> inequalities;
    private ArrayList<RealFunction> functions;
    private ArrayList<ChangeListener> listeners;

    public DefaultGraphPlotterModel() {
        inequalities = new ArrayList<>();
        functions = new ArrayList<>();
        listeners = new ArrayList<>();
    }

    @Override
    public ArrayList<Inequality> getInequalities() {
        return inequalities;
    }

    @Override
    public ArrayList<RealFunction> getFunctions() {
        return functions;
    }

    @Override
    public void addFunction(RealFunction function) {
        functions.add(function);
        fireChangeEvent(new ChangeEvent(this));
    }

    @Override
    public void removeFunction(RealFunction function) {
        functions.remove(function);
        fireChangeEvent(new ChangeEvent(this));
    }

    @Override
    public void addInequality(Inequality inequality) {
        inequalities.add(inequality);
        fireChangeEvent(new ChangeEvent(this));
    }

    @Override
    public void removeInequality(Inequality inequality) {
        inequalities.remove(inequality);
        fireChangeEvent(new ChangeEvent(this));
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void addChangeListener(ChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public void fireChangeEvent(ChangeEvent e) {
        for (ChangeListener listener : listeners)
            listener.stateChanged(e);
    }

    public void removeAllFunctions() {
        functions = new ArrayList<>();
        fireChangeEvent(new ChangeEvent(this));
    }

    public void removeAllInequalities() {
        inequalities = new ArrayList<>();
        fireChangeEvent(new ChangeEvent(this));
    }

    public void reset() {
        inequalities = new ArrayList<>();
        functions = new ArrayList<>();
        listeners = new ArrayList<>();
    }
}
