package com.example.navigationdrawerexample;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Handler;

import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager2;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.*;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayDeque;
import java.util.Arrays;

import java.io.Serializable;
import java.lang.reflect.Array;


public class MainActivity extends Activity
{
    private FrameLayout mFrameLayout;
    ObjectDrawerItem[] drawerItemNav;
    private String[] mNavigationdrawerItemNavTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    UITimer timerWeb;

    ///
    ObjectDrawerItem tmps;
    public void ListNevCreate(){
        int i=0;drawerItemNav= new ObjectDrawerItem[30];

        drawerItemNav[i++] = new ObjectDrawerItem(R.drawable.sm_ic_home, "خانه","home",false);
        drawerItemNav[i++] = new ObjectDrawerItem(R.drawable.sm_ic_login,"پروفایل کاربر","nprofile",false);
        drawerItemNav[i++] = new ObjectDrawerItem(R.drawable.sm_ic_logout, "خروج","exit",false);
        drawerItemNav[i++] = new ObjectDrawerItem(R.drawable.sm_ic_login, "عضویت","join",false);
        drawerItemNav[i++] = new ObjectDrawerItem(R.drawable.sm_ic_login,"_________________","_",false);
        drawerItemNav[i++] = new ObjectDrawerItem(R.drawable.sm_ic_category, "دسته بندی برنامه ها","EventsCats",false);
        drawerItemNav[i++] = new ObjectDrawerItem(R.drawable.sm_ic_category, "برنامه های باشگاه","events",false);
        drawerItemNav[i++] = new ObjectDrawerItem(R.drawable.sm_ic_category, "اخبار","newslist",false);
        //drawerItemNav[i++] = new ObjectDrawerItem(R.drawable.sm_ic_bookmark, "خدمات رفاهی","utilities",false);
        //drawerItemNav[i++] = new ObjectDrawerItem(R.drawable.sm_ic_bookmark, "دوره های آموزشی","regsterd",false);
        drawerItemNav[i++] = new ObjectDrawerItem(R.drawable.sm_ic_bookmark, "ارتباط با ما","contact us",false);

        drawerItemNav[i++] = new ObjectDrawerItem(R.drawable.sm_ic_category, "گالری تصاویر","gallery",false);
        drawerItemNav[i++] = new ObjectDrawerItem(R.drawable.sm_ic_bookmark, "نظر سنجی","rating",false);
        drawerItemNav[i++] = new ObjectDrawerItem(R.drawable.sm_ic_login,"_________________","_",false);
//        drawerItemNav[i++] = new ObjectDrawerItem(R.drawable.sm_ic_bullet, "معرفی باشگاه باهم","BahamClub",false);
        drawerItemNav[i++] = new ObjectDrawerItem(R.drawable.sm_ic_bullet, "فرهنگسرای خانواده","FamilyCulture",false);
        drawerItemNav[i++] = new ObjectDrawerItem(R.drawable.sm_ic_bullet, "معرفی باشگاه باهم","presidency",false);
        drawerItemNav[i++] = new ObjectDrawerItem(R.drawable.sm_ic_bullet, "مشاهده پورتال","showportal",false);
        drawerItemNav[i++] = new ObjectDrawerItem(R.drawable.sm_ic_bullet, "درباره نرم افزار","designing",false);

        ObjectDrawerItem[] tmps= new ObjectDrawerItem[i];System.arraycopy(drawerItemNav,0,tmps,0,i);drawerItemNav=tmps;
        ListNavRefresh();
    }


