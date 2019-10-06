package Vuelos;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import quick.dbtable.DBTable;

public class DataBaseConnection {
	private java.sql.Connection connection;
	private static DataBaseConnection INSTANCE;

	private DataBaseConnection() {}
	
	public static DataBaseConnection getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DataBaseConnection();
		}
		return INSTANCE;
	}

	public void connectToDatabase(DBTable table, String username, String password) {
		try {
			String driver = "com.mysql.jdbc.Driver";
			String servidor = "localhost:3306";
			String baseDatos = "vuelos";
			String url = "jdbc:mysql://" + servidor + "/" + baseDatos;
			table.connectDatabase(driver, url, username, password);
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(MainWindow.getInstance(),
					"Se produjo un error al intentar conectarse a la base de datos.\n" + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * public void connectToDatabase(DBTable table, String username, String
	 * password) { try { String driver = "com.mysql.cj.jdbc.Driver"; String server =
	 * "localhost:3306"; String database = "vuelos"; String uriConexion =
	 * "jdbc:mysql://" + server + "/" + database +
	 * "?serverTimezone=America/Argentina/Buenos_Aires";
	 * 
	 * // establece una conexi�n con la B.D. "vuelos" usando directamante una tabla
	 * // DBTable table.connectDatabase(driver, uriConexion, username, password);
	 * 
	 * } catch (SQLException ex) {
	 * JOptionPane.showMessageDialog(MainWindow.getInstance(),
	 * "Se produjo un error al intentar conectarse a la base de datos.\n" +
	 * ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	 * System.out.println("SQLException: " + ex.getMessage());
	 * System.out.println("SQLState: " + ex.getSQLState());
	 * System.out.println("VendorError: " + ex.getErrorCode()); } catch
	 * (ClassNotFoundException e) { e.printStackTrace(); } }
	 */

	public void disconnectDataBase(DBTable table) {
		try{
		     if (this.connection != null){
		        connection.close();
		        connection = null;
		     } 
		     table.close();
		 }catch (SQLException ex){
			 System.out.println("SQLExcepcion " + ex.getMessage());
			 System.out.println("SQLEstado: " + ex.getSQLState());
			 System.out.println("CodigoError: " + ex.getErrorCode());
		 }	    
	}

	public void refrescarTabla(DBTable table, String consult) {
		try {
			// seteamos la consulta a partir de la cual se obtendr�n los datos para llenar
			// la tabla
			table.setSelectSql(consult);

			// obtenemos el modelo de la tabla a partir de la consulta para
			// modificar la forma en que se muestran de algunas columnas
			table.createColumnModelFromQuery();
			for (int i = 0; i < table.getColumnCount(); i++) { // para que muestre correctamente los valores de tipo
																// TIME (hora)
				if (table.getColumn(i).getType() == Types.TIME) {
					table.getColumn(i).setType(Types.CHAR);
				}
				// cambiar el formato en que se muestran los valores de tipo DATE
				if (table.getColumn(i).getType() == Types.DATE) {
					table.getColumn(i).setDateFormat("dd/MM/YYYY");
				}
			}
			// actualizamos el contenido de la tabla.
			table.refresh();
			// No es necesario establecer una conexi�n, crear una sentencia y recuperar el
			// resultado en un resultSet, esto lo hace autom�ticamente la tabla (DBTable) a
			// patir de la conexi�n y la consulta seteadas con connectDatabase() y
			// setSelectSql() respectivamente.

		} catch (SQLException ex) {
			// en caso de error, se muestra la causa en la consola
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			JOptionPane.showMessageDialog(MainWindow.getInstance(), ex.getMessage() + "\n",
					"Error al ejecutar la consulta.", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void refrescarExecute(String message, JList<String> list, MainPanel panel) {
		try {
			DefaultListModel<String> model = new DefaultListModel<String>();
			Statement s = (Statement) connection.createStatement();
			s.executeQuery(message); 
			ResultSet rs = s.getResultSet();
			while (rs.next()) {
				model.addElement(rs.getString(1));
			}
			list.setModel(model);

		} catch (SQLException ex) {
			System.out.println("SQLExcepcion: " + ex.getMessage());
			System.out.println("SQLEstado: " + ex.getSQLState());
			System.out.println("CodigoError: " + ex.getErrorCode());
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(panel), ex.getMessage() + "\n",
					"Error en el acceso.", JOptionPane.ERROR_MESSAGE);
		}
	}
}
