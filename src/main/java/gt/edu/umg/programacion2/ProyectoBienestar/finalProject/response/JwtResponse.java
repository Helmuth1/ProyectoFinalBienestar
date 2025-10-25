package gt.edu.umg.programacion2.ProyectoBienestar.finalProject.response;

public class JwtResponse {
    private String token;
    private Long id;
    private String username;
    private String message;

    public JwtResponse(String token, Long id, String username, String message) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.message = message;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

}
