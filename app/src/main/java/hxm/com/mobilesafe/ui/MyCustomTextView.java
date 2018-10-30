package hxm.com.mobilesafe.ui;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;


/**
 * @author Administrator
 * 自定一个TextView 一出生就有焦点
 */
public class MyCustomTextView extends AppCompatTextView {

	public MyCustomTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyCustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyCustomTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 当前并没有焦点，我只是欺骗了Android系统
	 */
	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		// TODO Auto-generated method stub
		return true;
	}
}
