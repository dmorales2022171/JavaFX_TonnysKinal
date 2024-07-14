
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
import org.diegomorales.bean.Producto;
import org.diegomorales.bean.ProductosHasPlatos;
import org.diegomorales.db.Conexion;
import org.diegomorales.main.Principal;

public class ProductosHasPlatosController implements Initializable {
    private enum operaciones {NUEVO, GUARDAR, ACTUALIZAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <ProductosHasPlatos> listaProductosHasPlatos;
    private ObservableList <Plato> listaPlato;
    private ObservableList <Producto> listaProducto;
    private Principal escenarioPrincipal;
    
    @FXML private TextField txtCodigoProductosHas;
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private ComboBox cmbCodigoProducto;
    @FXML private TableView tblProductosHas;
    @FXML private TableColumn colCodigoHas;
    @FXML private TableColumn colCodigoPlato;
    @FXML private TableColumn colCodigoProducto;
    @FXML private Button btnNuevo;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoPlato.setItems(getPlato());
        cmbCodigoProducto.setItems(getProducto());
    }
    
    public void cargarDatos(){
        tblProductosHas.setItems(getProductosHasPlatos());
        colCodigoHas.setCellValueFactory(new PropertyValueFactory<ProductosHasPlatos, Integer>("Productos_codigoProducto"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<ProductosHasPlatos, Integer>("codigoPlato"));
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<ProductosHasPlatos, Integer>("codigoProducto"));

    }
    
    public void seleccionarElemento(){
        txtCodigoProductosHas.setText(String.valueOf(((ProductosHasPlatos)tblProductosHas.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        cmbCodigoProducto.getSelectionModel().select(buscarProducto(((ProductosHasPlatos)tblProductosHas.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        cmbCodigoPlato.getSelectionModel().select(buscarPlato(((ProductosHasPlatos)tblProductosHas.getSelectionModel().getSelectedItem()).getCodigoPlato()));        
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

    public Producto buscarProducto (int codigoProducto){
        Producto resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarProducto(?)");
            procedimiento.setInt(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()){
                resultado = new Producto (registro.getInt("codigoProducto"),
                                            registro.getString("nombreProducto"),
                                            registro.getInt("cantidad"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    
    public ObservableList <ProductosHasPlatos> getProductosHasPlatos(){
        ArrayList <ProductosHasPlatos> lista = new ArrayList <ProductosHasPlatos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarProducto_has_Platos");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new ProductosHasPlatos (resultado.getInt("Productos_codigoProducto"),
                                                    resultado.getInt("codigoPlato"),
                                                    resultado.getInt("codigoProducto")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaProductosHasPlatos = FXCollections.observableArrayList(lista);
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

    public ObservableList <Producto> getProducto(){
        ArrayList <Producto> lista = new ArrayList<Producto>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarProductos()");
            ResultSet resultado = procedimiento.executeQuery();
                    while (resultado.next()){
                    lista.add(new Producto (resultado.getInt("codigoProducto"),
                                resultado.getString("nombreProducto"),
                                resultado.getInt("cantidad")));
                            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaProducto = FXCollections.observableArrayList(lista);
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
        ProductosHasPlatos registro = new ProductosHasPlatos();
        registro.setProductos_codigoProducto(Integer.parseInt(txtCodigoProductosHas.getText()));
        registro.setCodigoPlato(((Plato)cmbCodigoPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
        registro.setCodigoProducto(((Producto)cmbCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarProducto_has_Platos(?,?,?)");
            procedimiento.setInt(1, registro.getProductos_codigoProducto());
            procedimiento.setInt(2, registro.getCodigoPlato());
            procedimiento.setInt(3, registro.getCodigoProducto());
            procedimiento.execute();
            listaProductosHasPlatos.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void desactivarControles(){
        txtCodigoProductosHas.setDisable(false);
        cmbCodigoPlato.setDisable(true);
        cmbCodigoProducto.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigoProductosHas.setDisable(false);
        cmbCodigoPlato.setDisable(false);
        cmbCodigoProducto.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoProductosHas.setText("");
        cmbCodigoPlato.setValue(null);
        cmbCodigoProducto.setValue(null);
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
