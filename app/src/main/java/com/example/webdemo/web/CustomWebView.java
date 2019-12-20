package com.example.webdemo.web;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.webdemo.MainActivity;
import com.example.webdemo.utils.NetUtils;
import com.github.lzyzsd.jsbridge.BridgeWebView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2016-2020
 * FileName: CustomWebView
 * Author: wei.zheng
 * Date: 2019/12/20 14:51
 * Description: CustomWebView
 */
public class CustomWebView extends BridgeWebView {
    private ProgressBar mProgressBar;
    private NetBadView mNetBadView;
    private Activity mActivity;
    private ValueCallback<Uri> uploadFile;
    private ValueCallback<Uri[]> uploadFiles;
    private Uri mCaptureUri;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    private View mCustomView;
    private Map<String, String> titles = new HashMap<>();

    public TitleChangeCallBack getTitleChangeCallBack() {
        return titleChangeCallBack;
    }

    public void setTitleChangeCallBack(TitleChangeCallBack titleChangeCallBack) {
        this.titleChangeCallBack = titleChangeCallBack;
    }

    private TitleChangeCallBack titleChangeCallBack;

    public ProgressBar getmProgressBar() {
        return mProgressBar;
    }

    public void setmProgressBar(ProgressBar mProgressBar) {
        this.mProgressBar = mProgressBar;
    }

    public NetBadView getmNetBadView() {
        return mNetBadView;
    }

    public void setmNetBadView(NetBadView mNetBadView) {
        this.mNetBadView = mNetBadView;
    }

    public Activity getmActivity() {
        return mActivity;
    }

