import java.util.Random;

public class RequestGenerator implements Runnable{
    private final Building building;
    private final Random random = new Random();

    public RequestGenerator(Building building_){
        this.building=building_;
    }

    @Override
    public void run(){
        while(true){
            try{
                generateRequest();
                Thread.sleep(2000); // генерация запросов каждые 2 секунды
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    private void generateRequest(){
        int beginFloor=random.nextInt(building.getFloors())+1;
        int endFloor;
        do{
            endFloor=random.nextInt(building.getFloors())+1;
        }while(endFloor==beginFloor);
        Request request=new Request(beginFloor,endFloor);
        building.addRequest(request);
        System.out.printf("New request: from floor %d to floor %d%n",beginFloor,endFloor);
    }
}