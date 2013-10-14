import java.util.Scanner;

public class Calculator implements Backbone {
    public static void main(String[] args) {
        Calculator calc = new Calculator();
        Scanner keyboard = new Scanner(System.in);

        double o1;
        double o2;

        System.out.println("Введите первое число");
        while (1 == 1) {
            String line = keyboard.next();
            try {
                o1 = calc.read_double(line);
                break;
            } catch (Message m) {
                System.out.println(m);
            }
        }
        System.out.println("Введите второе число");
        while (1 == 1) {
            String line = keyboard.next();
            try {
                o2 = calc.read_double(line);
                break;
            } catch (Message m) {
                System.out.println(m);
            }
        }

        System.out.println("Введите операцию");

        String operator;
        while (1 == 1) {
            String line = keyboard.next();
            try {
                operator = calc.read_operator(line);
                break;
            } catch (Message m) {
                System.out.println(m);
            }
        }
        double result;
        try {
            result = calc.get_result(o1, o2, operator);
            System.out.println(o1 + " " + operator + " " + o2 + " = " + result);
        } catch (Message m) {
            System.out.println(m);
        }

    }

    public double read_double(String str) throws Message {
        double d;
        try {
            d = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            throw new Message("Вводите нормальное число!");
        }
        return d;
    }

    public String read_operator(String str) throws Message {

        if (str.equals("+")) {
            return str;
        } else if (str.equals("-")) {
            return str;
        } else if (str.equals("*")) {
            return str;
        } else if (str.equals("/")) {
            return str;
        } else {
            throw new Message("Вводите правильное значение");  //код никогда не выполнится
        }
    }

    public double add(double a, double b) {
        return a + b;
    }

    public double sub(double a, double b) {
        return a - b;
    }

    public double mul(double a, double b) {
        return a * b;
    }

    public double div(double a, double b) throws Message {
        if (b != 0) {
            return a / b;
        } else {
            throw new Message("На ноль делить нельзя");
        }

    }

    public double get_result(double o1, double o2, String operator) throws Message {
        double result;
        if (operator.equals("+")) {
            result = add(o1, o2);
        } else if (operator.equals("-")) {
            result = sub(o1, o2);
        } else if (operator.equals("*")) {
            result = mul(o1, o2);
        } else if (operator.equals("/")) {
            result = div(o1, o2);
        } else {
            result = 0.0;
        }
        return result;
    }
}
