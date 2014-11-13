package practica5;

import java.util.HashMap;

public class Linea {
    
    String etiqueta = " ";
    String codop = " ";
    String operando = " "; 
    String comentario = " ";
    String aux = "";
    
    int tam = 0;
    String tamanho = " ";
    String dir = " ";
    String sistNum = " ";
    String codMaq = " ";
   
    boolean errorEtiqueta;
    boolean errorCodop;
    boolean errorOperando;
    boolean esComentario;
    
    String errores = "";
    
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
    public void validarDirectiva(HashMap<String, String> simbolos){
        
        if(codop.equalsIgnoreCase("ORG"))
        {   
            tam = 0;         
        }
        else if(codop.equalsIgnoreCase("DB")||codop.equalsIgnoreCase("DC.B")||codop.equalsIgnoreCase("FCB"))
        {
            int valor = convertirDecimal(operando);
            if(valor <= 255){
                tam = 1;
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
                    String hex = Integer.toHexString(valor);
                    hex = completarHexadecimal(hex, tam);
                    simbolos.put(etiqueta, hex);
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
    }
    public void validarCodOp(CodigoOperacion codigo, HashMap<String, String> simbolos)
    {
        if(codigo == null)
        {
            if(!codop.equals(" "))
            {
                errorCodop = true;
                errores += "Codigo de Operacion no encontrado\n";
            }
        }
        else{
            if(codigo.codigo.equals(codop))
            {   
                validarOperando(codigo, simbolos);
            }
        }
    }
    public void validarOperando(CodigoOperacion codigo, HashMap<String,String> simbolos)
    {
        int value;
        for(int i = 0; i < codigo.modosDir.size(); i++)
        {
            if(codigo.modosDir.get(i).tipo.equals("INH"))
            {
                if(DirInh())
                {
                    codMaq = codigo.modosDir.get(i).codMaq;
                    tam = codigo.modosDir.get(i).tamanho;
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
                    break;
                }
                else if(simbolos.containsKey(operando))
                {
                    tam = codigo.modosDir.get(i).tamanho;
                    codMaq = codigo.modosDir.get(i).codMaq.substring(0, 2);
                    codMaq += simbolos.get(operando);
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
                    break;
                }
                else if(simbolos.containsKey(operando))
                {
                    tam = codigo.modosDir.get(i).tamanho;
                    codMaq = codigo.modosDir.get(i).codMaq.substring(0, 2);
                    codMaq += simbolos.get(operando);
                    break;
                }
            }
            if(codigo.modosDir.get(i).tipo.equals("IDX"))
            {
                // no hay sustituciones en el cod maq??? se gurada completo??
                value = DirIndexado();
                if(value!= Integer.MIN_VALUE)
                {
                    String sub[];
                    tam = codigo.modosDir.get(i).tamanho;
                    codMaq = codigo.modosDir.get(i).codMaq;
                    sub = operando.split(",");
                    String result = Integer.toBinaryString(value);
                    result = CompletaBinario(value, result, 5);
                    result= sustituirRegistro(sub[1])+"0"+result;
                    int fin = Integer.parseInt(result,2);
                    result = Integer.toHexString(fin);
                    codMaq = codMaq.substring(0, 2)+ result;
                    
                    break;
                }
            }
            if(codigo.modosDir.get(i).tipo.equals("IDX1"))
            {
                if(DirIdx1())
                {
                    tam = codigo.modosDir.get(i).tamanho;
                    codMaq = codigo.modosDir.get(i).codMaq;
                    break;
                }
            }
            if(codigo.modosDir.get(i).tipo.equals("IDX2"))
            {
                if(DirIdx2())
                {
                    tam = codigo.modosDir.get(i).tamanho;
                    codMaq = codigo.modosDir.get(i).codMaq;
                    break;
                }
            }
            if(codigo.modosDir.get(i).tipo.equals("[IDX2]"))
            {
                if(DirIdxInd())
                {
                    tam = codigo.modosDir.get(i).tamanho;
                    codMaq = codigo.modosDir.get(i).codMaq;
                    break;
                }
            }
            if(codigo.modosDir.get(i).tipo.equals("[D,IDX]"))
            {
                if(DirIdxAcumD())
                {
                    tam = codigo.modosDir.get(i).tamanho;
                    codMaq = codigo.modosDir.get(i).codMaq;
                    break;
                }
            }      
        }   
        if(dir.equals(" "))
        {   
            errorOperando = true;
            errores += "Operando no valido para el direccionamiento \n";
        }
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
        //String ERInmediato  = "[#][%|$|@]{0,1}\\d+";
        if(operando.matches("#@[0-7]+")||operando.matches("#%[0-1]+")||
                operando.matches("#$(\\d|[a-f|A-F])+")||operando.matches("#\\d+")){
            int value = convertirDecimal(operando.substring(1));
            //if(value < rango){}
            dir = "IMM";
            return value;
        }
        return Integer.MIN_VALUE;
    }
    private int DirInmediato8()
    {
        //String ERInmediato  = "[#][%|$|@]{0,1}\\d+";
        if(operando.matches("#@[0-7]+")||operando.matches("#%[0-1]+")||
                operando.matches("#$(\\d|[a-f|A-F])+")||operando.matches("#\\d+")){
            int value = convertirDecimal(operando.substring(1));
            //if rango  
            dir = "IMM8";
            return value;
        }
        return Integer.MIN_VALUE;
    }
    private int DirInmediato16()
    {
        //String ERInmediato  = "[#][%|$|@]{0,1}\\d+";
        if(operando.matches("#@[0-7]+")||operando.matches("#%[0-1]+")||
                operando.matches("#$(\\d|[a-f|A-F])+")||operando.matches("#\\d+")){
            int value = convertirDecimal(operando.substring(1));
            //if
            dir = "IMM16";
            return value;
        }
        return Integer.MIN_VALUE;
    }
    private int DirDirecto()
    {
        //String ERDirecto = "[$|%|@]*(\\d|[a-f|A-F]){1,2}"; //"\\$([a-f]|[A-F]){1,2}|%([0-1]){1,8}|@[0-7]{0,3}|\\d{1,3}";
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
        //String ERExtendido = "(([$|%|@]{0,1}([a-f]|[A-F]|\\d){3,5})|(^([a-z]|[A-Z]|_)\\w*))";
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
        //String ERExtendido = "(([$|%|@]{0,1}([a-f]|[A-F]|\\d){3,5})|(^([a-z]|[A-Z]|_)\\w*))";
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
        String ERIndexado = "(-{0,1}\\d{1,3}){0,1},([xX]|[yY]|sp|SP|pc|PC){0,1}";
        if(operando.matches(ERIndexado)){
            String sub[];
            sub = operando.split(",");
            int res = Integer.parseInt(sub[0]);
            if(res>=-16 && res<=15){
                dir = "IDX";
                //tamanho = "2";
                //tam = 2;
                return res;
            }
        }
        return Integer.MIN_VALUE;
    }
    private boolean DirIdxI_D()
    {
        String ERIdxI_D = "^([1-8])(,)(-|\\+){0,1}([xX]|[yY]|sp|SP)(-|\\+){0,1}";
        if(operando.matches(ERIdxI_D)){
            dir = "IDX";
            tamanho = "2";
            tam = 2;
            return true;
        }
        return false;
    }
    private boolean DirIdxAcum()
    {
        String ERIdxAcum    = "^([a|A]|[b|B]|[d|D])(,)([xX]|[yY]|sp|SP|pc|PC)";
        if(operando.matches(ERIdxAcum)){
            dir = "IDX";
            tamanho = "2";
            tam = 2;
            return true;
        }
        return false;
    }
    private boolean DirIdx1()
    {
        String ERIdx1 = "-{0,1}(\\d{1,3}|)(,){0,1}([xX]|[yY]|sp|SP|pc|PC)";
        if(operando.matches(ERIdx1)){
            dir = "IDX1";
            tamanho = "3";
            tam = 3;
            return true;
        }
        return false;
    }
    private boolean DirIdx2()
    {
        String ERIdx2 = "(\\d{3,5}|-|,|)(\\d{3,5})(,){0,1}([xX]|[yY]|sp|SP|pc|PC)";
        if(operando.matches(ERIdx2)){
            dir = "IDX2";
            tamanho = "4";
            tam = 4;
            return true;
        }
        return false;
    }
    private boolean DirIdxInd()
    {
        String ERIdxInd = "^\\[(\\d{0,5}|,|)(\\d|[a-z]|[A-Z]){1,2},{0,1}([xX]|[yY]|sp|SP|pc|PC)\\]";
        if(operando.matches(ERIdxInd)){
            dir = "[IDX2]";
            tamanho = "4";
            tam = 4;
            return true;
        }
        return false;
    }    
    private boolean DirIdxAcumD()
    {
        String ERIdxAcumD   = "^(\\[)([d|D])(,)([xX]|[yY]|sp|SP|pc|PC)(\\])";
        if(operando.matches(ERIdxAcumD)){
            dir = "[D,IDX]";
            tamanho = "4";
            tam = 4;
            return true;
        }
        return false;
    }
    public String CompletaBinario(int value, String x, int t){
        String ret = "";
        String completar = ((value >= 0)?"0":"1");
        for(int i = 0; i < (t - x.length()); i++)
            ret = ret + completar;
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
        salida += "Tamaño: " + tamanho + "\n";
        salida += "Codigo Maquina: " + codMaq + "\n";
        salida += "Errores: "+ errores + "\n\n";
        
        return salida;
    }
}