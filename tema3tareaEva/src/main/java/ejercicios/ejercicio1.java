package ejercicios;

import java.sql.*;

public class ejercicio1 {

	public static void main(String[] args) {
		  // Propiedades
        String url = "jdbc:mysql://localhost:3306/dbeventos";
        String user = "root";
        String password = "vainilla";
	 
	// Trabajo con base de datos
        try (
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select e.nombre_evento, count(a.dni) as asistentes, u.nombre as ubicacion, u.direccion\r\n"
            		+ "from eventos e inner join asistentes_eventos ae on e.id_evento = ae.id_evento \r\n"
            		+ "inner join asistentes a on a.dni = ae.dni\r\n"
            		+ "inner join ubicaciones u on e.id_ubicacion = u.id_ubicacion\r\n"
            		+ "group by e.id_evento;")
        ){
        	 System.out.printf("%-30s%-15s%-40s%-40s%n","Evento", "Asistentes", "Ubicación", "Dirección"); 
             System.out.println("-----------------------------------------------------------------------------------------------");
            while(rs.next()){
            	String nombre = rs.getString("nombre_evento");
                int asistentes = rs.getInt("asistentes");
                String ubicacion = rs.getString("ubicacion");
                String direccion = rs.getString("direccion");
                
                System.out.printf( "%-30s%-15s%-40s%-40s%n", nombre, asistentes, ubicacion, direccion);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}


