package org.mitre.hossenlopp.buttongameshared;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.ArcShape;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by HOSSENLOPP on 6/26/2015.
 */
public class ArcView extends View {
    private ShapeDrawable mMyPie;
    private ShapeDrawable mTheirPie;
    private ShapeDrawable mBlackPie;


    public float getmPrecent() {
        return mPrecent;
    }

    public void setmPrecent(float mPrecent) {
        this.mPrecent = mPrecent;
    }

    private float mPrecent;

    public void setPrecentage(float precentage) {
        mPrecent = precentage;
        updateDrawables();
    }

    private void updateDrawables() {
        this.mMyPie = new ShapeDrawable(new ArcShape(180, mPrecent * 180));
        this.mMyPie.getPaint().setColor(0xff00ff00);
        this.mMyPie.setBounds(10, 10, 310, 310);

        this.mTheirPie = new ShapeDrawable(new ArcShape(180f + (mPrecent * 180f), 180 - (mPrecent * 180f) ));
        this.mTheirPie.getPaint().setColor(0xffff0000);
        this.mTheirPie.setBounds(10, 10, 310, 310);
    }

    public ArcView(Context ctx, AttributeSet attr) {
        super(ctx, attr);
        mPrecent = 0.5f;
        //this.mMyPath = new Path();
        //this.mMyPath.addArc(new RectF(10, 10, 300, 300), 180, 180);

        this.mMyPie = new ShapeDrawable(new ArcShape(180, mPrecent * 180));
        this.mMyPie.getPaint().setColor(0xff00ff00);
        this.mMyPie.setBounds(10, 10, 310, 310);

        this.mTheirPie = new ShapeDrawable(new ArcShape(180f + (mPrecent * 180f), 180 - (mPrecent * 180f) ));
        this.mTheirPie.getPaint().setColor(0xffff0000);
        this.mTheirPie.setBounds(10, 10, 310, 310);

        this.mBlackPie = new ShapeDrawable(new ArcShape(0f, 360f));
        this.mBlackPie.getPaint().setColor(0xff000000);
        this.mBlackPie.setBounds(15, 15, 305, 305);

    }

    protected void onDraw(Canvas canvas) {
        this.mMyPie.draw(canvas);
        this.mTheirPie.draw(canvas);
        this.mBlackPie.draw(canvas);
    }
}
