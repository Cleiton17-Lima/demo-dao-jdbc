package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DbException;
import model.entities.Seller;
import model.dao.SellerDao;

public class SellerDaoJDBC implements SellerDao {

    private final Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller seller) {
        String sql = "INSERT INTO seller (name, email, birthDate, baseSalary) VALUES (?, ?, ?, ?)";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, seller.getName());
            st.setString(2, seller.getEmail());
            st.setDate(3, toSqlDate(seller));
            st.setObject(4, seller.getBaseSalary());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void update(Seller seller) {
        String sql = "UPDATE seller SET name = ?, email = ?, birthDate = ?, baseSalary = ? WHERE id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, seller.getName());
            st.setString(2, seller.getEmail());
            st.setDate(3, toSqlDate(seller));
            st.setObject(4, seller.getBaseSalary());
            st.setObject(5, seller.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM seller WHERE id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setObject(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public Seller findById(Integer id) {
        String sql = "SELECT * FROM seller WHERE id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setObject(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return instantiateSeller(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public List<Seller> findAll() {
        String sql = "SELECT * FROM seller ORDER BY name";
        List<Seller> sellers = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                sellers.add(instantiateSeller(rs));
            }
            return sellers;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    private Seller instantiateSeller(ResultSet rs) throws SQLException {
        return new Seller(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getDate("birthDate"),
                rs.getDouble("baseSalary"));
    }

    private Date toSqlDate(Seller seller) {
        if (seller.getBirthDate() == null) {
            return null;
        }
        return new Date(seller.getBirthDate().getTime());
    }
}
