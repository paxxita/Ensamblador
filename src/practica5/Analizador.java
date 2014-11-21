package practica5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JTextArea;

public class Analizador {
    
    HashMap<String,String> simbolos;
    TablaOperandos tabop;
    ArrayList <Linea> ASM;
    JTextArea area;
    
    Auxiliar ventana = new Auxiliar();
    String salida = "";
    String salidaET = ""; 
    
    int dirIni;
    int contLoc;
    boolean existeEnd;
    
    String dirIniHex = " ";
    String contLocHex = " ";
    
    public void inicializar()
    {
        ASM = new ArrayList<>();
        simbolos = new HashMap<>();
        area.setText("");
        salida = "";
        salidaET = "";
        
        dirIni = 0;
        contLoc = 0;
        
        dirIniHex = " ";
        contLocHex = " ";
    }
    public void cargarASM (String archivoNombre) throws Exception
    {
        File archivo = new File(archivoNombre);
        FileReader lector = new FileReader(archivo);
        BufferedReader buffer = new BufferedReader(lector);
                      
        inicializar();
        
        String linea;
        Linea lineaASM;
        
        while((linea = buffer.readLine())!= null)
        {
            lineaASM = new Linea(linea);
            ASM.add(lineaASM);
        }
        
        buffer.close();
        lector.close();
    }
    public void cargarTabOp(String tabOP)
    {
        tabop = new TablaOperandos(tabOP);
    }
    public void analizarASM()
    {
        CodigoOperacion co;
        for(Linea linea: ASM)
        {
            linea.analizaLinea();
            if(!linea.errorCodop)
            {
                co = tabop.buscarCodigo(linea.codop);
                contLoc = linea.ensamblarPrimerPaso(simbolos, co, contLoc);        
            }
        }
        for(Linea linea: ASM)
        {
            if(!linea.errorCodop)
            {
                co = tabop.buscarCodigo(linea.codop);
                linea.ensamblarSegundoPaso(simbolos, co);
                if(!linea.errorCodop && !linea.errorEtiqueta && !linea.errorOperando){
                    salida += linea.impresion; 
                    if (linea.codop.equalsIgnoreCase("EQU"))
                       salidaET += "EQU (ABSOLUTA) \t" + linea.etiqueta + "\t" + linea.contLocHex.toUpperCase() + "\n"; 
                    else if(!linea.etiqueta.equals(" "))
                        salidaET += "CONT_LOC (RELATIVA)\t"+ linea.etiqueta + "\t" + linea.contLocHex.toUpperCase()+ "\n";
                }
            }
            area.append(linea.imprimirComandos());
        }        
        ventana.jTextArea1.setText(salida);
        ventana.jTextArea2.setText(salidaET);
        ventana.show();
    }
    
    public void imprimirLineas()
    {
        for(Linea linea: ASM)
        {
            area.append(linea.imprimirComandos());
        }
    }
}
