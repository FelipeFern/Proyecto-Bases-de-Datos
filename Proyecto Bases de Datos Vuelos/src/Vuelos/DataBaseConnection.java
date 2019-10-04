package Vuelos;

import java.awt.Component;
import java.sql.SQLException;
import java.sql.Types;
import javax.swing.JOptionPane;
import quick.dbtable.DBTable;

public class DataBaseConnection {
	private java.sql.Connection conection;

	public DataBaseConnection() {

	}

	public void connectToDatabase(DBTable table, String username, String password) {
		try {
			String driver = "com.mysql.cj.jdbc.Driver";
			String server = "localhost:3306";
			String database = "vuelos";
			String uriConexion = "jdbc:mysql://" + server + "/" + database
					+ "?serverTimezone=America/Argentina/Buenos_Aires";

			// establece una conexión con la B.D. "vuelos" usando directamante una tabla
			// DBTable
			table.connectDatabase(driver, uriConexion, username, password);

		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(MainWindow.getInstance(),
					"Se produjo un error al intentar conectarse a la base de datos.\n" + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void disconnectDataBase(Component component) {
		try {
			((DBTable)component).close();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public void refrescarTabla(DBTable table, String consult) {
		try {
			// seteamos la consulta a partir de la cual se obtendrán los datos para llenar
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
			// No es necesario establecer una conexión, crear una sentencia y recuperar el
			// resultado en un resultSet, esto lo hace automáticamente la tabla (DBTable) a
			// patir de la conexión y la consulta seteadas con connectDatabase() y
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
}
