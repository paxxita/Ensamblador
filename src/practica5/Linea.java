package practica5;

import java.util.HashMap;

public class Linea {
    
    String etiqueta = " ";
    String codop = " ";
    String operando = " "; 
    String comentario = " ";
    
    int tam = 0;
    String dir = " ";
    String sistNum = " ";
    String codMaq = " ";
   
    int contLoc = 0; 
    String contLocHex = " ";
    
    boolean errorEtiqueta;
    boolean errorCodop;
    boolean errorOperando;
    boolean esComentario;
    
    String errores = "";
    String impresion = "";
    
    public Linea (String linea)
    {
        separarLinea(linea);
    }    
    
    private void separarLinea(String linea)
    {
        char caracter;
        caracter = linea.charAt(0);
        if(caracter == ';')
        {
            comentario = linea.substring(1);
            esComentario = true;
        }
        else
        {
            int idx;
            idx = leerEtiqueta(linea);
            idx = leerEspacios(linea, idx);
            idx = leerCodOp(linea, idx);
            idx = leerEspacios(linea, idx);
            leerOperando(linea, idx);
        }
    }
    
    private int leerEtiqueta(String linea)
    {
        etiqueta = "";
        char caracter;
        int idx = linea.length();
        for (int i = 0; i < linea.length(); i++) 
        {
            caracter = linea.charAt(i);
            if(caracter == ' ' || caracter == 9)
            {
                idx = i;
                break;
            }
            etiqueta += caracter;  
        }
        if(etiqueta.equals(""))
            etiqueta = " ";
        return idx; 
    }
    private int leerEspacios(String linea, int idx)
    {
        char caracter;
        for (int i = idx + 1; i < linea.length(); i++) 
        {
            caracter = linea.charAt(i);
            if(caracter != ' ' && caracter != 9)
            {
                if(caracter == ';'){
                    comentario = linea.substring(i+1);
                    return linea.length();        
                }
                return i; 
            }  
        }
        return linea.length();
    }
    private int leerCodOp(String linea, int idx)
    {
        codop = "";
        char caracter;
        int idxRet = linea.length();
        for (int i = idx; i < linea.length(); i++) 
        {
            caracter = linea.charAt(i);
            if(caracter == ' ' || caracter == 9)
            {
                idxRet = i;
                break;
            }
            codop += caracter;  
        }
        if(codop.equals(""))
            codop = " ";
        return idxRet; 
    }
    private void leerOperando(String linea, int idx)
    {
        operando = "";
        char caracter;
        for (int i = idx; i < linea.length(); i++) 
        {
            caracter = linea.charAt(i);
            if(caracter == ' ' || caracter == 9)
            {
                leerEspacios(linea, i);
                break;
            }
            operando += caracter;  
        }
        
        if(operando.equals("")){
            operando = " "; 
        }
    }
    
    public void analizaLinea()
    {
        analizarEtiqueta();
        analizarCodOp();
        analizarOperando();
    }
    private void analizarEtiqueta()
    {
        if(!etiqueta.equals(" "))
        {
            if(etiqueta.length() > 32){
                errorEtiqueta = true;
                errores += "La longitud máxima de una etiqueta es de 32 caracteres \n";
            }
            if(etiqueta.charAt(0)<65 ||(etiqueta.charAt(0)>90 && etiqueta.charAt(0)<97 && etiqueta.charAt(0)!=95)||etiqueta.charAt(0)>122)
            {    
                errorEtiqueta = true;
                errores += "La etiqueta debe de iniciar con letra\n";
            }
            for(int i = 1; i < etiqueta.length(); i++) 
            {
                char car = etiqueta.charAt(i);
                if(car<48||(car>57 && car<65) ||(car>90 && car<97 && car!=95)||car>122)
                {
                    errorEtiqueta = true;
                    errores += "Los caracteres validos en etiquetas son letras, digitos(0..9) y el guion bajo \n";
                    break;
                }
            }
        }   
    }
    private void analizarCodOp()
    {
        if(!codop.equals(" "))
        {
            if(codop.length() > 5){
                errorCodop = true;
            }
            if(codop.charAt(0)<65 ||(codop.charAt(0)>90 && codop.charAt(0)<97)||codop.charAt(0)>122){
                errorCodop = true;
            }
            boolean usado = false;
            int pts = 0;
            for(int i = 1; i < codop.length(); i++) 
            {
                char car = codop.charAt(i);
                if(!usado &&((car<65 && car!=46) || (car>90 && car<97)|| car>122))
                {
                    usado = true;
                    errorCodop = true;
                }
                else if(car == 46)
                    pts++;
            }
            if(pts>1){
                errorCodop = true;
            }
            if(errorCodop)
                errores += "El codigo de operacion es invalido\n";
        }
        else
        {
            if(!esComentario)
            {
                errorCodop = true;
                errores += "Siempre debe existir un codigo de operacion\n";
            }
        }
    }
    private void analizarOperando()
    {
        if(!operando.equals(" ")){
            baseNumericaOperando();
        }        
    }
    private void baseNumericaOperando()
    {
        int pos = 0;
        if(operando.charAt(pos) == '#')
            pos = 1;
            
        if(operando.charAt(pos) == '%')
        {
            if(operando.substring(pos+1).matches("([0-1]+)")){
                sistNum = "BIN";
            }
        }
        else if(operando.charAt(pos) == '@')
        {
            if(operando.substring(pos+1).matches("[0-7]+")){
                sistNum = "OCT";
            }
        }
        else if(operando.charAt(pos) == '$')
        {
            if(operando.substring(pos+1).matches("(\\d|[a-f|A-F])+")){
                sistNum = "HEX";
            }
        }
        else if(operando.substring(pos).matches("(-{0,1}(\\d)+)"))
        {
            sistNum = "DEC";
        }
        else sistNum = " ";
    }   
    
