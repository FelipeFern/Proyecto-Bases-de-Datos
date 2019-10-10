package Vuelos;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
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

	public boolean connectToDatabase(DBTable table, String username, String password) {
		try {
			String driver = "com.mysql.jdbc.Driver";
			String servidor = "localhost:3306";
			String baseDatos = "vuelos";
			String url = "jdbc:mysql://" + servidor + "/" + baseDatos
					+ "?serverTimezone=America/Argentina/Buenos_Aires";
			if (username.equals("admin")) {
				table.connectDatabase(driver, url, username, password);
				connection = DriverManager.getConnection(url, username, password);
			} else {
				if (!connectAsEmployee(table, driver, url, username, password)) {
					return false;
				}
			}
			return true;
		} catch (SQLException ex) {
			String msj = "Se produjo un error al intentar conectarse a la base de datos.\nPor favor, revise el usuario y contraseña";
			printSqlException(ex, msj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean connectAsEmployee(DBTable table, String driver, String url, String username, String password) {
		try {
			table.connectDatabase(driver, url, "empleado", "empleado");
			connection = DriverManager.getConnection(url, "empleado", "empleado");
			String query = "SELECT legajo, password FROM empleados WHERE  password = md5('" + password + "') AND " + username
					+ " = legajo;";
			Statement s = (Statement) connection.createStatement();
			s.executeQuery(query);
			ResultSet rs = s.getResultSet();
			if (rs.next()) {
				return true;
			}
			disconnectDataBase(table);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			String msj = "Se produjo un error al intentar conectarse a la base de datos.\nPor favor, revise el usuario y contraseña";
			printSqlException(e, msj);
		}
		return false;
	}

	public void disconnectDataBase(DBTable table) {
		try {
			if (this.connection != null) {
				connection.close();
				connection = null;
			}
			table.close();
		} catch (SQLException ex) {
			printSqlException(ex, "");
		}
	}

	public void refreshTable(DBTable table, String query) {
		if (checkSelect(query)) {
			refreshTableQuery(table, query);
		} else {
			try {
				Statement s = (Statement) connection.createStatement();
				s.execute(query);				
			} catch (SQLException e) {
				String msj = "Error al ejecutar la sentencia, revise que sea una operacion válida";
				printSqlException(e, msj);
			}
		}
	}

	private void refreshTableQuery(DBTable table, String query) {
		try {
			table.setSelectSql(query);
			table.createColumnModelFromQuery();
			for (int i = 0; i < table.getColumnCount(); i++) {
				if (table.getColumn(i).getType() == Types.TIME) {
					table.getColumn(i).setType(Types.CHAR);
				}
				if (table.getColumn(i).getType() == Types.DATE) {
					table.getColumn(i).setDateFormat("dd/MM/YYYY");
				}
			}
			table.refresh();
		} catch (SQLException ex) {
			printSqlException(ex, "Error al ejecutar la consulta.");
		} catch (ClassCastException ex) {
			System.out.println("Class cast exeption");
			System.out.println("Consulta: " + query);
		}
	}
	
	private boolean checkSelect(String consult) {
		consult = consult.toUpperCase();
		if (consult.contains("SELECT") || consult.contains("DESCRIBE")) {
			return true;
		}
		return false;
	}

	public void refreshExcecute(String message, JComboBox<String> comboBox, MainPanel panel) {
		try {
			DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
			Statement s = (Statement) connection.createStatement();
			s.executeQuery(message);
			ResultSet rs = s.getResultSet();
			while (rs.next()) {
				String str;
				if (message.contains("Fecha")) {
					str = Fechas.convertirDateAString(rs.getDate(1));
				} else {
					str = rs.getString(1);
				}
				model.addElement(str);
			}
			comboBox.setModel(model);
		} catch (SQLException ex) {
			printSqlException(ex, "Error en el acceso.");
		}
	}

	private void printSqlException(SQLException ex, String msj) {
		System.out.println("SQLException: " + ex.getMessage());
		System.out.println("SQLState: " + ex.getSQLState());
		System.out.println("VendorError: " + ex.getErrorCode());
		JOptionPane.showMessageDialog(MainWindow.getInstance(), ex.getMessage() + "\n", msj, JOptionPane.ERROR_MESSAGE);
	}
}
