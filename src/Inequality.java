public class Inequality{
    public static final int GREATERTHAN = 1;
    public static final int LESSTHAN = -1;
    private RealFunction function;
    private int state;


    // x^3-2 lessthan => y > x^3-2

    public Inequality(RealFunction function, int state) {
        this.function = function;
        this.state = state;
    }

    public RealFunction getFunction() {
        return function;
    }

    public void setFunction(RealFunction function) {
        this.function = function;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}

