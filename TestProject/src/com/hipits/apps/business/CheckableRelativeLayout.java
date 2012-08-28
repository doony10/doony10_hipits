package com.hipits.apps.business;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Checkable;
import android.widget.RelativeLayout;

public class CheckableRelativeLayout extends RelativeLayout implements Checkable {
	final String NS = "http://schemas.android.com/apk/res/com.hipits.apps.business";
	final String ATTR = "checkable";

	int checkableId;
	Checkable checkable;

	public CheckableRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		checkableId = attrs.getAttributeResourceValue(NS, ATTR, 0);
	}

	public boolean isChecked() {
		checkable = (Checkable) findViewById(checkableId);
		if (checkable == null){
			return false;
		}
		return checkable.isChecked();
	}

	public void setChecked(boolean checked) {
		checkable = (Checkable) findViewById(checkableId);
		if (checkable == null){
			return;
		}
		checkable.setChecked(checked);
	}

	public void toggle() {
		checkable = (Checkable) findViewById(checkableId);
		if (checkable == null){
			return;
		}
		checkable.toggle();
	}
}
