import java.util.*;

public class MyUtilities {

    public static <T> ArrayList<T>  subListFromArray(T[] myArr, int start, int end) throws Exception{

        if ( start >= myArr.length )
            throw new Exception("Start greater than or equal to array length.");

        if (end > myArr.length)
            throw new Exception("End index greater than array length");

        if (start >= end)
            throw new Exception("Okay let's take this step by step bud.");

        if (start == 0 && end == myArr.length)
            throw new Exception("No. Look at the method name. SUB-list.");

        ArrayList<T> newSubList = new ArrayList<>();

        for(int i = start; i < end; i++ )
        {
            newSubList.add(myArr[i]);
        }

        return newSubList;
    }

}
