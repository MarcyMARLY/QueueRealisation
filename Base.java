import java.util.Vector;
import java.util.Random;
import java.util.*;

class G1 extends Thread {
Random rand = new Random();
    static final int MAXQUEUE = 20;
    private Vector messages = new Vector();

    @Override
    public void run() {
      while(true){
        int a = rand.nextInt(5000)+1000;

        try {
        Thread.sleep(a);

      } catch (InterruptedException e) {
        e.printStackTrace();
      }
        int b = rand.nextInt(20)+3;

        try {
        putMessage(b);


          } catch (InterruptedException e) {
            e.printStackTrace();
          }
      }
}
    private synchronized void putMessage(int a) throws InterruptedException {
        while (messages.size() == MAXQUEUE) {
            wait();
        }
        messages.addElement(a);
        //System.out.println("put message");
        notify();
        //Later, when the necessary event happens, the thread that is running it calls notify() from a block synchronized on the same object.
    }

    // Called by Consumer
    public synchronized Integer getMessage() throws InterruptedException {
        notify();
        while (messages.size() == 0) {
            wait();//By executing wait() from a synchronized block, a thread gives up its hold on the lock and goes to sleep.
        }
      Integer message = (Integer) messages.firstElement();

        messages.removeElement(message);
        return message;
    }
}

class G2 extends Thread {
Random rand = new Random();
    static final int MAXQUEUE = 20;
    private Vector messages = new Vector();

    @Override
    public void run() {
      while(true){
        int a = rand.nextInt(5000)+1000;

        try {
        Thread.sleep(a);

      } catch (InterruptedException e) {
        e.printStackTrace();
      }
        int b = rand.nextInt(20)+3;

        try {
        putMessage(b);


          } catch (InterruptedException e) {
            e.printStackTrace();
          }
      }
}
    private synchronized void putMessage(int a) throws InterruptedException {
        while (messages.size() == MAXQUEUE) {
            wait();
        }
        messages.addElement(a);
        //System.out.println("put message");
        notify();
        //Later, when the necessary event happens, the thread that is running it calls notify() from a block synchronized on the same object.
    }

    // Called by Consumer
    public synchronized Integer getMessage() throws InterruptedException {
        notify();
        while (messages.size() == 0) {
            wait();//By executing wait() from a synchronized block, a thread gives up its hold on the lock and goes to sleep.
        }
      Integer message = (Integer) messages.firstElement();

        messages.removeElement(message);
        return message;
    }
}
class P1 extends Thread {

Base base;
int store=1;
Vector p1List = new Vector();
List<Integer> l1 = new ArrayList<Integer>();
  P1(Base base){
    this.base = base;
  }
    @Override
    public void run(){

      try {

          while (true) {
            /*for(int i = 0;i<l1.size();i++){
              int val = l1.get(i);
              while(val>-1){
                val--;
              }
              p1List.removeElement(l1.get(i));
              l1.remove(l1.get(i));
            }*/
            //System.out.println(Thread.currentThread().getName());

            //System.out.println("dsdsd");
              Integer message = base.getMessageBase();

              Thread.sleep(1000);
              if(store == 1){
                System.out.println("Got message from base: " + message);
                putMessageP(message);
              }


              //sleep(200);
          }
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
    }
    private synchronized void putMessageP(int a) throws InterruptedException {
      p1List.addElement(a);
      l1.add(a);
      //System.out.println("put message to base");
      notify();
    }
    public synchronized List<Integer> getMessageP() throws InterruptedException {
          notify();
          while (p1List.size() == 0) {
              wait();//By executing wait() from a synchronized block, a thread gives up its hold on the lock and goes to sleep.
          }
          //Integer message = (Integer) p1List.firstElement();
          //System.out.println("G2:"+message);
          //p1List.removeElement(message);
          return l1;
      }

}
class P2 extends Thread {

