package com.adityay.sachintons.fragments;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;


import com.adityay.sachintons.R;
import com.adityay.sachintons.databinding.FragmentWebViewBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;



import static androidx.constraintlayout.widget.Constraints.TAG;


public class WebDialogFragment extends DialogFragment {

//    @BindView(R.id.mToolbar)
//    Toolbar mToolbar;
//    @BindView(R.id.tvToolbarTitle)
//    TextView tvToolbarTitle;
//    @BindView(R.id.webview)
//    WebView mWebView;
//    @BindView(R.id.progressbar)
//    ProgressBar progressbar;
//    @BindView(R.id.fab)
//    FloatingActionButton fab;

    private static String mUrl, mTitle, mContent;
    private static boolean isEnabled;

    private FragmentWebViewBinding binding;

    public static WebDialogFragment showDialog(FragmentManager fragmentManager, String url, String title, String content, boolean enabled) {
        WebDialogFragment webDialogFragment = new WebDialogFragment();
        mUrl = url;
        mTitle = title;
        mContent = content;
        isEnabled = enabled;
        webDialogFragment.show(fragmentManager, TAG);
        return webDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //View view = inflater.inflate(R.layout.fragment_web_view, container, false);

        binding = FragmentWebViewBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //ButterKnife.bind(this, view);
        initViews();
        return view;

    }

    private void initViews() {
        if (TextUtils.isEmpty(mTitle))
            binding.mToolbar.getRoot().setVisibility(View.GONE);
        else
            binding.mToolbar.tvToolbarTitle.setText(mTitle);


        binding.progressbar.setMax(100);
        binding.progressbar.setProgress(1);
        binding.mToolbar.ivBack.setOnClickListener(v -> onClickBackButton());
        binding.fab.setOnClickListener(v -> gotoYoutube());
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(!TextUtils.isEmpty(mUrl)) {
             binding.webview.setVisibility(View.VISIBLE);
             loadUrl();
        } else {
            binding.webview.setVisibility(View.GONE);
        }

//        if(TextUtils.isEmpty(mContent)){
//            binding.fab.hide();
//        }else{
//            binding.fab.show();
//        }
    }

    //@OnClick(R.id.ivBack)
    void onClickBackButton() {
        dismiss();
    }

    private void loadUrl() {
        binding.webview.setVisibility(View.VISIBLE);
        binding.webview.clearCache(true);
        if(isEnabled) {
            binding.webview.getSettings().setJavaScriptEnabled(true);
        }
        binding.webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                binding.progressbar.setProgress(newProgress);

                if (newProgress == 100) binding.progressbar.setVisibility(View.GONE);

            }
        });
        binding.webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                binding.webview.loadUrl(request.getUrl().toString());
                return false;
            }
        });
        binding.webview.loadUrl(mUrl);
    }

    //@OnClick(R.id.fab)
    void gotoYoutube(){
        if(!TextUtils.isEmpty(mContent)){
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mContent));
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + mContent));
            try {
                startActivity(appIntent);
            } catch (ActivityNotFoundException ex) {
                startActivity(webIntent);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