    private void SelectItem(String code)
    {
        Fragment fragment = null;
        switch (code) {
            case "home"://خانه
                PublicStaticValues.newpage="index.html#home=true";
                break;
            case "nprofile"://ورود
                if(!IsEmptyDV("id"))
                    PublicStaticValues.newpage=PublicStaticValues.urlsite_nz+"mobilepage/Mobile_profile.aspx?usrId="+GetDV("id");
                else
                    PublicStaticValues.newpage="tk_login.html";
                break;
            case "join":
                PublicStaticValues.newpage="iframe.html#index=0";
                break;
            case "EventsCats":
                PublicStaticValues.newpage="index.html#list=0";
                break;
            case "events":
                PublicStaticValues.newpage="index.html#events=0";
                break;
            case "newslist":
                PublicStaticValues.newpage="index.html#inf=0";
                break;
            case "gallery"://گالری تصاویر
                PublicStaticValues.newpage= "gallery.htm";
                break;
            case "rating":
                PublicStaticValues.newpage= "rating.html";
                break;//
            case "utilities":
                PublicStaticValues.newpage= "index.html#refahi=0";
                break;
            case "regsterd":
                PublicStaticValues.newpage= "search.html";
                break;
            case "exit"://خروج
                ExitAccountMenu();
                break;
            case "contact us"://ارتباط با ما
                PublicStaticValues.newpage="iframe.html#index=1";
                break;
            case "FamilyCulture"://چارت سازمانی
                PublicStaticValues.newpage="detail.html#grg=0";
                break;
            case "BahamClub"://چارت سازمانی
                PublicStaticValues.newpage="detail.html#Club=0";
                break;
            case "presidency"://ارتباط با ریاست
                PublicStaticValues.newpage="detail.html#pre=0";
                break;
            case "showportal"://مشاهده پرتال
                OpenChrome(PublicStaticValues.urlsite_nz);
                break;
            case "designing"://طراحی و اجرا
                PublicStaticValues.newpage="t.html";
                break;
        }
//#ea454545
        ViewPager viewPager1;

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            int position=lngi(code);
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            mDrawerLayout.closeDrawer(mDrawerList);

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
        mDrawerLayout.closeDrawer(mDrawerList);

    }

