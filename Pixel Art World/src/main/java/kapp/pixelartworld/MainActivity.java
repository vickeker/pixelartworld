package kapp.pixelartworld;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.gesture.GesturePoint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.widget.DrawerLayout;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.LikeView;
import com.facebook.share.widget.ShareDialog;
import com.github.danielnilsson9.colorpickerview.dialog.ColorPickerDialogFragment;
import com.github.danielnilsson9.colorpickerview.dialog.ColorPickerDialogFragment.ColorPickerDialogListener;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class MainActivity extends Activity implements ColorPickerDialogListener, CustomGridView.ViewWasTouchedListener, CustomGridView.ScaleChangeListener {

	private Button buttonRed = null;
	private Button buttonBlue = null;
	private Button buttonGreen = null;
	private Button buttonWhite = null;
	private Button buttonColorPanel = null;
	private Button buttonSave = null;
	private Button buttonNew = null;
	private Button buttonUndo = null;
	private Button buttonLateralMenu = null;
	private Button buttonOpen = null;
	private Button buttonSettings = null;
	private Button buttonInfo = null;
	private Button buttonDelete = null;
	private Button buttonZoomOut = null;
	private Button buttonZoomIn = null;
	private Button buttonMoveRight = null;
	private Button buttonMoveLeft = null;
	private Button buttonMoveUp = null;
	private Button buttonMoveDown = null;
	private Button buttonMoreColor = null;
	private Button buttonColorize = null;
	private ImageButton buttonPrefColor1 = null;
	private ImageButton buttonPrefColor2 = null;
	private ImageButton buttonPrefColor3 = null;
	private FrameLayout frameRed = null;
	private FrameLayout frameBlue = null;
	private FrameLayout frameGreen = null;
	private FrameLayout frameWhite = null;
	private FrameLayout frameColorPanel = null;
	private FrameLayout selectedframe = null;
	private LinearLayout color_menu = null;
	private CustomGridView customgridview1 = null;
	private Integer SelectedColor = Color.RED;
	private Integer SelectedColorId = R.id.FrameRed;
	private String DrawingToOpen;
	private ArrayList<Integer> ColorList = null;
	private LayoutInflater inflater;
	private String filename = null;
	private List<String> filenamelist;
	private int GVWIDTH;
	private int GVHEIGHT;
	private static final int DIALOG_ID = 0;
	private boolean COLORIZE_FLAG = false;
	private Integer prefColor = 1;
	private List<Integer> selectioncolorlist = null;
	private List<Integer> prefcolorlist = null;
	private String[] mMainDrawerMenu;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private LinearLayout mLinearDrawer;
	private String jpegpath;
	private ShareDialog shareDialog;
	private Button buttonplay;
	private Button buttonadd;
	private ImageView ivimage;
	private GifObject mygif;
	private Button buttondeleteimage;
	private NumberPicker np;
	private AnimationDrawable anim;
	private Drawing drawing;
	private Button gifsetting;
	private int giftimeframe=200;
	private boolean	gifloop=true;


	//For touch event test purpose
	/***private Button buttontest1;
	private Button buttontest2;
	private Button buttontest3;
	private Button buttontest4;
	private Button buttontest5;
	private Button buttontest6;
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */

	private GoogleApiClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Initialize the SDK before executing any other operations,
		FacebookSdk.sdkInitialize(getApplicationContext());
		AppEventsLogger.activateApp(this);

		setContentView(R.layout.activity_main);

		/*
		//For touch event test purpose
		buttontest1 = (Button) findViewById(R.id.test1);
		buttontest1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View vue) {

			}
		});
		buttontest2 = (Button) findViewById(R.id.test2);
		buttontest2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View vue) {

			}
		});
		buttontest3 = (Button) findViewById(R.id.test3);
		buttontest3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View vue) {
				final Instrumentation inst=new Instrumentation();
				final GesturePoint point1=new GesturePoint(100, 200,1);
				final GesturePoint point2=new GesturePoint(100, 100,1);
				final GesturePoint point3=new GesturePoint(100, 220,1);
				final GesturePoint point4=new GesturePoint(100, 320,1);
				new Thread(new Runnable() {
					@Override
					public void run() {
						generateZoomGesture(inst, System.currentTimeMillis(), true, point2, point4, point1, point3, 1000);
					}
				}).start();
			}
		});
		buttontest4 = (Button) findViewById(R.id.test4);
		buttontest4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View vue) {
			final Instrumentation inst=new Instrumentation();
				final GesturePoint point1=new GesturePoint(100, 200,1);
				final GesturePoint point2=new GesturePoint(100, 100,1);
				final GesturePoint point3=new GesturePoint(100, 220,1);
				final GesturePoint point4=new GesturePoint(100, 320,1);
				new Thread(new Runnable() {
					@Override
					public void run() {
						generateZoomGesture(inst, System.currentTimeMillis(), true, point1, point3, point2, point4, 1000);
					}
				}).start();


					}
		});
		buttontest5 = (Button) findViewById(R.id.test5);
		buttontest5.setOnClickListener(new View.OnClickListener() {
			public void onClick(View vue) {

			}
		});
		buttontest6 = (Button) findViewById(R.id.test6);
		buttontest6.setOnClickListener(new View.OnClickListener() {
			public void onClick(View vue) {

			}
		}); */

/*		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			AdView mAdView = (AdView) findViewById(R.id.adView);
			AdRequest adRequest = new AdRequest.Builder().build();
			mAdView.loadAd(adRequest);
		}*/

		mMainDrawerMenu = getResources().getStringArray(R.array.maindrawermenu);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mLinearDrawer = (LinearLayout) findViewById(R.id.left_drawer);
		LikeView likeView = (LikeView) findViewById(R.id.likeView);
		likeView.setLikeViewStyle(LikeView.Style.STANDARD);
		likeView.setAuxiliaryViewPosition(LikeView.AuxiliaryViewPosition.INLINE);
		likeView.setObjectIdAndType(
				"https://www.facebook.com/pixelartworldapp",
				LikeView.ObjectType.PAGE);

		TypedArray imgs = getResources().obtainTypedArray(R.array.maindrawermenuimage);
		String[] DrawerMenu = getApplicationContext().getResources().getStringArray(R.array.maindrawermenu);
		ArrayList<HashMap<String, Object>> drawermenulist = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> menuitem;
		for (int i = 0; i < DrawerMenu.length; i++) {
			menuitem = new HashMap<String, Object>();
			menuitem.put("image", Integer.toString(imgs.getResourceId(i, 0)));
			menuitem.put("title", DrawerMenu[i]);
			drawermenulist.add(menuitem);
		}
		imgs.recycle();


		mDrawerList = (ListView) findViewById(R.id.drawerlistview);

		// Set the adapter for the list view
		mDrawerList.setAdapter(new SimpleAdapter(this, drawermenulist,
				R.layout.drawer_list_item, new String[]{"image", "title"}, new int[]{R.id.drawermenuiv, R.id.drawermenutv}));
		// Set the list's click listener
		mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
			public void onItemClick(AdapterView parent, View view, int position, long id) {
				switch (position) {
					case 0:
						New();
						break;
					case 1:
						Open();
						break;
					case 2:
						NewGif();
						break;
					case 3:
						OpenGif();
						break;
					case 4:
						Share();
						break;
					case 5:
						Delete();
						break;
					case 6:
						Settings();
						break;}
				mDrawerList.setItemChecked(position, true);
				mDrawerLayout.closeDrawer(mLinearDrawer);
			}


		});


		color_menu = (LinearLayout) findViewById(R.id.layout_color_menu);
		ColorList = new ArrayList<Integer>();
		customgridview1 = (CustomGridView) findViewById(R.id.customgridview1);
		customgridview1.setWasTouchedListener(this);
		customgridview1.setScaleChangeListener(this);

		String[] colorsTxt = getApplicationContext().getResources().getStringArray(R.array.colors);
		selectioncolorlist = new ArrayList<Integer>();
		for (int i = 0; i < colorsTxt.length; i++) {
			int newColor = Color.parseColor(colorsTxt[i]);
			selectioncolorlist.add(newColor);
		}

		mygif=new GifObject(MainActivity.this);
		np = (NumberPicker) findViewById(R.id.numberPicker);
		updateNumberPicker();
		ivimage = (ImageView) findViewById(R.id.ivImage);

		if (savedInstanceState != null) {
			SelectedColor = savedInstanceState.getInt("selectedcolor");
			ColorList = savedInstanceState.getIntegerArrayList("colorlist");
			customgridview1.setColorList(ColorList);
			customgridview1.setSelectedColor(SelectedColor);
			SelectedColorId = savedInstanceState.getInt("selectedcolorid");
			customgridview1.setGlobalColorList(savedInstanceState.getIntegerArrayList("globallist"));
			customgridview1.setStartingPosition(savedInstanceState.getInt("startingposition"));
			customgridview1.setSavedActionList((ArrayList<HashMap<String, Integer>>) savedInstanceState.getSerializable("savedactionlist"));
			int numcol = savedInstanceState.getInt("numcol");
			customgridview1.setMaxChildren(numcol * numcol);
			customgridview1.setNumCol(numcol);
			mygif.setGiffromJsonString(savedInstanceState.getString("gif"));
			updateNumberPicker();
			np.setValue(savedInstanceState.getInt("npvalue"));
			Bitmap bitmapimage;
			bitmapimage = mygif.getImage(np.getValue());
			Drawable drawableimage=new BitmapDrawable(getResources(),bitmapimage);
			ivimage.setImageDrawable(drawableimage);
		} else {

			for (int i = 0; i <= customgridview1.getMaxChildren() - 1; i++) {
				ImageView child = new ImageView(this);
				LayoutParams layoutParams = new LayoutParams(-1, -1);
				child.setLayoutParams(layoutParams);
				child.setBackgroundColor(customgridview1.getBackgroundColor());
				customgridview1.addView(child);
			}
			ColorList = customgridview1.getColorList();
		}
		customgridview1.setSelectedColor(SelectedColor);


		buttonLateralMenu = (Button) findViewById(R.id.Morebutton);
		buttonLateralMenu.setOnClickListener(new View.OnClickListener() {
			public void onClick(View vue) {
				mDrawerLayout.openDrawer(mLinearDrawer);
				if (MainActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && color_menu.getVisibility() == View.VISIBLE) {
					color_menu.setVisibility(View.GONE);
				}
			}
		});


		buttonSave = (Button) findViewById(R.id.Save);
		buttonSave.setOnClickListener(new View.OnClickListener() {
			public void onClick(View vue) {
				AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
				final Map<String, Object> map = new HashMap<String, Object>();
				LinearLayout alertlayout = new LinearLayout(MainActivity.this);
				LayoutParams params = new LayoutParams(-1, -2);
				alertlayout.setLayoutParams(params);
				alertlayout.setOrientation(LinearLayout.VERTICAL);
				final CheckBox checkboxjpeg = new CheckBox(MainActivity.this);
				checkboxjpeg.setText(getString(R.string.jpegcheckbox));
				final EditText input = new EditText(MainActivity.this);
				input.setHint(getString(R.string.name));
				input.setInputType(InputType.TYPE_CLASS_TEXT);
				alertlayout.addView(checkboxjpeg);
				alertlayout.addView(input);
				alert.setView(alertlayout);
				alert.setTitle(getString(R.string.save));
				alert.setMessage("");
				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						filename = input.getText().toString();
						boolean fileNameExist = false;
						if (filename != "") {
							fileNameExist = customgridview1.saveList(ColorList, filename);
						}
						if (fileNameExist) {

							AlertDialog.Builder alert2 = new AlertDialog.Builder(MainActivity.this);
							alert2.setTitle(getString(R.string.nameexist));
							alert2.setMessage(getString(R.string.erasefile));
							alert2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									customgridview1.updateList(ColorList, filename);
									if (checkboxjpeg.isChecked()) {
										customgridview1.saveAsJpeg(filename);
									}
								}
							});
							alert2.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
								}
							});
							alert2.show();
						} else {
							if (checkboxjpeg.isChecked()) {
								customgridview1.saveAsJpeg(filename);
							}
						}
					}
				});
				alert.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});
				alert.show();
			}
		});


		buttonUndo = (Button) findViewById(R.id.Undo);
		buttonUndo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View vue) {
				customgridview1.undo();
				ColorList = customgridview1.getColorList();
			}
		});

		frameRed = (FrameLayout) findViewById(R.id.FrameRed);
		frameBlue = (FrameLayout) findViewById(R.id.FrameBlue);
		frameGreen = (FrameLayout) findViewById(R.id.FrameGreen);
		frameWhite = (FrameLayout) findViewById(R.id.FrameWhite);
		frameColorPanel = (FrameLayout) findViewById(R.id.FrameColorPanel);
		selectedframe = (FrameLayout) findViewById(SelectedColorId);

		if (SelectedColorId != R.id.FrameRed) {
			frameRed.setBackgroundColor(Color.WHITE);
			if (SelectedColorId == R.id.FrameColorPanel) {
				if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
					selectedframe.setBackgroundColor(SelectedColor);
				}
			} else {
				selectedframe.setBackgroundColor(Color.CYAN);
			}
		}

		buttonRed = (Button) findViewById(R.id.buttonRed);
		buttonRed.setOnClickListener(new View.OnClickListener() {
			public void onClick(View vue) {
				SelectedColor = Color.RED;
				SelectedColorId = R.id.FrameRed;
				if (MainActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
					color_menu.setVisibility(View.GONE);
					buttonColorPanel.setBackgroundResource(R.drawable.ic_color_lens);
					frameColorPanel.setBackgroundColor(Color.TRANSPARENT);
				} else {
					buttonColorize.setBackgroundResource(R.drawable.ic_colorize);
				}
				COLORIZE_FLAG = false;
				customgridview1.setcolorizeflag(false);
				customgridview1.setSelectedColor(SelectedColor);
				frameRed.setBackgroundColor(Color.CYAN);
				frameBlue.setBackgroundColor(Color.WHITE);
				frameGreen.setBackgroundColor(Color.WHITE);
				frameWhite.setBackgroundColor(Color.WHITE);

			}
		});

		buttonGreen = (Button) findViewById(R.id.buttonGreen);
		buttonGreen.setOnClickListener(new View.OnClickListener() {
			public void onClick(View vue) {
				SelectedColor = Color.GREEN;
				SelectedColorId = R.id.FrameGreen;
				if (MainActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
					color_menu.setVisibility(View.GONE);
					buttonColorPanel.setBackgroundResource(R.drawable.ic_color_lens);
					frameColorPanel.setBackgroundColor(Color.TRANSPARENT);
				} else {
					buttonColorize.setBackgroundResource(R.drawable.ic_colorize);
				}
				COLORIZE_FLAG = false;
				customgridview1.setcolorizeflag(false);
				customgridview1.setSelectedColor(SelectedColor);
				frameRed.setBackgroundColor(Color.WHITE);
				frameBlue.setBackgroundColor(Color.WHITE);
				frameGreen.setBackgroundColor(Color.CYAN);
				frameWhite.setBackgroundColor(Color.WHITE);

			}
		});

		buttonBlue = (Button) findViewById(R.id.buttonBlue);
		buttonBlue.setOnClickListener(new View.OnClickListener() {
			public void onClick(View vue) {
				SelectedColor = Color.BLUE;
				SelectedColorId = R.id.FrameBlue;
				if (MainActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
					color_menu.setVisibility(View.GONE);
					buttonColorPanel.setBackgroundResource(R.drawable.ic_color_lens);
					frameColorPanel.setBackgroundColor(Color.TRANSPARENT);
				} else {
					buttonColorize.setBackgroundResource(R.drawable.ic_colorize);
				}
				COLORIZE_FLAG = false;
				customgridview1.setcolorizeflag(false);
				customgridview1.setSelectedColor(SelectedColor);
				frameRed.setBackgroundColor(Color.WHITE);
				frameBlue.setBackgroundColor(Color.CYAN);
				frameGreen.setBackgroundColor(Color.WHITE);
				frameWhite.setBackgroundColor(Color.WHITE);

			}
		});


		buttonWhite = (Button) findViewById(R.id.buttonWhite);
		buttonWhite.setOnClickListener(new View.OnClickListener() {
			public void onClick(View vue) {
				SelectedColor = Color.WHITE;
				SelectedColorId = R.id.FrameWhite;
				if (MainActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
					color_menu.setVisibility(View.GONE);
					buttonColorPanel.setBackgroundResource(R.drawable.ic_color_lens);
					frameColorPanel.setBackgroundColor(Color.TRANSPARENT);
				} else {
					buttonColorize.setBackgroundResource(R.drawable.ic_colorize);
				}
				COLORIZE_FLAG = false;
				customgridview1.setcolorizeflag(false);
				customgridview1.setSelectedColor(SelectedColor);
				frameRed.setBackgroundColor(Color.WHITE);
				frameGreen.setBackgroundColor(Color.WHITE);
				frameWhite.setBackgroundColor(Color.CYAN);
				frameBlue.setBackgroundColor(Color.WHITE);
			}
		});

		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			buttonColorPanel = (Button) findViewById(R.id.buttonColorPanel);
			buttonColorPanel.setOnClickListener(new View.OnClickListener() {
				public void onClick(View vue) {
					buttonColorPanel.setBackgroundResource(R.drawable.ic_color_lens);
					COLORIZE_FLAG = false;
					customgridview1.setcolorizeflag(false);
					if (color_menu.getVisibility() == View.GONE) {
						color_menu.setVisibility(View.VISIBLE);
					} else {
						color_menu.setVisibility(View.GONE);
					}

				}
			});
		}


		buttonZoomOut = (Button) findViewById(R.id.Zoomout);
		buttonZoomOut.setOnClickListener(new View.OnClickListener() {
			public void onClick(View vue) {
				ColorList = customgridview1.zoomout(ColorList);

			}
		});

		buttonZoomIn = (Button) findViewById(R.id.Zoomin);
		buttonZoomIn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View vue) {
				ColorList = customgridview1.zoomin(ColorList);

			}
		});

		buttonMoveRight = (Button) findViewById(R.id.Moveright);
		buttonMoveRight.setOnClickListener(new View.OnClickListener() {
			public void onClick(View vue) {
				ColorList = customgridview1.moveright(ColorList);

			}
		});

		buttonMoveLeft = (Button) findViewById(R.id.Moveleft);
		buttonMoveLeft.setOnClickListener(new View.OnClickListener() {
			public void onClick(View vue) {
				ColorList = customgridview1.moveleft(ColorList);

			}
		});

		buttonMoveUp = (Button) findViewById(R.id.Moveup);
		buttonMoveUp.setOnClickListener(new View.OnClickListener() {
			public void onClick(View vue) {
				ColorList = customgridview1.moveup(ColorList);

			}
		});

		buttonMoveDown = (Button) findViewById(R.id.Movedown);
		buttonMoveDown.setOnClickListener(new View.OnClickListener() {
			public void onClick(View vue) {
				ColorList = customgridview1.movedown(ColorList);

			}
		});

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		GVWIDTH = dm.widthPixels;
		GVHEIGHT = dm.heightPixels;

		buttonMoreColor = (Button) findViewById(R.id.morecolor);
		buttonMoreColor.setOnClickListener(new View.OnClickListener() {
			public void onClick(View vue) {
				onClickColorPickerDialog();
				if (MainActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
					color_menu.setVisibility(View.GONE);
				} else {
					buttonColorize.setBackgroundResource(R.drawable.ic_colorize);
				}
			}
		});

		buttonColorize = (Button) findViewById(R.id.colorize);
		buttonColorize.setOnClickListener(new View.OnClickListener() {
			public void onClick(View vue) {
				COLORIZE_FLAG = true;
				customgridview1.setcolorizeflag(COLORIZE_FLAG);
				if (MainActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
					color_menu.setVisibility(View.GONE);
					buttonColorPanel.setBackgroundResource(R.drawable.ic_colorize);
				} else {
					buttonColorize.setBackgroundResource(R.drawable.ic_colorize_selected);
				}
			}
		});


		buttonPrefColor1 = (ImageButton) findViewById(R.id.prefcolor1);
		buttonPrefColor2 = (ImageButton) findViewById(R.id.prefcolor2);
		buttonPrefColor3 = (ImageButton) findViewById(R.id.prefcolor3);

		buttonplay = (Button) findViewById(R.id.play);
		buttonplay.setOnClickListener(new View.OnClickListener() {
			public void onClick(View vue) {
				if(mygif.getSize()>1) {
					AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

					builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//Exit dialog
						}
					}).setNegativeButton("Save", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//Save GIF Animation
							AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
							final Map<String, Object> map = new HashMap<String, Object>();
							LinearLayout alertlayout = new LinearLayout(MainActivity.this);
							LayoutParams params = new LayoutParams(-1, -2);
							alertlayout.setLayoutParams(params);
							alertlayout.setOrientation(LinearLayout.VERTICAL);
							final EditText input = new EditText(MainActivity.this);
							input.setHint(getString(R.string.name));
							input.setInputType(InputType.TYPE_CLASS_TEXT);
							alertlayout.addView(input);
							alert.setView(alertlayout);
							alert.setTitle(getString(R.string.save));
							alert.setMessage("");
							alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									filename = input.getText().toString();
									if (filename != "") {
										boolean filenameexist = false;
										try {
											filenameexist = mygif.NameExist(filename);
										} catch (ClassNotFoundException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										if (filenameexist) {
											AlertDialog.Builder alert2 = new AlertDialog.Builder(MainActivity.this);
											alert2.setTitle(getString(R.string.nameexist));
											alert2.setMessage(getString(R.string.erasefile));
											alert2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog, int whichButton) {
													//save gif drawing
													try {
														mygif.savedrawinglist(filename);
														mygif.generateGIF(filename);
													} catch (ClassNotFoundException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
														CharSequence text = "Error, couldnt save the GIF";
														int duration = Toast.LENGTH_LONG;
														Toast toast = Toast.makeText(MainActivity.this, text, duration);
														toast.show();
													}
												}
											});
											alert2.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog, int whichButton) {
												}
											});
											alert2.show();
										} else {
											//save gif drawing
											try {
												mygif.saveGifName(filename);
												mygif.savedrawinglist(filename);
												mygif.generateGIF(filename);
											} catch (ClassNotFoundException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
												CharSequence text = "Error, couldnt save the GIF";
												int duration = Toast.LENGTH_LONG;
												Toast toast = Toast.makeText(MainActivity.this, text, duration);
												toast.show();
											}
										}

									}
								}
							});
							alert.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
								}
							});
							alert.show();
						}
					});

					final AlertDialog dialog = builder.create();
					anim = new AnimationDrawable();
					LayoutInflater inflater = getLayoutInflater();
					View dialogLayout = inflater.inflate(R.layout.gifplaydialog, null);
					dialog.setView(dialogLayout);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					ImageView gifplayview = (ImageView) dialogLayout.findViewById(R.id.gifplayview);
					anim = mygif.getAnimation(gifplayview.getLayoutParams().width, gifplayview.getLayoutParams().height);
					gifplayview.setBackgroundDrawable(anim);
					final Handler startAnimation = new Handler() {
						public void handleMessage(Message msg) {
							super.handleMessage(msg);
							anim.start();
						}
					};

					dialog.setOnShowListener(new DialogInterface.OnShowListener() {
						@Override
						public void onShow(DialogInterface dialog) {
							Message msg = new Message();
							startAnimation.sendMessage(msg);
						}
					});

					dialog.show();
				}
			}
		});

		buttonadd = (Button) findViewById(R.id.add);
		buttonadd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View vue) {
				Bitmap bitmapimage=customgridview1.getCacheDrawing();
				mygif.addImage(np.getValue()+1,bitmapimage);
				drawing = new Drawing(customgridview1.getNumCol(),ColorList);
 				drawing.setStartposition(customgridview1.getStartingPosition());
				mygif.addDrawing(np.getValue(),drawing);
				updateNumberPicker();
				np.setValue(np.getValue()+1);
				Drawable drawableimage=new BitmapDrawable(getResources(),bitmapimage);
				ivimage.setImageDrawable(drawableimage);
			}
		});


		buttondeleteimage = (Button) findViewById(R.id.delete);
		buttondeleteimage.setOnClickListener(new View.OnClickListener() {
			public void onClick(View vue) {
				if(np.getValue()>=1) {
					mygif.deleteImage(np.getValue());
					Bitmap bitmapimage;
					if (np.getValue() < mygif.getSize()) {
						bitmapimage = mygif.getImage(np.getValue());
					} else {
						bitmapimage = mygif.getImage(np.getValue()-1);
					}
					updateNumberPicker();
					Drawable drawableimage = new BitmapDrawable(getResources(), bitmapimage);
					ivimage.setImageDrawable(drawableimage);
				}
				}
		});

		np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal){
				Bitmap bitmapimage;
					bitmapimage = mygif.getImage(newVal);
				Drawable drawableimage=new BitmapDrawable(getResources(),bitmapimage);
				ivimage.setImageDrawable(drawableimage);

			}
		});

		ivimage.setOnClickListener(new View.OnClickListener() {
			public void onClick(View vue) {
				if(np.getValue()==0) {
					Bitmap bitmapimage = customgridview1.getCacheDrawing();
					mygif.addImage(1,bitmapimage);
					updateNumberPicker();
					np.setValue(1);
					Drawable drawableimage = new BitmapDrawable(getResources(), bitmapimage);
					ivimage.setImageDrawable(drawableimage);
					drawing = new Drawing(customgridview1.getNumCol(),ColorList);
					drawing.setStartposition(customgridview1.getStartingPosition());
					mygif.addDrawing(0,drawing);
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

					builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							ColorList=customgridview1.openDrawing(mygif.getDrawing(np.getValue()-1));
						}
					}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});

					builder.setTitle("Edit drawing?");
					builder.setMessage("Note: The current drawing will be lost if not already saved or added to the animation");
					builder.show();
				}
			}
		});

		gifsetting = (Button) findViewById(R.id.gifsettings);
		gifsetting.setOnClickListener(new View.OnClickListener() {
			public void onClick(View vue) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				LayoutInflater inflater = getLayoutInflater();
				View convertView = (View) inflater.inflate(R.layout.gifsettingalert, null);
				builder.setView(convertView);
				TextView tv_timeframe = (TextView) convertView.findViewById(R.id.tv_timeframe);
				final EditText et_timeframe  = (EditText) convertView.findViewById(R.id.et_timeframe);
				final CheckBox cb_loop = (CheckBox) convertView.findViewById(R.id.cb_loop);
				cb_loop.setChecked(gifloop);
				et_timeframe.setText(String.valueOf(giftimeframe));
				builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					gifloop=cb_loop.isChecked();
						mygif.setLoop(gifloop);
					if(Integer.parseInt(et_timeframe.getText().toString())!=0) {
						giftimeframe = Integer.parseInt(et_timeframe.getText().toString());
					mygif.setTimeFrame(giftimeframe);
					}
					}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});

				builder.setTitle("GIF Settings");
				builder.show();
			}
		});



		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();




	}

	public void updateNumberPicker() {
  		if (mygif.getSize() == 0) {
/*			String[] nums = new String[1];
			nums[0] = "0";*/
			np.setMinValue(0);
			np.setMaxValue(0);
			np.setWrapSelectorWheel(false);
		//	np.setDisplayedValues(nums);
		} else {
/*			String[] nums = new String[mygif.getSize()];
			for (int i = 0; i < nums.length; i++)
				nums[i] = Integer.toString(i);*/
			np.setMinValue(0);
			np.setMaxValue(mygif.getSize()-1);
			np.setWrapSelectorWheel(false);
		//	np.setDisplayedValues(nums);
		}
	}

	public void Open() {
		final ArrayList<HashMap<String, Object>> filenamelist = new ArrayList<HashMap<String, Object>>();
		ArrayList<String> list = new ArrayList<String>();
		list = customgridview1.readDrawingName();
		if (list != null) {
			for (int i = 0; i <= list.size() - 1; i++) {
				HashMap<String, Object> h = new HashMap<String, Object>();
				h.put("name", list.get(i));
				filenamelist.add(h);
			}
		} else {
			CharSequence text = getString(R.string.nosaveddrawing);
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(MainActivity.this, text, duration);
			toast.show();
		}
		if (filenamelist.size() != 0) {
			final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
			LayoutInflater inflater = getLayoutInflater();
			View convertView = (View) inflater.inflate(R.layout.filenamelist, null);
			alertDialog.setView(convertView);
			ListView lv = (ListView) convertView.findViewById(R.id.namelistview);

			final SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, filenamelist, R.layout.file, new String[]{"name"}, new int[]{R.id.file_name}) {
				@Override
				public View getView(int position, View convertView,
									ViewGroup parent) {
					TextView textView;
					if (convertView == null) {
						LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(MainActivity.this);
						convertView = (View) inflater.inflate(R.layout.file, null);
						textView = (TextView) convertView
								.findViewById(R.id.file_name);
					} else {
						textView = (TextView) convertView
								.findViewById(R.id.file_name);
					}
					textView.setText(filenamelist.get(position).get("name").toString());

					return convertView;
				}
			};


			lv.setAdapter(adapter);
			alertDialog.setTitle(R.string.open);
			final AlertDialog alert = alertDialog.show();

			lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View vue,
										int position, long id) {
					DrawingToOpen = filenamelist.get(position).get("name").toString();
					ArrayList<Integer> mResult = new ArrayList<Integer>();
					mResult = customgridview1.readList(DrawingToOpen);
					if (mResult != null) {
						ColorList = mResult;
					} else {
						CharSequence text = getString(R.string.cantfind) + " " + DrawingToOpen;
						int duration = Toast.LENGTH_LONG;
						Toast toast = Toast.makeText(MainActivity.this, text, duration);
						toast.show();
					}
					alert.dismiss();
				}
			});
			alertDialog.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				}
			});
		}
	}


	public void Settings() {
		final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
		alertDialog.setTitle(getString(R.string.app_name) + " " + getString(R.string.settings_title));
		alertDialog.setMessage(R.string.settings);
		alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
			}
		});
		alertDialog.show();
	}


	public void Info() {
		final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
		alertDialog.setTitle(getString(R.string.app_name) + " Info");
		alertDialog.setMessage(getString(R.string.info));
		alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
			}
		});
		alertDialog.show();
	}

	public void Share() {
		mDrawerLayout.closeDrawer(mLinearDrawer);
		final ArrayList<HashMap<String, Object>> filelist = new ArrayList<HashMap<String, Object>>(customgridview1.getSavedJpeg());
		filelist.addAll(mygif.getSavedGif());
		if (filelist.size() != 0) {
			final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
			LayoutInflater inflater = getLayoutInflater();
			View convertView = (View) inflater.inflate(R.layout.gridviewdialog, null);
			alertDialog.setView(convertView);
			GridView lv = (GridView) convertView.findViewById(R.id.gridview);

			final SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, filelist, R.layout.jpegivlayout, new String[]{"image"}, new int[]{R.id.jpegiv}) {
				@Override
				public View getView(int position, View convertView,
									ViewGroup parent) {
					ImageView imageview;
					if (convertView == null) {
						LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(MainActivity.this);
						convertView = (View) inflater.inflate(R.layout.jpegivlayout, null);
						imageview = (ImageView) convertView
								.findViewById(R.id.jpegiv);
					} else {
						imageview = (ImageView) convertView
								.findViewById(R.id.jpegiv);
					}
					imageview.setImageDrawable((Drawable) filelist.get(position).get("image"));

				/*Boolean a = false;
				for (String c : selectedFileList) {
					if (c.equals(textView.getText().toString())) {
						convertView.setBackgroundColor(Color.LTGRAY);
						a = true;
					}
					if (!a) {
						convertView.setBackgroundColor(0);
					}
				}*/
					return convertView;
				}
			};

			lv.setAdapter(adapter);
			alertDialog.setTitle(R.string.imageselection);
			final AlertDialog alert = alertDialog.show();

			lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View vue,
										int position, long id) {
					jpegpath = filelist.get(position).get("path").toString();
					alert.dismiss();

					if (jpegpath != null) {

					final AlertDialog.Builder chooserDialog = new AlertDialog.Builder(MainActivity.this);
					LayoutInflater inflater1 = getLayoutInflater();
					View convertView1 = (View) inflater1.inflate(R.layout.gridviewdialog, null);
					chooserDialog.setView(convertView1);
					GridView lv1 = (GridView) convertView1.findViewById(R.id.gridview);

					TypedArray imgs = getResources().obtainTypedArray(R.array.apptoshare);
					final ArrayList<HashMap<String, Object>> applist = new ArrayList<HashMap<String, Object>>();
					HashMap<String, Object> appitem;
					for (int i = 0; i < imgs.length(); i++) {
						appitem = new HashMap<String, Object>();
						appitem.put("image", imgs.getResourceId(i, 0));
						applist.add(appitem);
					}
					imgs.recycle();

					final SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, applist, R.layout.jpegivlayout, new String[]{"image"}, new int[]{R.id.jpegiv}) {
						@Override
						public View getView(int position, View convertView,
											ViewGroup parent) {
							ImageView imageview;
							if (convertView == null) {
								LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(MainActivity.this);
								convertView = (View) inflater.inflate(R.layout.jpegivlayout, null);
								imageview = (ImageView) convertView
										.findViewById(R.id.jpegiv);
							} else {
								imageview = (ImageView) convertView
										.findViewById(R.id.jpegiv);
							}

							imageview.setImageDrawable((Drawable) getResources().getDrawable((Integer)applist.get(position).get("image")));
							return convertView;
						}
					};

					lv1.setAdapter(adapter);
					chooserDialog.setTitle(R.string.serviceselection);
					final AlertDialog alert = chooserDialog.show();

					lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View vue,
												int position, long id) {
							// what to do depending on the app choosen
							Intent sharingIntent = new Intent(Intent.ACTION_SEND);
							if(jpegpath.substring(jpegpath.length() - 4).equals(".gif")){
								sharingIntent.setType("image/gif");
							} else {
								sharingIntent.setType("image/jpeg");
							}
								Uri screenshotUri = Uri.parse("file://"+jpegpath);
							PackageManager pm=getPackageManager();


							List<ResolveInfo> resInfos=MainActivity.this.getPackageManager().queryIntentActivities(sharingIntent, 0);

							// what to do depending on the app choosen
							switch (position) {
								//facebook
								case 0:


									BitmapFactory.Options bmOptions = new BitmapFactory.Options();
									Bitmap image =  BitmapFactory.decodeFile(jpegpath,bmOptions);

									shareDialog = new ShareDialog(MainActivity.this);

									if (ShareDialog.canShow(SharePhotoContent.class)) {
										SharePhoto photo = new SharePhoto.Builder()
												.setBitmap(image)
												.build();
										SharePhotoContent content = new SharePhotoContent.Builder()
												.addPhoto(photo)
												.build();

										shareDialog.show(content);
									}
									break;
								//other apps

								case 1:
									try {

										Intent waIntent = new Intent(Intent.ACTION_SEND);
										if(jpegpath.substring(jpegpath.length() - 4).equals(".gif")){
											waIntent.setType("image/gif");
										} else {
											waIntent.setType("image/jpeg");
										}
										PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);

										waIntent.setPackage("com.whatsapp");

										waIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
										startActivity(Intent.createChooser(waIntent, "Share with"));

									} catch (PackageManager.NameNotFoundException e) {
										Toast.makeText(MainActivity.this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
												.show();
									}
									break;


								case 2:

									List<Intent> targetedShareIntents=new ArrayList<Intent>();


									if(!resInfos.isEmpty()){
										for(ResolveInfo resInfo : resInfos){
											String packageName=resInfo.activityInfo.packageName;
											Intent targetedShareIntent = new Intent(android.content.Intent.ACTION_SEND);
											if(jpegpath.substring(jpegpath.length() - 4).equals(".gif")){
												targetedShareIntent.setType("image/gif");
											} else {
												targetedShareIntent.setType("image/jpeg");
											}
											targetedShareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Pixel Art World drawing");


											targetedShareIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);

											targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Draw with Pixel Art World");

											targetedShareIntent.setPackage(packageName);
											targetedShareIntents.add(targetedShareIntent);

										}

										if(!targetedShareIntents.isEmpty()){
											Intent chooserIntent=Intent.createChooser(targetedShareIntents.remove(0), "Share via");
											chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[targetedShareIntents.size()]));
											MainActivity.this.startActivity(chooserIntent);
										}else{
										}
									}
									break;
							}

							alert.dismiss();


						}
					});









					} else {
						CharSequence text = getString(R.string.nosaveddrawing);
						int duration = Toast.LENGTH_LONG;
						Toast toast = Toast.makeText(MainActivity.this, text, duration);
						toast.show();
					}
				}
			});
			alertDialog.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				}
			});

		}
	}

	public void New() {
		if (MainActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			buttonColorPanel.setBackgroundResource(R.drawable.ic_color_lens);
		} else {
			buttonColorize.setBackgroundResource(R.drawable.ic_colorize);
		}
		COLORIZE_FLAG = false;
		customgridview1.setcolorizeflag(false);
		AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
		alert.setTitle(getString(R.string.newdrawing));
		alert.setMessage(getString(R.string.confirmnewdrawing));
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				ColorList = customgridview1.cleargrid();
			}
		});
		alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		alert.show();

	}

	public void NewGif(){
		AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
		alert.setTitle("New GIF");
		alert.setMessage("Start a new GIF");
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				mygif.GifClear();
				updateNumberPicker();
				ivimage.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));
			}
		});
		alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		alert.show();
	}

	public void OpenGif(){
		final ArrayList<HashMap<String, Object>> filenamelist = new ArrayList<HashMap<String, Object>>();
		ArrayList<String> list = new ArrayList<String>();
		list = mygif.readGifName();
		if (list != null) {
			for (int i = 0; i <= list.size() - 1; i++) {
				HashMap<String, Object> h = new HashMap<String, Object>();
				h.put("name", list.get(i));
				filenamelist.add(h);
			}
		} else {
			CharSequence text ="No saved GIF";
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(MainActivity.this, text, duration);
			toast.show();
		}
		if (filenamelist.size() != 0) {
			final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
			LayoutInflater inflater = getLayoutInflater();
			View convertView = (View) inflater.inflate(R.layout.filenamelist, null);
			alertDialog.setView(convertView);
			ListView lv = (ListView) convertView.findViewById(R.id.namelistview);

			final SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, filenamelist, R.layout.file, new String[]{"name"}, new int[]{R.id.file_name}) {
				@Override
				public View getView(int position, View convertView,
									ViewGroup parent) {
					TextView textView;
					if (convertView == null) {
						LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(MainActivity.this);
						convertView = (View) inflater.inflate(R.layout.file, null);
						textView = (TextView) convertView
								.findViewById(R.id.file_name);
					} else {
						textView = (TextView) convertView
								.findViewById(R.id.file_name);
					}
					textView.setText(filenamelist.get(position).get("name").toString());

					return convertView;
				}
			};


			lv.setAdapter(adapter);
			alertDialog.setTitle("Open a GIF");
			final AlertDialog alert = alertDialog.show();

			lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View vue,
										int position, long id) {
					DrawingToOpen = filenamelist.get(position).get("name").toString();
					mygif.readDrawingList(DrawingToOpen);
					if (mygif.getSize()!=0 && mygif.DrawingList.size()!=0) {
					ArrayList<Integer> mResult = new ArrayList<Integer>();
					mResult=customgridview1.openDrawing(mygif.getDrawing(0));
						ColorList = mResult;
						updateNumberPicker();
						np.setValue(1);
						Bitmap bitmapimage;
						bitmapimage = mygif.getImage(1);
						Drawable drawableimage=new BitmapDrawable(getResources(),bitmapimage);
						ivimage.setImageDrawable(drawableimage);
					} else {
						CharSequence text = getString(R.string.cantfind) + " " + DrawingToOpen;
						int duration = Toast.LENGTH_LONG;
						Toast toast = Toast.makeText(MainActivity.this, text, duration);
						toast.show();
					}
					alert.dismiss();
				}
			});
			alertDialog.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				}
			});
		}
	}

	public void Delete() {
		final ArrayList<String> selectedFileList = new ArrayList<String>();
		final ArrayList<HashMap<String, Object>> filenamelist = new ArrayList<HashMap<String, Object>>();
		ArrayList<String> list = new ArrayList<String>();
		list = customgridview1.readDrawingName();
		ArrayList<String> listgif=new ArrayList<String>();
		listgif=mygif.readGifName();
		for(int i =0;i<=mygif.readGifName().size()-1;i++){

			list.add(listgif.get(i)+".gif");
		}
		if (list != null) {
			for (int i = 0; i <= list.size() - 1; i++) {
				HashMap<String, Object> h = new HashMap<String, Object>();
				h.put("name", list.get(i));
				filenamelist.add(h);
			}
		} else {
			CharSequence text = getString(R.string.nosaveddrawing);
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(MainActivity.this, text, duration);
			toast.show();
		}

		if (filenamelist.size() != 0) {
			final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
			LayoutInflater inflater = getLayoutInflater();
			View view = (View) inflater.inflate(R.layout.filenamelist, null);
			alertDialog.setView(view);
			ListView lv = (ListView) view.findViewById(R.id.namelistview);
			final SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, filenamelist, R.layout.file, new String[]{"name"}, new int[]{R.id.file_name}) {
				@Override
				public View getView(int position, View convertView,
									ViewGroup parent) {
					TextView textView;
					if (convertView == null) {
						LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(MainActivity.this);
						convertView = (View) inflater.inflate(R.layout.file, null);
						textView = (TextView) convertView
								.findViewById(R.id.file_name);
					} else {
						textView = (TextView) convertView
								.findViewById(R.id.file_name);
					}
					textView.setText(filenamelist.get(position).get("name").toString());
					Boolean a = false;
					for (String c : selectedFileList) {
						if (c.equals(textView.getText().toString())) {
							convertView.setBackgroundColor(Color.LTGRAY);
							a = true;
						}
						if (!a) {
							convertView.setBackgroundColor(0);
						}
					}
					return convertView;
				}
			};

			lv.setAdapter(adapter);
			alertDialog.setTitle(R.string.delete);

			lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View vue,
										int position, long id) {
					final HashMap item = (HashMap) parent
							.getItemAtPosition(position);
					if (!selectedFileList.contains(item.get("name").toString())) {
						selectedFileList.add(item.get("name").toString());
					} else {
						selectedFileList.remove(item.get("name").toString());
					}
					adapter.notifyDataSetChanged();
				}

			});

			alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					if (selectedFileList.size() != 0) {
						for (int i = 0; i <= selectedFileList.size() - 1; i++) {
						String str=selectedFileList.get(i);
							if (str.length() > 4) {
									if(str.substring(str.length() - 4).equals(".gif")){
										str.replace(".gif", "");
										mygif.deletefile(str);
									} else {
										customgridview1.deletefile(str);
									}
							} else {
								customgridview1.deletefile(str);
							}

						}
					}
				}
			});
			alertDialog.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				}
			});
			alertDialog.show();
		}
	}



	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt("selectedcolor", SelectedColor);
		outState.putIntegerArrayList("colorlist", ColorList);
		outState.putIntegerArrayList("globallist", customgridview1.getGlobalColorList());
		outState.putInt("startingposition", customgridview1.getStartingPosition());
		outState.putInt("numcol", customgridview1.getNumCol());
		outState.putSerializable("savedactionlist", (Serializable) customgridview1.getSavedActionList());
		outState.putInt("selectedcolorid", SelectedColorId);
		String gifstring=mygif.getJsonString();
		outState.putString("gif",gifstring);
		outState.putInt("npvalue",np.getValue());
		super.onSaveInstanceState(outState);
	}

	public void onColorClick(View v) {
		ColorDrawable drawable = (ColorDrawable) v.getBackground();
		SelectedColor = drawable.getColor();
		SelectedColorId = R.id.FrameColorPanel;
		customgridview1.setSelectedColor(SelectedColor);
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			color_menu.setVisibility(View.GONE);
			frameColorPanel.setBackgroundColor(SelectedColor);
		} else {
			buttonColorize.setBackgroundResource(R.drawable.ic_colorize);
		}
		COLORIZE_FLAG = false;
		customgridview1.setcolorizeflag(false);
		frameRed.setBackgroundColor(Color.WHITE);
		frameBlue.setBackgroundColor(Color.WHITE);
		frameGreen.setBackgroundColor(Color.WHITE);
		frameWhite.setBackgroundColor(Color.WHITE);
	}

	public void onClickColorPickerDialog() {

		// The color picker menu item has been clicked. Show
		// a dialog using the custom ColorPickerDialogFragment class.

		ColorPickerDialogFragment f = ColorPickerDialogFragment
				.newInstance(DIALOG_ID, null, null, Color.BLACK, true);

		f.setStyle(DialogFragment.STYLE_NORMAL, R.style.LightPickerDialogTheme);
		f.show(getFragmentManager(), "d");

	}

	@Override
	public void onDialogDismissed(int dialogId) {

	}


	@Override
	public void onColorSelected(int dialogId, int color) {
		switch (dialogId) {

			case DIALOG_ID:
				SelectedColor = color;
				SelectedColorId = R.id.FrameColorPanel;
				customgridview1.setSelectedColor(SelectedColor);
				if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
					frameColorPanel.setBackgroundColor(SelectedColor);
				}
				COLORIZE_FLAG = false;
				customgridview1.setcolorizeflag(false);
				frameRed.setBackgroundColor(Color.WHITE);
				frameBlue.setBackgroundColor(Color.WHITE);
				frameGreen.setBackgroundColor(Color.WHITE);
				frameWhite.setBackgroundColor(Color.WHITE);

				if (prefcolorlist == null) {
					prefcolorlist = new ArrayList<Integer>();
				}

				if (!selectioncolorlist.contains(color) && !prefcolorlist.contains(color)) {
					if (prefcolorlist.size() >= prefColor) {
						prefcolorlist.set(prefColor - 1, color);
					} else {
						prefcolorlist.add(color);
					}
					switch (prefColor) {
						case 1:
							buttonPrefColor1.setBackgroundColor(color);
							prefColor = 2;
							break;

						case 2:
							buttonPrefColor2.setBackgroundColor(color);
							prefColor = 3;
							break;

						case 3:
							buttonPrefColor3.setBackgroundColor(color);
							prefColor = 1;
							break;
					}
					break;
				}
		}


	}


	private static String colorToHexString(int color) {
		return String.format("#%06X", 0xFFFFFFFF & color);
	}

	public void onViewTouched(int selectedcolor) {
		if (COLORIZE_FLAG) {
			SelectedColor = selectedcolor;
			SelectedColorId = R.id.FrameColorPanel;
			if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				frameColorPanel.setBackgroundColor(SelectedColor);
			}
			frameRed.setBackgroundColor(Color.WHITE);
			frameBlue.setBackgroundColor(Color.WHITE);
			frameGreen.setBackgroundColor(Color.WHITE);
			frameWhite.setBackgroundColor(Color.WHITE);

			if (prefcolorlist == null) {
				prefcolorlist = new ArrayList<Integer>();
			}

			if (!selectioncolorlist.contains(SelectedColor) && !prefcolorlist.contains(selectedcolor)) {
				if (prefcolorlist.size() >= prefColor) {
					prefcolorlist.set(prefColor - 1, selectedcolor);
				} else {
					prefcolorlist.add(selectedcolor);
				}
				switch (prefColor) {
					case 1:
						buttonPrefColor1.setBackgroundColor(selectedcolor);
						prefColor = 2;
						break;

					case 2:
						buttonPrefColor2.setBackgroundColor(selectedcolor);
						prefColor = 3;
						break;

					case 3:
						buttonPrefColor3.setBackgroundColor(selectedcolor);
						prefColor = 1;
						break;
				}
			}
			if (MainActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				buttonColorPanel.setBackgroundResource(R.drawable.ic_color_lens);
			} else {
				buttonColorize.setBackgroundResource(R.drawable.ic_colorize);
			}
			COLORIZE_FLAG = false;
		}
	}

	public void onScaleChange(ArrayList<Integer> colorlist) {
		ColorList = colorlist;
	}


	//For touche event test purpose
	public static void generateZoomGesture(Instrumentation inst,
										   long startTime, boolean ifMove, GesturePoint startPoint1,
										   GesturePoint startPoint2, GesturePoint endPoint1,
										   GesturePoint endPoint2, int duration) {

		if (inst == null || startPoint1 == null
				|| (ifMove && endPoint1 == null)) {
			return;
		}

		long eventTime = startTime;
		long downTime = startTime;
		MotionEvent event;
		float eventX1, eventY1, eventX2, eventY2;

		eventX1 = startPoint1.x;
		eventY1 = startPoint1.y;
		eventX2 = startPoint2.x;
		eventY2 = startPoint2.y;

		// specify the property for the two touch points
		MotionEvent.PointerProperties[] properties = new MotionEvent.PointerProperties[2];
		MotionEvent.PointerProperties pp1 = new MotionEvent.PointerProperties();
		pp1.id = 0;
		pp1.toolType = MotionEvent.TOOL_TYPE_FINGER;
		MotionEvent.PointerProperties pp2 = new MotionEvent.PointerProperties();
		pp2.id = 1;
		pp2.toolType = MotionEvent.TOOL_TYPE_FINGER;

		properties[0] = pp1;
		properties[1] = pp2;

		//specify the coordinations of the two touch points
		//NOTE: you MUST set the pressure and size value, or it doesn't work
		MotionEvent.PointerCoords[] pointerCoords = new MotionEvent.PointerCoords[2];
		MotionEvent.PointerCoords pc1 = new MotionEvent.PointerCoords();
		pc1.x = eventX1;
		pc1.y = eventY1;
		pc1.pressure = 1;
		pc1.size = 1;
		MotionEvent.PointerCoords pc2 = new MotionEvent.PointerCoords();
		pc2.x = eventX2;
		pc2.y = eventY2;
		pc2.pressure = 1;
		pc2.size = 1;
		pointerCoords[0] = pc1;
		pointerCoords[1] = pc2;

		//////////////////////////////////////////////////////////////
		// events sequence of zoom gesture
		// 1. send ACTION_DOWN event of one start point
		// 2. send ACTION_POINTER_2_DOWN of two start points
		// 3. send ACTION_MOVE of two middle points
		// 4. repeat step 3 with updated middle points (x,y),
		//      until reach the end points
		// 5. send ACTION_POINTER_2_UP of two end points
		// 6. send ACTION_UP of one end point
		//////////////////////////////////////////////////////////////

		// step 1
		event = MotionEvent.obtain(downTime, eventTime,
				MotionEvent.ACTION_DOWN, 1, properties,
				pointerCoords, 0, 0, 1, 1, 0, 0, 0, 0);

		inst.sendPointerSync(event);

		//step 2
		event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_POINTER_DOWN + (pp2.id << MotionEvent.ACTION_POINTER_INDEX_SHIFT), 2, properties, pointerCoords, 0, 0, 1, 1, 0, 0, 0, 0);

		inst.sendPointerSync(event);

		//step 3, 4
		if (ifMove) {
			int moveEventNumber = 1;
			moveEventNumber = duration / 1000;

			float stepX1, stepY1, stepX2, stepY2;

			stepX1 = (endPoint1.x - startPoint1.x) / moveEventNumber;
			stepY1 = (endPoint1.y - startPoint1.y) / moveEventNumber;
			stepX2 = (endPoint2.x - startPoint2.x) / moveEventNumber;
			stepY2 = (endPoint2.y - startPoint2.y) / moveEventNumber;

			for (int i = 0; i < moveEventNumber; i++) {
				// update the move events
				eventTime += 1000;
				eventX1 += stepX1;
				eventY1 += stepY1;
				eventX2 += stepX2;
				eventY2 += stepY2;

				pc1.x = eventX1;
				pc1.y = eventY1;
				pc2.x = eventX2;
				pc2.y = eventY2;

				pointerCoords[0] = pc1;
				pointerCoords[1] = pc2;

				event = MotionEvent.obtain(downTime, eventTime,
						MotionEvent.ACTION_MOVE, 2, properties,
						pointerCoords, 0, 0, 1, 1, 0, 0, 0, 0);

				inst.sendPointerSync(event);
			}
		}

		//step 5
		pc1.x = endPoint1.x;
		pc1.y = endPoint1.y;
		pc2.x = endPoint2.x;
		pc2.y = endPoint2.y;
		pointerCoords[0] = pc1;
		pointerCoords[1] = pc2;

		eventTime += 1000;
		event = MotionEvent.obtain(downTime, eventTime,
				MotionEvent.ACTION_POINTER_UP + (pp2.id << MotionEvent.ACTION_POINTER_INDEX_SHIFT), 2, properties,
				pointerCoords, 0, 0, 1, 1, 0, 0, 0, 0);
		inst.sendPointerSync(event);

		// step 6
		eventTime += 1000;
		event = MotionEvent.obtain(downTime, eventTime,
				MotionEvent.ACTION_UP, 1, properties,
				pointerCoords, 0, 0, 1, 1, 0, 0, 0, 0);
		inst.sendPointerSync(event);
	}


	@Override
	public void onStart() {
		super.onStart();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.connect();
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"Main Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app URL is correct.
				Uri.parse("android-app://kapp.pixelartworld/http/host/path")
		);
		AppIndex.AppIndexApi.start(client, viewAction);
	}

	@Override
	public void onStop() {
		super.onStop();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"Main Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app URL is correct.
				Uri.parse("android-app://kapp.pixelartworld/http/host/path")
		);
		AppIndex.AppIndexApi.end(client, viewAction);
		client.disconnect();
	}


}