    private boolean esEtiqueta(String x)
    {
        if(x.matches("^(_|[aA-zZ])\\w{1,32}"))
            return true; 
        return false;
    }
            
    public int ensamblarPrimerPaso(HashMap<String,String> simbolos, CodigoOperacion codigo, int cont)
    {
        int contActualizado = cont;
        if(!codop.equals(" ")){
            if(esDirectiva())
            {
                contActualizado = validarDirectiva(simbolos, cont);
            }
            else
            {            
                validarCodOp(simbolos, codigo);       
            
                if(!errorCodop)
                {
                    agregarEtiqueta(simbolos, cont);
                    contActualizado = validarOperando(simbolos, codigo, cont);
                }
            }
        }
        return contActualizado;
    }
    public void ensamblarSegundoPaso(HashMap<String,String> simbolos, CodigoOperacion codigo)
    {
        if(esDirectiva())
        {
            
        }
        else
        {
            if(dir.equals("EXT")||dir.equals("EXT1"))
            {
                if((codMaq.length()/2)!= tam){
                    if(simbolos.containsKey(operando)){
                        codMaq += simbolos.get(operando);
                    }
                }
            }
            if(dir.equals("REL8"))
            {
                if((codMaq.length()/2) != tam){
                    if(simbolos.containsKey(operando)){
                        int valor = Integer.parseInt(simbolos.get(operando), 16);
                        valor = valor - contLoc - tam;
                        codMaq += completarHexadecimal(Integer.toHexString(valor), 1);
                    }
                }
            }
        }
    }
    public void agregarEtiqueta(HashMap<String, String> simbolos, int cont)
    {
        if(!etiqueta.equals(" ") && !simbolos.containsKey(etiqueta))
        {
            contLoc = cont;
            contLocHex = completarHexadecimal(Integer.toHexString(contLoc), 2);
            simbolos.put(etiqueta, contLocHex);
        }
    }
    public boolean esDirectiva()
    {
        if(!codop.equals(" ")){
            if(codop.equalsIgnoreCase("END"))
            {
                dir = "DTV";
                return true;
            }
            else if(codop.equalsIgnoreCase("ORG")||codop.equalsIgnoreCase("EQU")||
                    codop.equalsIgnoreCase("DB")||codop.equalsIgnoreCase("DC.B")||
                    codop.equalsIgnoreCase("FCB") ||codop.equalsIgnoreCase("DW")||
                    codop.equalsIgnoreCase("DC.W")||codop.equalsIgnoreCase("FDB")||
                    codop.equalsIgnoreCase("FCC")||codop.equalsIgnoreCase("DS")||
                    codop.equalsIgnoreCase("DS.B")||codop.equalsIgnoreCase("RMB")||
                    codop.equalsIgnoreCase("DS.W")||codop.equalsIgnoreCase("RMW"))
            {
                dir = "DTV";
                return true;
            }
            else return false;
        }
        return false;
    }
    public int validarDirectiva(HashMap<String, String> simbolos, int cont){
        
        int contActualizado = cont;
        if(codop.equalsIgnoreCase("END"))
        {
            contLoc = cont;
            contLocHex = completarHexadecimal(Integer.toHexString(contLoc), 2);
            impresion = "CONT_LOC \t" + contLocHex.toUpperCase() + "\t" + imprimirComandosCortos();             
        }
        if(codop.equalsIgnoreCase("ORG"))
        {   
            tam = 0;
            contLoc = convertirDecimal(operando);
            contLocHex = completarHexadecimal(Integer.toHexString(contLoc), 2);
            impresion = "DIR_INIC \t" + contLocHex.toUpperCase() + "\t" + imprimirComandosCortos();
            
            contActualizado = contLoc;
        }
        else if(codop.equalsIgnoreCase("DB")||codop.equalsIgnoreCase("DC.B")||codop.equalsIgnoreCase("FCB"))
        {
            int valor = convertirDecimal(operando);
            if(valor <= 255){
                tam = 1;
                codMaq = completarHexadecimal(Integer.toHexString(valor),1);
                contLoc = cont;
                contLocHex = completarHexadecimal(Integer.toHexString(contLoc), 2);
                impresion = "CONT_LOC \t" + contLocHex.toUpperCase() + "\t" + imprimirComandosCortos();             
                contActualizado += tam;
            }
            else {
                errorOperando = true;
                errores += "Operando mayor a 255 \n";
            }
        }
        else if(codop.equalsIgnoreCase("DW")||codop.equalsIgnoreCase("DC.W")||codop.equalsIgnoreCase("FDB"))
        {
            int valor = convertirDecimal(operando);
            if(valor <= 65535){
                tam = 2;
                codMaq = completarHexadecimal(Integer.toHexString(valor),2);
                contLoc = cont;
                contLocHex = completarHexadecimal(Integer.toHexString(contLoc), 2);
                impresion = "CONT_LOC \t" + contLocHex.toUpperCase() + "\t" + imprimirComandosCortos();             
                contActualizado += tam;
            }
            else {
                errorOperando = true;
                errores += "Operando mayor a 65535 \n";
            }
        }
        else if(codop.equalsIgnoreCase("FCC"))
        {   
            if(operando.charAt(0)=='"' && operando.charAt(operando.length()-1)=='"'){
                tam = operando.length() - 2;
                for(int i = 1; i < operando.length()-1; i++)
                {
                    codMaq += Integer.toHexString((int)operando.charAt(i)) + " ";
                }
                contLoc = cont;
                contLocHex = completarHexadecimal(Integer.toHexString(contLoc), 2);
                impresion = "CONT_LOC \t" + contLocHex.toUpperCase() + "\t" + imprimirComandosCortos();             
                contActualizado += tam;
            }
            else{
                errorOperando = true;
                errores += "Operando con formato incorrecto \n";
            }
        }
        else if(codop.equalsIgnoreCase("DS")||codop.equalsIgnoreCase("DS.B")||codop.equalsIgnoreCase("RMB"))
        {
            int valor = convertirDecimal(operando);
            if(valor <= 65535){
                tam = valor;
                contLoc = cont;
                contLocHex = completarHexadecimal(Integer.toHexString(contLoc), 2);
                impresion = "CONT_LOC \t" + contLocHex.toUpperCase() + "\t" + imprimirComandosCortos();             
                contActualizado += tam;
            }
            else {
                errorOperando = true;
                errores += "Operando mayor a 65535 \n";
            }
        }
        else if(codop.equalsIgnoreCase("DS.W")||codop.equalsIgnoreCase("RMW"))
        {
            int valor = convertirDecimal(operando);
            if(valor <= 65535){
                tam = valor * 2;
                contLoc = cont;
                contLocHex = completarHexadecimal(Integer.toHexString(contLoc), 2);
                impresion = "CONT_LOC \t" + contLocHex.toUpperCase() + "\t" + imprimirComandosCortos();             
                contActualizado += tam;
            }
            else{
                errorOperando = true;
                errores += "Operando mayor a 65535 \n";
            }
        }
        else if(codop.equalsIgnoreCase("EQU"))
        {
            int valor = convertirDecimal(operando);
            if(valor <= 65535 && !etiqueta.equals(" "))
            {
                if(!simbolos.containsKey(etiqueta)){
                    tam = 2;  
                    contLoc = valor;
                    contLocHex = Integer.toHexString(valor);
                    contLocHex = completarHexadecimal(contLocHex, tam);
                    simbolos.put(etiqueta, contLocHex);
                    impresion = "VALOR EQU \t" + contLocHex.toUpperCase() + "\t" + imprimirComandosCortos();
                }
                else{
                    errorOperando = true;
                    errores += "La etiqueta ya existe \n";
                }
            }
            else if(valor > 65535){
                errorOperando = true;
                errores += "Operando mayor a 65535 \n";
            }
            else 
            {
                errorEtiqueta = true;
                errores += "Debe contener etiqueta \n";
            }
        }
        return contActualizado;
    }
    public void validarCodOp(HashMap<String, String> simbolos, CodigoOperacion codigo)
    {
        if(codigo == null)
        {
            if(!codop.equals(" "))
            {
                errorCodop = true;
                errores += "Codigo de Operacion no encontrado\n";
            }
        }   
    }
    public int validarOperando(HashMap<String,String> simbolos, CodigoOperacion codigo, int cont)
    {
        int contActualizado = cont;
        int value;
        for(int i = 0; i < codigo.modosDir.size(); i++)
        {
            if(codigo.modosDir.get(i).tipo.equals("INH"))
            {
                if(DirInh())
                {
                    codMaq = codigo.modosDir.get(i).codMaq;
                    tam = codigo.modosDir.get(i).tamanho;
                    contLoc = cont;
                    contLocHex = completarHexadecimal(Integer.toHexString(contLoc), 2);
                    impresion = "CONT_LOC \t" + contLocHex.toUpperCase() + "\t" + imprimirComandosCortos();             
                    contActualizado += tam;
                    break;
                }
            }
            if(codigo.modosDir.get(i).tipo.equals("IMM"))
            {
                value = DirInmediato();
                if(value != Integer.MIN_VALUE)
                {
                    tam = codigo.modosDir.get(i).tamanho;
                    codMaq = codigo.modosDir.get(i).codMaq.substring(0, 2);
                    String hex = Integer.toHexString(value);
                    hex = completarHexadecimal(hex, tam - 1);
                    codMaq += hex;
                    contLoc = cont;
                    contLocHex = completarHexadecimal(Integer.toHexString(contLoc), 2);
                    impresion = "CONT_LOC \t" + contLocHex.toUpperCase() + "\t" + imprimirComandosCortos();             
                    contActualizado += tam;
                    break;
                }
            }
            if(codigo.modosDir.get(i).tipo.equals("IMM8"))
            {
                value = DirInmediato8();
                if(value != Integer.MIN_VALUE)
                {
                    tam = codigo.modosDir.get(i).tamanho;
                    codMaq = codigo.modosDir.get(i).codMaq.substring(0, 2);
                    String hex = Integer.toHexString(value);
                    hex = completarHexadecimal(hex, tam - 1);
                    codMaq += hex;
                    contLoc = cont;
                    contLocHex = completarHexadecimal(Integer.toHexString(contLoc), 2);
                    impresion = "CONT_LOC \t" + contLocHex.toUpperCase() + "\t" + imprimirComandosCortos();             
                    contActualizado += tam;
                    break;
                }
            }
            if(codigo.modosDir.get(i).tipo.equals("IMM16"))
            {
                value = DirInmediato16();
                if(value != Integer.MIN_VALUE)
                {
                    tam = codigo.modosDir.get(i).tamanho;
                    codMaq = codigo.modosDir.get(i).codMaq.substring(0, 2);
                    String hex = Integer.toHexString(value);
                    hex = completarHexadecimal(hex, tam - 1);
                    codMaq += hex;
                    contLoc = cont;
                    contLocHex = completarHexadecimal(Integer.toHexString(contLoc), 2);
                    impresion = "CONT_LOC \t" + contLocHex.toUpperCase() + "\t" + imprimirComandosCortos();             
                    contActualizado += tam;
                    break;
                }
            }
            if(codigo.modosDir.get(i).tipo.equals("DIR"))
            {
                value = DirDirecto();
                if(value != Integer.MIN_VALUE)
                {
                    tam = codigo.modosDir.get(i).tamanho;
                    codMaq = codigo.modosDir.get(i).codMaq.substring(0, 2);
                    String hex = Integer.toHexString(value);
                    hex = completarHexadecimal(hex, tam - 1);
                    codMaq += hex;
                    contLoc = cont;
                    contLocHex = completarHexadecimal(Integer.toHexString(contLoc), 2);
                    impresion = "CONT_LOC \t" + contLocHex.toUpperCase() + "\t" + imprimirComandosCortos();             
                    contActualizado += tam;
                    break;
                }
            }
            if(codigo.modosDir.get(i).tipo.equals("EXT"))
            {
                value = DirExtendido();
                if(value != Integer.MIN_VALUE)
                {
                    tam = codigo.modosDir.get(i).tamanho;
                    codMaq = codigo.modosDir.get(i).codMaq.substring(0, 2);
                    String hex = Integer.toHexString(value);
                    hex = completarHexadecimal(hex, tam - 1);
                    codMaq += hex;
                    contLoc = cont;
                    contLocHex = completarHexadecimal(Integer.toHexString(contLoc), 2);
                    impresion = "CONT_LOC \t" + contLocHex.toUpperCase() + "\t" + imprimirComandosCortos();             
                    contActualizado += tam;
                    break;
                }
                else if(simbolos.containsKey(operando))
                {
                    dir = "EXT";
                    tam = codigo.modosDir.get(i).tamanho;
                    codMaq = codigo.modosDir.get(i).codMaq.substring(0, 2);
                    codMaq += simbolos.get(operando);
                    contLoc = cont;
                    contLocHex = completarHexadecimal(Integer.toHexString(contLoc), 2);
                    impresion = "CONT_LOC \t" + contLocHex.toUpperCase() + "\t" + imprimirComandosCortos();             
                    contActualizado += tam;
                    break;
                }
                else if(esEtiqueta(operando))
                {
                    dir = "EXT";
                    tam = codigo.modosDir.get(i).tamanho;
                    codMaq = codigo.modosDir.get(i).codMaq.substring(0, 2);
                    contLoc = cont;
                    contLocHex = completarHexadecimal(Integer.toHexString(contLoc), 2);
                    impresion = "CONT_LOC \t" + contLocHex.toUpperCase() + "\t" + imprimirComandosCortos();             
                    contActualizado += tam;
                    break;
                }
            }
            if(codigo.modosDir.get(i).tipo.equals("EXT1"))
            {
                value = DirExtendido1();
                if(value != Integer.MIN_VALUE)
                {
                    tam = codigo.modosDir.get(i).tamanho;
                    codMaq = codigo.modosDir.get(i).codMaq.substring(0, 2);
                    String hex = Integer.toHexString(value);
                    hex = completarHexadecimal(hex, tam - 1);
                    codMaq += hex;
                    contLoc = cont;
                    contLocHex = completarHexadecimal(Integer.toHexString(contLoc), 2);
                    impresion = "CONT_LOC \t" + contLocHex.toUpperCase() + "\t" + imprimirComandosCortos();             
                    contActualizado += tam;
                    break;
                }
                else if(simbolos.containsKey(operando))
                {
                    dir = "EXT1";
                    tam = codigo.modosDir.get(i).tamanho;
                    codMaq = codigo.modosDir.get(i).codMaq.substring(0, 2);
                    codMaq += simbolos.get(operando);
                    contLoc = cont;
                    contLocHex = completarHexadecimal(Integer.toHexString(contLoc), 2);
                    impresion = "CONT_LOC \t" + contLocHex.toUpperCase() + "\t" + imprimirComandosCortos();             
                    contActualizado += tam;
                    break;
                }
                else if(esEtiqueta(operando))
                {
                    dir = "EXT1";
                    tam = codigo.modosDir.get(i).tamanho;
                    codMaq = codigo.modosDir.get(i).codMaq.substring(0, 2);
                    contLoc = cont;
                    contLocHex = completarHexadecimal(Integer.toHexString(contLoc), 2);
                    impresion = "CONT_LOC \t" + contLocHex.toUpperCase() + "\t" + imprimirComandosCortos();             
                    contActualizado += tam;
                    break;
                }
            }
            if(codigo.modosDir.get(i).tipo.equals("REL8"))
            {
                if(esEtiqueta(operando))
                {
                    dir = "REL8";
                    tam = codigo.modosDir.get(i).tamanho;
                    codMaq = codigo.modosDir.get(i).codMaq.substring(0, 2);
                    if(simbolos.containsKey(operando)){
                        int valor = Integer.parseInt(simbolos.get(operando), 16);
                        valor = valor - cont - tam;
                        codMaq += completarHexadecimal(Integer.toHexString(valor), 1);
                    }
                    contLoc = cont;
                    contLocHex = completarHexadecimal(Integer.toHexString(contLoc), 2);
                    impresion = "CONT_LOC \t" + contLocHex.toUpperCase() + "\t" + imprimirComandosCortos();             
                    contActualizado += tam;
                    break;
                }
            }
            if(codigo.modosDir.get(i).tipo.equals("REL16"))
            {
                
            }
            if(codigo.modosDir.get(i).tipo.equals("IDX"))
            {
                value = DirIndexado();
                if(value!= Integer.MIN_VALUE)
                {
                    String sub[];
                    tam = codigo.modosDir.get(i).tamanho;
                    codMaq = codigo.modosDir.get(i).codMaq.substring(0, 2);
                    sub = operando.split(",");
                    String result = Integer.toBinaryString(value);
                    result = completaBinario(value, result, 5);
                    result = sustituirRegistro(sub[1])+"0"+result;
                    int fin = Integer.parseInt(result,2);
                    result = Integer.toHexString(fin);
                    codMaq += result;
                    contLoc = cont;
                    contLocHex = completarHexadecimal(Integer.toHexString(contLoc), 2);
                    impresion = "CONT_LOC \t" + contLocHex.toUpperCase() + "\t" + imprimirComandosCortos();             
                    contActualizado += tam;
                    break;
                }
                if(DirIdxI_PPD())
                {
                    tam = codigo.modosDir.get(i).tamanho;
                    codMaq = codigo.modosDir.get(i).codMaq;
                    break;
                }
                if(DirIdxAcum_Idx())
                {
                    String sub[], resultRegIdx ,result;
                    tam = codigo.modosDir.get(i).tamanho;
                    codMaq = codigo.modosDir.get(i).codMaq.substring(0, 2);
                    sub = operando.split(",");
                    resultRegIdx = sustituirRegistroIdx(sub[0]);
                    result = sustituirRegistro(sub[1]);
                    result = "111"+result+"1"+resultRegIdx;
                    int fin = Integer.parseInt(result);
                    result = Integer.toHexString(fin);
                    codMaq += result;
                    contLoc = cont;
                    contLocHex = completarHexadecimal(Integer.toHexString(contLoc), 2);
                    impresion = "CONT_LOC \t" + contLocHex.toUpperCase() + "\t" + imprimirComandosCortos();             
                    contActualizado += tam;
                    break;
                }
            }
            if(codigo.modosDir.get(i).tipo.equals("IDX1"))
            {
                value = DirIdx1();
                if(value != Integer.MIN_VALUE)
                {
                    String s,sub[],result;
                    tam = codigo.modosDir.get(i).tamanho;
                    codMaq = codigo.modosDir.get(i).codMaq.substring(0, 2);
                    sub = operando.split(",");
                    if(value <= 0){
                        s = "1";
                        value = value * -1;
                    }
                    else s = "0";
                    result = "111" + sustituirRegistro(sub[1]) + "00" + s;
                    int fin = Integer.parseInt(result,2);
                    result = Integer.toHexString(fin);
                    codMaq += result + Integer.toHexString(value);
                    contLoc = cont;
                    contLocHex = completarHexadecimal(Integer.toHexString(contLoc), 2);
                    impresion = "CONT_LOC \t" + contLocHex.toUpperCase() + "\t" + imprimirComandosCortos();             
                    contActualizado += tam;
                    break;
                }
            }
            if(codigo.modosDir.get(i).tipo.equals("IDX2"))
            {
                value = DirIdx2();
                if(value != Integer.MIN_VALUE)
                {
                    String s = "0", z = "1", sub[], result, resultHex;
                    tam = codigo.modosDir.get(i).tamanho;
                    codMaq = codigo.modosDir.get(i).codMaq.substring(0, 2);
                    sub = operando.split(",");
                    result = "111"+sustituirRegistro(sub[1]) + "0" + z + s;
                    int fin = Integer.parseInt(result,2);
                    result = Integer.toHexString(fin);
                    resultHex = Integer.toHexString(value);
                    resultHex = completarHexadecimal(resultHex, 2);
                    codMaq += result + resultHex;
                    contLoc = cont;
                    contLocHex = completarHexadecimal(Integer.toHexString(contLoc), 2);
                    impresion = "CONT_LOC \t" + contLocHex.toUpperCase() + "\t" + imprimirComandosCortos();             
                    contActualizado += tam;
                    break;
                }                
            }
            if(codigo.modosDir.get(i).tipo.equals("[IDX2]"))
            {
                value = DirIdxInd();
                if(value != Integer.MIN_VALUE)
                {
                    String sub[];
                    tam = codigo.modosDir.get(i).tamanho;
                    codMaq = codigo.modosDir.get(i).codMaq.substring(0, 2);
                    sub = operando.split(",");
                    String resultHex = Integer.toHexString(value);
                    resultHex = completarHexadecimal(resultHex, 2);
                    String result = "111"+ sustituirRegistro(sub[1].substring(0, sub[1].length()-1))+"011";
                    int fin = Integer.parseInt(result,2);
                    result = Integer.toHexString(fin);
                    codMaq += result + resultHex;
                    contLoc = cont;
                    contLocHex = completarHexadecimal(Integer.toHexString(contLoc), 2);
                    impresion = "CONT_LOC \t" + contLocHex.toUpperCase() + "\t" + imprimirComandosCortos();             
                    contActualizado += tam;
                    break;
                }
            }
            if(codigo.modosDir.get(i).tipo.equals("[D,IDX]"))
            {
                value = DirIdxAcumD();
                if(value != Integer.MIN_VALUE)
                {
                    String sub[];
                    tam = codigo.modosDir.get(i).tamanho;
                    codMaq = codigo.modosDir.get(i).codMaq.substring(0, 2);
                    sub = operando.split(",");
                    String result = "111"+ sustituirRegistro(sub[1].substring(0, sub[1].length()-1))+"111";
                    int fin = Integer.parseInt(result);
                    result = Integer.toHexString(fin);
                    codMaq += result;
                    contLoc = cont;
                    contLocHex = completarHexadecimal(Integer.toHexString(contLoc), 2);
                    impresion = "CONT_LOC \t" + contLocHex.toUpperCase() + "\t" + imprimirComandosCortos();             
                    contActualizado += tam;
                    break;
                }
            }      
        }   
        /*if(dir.equals(" "))
        {   
            errorOperando = true;
            errores += "Operando no valido para el direccionamiento \n";
        }*/
        return contActualizado;
    }
    
