package hxm.com.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.Toast;

public abstract class GeneralGuideActivity extends Activity {
	//定义一个手挚识别器
	private GestureDetector gd ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//实例化手挚识别器
		gd = new GestureDetector(this, new SimpleOnGestureListener(){
			
			/**
			 * 当我们的手指在上面滑动的时候回调
			 * e1：手势起点的移动事件
				e2：当前手势点的移动事件
				velocityX： 每秒x轴方向移动的像素
				velocityY： 每秒y轴方向移动的像素
			 */
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				
				//屏蔽慢滑
				if(Math.abs(velocityX)<200){
					Toast.makeText(getApplicationContext(), "亲，滑快点哦！",Toast.LENGTH_LONG).show();
					return true;
				}
				//屏蔽斜滑
				if(Math.abs(e1.getRawY()-e2.getRawY())>200){
					Toast.makeText(getApplicationContext(), "亲，不可以斜着滑哦！",Toast.LENGTH_LONG).show();
					return true;
				}
				
				//从右向左滑动==》显示下一页
				if(e1.getRawX()-e2.getRawX()>200){
					showNext();
				}
				//从左向右滑动=》显示上一页
				if(e2.getRawX()-e1.getRawX()>200){
					showPrev();
				}
				
				return super.onFling(e1, e2, velocityX, velocityY);
			}	
		});
	}
	public abstract void showPrev(); 
	public abstract void showNext();
	
	//作用手挚识别器
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		gd.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	
	//button 下键的响应
	public void next(View view){
		showNext();
	}
	//button 上键的响应
	public void prev(View view){
		showPrev();
	}
}
