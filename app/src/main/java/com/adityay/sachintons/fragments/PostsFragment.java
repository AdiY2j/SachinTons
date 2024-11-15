package com.adityay.sachintons.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.adityay.sachintons.R;
import com.adityay.sachintons.activities.MainActivity;
import com.adityay.sachintons.databinding.FragmentPostsBinding;




public class PostsFragment extends Fragment {

//    @BindView(R.id.webview)
//    WebView webView;
//    @BindView(R.id.progressbar)
//    ProgressBar progressBar;


    private FragmentPostsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_posts, container, false);

        binding = FragmentPostsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTweets();
    }

    private void setTweets() {
        binding.webview.clearCache(true);
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                binding.webview.setVisibility(View.GONE);
                binding.progressbar.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                WebDialogFragment.showDialog(getChildFragmentManager(), request.getUrl().toString(), "Twitter", "", true);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                binding.webview.setVisibility(View.VISIBLE);
                binding.progressbar.setVisibility(View.GONE);
            }
        });
        binding.webview.loadDataWithBaseURL("https://twitter.com", "<a class=\"twitter-timeline\" data-theme=\"light\" href=\"https://twitter.com/sachin_rt?ref_src=twsrc%5Etfw\">Tweets by sachin_rt</a> <script async src=\"https://platform.twitter.com/widgets.js\" charset=\"utf-8\"></script>", "text/html", "UTF-8", "");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