    private boolean DirInh()
    {
        if(operando.equals(" ")){
            dir = "INH";
            return true;
        }
        return false;       
    }
    private int DirInmediato()
    {
        if(operando.matches("#@[0-7]+")||operando.matches("#%[0-1]+")||
                operando.matches("#$(\\d|[a-f|A-F])+")||operando.matches("#\\d+")){
            int value = convertirDecimal(operando.substring(1));
   
            dir = "IMM";
            return value;
        }
        return Integer.MIN_VALUE;
    }
    private int DirInmediato8()
    {
        if(operando.matches("#@[0-7]+")||operando.matches("#%[0-1]+")||
                operando.matches("#$(\\d|[a-f|A-F])+")||operando.matches("#\\d+")){
            int value = convertirDecimal(operando.substring(1));
            
            dir = "IMM8";
            return value;
        }
        return Integer.MIN_VALUE;
    }
    private int DirInmediato16()
    {
        if(operando.matches("#@[0-7]+")||operando.matches("#%[0-1]+")||
                operando.matches("#$(\\d|[a-f|A-F])+")||operando.matches("#\\d+")){
            int value = convertirDecimal(operando.substring(1));
            
            dir = "IMM16";
            return value;
        }
        return Integer.MIN_VALUE;
    }
    private int DirDirecto()
    {
        if(operando.matches("@[0-7]{1,3}")||operando.matches("%[0-1]{1,8}")||
                operando.matches("$(\\d|[a-f|A-F]){1,2}")||operando.matches("\\d{1,3}")){
            int value = convertirDecimal(operando);
            if(value <= 255){
                dir = "DIR";
                return value;
            }
        }
        return Integer.MIN_VALUE;
    }
    private int DirExtendido()
    {
        if(operando.matches("@[0-7]{3,6}")||operando.matches("%[0-1]{9,16}")||
                operando.matches("$(\\d|[a-f|A-F]){3,4}")||operando.matches("\\d{3,5}")){
            int value = convertirDecimal(operando);
            if(value >= 256 && value <= 65535){
                dir = "EXT";
                return value;
            }
        }
        return Integer.MIN_VALUE;
    }
    private int DirExtendido1()
    {
        if(operando.matches("@[0-7]{1,6}")||operando.matches("%[0-1]{1,16}")||
                operando.matches("$(\\d|[a-f|A-F]){1,4}")||operando.matches("(\\d){1,5}")){
            int value = convertirDecimal(operando);
            if(value >= 0 && value <= 65535){
                dir = "EXT1";
                return value;
            }
        }
        return Integer.MIN_VALUE;
    }
    
