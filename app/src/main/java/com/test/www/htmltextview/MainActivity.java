package com.test.www.htmltextview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wangbo.www.htmltextlib.view.HtmlTextView;

public class MainActivity extends AppCompatActivity {
    private HtmlTextView htmlTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        htmlTextView=this.findViewById(R.id.tets);
        htmlTextView.showTxt("已知地球上海洋面积约为316&nbsp;000&nbsp;000km2，数据316&nbsp;000&nbsp;000用科学记数法可表示为（　");
        htmlTextView.setAuotOption(1);
        htmlTextView.showErrorOption();
    }
}
