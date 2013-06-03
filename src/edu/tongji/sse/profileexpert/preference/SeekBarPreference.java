package edu.tongji.sse.profileexpert.preference;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class SeekBarPreference extends DialogPreference implements SeekBar.OnSeekBarChangeListener
{
	private static final String androidns="http://schemas.android.com/apk/res/android";

	private SeekBar seekBar;
	private TextView splashText,valueText;
	private Context context;

	private String dialogMessage, suffix;
	private int _default, _max, _value, temp_progress = 0;

	public SeekBarPreference(Context context, AttributeSet attrs) { 
		super(context,attrs); 
		this.context = context;

		dialogMessage = attrs.getAttributeValue(androidns,"dialogMessage");
		suffix = attrs.getAttributeValue(androidns,"text");
		_default = attrs.getAttributeIntValue(androidns,"defaultValue", 0);
		_max = attrs.getAttributeIntValue(androidns,"max", 100);

	}
	@SuppressWarnings("deprecation")
	@Override 
	protected View onCreateDialogView() {
		LinearLayout.LayoutParams params;
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setPadding(6,6,6,6);

		splashText = new TextView(context);
		if (dialogMessage != null)
			splashText.setText(dialogMessage);
		layout.addView(splashText);

		valueText = new TextView(context);
		valueText.setGravity(Gravity.CENTER_HORIZONTAL);
		valueText.setTextSize(32);
		valueText.setText(suffix == null ? ""+_default : _default+suffix);
		params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, 
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layout.addView(valueText, params);

		seekBar = new SeekBar(context);
		seekBar.setOnSeekBarChangeListener(this);
		layout.addView(seekBar, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

		if (shouldPersist())
			_value = getPersistedInt(_default);

		seekBar.setMax(_max);
		seekBar.setProgress(_value);
		return layout;
	}
	@Override 
	protected void onBindDialogView(View v) {
		super.onBindDialogView(v);
		seekBar.setMax(_max);
		seekBar.setProgress(_value);
	}
	@Override
	protected void onSetInitialValue(boolean restore, Object defaultValue)  
	{
		super.onSetInitialValue(restore, defaultValue);
		if (restore) 
			_value = shouldPersist() ? getPersistedInt(_default) : 0;
			else 
				_value = (Integer)defaultValue;
	}

	@SuppressLint("UseValueOf")
	public void onProgressChanged(SeekBar seek, int value, boolean fromTouch)
	{
		String t = String.valueOf(value);
		valueText.setText(suffix == null ? t : t.concat(suffix));
		if (shouldPersist())
			persistInt(value);
		callChangeListener(new Integer(value));
	}
	public void onStartTrackingTouch(SeekBar seek) {}
	public void onStopTrackingTouch(SeekBar seek) {}

	public void setMax(int max) { _max = max; }
	public int getMax() { return _max; }

	public void setProgress(int progress) { 
		_value = progress;
		if (seekBar != null)
			seekBar.setProgress(progress); 
	}
	public int getProgress() { return _value; }
	
	@Override
	protected void onDialogClosed(boolean positiveResult)
	{
		if(!positiveResult)
			this.setProgress(temp_progress);
		super.onDialogClosed(positiveResult);
	}
	@Override
	protected void onPrepareDialogBuilder(Builder builder)
	{
		temp_progress = _value;
		super.onPrepareDialogBuilder(builder);
	}
}
