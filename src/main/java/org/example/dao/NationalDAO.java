package org.example.dao;

import org.example.model.National;
import org.example.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class NationalDAO {
    public void insertNational(National n) throws Exception {
        Connection conn = DBUtil.getConnection();
        String sql = "INSERT INTO National (NationalName) VALUES (?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, n.getNationalName());
        ps.executeUpdate();
        conn.close();
    }

    public void deleteNational(int id) throws Exception {
        Connection conn = DBUtil.getConnection();
        String sql = "DELETE FROM National WHERE NationalId=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        conn.close();
    }

    public List<National> getAllNationals() throws Exception {
        List<National> list = new ArrayList<>();
        Connection conn = DBUtil.getConnection();
        String sql = "SELECT * FROM National";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            list.add(new National(
                    rs.getInt("NationalId"),
                    rs.getString("NationalName")
            ));
        }
        conn.close();
        return list;
    }
}
