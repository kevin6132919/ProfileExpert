package edu.tongji.sse.profileexpert.help;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;


public class ViewPagerAdapter extends PagerAdapter
{

	private Context context;
	private JSONArray jsonArray;
	private HashMap<Integer, ViewPagerItemView> hashMap;//Hashmap保存相片的位置以及ItemView.
	
	
	@SuppressLint("UseSparseArrays")
	public ViewPagerAdapter(Context context,JSONArray arrays)
	{
		this.context = context;
		this.jsonArray = arrays;
		hashMap = new HashMap<Integer, ViewPagerItemView>();
	}
	
	//这里进行回收，当我们左右滑动的时候，会把早期的图片回收掉.
	@Override
	public void destroyItem(View container, int position, Object object) {
		ViewPagerItemView itemView = (ViewPagerItemView)object;
		itemView.recycle();
	}
	
	@Override
	public void finishUpdate(View view) {

	}

	//这里返回相册有多少条,和BaseAdapter一样.
	@Override
	public int getCount() {
		return jsonArray.length();
	}

	//这里就是初始化ViewPagerItemView.如果ViewPagerItemView已经存在,
	//重新reload，不存在new一个并且填充数据.
	@Override
	public Object instantiateItem(View container, int position)
	{	
		ViewPagerItemView itemView;
		if(hashMap.containsKey(position)){
			itemView = hashMap.get(position);
			itemView.reload();
		}else{
			itemView = new ViewPagerItemView(context);
			try {
				JSONObject dataObj = (JSONObject) jsonArray.get(position);
				itemView.setData(dataObj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			hashMap.put(position, itemView);
			((ViewPager) container).addView(itemView);
		}
		
		return itemView;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View view) {

	}
}
