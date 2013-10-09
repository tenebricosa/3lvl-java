/**
 * Created with IntelliJ IDEA.
 * User: пользователь
 * Date: 10.10.13
 * Time: 0:32
 * To change this template use File | Settings | File Templates.
 */
public class ServerCalculator extends Server {
    public static void main(String[] args) {
        Server server = new ServerCalculator();
        server.start();
    }

    @Override
    public void do_it() throws Exception {
        Calculator calc = new Calculator();

        while (true) {
            double o1;
            double o2;

            out.writeUTF("Введите первый оператор");
            while (1 == 1) {
                String line = in.readUTF() ;
                try {
                    o1 = calc.read_double(line);
                    break;
                } catch (Message m) {
                    out.writeUTF(m.toString());
                }
            }
            out.writeUTF("Введите второй оператор");
            while (1 == 1) {
                String line = in.readUTF() ;
                try {
                    o2 = calc.read_double(line);
                    break;
                } catch (Message m) {
                    out.writeUTF(m.toString());
                }
            }

            out.writeUTF("Введите операцию");

            String operator;
            while (1 == 1) {
                String line = in.readUTF() ;
                try {
                    operator = calc.read_operator(line);
                    break;
                } catch (Message m) {
                    out.writeUTF(m.toString());
                }
            }
            double result;
            try {
                result = calc.get_result(o1, o2, operator);
                out.writeUTF(o1 + " " + operator + " " + o2 + " = " + result);
            } catch (Message m) {
                out.writeUTF(m.toString());
            }
        }
    }
}
