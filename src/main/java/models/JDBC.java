package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class JDBC {


  private static final String DEFAULT_DB_URL = "jdbc:postgresql://aws-1-ap-south-1.pooler.supabase.com:6543/postgres?user=postgres.tereexilkchduyiclgoy&password=SiAlumni567#&sslmode=require";
   private static final String DEFAULT_DB_USER = "postgres.tereexilkchduyiclgoy";
    private static final String DEFAULT_DB_PASS = "SiAlumni567#";

    public static final String DB_URL  = System.getenv("DB_URL")  != null && !System.getenv("DB_URL").isEmpty()  ? System.getenv("DB_URL")  : DEFAULT_DB_URL;
    public static final String DB_USER = System.getenv("DB_USER") != null && !System.getenv("DB_USER").isEmpty() ? System.getenv("DB_USER") : DEFAULT_DB_USER;
    public static final String DB_PASS = System.getenv("DB_PASS") != null && !System.getenv("DB_PASS").isEmpty() ? System.getenv("DB_PASS") :  DEFAULT_DB_PASS;
    public static final String SUPABASE_API_KEY = System.getenv("sb_publishable_-A-NUQledob1Q8cUxKTpLA__RJi3dqa");

   
    protected Connection conn;

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    
    protected void connect() {
        try {
            conn = getConnection();
        } catch (ClassNotFoundException e) {
            System.out.println("Driver PostgreSQL tidak ditemukan: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Gagal konek ke database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    
    protected void disconnect() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Gagal tutup koneksi: " + e.getMessage());
        }
    }

    
    public abstract String getProfile();
}
