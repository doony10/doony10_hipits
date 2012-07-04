package hipits.com;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Checkable;
import android.widget.RelativeLayout;

public class CheckableRelativeLayout extends RelativeLayout implements Checkable {
	final String NS = "http://schemas.android.com/apk/res/hipits.com";
	final String ATTR = "checkable";

	int checkableId;
	Checkable checkable;

	public CheckableRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		checkableId = attrs.getAttributeResourceValue(NS, ATTR, 0);
		Log.v("checkable", "1");
	}

	public boolean isChecked() {
		checkable = (Checkable) findViewById(checkableId);
		if (checkable == null){
			Log.v("checkable", "2");
			return false;
		}
		Log.v("checkable", "2.5");
		return checkable.isChecked();
	}

	public void setChecked(boolean checked) {
		checkable = (Checkable) findViewById(checkableId);
		if (checkable == null){
			Log.v("checkable", "3");			
			return;
		}
		Log.v("checkable", "3.5");
		checkable.setChecked(checked);
		Log.v("checkable", "3.8");
	}

	public void toggle() {
		checkable = (Checkable) findViewById(checkableId);
		if (checkable == null){
			Log.v("checkable", "4");
			return;
		}
		Log.v("checkable", "4.5");
		checkable.toggle();
	}
}
