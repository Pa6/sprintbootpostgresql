/////////////created by PACODER //////////
/////////////JAVA SPRING BOOT - POSTGRESQL CRUD
package com.pacoder.crud.controller;

import com.pacoder.crud.model.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@RestController
public class WelcomeController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("welcome")
    public String welcomeFunction(){
        String welcome = "welcome on spring ";
        return welcome;
    }

    @RequestMapping(method = RequestMethod.GET, value = "get-users")
    public List<User> getUser(){

        ////build query
        String sql = "SELECT * from users";
        List<User> user = null;
        try{

            user = jdbcTemplate.query(sql, (rs, rowNumber) -> new User(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name")));

        }catch (Exception ex){
            ///catch error
            ex.getMessage();
        }

        /// retturn data
        return user;
    }

    @PostMapping("create-user")
    public String CreateUser(@RequestParam(name = "first_name") String first_name, @RequestParam(name = "last_name") String last_name) throws JSONException {
        String response = new JSONObject()
                .put("error", "an error happened")
                .put("status", "400")
                .toString();
        try {

            long unixTime = System.currentTimeMillis() / 1000L;

            KeyHolder holder = new GeneratedKeyHolder();
            String sql_data = "INSERT INTO users values (?,?,?)";
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public java.sql.PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    java.sql.PreparedStatement ps = connection.prepareStatement(sql_data, Statement.RETURN_GENERATED_KEYS);
                    ps.setInt(1, (int) unixTime);
                    ps.setString(2, first_name);
                    ps.setString(3, last_name);
                    return ps;
                }
            }, holder);

            response = new JSONObject()
                    .put("message", "User created successfully")
                    .put("status", "200")
                    .toString();

        }catch (Exception e){
            response = new JSONObject()
                    .put("error", "an error happened")
                    .put("status", "400")
                    .put("details", e.getMessage())
                    .toString();
        }
        return response;
    }


    @RequestMapping(method = RequestMethod.POST, value = "update-user")
    public String updateUser(@RequestParam(name = "first_name") String first_name,
                             @RequestParam(name = "last_name") String last_name,
                             @RequestParam(name = "id") int id
                             ) throws JSONException {
        String response = new JSONObject()
                .put("error", "an error happened")
                .put("status", "400")
                .toString();

        try {
            KeyHolder holder = new GeneratedKeyHolder();

            String sql_data = "UPDATE users SET   first_name = ?, last_name = ?  WHERE id = ?";
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public java.sql.PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    java.sql.PreparedStatement ps = connection.prepareStatement(sql_data, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, first_name);
                    ps.setString(2, last_name);
                    ps.setInt(3, id);

                    return ps;
                }
            }, holder);

            response = new JSONObject()
                    .put("message", "user updated successfully")
                    .put("status", "200")
                    .toString();
        }catch (Exception e){
            response = new JSONObject()
                    .put("message", "an error occurred while updating, try again")
                    .put("status", "400")
                    .put("details", e.getMessage())
                    .toString();
        }
        return response;
    }


    @RequestMapping(method = RequestMethod.POST, value = "delete-user")
    public String deleteUser(@RequestParam(name = "id") int id
    ) throws JSONException {
        String response = new JSONObject()
                .put("error", "an error happened")
                .put("status", "400")
                .toString();

        try {
            KeyHolder holder = new GeneratedKeyHolder();

            String sql_data = "DELETE FROM users  WHERE id = ?";
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public java.sql.PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    java.sql.PreparedStatement ps = connection.prepareStatement(sql_data, Statement.RETURN_GENERATED_KEYS);
                    ps.setInt(1, id);

                    return ps;
                }
            }, holder);

            response = new JSONObject()
                    .put("message", "user deleted successfully")
                    .put("status", "200")
                    .toString();
        }catch (Exception e){
            response = new JSONObject()
                    .put("message", "an error occurred while deleting, try again")
                    .put("status", "400")
                    .put("details", e.getMessage())
                    .toString();
        }
        return response;
    }



}