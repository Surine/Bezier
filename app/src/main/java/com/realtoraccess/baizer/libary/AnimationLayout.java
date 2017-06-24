package com.realtoraccess.baizer.libary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.realtoraccess.baizer.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 点赞效果
 * <p>
 * <p>
 * Created by Allen Zhang on 2017/6/23.
 */

public class AnimationLayout extends RelativeLayout {

    //初始化所需要的图片
    private SparseArray<Drawable> drawables = new SparseArray<>();
    private SparseArray<Interpolator> interpolators = new SparseArray<>();
    //图片的宽高
    private int drawableHeight;
    private int drawableWidth;

    private LayoutParams params;
    private Random mRandom = new Random();

    private int mWidth;
    private int mHeight;


    private PointF startPoint;
    private PointF endtPoint;


    public AnimationLayout(Context context) {
        super(context);
        init();
    }

    public AnimationLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnimationLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AnimationLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        //初始化图片
        drawables.append(0, getResources().getDrawable(R.mipmap.pl_blue));
        drawables.append(1, getResources().getDrawable(R.mipmap.pl_red));
        drawables.append(2, getResources().getDrawable(R.mipmap.pl_yellow));

        //获取图片的固有宽高
        drawableHeight = getResources().getDrawable(R.mipmap.pl_yellow).getIntrinsicHeight();
        drawableWidth = getResources().getDrawable(R.mipmap.pl_yellow).getIntrinsicWidth();

        //控件居中设置
        params = new LayoutParams(drawableWidth, drawableHeight);
        params.addRule(CENTER_HORIZONTAL, TRUE);
        params.addRule(ALIGN_PARENT_BOTTOM, TRUE);

        //初始化插值器
        interpolators.append(0, new LinearInterpolator()); //匀速
        interpolators.append(1, new AccelerateInterpolator());//加速
        interpolators.append(2, new DecelerateInterpolator());//减速
        interpolators.append(3, new AccelerateDecelerateInterpolator());//先加速后加速
    }


    public void thumbsUp() {
        ImageView view = new ImageView(getContext());
        //设置属性
        view.setImageDrawable(drawables.get(mRandom.nextInt(3)));
        view.setLayoutParams(params);
        addView(view);

        //View添加动画组
        Animator animator = getAnimations(view);
        animator.addListener(new AnimationFinishedListener(view));
        animator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    /**
     * 获取动画组
     *
     * @param view
     */
    private Animator getAnimations(ImageView view) {
        //添加图片显示的动画
        AnimatorSet animationShow = generatorSet(view);

        AnimatorSet animationAll = new AnimatorSet();
        //创建贝塞尔曲线
        ValueAnimator bezier = getBezierAnimator(view);

        //显示动画完成之后要接着做曲线运动
        animationAll.playSequentially(animationShow, bezier);
        //移动曲线的插值器
        animationAll.setInterpolator(interpolators.get(mRandom.nextInt(4)));
        animationAll.setTarget(view);
        return animationAll;
    }

    /**
     * 创建贝塞尔曲线
     *
     * @param view
     * @return
     */
    private ValueAnimator getBezierAnimator(ImageView view) {
        List<PointF> pointFs = new ArrayList<>();
        startPoint = new PointF((mWidth - drawableWidth) / 2, mHeight - drawableHeight);
        endtPoint = new PointF(mRandom.nextInt(getWidth()), 0);
        pointFs.add(startPoint);
        pointFs.add(getPoint(1));
        pointFs.add(getPoint(2));
        pointFs.add(getPoint(3));
        pointFs.add(endtPoint);
        BezierExpression evaluator = new BezierExpression(pointFs);
        //动画的起始点、结束点
        ValueAnimator animator = ValueAnimator.ofObject(evaluator, startPoint, endtPoint);
        animator.addUpdateListener(new AnimatorListener(view));
        animator.setTarget(view);
        animator.setDuration(3500);
        return animator;
    }

    /**
     * 点赞之后漂浮动画
     *
     * @param view
     * @return
     */
    private AnimatorSet generatorSet(ImageView view) {

        ObjectAnimator alpah = ObjectAnimator.ofFloat(view, View.ALPHA, 0.3f, 1.0f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 0.3f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 0.3f, 1.0f);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(500);
        set.setInterpolator(new LinearInterpolator());
        set.playTogether(alpah, scaleX, scaleY);
        set.setTarget(view);
        return set;
    }


    /**
     * 获取中间的控制点
     *
     * @param scale
     * @return
     */
    private PointF getPoint(int scale) {
        PointF pointF = new PointF();
        pointF.x = mRandom.nextInt(mWidth - mWidth / 2) / scale;
        pointF.y = mRandom.nextInt(mHeight - mHeight / 3) / scale;
        return pointF;
    }


    /**
     * 图片显示动画监听器
     */
    private class AnimationFinishedListener extends AnimatorListenerAdapter {
        private View mView;

        AnimationFinishedListener(View view) {
            this.mView = view;
        }


        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            removeView(mView);
        }
    }

    /**
     * View 动画更新监听器
     */
    private class AnimatorListener implements ValueAnimator.AnimatorUpdateListener {

        private View view;

        AnimatorListener(View view) {
            this.view = view;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            //更新当前图片的 位置
            PointF pointF = (PointF) animation.getAnimatedValue();
            view.setX(pointF.x);
            view.setY(pointF.y);
            view.setAlpha(1 - animation.getAnimatedFraction());
        }
    }
}
