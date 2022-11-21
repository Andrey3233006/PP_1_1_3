package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
public class UserDaoJDBCImpl extends Util implements UserDao {


//    Создание таблицы для User(ов) – не должно приводить к исключению, если такая таблица уже существует
//    Удаление таблицы User(ов) – не должно приводить к исключению, если таблицы не существует
//    Очистка содержания таблицы
//    Добавление User в таблицу
//    Удаление User из таблицы ( по id )
//    Получение всех User(ов) из таблицы



    //

    public UserDaoJDBCImpl() throws SQLException {
    }


    @Override
    public void createUsersTable() throws SQLException {

        try (Statement statement = Util.getConnection().createStatement()) {

            String createSql = "CREATE TABLE IF NOT EXISTS users (id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR (50) NOT NULL, lastName VARCHAR (50) NOT NULL, age TINYINT NOT NULL)";
            statement.execute(createSql);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dropUsersTable() throws SQLException {

        try (Statement statement = Util.getConnection().createStatement()) {

            String dropSql = "DROP TABLE IF EXISTS users";
            statement.execute(dropSql);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void saveUser(String name, String lastName, byte age) throws SQLException {

        String saveSql = "INSERT INTO users ( NAME, LASTNAME, AGE) VALUES ( ?, ?, ?)";

        try (PreparedStatement preparedStatement = Util.getConnection().prepareStatement(saveSql)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);

            preparedStatement.executeUpdate();

            System.out.println("User "+ name + " успешно добавлен в базу данных.");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeUserById ( long id) throws SQLException {

            String removeSql = "DELETE FROM users WHERE ID = ?";

            try (PreparedStatement preparedStatement = Util.getConnection().prepareStatement(removeSql)) {

                preparedStatement.setLong(1,id);
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }

    @Override
    public List<User> getAllUsers () throws SQLException {
        List<User> userList = new ArrayList<>();
        String getAllSql = "SELECT id, name, lastname, age FROM users";

        try (PreparedStatement preparedStatement = Util.getConnection().prepareStatement(getAllSql)) {
            ResultSet resultSet = preparedStatement.executeQuery(getAllSql);
            while (resultSet.next()) {

                User users = new User();
                users.setId(resultSet.getLong("id"));
                users.setName(resultSet.getString("name"));
                users.setLastName(resultSet.getString("lastName"));
                users.setAge( resultSet.getByte("age"));

                userList.add(users);
            }

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        System.out.println(userList);
        return userList;
    }

    @Override
    public void cleanUsersTable () throws SQLException {

        String cleanSql = "TRUNCATE users";
        try (Statement statement = Util.getConnection().prepareStatement(cleanSql)) {
            statement.executeUpdate(cleanSql);

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}

