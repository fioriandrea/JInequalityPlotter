import org.mariuszgromada.math.mxparser.Function;

import javax.swing.*;

public class MainController {
    private JFrame frame;

    private JGraphPlotter plotter;
    private DefaultGraphPlotterModel model;

    private JMenuBar bar;
    private JMenu menu;

    private void initializeJFrame() {
        frame = new JFrame("JInequalityPlotter");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initializeGraphModel() {
        model = new DefaultGraphPlotterModel();
    }

    private void initializeGraphView(DefaultGraphPlotterModel model) {
        plotter = new JGraphPlotter(model);
    }

    private void initializeMenus() {
        bar = new JMenuBar();
        menu = new JMenu("menu");
        JMenuItem addFunction = new JMenuItem("Add Function");
        addFunction.addActionListener((e) -> {
            String fString = JOptionPane.showInputDialog(frame, "Insert a new function to draw");
            Function function = new Function("f(x) = " + fString);

            if (function.checkSyntax())
                model.addFunction((x) -> function.calculate(x));
            else
                JOptionPane.showMessageDialog(frame, "Invalid function");
        });
        menu.add(addFunction);
        JMenuItem addInequality = new JMenuItem("Add Inequality");
        addInequality.addActionListener((e) -> {
            Object[] opt = new Object[]{"Below", "Above"};
            String fString = JOptionPane.showInputDialog(frame, "Insert the function");
            Function function = new Function("f(x) = " + fString);

            if (function.checkSyntax()) {
                int k = JOptionPane.showOptionDialog(frame, "Color below or above?", "",
                        JOptionPane.PLAIN_MESSAGE, 1, null, opt, 0);
                model.addInequality(new Inequality((x) -> function.calculate(x), k == 0 ?
                        Inequality.GREATERTHAN : Inequality.LESSTHAN));
            }

            else
                JOptionPane.showMessageDialog(frame, "Invalid function");
        });

        menu.add(addInequality);

        JMenuItem reset = new JMenuItem("Reset");

        reset.addActionListener((e) -> {
            model.removeAllFunctions();
            model.removeAllInequalities();
        });

        menu.add(reset);

        JMenuItem setMax = new JMenuItem("Set MaxValue");
        setMax.addActionListener((e) -> {
            String maxStr = JOptionPane.showInputDialog(frame, "insert maxValue");

            try {
                int k = Math.abs(Integer.parseInt(maxStr));
                plotter.setMaxValue(k);
            } catch (NumberFormatException exc) {
                JOptionPane.showMessageDialog(frame, "Invalid number");
            }
        });

        menu.add(setMax);

        bar.add(menu);
    }

    private void placeComponents() {
        frame.add(plotter);
        frame.setJMenuBar(bar);
    }

    public MainController() {
        initializeJFrame();
        initializeGraphModel();
        initializeGraphView(model);
        initializeMenus();
        placeComponents();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new MainController();
    }
}
