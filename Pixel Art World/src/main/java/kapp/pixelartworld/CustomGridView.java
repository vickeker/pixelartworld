package kapp.pixelartworld;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomGridView extends ViewGroup {

	private static final int DEFAULT_COUNT = 3;

	private Paint mGridPaint;
	private int mStartingPosition;
	private int mColumnCount;
	private int mMaxChildren;
	private int mMaxColumnCount;
	private int mBackgroundColor;
	private int mSelectedColor;
	private int Default_mColumnCount;
	private int Default_mStartingPosition;
	private int Default_mBackgroundColor;
	private ArrayList<Integer> Default_mGlobalColorList;
	private ArrayList<Integer> Default_mColorList;

	private ArrayList<Integer> mColorList;
	private ArrayList<HashMap<String, Integer>> SavedActionList = null;
	private ArrayList<Integer> mGlobalColorList; // Le premier element est mMaxColumnCount
	private boolean COLORIZE_FLAG=false;
	private Context mContext;
	private ScaleGestureDetector mScaleDetector;
	private GestureDetector mGestureDetector;
	private float mScaleFactor = 1.0f;
	private static final int SWIPE_MIN_DISTANCE = 10;
	private static final int SWIPE_THRESHOLD_VELOCITY = 10;
	private int viewsize;
	private float extraGrid;
	private float mTotalSpan=0;
	private int focusX=-1;
	private int focusY=-1;
	private int REQUEST_WRITE_EXTERNAL_STORAGE=1;

	public CustomGridView(Context context) {
		this(context, null);
		mContext = context;
	}

	public CustomGridView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		mContext = context;
	}

