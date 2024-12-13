package ejercicios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Ejercicio2 {
	
    private static final String CONNECTION_URL = "jdbc:mysql://localhost/dbeventos";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "vainilla";
	public static void main(String[] args) {
		try (Connection connection = DriverManager.getConnection(CONNECTION_URL, USERNAME, PASSWORD)) {
                      
            System.out.println("Introduce el nombre de la ubicación: ");
             String ubicacion = Consola.readString();
             int capacidad = getCapacidad(connection, ubicacion);

            if (ubicacion != null) {
                System.out.println("La capacidad actual de la ubicación " + "'" + ubicacion + "'" + "es: " + capacidad);
                System.out.println("Introduce la nueva capacidad máxima ");
                int nuevaCapacidad = getCapacidadFromUser();

                if (nuevaCapacidad !=0) {
                    updateCapacidad(connection, ubicacion, nuevaCapacidad);
                    System.out.println("Capacidad actualizada correctamente");
                } else {
                    System.out.println("No ha introducido la nueva capacidad");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al conectarse o trabajar con la base de datos: " + e.getMessage());
        }
	}
	
		    private static int getCapacidad(Connection connection, String nombre) throws SQLException {
		        String sql = "select capacidad from ubicaciones where nombre = ?";
		        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
		            stmt.setString(1, nombre);
		            ResultSet rs = stmt.executeQuery();
		                if (rs.next()) {
		                    return rs.getInt("capacidad");   
		                    }
		        }
		        return 0; // Devuelve 0 si no se encuentra la capacidad
		    }

		    private static int getCapacidadFromUser() {
		        return Consola.readInt();
		    }

		    private static void updateCapacidad(Connection connection, String ubicacion, int cap) throws SQLException {
		        String sql = "UPDATE ubicaciones SET capacidad = ? WHERE nombre= ?";
		        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
		            stmt.setInt(1, cap);
		            stmt.setString(2, ubicacion);
		            stmt.executeUpdate();
		        }
    }
	}


