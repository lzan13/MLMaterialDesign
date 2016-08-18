package net.melove.demo.design.parsehtml;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import net.melove.demo.design.R;
import net.melove.demo.design.application.MLBaseActivity;
import net.melove.demo.design.utils.MLLog;
import net.melove.demo.design.widget.MLToast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by lzan13 on 2016/5/27.
 * 解析HTML文件类，这里解析 IShadowsocks 提供的影梭免费账户信息
 */
public class MLParseHtmlActivity extends MLBaseActivity {

    private String htmlUrl = "http://www.ishadowsocks.net";
    //    private String htmlUrl = "http://www.baidu.com";

    // 剪切板
    private ClipboardManager mClipboard;

    // 界面控件
    private Button mParseBtn;
    private TextView mShadowsocks0;
    private TextView mShadowsocks1;
    private TextView mShadowsocks2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parse_html);

        initView();

    }

    /**
     * 初始化界面UI等操作，
     */
    private void initView() {
        mActivity = this;
        mClipboard = (ClipboardManager) mActivity.getSystemService(CLIPBOARD_SERVICE);

        mParseBtn = (Button) findViewById(R.id.ml_btn_parse);
        mShadowsocks0 = (TextView) findViewById(R.id.ml_text_shadowsocks_0);
        mShadowsocks1 = (TextView) findViewById(R.id.ml_text_shadowsocks_1);
        mShadowsocks2 = (TextView) findViewById(R.id.ml_text_shadowsocks_2);

        //为控件设置点击监听
        mParseBtn.setOnClickListener(viewListener);
        mShadowsocks0.setOnClickListener(viewListener);
        mShadowsocks1.setOnClickListener(viewListener);
        mShadowsocks2.setOnClickListener(viewListener);
    }

    /**
     * 界面控件点击监听
     */
    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.ml_btn_parse:
                jsoupParseHtml(htmlUrl);
                break;
            case R.id.ml_text_shadowsocks_0:
                copyToClipboard(mShadowsocks0.getText().toString());
                break;
            case R.id.ml_text_shadowsocks_1:
                copyToClipboard(mShadowsocks1.getText().toString());
                break;
            case R.id.ml_text_shadowsocks_2:
                copyToClipboard(mShadowsocks2.getText().toString());
                break;
            default:
                break;
            }
        }
    };

    private void copyToClipboard(String str) {
        ClipData clipData = ClipData.newPlainText("ishadowsocks", str);
        mClipboard.setPrimaryClip(clipData);
        MLToast.rightToast("内容已经复制到剪切板 " + str).show();
    }

    /**
     * 使用第三方库 Jsoup 解析指定地址的网页内容
     */
    private void jsoupParseHtml(final String url) {
        new AsyncTask<Object, Integer, Document>() {

            @Override
            protected Document doInBackground(Object... params) {
                try {
                    // Document document = Jsoup.parse(new URL((String) params[0]), 5000);
                    Document document = Jsoup.connect(url).get();
                    return document;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Document document) {
                MLLog.i("html：" + document.html());
                parseHtmlToText(document);
            }
        }.execute(url);

    }


    private void parseHtmlToText(Document document) {
        Element freeElement = document.getElementById("free");
        Elements elements = freeElement.getElementsByClass("col-lg-4");
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            MLLog.i("shadowsocks " + element.html());
            String str0 = element.child(0).html();
            String str1 = element.child(1).html();
            String str2 = element.child(2).html();
            String content = str0.substring(str0.indexOf(":") + 1, str0.length()) + "\n"
                    + str1.substring(str1.indexOf(":") + 1, str1.length()) + "\n"
                    + str2.substring(str2.indexOf(":") + 1, str2.length());
            switch (i) {
            case 0:
                mShadowsocks0.setText(content);
                break;
            case 1:
                mShadowsocks1.setText(content);
                break;
            case 2:
                mShadowsocks2.setText(content);
                break;
            }


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