    private int DirIndexado()
    {
        String ERIndexado = "(-{0,1}\\d{1,3}){0,1},([xX]|[yY]|sp|SP|pc|PC){1}"; //pueden no estar los registros ???
        if(operando.matches(ERIndexado)){
            String sub[];
            sub = operando.split(",");
            int res = (sub[0].length()==0) ? 0 : Integer.parseInt(sub[0]);
            if(res >= -16 && res <= 15){
                dir = "IDX";
                return res;
            }
        }
        return Integer.MIN_VALUE;
    }
    
    private boolean DirIdxI_PPD()
    {
        String ERIdxI_PPD = "^([1-8])(,)(-|\\+){0,1}([xX]|[yY]|sp|SP)(-|\\+){0,1}";
        if(operando.matches(ERIdxI_PPD)){
            dir = "IDX";
            return true;
        }
        return false;
    }
    private boolean DirIdxAcum_Idx()
    {
        String ERIdxAcum_Idx = "^([a|A]|[b|B]|[d|D])(,)([xX]|[yY]|sp|SP|pc|PC)";
        if(operando.matches(ERIdxAcum_Idx)){
            dir = "IDX";
            return true;
        }
        return false;
    }
    private int DirIdx1()
    {
        String ERIdx1 = "-{0,1}(\\d{1,3}|)(,){0,1}([xX]|[yY]|sp|SP|pc|PC)";
        if(operando.matches(ERIdx1)){
            String sub[];
            sub = operando.split(",");
            int res = Integer.parseInt(sub[0]);
            if(res >= -256 && res <= 255){
                dir = "IDX1";
                return res;
            }
        }
        return Integer.MIN_VALUE;
    }
    private int DirIdx2()
    {
        String ERIdx2 = "(\\d{3,5}|-|,|)(\\d{3,5})(,){0,1}([xX]|[yY]|sp|SP|pc|PC)";
        if(operando.matches(ERIdx2)){
            String sub[];
            sub = operando.split(",");
            int res = Integer.parseInt(sub[0]);
            if(res >= -256 && res <= 255){
                dir = "IDX2";
                return res;
            }
        }
        return Integer.MIN_VALUE;
    }
    private int DirIdxInd()
    {
        String ERIdxInd = "^\\[(\\d{0,5}|,|),{0,1}([xX]|[yY]|sp|SP|pc|PC)\\]";
        if(operando.matches(ERIdxInd)){
            String sub[];
            sub = operando.split(",");
            int res = Integer.parseInt(sub[0].substring(1, sub[0].length()));
            if(res>= 0 && res <= 65535){
                dir = "[IDX2]";
                return res;
            }
        }
        return Integer.MIN_VALUE;
    }    
    private int DirIdxAcumD()
    {
        String ERIdxAcumD   = "^(\\[)([d|D])(,)([xX]|[yY]|sp|SP|pc|PC)(\\])";
        if(operando.matches(ERIdxAcumD)){
            String sub[];
            sub = operando.split(",");
            String res = sub[0].substring(1, sub[0].length());
            if(res.matches("D")){
                int valor = 1;
                dir = "[D,IDX]";
                return valor;
            }
        }
        return Integer.MIN_VALUE;
    }
    
