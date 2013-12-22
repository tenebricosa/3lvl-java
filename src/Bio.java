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

    public String getRepresentation() {
        return id + ":" + year + ":" + month + ":" + text;
    }

    static Bio parseBio(String str) {

        String[] b = str.split(":");
        int id = Integer.parseInt(b[0]);
        int year = Integer.parseInt(b[1]);
        int month = Integer.parseInt(b[2]);
        String text = b[3];
        return new Bio(id, year, month, text);
    }

    public String getId(){
        return ""+id;
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