private Bitmap bitmap;



	public CustomGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.CustomGridView, 0, defStyle);

		int strokeWidth = a.getDimensionPixelSize(
				R.styleable.CustomGridView_separatorWidth, 0);
		int strokeColor = a.getColor(R.styleable.CustomGridView_separatorColor,
				Color.GRAY);
		int backgroundColor = a.getColor(
				R.styleable.CustomGridView_BackgroundColor, Color.WHITE);
		mColumnCount = a.getInteger(R.styleable.CustomGridView_numColumns,
				DEFAULT_COUNT);
		Default_mColumnCount=mColumnCount;
		mMaxChildren = mColumnCount * mColumnCount;
		mMaxColumnCount = mColumnCount;
		mBackgroundColor = Color.WHITE;
		Default_mBackgroundColor=mBackgroundColor;
		mStartingPosition = 1;
		Default_mStartingPosition=mStartingPosition;
		mGlobalColorList = new ArrayList<Integer>();
					mColorList = new ArrayList<Integer>();
					mGlobalColorList.add(mMaxColumnCount);
		Default_mGlobalColorList=new ArrayList<Integer>();
		Default_mGlobalColorList.add(mMaxColumnCount);
		Default_mColorList= new ArrayList<Integer>();
		for (int i = 1; i <= mMaxChildren; i++) {
			mGlobalColorList.add(mBackgroundColor);
			mColorList.add(mBackgroundColor);
			Default_mGlobalColorList.add(mBackgroundColor);
			Default_mColorList.add(mBackgroundColor);
		}

		SavedActionList = new ArrayList<HashMap<String, Integer>>();

		a.recycle();

		mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mGridPaint.setStyle(Paint.Style.STROKE);
		mGridPaint.setColor(strokeColor);
		mGridPaint.setStrokeWidth(strokeWidth);

		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
		mGestureDetector=new GestureDetector(context, new GestureDetectorListener());

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev){
       // mScaleDetector.onTouchEvent(ev);
	//	mGestureDetector.onTouchEvent(ev);
		super.dispatchTouchEvent(ev);
		return true;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		final int action = MotionEventCompat.getActionMasked(ev);

		mScaleDetector.onTouchEvent(ev);
		if(!mScaleDetector.isInProgress()) {
			mGestureDetector.onTouchEvent(ev);
		}

		if (action == MotionEvent.ACTION_MOVE) {
			final int xDiff;
			final int yDiff;

			if (ev.getHistorySize() > 0) {
				xDiff = Math.abs((int) (ev.getHistoricalX(0) - ev.getX()));
				yDiff = Math.abs((int) (ev.getHistoricalY(0) - ev.getY()));
				if ((xDiff > SWIPE_MIN_DISTANCE || yDiff > SWIPE_MIN_DISTANCE)) {
					return true;
				} else {
					return false;
				}

			} else {
				return false;
			}


		} else {
			//go to child click
			return false;
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		super.onTouchEvent(ev);
		boolean retVal = mScaleDetector.onTouchEvent(ev);
		retVal = mGestureDetector.onTouchEvent(ev) || retVal;
		return retVal;

	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize, heightSize;

		// Get the width based on the measure specs
		widthSize = getDefaultSize(0, widthMeasureSpec);

		// Get the height based on measure specs
		heightSize = getDefaultSize(0, heightMeasureSpec);

		int majorDimension = Math.min(widthSize, heightSize);
		// Measure all child views
		int blockDimension = majorDimension / mColumnCount;
		extraGrid=majorDimension%mColumnCount;
		int blockSpec = MeasureSpec.makeMeasureSpec(blockDimension,
				MeasureSpec.EXACTLY);
		measureChildren(blockSpec, blockSpec);

		// MUST call this to save our own dimensions
		setMeasuredDimension(majorDimension, majorDimension);
		viewsize=majorDimension;
	}

	public int getNumCol() {
		return mColumnCount;
	}

	public void setNumCol(int newnumcol) {
		mColumnCount = newnumcol;
		updatechild(mColorList);
		invalidate();
		requestLayout();
	}

	public void setcolorizeflag(boolean flag){
	COLORIZE_FLAG=flag;
	}

	public void setMaxChildren(int maxchildren){
		mMaxChildren=maxchildren;
	}

	public int getMaxChildren() {
		return mMaxChildren;
	}

	public int getBackgroundColor() {
		return mBackgroundColor;
	}

	public void setSelectedColor(int selectedcolor) {
		mSelectedColor = selectedcolor;
	}

	public void setColorList(ArrayList<Integer> colorlist) {
		mColorList=new ArrayList<Integer>();
		mColorList = colorlist;
	}

	public ArrayList<Integer> getColorList() {
		return mColorList;
	}

	public void setGlobalColorList(ArrayList<Integer> globalcolorlist) {
		mGlobalColorList=new ArrayList<Integer>();
		mGlobalColorList = globalcolorlist;
		mMaxColumnCount=mGlobalColorList.get(0);
	}

	public ArrayList<Integer> getGlobalColorList() {
		return mGlobalColorList;
	}

	public void setSavedActionList(ArrayList<HashMap<String,Integer>> savedactionlist) {
		SavedActionList=new ArrayList<HashMap<String, Integer>>();
		SavedActionList = savedactionlist;
	}

	public ArrayList<HashMap<String,Integer>> getSavedActionList() {
		return SavedActionList;
	}

	public int getStartingPosition() {
		return mStartingPosition;
	}

	public void setStartingPosition(int startingposition) {
		mStartingPosition = startingposition;
	}

	public interface ViewWasTouchedListener {
		void onViewTouched(int mSelectedColor);
	}
	public interface ScaleChangeListener {
		void onScaleChange(ArrayList<Integer> mColorList);
	}

	ArrayList<ViewWasTouchedListener> listeners = new ArrayList<ViewWasTouchedListener>();
	public void setWasTouchedListener(ViewWasTouchedListener listener){
		listeners.add(listener);
	}

	ArrayList<ScaleChangeListener> listeners2 = new ArrayList<ScaleChangeListener>();
	public void setScaleChangeListener(ScaleChangeListener listener2){
		listeners2.add(listener2);
	}



	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int row, col, left, top;
		for (int i = 0; i < getChildCount(); i++) {
			row = i / mColumnCount;
			col = i % mColumnCount;
			View child = getChildAt(i);
			left =(int)(extraGrid/2)+ col * child.getMeasuredWidth();
			top = (int)(extraGrid/2)+ row * child.getMeasuredHeight();

			child.layout(left, top, left + child.getMeasuredWidth(), top
					+ child.getMeasuredHeight());
		}

	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		// Let the framework do its thing
		super.dispatchDraw(canvas);

		// Draw the grid lines
		for (int i = (int)extraGrid/2; i <= getWidth()-(int)extraGrid/2; i += (getWidth() / mColumnCount)) {
			canvas.drawLine(i, 0+extraGrid/2, i, getHeight()-extraGrid/2, mGridPaint);
		}
		for (int i = (int)extraGrid/2; i <= getHeight()-(int)extraGrid/2; i += (getHeight() / mColumnCount)) {
			canvas.drawLine(extraGrid/2, i, getWidth()-extraGrid/2, i, mGridPaint);
		}
	}

	@Override
	public void addView(View child) {
		if (getChildCount() > mMaxChildren - 1) {
			throw new IllegalStateException(
					Integer.toString(getChildCount())+": BoxGridLayout cannot have more than " + mMaxChildren
							+ " direct children");
		} else {
			child.setOnClickListener(new View.OnClickListener() {
				public void onClick(View vue) {
					if(COLORIZE_FLAG){
						ColorDrawable color=(ColorDrawable) vue.getBackground();
						mSelectedColor=color.getColor();
						setSelectedColor(color.getColor());
						for (ViewWasTouchedListener listener:listeners){
							listener.onViewTouched(mSelectedColor);
						}
						COLORIZE_FLAG=false;
					}else {
						HashMap<String, Integer> SavedAction = new HashMap<String, Integer>();
						int position = indexOfChild(vue);
						int globalposition = getGlobalPosition(position + 1);
						SavedAction.put("Position", globalposition);
						ColorDrawable drawable = (ColorDrawable) vue
								.getBackground();
						SavedAction.put("PrevColor", drawable.getColor());
						SavedActionList.add(SavedAction);
						vue.setBackgroundColor(mSelectedColor);
						if (mColorList.size() <= position) {
							for (int i = mColorList.size(); i < position; i++) {
								mColorList.set(i, mBackgroundColor);
							}
						}
						mColorList.set(position, mSelectedColor);
						invalidate();
						requestLayout();
					}
					}

			});
		}

		super.addView(child);
	}

	@Override
	public void addView(View child, int index) {
		if (getChildCount() > mMaxChildren - 1) {
			throw new IllegalStateException(
					"BoxGridLayout cannot have more than " + mMaxChildren
							+ " direct children");
		}

		super.addView(child, index);
	}

	@Override
	public void addView(View child, int index, LayoutParams params) {
		if (getChildCount() > mMaxChildren - 1) {
			throw new IllegalStateException(
					"BoxGridLayout cannot have more than " + mMaxChildren
							+ " direct children");
		}

		super.addView(child, index, params);
	}

	@Override
	public void addView(View child, LayoutParams params) {
		if (getChildCount() > mMaxChildren - 1) {
			throw new IllegalStateException(
					"BoxGridLayout cannot have more than " + mMaxChildren
							+ " direct children");
		}

		super.addView(child, params);
	}

	@Override
	public void addView(View child, int width, int height) {
		if (getChildCount() > mMaxChildren - 1) {
			throw new IllegalStateException(
					"BoxGridLayout cannot have more than " + mMaxChildren
							+ " direct children");
		}

		super.addView(child, width, height);
	}

	public void undo() {
		if (SavedActionList.size() != 0) {
			int lastelement = SavedActionList.size() - 1;
			int pos = (Integer) SavedActionList.get(lastelement)
					.get("Position");
			int prevcolor = (Integer) SavedActionList.get(lastelement).get(
					"PrevColor");
			SavedActionList.remove(lastelement);
			if(pos<mStartingPosition||((pos-1)%mMaxColumnCount)<((mStartingPosition-1)%mMaxColumnCount)||((pos-1)%mMaxColumnCount)>=(mStartingPosition%mMaxColumnCount)+mColumnCount-1||pos>mStartingPosition+(mColumnCount*mMaxColumnCount)-(mStartingPosition%mMaxColumnCount)){
				mGlobalColorList.set(pos,prevcolor);
			} else {
				int displaypos = getDisplayPosition(pos);
				mColorList.set(displaypos, prevcolor);
				setGlobalList(mColorList);
				ImageView iv = (ImageView) getChildAt(displaypos);
				iv.setBackgroundColor(prevcolor);
				invalidate();
				requestLayout();
			}
		} else {
			CharSequence text = mContext.getString(R.string.noactionsaved);
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(mContext, text, duration);
			toast.show();
		}
	}

	public void updateSavedActionList(int maxCol, int col){
		for (int i=0;i<=SavedActionList.size()-1;i++){
			int oldposition=(Integer) SavedActionList.get(i).get("Position");
			HashMap<String, Integer> map=new HashMap<String, Integer>();
			int newposition=((int)(Math.floor(oldposition/(maxCol-2))+1))*maxCol+(oldposition%(maxCol-2)+1);
			map.put("Position",newposition);
			map.put("PrevColor", (Integer) SavedActionList.get(i).get("PrevColor"));
			SavedActionList.set(i, map);
		}
	}

	public int getGlobalPosition(int oldposition) {
		int newposition=(int)(Math.floor(mStartingPosition/mMaxColumnCount)+Math.floor(oldposition/mColumnCount))*mMaxColumnCount+mStartingPosition%mMaxColumnCount-1+oldposition%mColumnCount;
				return newposition;
	}

	public int getDisplayPosition(int oldposition) {
		int a=oldposition-mStartingPosition;
		int b=(int)Math.floor((oldposition-mStartingPosition)/mMaxColumnCount);
		int newposition=((a)-b*(mMaxColumnCount-mColumnCount));
		return newposition;
	}


	public ArrayList<Integer> cleargrid() {
			mColumnCount=Default_mColumnCount;
			ArrayList<Integer> newlist=new ArrayList<Integer>();

			mBackgroundColor=Default_mBackgroundColor;
			mStartingPosition=Default_mStartingPosition;
			mGlobalColorList.clear();
			mGlobalColorList.addAll(Default_mGlobalColorList);
			mColorList.clear();
			mColorList.addAll(Default_mColorList);
			mMaxColumnCount=mColumnCount;
			mMaxChildren=mColumnCount*mColumnCount;
			newlist=mColorList;



			updatechild(mColorList);
			invalidate();
			requestLayout();

	if (SavedActionList != null) {
		SavedActionList.clear();
	}

			return newlist;
	}

	public void updatechild(ArrayList<Integer> newlist) {
		for (int i = 0; i <= newlist.size() - 1; i++) {
			if (getChildAt(i) != null) {
				ImageView child = (ImageView) getChildAt(i);
				child.setBackgroundColor(newlist.get(i));
			} else {
				ImageView child = new ImageView(mContext);
				child.setBackgroundColor(newlist.get(i));
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						-1, -1);
				child.setLayoutParams(layoutParams);
				addView(child);
			}
		}


	}

	public void setGlobalList(ArrayList<Integer> colorlist) {
		if (colorlist.size() < mGlobalColorList.size() - 1) {
			int a = 0;
			for (int j = 0; j <= mColumnCount - 1; j++) {
				for (int i = mStartingPosition + (j * mMaxColumnCount); i <= mStartingPosition
						+ (j * mMaxColumnCount) + mColumnCount - 1; i++) {
					mGlobalColorList.set(i, colorlist.get(a));
					a++;
				}
			}
		} else if (colorlist.size() >= mGlobalColorList.size() - 1) {
			if(mColumnCount!=mMaxColumnCount) {
				updateSavedActionList(mColumnCount, mMaxColumnCount);
			}
			mGlobalColorList.clear();
			mGlobalColorList.add(mColumnCount);
			mGlobalColorList.addAll(colorlist);
			mMaxColumnCount = mColumnCount;

		}

	}


	public void updateList(ArrayList<Integer> list, String myfilename) {
			try {
				setGlobalList(list);
				File file = mContext.getFilesDir();
				File filename = new File(file, myfilename);
				FileOutputStream fos = new FileOutputStream(filename);
				ObjectOutputStream out = new ObjectOutputStream(fos);
				out.writeObject(mGlobalColorList);
				out.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
	}

	public boolean saveList(ArrayList<Integer> list, String myfilename) {
		boolean fileNameExist = false;
		try {
			fileNameExist = saveDrawingName(myfilename);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!fileNameExist) {
			try {
				setGlobalList(list);
				File file = mContext.getFilesDir();
				File filename = new File(file, myfilename);
				FileOutputStream fos = new FileOutputStream(filename);
				ObjectOutputStream out = new ObjectOutputStream(fos);
				out.writeObject(mGlobalColorList);
				out.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return fileNameExist;
	}

	public ArrayList<Integer> readList(String myfilename) {
		try {
			File file = mContext.getFilesDir();
			File filename = new File(file, myfilename);
			FileInputStream fis = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(fis);
			mGlobalColorList = (ArrayList<Integer>) in.readObject();
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
	}

	public ArrayList<Integer> openDrawing(Drawing drawing){
		ArrayList<Integer> drawinglist = new ArrayList<Integer>(drawing.getColorlist());
		drawinglist.add(0,drawing.getColnum());
		mGlobalColorList = drawinglist;
		mColumnCount = mGlobalColorList.get(0);
		mMaxColumnCount=mColumnCount;
		mMaxChildren=mMaxColumnCount*mMaxColumnCount;
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 1; i <= mGlobalColorList.size() - 1; i++) {
			list.add(mGlobalColorList.get(i));
		}
		mStartingPosition=drawing.getStartposition();
		mColorList.clear();
		mColorList = list;
		updatechild(list);
		invalidate();
		requestLayout();
		return list;
	}

	public boolean saveDrawingName(String myfilename)
			throws ClassNotFoundException {
		ArrayList<String> list;
		Boolean filenameExist = false;
		try {
			File file = mContext.getFilesDir();
			File filename = new File(file, "AppDrawingName");
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
		if (!filenameExist) {
			list.add(myfilename);
			File file = mContext.getFilesDir();
			File filename = new File(file, "AppDrawingName");
			try {
				FileOutputStream fos = new FileOutputStream(filename);
				ObjectOutputStream out = new ObjectOutputStream(fos);
				out.writeObject(list);
				out.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return filenameExist;
	}

	public ArrayList<String> readDrawingName() {
		ArrayList<String> list=new ArrayList<String>();
		try {
			File file = mContext.getFilesDir();
			File filename = new File(file, "AppDrawingName");
			FileInputStream fis = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(fis);
			 list= (ArrayList<String>)in.readObject();
			in.close();
			return list;
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public Bitmap getCacheDrawing(){
		this.setDrawingCacheEnabled(true);
		Bitmap bitmap = Bitmap.createBitmap(this.getWidth(),
				this.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		this.draw(canvas);
		return bitmap;
	}

	public void saveAsJpeg(String myfilename) {
		Bitmap b = getCacheDrawing();
		File folder = new File(Environment.getExternalStorageDirectory(),"PixelWorld/");
	if(!folder.exists()) {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			//RUNTIME PERMISSION Android M
			if(PackageManager.PERMISSION_GRANTED== ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
				folder = new File(Environment.getExternalStorageDirectory(),"PixelWorld/");
				if(!folder.exists()) {
					boolean dir = folder.mkdirs();
				}
			}else{
				ActivityCompat.requestPermissions((Activity)mContext,
						new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
						REQUEST_WRITE_EXTERNAL_STORAGE);
			}

		}
		boolean dir = folder.mkdirs();
		}
		File file=new File(folder, myfilename+".jpg");
			try {
				b.compress(CompressFormat.JPEG, 95, new FileOutputStream(
						file));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				CharSequence text = mContext.getString(R.string.nojpegsaved);
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(mContext, text, duration);
				toast.show();
			}
	}

	public ArrayList<HashMap<String, Object>> getSavedJpeg(){
	HashMap<String, Object> map;
		ArrayList<HashMap<String, Object>> list=new ArrayList<HashMap<String, Object>>();

		String path = Environment.getExternalStorageDirectory().toString()+"/PixelWorld";
		File f = new File(path);
		File file[] = f.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".jpg");
			}
		});
		if (file.length>0) {
			for (int i = 0; i < file.length; i++) {
				map = new HashMap<String, Object>();
				map.put("path", file[i].getPath());
				Drawable d = Drawable.createFromPath(file[i].getPath());
				map.put("image", d);
				list.add(map);
			}
		}
		return list;
	}




	public void deletefile(String myfilename) {
		mContext.deleteFile(myfilename);
		try {
		File file = mContext.getFilesDir();
		File filename = new File(file, "AppDrawingName");
		FileInputStream fis = new FileInputStream(filename);
		ObjectInputStream in = new ObjectInputStream(fis);
		ArrayList<String> list = (ArrayList<String>) in.readObject();
			list.remove(myfilename);
		in.close();
			FileOutputStream fos = new FileOutputStream(filename);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(list);
			out.close();
	} catch (IOException ex) {
		ex.printStackTrace();
	} catch (ClassNotFoundException ex) {
		ex.printStackTrace();
	}

	}


	// NumColaAjouter doit etre pair et la repartition des colonnes est
	// symetrique
	public ArrayList<Integer> addcol(ArrayList<Integer> oldlist,
			int oldcolcount, int NumColaAjouter) {
		if(oldlist.get(0)>0){
			oldlist.remove(0);
		}
		ArrayList<Integer> newList = new ArrayList<Integer>();
		if (NumColaAjouter % 2 == 0 || NumColaAjouter != 0) {
			for (int i = 0; i <= (NumColaAjouter / 2)
					* (oldcolcount + NumColaAjouter) - 1; i++) {
				newList.add(mBackgroundColor);
			}
			int a = 0;
			for (int j = 0; j <= oldcolcount - 1; j++) {
				for (int i = 0; i <= (NumColaAjouter / 2) - 1; i++) {
					newList.add(mBackgroundColor);
				}
				for (int i = NumColaAjouter
						+ (j * (oldcolcount + NumColaAjouter)); i <= NumColaAjouter
						+ (j * (oldcolcount + NumColaAjouter))
						+ oldcolcount
						- 1; i++) {
					newList.add(oldlist.get(a));
					a++;
				}
				for (int i = 0; i <= (NumColaAjouter / 2) - 1; i++) {
					newList.add(mBackgroundColor);
				}
			}
			for (int i = 0; i <= (NumColaAjouter / 2)
					* (oldcolcount + NumColaAjouter) - 1; i++) {
				newList.add(mBackgroundColor);
			}
		} else {
			newList = null;
		}
		return newList;
	}

	public ArrayList<Integer> zoomout(ArrayList<Integer> oldlist) {
		ArrayList<Integer> newList = new ArrayList<Integer>();

		if (oldlist.size() < mGlobalColorList.size() - 1) {
			setGlobalList(oldlist);

			
			if((mStartingPosition%mMaxColumnCount)==1||(mStartingPosition+mColumnCount-1)%mMaxColumnCount==0||mStartingPosition<=mMaxColumnCount||mStartingPosition>(mMaxColumnCount*mMaxColumnCount)-(mColumnCount*mMaxColumnCount)){
				oldlist=addcol(mGlobalColorList, mMaxColumnCount,2);
				int a=mColumnCount;
				mColumnCount=mMaxColumnCount+2;
				setGlobalList(oldlist);
				mColumnCount=a;
				mStartingPosition=(((int)Math.floor(mStartingPosition/(mMaxColumnCount-2)))*(mMaxColumnCount))+(mStartingPosition%(mMaxColumnCount-2));
				}
			/*
			if((mStartingPosition%(mMaxColumnCount-2))==1){
			//aligné gauche
			mStartingPosition=(((int)Math.floor(mStartingPosition/(mMaxColumnCount-2)))*(mMaxColumnCount))+1;
			} else if((mStartingPosition+mColumnCount-1)%(mMaxColumnCount-2)==0){
			//aligné droite
				//verifier les valeurs de mMaxColumn et mColumn
				mStartingPosition=(((int)Math.floor(mStartingPosition/(mMaxColumnCount-2)))*(mMaxColumnCount))+(mStartingPosition%(mMaxColumnCount-2));
			} else if(mStartingPosition<=(mMaxColumnCount-2)){
			//aligné haut	 
			mStartingPosition=mStartingPosition+mMaxColumnCount;
			} else if(mStartingPosition>=mMaxChildren-(mMaxColumnCount-2)){
			//aligné bas
				mStartingPosition=((int)(mStartingPosition/(mColumnCount))+1)*mMaxColumnCount+(mStartingPosition%(mMaxColumnCount-2))+1;
			} */ else {
			mStartingPosition = mStartingPosition - (mMaxColumnCount) - 1;
			}
			mColumnCount=mColumnCount+2;
			for (int j = 0; j <= mColumnCount - 1; j++) {
				for (int i = mStartingPosition + (j * mMaxColumnCount); i <= mStartingPosition
						+ (j * mMaxColumnCount) + mColumnCount - 1; i++) {
					newList.add(mGlobalColorList.get(i));
				}
			}
		} else {
			oldlist = addcol(oldlist, mColumnCount, 2
			);
			mColumnCount=mColumnCount+2;
			setGlobalList(oldlist);
			for (int i = 1; i <= mGlobalColorList.size()-1; i++) {
				newList.add(mGlobalColorList.get(i));
			}
		}
		mMaxChildren = mColumnCount * mColumnCount;
		mColorList.clear();
		mColorList = newList;
		updatechild(newList);
		invalidate();
		requestLayout();
		return newList;
	}

	public ArrayList<Integer> zoomin(ArrayList<Integer> oldlist) {
		ArrayList<Integer> newList = new ArrayList<Integer>();
		if (mColumnCount >= 4) {
			setGlobalList(oldlist);
			int a = mStartingPosition + mMaxColumnCount + 1;
			mStartingPosition = a;
			mColumnCount = mColumnCount - 2;
			for (int j = 0; j <= mColumnCount - 1; j++) {
				for (int i = a + (j * mMaxColumnCount); i <= a
						+ (j * mMaxColumnCount) + mColumnCount - 1; i++) {
					newList.add(mGlobalColorList.get(i));
				}
			}
			mColorList.clear();
			mColorList = newList;
			updatechild(newList);
		} else {
			newList = null;
		}
		mMaxChildren = mColumnCount * mColumnCount;
		invalidate();
		requestLayout();
		return newList;
	}

	public ArrayList<Integer> moveright(ArrayList<Integer> oldlist) {
		ArrayList<Integer> newList = new ArrayList<Integer>();
		ArrayList<Integer> list = new ArrayList<Integer>();
		setGlobalList(oldlist);
		int line = (int) mStartingPosition / mMaxColumnCount;
		if (mStartingPosition + mColumnCount > mMaxColumnCount*(line+1)) {
			list = addcol(mGlobalColorList, mMaxColumnCount, 2);
			mGlobalColorList.clear();
			mMaxColumnCount = mMaxColumnCount + 2;
			mGlobalColorList.add(mMaxColumnCount);
			mGlobalColorList.addAll(list);
			mStartingPosition = mStartingPosition + mMaxColumnCount
					+ line * 2 +2;
			for (int j = 0; j <= mColumnCount - 1; j++) {
				for (int i = mStartingPosition + (j * mMaxColumnCount); i <= mStartingPosition
						+ (j * mMaxColumnCount) + mColumnCount - 1; i++) {
					newList.add(mGlobalColorList.get(i));
				}
			}
			mMaxChildren = mColumnCount * mColumnCount;
			updateSavedActionList(mMaxColumnCount, mMaxColumnCount-2);
		} else {
			mStartingPosition=mStartingPosition+1;
			for (int j = 0; j <= mColumnCount - 1; j++) {
				for (int i = mStartingPosition + (j * mMaxColumnCount); i <= mStartingPosition
						+ (j * mMaxColumnCount) + mColumnCount - 1; i++) {
					newList.add(mGlobalColorList.get(i));
				}
			}
		}
		mColorList.clear();
		mColorList = newList;
		updatechild(newList);
		invalidate();
		requestLayout();
		return newList;
	}

	
	public ArrayList<Integer> moveleft(ArrayList<Integer> oldlist) {
		ArrayList<Integer> newList = new ArrayList<Integer>();
		ArrayList<Integer> list = new ArrayList<Integer>();
		setGlobalList(oldlist);
		int line = (int) mStartingPosition / mMaxColumnCount;
		if ((mStartingPosition-1)%mMaxColumnCount==0) {
			list = addcol(mGlobalColorList, mMaxColumnCount, 2);
			mGlobalColorList.clear();
			mMaxColumnCount = mMaxColumnCount + 2;
			mGlobalColorList.add(mMaxColumnCount);
			mGlobalColorList.addAll(list);
			mStartingPosition = (line+1)* mMaxColumnCount + 1;
			for (int j = 0; j <= mColumnCount - 1; j++) {
				for (int i = mStartingPosition + (j * mMaxColumnCount); i <= mStartingPosition
						+ (j * mMaxColumnCount) + mColumnCount - 1; i++) {
					newList.add(mGlobalColorList.get(i));
				}
			}
			mMaxChildren = mColumnCount * mColumnCount;
			updateSavedActionList(mMaxColumnCount, mColumnCount);

		} else {
			mStartingPosition=mStartingPosition-1;
			for (int j = 0; j <= mColumnCount - 1; j++) {
				for (int i = mStartingPosition + (j * mMaxColumnCount); i <= mStartingPosition
						+ (j * mMaxColumnCount) + mColumnCount - 1; i++) {
					newList.add(mGlobalColorList.get(i));
				}
			}
		}
		mColorList.clear();
		mColorList = newList;
		updatechild(newList);
		invalidate();
		requestLayout();
		return newList;
	}
	
	public ArrayList<Integer> moveup(ArrayList<Integer> oldlist) {
		ArrayList<Integer> newList = new ArrayList<Integer>();
		ArrayList<Integer> list = new ArrayList<Integer>();
		setGlobalList(oldlist);
		int line = (int) mStartingPosition / mMaxColumnCount;
		if (mStartingPosition<=mMaxColumnCount) {
			list = addcol(mGlobalColorList, mMaxColumnCount, 2);
			mGlobalColorList.clear();
			mMaxColumnCount = mMaxColumnCount + 2;
			mGlobalColorList.add(mMaxColumnCount);
			mGlobalColorList.addAll(list);
			mStartingPosition=mStartingPosition+1;
			for (int j = 0; j <= mColumnCount - 1; j++) {
				for (int i = mStartingPosition + (j * mMaxColumnCount); i <= mStartingPosition
						+ (j * mMaxColumnCount) + mColumnCount -1; i++) {
					newList.add(mGlobalColorList.get(i));
				}
			}
			mMaxChildren = mColumnCount * mColumnCount;
			updateSavedActionList(mMaxColumnCount, mColumnCount);

		} else {
			mStartingPosition=mStartingPosition-mMaxColumnCount;
			for (int j = 0; j <= mColumnCount - 1; j++) {
				for (int i = mStartingPosition + (j * mMaxColumnCount); i <= mStartingPosition
						+ (j * mMaxColumnCount) + mColumnCount - 1; i++) {
					newList.add(mGlobalColorList.get(i));
				}
			}
					}
		mColorList.clear();
		mColorList = newList;
		updatechild(newList);
		invalidate();
		requestLayout();
		return newList;
	}
	
	public ArrayList<Integer> movedown(ArrayList<Integer> oldlist) {
		ArrayList<Integer> newList = new ArrayList<Integer>();
		ArrayList<Integer> list = new ArrayList<Integer>();
		setGlobalList(oldlist);
		int line = (int) mStartingPosition / mMaxColumnCount;
		if (mStartingPosition>mMaxChildren-(mColumnCount*mMaxColumnCount)) {
			list = addcol(mGlobalColorList, mMaxColumnCount, 2);
			mMaxColumnCount = mMaxColumnCount+2;
			mGlobalColorList.clear();
			mGlobalColorList.add(mMaxColumnCount);
			mGlobalColorList.addAll(list);
			mStartingPosition = mStartingPosition + mMaxColumnCount*2
					+ line * 2 + 1;
			for (int j = 0; j <= mColumnCount -1; j++) {
				for (int i = mStartingPosition + (j * mMaxColumnCount); i <= mStartingPosition
						+ (j * mMaxColumnCount) + mColumnCount - 1; i++) {
					newList.add(mGlobalColorList.get(i));
				}
			}
			mMaxChildren = mColumnCount * mColumnCount;
			updateSavedActionList(mMaxColumnCount, mColumnCount);

		} else {
			mStartingPosition=mStartingPosition+mMaxColumnCount;
			for (int j = 0; j <= mColumnCount - 1; j++) {
				for (int i = mStartingPosition + (j * mMaxColumnCount); i <= mStartingPosition
						+ (j * mMaxColumnCount) + mColumnCount - 1; i++) {
					newList.add(mGlobalColorList.get(i));
				}
			}
		}
		mColorList.clear();
		mColorList = newList;
		updatechild(newList);
		invalidate();
		requestLayout();
		return newList;
	}

	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			if(focusX==-1){
				focusX=(int)detector.getFocusX();
						}
			if(focusY==-1){
				focusY=(int)detector.getFocusY();
							}
			int focusCell=getfocuscell();
			int globalfocusCell=getGlobalPosition(focusCell);

			mScaleFactor = detector.getScaleFactor();
			int mScaleSpan=(int)detector.getCurrentSpan();
			mTotalSpan+=(detector.getCurrentSpan() - (detector.getCurrentSpan() / mScaleFactor));
			// Don't let the object get too small or too large.
			int colscalefactor;

			colscalefactor = (int) (mTotalSpan/Math.min((viewsize/mColumnCount),80f));

			colscalefactor = Math.min(Math.max(-5, Math.min(colscalefactor, 5)), (mColumnCount-2)/2);

			if(colscalefactor!=0) {

				if (colscalefactor < 0) {
					for (int i = 1; i <= (-colscalefactor); i++) {
						zoomout(mColorList);
						globalfocusCell=((int)(Math.floor(globalfocusCell/(mMaxColumnCount-2))+1))*mMaxColumnCount+(globalfocusCell%(mMaxColumnCount-2)+1);
					}
				} else if(colscalefactor>0) {
					for (int i = 1; i <= colscalefactor; i++) {
						zoomin(mColorList);
					}
					centercell(globalfocusCell);
				}

				for (ScaleChangeListener listener2 : listeners2) {
					listener2.onScaleChange(mColorList);
				}

				mTotalSpan=0;
				invalidate();
			}

			return true;
		}

		@Override
		public void onScaleEnd (ScaleGestureDetector detector){
			mTotalSpan=0;
			focusX=-1;
			focusY=-1;
		}
	}

	private class GestureDetectorListener extends GestureDetector.SimpleOnGestureListener {
		protected MotionEvent mLastOnDownEvent = null;

		@Override
		public boolean onDown(MotionEvent e) {
			mLastOnDownEvent = e;
			return true;
		}

		@Override
		public boolean onScroll (MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			int deltaCol=0;
			if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				deltaCol=(int)((e1.getX()-e2.getX())/(viewsize/mColumnCount));
				for(int i = 0;i <= deltaCol;i++){
					moveright(mColorList);
				}

 // Right to left
			}  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				deltaCol=(int)((e2.getX()-e1.getX())/(viewsize/mColumnCount));
				for(int i=0;i<=deltaCol;i++){
					moveleft(mColorList);
				}

// Left to right
			}

			if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
				deltaCol=(int)((e1.getY()-e2.getY())/(viewsize/mColumnCount));
				for(int i=0;i<=deltaCol;i++){
					movedown(mColorList);
				}

 // Bottom to top
			}  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
				deltaCol=(int)((e2.getY()-e1.getY())/(viewsize/mColumnCount));
				for(int i=0;i<=deltaCol;i++){
					moveup(mColorList);
				}

 // Top to bottom
			}
			for (ScaleChangeListener listener2:listeners2){
				listener2.onScaleChange(mColorList);
			}
			return true;
		}
	}

	public int getfocuscell(){
		int cell;
		cell=mColumnCount*((int)((focusY-(extraGrid/2))/((viewsize-extraGrid)/mColumnCount)))+1+(int)(((focusX-(extraGrid/2))/((viewsize-extraGrid)/mColumnCount)));
		return cell;
	}

	public void centercell(int globalfocuscell) {
		ArrayList<Integer> newList = new ArrayList<Integer>();
		int focuscell = getfocuscell()-1;
		mStartingPosition = globalfocuscell - focuscell - (mMaxColumnCount-mColumnCount)*(int)(focuscell/mColumnCount);

		for (int j = 0; j <= mColumnCount - 1; j++) {
			for (int i = mStartingPosition + (j * mMaxColumnCount); i <= mStartingPosition
					+ (j * mMaxColumnCount) + mColumnCount - 1; i++) {
				newList.add(mGlobalColorList.get(i));
			}
		}
			mColorList.clear();
			mColorList = newList;
			updatechild(newList);
			invalidate();

	}

}

