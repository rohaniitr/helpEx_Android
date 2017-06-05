package Assets;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;

/**
 * Created by rohan on 17/3/17.
 */
public class Util {

    private static String LOG_TAG = "Util";

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

        String[] contentArray= content.split(separator, -1);

        for(int i=0; i<contentArray.length; i++){
            contentList.add(contentArray[i]);
        }
        return contentList;
    }

    public static String getString(ArrayList<String> contentList, String symbol){
        String s = "";
        for(int i=0; i<contentList.size(); i++)
            s+= contentList.get(i) + " ";
        Log.d(LOG_TAG, "Content Size : " + contentList.size() + " - " + s);

        if (contentList.size()<=0)
            return s;

        s = contentList.get(0);
        for (int i=0; i<contentList.size(); i++)
            s += symbol + contentList.get(i);

        Log.d(LOG_TAG, "s : " + s);
        return s;
    }

    //APIs for Soft Keyboard.
    private void showKeyboard(Context context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
    private void hideKeyboard(Context context, View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String GRAPH_LIST_SIZE = "graphListSize";
    public static String GRAPH_LIST = "graphList";
    public static String EXPERIMENT_ID = "experimentId";


}
