
package compilador.models;


public class Endereco {
    private String endereco;
    private String codigo;

    public Endereco(String endereco, String codigo) {
        this.endereco = endereco;
        this.codigo = codigo;
    }
    
    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    
}
