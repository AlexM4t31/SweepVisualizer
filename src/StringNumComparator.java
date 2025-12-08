import java.util.Comparator;

public class StringNumComparator implements Comparator<String> {


    @Override
    public int compare(String o1,String  o2) {
        float d = Float.parseFloat(o1) - Float.parseFloat(o2);
        if ( d == 0 )
                return 0;
        else if ( d > 0 )
                return 1;
        else
            return -1;


    }
}
