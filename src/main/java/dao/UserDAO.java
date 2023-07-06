package dao;

import dto.UserDTO;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;

public interface  UserDAO {
    public ArrayList<UserDTO> getAll() throws SQLException, ClassNotFoundException;
    public boolean add(UserDTO employee) throws SQLException, ClassNotFoundException;
    public String generateNewID() throws SQLException, ClassNotFoundException;

}
