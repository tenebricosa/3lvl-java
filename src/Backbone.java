/**
 * Created with IntelliJ IDEA.
 * User: пользователь
 * Date: 09.10.13
 * Time: 21:23
 * To change this template use File | Settings | File Templates.
 */
public interface Backbone {
    public double read_double(String str) throws Message;

    public String read_operator(String str) throws Message;

    public double add(double a, double b);

    public double sub(double a, double b);

    public double mul(double a, double b);

    public double div(double a, double b) throws Message;
}
