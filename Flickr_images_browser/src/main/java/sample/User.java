package sample;

public class User {

    String loggin;
    String password;

    public User(String loggin, String password) {
        this.loggin = loggin;
        this.password = password;
    }

    public String getLoggin() {
        return loggin;
    }

    public String getPassword() {
        return password;
    }

    public void setLoggin(String loggin) {
        this.loggin = loggin;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "loggin='" + loggin + '\'' +
                ", password='" + password;
    }
}
