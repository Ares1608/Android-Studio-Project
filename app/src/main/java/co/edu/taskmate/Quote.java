package co.edu.taskmate;

public class Quote {
    private String q; // La cita
    private String a; // El autor

    // Getters y setters
    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    // Métodos para convertir estos a 'text' y 'author' si prefieres ese nombre
    public String getText() {
        return q;
    }

    public String getAuthor() {
        return a;
    }
}


