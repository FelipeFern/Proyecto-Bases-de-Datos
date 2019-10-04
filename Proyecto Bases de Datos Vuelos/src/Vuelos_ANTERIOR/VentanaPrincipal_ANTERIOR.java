package Vuelos_ANTERIOR;


import java.sql.DriverManager;

import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class VentanaPrincipal_ANTERIOR extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnAdmin;
	private java.sql.Connection conexion;
	private JLabel lblSistemaDeParquimetros;
	private VentanaEmpleado_ANTERIOR ventanaEmpleado;
	private VentanaAdministrador_ANTERIOR ventanaAdmin;
	private JButton btnInspector;
	private JLabel lblParquimetros;
	
/*
 * La idea es que en esta clase se conecte a la base de datos y te mueste un frame para ver en que opcion
 * te queres conectar, si para hacer consultas como administrador o como Empleado. Entonces le metemos dos 
 * botones y que vayan a los distintas clases (Paneles) donde hagan lo que tengan que hacer.
 * 
 */
	
	
	public VentanaPrincipal_ANTERIOR() {
		
		conectarBD();
	}
	
	private void conectarBD() {
		try
        {
		//Intento de conectar a la base de datos
		   String servidor = "localhost:3306";
           String baseDatos = "vuelos";
           String url = "jdbc:mysql://" + servidor + "/" + baseDatos;
  
          conexion= DriverManager.getConnection(url, "admin", "admin");
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(this,
                                          "Se produjo un error al intentar conectarse a la base de datos.\n" + ex.getMessage(),
                                          "Error",
                                          JOptionPane.ERROR_MESSAGE);
            System.out.println("SQLExcepcion: " + ex.getMessage());
            System.out.println("SQLEstado: " + ex.getSQLState());
            System.out.println("CodigoError: " + ex.getErrorCode());
        }
        catch (Exception e)
        {
           e.printStackTrace();
        }
	}

	/*
	private void consultaAdmin (String pass) {
		 try
         {
        	String servidor = "localhost:3306";
            String baseDatos = "vuelos";
            String uriConexion = "jdbc:mysql://" + servidor + "/" + baseDatos;
   
            //Intento de conexion a la base con las password ingresada para el admin
           conexion =DriverManager.getConnection(uriConexion, "admin", pass);
           //Cambio a la ventana para el admin
           ventanaAdmin= new VentanaAdministrador(this,pass);
           ventanaAdmin.setVisible(true);
          // cerrarVistaActual();
         }
         catch (Exception ex) {
        	 String msg= "Password incorrecta - Intente nuevamente";
        	 JOptionPane.showMessageDialog(new JFrame(), msg, "Error",JOptionPane.ERROR_MESSAGE);
         }
		
	}
	

	private boolean consultarLoginInspector(String legajo, String pass){
		String consulta="select * from inspectores where legajo='"+legajo+"' and password=md5('"+pass+"');";
		boolean respuesta=true;
		try{
			Statement s=(Statement) conexion.createStatement();
			s.executeQuery(consulta);
			ResultSet rs= s.getResultSet();
			respuesta=rs.next();
			
		}catch (SQLException ex)
	      {
	         // en caso de error, se muestra la causa en la consola
	         System.out.println("SQLException: " + ex.getMessage());
	         System.out.println("SQLState: " + ex.getSQLState());
	         System.out.println("VendorError: " + ex.getErrorCode());
	         JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
	                                       ex.getMessage() + "\n", 
	                                       "Error al Querer acceder.",
	                                       JOptionPane.ERROR_MESSAGE);
	         
	      }
		return respuesta;
	}

	private void consultaInspector (String legajo, String pass) {
		 try
         {
        	String servidor = "localhost:3306";
            String baseDatos = "vuelos";
            String uriConexion = "jdbc:mysql://" + servidor + "/" + baseDatos;
   
            //Intento de conexion a la base con las password ingresada para el admin
            conexion = DriverManager.getConnection(uriConexion, "admin", "admin");
           
           	if(consultarLoginInspector(legajo,pass)){
        	   	cerrarVistaActual();
	           	//Cambio a la ventana para el admin
	           	ventanaAdministrador= new VentanaAdministrador(this,legajo,pass);
	           	ventanaAdministrador.setVisible(true);
         	}else{
          	 	String msg= "Legajo/Password incorrectos - Intente nuevamente";
          	 	JOptionPane.showMessageDialog(new JFrame(), msg, "Error",JOptionPane.ERROR_MESSAGE);        	   
           }
         }
         catch (Exception ex) {
        	 String msg= "Error al conectar la base de datos - Intente nuevamente";
        	 JOptionPane.showMessageDialog(new JFrame(), msg, "Error",JOptionPane.ERROR_MESSAGE);
         }
		
	}
	*/
	
	private void cerrarVistaActual() {
		cerrarBD();
		this.setVisible(false);
	}
		
	private void cerrarBD() {
		if (this.conexion != null)
	      {
	         try
	         {
	            conexion.close();
	            conexion = null;
	         }
	         catch (SQLException ex)
	         {
	            System.out.println("SQLExcepcion: " + ex.getMessage());
	            System.out.println("SQLEstado: " + ex.getSQLState());
	            System.out.println("CodigoError: " + ex.getErrorCode());
	         }
	      }
	}
	
	public void restaurar() {
		setVisible(true);
		conectarBD();
	}
	
	

}