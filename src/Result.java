import java.lang.reflect.Array;
import java.util.ArrayList;

public class Result {

    private String runNumber;
    private ArrayList<String> values;

    private String imageName;

    public Result(String runNumber, String imageName, ArrayList<String> values)
    {
        this.imageName = imageName;
        this.values = values;
        this.runNumber = runNumber;
    }

    public String getRunNumber(){
        return runNumber;
    }
    public String getImageName() {
        return imageName;
    }
    public ArrayList<String> getValues() {
        return values;
    }
}
