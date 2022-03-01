
package compilador.models;


public class Token {
    private String token;
    private String cadeia;
    private int linha;

    public Token(String token, String cadeia) {
        this.token = token;
        this.cadeia = cadeia;
        this.linha=0;
    }
    
    public Token(String token, String cadeia, int linha) {
        this.token = token;
        this.cadeia = cadeia;
        this.linha = linha;
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

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }
    
    
    
    
    
}
