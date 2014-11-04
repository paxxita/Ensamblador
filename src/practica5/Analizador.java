package practica5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JTextArea;

public class Analizador {
    
    HashMap<String,String> simbolos = new HashMap<>();
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
    
    public void cargarASM (String archivoNombre) throws Exception
    {
        File archivo = new File(archivoNombre);
        FileReader lector = new FileReader(archivo);
        BufferedReader buffer = new BufferedReader(lector);
                      
        ASM = new ArrayList<>();
        
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
                if(linea.esDirectiva())
                {
                    linea.validarDirectiva(simbolos);
                    imprimirDirectivas(linea);
                }
                else
                {
                    co = tabop.buscarCodigo(linea.codop);
                    linea.validarCodOp(co,simbolos);  
                    if(!linea.errorCodop && !linea.errorEtiqueta && !linea.errorOperando && !linea.esComentario)
                    {
                        imprimirSalida(linea);
                        contLoc += linea.tam;
                        contLocHex = Integer.toHexString(contLoc);
                    }
                }
            }
            area.append(linea.imprimirComandos());
        }
        ventana.jTextArea1.setText(salida);
        ventana.jTextArea2.setText(salidaET);
        ventana.show();
    }
        
    public void imprimirDirectivas(Linea linea)
    {
        if(!linea.codop.equals(" ")){
            if(linea.codop.equalsIgnoreCase("ORG"))
                    {
                        dirIni = convertirDecimal(linea.operando, linea.sistNum);
                        dirIniHex = Integer.toHexString(dirIni);
                        contLoc = dirIni;
                        contLocHex = dirIniHex;
                        
                        salida += "DIR_INIC \t" + completarHexadecimal(dirIniHex).toUpperCase() +
                                "\t" + linea.imprimirComandosCortos();
                    }
            if(linea.codop.equalsIgnoreCase("DB")||linea.codop.equalsIgnoreCase("DC.B")||linea.codop.equalsIgnoreCase("FCB"))
            {
                int valor = convertirDecimal(linea.operando, linea.sistNum);
                imprimirSalida(linea);
                
                contLoc += linea.tam;
                contLocHex = Integer.toHexString(contLoc);
            }
            if(linea.codop.equalsIgnoreCase("DW")||linea.codop.equalsIgnoreCase("DC.W")||linea.codop.equalsIgnoreCase("FDB"))
            {
                imprimirSalida(linea);
                contLoc += linea.tam;
                contLocHex = Integer.toHexString(contLoc);
            }
            if(linea.codop.equalsIgnoreCase("FCC"))
            {
                imprimirSalida(linea);
                contLoc += linea.tam;
                contLocHex = Integer.toHexString(contLoc);
            }
            if(linea.codop.equalsIgnoreCase("DS")||linea.codop.equalsIgnoreCase("DS.B")||linea.codop.equalsIgnoreCase("RMB"))
            {
                imprimirSalida(linea);
                contLoc += linea.tam;
                contLocHex = Integer.toHexString(contLoc);
            }
            if(linea.codop.equalsIgnoreCase("DS.W")||linea.codop.equalsIgnoreCase("RMW"))
            {
                imprimirSalida(linea);
                contLoc += linea.tam;
                contLocHex = Integer.toHexString(contLoc);
            }
            if(linea.codop.equalsIgnoreCase("EQU"))
            {
                int valor = convertirDecimal(linea.operando, linea.sistNum);
                String valorHex = Integer.toHexString(valor);
                    
                salida += "VALOR EQU \t" + completarHexadecimal(valorHex).toUpperCase() + "\t" + linea.imprimirComandosCortos();
                salidaET += "EQU (ABSOLUTA) \t" + linea.etiqueta + "\t" + completarHexadecimal(valorHex).toUpperCase() + "\n";
            }
            if(linea.codop.equalsIgnoreCase("END"))
            {
                existeEnd = true;
                imprimirSalida(linea);
            }
        }
    }
    public int convertirDecimal(String num, String sistNum)
    {
        int res = 0;
        if(sistNum.equals("DEC"))
        {
            res = Integer.parseInt(num);
        }
        else if (sistNum.equals("BIN"))
        {
            res = Integer.parseInt(num.substring(1), 2);
        }
        else if (sistNum.equals("OCT"))
        {
            res = Integer.parseInt(num.substring(1), 8);
        }
        else if (sistNum.equals("HEX"))
        {
            res = Integer.parseInt(num.substring(1), 16);
        }
        return res;
    }
    public String completarHexadecimal(String x)
    {
        String ret = "";
        for(int i = 0; i < (4 - x.length()); i++)
            ret += "0";
        ret += x;
        return ret;
    }
    public void imprimirLineas()
    {
        for(Linea linea: ASM)
        {
            area.append(linea.imprimirComandos());
        }
    }
    public void imprimirSalida(Linea linea)
    {
        salida += "CONT_LOC \t" + completarHexadecimal(contLocHex).toUpperCase() + "\t" + linea.etiqueta + "\t" + linea.codop + "\t" + linea.operando +"\t"+linea.codMaq+"\n";
        if(!linea.etiqueta.equals(" "))
            salidaET += "CONT_LOC (RELATIVA)\t"+ linea.etiqueta + "\t" + completarHexadecimal(contLocHex).toUpperCase()+ "\n";
    }
    
}
