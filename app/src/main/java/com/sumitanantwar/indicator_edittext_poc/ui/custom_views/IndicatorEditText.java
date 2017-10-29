package com.sumitanantwar.indicator_edittext_poc.ui.custom_views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.sumitanantwar.indicator_edittext_poc.R;
import com.sumitanantwar.indicator_edittext_poc.utilities.ColorUtility;

/**
 * Created by Sumit Anantwar on 10/27/17.
 */

public class IndicatorEditText extends AppCompatEditText
{
    private static final String LOG_TAG = "IndicatorEditText";

    // Indicator Constants
    private static final float INDICATOR_X_OFFSET      = 25.0f;
    private static final float INDICATOR_RADIUS        = 10.0f;
    private static final int   INDICATOR_COLOR_ERROR   = Color.RED;
    private static final int   INDICATOR_COLOR_CORRECT = ColorUtility.darkerColor(Color.GREEN);
    private static final int   INDICATOR_COLOR_NEUTRAL = Color.TRANSPARENT;

    // Spinner Constants
    private static final float SPINNER_RADIUS = 15.0f;
    private static final float ROTATION_INC   = 1.0f;
    private static final float SWEEP_INC      = 12.0f;
    private static final float SWEEP_MAX      = 300.0f;
    private static final float SWEEP_MIN      = 10.0f;

    // State Enum
    public enum State
    {
        STATE_NEUTRAL,
        STATE_CORRECT,
        STATE_ERROR,
        STATE_BUSY;

        private static State[] values = null;

        public static State fromInt(int i)
        {
            if (values == null) {
                values = State.values();
            }

            return State.values[i];
        }
    }

    // Private fields
    private Paint indicatorPaint, spinnerPaint;
    private State state;

    // Spinner
    private RectF spinnerRect                     = new RectF();
    private float sRotation, sEnd, sStart, sTotal = 0;
    private boolean sweepAhead = true;

    // ======= Designated Initializers =======

    public IndicatorEditText(Context context)
    {
        super(context);

        initialize();
    }

    public IndicatorEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        initialize();
    }

    public IndicatorEditText(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        initialize();
    }

    private void initialize()
    {
        indicatorPaint = new Paint();
        indicatorPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/" + "FontAwesome.otf"));
        indicatorPaint.setTextSize(30.0f);
        indicatorPaint.setTextAlign(Paint.Align.CENTER);

        spinnerPaint = new Paint();
        spinnerPaint.setStyle(Paint.Style.STROKE);
        spinnerPaint.setStrokeWidth(5.0f);

        // Set default State
        setState(State.STATE_NEUTRAL);

        // Set Right Padding to make space for the Indicator
        setPadding(getPaddingLeft(), getPaddingTop(), (((int) (getPaddingRight() + (INDICATOR_X_OFFSET * 1.5)))), getPaddingBottom());
    }

    // ======= Getters & Setters =======

    public State getState()
    {
        return state;
    }

    public void setState(State state)
    {
        this.state = state;

        invalidate();
    }

    // ======= Overrides =======
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        float cX = (getMeasuredWidth() - INDICATOR_X_OFFSET);
        float cY = ((getMeasuredHeight() / 2) + getScrollY());


        if (state == State.STATE_BUSY) {
            // if STATE_BUSY, show Spinner

            // Spinner Rotation
            sRotation += ROTATION_INC;
            if (sRotation > 360) sRotation = 0;

            // Spinner Arc Angle
            if (sweepAhead) {
                sEnd += SWEEP_INC;

                if (sTotal > SWEEP_MAX) sweepAhead = false;
            } else {
                sEnd += ROTATION_INC * 2;
                sStart += SWEEP_INC;

                if (sTotal <= SWEEP_MIN) sweepAhead = true;
            }

            sTotal = sEnd - sStart;

            canvas.save();
            canvas.rotate(sRotation, cX, cY);

            spinnerRect.set(cX - SPINNER_RADIUS, cY - SPINNER_RADIUS, cX + SPINNER_RADIUS, cY + SPINNER_RADIUS);

            canvas.drawArc(spinnerRect, sStart, sTotal, false, spinnerPaint);

            canvas.restore();

            invalidate();
        } else if (state == State.STATE_CORRECT) {

            indicatorPaint.setColor(INDICATOR_COLOR_CORRECT);
            canvas.drawText(getContext().getString(R.string.fa_icon_ok), cX, (cY + (indicatorPaint.getTextSize() * 0.35f)), indicatorPaint);
        } else if (state == State.STATE_ERROR) {

            indicatorPaint.setColor(INDICATOR_COLOR_ERROR);
            canvas.drawText(getContext().getString(R.string.fa_icon_warning), cX, (cY + (indicatorPaint.getTextSize() * 0.35f)), indicatorPaint);
        }
    }
}
