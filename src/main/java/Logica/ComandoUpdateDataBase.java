/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import Datos.FABRICADAO;
import Datos.IDAOProfesor;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @author el_je_000
 */
public class ComandoUpdateDataBase extends ComandoGeneral{
    @Override
    public Object Ejecutar (Object parametro)
    {
        try
        {
           System.out.println(" LLAMADO A FABRICA OBTENER DAOCrearPreferencia " + parametro.toString());
           IDAOProfesor _daoProfesor;
           System.out.println("LLAMAR a FABRICADAO");
           _daoProfesor = FABRICADAO.ObtenerDAOProfesor();
           //System.out.println("clases "+System.getProperty("java.class.path"));           
           boolean Update = _daoProfesor.UpdateDatabase();                           
           return Update;                      
        }
        catch (UnsupportedOperationException e)
        {
             System.out.println("ENTRO EN LA EXCEPTION DE COMANDO Update data base");
             JsonParser parser = new JsonParser();
             JsonObject o = (JsonObject)parser.parse("{\"respuesta\": \"No se pudo cargar las preferencias\"}");
             return o.toString();                      
        }
    }
    
    
}
