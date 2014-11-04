package practica5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TablaOperandos {
    
    ArrayList <CodigoOperacion> tabop;
    
    public TablaOperandos(String archivo)
    {
        try {
            cargarTabOp(archivo);
        } catch (Exception ex) {
            Logger.getLogger(TablaOperandos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void cargarTabOp(String txt) throws Exception
    {
        File archivo = new File(txt);
        FileReader lector = new FileReader(archivo);
        BufferedReader buffer = new BufferedReader(lector);
            
        tabop = new ArrayList<>();
        String linea;
        String[] sub;
        
        String codop;
        CodigoOperacion co;
        Direccionamiento dir;
        
        boolean encontrado = false;
        
        while((linea = buffer.readLine())!= null)
        {
            if(!linea.equals(""))
            {
                sub = linea.split("/");
                codop = sub[0];
                dir = new Direccionamiento();
                dir.tipo = sub[1];
                dir.codMaq = sub [2];
                dir.tamanho = Integer.parseInt(sub[3]);
                
                for (CodigoOperacion codigo : tabop) {
                    if(codigo.codigo.equals(codop)){
                        codigo.modosDir.add(dir);
                        encontrado = true;
                        break;
                    }
                }
                if(!encontrado) {
                    co = new CodigoOperacion();
                    co.codigo = codop;
                    co.modosDir.add(dir);
                    tabop.add(co);
                }
                encontrado = false;
            }
        }
        buffer.close();
        lector.close();
    }
    
    public CodigoOperacion buscarCodigo(String codop)
    {
        for (CodigoOperacion tabop1 : tabop) {
            if (tabop1.codigo.equalsIgnoreCase(codop)) {
                return tabop1;
            }
        }
        return null;
    }
}
