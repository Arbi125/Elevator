import java.util.*;
import java.util.concurrent.BlockingQueue;

public class Elevator implements Runnable{
    private final int id;
    private int currentFloor=1;
    private boolean goingUp=true;
    private final List<Request> passengers=new ArrayList<>();
    private final BlockingQueue<Request> requestsQueue;
    private final PriorityQueue<Request> plannedStops=new PriorityQueue<>(Comparator.comparingInt(request->Math.abs(currentFloor-request.getBeginFloor())));
    private int lastFloor=-1;

    public Elevator(int id_,BlockingQueue<Request>requestsQueue_){
        this.id=id_;
        this.requestsQueue=requestsQueue_;
    }

    @Override
    public void run() {
        while (true){
            try {
                requestsHandler();
                Thread.sleep(1000);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    private void requestsHandler(){
        passengers.removeIf(request -> {
            if(request.getEndFloor()==currentFloor){
                log(String.format("Passenger dropped off at floor %d", currentFloor));
                return true;
            }
            return false;
        });

        synchronized (requestsQueue){
            Iterator<Request> iterator=requestsQueue.iterator();
            while (iterator.hasNext()){
                Request request=iterator.next();
                if(canPickUp(request)){
                    plannedStops.add(request);
                    iterator.remove();
                    log(String.format("Scheduled stop to pick up passenger at floor %d going to floor %d",request.getBeginFloor(),request.getEndFloor()));
                }
            }
        }

        plannedStops.removeIf(request -> {
            if(request.getBeginFloor()==currentFloor){
                passengers.add(request);
                log(String.format("Picked up passenger at floor %d going to floor %d",request.getBeginFloor(),request.getEndFloor()));
                return true;
            }
            return false;
        });

        if(!plannedStops.isEmpty()){
            Request nextStop=plannedStops.peek();
            if(nextStop.getBeginFloor()>currentFloor){
                goingUp=true;
                currentFloor++;
            }
            else if(nextStop.getBeginFloor()<currentFloor){
                goingUp=false;
                currentFloor--;
            }
            else
                plannedStops.poll();
        }
        else if(!passengers.isEmpty()){
            int end=passengers.get(0).getEndFloor();
            if(end>currentFloor){
                goingUp=true;
                currentFloor++;
            }
            else if(end<currentFloor){
                goingUp=false;
                currentFloor--;
            }
        }

        if(currentFloor!=lastFloor){
            log(String.format("Moved to floor %d",currentFloor));
            lastFloor=currentFloor;
        }
    }

    private boolean canPickUp(Request request_){
        if(passengers.isEmpty()){
            return true;
        }

        int firstEnd=passengers.get(0).getEndFloor();

        if(goingUp){
            return currentFloor<=request_.getBeginFloor()&&request_.getBeginFloor()<=firstEnd;
        }
        else {
            return currentFloor>=request_.getBeginFloor()&&request_.getBeginFloor()>=firstEnd;
        }
    }

    private void log(String message_){
        System.out.printf("Elevator %d: %s%n",id,message_);
    }
}
