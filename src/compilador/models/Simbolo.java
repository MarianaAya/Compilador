
package compilador.models;

public class Simbolo {
     private String token;
    private String cadeia;
    private String tipo;
    private String valor;
    private int linha;
    private String categoria;

    public Simbolo(String token, String cadeia, String tipo, String valor,int linha) {
        this.token = token;
        this.cadeia = cadeia;
        this.tipo = tipo;
        this.valor = valor;
        this.linha=linha;
        this.categoria = "var";
    }

    public Simbolo(String token, String cadeia, String tipo, String valor,int linha, String categoria) {
        this.token = token;
        this.cadeia = cadeia;
        this.tipo = tipo;
        this.valor = valor;
        this.linha=linha;
        this.categoria = categoria;
    }
    
    
    
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCadeia() {
        return cadeia;
    }

    public void setCadeia(String cadeia) {
        this.cadeia = cadeia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }
}
