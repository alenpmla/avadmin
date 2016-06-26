package in.codme.axisvoiceadmin;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * Created by Alen on 26-Jun-16.
 */
public class ChatFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_chat,container,false);

        WebView webview=(WebView)rootView.findViewById(R.id.webview);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            webview.evaluateJavascript("enable();", null);
        } else {
            webview.loadUrl("javascript:enable();");
        }

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setAllowFileAccess(true);

        webview.loadUrl("file:///android_asset/chat.html");

        return rootView;
    }
}
