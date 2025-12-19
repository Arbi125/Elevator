public class Main {
    public static void main(String[] args_){
        Building building=new Building(10, 2);
        RequestGenerator generator=new RequestGenerator(building);
        Thread generatorThread=new Thread(generator);
        generatorThread.start();
        building.startElevators();
    }
}