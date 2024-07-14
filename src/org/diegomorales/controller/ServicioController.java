
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
import javax.swing.JOptionPane;
import org.diegomorales.bean.Empresa;
import org.diegomorales.bean.Servicio;
import org.diegomorales.db.Conexion;
import org.diegomorales.main.Principal;

public class ServicioController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones {GUARDAR,ELIMINAR,ACTUALIZAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <Servicio> listaServicio;
    private ObservableList <Empresa> listaEmpresa;
    private DatePicker fecha;
    
    @FXML private TextField txtCodigoServicio;
    @FXML private TextField txtTipoServicio;
    @FXML private TextField txtLugarServicio;
    @FXML private TextField txtTelefonoContacto;
    @FXML private ComboBox cmbCodigoEmpresa;
    @FXML private GridPane grpFecha;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    @FXML private Button btnEditar;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private JFXTimePicker tpHoraServicio;
    @FXML private TableView tblServicios;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colTipoServicio;
    @FXML private TableColumn colLugarServicio;
    @FXML private TableColumn colFechaServicio;
    @FXML private TableColumn colHoraServicio;
    @FXML private TableColumn colTelefonoContacto;
    @FXML private TableColumn colCodigoEmpresa;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fecha = new DatePicker (Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/diegomorales/resource/TonysKinal.css");
        grpFecha.add(fecha,3,0);
        cmbCodigoEmpresa.setItems(getEmpresa());        
    }
    public void cargarDatos(){
        tblServicios.setItems(getServicio());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicio, Integer>("codigoServicio"));
        colFechaServicio.setCellValueFactory(new PropertyValueFactory<Servicio, Date>("fechaServicio"));
        colTipoServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("tipoServicio"));
        colHoraServicio.setCellValueFactory(new PropertyValueFactory<Servicio, Time>("horaServicio"));
        colLugarServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("lugarServicio"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<Servicio, String>("telefonoContacto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Servicio, Integer>("codigoEmpresa"));
        
    }
    
    public Empresa buscarEmpresa(int codigoEmpresa){
        Empresa resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarEmpresa(?)");
            procedimiento.setInt(1, codigoEmpresa);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()){
                resultado = new Empresa(registro.getInt("codigoEmpresa"),
                                    registro.getString("nombreEmpresa"),
                                    registro.getString("direccion"),
                                    registro.getString("telefono"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
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
    
        public ObservableList<Empresa> getEmpresa(){
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpresas");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Empresa (resultado.getInt("codigoEmpresa"),
                            resultado.getString("nombreEmpresa"),
                            resultado.getString("direccion"),
                            resultado.getString("telefono")));
                
                

                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaEmpresa = FXCollections.observableArrayList(lista);
    }
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/diegomorales/image/Guardar.png"));
                imgEliminar.setImage(new Image("/org/diegomorales/image/Cancelar.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                break;
                
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image ("/org/diegomorales/image/Create.png"));
                imgEliminar.setImage(new Image ("/org/diegomorales/image/Delete.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    } 
    
   public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image ("/org/diegomorales/image/Create.png"));
                imgEliminar.setImage(new Image ("/org/diegomorales/image/Delete.png"));
                 tipoDeOperacion = operaciones.NINGUNO;
                break;
                
            default:
                
               if (tblServicios.getSelectionModel().getSelectedItem() != null){
                   int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar servicio", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                   if(respuesta == JOptionPane.YES_OPTION){
                       try{
                           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarServicio (?)");
                           procedimiento.setInt(1, ((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio());
                           procedimiento.execute();
                           listaServicio.remove(tblServicios.getSelectionModel().getSelectedItem());
                           limpiarControles();
                       }catch(Exception e){
                           e.printStackTrace();
                       }
                   }
               }else{
                   JOptionPane.showMessageDialog(null, "Debe de seleccionar un elemento");
               }
            
        }
    } 

   
    public void editar(){
        switch (tipoDeOperacion){
            case NINGUNO:
                if(tblServicios.getSelectionModel().getSelectedItem() != null){
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image ("/org/diegomorales/image/Actualizar.png"));
                    imgReporte.setImage(new Image ("/org/diegomorales/image/Cancelar.png"));
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un elemento");
                    
                }
                
                break;
              
            case ACTUALIZAR:
                actualizar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image ("/org/diegomorales/image/Update.png"));
                imgReporte.setImage(new Image ("/org/diegomorales/image/Report.png"));
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
                
        }
    }   
    
    public void reporte(){
        switch(tipoDeOperacion){
            case ACTUALIZAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("/org/diegomorales/image/Update.png"));
                imgReporte.setImage(new Image("/org/diegomorales/image/Report.png"));
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }

    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarServicio (?,?,?,?,?,?,?)");
            Servicio registro = new Servicio();
            LocalTime selecTime = tpHoraServicio.getValue();
            java.sql.Time sqlTime = java.sql.Time.valueOf(selecTime);
            
            registro.setCodigoServicio(Integer.parseInt(txtCodigoServicio.getText()));
            registro.setFechaServicio(fecha.getSelectedDate());
            registro.setTipoServicio(txtTipoServicio.getText());
            registro.setHoraServicio(sqlTime);
            registro.setLugarServicio(txtLugarServicio.getText());
            registro.setTelefonoContacto(txtTelefonoContacto.getText());
            registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
            
            procedimiento.setInt(1, registro.getCodigoServicio());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaServicio().getTime()));
            procedimiento.setString(3, registro.getTipoServicio());
            procedimiento.setTime(4, new java.sql.Time(registro.getHoraServicio().getTime()));
            procedimiento.setString(5, registro.getLugarServicio());
            procedimiento.setString(6, registro.getTelefonoContacto());
            procedimiento.setInt(7, registro.getCodigoEmpresa());
            procedimiento.execute();
            listaServicio.add(registro);
        }catch(Exception e){
            e.printStackTrace();
    }
        
    }
   
   
    public void guardar(){
        Servicio registro = new Servicio();
        LocalTime selecTime = tpHoraServicio.getValue();
        java.sql.Time sqlTime = java.sql.Time.valueOf(selecTime);
        
        registro.setFechaServicio(fecha.getSelectedDate());
        registro.setTipoServicio(txtTipoServicio.getText());
        registro.setHoraServicio(sqlTime);
        registro.setLugarServicio(txtLugarServicio.getText());
        registro.setTelefonoContacto(txtTelefonoContacto.getText());
        registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarServicio(?,?,?,?,?,?) ");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaServicio().getTime()));
            procedimiento.setString(2, registro.getTipoServicio());
            procedimiento.setTime(3, new java.sql.Time(registro.getHoraServicio().getTime()));
            procedimiento.setString(4, registro.getLugarServicio());
            procedimiento.setString(5, registro.getTelefonoContacto());
            procedimiento.setInt(6, registro.getCodigoEmpresa());
            procedimiento.execute();
            listaServicio.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
       
    public void seleccionarElemento(){
        txtCodigoServicio.setText(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio()));
        fecha.selectedDateProperty().set(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getFechaServicio());
        txtTipoServicio.setText(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTipoServicio()));
        tpHoraServicio.promptTextProperty().set(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getHoraServicio()));
        txtLugarServicio.setText(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getLugarServicio()));
        txtTelefonoContacto.setText(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTelefonoContacto()));
        cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
    }
    
    public void desactivarControles(){
        txtCodigoServicio.setDisable(false);
        txtTipoServicio.setDisable(false);
        txtLugarServicio.setDisable(false);
        txtTelefonoContacto.setDisable(false);
        cmbCodigoEmpresa.setDisable(false);
        grpFecha.setDisable(false);
        tpHoraServicio.setDisable(false);
    }
    
    public void activarControles(){
        txtCodigoServicio.setEditable(false);
        txtTipoServicio.setEditable(true);
        txtLugarServicio.setEditable(true);
        txtTelefonoContacto.setEditable(true);
        cmbCodigoEmpresa.setDisable(false);
        grpFecha.setDisable(false);
        tpHoraServicio.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoServicio.setText("");
        txtTipoServicio.setText("");
        txtLugarServicio.setText("");
        txtLugarServicio.setText("");
        txtTelefonoContacto.setText("");
        cmbCodigoEmpresa.setValue(null);
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