    public void setmActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (context != null && context instanceof Activity) {
            mActivity = (Activity) context;
        }
        initWebView();
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (context != null && context instanceof Activity) {
            mActivity = (Activity) context;
        }
        initWebView();
    }

    public CustomWebView(Context context) {
        super(context);
        if (context != null && context instanceof Activity) {
            mActivity = (Activity) context;
        }
        initWebView();
    }

    private void initWebView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true);
        }
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);//支持javaScript
        settings.setDefaultTextEncodingName("utf-8");//设置网页默认编码
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowFileAccessFromFileURLs(true);
        }
        settings.setAllowFileAccess(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setWebContentsDebuggingEnabled(true);
        }
        // 保存表单数据
        settings.setSaveFormData(true);
        // 是否应该支持使用其屏幕缩放控件和手势缩放
        settings.setSupportZoom(true);
        // 启动应用缓存
        settings.setAppCacheEnabled(true);
        // 排版适应屏幕，只显示一列
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setBlockNetworkImage(false);
        settings.setDatabaseEnabled(true);
        if (NetUtils.isNetworkConnected(mActivity)) {
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
        }

        CustomWebViewClient webViewClient = new CustomWebViewClient();
        webViewClient.setOnWebViewListener(new CustomWebViewClient.OnWebViewListener() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.i("lzp", "load_url=" + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (mNetBadView != null) {
                    Log.d("MAPP_WEB", "onPageFinished");
                    mNetBadView.checkNet(mActivity);
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

            }

            @Override
            public void shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("lzp", "load_url=" + url);
            }
        });
        setWebViewClient(webViewClient);
        setWebChromeClient(new ChromeClient());
        setDownloadListener(new WebViewDownLoadListener(mActivity));
    }

    private class ChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (view instanceof CustomWebView) {
                CustomWebView v = (CustomWebView) view;

                ProgressBar pvWeb = v.getmProgressBar();

//                if (pvWeb == null) {
//                    super.onProgressChanged(view, newProgress);
//                    return;
//                }
                Log.d("lzp", "onProgressChanged:" + newProgress);
                if (newProgress == 100) {
                    if (titleChangeCallBack != null) {
                        String url = v.getUrl();
                        String title = titles.get(url);
                        if (!TextUtils.isEmpty(title)) {
                            titleChangeCallBack.onTitleChange(v.getTitle());
                        }
                        MainActivity activity = null;
                        if (getContext() instanceof MainActivity) {
                            activity = (MainActivity) getContext();
                        }
                        /*if (activity != null && view.canGoBack()) {
                            activity.getBottomNavigationBar().setVisibility(GONE);
                        } else if (activity != null && !view.canGoBack()) {
                            activity.getBottomNavigationBar().setVisibility(VISIBLE);
                        }*/
                        Log.d("lzp", "title=" + v.getTitle() + ",url=" + v.getUrl());
                    }
                    if (pvWeb != null) {
                        pvWeb.setVisibility(View.GONE);
                    }
                } else {
                    if (pvWeb != null) {
                        pvWeb.setVisibility(View.VISIBLE);
                        pvWeb.setProgress(newProgress);
                    }
                }
                return;
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            Log.d("lzp", "onreceive title=" + title);
            if (title != null && title.contains("及优LinkPro")) {
                return;
            }
            if (titleChangeCallBack != null) {
                titleChangeCallBack.onTitleChange(title);
                if (!TextUtils.isEmpty(title)) {
                    titles.put(view.getUrl(), title);
                }
            }
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            Log.i("test", "openFileChooser 1");
            CustomWebView.this.uploadFile = uploadMsg;
            openFileChooseProcess();
        }

        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsgs) {
            Log.i("test", "openFileChooser 2");
            CustomWebView.this.uploadFile = uploadMsgs;
            openFileChooseProcess();
        }

        // For Android  > 4.1.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            Log.i("test", "openFileChooser 3");
            CustomWebView.this.uploadFile = uploadMsg;
            openFileChooseProcess();
        }

        // For Android  >= 5.0
        public boolean onShowFileChooser(WebView webView,
                                         ValueCallback<Uri[]> filePathCallback,
                                         FileChooserParams fileChooserParams) {
            Log.i("test", "openFileChooser 4:" + filePathCallback.toString());
            mCaptureUri = null;
            CustomWebView.this.uploadFiles = filePathCallback;
//            openFileChooseProcess();
            if (Build.VERSION.SDK_INT >= 23) {
                List<String> permissions = null;
                if (getContext().checkSelfPermission(Manifest.permission.CAMERA) !=
                        PackageManager.PERMISSION_GRANTED) {
                    permissions = new ArrayList<>();
                    permissions.add(Manifest.permission.CAMERA);
                }
                if (getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    if (permissions == null) {
                        permissions = new ArrayList<>();
                    }
                    permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    if (permissions == null) {
                        permissions = new ArrayList<>();
                    }
                    permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                }

                if (permissions == null) {
                    take();
                } else {
                    String[] permissionArray = new String[permissions.size()];
                    permissions.toArray(permissionArray);
                    // Request the permission. The result will be received
                    // in onRequestPermissionResult()
                    ((Activity) getContext()).requestPermissions(permissionArray, 0);
                }
            } else {
                take();
            }

            return true;
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback customViewCallback) {
            mCustomViewCallback = customViewCallback;
            if (mActivity != null) {
                onCustomViewShow(view);
            }
        }

        @Override
        public void onHideCustomView() {
            if (mCustomViewCallback != null) {
                mCustomViewCallback.onCustomViewHidden();
                mCustomViewCallback = null;
            }
            if (mActivity != null) {
                onCustomViewHidden();
            }
        }
    }

    public void onCustomViewShow(View view) {
        mCustomView = view;
        ((ViewGroup) mActivity.findViewById(android.R.id.content)).addView(view);
    }

    public void onCustomViewHidden() {
        if (mCustomView != null) {
            ((ViewGroup) mActivity.findViewById(android.R.id.content)).removeView(mCustomView);
            final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) getLayoutParams();
            final int originMargin = params.bottomMargin;
            params.bottomMargin = 100;
            setLayoutParams(params);
            post(new Runnable() {
                @Override
                public void run() {
                    params.bottomMargin = originMargin;
                    setLayoutParams(params);
                }
            });
            mCustomView = null;
        }
    }

    private void openFileChooseProcess() {
        if (mActivity == null) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        mActivity.startActivityForResult(Intent.createChooser(i, "test"), 0);
    }

    public void take() {
        File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyApp");
        // Create the storage directory if it does not exist
        if (!imageStorageDir.exists()) {
            imageStorageDir.mkdirs();
        }
        File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        mCaptureUri = Uri.fromFile(file);

        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getContext().getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent i = new Intent(captureIntent);
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            i.setPackage(packageName);
            i.putExtra(MediaStore.EXTRA_OUTPUT, mCaptureUri);
            cameraIntents.add(i);

        }
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntents.get(0)});
        mActivity.startActivityForResult(chooserIntent, 0);
    }

    public Uri getCaptureUri() {
        return mCaptureUri;
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    public void setUploadFileNull() {
        uploadFile = null;
    }

    public void setUploadFilesNull() {
        uploadFiles = null;
    }

    public ValueCallback<Uri> getUploadFile() {
        return uploadFile;
    }

    public ValueCallback<Uri[]> getUploadFiles() {
        return uploadFiles;
    }

    public void setUploadFileResult(Uri result) {
        if (uploadFile != null) {
            uploadFile.onReceiveValue(result);
        }
    }

    public void setUploadFilesResult(Uri[] result) {
        if (uploadFiles != null) {
            uploadFiles.onReceiveValue(result);
        }
    }

    public interface TitleChangeCallBack {
        void onTitleChange(String title);
    }
}
