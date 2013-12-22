public class Bio {
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