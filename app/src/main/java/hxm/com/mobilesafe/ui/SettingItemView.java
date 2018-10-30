package hxm.com.mobilesafe.ui;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import hxm.com.mobilesafe.R;

/**
 * @author Administrator
 * 我们自定义的组合控件，它里面有两个TextView ，还有一个CheckBox,还有一个View
 */
public class SettingItemView extends RelativeLayout {
	
	private TextView title;
	private TextView content;
	private CheckBox checkbox;

	public void initView(Context context){
		//把一个布局文件---》View 并且加载在SettingItemView
		View view = View.inflate(context, R.layout.setting_item, this);
		title = (TextView)view.findViewById(R.id.tilte_tv);
		content = (TextView)view.findViewById(R.id.content_tv);
		checkbox = (CheckBox)view.findViewById(R.id.checkbox_ck);
	}

	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
		String title_str = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "title");
		//String content_on_str = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "content_on");
		String content_off_str = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "content_off");
		setTitle(title_str);
		setContent(content_off_str);
	}

	public void setTitle(String title) {
		this.title.setText(title);
	}

	public void setContent(String content) {
		this.content.setText(content);
	}

	public void setChecked(boolean checked) {

		this.checkbox.setChecked(checked);
	}
	public boolean isChecked(){
		return checkbox.isChecked();
	}
	
}
