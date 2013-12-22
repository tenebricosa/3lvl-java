import freemarker.template.TemplateScalarModel;

/**
 * Created with IntelliJ IDEA.
 * User: пользователь
 * Date: 19.12.13
 * Time: 0:48
 * To change this template use File | Settings | File Templates.
 */
import freemarker.template.*;
public class Bio implements TemplateScalarModel {
    public int id;
    public int year;
    public int month;
    public String text;

    public Bio(int id, int year, int month, String text) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.text = text;
    }

    @Override
    public String getAsString() throws TemplateModelException {
        return id + " " + year + " " + month + " " + text;
    }


    public String getYear() {
        return "" + year;
    }

    public String getMonth() {
        return "" + month;
    }

    public String getText() {
        return text;
    }
}