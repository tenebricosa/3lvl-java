public interface Backbone {
    public double read_double(String str) throws Message;

    public String read_operator(String str) throws Message;

    public double add(double a, double b);

    public double sub(double a, double b);

    public double mul(double a, double b);

    public double div(double a, double b) throws Message;
}
