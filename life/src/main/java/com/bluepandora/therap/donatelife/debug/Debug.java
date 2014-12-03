/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluepandora.therap.donatelife.debug;

/**
 *
 * @author Biswajit Debnath
 */
public class Debug {
    
    public static void debugLog(Object ...logMessage){
        for(Object log:logMessage){
            System.out.print(log +" ");
        }
        System.out.println("");
    }
    
}
