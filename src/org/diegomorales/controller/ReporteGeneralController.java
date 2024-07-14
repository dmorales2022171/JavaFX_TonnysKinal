/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diegomorales.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import org.diegomorales.bean.Empresa;
import org.diegomorales.db.Conexion;
import org.diegomorales.main.Principal;
import org.diegomorales.report.GenerarReporte;

/**
 *
 * @author User
 */
public class ReporteGeneralController implements Initializable {
    private Principal escenarioPrincipal;
    private ObservableList<Empresa> listaEmpresa;
    private final String fondo = "/org/diegomorales/image/fondo.PNG";
    private final String SegundoLogo = "/org/diegmorales/image/SegundoLogo.png";
    
    @FXML private ComboBox cmbCodigoEmpresa;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbCodigoEmpresa.setItems(getEmpresa());
    }
    
    public ObservableList<Empresa> getEmpresa(){
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpresas");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empresa(resultado.getInt("codigoEmpresa"),
                                        resultado.getString("nombreEmpresa"),
                                        resultado.getString("direccion"),
                                        resultado.getString("telefono")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEmpresa = FXCollections.observableArrayList(lista);
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        int codEmpresa = Integer.valueOf(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        parametros.put("codEmpresa", codEmpresa);
        parametros.put("fondo", this.getClass().getResourceAsStream(fondo));
        parametros.put("SegundoLogo", this.getClass().getResourceAsStream(SegundoLogo));
        GenerarReporte.mostrarReporte("ReporteGeneral.jasper", "Reporte General", parametros);
        
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
