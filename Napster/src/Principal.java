public class Principal {
    public static void main(String[] args) throws Exception {
        ThreadDemo td1 = new ThreadDemo("primeira");
        ThreadDemo td2 = new ThreadDemo("segunda");
        ThreadDemo td3 = new ThreadDemo("terceira");
        td1.start();
        td2.start();
        td3.start();
    }
}
