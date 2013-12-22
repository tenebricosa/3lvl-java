package DAO;

import logic.Bio;

import java.sql.SQLException;
import java.util.List;

public interface BioDAO {
    public void addBio(Bio Bio) throws SQLException;   //добавить студента
    public void updateBio(Bio Bio) throws SQLException;//обновить студента
    public Bio getBioById(int id) throws SQLException;    //получить стедента по id
    public List getAllBios() throws SQLException;              //получить всех студентов
    public void deleteBio(Bio Bio) throws SQLException;//удалить студента
}