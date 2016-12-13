package kapp.pixelartworld;

import java.util.ArrayList;

/**
 * Created by vscheidecker on 12/11/2016.
 */

public class Drawing {
    public int colnum;
    public int startposition;
    public ArrayList<Integer> colorlist;

    public Drawing(int colNum, ArrayList<Integer> colorList){
        colnum=colNum;
        colorlist=new ArrayList<Integer>();
        for (Integer c : colorList) {
            colorlist.add(c);
        }
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
