/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.edu.ucab;

import WebService.HorarioRecurso;
import WebService.ProfesorRecurso;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 *
 * @author el_je_000
 */
@Path("/v1")
public class ProgramacionAcademica {
     static int numeroProfesores = 0;
    public static final String FILE_SERVER_LOCATION = "C:/Users/el_je_000/Documents/Prueba-carga/"; 

    // http://localhost:8080/file-service/api/v1/upload
    @POST
    @Path("/programacionacademica/carga")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("uploadFile") InputStream fileInputStream,
            @FormDataParam("uploadFile") FormDataContentDisposition fileFormDataContentDisposition) throws Exception {

        String fileName = null;
        String uploadFilePath = null;

        try {
            fileName = fileFormDataContentDisposition.getFileName();
            uploadFilePath = writeToFileServer(fileInputStream, fileName);
            ReadFile(uploadFilePath);            
            
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            // Liberar recursos en caso de ser necesario...
        }

        return Response.ok("El archivo fue cargado exitosamente en " + uploadFilePath).build();
    }
    // http://localhost:8080/file-service/api/v1/upload
    @POST
    @Path("/programacionacademica/edicion")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response EditFile(
            @FormDataParam("uploadFile") InputStream fileInputStream,
            @FormDataParam("uploadFile") FormDataContentDisposition fileFormDataContentDisposition) throws Exception {

        String fileName = null;
        String uploadFilePath = null;

        try {
            fileName = fileFormDataContentDisposition.getFileName();
            Response create = ProfesorRecurso.UpdateDataBase();
            uploadFilePath = writeToFileServer(fileInputStream, fileName);
            ReadFile(uploadFilePath);            
            
        } catch (IOException ioe) {
           ioe.printStackTrace();
        } finally {
            // Liberar recursos en caso de ser necesario...
        }

        return Response.ok("El archivo fue cargado exitosamente en " + uploadFilePath).build();
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
            outputStream = new FileOutputStream(new File(qualifiedUploadFilePath));
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

            outputStream.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            outputStream.close();
        }
        return qualifiedUploadFilePath;
    }
    
    private static void ReadFile(String filePath) throws Exception{
        File f = new File(filePath);
        try {
            Workbook wb = Workbook.getWorkbook(f);
            Sheet s = wb.getSheet(0);
            int row = s.getRows();
            int col = s.getColumns();
            List<String> profesores = new ArrayList<>();
            for (int i = 0; i< row;i++){
                for (int j = 0; j<col;j++){
                    Cell c = s.getCell(j, i);
                    if("PROFESOR".equals(c.getContents())){
                        for (int fila = 1; fila<row; fila++){
                            Cell cp = s.getCell(j, fila);
                            if(!profesores.contains(cp.getContents())){
                                profesores.add(cp.getContents());
                            }                    
                        }                    
                        String consultar_profesor = ReadList(profesores,filePath);
                        System.out.println(consultar_profesor);
                    }

                }
                System.out.println("");
            }
        }
        catch(Exception e){
            throw e;
        }        
    }
    private static String ReadList(List<String> profesores,String path) throws Exception{
        numeroProfesores = profesores.size();
        for (int i= 0; i<profesores.size();i++){
            System.out.println(profesores.get(i) + " hay " + profesores.size() + " profesores en el file");
            try{
                Response create = ProfesorRecurso.createProfesores(profesores.get(i));
            }catch(Exception e)
            {
                throw e;
            }
            ReadFileInformation(path,profesores.get(i));            
        }
        return "Profesores leidos correctamente";
    }
    private static void ReadFileInformation(String filePath,String profesor) throws Exception{
        File f = new File(filePath);
        try {
        Workbook wb = Workbook.getWorkbook(f);
        Sheet s = wb.getSheet(0);
        int row = s.getRows();
        int col = s.getColumns();
        List<String> materias = new ArrayList<>();
        for (int i = 0; i< row;i++){
            for (int j = 0; j<col;j++){
                Cell c = s.getCell(j, i);
                if(profesor.equals(c.getContents())){
                   System.out.print(c.getContents()+"\t\t");
                    for (int p = 0; p<col ;p++){
                        Cell profesorlist = s.getCell(p, i);
                        Cell header = s.getCell(p, 0);
                        materias.add("{"+profesorlist.getContents()+"}");
                    }
                }                
            }                   
            System.out.println("");
        }
        String numeroString="";
        if (Integer.toString(numeroProfesores).length()==1){
            numeroString = "0"+Integer.toString(numeroProfesores);
        }else {
            numeroString = Integer.toString(numeroProfesores);
        }
         Response create = HorarioRecurso.createHorario(materias.toString()+numeroString);
        }
        catch(Exception e){
            throw e;
        }        
    }

    private static void ReadListMat(List<String> Lista) throws Exception{
            for (int i = 0; i<Lista.size() ;i++){
                System.out.println("materias "+ Lista.get(i));
            }
    }
    
}