    public void ExitAccountMenu()
    {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.hand_cursor_filled)
                .setTitle("بستن برنامه")
                .setMessage("آیا مایل به خروج از حساب کاربری خود هستید؟")
                .setPositiveButton("بلی", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SetDV("id", "");
                        SetDV("nameuser", "");
                        SetDV("ut", "");

                        ListNevLoginLogout();
                        ul.Alert("خروج");
                    }
                })
                .setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    public String GetDV(String id)
    {
        SharedPreferences settings =PublicStaticValues.mContext.getSharedPreferences("UserInfo", 0);
        return settings.getString(id, "").toString();
    }

    public boolean IsEmptyDV(String id)
    {
        SharedPreferences settings =PublicStaticValues.mContext.getSharedPreferences("UserInfo", 0);
        String str=settings.getString(id, "").toString();
        return ((str==null) || (str.length()==0)||((str.contains("NaN"))&&(str.length()==3)));// !Strings.isNullOrEmpty(settings.getString(id, "").toString());
    }

    public void SetDV(String id,String val)
    {
        SharedPreferences settings =PublicStaticValues.mContext.getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(id,val);
        editor.commit();
    }

    public void ListNevLoginLogout()
    {
        if (IsEmptyDV("nameuser"))
        {
            ListNav_Set_Name(lngi("nprofile"), "ورود");
            ListNav_Set_Hide(lngi("exit"), true);
            ListNav_Set_Hide(lngi("join"), false);
        }
        else
        {
            //ListNav_Set_Name(lngi("nprofile"), GetDV("nameuser"));
            ListNav_Set_Hide(lngi("nprofile"), true);
            ListNav_Set_Hide(lngi("exit"), false);
            ListNav_Set_Hide(lngi("join"), true);

            if(Integer.parseInt(GetDV("ul"))>4)
                ListNav_Set_Hide(lngi("regsterd"), true);

        }
    }


    public void ReplaceFragment(Fragment fragmentNow,int mId)
    {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(mId, fragmentNow).commit();
    }


    public void ListNavRefresh()
    {
        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.listview_item_row, drawerItemNav);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new drawerItemNavClickListener());
    }

    public int lngi(String codem)
    {
        for(int i=0;i<mDrawerList.getAdapter().getCount();i++)
            if((((DrawerItemCustomAdapter)mDrawerList.getAdapter()).getItem(i).code).equalsIgnoreCase(codem))
                return i;

        ul.Alert("Error ListNav_Get_Code: " + codem);
        return -1;
    }

    public String ListNav_Get_Code(int position)
    {
        return ((DrawerItemCustomAdapter)mDrawerList.getAdapter()).getItem(position).code;
    }
    public void ListNav_Set_Code(int position,String Value)
    {
        ((DrawerItemCustomAdapter)mDrawerList.getAdapter()).getItem(position).code=Value;
    }

    public boolean ListNav_Get_Hide(int position)
    {
        return ((DrawerItemCustomAdapter)mDrawerList.getAdapter()).getItem(position).hide;
    }
    public void ListNav_Set_Hide(int position,boolean Value)
    {
        Value=Value;
        ((DrawerItemCustomAdapter)mDrawerList.getAdapter()).getItem(position).hide=Value;
        ListNavRefresh();
    }

    public String ListNav_Get_Name(int position)
    {
        return ((DrawerItemCustomAdapter)mDrawerList.getAdapter()).getItem(position).name;
    }

    public void ListNav_Set_Name(int position,String Value)
    {
        ((DrawerItemCustomAdapter)mDrawerList.getAdapter()).getItem(position).name=Value;
        ListNavRefresh();
    }



    public void OpenChrome(String url) {
        try {
            Intent i = new Intent("android.intent.action.MAIN");
            i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
            i.addCategory("android.intent.category.LAUNCHER");
            i.setData(Uri.parse(url));
            startActivity(i);
        }
        catch(ActivityNotFoundException e) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(i);
        }
    }

    private void selectPage(String url) {
        Fragment fragment = null;
        fragment  = WebViewFragment.newInstance(url);

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//only PORTRAIT
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//only PORTRAIT
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
		setContentView(R.layout.activity_main);
        mFrameLayout = (FrameLayout) findViewById(R.id.content_frameMsg);
        PublicStaticValues.mContext=this;
        PublicStaticValues.mActivity=this;



        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float yInches = (metrics.heightPixels / metrics.ydpi)*(2/metrics.density-2);
        float xInches = (metrics.widthPixels / metrics.xdpi)*(2/metrics.density-2);
        double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);

        //ul.Alert("diagonalInches: "+ diagonalInches);

        int dpi = getResources().getDisplayMetrics().densityDpi;
        int size_header=60;
        int top_padding=130;

