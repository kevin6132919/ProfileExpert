package edu.tongji.sse.profileexpert.control;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class MatterView extends View
{
	private String text = null;
	Paint paint = new Paint(Paint.SUBPIXEL_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
	
	public MatterView(Context context,String text)
	{
		super(context);
		this.text = text;
	}
	
	public MatterView(Context context)
	{
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
	    paint.setColor(Color.BLACK);                    //…Ë÷√ª≠± —’…´  
	    paint.setStrokeWidth((float) 3.0);              //œﬂøÌ  
	    canvas.drawText(text, 20, 20, paint);
	}
}
