package kapp.pixelartworld;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 02/12/2016.
 */
public class GifObject {
    public Bitmap bm;
    public List<Bitmap> GifList;

    public GifObject(Context context){
        GifList=new ArrayList<Bitmap>();
        bm= BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_add);
        GifList.add(bm);

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

    public AnimationDrawable getAnimation(Context context,int width, int height){
        //Return animation from giflist
        AnimationDrawable animDrawable=new AnimationDrawable();
        for(int i=1;i<=GifList.size()-1;i++){
            Bitmap b = Bitmap.createScaledBitmap(GifList.get(i), width, height, false);
        Drawable frame = new BitmapDrawable(context.getResources(), b);
        animDrawable.addFrame(frame, 200);
        }

        return animDrawable;
    }

}
