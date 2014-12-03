/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluepandora.therap.donatelife.adminpanel;

import com.bluepandora.therap.donatelife.debug.Debug;
import com.bluepandora.therap.donatelife.debug.LogMessageJson;
import com.bluepandora.therap.donatelife.jsonsender.SendJsonData;
import com.bluepandora.therap.donatelife.constant.Enum;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Biswajit Debnath
 */
@WebServlet(name = "AdminServlet", urlPatterns = {"/admin"})
public class AdminServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws java.io.IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JSONException {
        response.setContentType("text/html;charset=UTF-8");
        Debug.debugLog("ADMIN SERVLET HITTED FROM: "  +  request.getRemoteAddr());

        if (request.getParameter("requestName") != null && request.getParameter("requestFrom") != null) {
            if (request.getParameter("requestFrom").equals("AdMiN")) {

                String requestName = request.getParameter("requestName");

                if (requestName.equals(AdminRequest.requestDonatorList)) {
                    AdminService.getDonatorList(request, response);
                } else if (requestName.equals(AdminRequest.requestMobileNumberDetail)) {
                    AdminService.getMobileNumberDetail(request, response);
                } else if (requestName.equals(AdminRequest.requestAdminList)) {
                    AdminService.getAdminList(request, response);
                } else if (requestName.equals(AdminRequest.requestFeedBack)) {
                    AdminService.getFeedBackList(request, response);
                } else {
                    JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, "UNKNOWN SERVICE NAME!");
                    SendJsonData.sendJsonData(request, response, jsonObject);
                }
                
            } else {
                JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, "UNAUTHORIZE ACCESS!");
                SendJsonData.sendJsonData(request, response, jsonObject);
            }
        } else {
            JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, "UNAUTHORIZE ACCESS!");
            SendJsonData.sendJsonData(request, response, jsonObject);
        }

    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws java.io.IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);

        } catch (JSONException ex) {
            Logger.getLogger(AdminServlet.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws java.io.IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);

        } catch (JSONException ex) {
            Logger.getLogger(AdminServlet.class
                    .getName()).log(Level.SEVERE, null, ex);
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
