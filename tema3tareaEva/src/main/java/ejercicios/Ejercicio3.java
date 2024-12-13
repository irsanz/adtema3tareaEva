package ejercicios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

public class Ejercicio3 {


    public static void main(String[] args) {
        try (Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost/dbeventos", "root", "vainilla")) {

            System.out.println("Introduce el DNI del asistente");
            String dni = Consola.readString();
            if (Pattern.matches("\\d{8}[A-Za-z]", dni)) {
                System.out.println("No se encontró un asistente con el DNI proporcionado.");
                registrarAsistente(conexion, dni);}

            System.out.println("Estás realizando la reserva para: ");
            displayName(conexion, dni);
            
            System.out.println("Lista de eventos: ");
            listaEventos(conexion);

            System.out.println("Elige el número del evento al que se quiere asistir");
            int id_evento = Consola.readInt();
            eventoLleno(conexion, id_evento);
            
            if (eventoLleno(conexion, id_evento)) {
                System.out.println("No es posible realizar el registro: evento lleno.");
                return;
            }

            registrarAsistente(conexion, dni);
            registrarAsistencia(conexion, dni, id_evento);

            System.out.println("Asistencia registrada correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
            
            
            private static void displayName(Connection conexion, String dni) throws SQLException {
                String sql = "SELECT nombre FROM asistentes WHERE dni= ?";
                try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
                    sentencia.setString(1, dni);
                    try (ResultSet rs = sentencia.executeQuery()) {
                        if (rs.next()) {
                            System.out.println(rs.getString("nombre"));
                        }
                    }
                }
            }
            
            private static void registrarAsistente(Connection conexion, String dni) throws SQLException {
                String sql = "SELECT COUNT(*) FROM asistentes WHERE dni = ?";
                try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
                    sentencia.setString(1, dni);
                    try (ResultSet rs = sentencia.executeQuery()) {
                        if (rs.next() && rs.getInt(1) == 0) {
                            System.out.print("Introduce el nombre del asistente: ");
                            String nombre = Consola.readString();

                            String insertQuery = "INSERT INTO asistentes (dni, nombre) VALUES (?, ?)";
                            try (PreparedStatement insertStmt = conexion.prepareStatement(insertQuery)) {
                                insertStmt.setString(1, dni);
                                insertStmt.setString(2, nombre);
                                insertStmt.executeUpdate();
                            }
                        }
                    }
                }
            }
                        private static void listaEventos(Connection conexion) throws SQLException {
                            String query = "SELECT e.id_evento, e.nombre_evento, u.nombre AS ubicacion, u.capacidad, " +
                                           "COUNT(ae.dni) AS registrados " +
                                           "FROM eventos e " +
                                           "INNER JOIN ubicaciones u ON e.id_ubicacion = u.id_ubicacion " +
                                           "LEFT JOIN asistentes_eventos ae ON e.id_evento = ae.id_evento " +
                                           "GROUP BY e.id_evento, e.nombre_evento, u.nombre, u.capacidad";

                            try (Statement stmt = conexion.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                                while (rs.next()) {
                                    System.out.printf("%d - %s (Ubicación: %s, Capacidad: %d, Registrados: %d)%n",
                                            rs.getInt("id_evento"),
                                            rs.getString("nombre_evento"),
                                            rs.getString("ubicacion"),
                                            rs.getInt("capacidad"),
                                            rs.getInt("registrados"));
                                }
                            }
                        }
                            
                            private static boolean eventoLleno(Connection connection, int idEvento) throws SQLException {
                                String query = "SELECT u.capacidad, COUNT(ae.dni) AS registrados " +
                                               "FROM eventos e " +
                                               "INNER JOIN ubicaciones u ON e.id_ubicacion = u.id_ubicacion " +
                                               "LEFT JOIN asistentes_eventos ae ON e.id_evento = ae.id_evento " +
                                               "WHERE e.id_evento = ? " +
                                               "GROUP BY u.capacidad";

                                try (PreparedStatement sentencia = connection.prepareStatement(query)) {
                                    sentencia.setInt(1, idEvento);
                                    try (ResultSet rs = sentencia.executeQuery()) {
                                        if (rs.next()) {
                                            return rs.getInt("registrados") >= rs.getInt("capacidad");
                                        }
                                    }
                                }
                                return false;
                            }  
        private static void registrarAsistencia(Connection connection, String dni, int id_evento) throws SQLException {
        String query = "INSERT INTO asistentes_eventos (dni, id_evento) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, dni);
            pstmt.setInt(2, id_evento);
            pstmt.executeUpdate();
        }
        
}                        }