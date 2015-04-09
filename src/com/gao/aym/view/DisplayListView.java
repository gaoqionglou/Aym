package com.gao.aym.view;


import java.text.SimpleDateFormat;
import java.util.Date;






import com.gao.aym.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 下拉刷新列表控件</br>
 * 通过设置setonRefreshListener，</br>setOnLoadDataListener来确定控件是否具有下拉刷新或者点击加载的功能
 * @version 1.0
 */
@SuppressLint("SimpleDateFormat")
public class DisplayListView extends ListView implements OnScrollListener {



	public final static int RELEASE_To_REFRESH = 0;
	public final static int PULL_To_REFRESH = 1;
	public final static int REFRESHING = 2;
	public final static int DONE = 3;
	public final static int LOADING = 4;
	private int minAutoLoadCount=10;

	// 实际的padding的距离与界面上偏移距离的比例
	private final static int RATIO = 3;

	private LayoutInflater inflater;

	private LinearLayout headView;
	public LinearLayout getHeadView() {
		return headView;
	}
	private LinearLayout footerView;
 
	private TextView tipsTextview;
	private TextView footer_tipsTextview;
	private TextView lastUpdatedTextView;
	private ImageView arrowImageView;
	private ProgressBar progressBar;
	private ProgressBar footer_progressBar;


	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;

	// 用于保证startY的值在一个完整的touch事件中只被记录一次
	private boolean isRecored;

	@SuppressWarnings("unused")
	private int headContentWidth;
	private int headContentHeight;

	private int startY;
	private int firstItemIndex;

	private int state;

	private boolean isBack;

	private OnRefreshListener refreshListener;

	private boolean isRefreshable;

	private OnLoadDataListener onLoadDataListener;

	private int lastItem;

	private int totalItemCount;
	
	private boolean isLoading;



	public DisplayListView(Context context) {
		super(context);
		init(context);
	}

	public DisplayListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		
		//setCacheColorHint(context.getResources().getColor(R.color.transparent));
		inflater = LayoutInflater.from(context);
		headView = (LinearLayout) inflater.inflate(R.layout.listview_head, null);

		footerView= (LinearLayout) inflater.inflate(R.layout.listview_footer, null);
		footerView.setClickable(false);
		footer_tipsTextview=(TextView) footerView.findViewById(R.id.footer_tipsTextView);
		footer_progressBar =(ProgressBar) footerView.findViewById(R.id.footer_progressBar);
		footer_progressBar.setIndeterminate(false);   // 设置进度条状态不明确
		//加载尾控件
		addFooterView(footerView);
		//默认隐藏加载数据
		footerView.setVisibility(GONE);

		footerView.setOnClickListener(onfooterViewOnClickListener);

		arrowImageView = (ImageView) headView
				.findViewById(R.id.head_arrowImageView);
		arrowImageView.setMinimumWidth(70);
		arrowImageView.setMinimumHeight(50);
		progressBar = (ProgressBar) headView
				.findViewById(R.id.head_progressBar);
		progressBar.setIndeterminate(false);
		tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
		lastUpdatedTextView = (TextView) headView
				.findViewById(R.id.head_lastUpdatedTextView);

		//添加头控件
		measureView(headView);
		headContentHeight = headView.getMeasuredHeight();
		headContentWidth = headView.getMeasuredWidth();

		headView.setPadding(0, -1 * headContentHeight, 0, 0);
		headView.invalidate();

