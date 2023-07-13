package dao;

import dto.UserDTO;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAOImpl implements UserDAO {

    @Override
    public ArrayList<UserDTO> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<UserDTO> all = new ArrayList<>();
        String sql = "SELECT * FROM user";
        ResultSet resultSet = CrudUtil.execute(sql);
        while (resultSet.next()) {
            String id = resultSet.getString("id");
            String frist_namename = resultSet.getString("frist_name");
            String last_name = resultSet.getString("last_name");
            String password = resultSet.getString("password");


            UserDTO userDTO = new UserDTO(id, frist_namename+last_name, password);
            all.add(userDTO);
        }
        return all;
    }

    @Override
    public boolean add(UserDTO userDTO) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO user (id, frist_name, last_name, password) " +
                "VALUES (?, ?, ?, ?)";
        int rowsAffected = CrudUtil.execute(sql,userDTO.getId(),userDTO.getName(),userDTO.getName(),userDTO.getPassword());
        return rowsAffected > 0;
    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException {
        String sql = "SELECT MAX(id) FROM user";
        ResultSet resultSet = CrudUtil.execute(sql);
        if (resultSet.next()) {
            String maxID = resultSet.getString(1);
            if (maxID != null) {
                int idNumber = Integer.parseInt(maxID.substring(1)) + 1;
                String newID = "U" + String.format("%04d", idNumber);
                return newID;
            } else {
                return "U0001"; // If no existing IDs, start from E0001
            }
        }
        return null;
    }
}
