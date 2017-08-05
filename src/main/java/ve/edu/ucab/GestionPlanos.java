/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.edu.ucab;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Base64;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
/**
 *
 * @author el_je_000
 */
@Path("/v1")
public class GestionPlanos {
    public static final String FILE_SERVER_LOCATION = "C:\\Users\\el_je_000\\Documents\\NetBeansProjects\\programacion-plano\\src\\main\\webapp\\planos\\"; 

    // http://localhost:8080/file-service/api/v1/upload
    @POST
    @Path("/planos/carga")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("uploadFile") InputStream fileInputStream,
            @FormDataParam("uploadFile") FormDataContentDisposition fileFormDataContentDisposition) {

        String fileName = null;
        String uploadFilePath = null;

        try {
            fileName = fileFormDataContentDisposition.getFileName();
            uploadFilePath = writeToFileServer(fileInputStream, fileName);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            // Liberar recursos en caso de ser necesario...
        }

        return Response.ok("El archivo fue cargado exitosamente").build();
    }

    /**
     * Escribe un flujo de entrada (input stream) al "servidor de archivos"
     *
     * @param inputStream
     * @param fileName
     * @throws IOException
     */
    private String writeToFileServer(InputStream inputStream, String fileName) throws IOException {

        OutputStream outputStream = null;
        String qualifiedUploadFilePath = FILE_SERVER_LOCATION + fileName;

        try {
            // ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
             outputStream = new FileOutputStream(new File(qualifiedUploadFilePath));
            int read = 0;
            byte[] bytes = new byte[1024];
            
            while ((read = inputStream.read(bytes)) != -1) {
                // bos.write(bytes, 0, read);
                 System.out.println("read " + read + " bytes,");
                 outputStream.write(bytes, 0, read);
            }
               // byte[] buf = bos.toByteArray();
             
             
            // byte[] encoded = Base64.getEncoder().encode(buf);
             //System.out.println("Base64 string" + " " + encoded);
             
             //String baseencoded = Base64.getEncoder().encodeToString(buf);
             
             //System.out.println("base64 encoded" + " " + baseencoded);

          
            outputStream.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            // Liberar recursos en caso de ser necesario...
            outputStream.close();
        }
        return qualifiedUploadFilePath;
    }
}