//        if(diagonalInches>6)
//        {
//            size_header=53;
//            top_padding=24;
//        }
        top_padding=0;

        FrameLayout ActionBarTools = (FrameLayout)findViewById(R.id.ActionBarTools);
        ActionBarTools.setVisibility(View.GONE);
        FrameLayout content_frame = (FrameLayout)findViewById(R.id.content_frame);
        content_frame.setPadding(0,top_padding,0,0);
        content_frame.setTop(0);
        ReplaceFragment(WebViewFragment.newInstance("actionbar.html#size="+size_header),R.id.ActionBarTools);


		mTitle = mDrawerTitle = getTitle();


		mNavigationdrawerItemNavTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        ListNevCreate();


        ListNevLoginLogout();


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) { };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        
        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().hide();
        
        
        if (savedInstanceState == null) {
            // on first time display view for first nav item
            SelectItem("home");
        }

        Handler uiHandlerWeb = new Handler();
        Runnable runMethodWeb = new Runnable(){
            public void run()
            {
                if(PublicStaticValues.newSelectItem.length()>0)
                {
                    if(PublicStaticValues.newSelectItem!=PublicStaticValues.oldSelectItem)
                    {
                        PublicStaticValues.oldSelectItem=PublicStaticValues.newSelectItem;
                        SelectItem(PublicStaticValues.newSelectItem);
                    }
                }


                if(PublicStaticValues.frameMsg.length()>0)
                {
                    if(PublicStaticValues.frameMsg_tmp!=PublicStaticValues.frameMsg) {
                        PublicStaticValues.frameMsg_tmp=PublicStaticValues.frameMsg;
                        WebView webMsg = (WebView) findViewById(R.id.webMsg);
                        webMsg.loadUrl("file:///android_asset/" + PublicStaticValues.frameMsg);
                        webMsg.getLayoutParams().width= (int)(getWindowManager().getDefaultDisplay().getWidth()*0.8);
                        webMsg.getLayoutParams().height= (int)(getWindowManager().getDefaultDisplay().getHeight()*0.3);
                        webMsg.requestLayout();
                        mFrameLayout.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    mFrameLayout.setVisibility(View.GONE);
                }

                if(!PublicStaticValues.oldpage.equalsIgnoreCase(PublicStaticValues.newpage))
                {
                    PublicStaticValues.oldpage=PublicStaticValues.newpage;
                    if((PublicStaticValues.newpage.length()>0)&&(!PublicStaticValues.newpage.contains("tk_error_connetion.html")))
                        PublicStaticValues.lastpage=PublicStaticValues.newpage;
                    if(PublicStaticValues.newpage=="")return;

                    if(!PublicStaticValues.newpage.contains("home"))
                        exit_program_number=0;

                    selectPage(PublicStaticValues.newpage);
                    PublicStaticValues.spages.push(PublicStaticValues.newpage);
                }

                if(PublicStaticValues.OpenNav==1)
                {
                    PublicStaticValues.OpenNav=0;
                    mDrawerLayout.openDrawer(mDrawerList);
                }

                if(PublicStaticValues.ExecuteFunction!=0)
                {
                    if(PublicStaticValues.ExecuteFunction==1)
                    {
                        PublicStaticValues.ExecuteFunction = 0;
                        ListNevLoginLogout();

                    }
                }
            }

        };
        timerWeb = new UITimer(uiHandlerWeb, runMethodWeb, 1);
        timerWeb.start();

        SelectItem("home");
	}





    @Override
	public boolean onCreateOptionsMenu(Menu menu)
    {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
    {
       if (mDrawerToggle.onOptionsItemSelected(item)){
           return true;
       }
       return super.onOptionsItemSelected(item);
	}


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
	



    private class drawerItemNavClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SelectItem((((DrawerItemCustomAdapter) mDrawerList.getAdapter()).getItem(position).code));
        }
    }




    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    int exit_program_number=0;
    @Override
    public void onBackPressed()
    {
        if(exit_program_number==0) {
            SelectItem("home");
            ul.Alert("برای بستن برنامه مجدد کلید نمایید");
            exit_program_number=1;
            return;
        }
        if(exit_program_number==1) {
//            final ScrollView s_view = new ScrollView(getApplicationContext());
//            final TextView t_view = new TextView(getApplicationContext());
//            t_view.setText("مایل به بستن برنامه هستید؟");
//            t_view.setTextSize(17);
//            t_view.setTextColor(Color.parseColor("#000000"));
//            t_view.setPadding(20,10,20,10);
//            t_view.setTypeface(Typeface.createFromAsset(PublicStaticValues.mActivity.getAssets(), "fonts/byekan.otf"));
//            s_view.addView(t_view);
//
//            new AlertDialog.Builder(this)
//                    .setIcon(R.drawable.hand_cursor_filled)
//                    .setTitle("بستن برنامه")
//                    .setView(s_view)
//                            //.setMessage("مایل به بستن برنامه هستید؟")
//                    .setPositiveButton("بلی", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                            finish();
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
//                        }
//                    })
//                    .setNegativeButton("خیر",  new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            exit_program_number=0;
//                        }
//                    })
//                    .show();
        }


    }

}