		addHeaderView(headView, null, false);
		setOnScrollListener(this);

		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);

		state = DONE;
		isRefreshable = false;
	}

	public LinearLayout getFooterView() {
		return footerView;
	}

	public void onScroll(AbsListView arg0, int firstVisiableItem, int visibleItemCount,
			int totalItemCount) {
		firstItemIndex = firstVisiableItem;
		  //这里要减二，因为我加了header footer
        lastItem = firstItemIndex + visibleItemCount - 2;
        this.totalItemCount=totalItemCount;
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int scrollState) {
		 //当滚动停止且滚动的总数等于数据的总数，去加载
        if (lastItem>minAutoLoadCount-1&&lastItem == totalItemCount-2 && scrollState == SCROLL_STATE_IDLE) {
        	if(!isLoading){
        		isLoading=true;
            	onfooterViewOnClickListener.onClick(null);
        	}
        }
		
	}

	public boolean onTouchEvent(MotionEvent event) {

		if (isRefreshable) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (firstItemIndex == 0 && !isRecored) {
					isRecored = true;
					startY = (int) event.getY();
				}
				break;

			case MotionEvent.ACTION_UP:

				if (state != REFRESHING && state != LOADING) {
					if (state == DONE) {
						// 什么都不做
					}
					if (state == PULL_To_REFRESH) {
						state = DONE;
						changeHeaderViewByState();

					}
					if (state == RELEASE_To_REFRESH) {
						state = REFRESHING;
						if(duration!=0){
							smoothScrollBy((int) Math.abs(startY-event.getY()), duration);
						}
						changeHeaderViewByState();
						onRefresh();

					}
				}

				isRecored = false;
				isBack = false;

				break;

			case MotionEvent.ACTION_MOVE:
				int tempY = (int) event.getY();

				if (!isRecored && firstItemIndex == 0) {
					isRecored = true;
					startY = tempY;
				}

				if (state != REFRESHING && isRecored && state != LOADING) {

					// 保证在设置padding的过程中，当前的位置一直是在head，否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动

					// 可以松手去刷新了
					if (state == RELEASE_To_REFRESH) {

						setSelection(0);

						// 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
						if (((tempY - startY) / RATIO < headContentHeight)
								&& (tempY - startY) > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();

						}
						// 一下子推到顶了
						else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();

						}
						// 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
						else {
							// 不用进行特别的操作，只用更新paddingTop的值就行了
						}
					}
					// 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
					if (state == PULL_To_REFRESH) {

						setSelection(0);

						// 下拉到可以进入RELEASE_TO_REFRESH的状态
						if ((tempY - startY) / RATIO >= headContentHeight) {
							state = RELEASE_To_REFRESH;
							isBack = true;
							changeHeaderViewByState();

						}
						// 上推到顶了
						else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();

						}
					}

					// done状态下
					if (state == DONE) {
						if (tempY - startY > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
					}

					// 更新headView的size
					if (state == PULL_To_REFRESH) {
						headView.setPadding(0, -1 * headContentHeight
								+ (tempY - startY) / RATIO, 0, 0);

					}

					// 更新headView的paddingTop
					if (state == RELEASE_To_REFRESH) {
						headView.setPadding(0, (tempY - startY) / RATIO
								- headContentHeight, 0, 0);
					}

				}

				break;
			}
		}

		return super.onTouchEvent(event);
	}

	// 当状态改变时候，调用该方法，以更新界面
	private void changeHeaderViewByState() {
		switch (state) {
		case RELEASE_To_REFRESH:
			arrowImageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			arrowImageView.clearAnimation();
			arrowImageView.startAnimation(animation);

			tipsTextview.setText("松开刷新");

			break;
		case PULL_To_REFRESH:
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.VISIBLE);
			// 是由RELEASE_To_REFRESH状态转变来的
			if (isBack) {
				isBack = false;
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(reverseAnimation);

				tipsTextview.setText("下拉刷新");
			} else {
				tipsTextview.setText("下拉刷新");
			}
			break;

		case REFRESHING:

			headView.setPadding(0, 0, 0, 0);

			progressBar.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.GONE);
			tipsTextview.setText("正在刷新查询结果");
			lastUpdatedTextView.setVisibility(View.GONE);
			if(custemHeaderLoadView!=null){
				headView.findViewById(R.id.head_contentLayout).setVisibility(View.GONE);
				custemHeaderLoadView.setVisibility(View.VISIBLE);
			}
			break;
		case DONE:
			headView.setPadding(0, -1 * headContentHeight, 0, 0);

			progressBar.setVisibility(View.GONE);
			arrowImageView.clearAnimation();
			arrowImageView.setImageResource(R.drawable.indicator_arrow);
			tipsTextview.setText("下拉刷新");
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			break;
		}
	}
	// 当状态改变时候，调用该方法，以更新界面
	public void changeHeaderViewByState(int _state) {
		try {
			setSelection(0);
		} catch (Exception e) {
		}
		switch (_state) {
		case RELEASE_To_REFRESH:
			arrowImageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			arrowImageView.clearAnimation();
			arrowImageView.startAnimation(animation);

			tipsTextview.setText("松开刷新");

			break;
		case PULL_To_REFRESH:
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.VISIBLE);
			// 是由RELEASE_To_REFRESH状态转变来的
			if (isBack) {
				isBack = false;
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(reverseAnimation);

				tipsTextview.setText("下拉刷新");
			} else {
				tipsTextview.setText("下拉刷新");
			}
			break;

		case REFRESHING:

			headView.setPadding(0, 0, 0, 0);

			progressBar.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.GONE);
			tipsTextview.setText("正在刷新...");
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			if(custemHeaderLoadView!=null){
				headView.findViewById(R.id.head_contentLayout).setVisibility(View.GONE);
				custemHeaderLoadView.setVisibility(View.VISIBLE);
			}
			break;
		case DONE:
			headView.setPadding(0, -1 * headContentHeight, 0, 0);

			progressBar.setVisibility(View.GONE);
			arrowImageView.clearAnimation();
			arrowImageView.setImageResource(R.drawable.arrow_down);
			tipsTextview.setText("下拉刷新");
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			break;
		}
	}

	//加载数据
	public void setOnLoadDataListener(OnLoadDataListener loadDataListener) {
		this.onLoadDataListener =loadDataListener;
		footerView.setVisibility(VISIBLE);
	}
	//加载数据
	private OnClickListener onfooterViewOnClickListener =new OnClickListener() {

		@Override
		public void onClick(View v) {
			footer_tipsTextview.setText("正在加载中...");
			footer_progressBar.setVisibility(View.VISIBLE);
			footerView.setClickable(false);
			if(onLoadDataListener!=null){
				onLoadDataListener.onLoad();
			}
		}
	};
	private View custemHeaderLoadView;
	private int duration;

	
	public void onLoadComplete() {
		isLoading=false;
		footer_tipsTextview.setText("加载更多");
		footer_progressBar.setVisibility(View.GONE);
		footerView.setClickable(true);
		if(custemHeaderLoadView!=null){
			headView.findViewById(R.id.head_contentLayout).setVisibility(View.VISIBLE);
			custemHeaderLoadView.setVisibility(View.GONE);
		}
	}


	//刷新
	public void setonRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
		isRefreshable = true;
	}

	public interface OnRefreshListener {
		public void onRefresh();
	}

	public interface OnLoadDataListener {
		public void onLoad();
	}

	public void onRefreshComplete() {
		state = DONE;
		SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
		String date=format.format(new Date());
		lastUpdatedTextView.setText("最近更新:" + date);
		changeHeaderViewByState();
		if(custemHeaderLoadView!=null){
			custemHeaderLoadView.setVisibility(View.GONE);
			headView.findViewById(R.id.head_contentLayout).setVisibility(View.VISIBLE);

		}
	}

	private void onRefresh() {
		if (refreshListener != null) {
			refreshListener.onRefresh();
		}
	}

	//估计headView的width以及height
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0,
				0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.UNSPECIFIED);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	public void setAdapter(BaseAdapter adapter) {
		SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
		String date=format.format(new Date());
		lastUpdatedTextView.setText("最近更新:" + date);
		super.setAdapter(adapter);
	}

	/**
	 * @return the minAutoLoadCount
	 */
	public int getMinAutoLoadCount() {
		return minAutoLoadCount;
	}

	/**
	 * @param minAutoLoadCount the minAutoLoadCount to set
	 */
	public void setMinAutoLoadCount(int minAutoLoadCount) {
		this.minAutoLoadCount = minAutoLoadCount;
	}

	/**
	 * 设置自定义正在加载的视图
	 * @param view
	 */
	public void setCustemHeaderLoadView(View view) {
		custemHeaderLoadView=view;
		headView.addView(view);
	}
	
	/**
	 * 设置滑动时间
	 * @param duration
	 */
	public void setDuration(int duration ) {
		this.duration=duration;
	}

}
