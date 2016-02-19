package com.azzdorfrobotics.android.legstep.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.azzdorfrobotics.android.legstep.BaseActivity;
import com.azzdorfrobotics.android.legstep.R;
import com.azzdorfrobotics.android.legstep.helpers.Common;

/**
 * Created on 17.02.2015
 *
 * @author Mykola Tychyna (iMykolaPro)
 */
public class InlineNumberPicker extends LinearLayout {

    private static final String INITIAL_FORMAT = "%d";

    private ImageButton minusButton;
    private TextView valueText;
    private ImageButton plusButton;

    private int minValue = Integer.MIN_VALUE;
    private int maxValue = Integer.MAX_VALUE;
    private int stepSize = 1;

    private OnValueChanged onChanged;

    /**
     * Current value
     */
    private int value = 0;

    /**
     * Widget uses this format to display a value
     */
    private String format = INITIAL_FORMAT;

    public InlineNumberPicker(Context context) {
        super(context);
        init(null);
    }

    public InlineNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public InlineNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    protected void init(AttributeSet attrs) {
        View.inflate(getContext(), R.layout.widget_inline_number_picker, this);

        minusButton = (ImageButton) findViewById(R.id.minus_button);
        minusButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setValue(value - stepSize);
            }
        });
        valueText = (TextView) findViewById(R.id.value_text);
        plusButton = (ImageButton) findViewById(R.id.plus_button);
        plusButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setValue(value + stepSize);
            }
        });

        if (attrs != null) {
            TypedArray styles = getContext().obtainStyledAttributes(attrs, R.styleable.InlineNumberPicker);
            try {
                if (styles.hasValue(R.styleable.InlineNumberPicker_format)) {
                    setFormat(styles.getString(R.styleable.InlineNumberPicker_format));
                }
                setMinValue(styles.getInt(R.styleable.InlineNumberPicker_minValue, 0));
                setMaxValue(styles.getInt(R.styleable.InlineNumberPicker_maxValue, Integer.MAX_VALUE));
                setValue(styles.getInt(R.styleable.InlineNumberPicker_numberValue, minValue));
                setStepSize(styles.getInt(R.styleable.InlineNumberPicker_stepSize, stepSize));
                setEnabled(styles.getBoolean(R.styleable.InlineNumberPicker_enabled, true));
            } finally {
                styles.recycle();
            }
        }
    }

    /**
     * Checks & sets current value
     *
     * @param value
     */
    public void setValue(int value) {
        /**
         * Respect range
         */
        if (value > maxValue) {
            value = maxValue;
        } else if (value < minValue) {
            value = minValue;
        }

        this.value = value;
        valueText.setText(formatValue(value));

        // invoke callback if set
        if (onChanged != null) {
            onChanged.onChange(value);
        }
    }

    public int getValue() {
        return value;
    }

    public void setMinValue(int min) {
        minValue = min;
    }

    public void setMaxValue(int max) {
        maxValue = max;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public float getStepSize() {
        return stepSize;
    }

    public void setStepSize(int stepSize) {
        this.stepSize = stepSize;
    }

    public String formatValue(int value) {
        return Common.formatString(format, value);
    }

    public void setOnChanged(OnValueChanged onChanged) {
        this.onChanged = onChanged;
    }

    /**
     * For callback
     */
    public interface OnValueChanged {
        void onChange(int newValue);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        minusButton.setEnabled(enabled);
        plusButton.setEnabled(enabled);
    }
}
