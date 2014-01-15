package com.sked.dataexporter;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ScrollView;
import android.widget.TableLayout;

public class ScrollPager implements OnTouchListener {

	

	public ScrollPager(final ScrollView scrollView, final TableLayout contentView) {
		// TODO Auto-generated constructor stub
		/*ScrollView MscrollView;
		TableLayout McontentView;
		McontentView=contentView;
		MscrollView=scrollView;*/
		scrollView.post(new Runnable()
	    {
	      public void run()
	      {
	        scrollView.scrollTo(20, contentView.getPaddingTop());
	      }
	    });
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}
	

}