  Base base;
  Vector p2List = new Vector();
  int store=2;
  List<Integer> l2 = new ArrayList<Integer>();
    P2(Base base){
      this.base = base;
    }
      @Override
      public void run(){

        try {

            while (true) {
              //System.out.println(Thread.currentThread().getName());

              //System.out.println("dsdsd");
                Integer message = base.getMessageBase();
                Thread.sleep(1000);
                if(store == 2){
                  System.out.println("Got message from base2: " + message);
                  putMessageP(message);
                }


                //sleep(200);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
      }
      private synchronized void putMessageP(int a) throws InterruptedException {
        p2List.addElement(a);
        l2.add(a);
        //System.out.println("put message to base");
        notify();
      }
      public synchronized List<Integer> getMessageP() throws InterruptedException {
            notify();
            while (p2List.size() == 0) {
                wait();//By executing wait() from a synchronized block, a thread gives up its hold on the lock and goes to sleep.
            }
            //Integer message = (Integer) p1List.firstElement();
            //System.out.println("G2:"+message);
            //p1List.removeElement(message);
            return l2;
        }
}

class Calc extends Thread{
  P1 pp1;
  P2 pp2;
  double Ssum1=0;
  double Ssum2=0;
  Calc(P1 ppp1, P2 ppp2){
    pp1=ppp1;
    pp2 = ppp2;
  }
  @Override
  public void run() {
      try {

          while (true) {
              //System.out.println(Thread.currentThread().getName());
            System.out.println("dsdsd");
              List<Integer> message = pp1.getMessageP();
              Thread.sleep(1000);
              System.out.println("Got message in Calc: " + message);
              int sum1 = 0 ;
              for(int i=0;i<message.size();i++){
                sum1+=message.get(i);
              }
              List<Integer> message1 = pp2.getMessageP();
              Thread.sleep(1000);
              System.out.println("Got message2 in Calc: " + message1);
              int sum2 = 0 ;
              for(int i=0;i<message1.size();i++){
                sum2+=message1.get(i);
              }
              putMessageCalc(sum1,sum2);
              getMessageCalc();




              //sleep(200);
          }
      } catch (InterruptedException e) {
          e.printStackTrace();
      }

  }
  private synchronized void putMessageCalc(int a, int b) throws InterruptedException {
    Ssum1 = a*0.5;
    Ssum2 = b*0.9;
    //System.out.println("put message to base");
    notify();
  }
  public synchronized void getMessageCalc() throws InterruptedException {
        notify();
        if(Ssum1==0){
          pp1.store = 1;
          pp2.store = 1;
        }
        if(Ssum2==0){
          pp1.store = 2;
          pp2.store = 2;
        }

        double MainSum = (Ssum1+Ssum2)/2;
        double diff1 = MainSum - Ssum1;
        double diff2 = MainSum - Ssum2;
        if (diff1 > diff2){
          pp1.store = 1;
          pp2.store = 1;
        }else {
          pp1.store = 2;
          pp2.store = 2;
        }
    }

}



class Base extends Thread {

    G1 g1;
    G2 g2;
    Calc c1;
    Vector baseList = new Vector();

    Base(G1 p,G2 p2) {
        g1 = p;
        g2 = p2;
    }

    @Override
    public void run() {
        try {

            while (true) {
                //System.out.println(Thread.currentThread().getName());
              //System.out.println("dsdsd");
                Integer message = g1.getMessage();
                //System.out.println("Got message: " + message);
                putMessageBase(message);

                Integer message1 = g2.getMessage();
                //System.out.println("Got message: " + message1);
                putMessageBase(message1);

                //sleep(200);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    private synchronized void putMessageBase(int a) throws InterruptedException {
      baseList.addElement(a);
      //System.out.println("put message to base");
      notify();
    }
    public synchronized Integer getMessageBase() throws InterruptedException {
          notify();
          while (baseList.size() == 0) {
              wait();//By executing wait() from a synchronized block, a thread gives up its hold on the lock and goes to sleep.
          }
          Integer message = (Integer) baseList.firstElement();
          //System.out.println("G2:"+message);
          baseList.removeElement(message);
          return message;
      }

    public static void main(String args[]) {
        G1 g1 = new G1();
        G2 g2 = new G2();

        g1.start();
        g2.start();
        Base base1  = new Base(g1,g2);
        P1 p1 = new P1(base1);
        P2 p2 = new P2(base1);
        Calc c1 = new Calc(p1,p2);
        base1.start();
        p1.start();
        p2.start();
        c1.start();

    }
}
