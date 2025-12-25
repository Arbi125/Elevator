public class Request {
    private final int beginFloor;
    private final int endFloor;

    public Request(int beginFloor_, int endFloor_){
        this.beginFloor=beginFloor_;
        this.endFloor=endFloor_;
    }

    public int getBeginFloor(){
        return beginFloor;
    }

    public int getEndFloor(){
        return endFloor;
    }
}
