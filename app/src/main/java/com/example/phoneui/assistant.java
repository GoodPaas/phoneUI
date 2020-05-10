package com.example.phoneui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;


public class assistant {
    private Context context_;
    private Toast_ toast = new Toast_();
    private SpeechRecognizer mIat;
    private int state = 0;
    private boolean testMode = true;
    private StringBuffer listen_ = new StringBuffer();
    private String cls_str = "";
    private speaker speech_speaker;

    // 构造函数
    public assistant(boolean is_init_utility, Context context)
    {
        context_ = context;

        // 状态判断的变量，如果state后续中小于某个值就会无法执行
        state = 0;

        if(is_init_utility)
        {
            //toast.show(context, "init utility success", 1000);

            // 状态判断的变量，如果state后续中小于某个值就会无法执行
            state = state + 1;

            speech_speaker = new speaker(context_);
        }
        else
        {
            toast.show(context, "init utility fail", 1000 );
        }
    }


    // 统一构造封装的听写方法
    public void assistant_listen()
    {

        init_listener();

        listen_result();
    }


    // 听写初始化
    public void init_listener()
    {
        if(state < 1) {
            toast.show(context_, "初始化错误", toast.short_time_len);
            return;
        }

        InitListener mInitListener = new InitListener() {
            @Override
            public void onInit(int i) {
                
            }
        };
        mIat = SpeechRecognizer.createRecognizer(context_, mInitListener);
        if(mIat == null)
        {
            toast.show(context_, "init recognizer fail", toast.short_time_len);
        }
        else
        {
            // 状态判断的变量，如果state后续中小于某个值就会无法执行
            state = state + 10;

            //设置语法ID和 SUBJECT 为空，以免因之前有语法调用而设置了此参数；或直接清空所有参数，具体可参考 DEMO 的示例。
            mIat.setParameter( SpeechConstant.CLOUD_GRAMMAR, null );
            mIat.setParameter( SpeechConstant.SUBJECT, null );
            //设置返回结果格式，目前支持json,xml以及plain 三种格式，其中plain为纯听写文本内容
            mIat.setParameter(SpeechConstant.RESULT_TYPE, "plain");
            //此处engineType为“cloud”
            mIat.setParameter( SpeechConstant.ENGINE_TYPE, "cloud");
            //设置语音输入语言，zh_cn为简体中文
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            //设置结果返回语言
            mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
            // 设置语音前端点:静音超时时间，单位ms，即用户多长时间不说话则当做超时处理
            //取值范围{1000～10000}
            mIat.setParameter(SpeechConstant.VAD_BOS, "2000");
            //设置语音后端点:后端点静音检测时间，单位ms，即用户停止说话多长时间内即认为不再输入，
            //自动停止录音，范围{0~10000}
            mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
            //设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
            mIat.setParameter(SpeechConstant.ASR_PTT,"1");
        }
    }

    // 获取听写的音频和文字、分类
    public void listen_result()
    {

        if(state < 11) {
            toast.show(context_, "初始化错误", toast.short_time_len);
            return;
        }

        listen_.setLength(0);
        //监听器初始化
        RecognizerListener mRecogListener = new RecognizerListener() {
            @Override
            public void onVolumeChanged(int i, byte[] bytes) {

            }

            @Override
            public void onBeginOfSpeech() {
                toast.show(context_, "请说话", toast.short_time_len);

            }

            @Override
            public void onEndOfSpeech() {
                if(testMode)
                {
                    if(listen_.length() > 1)
                    toast.show(context_, listen_.append(" -testMode").toString(), 2*toast.short_time_len);
                }
                else {
                    toast.show(context_, "录音结束", 300);
                }

                //mIat.stopListening();

                cls_str = classify(listen_.toString());

                excute(cls_str);

                speech_speaker.doSpeech(listen_.toString());

            }

            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                if(recognizerResult != null)
                {
                    if(b)
                    {
                        listen_.append(recognizerResult.getResultString());
                    }
                    else
                    {
                        listen_.append(recognizerResult.getResultString());
                    }
                }
                else
                {
                    ;
                }
            }

            @Override
            public void onError(SpeechError speechError) {
                toast.show(context_, "意外错误，请重试", toast.short_time_len);
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        };

        //开始识别
        mIat.startListening(mRecogListener);
    }

    private String classify(String words)
    {
        if(!testMode)
        {
            Log.d("a____________a", "classify: start");
            toast.show(context_, words + "-classify", toast.short_time_len);
        }


        return "default";
    }

    public void excute(String classify)
    {
        switch (classify){
            case "default":
                break;
        }
        return;
    }
}
