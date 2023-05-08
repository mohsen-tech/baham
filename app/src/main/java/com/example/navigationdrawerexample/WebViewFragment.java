package com.example.navigationdrawerexample;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.TouchPoint;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.util.List;
import android.app.Fragment;
import android.graphics.Bitmap;



public class WebViewFragment extends Fragment
{
    public final static String TAG = WebViewFragment.class.getSimpleName();
    private WebView browser;
    public String urlp="";
    public Intent target_FileChooser;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    public WebViewFragment(String url)
    {
        urlp=url;
    }
    public Activity mActivity;

    public  final  int REQUEST_CODE = 6384; // onActivityResult request

    public static WebViewFragment newInstance(String url) {
        return new WebViewFragment(url);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        AssetManager am = getResources().getAssets();
        mActivity= getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_webview, container, false);
    }

    private final View.OnTouchListener disablingTouchListener = new View.OnTouchListener()
    {
        @Override
        public boolean onTouch(View v, MotionEvent ev) {
            int action1 = ev.getAction();
            switch (action1)
            {
                case MotionEvent.ACTION_DOWN:
                    TouchPoint.d_x=ev.getX();
                    TouchPoint.d_y=ev.getY();
                    TouchPoint.TouchDown();
                    break;
                case MotionEvent.ACTION_MOVE:
                    TouchPoint.m_x=ev.getX();
                    TouchPoint.m_y=ev.getY();
                    TouchPoint.TouchMove();
                    break;
                case MotionEvent.ACTION_UP:
                    TouchPoint.u_x=ev.getX();
                    TouchPoint.u_y=ev.getY();
                    TouchPoint.TouchUp();
                    break;
            }

            return false;
        }
    };

    public void OpenFile(String url)
    {
        OpenUrl("file:///android_asset/"+url);
    }

    public static String OpenUrl_Url="";
    public void OpenUrl(String urlin)
    {
        OpenUrl_Url=urlin;
        browser.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //view.loadUrl(url);
                return true;
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                view.clearView();
                OpenUrl_Url="file:///android_asset/tk_error_connetion.html";
                browser.loadUrl(OpenUrl_Url);//

            }

            //Show loader on url load
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(mActivity);
                    progressDialog.setMessage("Loading...");
                    //progressDialog.show();
                }
            }
            public void onPageFinished(final WebView view, String url) {
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    }
                }, 100);

                try{
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }
        });


        browser.setWebChromeClient(new WebChromeClient()
        {
            // openFileChooser for Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType){
                PublicStaticValues.mUploadMessage = uploadMsg;
                try{
                    File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "AndroidExampleFolder");
                    if (!imageStorageDir.exists()) {
                        imageStorageDir.mkdirs();
                    }
                    File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                    PublicStaticValues.mCapturedImageURI = Uri.fromFile(file); // save to the private variable
                    final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, PublicStaticValues.mCapturedImageURI);
                    // captureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("image/*");

                    Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[] { captureIntent });

                    startActivityForResult(chooserIntent, PublicStaticValues.FILECHOOSER_RESULTCODE);
                }
                catch(Exception e){
                    e=e;
                    //Toast.makeText(getBaseContext(), "Camera Exception:"+e, Toast.LENGTH_LONG).show();
                }
            }
            // openFileChooser for Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg){
                openFileChooser(uploadMsg, "");
            }
            //openFileChooser for other Android versions
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                openFileChooser(uploadMsg, acceptType);
            }

            public boolean onConsoleMessage(ConsoleMessage cm) {
                onConsoleMessage(cm.message(), cm.lineNumber(), cm.sourceId());
                return true;
            }
            public void onConsoleMessage(String message, int lineNumber, String sourceID) {

            }
        });

        WebSettings settings = browser.getSettings();
        settings.setJavaScriptEnabled(true);

        settings.setLoadsImagesAutomatically(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportMultipleWindows(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        settings.setUseWideViewPort(false);
        settings.setBuiltInZoomControls(false);
        settings.setSupportZoom(false );
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAppCacheEnabled(true);

        settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheMaxSize(1024*1024*8);
        settings.setAppCachePath("/data/data/"+  getActivity().getPackageName() +"/cache");
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        browser.addJavascriptInterface(new JSInterface(browser), "JSInterface");
        browser.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        browser.setOnTouchListener(new View.OnTouchListener() {
            float m_downX=1;
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        m_downX = event.getX();
                    }
                    break;
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP: {
                        event.setLocation(m_downX, event.getY());
                    }
                    break;
                }
                return false;
            }
        });

        if(!(OpenUrl_Url.contains("http")||OpenUrl_Url.contains("www.")))
        {
            String text_html = "";
            try {
                String uurl = OpenUrl_Url.replace("file:///android_asset/", "");
                int sharp=uurl.indexOf("#");
                if(sharp>1) {
                    uurl = uurl.substring(0, sharp);
                }
                AssetManager am = getResources().getAssets();
                InputStream is = am.open(uurl);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                text_html = new String(buffer);
            } catch (IOException e) {
                ///throw new RuntimeException(e);
                text_html= "<body>Error Not Find File Object "+OpenUrl_Url+"</body>";
            }

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            text_html = ReplaceAllStr(text_html, ".///", "file:///android_asset/");
            browser.loadDataWithBaseURL( "file:///android_asset/"+OpenUrl_Url, text_html, "text/html", "utf-8", null);
        }
        else {
            browser.loadUrl(OpenUrl_Url);
        }
    }


    private Uri getTempUri() {
        return Uri.fromFile(getTempFile());
    }
    private static final String TEMP_PHOTO_FILE = "temporary_holder.jpg";
    private File getTempFile() {
        if (isSDCARDMounted()) {

            File f = new File(Environment.getExternalStorageDirectory(),TEMP_PHOTO_FILE);
            try {
                f.createNewFile();
            } catch (IOException e) {

            }
            return f;
        } else {
            return null;
        }
    }

    private boolean isSDCARDMounted(){
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED))
            return true;
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final int CROP_PIC = 2;
        if (requestCode == CROP_PIC) {
            Bundle extras = data.getExtras();
            // get the cropped bitmap
            Bitmap imageBitmap = extras.getParcelable("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            PublicStaticValues.BufferChooseFile =byteArray;
            return;
        }

        if ((requestCode==REQUEST_CODE) || (requestCode==2888)) {
            //If the file selection was successful
            if (resultCode ==Activity.RESULT_OK) {
                if (data != null) {
                    final Uri uri = data.getData();
                    try {
                        if(uri==null) {
                            Bundle extras = data.getExtras();
                            Bitmap imageBitmap = (Bitmap) extras.get("data");

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] byteArray = stream.toByteArray();

                            File file = getTempFile();
                            FileOutputStream fos = new FileOutputStream (file);
                            fos.write(byteArray,0,byteArray.length);
                            fos.close();
                            Uri t= getTempUri();
                            CropImage(t);
                            byteArray=byteArray;
                            //PublicStaticValues.BufferChooseFile =byteArray;
                        }
                        else
                        {
                            final String path = ul.getPath(PublicStaticValues.mContext, uri);
                            File file = new File(path);
                            FileInputStream fin = null;
                            fin = new FileInputStream(file);

                            byte fileContent[] = new byte[(int) file.length()];
                            fin.read(fileContent);

                            File file2 = getTempFile();
                            FileOutputStream fos = new FileOutputStream (file2);
                            fos.write(fileContent,0,fileContent.length);
                            fos.close();
                            Uri t= getTempUri();
                            CropImage(t);
                            //PublicStaticValues.BufferChooseFile = fileContent;
                        }
                    }
                    catch (FileNotFoundException e) {
                        System.out.println("File not found" + e);
                    }
                    catch (IOException ioe) {
                        System.out.println("Exception while reading file " + ioe);
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void CropImage(final Uri iUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(iUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 350);
            cropIntent.putExtra("outputY", 320);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, 2);
        }

        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast
                    .makeText(PublicStaticValues.mContext, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }



    public String ReplaceAllStr(String src,String old,String neww){
        while(src.indexOf(old)!=-1)
            src=src.replace(old, neww);
        return src;
    }

    public class JSInterface
    {
        private WebView mAppView;
        public byte[] test11;
        final int CROP_PIC = 2;

        @JavascriptInterface
        public int ConfirmT(String Title,String Message,final String JsYes,final String JsNo,final String CMD)
        {
            new AlertDialog.Builder(PublicStaticValues.mContext)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(Title)
                    .setMessage(Message)
                    .setPositiveButton("بلی", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(JsYes.length()>1) {
                                if(CMD.contains("OpenChrome"))
                                    OpenChrome(JsYes);
                                else
                                    PublicStaticValues.newpage=JsYes;
                            }
                        }
                    })
                    .setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(JsNo.length()>1){
                                if(CMD.contains("OpenChrome"))
                                    OpenChrome(JsNo);
                                else
                                    PublicStaticValues.newpage=JsNo;
                            }
                        }
                    })
                    .show();
            return  0;
        }

        @JavascriptInterface
        public void SelectItem(String val)
        {
            PublicStaticValues.newSelectItem=val;
        }

        @JavascriptInterface
        public int Width()
        {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            return  metrics.widthPixels;
        }

        @JavascriptInterface
        public int Height()
        {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            return  metrics.heightPixels;
        }

        @JavascriptInterface
        public int GetVerNumber() {
            int versionCode = BuildConfig.VERSION_CODE;
            String versionName = BuildConfig.VERSION_NAME;
            return versionCode;
        }

        private static final int FILE_SELECT_CODE = 0;

        public Intent createGetContentIntent() {
            final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            return intent;
        }

        @JavascriptInterface
        public void ShowFileChooser() {
            new AlertDialog.Builder(PublicStaticValues.mContext)
                    .setIcon(android.R.drawable.ic_menu_camera)
                    .setTitle("انتخاب تصویر")
                    .setMessage("منبع انتخاب تصویر رو مشخص نمایید.")
                    .setPositiveButton("دوربین", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            target_FileChooser = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            Intent intent = Intent.createChooser(
                                    target_FileChooser, "");
                            try {
                                startActivityForResult(intent, REQUEST_CODE);
                            } catch (ActivityNotFoundException e) {
                            }
                        }
                    })
                    .setNegativeButton("حافظه", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            target_FileChooser = createGetContentIntent();
                            Intent intent = Intent.createChooser(
                                    target_FileChooser, "");
                            try {
                                startActivityForResult(intent, REQUEST_CODE);
                            } catch (ActivityNotFoundException e) {
                            }
                        }
                    })
                    .show();

        }

        @JavascriptInterface
        public void ShowFileChooser2() {
            Intent target = createGetContentIntent();
            Intent intent = Intent.createChooser(target, "");
            try {
                startActivityForResult(intent, REQUEST_CODE);
            } catch (ActivityNotFoundException e) {}
        }


        @JavascriptInterface
        public String GetVerName() {
            String versionName = BuildConfig.VERSION_NAME;
            return  versionName;
        }

        @JavascriptInterface
        public String UpWin(String v)
        {
            return PublicStaticValues.frameMsg = v;
        }

        @JavascriptInterface
        public void NewPage(String val) {
            PublicStaticValues.newpage=val;
        }

        @JavascriptInterface
        public void OpenNav() {
            PublicStaticValues.OpenNav=1;
        }

        @JavascriptInterface
        public void ExeFunc(int val){
            PublicStaticValues.ExecuteFunction=val;
        }

        @JavascriptInterface
        public boolean IsEmptyDV(String id)
        {
            SharedPreferences settings =PublicStaticValues.mContext.getSharedPreferences("UserInfo", 0);
            String str=settings.getString(id, "").toString();
            return ((str==null) || (str.length()==0)||((str.contains("NaN"))&&(str.length()==3)));// !Strings.isNullOrEmpty(settings.getString(id, "").toString());
        }

        @JavascriptInterface
        public String GetDV(String id)
        {
            SharedPreferences settings =PublicStaticValues.mContext.getSharedPreferences("UserInfo", 0);
            return settings.getString(id, "").toString();
        }

        @JavascriptInterface
        public void SetDV(String id,String val)
        {
            SharedPreferences settings =PublicStaticValues.mContext.getSharedPreferences("UserInfo", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(id,val);
            editor.commit();
        }

        @JavascriptInterface
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

        @JavascriptInterface
        public void GetBufferClear() {
            PublicStaticValues.BufferChooseFile=null;
        }

        @JavascriptInterface
        public String GetBufferValueBase64() {
            if(PublicStaticValues.BufferChooseFile!=null)
                return (new String(Base64.encodeToString(PublicStaticValues.BufferChooseFile, 0)));
            return "";
        }

        @JavascriptInterface
        public String GetBufferImage() {
            if(PublicStaticValues.BufferChooseFile!=null)
            {
                String t ="data:image/png;base64," +(new String(Base64.encodeToString(PublicStaticValues.BufferChooseFile, 0)));
                return t;
            }
            return "";
        }

        public JSInterface(WebView appView)
        {
            this.mAppView = appView;
        }

        @JavascriptInterface
        public String GetUrlConnect(){
            return PublicStaticValues.lastpage;
        }


        @JavascriptInterface
        public boolean Dial(String tel)
        {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+tel));
            startActivity(intent);
            return true;
        }


        @JavascriptInterface
        public int DialogTell(String Title,String Message,final String tell)
        {
            new AlertDialog.Builder(PublicStaticValues.mContext)
                    .setIcon(android.R.drawable.ic_menu_call)
                    .setTitle(Title)
                    .setMessage(Message)
                    .setPositiveButton("SMS", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Sms(tell);
                        }
                    })
                    .setNegativeButton("Dail", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Dial(tell);
                        }
                    })
                    .show();
            return  0;
        }


        @JavascriptInterface
        public boolean Sms(String tel)
        {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:" + Uri.encode(tel)));
            startActivity(intent);
            return true;
        }
        @JavascriptInterface
        public boolean Email(String email)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
            startActivity(intent);
            return true;
        }

        int Notification_c=0;
        @JavascriptInterface
        private void Notification(String text, String link){

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(PublicStaticValues.mContext)
                            .setAutoCancel(true)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle(text);

            NotificationManager mNotificationManager =
                    (NotificationManager) PublicStaticValues.mContext.getSystemService(Context.NOTIFICATION_SERVICE);

            Intent resultIntent = new Intent(Intent.ACTION_VIEW);
            resultIntent.setData(Uri.parse(link));

            PendingIntent pending = PendingIntent.getActivity(PublicStaticValues.mContext, 0, resultIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
            notificationBuilder.setContentIntent(pending);

            mNotificationManager.notify(String.valueOf(System.currentTimeMillis()), Notification_c, notificationBuilder.build());
            Notification_c++;
        }

        @JavascriptInterface
        public boolean AddToBack(String url)
        {
            // public static int new_uback=0;
            PublicStaticValues.new_uback=1;
            url=url.replace("file:///android_asset/","");
            PublicStaticValues.spages.push(url);
            return true;
        }


        @JavascriptInterface
        public boolean MSG_Expire(String url)
        {
            final String url_download=url;
            final ScrollView s_view = new ScrollView(PublicStaticValues.mContext);
            final TextView t_view = new TextView(PublicStaticValues.mContext);
            t_view.setText("برای اجرای برنامه می بایست حتما برنامه شما بروز گردد?");
            t_view.setTextSize(17);
            t_view.setTextColor(Color.parseColor("#000000"));
            t_view.setPadding(20,10,20,10);
            t_view.setTypeface(Typeface.createFromAsset(PublicStaticValues.mActivity.getAssets(), "fonts/byekan.otf"));
            s_view.addView(t_view);

            new AlertDialog.Builder(PublicStaticValues.mContext)
                    .setIcon(R.drawable.update)
                    .setTitle("بروز رسانی")
                    .setView(s_view)

                    .setPositiveButton("قبول", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            OpenChrome(url_download);
                            MSG_Expire(url_download);
                        }
                    })
                    .setNegativeButton("خروج",  new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                            //finish();
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            MSG_Expire(url_download);
                        }
                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            MSG_Expire(url_download);// Your code
                        }
                    })
                    .show();
            return true;
        }


        @JavascriptInterface
        public boolean MSG_Update(String url)
        {
            final String url_download=url;
            final ScrollView s_view = new ScrollView(PublicStaticValues.mContext);
            final TextView t_view = new TextView(PublicStaticValues.mContext);
            t_view.setText("نسخه جدیدتری از برنامه آماده دانلود است آیا مایل به دریافت آن هستید؟");
            t_view.setTextSize(17);
            t_view.setTextColor(Color.parseColor("#000000"));
            t_view.setPadding(20,10,20,10);
            t_view.setTypeface(Typeface.createFromAsset(PublicStaticValues.mActivity.getAssets(), "fonts/byekan.otf"));
            s_view.addView(t_view);

            new AlertDialog.Builder(PublicStaticValues.mContext)
                    .setIcon(R.drawable.update)
                    .setTitle("بروز رسانی")
                    .setView(s_view)

                    .setPositiveButton("باشه", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            OpenChrome(url_download);
                        }
                    })
                    .setNegativeButton("یک وقت دیگه",  new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            MSG_Update(url_download);// Your code
                        }
                    })
                    .show();
            return true;
        }


        @JavascriptInterface
        public String GetUrl(){
            return PublicStaticValues.newpage;
        }

        @JavascriptInterface
        public void Alert(String echo){
            ul.Alert(echo);
        }

        @JavascriptInterface
        public void Share(String val) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,val);
            //"Hey check out my app at: https://play.google.com/store/apps/details?id=com.google.android.apps.plus");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }


        @JavascriptInterface
        public int GetOrientation(){
            Display display = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            int orientation = display.getOrientation();
            return orientation;
        }

        @JavascriptInterface
        public String getInstalledApps(boolean showImage) {
            boolean getSysPackages=false;
            JSONArray ja=new JSONArray();

            AssetManager am = getResources().getAssets();

            List<PackageInfo> packs = getActivity().getPackageManager().getInstalledPackages(0);
            for(int i=0;i<packs.size();i++)
            {
                PackageInfo p = packs.get(i);
                if ((!getSysPackages) && (p.versionName == null)) continue ;

                try
                {
                    JSONObject pnObj = new JSONObject();
                    if ((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
                    {
                        pnObj.put("appname", p.applicationInfo.loadLabel(getActivity().getPackageManager()).toString());
                        pnObj.put("versionName", p.versionName);
                        pnObj.put("versionCode", p.versionCode);

                        if(showImage) {
                            Drawable d = p.applicationInfo.loadIcon(getActivity().getPackageManager());
                            Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] bitmapdata = stream.toByteArray();
                            String strDataIcon = "data:image/png;base64," + (new String(Base64.encodeToString(bitmapdata, 0)));
                            pnObj.put("bitmap", strDataIcon);
                            test11 = bitmapdata;
                        }
                        ja.put(pnObj);
                    }
                }catch(JSONException e){}
            }
            return ja.toString();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        browser = (WebView) view.findViewById(R.id.webView);
        OpenUrl(urlp);
    }

}
