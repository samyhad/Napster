public class ThreadDemo extends Thread{

    private String threadName;

    public ThreadDemo(String nome){
        threadName = nome;
    }

    public void run(){
        try{
            for(int i=4; i>0; i--){
                System.out.println("T: "+ threadName + " " + i);
                Thread.sleep(50);
            }
        } catch (InterruptedException e){
            
        }


    }
    
}
