
package org.diegomorales.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
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
import org.diegomorales.bean.Plato;
import org.diegomorales.bean.Servicio;
import org.diegomorales.bean.ServiciosHasPlatos;
import org.diegomorales.db.Conexion;
import org.diegomorales.main.Principal;


public class ServiciosHasPlatosController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones {NUEVO, GUARDAR, ACUALIZAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <ServiciosHasPlatos> listaServiciosHasPlatos;
    private ObservableList <Plato> listaPlato;
    private ObservableList <Servicio> listaServicio;
    
    @FXML private TextField txtCodigoServiciosHas;
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private ComboBox cmbCodigoServicio;
    @FXML private TableView tblServiciosHas;
    @FXML private TableColumn colCodigoHasServicios;
    @FXML private TableColumn colCodigoPlato;
    @FXML private TableColumn colCodigoServicio;
    @FXML private Button btnNuevo;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgReporte;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoPlato.setItems(getPlato());
        cmbCodigoServicio.setItems(getServicio());
    }
    
    public void cargarDatos(){
        tblServiciosHas.setItems(getServiciosHasPlatos());
        colCodigoHasServicios.setCellValueFactory(new PropertyValueFactory<ServiciosHasPlatos, Integer>("Servicios_codigoServicio"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<ServiciosHasPlatos, Integer>("codigoPlato"));
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServiciosHasPlatos, Integer>("codigoServicio"));
        
    }
    
    public void seleccionarElemento(){
        txtCodigoServiciosHas.setText(String.valueOf(((ServiciosHasPlatos)tblServiciosHas.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
        cmbCodigoPlato.getSelectionModel().select(buscarPlato(((ServiciosHasPlatos)tblServiciosHas.getSelectionModel().getSelectedItem()).getCodigoPlato()));
        cmbCodigoServicio.getSelectionModel().select(buscarServicio(((ServiciosHasPlatos)tblServiciosHas.getSelectionModel().getSelectedItem()).getCodigoServicio()));        
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
    
    public Plato buscarPlato(int codigoPlato){
        Plato resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarPlato (?)");
            procedimiento.setInt(1, codigoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()){
                resultado = new Plato (registro.getInt("codigoPlato"),
                                        registro.getInt("cantidad"),
                                        registro.getString("nombrePlato"),
                                        registro.getString("descripcionPlato"),
                                        registro.getDouble("precioPlato"),
                                        registro.getInt("codigoTipoPlato"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public ObservableList <ServiciosHasPlatos> getServiciosHasPlatos(){
        ArrayList <ServiciosHasPlatos> lista = new ArrayList <ServiciosHasPlatos>();
        try{
            PreparedStatement procedimiento= Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicios_has_Platos");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new ServiciosHasPlatos (resultado.getInt("Servicios_codigoServicio"),
                                                    resultado.getInt("codigoPlato"),
                                                    resultado.getInt("codigoServicio")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaServiciosHasPlatos = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList <Plato> getPlato(){
        ArrayList <Plato> lista = new ArrayList <Plato>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarPlatos");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Plato (resultado.getInt("codigoPlato"),
                                        resultado.getInt("cantidad"),
                                        resultado.getString("nombrePlato"),
                                        resultado.getString("descripcionPlato"),
                                        resultado.getDouble("precioPlato"),
                                        resultado.getInt("codigoTipoPlato")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaPlato = FXCollections.observableArrayList(lista);
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
                btnReporte.setText(".");
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
        ServiciosHasPlatos registro = new ServiciosHasPlatos();
        registro.setServicios_codigoServicio(Integer.parseInt(txtCodigoServiciosHas.getText()));
        registro.setCodigoPlato(((Plato)cmbCodigoPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
        registro.setCodigoServicio(((Servicio)cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarServicio_has_Platos(?,?,?)");
            procedimiento.setInt(1, registro.getServicios_codigoServicio());
            procedimiento.setInt(2, registro.getCodigoPlato());
            procedimiento.setInt(3, registro.getCodigoServicio());
            procedimiento.execute();
            listaServiciosHasPlatos.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void desactivarControles(){
      txtCodigoServiciosHas.setDisable(false);
      cmbCodigoPlato.setDisable(true);
      cmbCodigoServicio.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigoServiciosHas.setDisable(false);
        cmbCodigoPlato.setDisable(false);
        cmbCodigoServicio.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoServiciosHas.setText("");
        cmbCodigoPlato.setValue(null);
        cmbCodigoServicio.setValue(null);
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
