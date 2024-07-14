/*
Nombres: Diego Alexander 
Apellidos: Morales Dieguez 
Código Técnico: IN5BV
Carné: 2022171
Fecha de creación: 14/04/2023
Fecha de modificación:  
 */
package org.diegomorales.main;

import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import org.diegomorales.controller.EmpleadoController;
import org.diegomorales.controller.EmpresaController;
import org.diegomorales.controller.LoginController;
import org.diegomorales.controller.MenuPrincipalController;
import org.diegomorales.controller.PlatoController;
import org.diegomorales.controller.PresupuestoController;
import org.diegomorales.controller.ProductoController;
import org.diegomorales.controller.ProductosHasPlatosController;
import org.diegomorales.controller.ProgramadorController;
import org.diegomorales.controller.ReporteGeneralController;
import org.diegomorales.controller.ServicioController;
import org.diegomorales.controller.ServiciosHasEmpleadosController;
import org.diegomorales.controller.ServiciosHasPlatosController;
import org.diegomorales.controller.TipoEmpleadoController;
import org.diegomorales.controller.TipoPlatoController;
import org.diegomorales.controller.UsuarioController;
/**
 *
 * @author informatica
 */
public class Principal extends Application {
    private final String PAQUETE_VISTA = "/org/diegomorales/view/";
    private Stage escenarioPrincipal;
    private Scene escena;
    
    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Tony's Kinal 2023");
        escenarioPrincipal.getIcons().add(new Image("/org/diegomorales/image/SegundoLogo.png"));
//        Parent root = FXMLLoader.load(getClass().getResource("/org/diegomorales/view/ProgramadorView.fxml"));
//        Parent root = FXMLLoader.load(getClass().getResource("/org/diegomorales/view/MenuPrincipalView.fxml"));
        
//        Parent root = FXMLLoader.load(getClass().getResource("/org/diegomorales/view/EmpresaView.fxml"));
//        Scene escena = new Scene (root);
//        escenarioPrincipal.setScene(escena);
        login();
        escenarioPrincipal.show();

        
    }
        public void menuPrincipal(){
        try{
            MenuPrincipalController menu = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml",484,406);
            menu.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaProgramador(){
        try{
            ProgramadorController programador = (ProgramadorController)cambiarEscena("ProgramadorView.fxml",533 ,580);
            programador.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaEmpresa (){
        try{
            EmpresaController empresa = (EmpresaController)cambiarEscena("EmpresaView.fxml", 832,603);
            empresa.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void ventanaTipoEmpleado(){
        try{
            TipoEmpleadoController tipoempleado = (TipoEmpleadoController)cambiarEscena("TipoEmpleadoView.fxml",832,603);
            tipoempleado.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaTipoPlato(){
        try{
            TipoPlatoController tipoplato = (TipoPlatoController) cambiarEscena ("TipoPlatoView.fxml",894,603);
            tipoplato.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaProducto(){
        try{
            ProductoController producto = (ProductoController) cambiarEscena ("ProductoView.fxml",832,603);
            producto.setEscenarioPrincipa(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaPresupuesto(){
        try{
            PresupuestoController presupuesto = (PresupuestoController) cambiarEscena ("PresupuestoView.fxml",892,603);
            presupuesto.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaEmpleado(){
        try{
            EmpleadoController empleado = (EmpleadoController) cambiarEscena ("EmpleadoView.fxml",1285,603);
            empleado.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaReporteGeneral(){
        try{
            ReporteGeneralController reporte = (ReporteGeneralController) cambiarEscena ("ReporteGeneralView.fxml",743,603);
            reporte.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaPlato(){
        try{
            PlatoController plato = (PlatoController) cambiarEscena ("PlatoView.fxml", 1059,603);
            plato.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void login(){
        try{
            LoginController login = (LoginController) cambiarEscena ("LoginView.fxml" ,636,661 );
            login.setEscenarioPrincipal(this);
        }catch(Exception e){
           e.printStackTrace();
        }
    }
    
    public void ventanaUsuario(){
        try{
            UsuarioController usuarioController = (UsuarioController)cambiarEscena ("UsuarioView.fxml" ,636,661);
            usuarioController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaProductoHasPlatos(){
        try{
            ProductosHasPlatosController productosHasPlatos = (ProductosHasPlatosController) cambiarEscena ("ProductosHasPlatosView.fxml",795,603);
            productosHasPlatos.setEscenarioPrincipal(this);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaServiciosHasPlatos(){
        try{
            ServiciosHasPlatosController serviciosHasPlatos = (ServiciosHasPlatosController) cambiarEscena("ServiciosHasPlatosView.fxml",795,603);
            serviciosHasPlatos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void ventanaServicio(){
        try{
            ServicioController servicio = (ServicioController) cambiarEscena ("ServicioView.fxml",1059,603);
            servicio.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaServiciosHasEmpleados(){
        try{
            ServiciosHasEmpleadosController serviciosHasEmpleados = (ServiciosHasEmpleadosController) cambiarEscena("ServiciosHasEmpleadosView.fxml",990,603);
            serviciosHasEmpleados.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo),ancho,alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        
        return resultado;
    }
}
