package unititled3;

public class Function {

    private double period = 2 * Math.PI;

    public double detValue(double arg) {
        return Math.cos(2 * arg) + Math.sin(5 * arg);
    }
    public double getPeriod() {
        return period;
    }
}
