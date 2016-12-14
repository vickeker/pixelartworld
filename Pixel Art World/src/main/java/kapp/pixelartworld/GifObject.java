package kapp.pixelartworld;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 02/12/2016.
 */
public class GifObject {
    public Bitmap bm;
    public List<Bitmap> GifList;
    public List<Drawing> DrawingList;
    private int REQUEST_WRITE_EXTERNAL_STORAGE=1;
    public Context context;

    public GifObject(Context mcontext){
        GifList=new ArrayList<Bitmap>();
        DrawingList=new ArrayList<Drawing>();
        bm= BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_add);
        GifList.add(bm);
        context=mcontext;

    }

    public void addImage(int position, Bitmap image){
        GifList.add(position,image);
    }

    public void deleteImage(int position){
        GifList.remove(position);
    }

    public Bitmap getImage(int position){
        return GifList.get(position);
    }

    public Integer getSize(){
        return GifList.size();
    }

    public void addDrawing(int position, Drawing drawing){
        DrawingList.add(position, drawing);
    }

    public Drawing getDrawing(int position){
        return DrawingList.get(position);
    }

    public AnimationDrawable getAnimation(int width, int height){
        //Return animation from giflist
        AnimationDrawable animDrawable=new AnimationDrawable();
        for(int i=1;i<=GifList.size()-1;i++){
            Bitmap b = Bitmap.createScaledBitmap(GifList.get(i), width, height, false);
        Drawable frame = new BitmapDrawable(context.getResources(), b);
        animDrawable.addFrame(frame, 200);
        }

        return animDrawable;
    }

    public void generateGIF(String myfilename) {
      //  ArrayList<Bitmap> bitmaps = adapter.getBitmapArray();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.start(bos);
        for (Bitmap bitmap : GifList) {
            encoder.addFrame(bitmap);
        }
        encoder.finish();
        FileOutputStream outStream = null;
        File folder = new File(Environment.getExternalStorageDirectory(),"PixelWorld/gif/");
        if(!folder.exists()) {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                //RUNTIME PERMISSION Android M
                if(PackageManager.PERMISSION_GRANTED== ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    folder = new File(Environment.getExternalStorageDirectory(),"PixelWorld/gif/");
                    if(!folder.exists()) {
                        boolean dir = folder.mkdirs();
                    }
                }else{
                    ActivityCompat.requestPermissions((Activity)context,
                            new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_WRITE_EXTERNAL_STORAGE);
                }

            }
            boolean dir = folder.mkdirs();
        }
        File file=new File(folder, myfilename+".gif");
        try{
            outStream = new FileOutputStream(file);
            outStream.write(bos.toByteArray());
            outStream.close();
        }catch(Exception e){
            e.printStackTrace();
            CharSequence text = "Saving GIF failed";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }


    public boolean NameExist(String myfilename)
            throws ClassNotFoundException {
        ArrayList<String> list;
        Boolean filenameExist = false;
        try {
            File file = context.getFilesDir();
            File filename = new File(file, "AppGifName");
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fis);
            list = (ArrayList<String>) in.readObject();
        } catch (IOException ex) {
            ex.printStackTrace();
            list = new ArrayList<String>();
        }

        for (int i = 0; i <= list.size() - 1; i++) {
            if (list.get(i).toString().equals(myfilename)) {
                filenameExist = true;
            }
        }

        return filenameExist;
    }

    public void savedrawinglist(String myfilename)
        throws ClassNotFoundException {
            ArrayList<Integer> fulllist;

            try {
                File file = context.getFilesDir();
                File filename = new File(file, myfilename);
                FileOutputStream fos = new FileOutputStream(filename);
                ObjectOutputStream out = new ObjectOutputStream(fos);
                for(Drawing d: DrawingList){
                    fulllist=new ArrayList<Integer>();
                    fulllist.add(d.getColnum());
                    fulllist.add(d.getStartposition());
                    for(Integer a: d.getColorlist()) {
                        fulllist.add(a);
                    }
                    out.writeObject(fulllist);
                }
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();

            }
    }


    public void saveGifName(String myfilename)
            throws ClassNotFoundException {
        ArrayList<String> list;
        Boolean filenameExist = false;
        try {
            File file = context.getFilesDir();
            File filename = new File(file, "AppGifName");
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fis);
            list = (ArrayList<String>) in.readObject();
        } catch (IOException ex) {
            ex.printStackTrace();
            list = new ArrayList<String>();
        }

        if (!filenameExist) {
            list.add(myfilename);
            File file = context.getFilesDir();
            File filename = new File(file, "AppGifName");
            try {
                FileOutputStream fos = new FileOutputStream(filename);
                ObjectOutputStream out = new ObjectOutputStream(fos);
                out.writeObject(list);
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
/*
    public ArrayList<HashMap<String, Object>> getSavedGif(){
        HashMap<String, Object> map;
        ArrayList<HashMap<String, Object>> list=new ArrayList<HashMap<String, Object>>();

        String path = Environment.getExternalStorageDirectory().toString()+"/PixelWorld/gif";
        File f = new File(path);
        File file[] = f.listFiles();
        if (file.length>0) {
            for (int i = 0; i < file.length; i++) {
                map = new HashMap<String, Object>();
                map.put("path", file[i].getPath());
                Drawable d = Drawable.createFromPath(file[i].getPath());
                map.put("gif", d);
                list.add(map);
            }
        }
        return list;
    }

    public void readDrawingList(String myfilename) {
        try {
            File file = context.getFilesDir();
            File filename = new File(file, myfilename);
            FileReader inputFil = new FileReader(filename);
            BufferedReader in = new BufferedReader(inputFil);
            String s =in.readLine();
         //   FileInputStream fis = new FileInputStream(filename);
*//*            byte[] bytes = new byte[(int) filename.length()];
            FileInputStream fis = new FileInputStream(filename);
            fis.read(bytes);
            fis.close();*//*
            while(s!=null)
            {
                int i = 0;
                ArrayList<Integer> list=new ArrayList<Integer>();
                list.add(Integer.parseInt(s));
                int a = list.get(0);
                int b = list.get(1);
                list.remove(0);
                list.remove(1);
                Drawing drawing=new Drawing(a,list);
                drawing.setStartposition(b);
                DrawingList.add(drawing);
                s = in.readLine();
            }

            in.close();
            String[] valueStr = new String(bytes).trim().split("\\s+");
            for (int i = 0; i < valueStr.length; i++){
                ArrayList<Integer> list=new ArrayList<Integer>();
                list.add(Integer.parseInt(valueStr[i]));
                int a = list.get(0);
                int b = list.get(1);
                list.remove(0);
                list.remove(1);
                Drawing drawing=new Drawing(a,list);
                drawing.setStartposition(b);
                DrawingList.add(drawing);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
        mColumnCount = mGlobalColorList.get(0);
        mMaxColumnCount=mColumnCount;
        mMaxChildren=mMaxColumnCount*mMaxColumnCount;
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 1; i <= mGlobalColorList.size() - 1; i++) {
            list.add(mGlobalColorList.get(i));
        }
        mStartingPosition=1;

        mColorList.clear();
        mColorList = list;
        updatechild(list);
        invalidate();
        requestLayout();
        return list;
    }*/
}
