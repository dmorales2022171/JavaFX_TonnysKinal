
package org.diegomorales.controller;

import com.jfoenix.controls.JFXTimePicker;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.diegomorales.bean.Empleado;
import org.diegomorales.bean.Servicio;
import org.diegomorales.bean.ServiciosHasEmpleados;
import org.diegomorales.db.Conexion;
import org.diegomorales.main.Principal;

public class ServiciosHasEmpleadosController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones {GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO};  
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <ServiciosHasEmpleados> listaServiciosHasEmpleados;
    private ObservableList <Servicio> listaServicio;
    private ObservableList <Empleado> listaEmpleado;
    private DatePicker fecha;
    
    @FXML private TextField txtCodigoServiciosHas;
    @FXML private ComboBox cmbCodigoServicio;
    @FXML private ComboBox cmbCodigoEmpleado;
    @FXML private JFXTimePicker tpHoraEvento;
    @FXML private GridPane grpFecha;
    @FXML private TextField txtLugarEvento;
    @FXML private Button btnNuevo;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgReporte;
    @FXML private TableView tblServiciosHasEmpleados;
    @FXML private TableColumn colCodigoServiciosHas;
    @FXML private TableColumn colFechaEvento;
    @FXML private TableColumn colHoraEvento;
    @FXML private TableColumn colLugarEvento;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colCodigoEmpleado;
            

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoServicio.setItems(getServicio());
        cmbCodigoEmpleado.setItems(getEmpleado());
        fecha = new DatePicker (Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/diegomorales/resource/TonysKinal.css");
        grpFecha.add(fecha,3,0);
    }
    
    public void cargarDatos(){
        tblServiciosHasEmpleados.setItems(getServiciosHasEmpleados());
        colCodigoServiciosHas.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, Integer>("Servicios_codigoServicio"));
        colFechaEvento.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, Date>("fechaEvento"));
        colHoraEvento.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, Time>("horaEvento"));
        colLugarEvento.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, String>("lugarEvento"));
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, Integer>("codigoServicio"));
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<ServiciosHasEmpleados, Integer>("codigoEmpleado"));
    }
    
    public void seleccionarElemento(){
        txtCodigoServiciosHas.setText(String.valueOf(((ServiciosHasEmpleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
        cmbCodigoServicio.getSelectionModel().select(buscarServicio(((ServiciosHasEmpleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getCodigoServicio()));
        cmbCodigoEmpleado.getSelectionModel().select(buscarEmpleado(((ServiciosHasEmpleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
        fecha.selectedDateProperty().set(((ServiciosHasEmpleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getFechaEvento());
        tpHoraEvento.promptTextProperty().set(String.valueOf(((ServiciosHasEmpleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getHoraEvento()));
        txtLugarEvento.setText(((ServiciosHasEmpleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getLugarEvento());
     }      
    
    public Servicio buscarServicio(int codigoServicio){
        Servicio resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarServicio(?)");
            procedimiento.setInt(1, codigoServicio);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()){
                resultado = new Servicio (registro.getInt("codigoServicio"),
                                        registro.getDate("fechaServicio"),
                                        registro.getString("tipoServicio"),
                                        registro.getTime("horaServicio"),
                                        registro.getString("lugarServicio"),
                                        registro.getString("telefonoContacto"),
                                        registro.getInt("codigoEmpresa"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }        
    
    public Empleado buscarEmpleado(int codigoEmpleado){
        Empleado resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarEmpleado(?)");
            procedimiento.setInt(1, codigoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()){
                resultado = new Empleado (registro.getInt("codigoEmpleado"),
                                            registro.getInt("numeroEmpleado"),
                                            registro.getString("apellidosEmpleado"),
                                            registro.getString("nombresEmpleado"),
                                            registro.getString ("direccionEmpleado"),
                                            registro.getString("telefonoContacto"),
                                            registro.getString("gradoCocinero"),
                                            registro.getInt("codigoTipoEmpleado"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    
    public ObservableList <ServiciosHasEmpleados> getServiciosHasEmpleados(){
        ArrayList <ServiciosHasEmpleados> lista = new ArrayList <ServiciosHasEmpleados>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicio_has_Empleados");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new ServiciosHasEmpleados (resultado.getInt("Servicios_codigoServicio"),
                                                        resultado.getInt("codigoServicio"),
                                                        resultado.getInt("codigoEmpleado"),
                                                        resultado.getDate("fechaEvento"),
                                                        resultado.getTime("horaEvento"),
                                                        resultado.getString("lugarEvento")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaServiciosHasEmpleados = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList <Servicio> getServicio(){
        ArrayList <Servicio> lista = new ArrayList<Servicio>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicios ");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Servicio (resultado.getInt("codigoServicio"),
                                        resultado.getDate("fechaServicio"),
                                        resultado.getString("tipoServicio"),
                                        resultado.getTime("horaServicio"),
                                        resultado.getString("lugarServicio"),
                                        resultado.getString("telefonoContacto"),
                                        resultado.getInt("codigoEmpresa")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaServicio = FXCollections.observableArrayList(lista);
    }

    public ObservableList <Empleado> getEmpleado(){
        ArrayList <Empleado> lista = new ArrayList <Empleado>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpleados");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empleado (resultado.getInt("codigoEmpleado"),
                                        resultado.getInt("numeroEmpleado"),
                                        resultado.getString("apellidosEmpleado"),
                                        resultado.getString("nombresEmpleado"),
                                        resultado.getString("direccionEmpleado"),
                                        resultado.getString("telefonoContacto"),
                                        resultado.getString("gradoCocinero"),
                                        resultado.getInt("codigoTipoEmpleado")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaEmpleado = FXCollections.observableArrayList(lista);
    }    

    public void nuevo(){
        switch (tipoDeOperacion){
            case NINGUNO:
            limpiarControles();
            activarControles();
            btnNuevo.setText("Guardar");
            btnReporte.setText("Cancelar");

            imgNuevo.setImage(new Image("/org/diegomorales/image/Guardar.png"));
            imgReporte.setImage(new Image ("/org/diegomorales/image/Cancelar.png"));
            tipoDeOperacion = operaciones.GUARDAR;
            break;
            
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnReporte.setText(" . ");
                imgNuevo.setImage(new Image ("/org/diegomorales/image/Create.png"));
                imgReporte.setImage(new Image ("/org/diegomorales/image/boton.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnReporte.setText(".");
                imgNuevo.setImage(new Image ("/org/diegomorales/image/Create.png"));
                imgReporte.setImage(new Image ("/org/diegomorales/image/boton.png"));
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;

        }
    }

    
    public void guardar(){
        ServiciosHasEmpleados registro = new ServiciosHasEmpleados();
        LocalTime selecTime = tpHoraEvento.getValue();
        java.sql.Time sqlTime = java.sql.Time.valueOf(selecTime);        
        
        registro.setServicios_codigoServicio(Integer.parseInt(txtCodigoServiciosHas.getText()));
        registro.setFechaEvento(fecha.getSelectedDate());
        registro.setHoraEvento(sqlTime);
        registro.setLugarEvento(txtLugarEvento.getText());
        registro.setCodigoServicio(((Servicio)cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        registro.setCodigoEmpleado(((Empleado)cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarServicio_has_Empleados(?,?,?,?,?,?)");
            procedimiento.setInt(1, registro.getServicios_codigoServicio());
            procedimiento.setInt(2, registro.getCodigoServicio());
            procedimiento.setInt(3, registro.getCodigoEmpleado());            
            procedimiento.setDate(4, new java.sql.Date(registro.getFechaEvento().getTime()));
            procedimiento.setTime(5, new java.sql.Time(registro.getHoraEvento().getTime()));
            procedimiento.setString(6, registro.getLugarEvento());

            procedimiento.execute();
            listaServiciosHasEmpleados.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void desactivarControles(){
        txtCodigoServiciosHas.setDisable(false);
        txtLugarEvento.setDisable(false);    
        cmbCodigoServicio.setDisable(true);
        cmbCodigoEmpleado.setDisable(true);
        grpFecha.setDisable(false);
        tpHoraEvento.setDisable(false);
    }
    
    public void activarControles(){
        txtCodigoServiciosHas.setEditable(true);
        txtLugarEvento.setEditable(true);
        cmbCodigoServicio.setDisable(false);
        cmbCodigoEmpleado.setDisable(false);
        grpFecha.setDisable(false);
        tpHoraEvento.setDisable(false);
        
    }
    
    public void limpiarControles(){
        txtCodigoServiciosHas.setText("");
        txtLugarEvento.setText("");
        cmbCodigoServicio.setValue(null);
        cmbCodigoEmpleado.setValue(null);
        fecha.selectedDateProperty().set(null);
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
}
