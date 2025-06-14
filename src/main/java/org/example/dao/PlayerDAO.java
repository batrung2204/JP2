package org.example.dao;

import org.example.model.Player;
import org.example.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PlayerDAO {
    public void insertPlayer(Player p) throws Exception {
        Connection conn = DBUtil.getConnection();
        String sql = "INSERT INTO Player (NationalId, PlayerName, HighScore, Level) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, p.getNationalId());
        ps.setString(2, p.getPlayerName());
        ps.setInt(3, p.getHighScore());
        ps.setInt(4, p.getLevel());
        ps.executeUpdate();
        conn.close();
    }

    public void deletePlayer(int playerId) throws Exception {
        Connection conn = DBUtil.getConnection();
        String sql = "DELETE FROM Player WHERE PlayerId=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, playerId);
        ps.executeUpdate();
        conn.close();
    }

    public List<Player> displayAll() throws Exception {
        List<Player> list = new ArrayList<>();
        Connection conn = DBUtil.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM Player");
        while (rs.next()) {
            list.add(new Player(
                    rs.getInt("PlayerId"),
                    rs.getInt("NationalId"),
                    rs.getString("PlayerName"),
                    rs.getInt("HighScore"),
                    rs.getInt("Level")
            ));
        }
        conn.close();
        return list;
    }

    public List<Player> displayByName(String name) throws Exception {
        List<Player> list = new ArrayList<>();
        Connection conn = DBUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM Player WHERE PlayerName LIKE ?");
        ps.setString(1, "%" + name + "%");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(new Player(
                    rs.getInt("PlayerId"),
                    rs.getInt("NationalId"),
                    rs.getString("PlayerName"),
                    rs.getInt("HighScore"),
                    rs.getInt("Level")
            ));
        }
        conn.close();
        return list;
    }

    public List<Player> displayTop10() throws Exception {
        List<Player> list = new ArrayList<>();
        Connection conn = DBUtil.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM Player ORDER BY HighScore DESC LIMIT 10");
        while (rs.next()) {
            list.add(new Player(
                    rs.getInt("PlayerId"),
                    rs.getInt("NationalId"),
                    rs.getString("PlayerName"),
                    rs.getInt("HighScore"),
                    rs.getInt("Level")
            ));
        }
        conn.close();
        return list;
    }
}
