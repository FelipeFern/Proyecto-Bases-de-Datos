package Vuelos;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import quick.dbtable.DBTable;

public class DataBaseConnection {
	private java.sql.Connection connection;
	private static DataBaseConnection INSTANCE;

	private DataBaseConnection() {
	}

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
			printSqlException(ex, msj, "Error de conexion.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean connectAsEmployee(DBTable table, String driver, String url, String username, String password) {
		try {
			table.connectDatabase(driver, url, "empleado", "empleado");
			connection = DriverManager.getConnection(url, "empleado", "empleado");
			String query = "SELECT legajo, password FROM empleados WHERE  password = md5('" + password + "') AND "
					+ username + " = legajo;";
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
			printSqlException(e, msj, "Error de conexion.");
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
			printSqlException(ex, "Error al desconectarse de la base de datos.", "Error de desconexion");
		}
	}

	public void refreshTable(DBTable table, String query) {
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
		} catch (SQLException e) {
			String msj = "Error al obtener datos.";
			printSqlException(e, msj, "Error al refrescar la tabla.");
		} catch (ClassCastException ex) {
			System.out.println("Class cast exeption");
			System.out.println("Consulta: " + query);
		}
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
			printSqlException(ex, "Error al obtener datos.", "Error de conexion.");
		}
	}

	private void printSqlException(SQLException ex, String msj, String description) {
		System.out.println("SQLException: " + ex.getMessage());
		System.out.println("SQLState: " + ex.getSQLState());
		System.out.println("VendorError: " + ex.getErrorCode());
		JOptionPane.showMessageDialog(MainWindow.getInstance(), msj, description, JOptionPane.ERROR_MESSAGE);
	}

	
	public void excecuteQuery(String query) {
		try {
			Statement s = (Statement) connection.createStatement();
			s.execute(query);
		} catch (SQLException e) {
			printSqlException(e, e.getMessage(), "Error");
		}
	}

	public JTable getFilledUpTable(String query) {
		try {
			Statement s = (Statement) connection.createStatement();
			ResultSet rs = s.executeQuery(query);
			return new JTable(buildTableModel(rs));
		} catch (SQLException e) {
			printSqlException(e, "Error al mostrar los datos", "Error al mostrar los datos");
		}
		return null;
	}

	private TableModel buildTableModel(ResultSet rs) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		Vector<String> columnNames = new Vector<String>();
		int columnCount = metaData.getColumnCount();
		for (int column = 1; column <= columnCount; column++) {
			columnNames.add(metaData.getColumnName(column));
		}
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		while (rs.next()) {
			Vector<Object> vector = new Vector<Object>();
			for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
				vector.add(rs.getObject(columnIndex));
			}
			data.add(vector);
		}
		return new DefaultTableModel(data, columnNames);
	}

	public String excecuteProcedure(String query, ArrayList<Object> parametersList) {
		try {
			CallableStatement statement = (CallableStatement) connection.prepareCall(query);
			int i = 0;
			statement.setString(++i, (String) parametersList.get(i - 1));
			statement.setString(++i, (String) parametersList.get(i - 1));
			statement.setDate(++i, (Date) parametersList.get(i - 1));
			if (parametersList.size() != 7) {
				statement.setString(++i, (String) parametersList.get(i - 1));
				statement.setString(++i, (String) parametersList.get(i - 1));
				statement.setDate(++i, (Date) parametersList.get(i - 1));
			}
			statement.setString(++i, (String) parametersList.get(i - 1));
			statement.setInt(++i, (int) parametersList.get(i - 1));
			statement.setInt(++i, (int) parametersList.get(i - 1));
			statement.registerOutParameter(++i, Types.VARCHAR);
			statement.executeQuery();
			return statement.getString(i);
		} catch (SQLException e) {
			e.printStackTrace();
			printSqlException(e, e.getMessage(), "Error");
			System.out.println("Error en DATABASE CONNECTION");
			return null;
		}
	}
}
