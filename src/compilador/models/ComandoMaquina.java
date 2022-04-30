
package compilador.models;


public class ComandoMaquina {
    private String rotulo;
    private String comando;
    private String operando1;
    private String operando2;
    private String operando3;

    public ComandoMaquina(String rotulo, String comando, String operando1, String operando2, String operando3) {
        this.rotulo = rotulo;
        this.comando = comando;
        this.operando1 = operando1;
        this.operando2 = operando2;
        this.operando3 = operando3;
    }
    

    public ComandoMaquina(String rotulo, String comando, String operando1) {
        this.rotulo = rotulo;
        this.comando = comando;
        this.operando1 = operando1;
    }

    public ComandoMaquina(String rotulo, String comando, String operando1, String operando2) {
        this.rotulo = rotulo;
        this.comando = comando;
        this.operando1 = operando1;
        this.operando2 = operando2;
    }
    
    public ComandoMaquina(String rotulo, String comando) {
        this.rotulo = rotulo;
        this.comando = comando;
    }

    

    public String getRotulo() {
        return rotulo;
    }

    public void setRotulo(String rotulo) {
        this.rotulo = rotulo;
    }
    
    
    
    public String getComando() {
        return comando;
    }

    public void setComando(String comando) {
        this.comando = comando;
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

    public String getOperando3() {
        return operando3;
    }

    public void setOperando3(String operando3) {
        this.operando3 = operando3;
    }
    
    
}
