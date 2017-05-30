package Assets;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by rohan on 17/3/17.
 */
public class Util {

    public enum DatabaseType{
        EXPERIMENT_DETAILS,
        EXPERIMENT_RECORDS
    }

    public static enum StarType{
        STARRED(0),
        NOT_STARRED(1),
        HIDDEN(2);

        int value;
        private StarType(int val){
            this.value = val;
        }
        public int getInt(){
            return value;
        }
    }

    public static int getPixelsFromSP(int sp, Context context){
        return (int) (sp * context.getResources().getDisplayMetrics().scaledDensity);
    }

    public static ArrayList<String> splitString(String content, String separator){
        ArrayList<String> contentList = new ArrayList<>();

        if(content== null || !content.contains(separator))
            return contentList;

        String[] contentArray= content.split(separator);

        for(int i=0; i<contentArray.length; i++){
            contentList.add(contentArray[i]);
        }
        return contentList;
    }

    public static String GRAPH_LIST_SIZE = "graphListSize";
    public static String GRAPH_LIST = "graphList";
    public static String EXPERIMENT_ID = "experimentId";


}
