/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sysware.concesionario.servers;

import com.sysware.concesionario.app.App;
import com.sysware.concesionario.entitie.Entitie;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author andre
 */
public class DataPolizasAnuRenAction extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try{
            if(request.getSession().getAttribute("session").equals("true")){
                String menu="0";
                String servicio="";
                String poliza= "";
                try{
                    menu=request.getParameter("menu");
                    servicio= request.getParameter("servicio");
                    poliza= request.getParameter("poliza");
                }catch(IndexOutOfBoundsException s){
                    s.printStackTrace();
                }
                ///PROCESO DE ANULZACIÓN DE LAS POLIZAS
                if(menu.equals("anular")){
                   if(servicio.equals("1")){
                       Entitie rsoat = new Entitie(App.TABLE_REGISTROSOAT);
                       try{
                           rsoat = rsoat.getEntitieParam("POLIZA", poliza).get(0);
                           rsoat.getData().set(rsoat.getColums().indexOf("ESTADOP"),"ANULADA");
                           rsoat.update();
                       }catch(IndexOutOfBoundsException s){
                           s.printStackTrace();
                       }
                   }
                   if(servicio.equals("2")){
                       Entitie ad = new Entitie(App.TABLE_ASIS_DENTAL);
                       try{
                           ad = ad.getEntitieParam("POLIZA", poliza).get(0);
                           ad.getData().set(ad.getColums().indexOf("ESTADO"), "3");
                           ad.getData().set(ad.getColums().indexOf("ESTADOPOL"), "ANULADA");
                           ad.update();
                       }catch(IndexOutOfBoundsException s){
                           s.printStackTrace();
                       }
                   }
                }
                
                //PROCESO DE RENOVACION DE LAS POLIZAS
                if(menu.equals("anular")){
                   if(servicio.equals("1")){
                       Entitie rsoat = new Entitie(App.TABLE_REGISTROSOAT);
                       try{
                           rsoat = rsoat.getEntitieParam("POLIZA", poliza).get(0);
                           rsoat.getData().set(rsoat.getColums().indexOf("ESTADOP"),"ANULADA");
                           rsoat.update();
                       }catch(IndexOutOfBoundsException s){
                           s.printStackTrace();
                       }
                   }
                   if(servicio.equals("2")){
                       Entitie ad = new Entitie(App.TABLE_ASIS_DENTAL);
                       try{
                           ad = ad.getEntitieParam("POLIZA", poliza).get(0);
                           ad.getData().set(ad.getColums().indexOf("ESTADO"), "3");
                           ad.getData().set(ad.getColums().indexOf("ESTADOPOL"), "ANULADA");
                           ad.update();
                       }catch(IndexOutOfBoundsException s){
                           s.printStackTrace();
                       }
                   }
                }
                
            }
            else{
                response.sendRedirect("login.jsp?validate=Por+favor+ingresar+credenciales");
            }
        }catch(NullPointerException e){
            response.sendRedirect("login.jsp?validate=Por+favor+ingresar+credenciales");
            e.printStackTrace();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(DataPolizasAnuRenAction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(DataPolizasAnuRenAction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
