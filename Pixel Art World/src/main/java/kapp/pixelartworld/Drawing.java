package kapp.pixelartworld;

import java.util.ArrayList;

/**
 * Created by vscheidecker on 12/11/2016.
 */

public class Drawing {
    public int colnum;
    public int startposition;
    public ArrayList<Integer> colorlist;

    public void Drawing(int colNum, ArrayList<Integer> colorList){
        colnum=colNum;
        colorlist=colorList;
    }

    public void setStartposition(int startPosition){
        startposition=startPosition;
    }

    public int getStartposition(){
        return startposition;
    }

    public int getColnum(){
        return colnum;
    }

    public ArrayList<Integer> getColorlist(){
        return colorlist;
    }
}
