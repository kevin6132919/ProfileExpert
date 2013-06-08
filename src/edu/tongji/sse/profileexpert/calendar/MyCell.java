package edu.tongji.sse.profileexpert.calendar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class MyCell {

	//private static final String TAG = "Cell";
	protected Rect rect = null;
	protected int dayOfMonth = 1;	// from 1 to 31
	private Paint paint = new Paint(Paint.SUBPIXEL_TEXT_FLAG
            |Paint.ANTI_ALIAS_FLAG);
	private int dx, dy;
	
	public MyCell(int dayOfMon, Rect rect, float textSize, boolean bold)
	{
		dayOfMonth = dayOfMon;
		this.rect = rect;
		getPaint().setTextSize(textSize/*26f*/);
		getPaint().setColor(Color.BLACK);
		if(bold) getPaint().setFakeBoldText(true);
		
		dx = (int) getPaint().measureText(String.valueOf(dayOfMonth)) / 2;
		dy = (int) (-getPaint().ascent() + getPaint().descent()) / 2;
	}
	
	public MyCell(int dayOfMon, Rect rect, float textSize)
	{
		this(dayOfMon, rect, textSize, false);
	}
	
	protected void draw(Canvas canvas)
	{
		//Paint _paint = new Paint(Paint.SUBPIXEL_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
		float x = rect.centerX() - dx;
		float y = rect.centerY() + dy;
		canvas.drawText(String.valueOf(dayOfMonth), x, y, getPaint());
	}
	
	public int getDayOfMonth(){
		return dayOfMonth;
	}
	
	public boolean hitTest(int x, int y) {
		return rect.contains(x, y);
	}
	
	public Rect getBound() {
		return rect;
	}
	
	public String toString() {
		return String.valueOf(dayOfMonth)+"("+rect.toString()+")";
	}

	public Paint getPaint() {
		return paint;
	}

	public void setPaint(Paint paint) {
		this.paint = paint;
	}

}
