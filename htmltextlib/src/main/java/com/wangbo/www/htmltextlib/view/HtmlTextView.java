package com.wangbo.www.htmltextlib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wangbo.www.htmltextlib.R;
import com.wangbo.www.htmltextlib.call.HtmlTextViewCall;
import com.wangbo.www.htmltextlib.utils.StrUtils;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.LinkHolder;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.Callback;
import com.zzhoujay.richtext.callback.LinkFixCallback;
import com.zzhoujay.richtext.callback.OnImageClickListener;
import com.zzhoujay.richtext.callback.OnImageLongClickListener;
import com.zzhoujay.richtext.callback.OnUrlClickListener;
import com.zzhoujay.richtext.callback.OnUrlLongClickListener;
import com.zzhoujay.richtext.parser.CachedSpannedParser;

import java.util.List;

public class HtmlTextView extends RelativeLayout {
    private boolean isOption;
    private int errorOption,noDoOption,rightOption,errorImage;
    private TextView txt_tv,option_tv;
    private HtmlTextViewCall htmlTextViewCall;

    public void setHtmlTextViewCall(HtmlTextViewCall htmlTextViewCall) {
        this.htmlTextViewCall = htmlTextViewCall;
    }

    public HtmlTextView(Context context) {
        this(context,null);
    }

    public HtmlTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HtmlTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);
    }
    private void initView(Context context,AttributeSet attributeSet){
        TypedArray array=context.obtainStyledAttributes(attributeSet, R.styleable.HtmlTextView);
        isOption=array.getBoolean(R.styleable.HtmlTextView_isOption,false);
        errorOption=array.getResourceId(R.styleable.HtmlTextView_errorOption,R.drawable.error_bg);
        rightOption=array.getResourceId(R.styleable.HtmlTextView_rightOption,R.drawable.right_bg);
        noDoOption=array.getResourceId(R.styleable.HtmlTextView_noDoOption,R.drawable.nodo_bg);
        errorImage=array.getResourceId(R.styleable.HtmlTextView_errorImage,0);
        View mView= LayoutInflater.from(context).inflate(R.layout.layout_html,this);
        txt_tv=mView.findViewById(R.id.txt_tv);
        option_tv=mView.findViewById(R.id.option_tv);
        initData();
    }
    private void initData(){
        if(isOption){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dip2px(getContext(),35),dip2px(getContext(),35));
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            params.setMargins(dip2px(getContext(),10),dip2px(getContext(),10),0,dip2px(getContext(),10));
            option_tv.setLayoutParams(params);
            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
            params1.addRule(RelativeLayout.CENTER_VERTICAL);
            params1.addRule(RelativeLayout.RIGHT_OF,R.id.option_tv);
            params1.setMargins(dip2px(getContext(),10),dip2px(getContext(),10),dip2px(getContext(),10),dip2px(getContext(),10));
            txt_tv.setLayoutParams(params1);
        }
        option_tv.setVisibility(isOption?VISIBLE:GONE);
    }

    /**
     * 展示选项  正确选项
     */
    public void showRightOption(){
        option_tv.setBackgroundResource(rightOption);
        option_tv.setSelected(true);
    }
    /**
     * 展示选项  错误选项
     */
    public void showErrorOption(){
        option_tv.setBackgroundResource(errorOption);
        option_tv.setSelected(true);
    }
    /**
     * 展示选项  未选择选项
     */
    public void showNoDoOption(){
        option_tv.setBackgroundResource(noDoOption);
        option_tv.setSelected(false);
    }

    /**
     * 设置类容
     * @param txt
     */
    public void showTxt(String txt){
                 RichText
                .from(txt) // 数据源
                .autoFix(false) // 是否自动修复，默认true
                .autoPlay(true) // gif图片是否自动播放
                .showBorder(false) // 是否显示图片边框
                .borderColor(Color.RED) // 图片边框颜色
                .borderSize(10) // 边框尺寸
                .borderRadius(50) // 图片边框圆角弧度
                .scaleType(ImageHolder.ScaleType.fit_auto) // 图片缩放方式
                .size(ImageHolder.MATCH_PARENT, ImageHolder.WRAP_CONTENT)
                .linkFix(new LinkFixCallback() {
                    @Override
                    public void fix(LinkHolder holder) {
                        if(htmlTextViewCall==null)
                            return;
                        htmlTextViewCall.linkFixCall(holder.getUrl());
                    }
                }) // 设置链接自定义回调
                .noImage(true) // 不显示并且不加载图片
                .resetSize(false) // 默认false，是否忽略img标签中的宽高尺寸（只在img标签中存在宽高时才有效），true：忽略标签中的尺寸并触发SIZE_READY回调，false：使用img标签中的宽高尺寸，不触发SIZE_READY回调
                .clickable(true) // 是否可点击，默认只有设置了点击监听才可点击
                .imageClick(new OnImageClickListener() {
                    @Override
                    public void imageClicked(List<String> imageUrls, int position) {
                        if(htmlTextViewCall==null)
                            return;
                        htmlTextViewCall.imageClicked(imageUrls, position);
                    }
                }) // 设置图片点击回调
                .done(new Callback() {
                    @Override
                    public void done(boolean imageLoadDone) {
                        if(htmlTextViewCall==null)
                            return;
                        htmlTextViewCall.done(imageLoadDone);
                    }
                }) // 解析完成回调
                .into(txt_tv); // 设置目标TextView
    }

    /**
     * 根据数字自动设置 A-Z
     * @param num
     */
    public void setAuotOption(int num){
        option_tv.setText(StrUtils.Instance().numberToLetter(num));
    }

    /**
     * 用户设置选项
     * @param txt
     */
    public void setOption(String txt){
        option_tv.setText(txt);
    }

    /**
     * 根据选项 得到顺序  只有自动排序时生效
     * @param le
     * @return
     */
    public int getOptionNum(String le){
        return StrUtils.Instance().letterToNumber(le);
    }
    private   int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
