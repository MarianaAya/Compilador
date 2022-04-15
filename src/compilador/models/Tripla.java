
package compilador.models;

public class Tripla {
    private int codigo;
    private String operador;
    private String operando1;
    private String operando2;

    public Tripla(int codigo, String operador, String operando1, String operando2) {
        this.codigo = codigo;
        this.operador = operador;
        this.operando1 = operando1;
        this.operando2 = operando2;
    }
    public Tripla(int codigo, String operador, String operando1) {
        this.codigo = codigo;
        this.operador = operador;
        this.operando1 = operando1;
        this.operando2 = "";
    }
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public String getOperando1() {
        return operando1;
    }

    public void setOperando1(String operando1) {
        this.operando1 = operando1;
    }

    public String getOperando2() {
        return operando2;
    }

    public void setOperando2(String operando2) {
        this.operando2 = operando2;
    }
    
    
}
