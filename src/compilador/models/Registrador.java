
package compilador.models;


public class Registrador {
    private String variavel;
    private String registrador;

    public Registrador(String registrador, String variavel) {
        this.variavel = variavel;
        this.registrador = registrador;
    }
    
    public String getVariavel() {
        return variavel;
    }

    public void setVariavel(String variavel) {
        this.variavel = variavel;
    }

    public String getRegistrador() {
        return registrador;
    }

    public void setRegistrador(String registrador) {
        this.registrador = registrador;
    }
    
    
}
