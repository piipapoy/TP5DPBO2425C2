import java.sql.*;

public class Database {
    private Connection connection;
    private Statement statement;

    // constructor
    public Database() {
        try {
            // Buat koneksi ke database tp4
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/tp4",
                    "root",
                    ""
            );
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Method untuk query SELECT
    public ResultSet selectQuery(String sql) {
        try {
            // Jalankan query dan kembalikan hasilnya
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Method untuk query INSERT, UPDATE, DELETE
    public int insertUpdateDeleteQuery(String sql) {
        try {
            // Jalankan query dan kembalikan jumlah baris yang terpengaruh
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Getter untuk statement jika diperlukan
    public Statement getStatement() {
        return statement;
    }
}