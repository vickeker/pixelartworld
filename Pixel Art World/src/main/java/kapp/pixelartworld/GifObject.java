package kapp.pixelartworld;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 02/12/2016.
 */
public class GifObject {

    public List<Bitmap> GifList;

    public GifObject(){
        GifList=new ArrayList<Bitmap>();
    }

    public void addImage(Bitmap image){
        GifList.add(image);
    }

    public void insertImage(int position, Bitmap image){
        GifList.add(position, image);
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

    public AnimationDrawable getAnimation(Context context){
        //Return animation from giflist
        AnimationDrawable animDrawable=new AnimationDrawable();
        for(int i=1;i<=GifList.size();i++){
        Drawable frame = new BitmapDrawable(context.getResources(), GifList.get(i));
        animDrawable.addFrame(frame, 200);
        }

        return animDrawable;
    }

}
