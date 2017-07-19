package com.liwenzhi.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * 各种动画效果显示
 */
public class MainActivity extends Activity {
    ImageView iv;
    Animation animation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        iv = (ImageView) findViewById(R.id.iv);
    }


    /**
     * 平移动画
     */
    public void translate(View view) {
        animation = AnimationUtils.loadAnimation(this, R.anim.translate_anim);
        iv.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void onAnimationEnd(Animation animation) {
                setIVAnimation(R.anim.scale_anim);
            }

            public void onAnimationRepeat(Animation animation) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }


    /**
     * 透明动画
     */
    public void alpha(View view) {
        animation = AnimationUtils.loadAnimation(this, R.anim.alpha_anim);
        iv.startAnimation(animation);
    }


    /**
     * 缩放动画
     */

    public void scale(View view) {
        iv.startAnimation(getAnimation(R.anim.scale_anim));
    }

    /**
     * 旋转动画
     */
    public void rotate(View view) {
        setIVAnimation(R.anim.rotate_anim);
    }

    /**
     * 各种效果集于一身的动画
     */
    public void all(View view) {
        setIVAnimation(R.anim.all_anim);
    }

    /**
     * 传入动画的ID，获取动画对象
     */
    private Animation getAnimation(int animID) {
        return AnimationUtils.loadAnimation(this, animID);
    }

    /**
     * 传入动画的ID，直接给ImageView设置动画
     */
    private void setIVAnimation(int animID) {
        iv.startAnimation(AnimationUtils.loadAnimation(this, animID));
    }

}
