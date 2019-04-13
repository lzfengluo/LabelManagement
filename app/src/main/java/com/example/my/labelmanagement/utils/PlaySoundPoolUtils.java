package com.example.my.labelmanagement.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

/**
 * 声音工具类
 * 播放正确与错误的提示音
 *
 * @author 张智超
 * @date 2019/3/21
 */
public class PlaySoundPoolUtils {
    private static PlaySoundPoolUtils playSoundPoolUtils;
    private Context context;
    /**
     * 定义SoundPool 对象
     */
    private SoundPool soundPool;
    private int s1, s2;
    /**
     * 定义HASH表
     */
    private HashMap<Integer, Integer> soundPoolMap;
    /**
     * 正常扫描声音
     */
    private static final int LASER = 1;
    /**
     * 错误声音
     */
    private static final int ERROR = 2;

    private AudioManager mgr;

    public static PlaySoundPoolUtils getPlaySoundPoolUtils(Context context) {
        if (playSoundPoolUtils == null) {
            playSoundPoolUtils = new PlaySoundPoolUtils(context);
        }
        return playSoundPoolUtils;
    }

    private PlaySoundPoolUtils(Context context) {
        this.context = context;
        initSounds();
        loadSounds();
    }


    /**
     * 初始化
     */
    @SuppressLint("UseSparseArrays")
    private void initSounds() {
        // 初始化soundPool 对象,
        // 第一个参数是允许有多少个声音流同时播放,
        // 第2个参数是声音类型,
        // 第三个参数是声音的品质
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

    }

    /**
     * 播放声音
     *
     * @param sound id
     */
    private void play(int sound) {
        soundPool.play(
                //播放的音乐id
                sound,
                // 左声道音量
                1,
                // 右声道音量
                1,
                // 优先级，0为最低
                0,
                // 循环次数，0 不循环，-1 永远循环
                0,
                // 回放速度 ，该值在0.5-2.0之间，1为正常速度
                1f);
    }

    /**
     * 加载声音
     */
    private void loadSounds() {
        s1 = soundPool.load(context, com.speedata.deivice.R.raw.scan, 0);
        s2 = soundPool.load(context, com.speedata.deivice.R.raw.error, 0);
    }

    /**
     * 播放扫描成功声音
     */
    public void playLaser() {
        play(s1);
    }

    /**
     * 播放错误声音
     */
    public void playError() {
        play(s2);
    }

    /**
     * 释放资源
     */
    public void closeSound() {
        soundPool.release();
    }
}
