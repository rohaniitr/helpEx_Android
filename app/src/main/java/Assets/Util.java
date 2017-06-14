package Assets;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.rohansarkar.helpex.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

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

        if (contentList.size()<=0)
            return s;

        s = contentList.get(0);
        for (int i=1; i<contentList.size(); i++)
            s += symbol + contentList.get(i);

        Log.d(LOG_TAG, "s : " + s);
        return s;
    }

    //Checks for empty rows.
    public static boolean isEmptyRow(String rowString){
        for(int i=0; i<10; i++){
            if(rowString.contains(i+""))
                return false;
        }
        return  true;
    }

    //Savves image to
    public static boolean saveImage(View view, String folderName, String imageName, Context context){
        if(!createFolder(folderName, context)){
            return false;
        }

        String imagePath = Environment.getExternalStorageDirectory().toString() + File.separator +
                context.getString(R.string.app_name) +  File.separator + folderName.replace(" ", "") +
                File.separator + imageName.replace(" ","") + ".jpg";

        //Create bitmap image path.
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        OutputStream fout = null;
        File imageFile = new File(imagePath);

        try {
            fout = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90,  fout);
            fout.flush();
            fout.close();
            Log.d(LOG_TAG, "Path : " + imagePath);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            Log.d(LOG_TAG, "Exception in saving image.");
            return false;
        }

    }

    //Checks & Creates Folder for saving Files for this experiment.
    public static boolean createFolder(String folderName, Context context){
        File folder = new File(Environment.getExternalStorageDirectory().toString() + "/" +
                context.getString(R.string.app_name));

        boolean folderCreated = true;
        if(!folder.exists()){
            folderCreated = folder.mkdir();
        }

        if(!folderCreated)
            return false;

        File innerFolder = new File(Environment.getExternalStorageDirectory().toString() + File.separator +
                context.getString(R.string.app_name) +  File.separator + folderName.replace(" ", ""));

        if(!innerFolder.exists()){
            folderCreated = innerFolder.mkdir();
        }
        return folderCreated;
    }

    public static boolean isExplorerPresent(Context context){
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");

        List<ResolveInfo> list =  packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        if(list.size()>=0)
            return true;
        return false;
    }

    //APIs for Soft Keyboard.
    private void showKeyboard(Context context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
    public static void hideKeyboard(Context context, View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String GRAPH_LIST_SIZE = "graphListSize";
    public static String GRAPH_LIST = "graphList";
    public static String EXPERIMENT_ID = "experimentId";

    public static int MARSHMALLOW = 23;
}
