package edu.tongji.sse.profileexpert.calendar;

import java.util.Calendar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.MonthDisplayHelper;
import android.widget.ImageView;
import edu.tongji.sse.profileexpert.R;

public class MyCalendarView extends ImageView
{
    private static int WEEK_TOP_MARGIN = 33;
    private static int WEEK_LEFT_MARGIN = 50;
    private static int CELL_WIDTH = 58;
    private static int CELL_HEIGH = 53;
    private static int CELL_MARGIN_TOP = 51;
    private static int CELL_MARGIN_LEFT = 41;
    private static float CELL_TEXT_SIZE = 36;
    
    // -1 为上月  1为下月  0为此月
    public static final int CURRENT_MOUNT = 0;
    public static final int NEXT_MOUNT = 1;
    public static final int PREVIOUS_MOUNT = -1;
    public static final double background_aspect_ratio = 1.159;
	
	private Context context = null;
	private Calendar rightNow = null;
	private MonthDisplayHelper helper = null;

    private Drawable decoration = null;
    private Drawable weekTitle = null;
    private Drawable decoraClick = null;
	
    private MyCell[][] cells = new MyCell[6][7];
    private MyCell today_cell = null;
	
	public MyCalendarView(Context context)
	{
		this(context, null);
	}
	public MyCalendarView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public MyCalendarView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		this.context = context;
		decoration = context.getResources().getDrawable(R.drawable.calendar_today);
		decoraClick = context.getResources().getDrawable(R.drawable.calendar_today);
		initCalendarView();
	}
	
	//初始化
	private void initCalendarView()
	{

		rightNow = Calendar.getInstance();
		// prepare static vars
		Resources res = getResources();
		
		// set background
		setImageResource(R.drawable.calendar_background);
		weekTitle = res.getDrawable(R.drawable.calendar_week);
		helper = 
				new MonthDisplayHelper(
						rightNow.get(Calendar.YEAR),
						rightNow.get(Calendar.MONTH),
						rightNow.getFirstDayOfWeek()
					);
	}
	

	private void initCells() {
	    class _calendar {
	    	public int day;
	    	public int whichMonth;  // -1 为上月  1为下月  0为此月
	    	public _calendar(int d, int b) {
	    		day = d;
	    		whichMonth = b;
	    	}
	    	public _calendar(int d) { // 上个月 默认为
	    		this(d, PREVIOUS_MOUNT);
	    	}
	    };
	    _calendar tmp[][] = new _calendar[6][7];
	    
	    for(int i=0; i<tmp.length; i++) {
	    	int n[] = helper.getDigitsForRow(i);
	    	for(int d=0; d<n.length; d++) {
	    		if(helper.isWithinCurrentMonth(i,d))
	    			tmp[i][d] = new _calendar(n[d], CURRENT_MOUNT);
	    		else if(i == 0) {
	    			tmp[i][d] = new _calendar(n[d]);
	    		} else {
	    			tmp[i][d] = new _calendar(n[d], NEXT_MOUNT);
	    		}
	    		
	    	}
	    }

	    Calendar today = Calendar.getInstance();
	    int thisDay = 0;
	    today_cell = null;
	    if(helper.getYear()==today.get(Calendar.YEAR) && helper.getMonth()==today.get(Calendar.MONTH)) {
	    	thisDay = today.get(Calendar.DAY_OF_MONTH);
	    }
		// build cells
		Rect Bound = new Rect(CELL_MARGIN_LEFT, CELL_MARGIN_TOP, CELL_WIDTH+CELL_MARGIN_LEFT, CELL_HEIGH+CELL_MARGIN_TOP);
		for(int week=0; week<cells.length; week++) {
			for(int day=0; day<cells[week].length; day++) {
				if(tmp[week][day].whichMonth == CURRENT_MOUNT) { // 此月 
					if(day==0 || day==6 )
						cells[week][day] = new RedCell(tmp[week][day].day, new Rect(Bound), CELL_TEXT_SIZE);
					else 
						cells[week][day] = new MyCell(tmp[week][day].day, new Rect(Bound), CELL_TEXT_SIZE);
				} else if(tmp[week][day].whichMonth == PREVIOUS_MOUNT) {  // 上月为gray
					cells[week][day] = new GrayCell(tmp[week][day].day, new Rect(Bound), CELL_TEXT_SIZE);
				} else { // 下月为LTGray
					cells[week][day] = new LTGrayCell(tmp[week][day].day, new Rect(Bound), CELL_TEXT_SIZE);
				}
				
				Bound.offset(CELL_WIDTH, 0); // 移动至下一列
				
				// get today
				if(tmp[week][day].day==thisDay && tmp[week][day].whichMonth == 0) {
					today_cell = cells[week][day];
					
					//对今天的符号的位置进行修正
					Rect today_bounds = today_cell.getBound();
					int today_left = today_bounds.left;
					int today_top = today_bounds.top + CELL_HEIGH/10;
					int today_right = today_bounds.right;
					int today_bottom = today_bounds.bottom + CELL_HEIGH/10;
					decoration.setBounds(new Rect(today_left, today_top, today_right, today_bottom));
				}
			}
			Bound.offset(0, CELL_HEIGH); // 移动到下一行的第一列
			Bound.left = CELL_MARGIN_LEFT;
			Bound.right = CELL_MARGIN_LEFT+CELL_WIDTH;
		}		
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		// draw background
		super.onDraw(canvas);
		
		weekTitle.draw(canvas);

		//测试
		/*Paint paint = new Paint(Paint.SUBPIXEL_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
		canvas.drawText("test", 100, 100, paint);*/
		
		// draw cells
		for(MyCell[] week : cells) {
			for(MyCell day : week) {
				day.draw(canvas);
			}
		}
		
		// draw today
		if(decoration!=null && today_cell!=null) {
			decoration.draw(canvas);
		}
		if(decoraClick.getBounds()!=null) {
			decoraClick.draw(canvas);
			// 设置这里过后  要想办法
			decoraClick = context.getResources().getDrawable(R.drawable.calendar_today);
//			mDecoraClick.setBounds(null);
		}
	}
	
	//在绘制之前设置MyCell的属性
	private void setCellProperties()
	{
		ImageView view = (ImageView)findViewById(R.id.my_calendar);
		//int width = view.getWidth();
		int height = view.getHeight();
		//由于得到的宽度不是实际的宽度
		//故通过计算得出calendar的宽度
		int width = (int) (height * background_aspect_ratio);
		
		WEEK_TOP_MARGIN = height/15;
	    //WEEK_LEFT_MARGIN = 0;
	    CELL_MARGIN_TOP = height/10;
	    //CELL_MARGIN_LEFT = view.getLeft();
	    CELL_WIDTH = (int) (width/7.2);
	    CELL_HEIGH = (int) (height*0.15);
	    CELL_TEXT_SIZE = getResources().getDimension(R.dimen.calendar_text_size);
		
	}
	
	@Override
	public void onLayout(boolean changed, int left, int top, int right, int bottom) {
		/*android.util.Log.d(TAG, "left="+left);*/
		Rect re = getDrawable().getBounds();
		WEEK_LEFT_MARGIN = CELL_MARGIN_LEFT = (right-left - re.width()) / 2;
		setCellProperties();
		weekTitle.setBounds(
				WEEK_LEFT_MARGIN,
				WEEK_TOP_MARGIN,
				WEEK_LEFT_MARGIN+weekTitle.getMinimumWidth(),
				WEEK_TOP_MARGIN+weekTitle.getMinimumHeight());
		initCells();
		super.onLayout(changed, left, top, right, bottom);
	}	
	
	private class GrayCell extends MyCell {
		public GrayCell(int dayOfMon, Rect rect, float s) {
			super(dayOfMon, rect, s);
			paint.setColor(Color.GRAY);
		}			
	}
	
	private class LTGrayCell extends MyCell {
		public LTGrayCell(int dayOfMon, Rect rect, float s) {
			super(dayOfMon, rect, s);
			paint.setColor(Color.LTGRAY);
		}			
	}
	
	private class RedCell extends MyCell {
		public RedCell(int dayOfMon, Rect rect, float s) {
			super(dayOfMon, rect, s);
			paint.setColor(0xdddd0000);
		}			
		
	}
}
