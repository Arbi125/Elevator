import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Building {
    private final int floors;
    private final Elevator[] elevators;
    private final BlockingQueue<Request> requestBlockingQueue;

    public Building(int floors_, int numberOfElevators_){
        this.floors=floors_;
        this.requestBlockingQueue=new LinkedBlockingQueue<>();
        this.elevators=new Elevator[numberOfElevators_];

        for(int i=0;i<numberOfElevators_;i++){
            elevators[i]=new Elevator(i+1, requestBlockingQueue);
        }
    }

    public void addRequest(Request request_){
        requestBlockingQueue.add(request_);
    }

    public void startElevators(){
        for (Elevator elevator:elevators)
            new Thread(elevator).start();
    }

    public int getFloors(){
        return floors;
    }
}
