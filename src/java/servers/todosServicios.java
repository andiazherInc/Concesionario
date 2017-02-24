/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servers;

import com.sysware.concesionario.App;
import com.sysware.concesionario.entitie.Entitie;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
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
public class todosServicios extends HttpServlet {

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
                String name="Sin nombre";
                ArrayList<Entitie> servicios= new ArrayList<>();
                String fi="";
                String ff="";
                String placa="";
                String cliente="";
                String os="";
                try{
                    fi= request.getParameter("fi");
                    ff= request.getParameter("ff");
                    placa= request.getParameter("placa");
                    cliente= request.getParameter("cliente");
                    os= request.getParameter("os");
                }catch(NullPointerException s){
                    System.out.println("Error: "+s);
                }
                Entitie servicio = new Entitie(App.TABLE_ORDENSERVICIO);
                name= "RESULTADOS PARA";
                boolean nada=false;
                ArrayList<String> param1=new ArrayList<>();
                ArrayList<String> param2=new ArrayList<>();
                ArrayList<String> operation=new ArrayList<>();
                
                if(!fi.equals("")){
                    name+=" DESPUES DE: </b>"+fi+"<b>";
                    param1.add("FECHA");
                    param2.add(fi);
                    operation.add(">=");
                    nada=true;
                }
                if(!ff.equals("")){
                    if(nada){
                        name+=",";
                    }
                    name+=" ANTES DE: </b>"+ff+"<b> ";
                    param1.add("FECHA");
                    param2.add(ff);
                    operation.add("<=");
                    nada=true;
                }
                if(!placa.equals("")){
                    if(nada){
                        name+=",";
                    }
                    name+=" PLACA: </b>"+placa+"<b> ";
                    Entitie vehi = new Entitie(App.TABLE_VEHICULO);
                    vehi= vehi.getEntitieParam("PLACA", placa).get(0);
                    param1.add("VEHICULO");
                    param2.add(vehi.getId());
                    operation.add("=");
                    nada=true;
                }
                if(!cliente.equals("")){
                    if(nada){
                        name+=",";
                    }
                    name+=" CLIENTE: </b>"+cliente+"<b> ";
                    Entitie client = new Entitie(App.TABLE_PROPIETARIO);
                    client= client.getEntitieParam("CEDULA", cliente).get(0);
                    param1.add("PROPIETARIO");
                    param2.add(client.getId());
                    operation.add("=");
                    nada=true;
                }
                if(!os.equals("")){
                    if(nada){
                        name+=" Y ";
                    }
                    param1.add("ID");
                    param2.add(os);
                    operation.add("=");
                    name+=" OS: </b>"+os+"<b> ";
                }
                
                
                servicios = servicio.getEntitieParams(param2, param2, operation);
                
                try (PrintWriter out = response.getWriter()) {
                    String idOrder="-1";
                    out.println("<h4 class=\"card-title text-center\" id=\"titleContend\"> <b> "+name+" </b> </h4>");
                    out.println("<table class=\"table\">");
                    out.println("<thead class=\"\">\n" +
"                                                <th>No</th>\n" +
"                                                <th>Fecha</th>\n" +
"                                                <th>Documento</th>\n" +
"                                                <th>Placa</th>\n" +
"                                                <th>Servicios</th>\n" +
"                                                <th>Canal</th>\n" +
"                                            </thead>");
                    out.println("<tbody>");
                    for(Entitie i: servicios){
                        if(!idOrder.equals(i.getId())){
                            String a="";
                            a+="onclick=\"openViewOrderService("+i.getId()+")\"";
                            out.println("<tr>");
                            out.println("<td><a href=\"#OS"+i.getId()+"\" class=\"\" "+a+">"+i.getId()+"</a></td>");
                            out.println("<td>"+i.getDataOfLabel("FECHA")+"</td>");
                            try{
                                Entitie propietario = new Entitie(App.TABLE_PROPIETARIO);
                                propietario.getEntitieID(i.getDataOfLabel("PROPIETARIO"));
                                out.println("<td>"+propietario.getDataOfLabel("TIPODOC")
                                        +propietario.getDataOfLabel("CEDULA")+"</td>");
                                Entitie vehiculo = new Entitie(App.TABLE_VEHICULO);
                                vehiculo.getEntitieID(i.getDataOfLabel("VEHICULO"));
                                out.println("<td>"+vehiculo.getDataOfLabel("PLACA")+"</td>");
                                Entitie osdetalle = new Entitie(App.TABLE_OSDETALLE);
                                ArrayList<Entitie> detalle = osdetalle.getEntitieParam("OS", i.getId());
                                int cant=detalle.size();
                                int cant2 = 0;
                                for(Entitie j: detalle){
                                    if(j.getDataOfLabel("ESTADO").equals("2")){
                                        cant2++;
                                    }
                                }
                                out.println("<td>"+cant+" | "+(cant2)+"</td>");
                                Entitie canal = new Entitie(App.TABLE_CANALES);
                                canal.getEntitieID(i.getDataOfLabel("ID_CANAL"));
                                out.println("<td>"+canal.getDataOfLabel("NOMBRE")+"</td>");
                                out.println("</tr>");
                                idOrder = i.getId();
                            }catch(IndexOutOfBoundsException s){
                                System.out.println("Error:"+s);
                            }
                            
                        }
                        
                    }
                    out.println("</tbody>");
                    out.println("</table>");
                }
            }
            else{
                response.sendRedirect("login.jsp?validate=Por+favor+ingresar+credenciales");
            }
        }
        catch(NullPointerException e){
            response.sendRedirect("login.jsp?validate=Por+favor+ingresar+credenciales");
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
            Logger.getLogger(todosServicios.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(todosServicios.class.getName()).log(Level.SEVERE, null, ex);
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