package logic;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


@Entity
@Table(name = "bio")
public class Bio {
    public int id;
    public int year;
    public int month;
    public String text;

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id")
    public int getId() {
        return id;
    }

    @Column(name = "year")
    public int getYear() {
        return year;
    }

    @Column(name = "month")
    public int getMonth() {
        return month;
    }

    @Column(name = "text")
    public String getText() {
        return text;
    }

    public void setId(int i) {
        id = i;
    }

    public void setYear(int y) {
        year = y;
    }

    public void setMonth(int m) {
        month = m;
    }

    public void setText(String t) {
        text = t;
    }

}