    public String completaBinario(int value, String x, int t){
        String ret = "";
        String completar = ((value >= 0)?"0":"1");
        t = t - x.length();
        for(int i = 0; i < t; i++){
            ret += completar;
        }
        ret = ret + x;
        return ret;
    }
    public String completarHexadecimal(String x, int t)
    {
        String ret = "";
        t *= 2;
        for(int i = 0; i < (t - x.length()); i++)
            ret = ret + "0";
        ret = ret + x;
        return ret;
    }
    private int convertirDecimal(String num)
    {
        int res = Integer.MIN_VALUE;
        switch (sistNum) {
            case "DEC":
                res = Integer.parseInt(num);
                break;
            case "BIN":
                res = Integer.parseInt(num.substring(1), 2);
                break;
            case "OCT":
                res = Integer.parseInt(num.substring(1), 8);
                break;
            case "HEX":
                res = Integer.parseInt(num.substring(1), 16);
                break;
        }
        return res;
    }
    
    public String sustituirRegistroIdx(String x){
        if(x.equals("a")||x.equals("A"))
                return "00";
        if(x.equals("b")||x.equals("B"))
                return "01";
        if(x.equals("d")||x.equals("D"))
                return "10";
        return "";
    }
    
    public String sustituirRegistro(String x){
        if(x.equals("x")||x.equals("X"))
            return "00";
        if(x.equals("y")||x.equals("Y"))
            return "01";
        if(x.equals("sp")||x.equals("SP"))
            return "10";
        if(x.equals("pc")||x.equals("PC"))
            return "11";
        return "";
    }
    public String imprimirComandosCortos()
    {
        return etiqueta + "\t" + codop + "\t" + operando + "\t" + codMaq + "\n";
    }
    public String imprimirComandos()
    {
        String salida = "";
        
        salida += "Etiqueta: " + etiqueta + "\n";
        salida += "Instruccion: " + codop + "\n";
        salida += "Operando: " + operando + "\n";
        salida += "Tipo de Dir: " + dir + "\n";
        salida += "Sist Num: " + sistNum + "\n";
        salida += "Comentario: " + comentario + "\n";
        salida += "Tamaño: " + tam + "\n";
        salida += "Codigo Maquina: " + codMaq + "\n";
        salida += "Errores: "+ errores + "\n\n";
        
        return salida;
    }
}