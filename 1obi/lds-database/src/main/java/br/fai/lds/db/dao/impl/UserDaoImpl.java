package br.fai.lds.db.dao.impl;

import br.fai.lds.db.connection.ConnectionFactory;
import br.fai.lds.db.dao.UserDao;
import br.fai.lds.model.entities.UserModel;
import br.fai.lds.model.enums.UserType;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao<UserModel> {

    @Override
    public List<UserModel> find() {

        List<UserModel> items = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        final String sql = "SELECT * FROM usuario ;";

        try {
            connection = ConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                UserModel user = new UserModel();

                user.setId(resultSet.getInt("id"));

                user.setUsername(resultSet.getString("nome_usuario"));

                user.setFullName(resultSet.getString("nome_completo"));

                user.setEmail(resultSet.getString("email"));

                user.setActive(resultSet.getBoolean("esta_ativo"));

                user.setLastModified(resultSet.getTimestamp("ultima_modificacao"));

                String userType = resultSet.getString("tipo");
                user.setType(UserType.valueOf(userType));

                items.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(preparedStatement, connection, resultSet);
        }

        return items;
    }

    @Override
    public UserModel findById(int id) {
        UserModel item = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        final String sql = "SELECT * FROM usuario WHERE id = ?;";

        try {
            connection = ConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                item = new UserModel();

                item.setId(resultSet.getInt("id"));

                item.setUsername(resultSet.getString("nome_usuario"));

                item.setFullName(resultSet.getString("nome_completo"));

                item.setEmail(resultSet.getString("email"));

                item.setActive(resultSet.getBoolean("esta_ativo"));

                item.setLastModified(resultSet.getTimestamp("ultima_modificacao"));

                String userType = resultSet.getString("tipo");
                item.setType(UserType.valueOf(userType));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(preparedStatement, connection, resultSet);
        }

        return item;
    }

    @Override
    public int create(UserModel entity) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        int id = -1;

        String sql = "INSERT INTO usuario(nome_completo, ";
        sql += " senha, nome_usuario, email, tipo, ";
        sql += " esta_ativo, criado_em, criado_por, ";
        sql += " ultima_modificacao ";
        sql += " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try {
            connection = ConnectionFactory.getConnection();

            connection.setAutoCommit(false);

            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, entity.getFullName());
            preparedStatement.setString(2, entity.getPassword());
            preparedStatement.setString(3, entity.getUsername());
            preparedStatement.setString(4, entity.getEmail());
            preparedStatement.setString(5, UserType.USUARIO.toString());
            preparedStatement.setBoolean(6, true);
            preparedStatement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setString(8, entity.getUsername());
            preparedStatement.setTimestamp(9, new Timestamp(System.currentTimeMillis()));

            preparedStatement.execute();

            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }

            connection.commit();
            return id;
        } catch (SQLException e) {
            e.printStackTrace();

            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return id;
        } finally {
            ConnectionFactory.close(preparedStatement, connection, resultSet);
        }
    }

    @Override
    public boolean update(UserModel entity) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String sql = "UPDATE usuario SET nome_completo = ?, ";
        sql += " ultima_modificacao = ?, email = ? ";
        sql += " WHERE ";
        sql += " id = ? ;";

        try {
            connection = ConnectionFactory.getConnection();

            connection.setAutoCommit(false);

            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, entity.getFullName());

            preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));

            preparedStatement.setString(3, entity.getEmail());

            preparedStatement.setInt(4, entity.getId());

            preparedStatement.execute();
            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();

            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            ConnectionFactory.close(preparedStatement, connection);
        }
    }

    @Override
    public boolean deleteById(int id) {

        boolean result = false;

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        final String sql = "DELETE FROM usuario WHERE id = ?;";

        try {
            connection = ConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            connection.setAutoCommit(false);

            preparedStatement.execute();
            connection.commit();
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();

            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            ConnectionFactory.close(preparedStatement, connection);
        }

        return result;
    }

    @Override
    public UserModel validateUsernameAndPassword(String username, String password) {

        UserModel user = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        final String sql = "SELECT * FROM usuario WHERE nome_usuario = ? AND senha = ? ;";

        try {
            connection = ConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return user;
            }

            user = new UserModel();

            user.setId(resultSet.getInt("id"));

            user.setUsername(resultSet.getString("nome_usuario"));

            user.setFullName(resultSet.getString("nome_completo"));

            user.setEmail(resultSet.getString("email"));

            user.setActive(resultSet.getBoolean("esta_ativo"));

            user.setLastModified(resultSet.getTimestamp("ultima_modificacao"));

            String userType = resultSet.getString("tipo");
            user.setType(UserType.valueOf(userType));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(preparedStatement, connection, resultSet);
        }

        return user;
    }
}
