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
 * ����ˢ���б�ؼ�</br>
 * ͨ������setonRefreshListener��</br>setOnLoadDataListener��ȷ���ؼ��Ƿ��������ˢ�»��ߵ�����صĹ���
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

	// ʵ�ʵ�padding�ľ����������ƫ�ƾ���ı���
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

	// ���ڱ�֤startY��ֵ��һ��������touch�¼���ֻ����¼һ��
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
		footer_progressBar.setIndeterminate(false);   // ���ý�����״̬����ȷ
		//����β�ؼ�
		addFooterView(footerView);
		//Ĭ�����ؼ�������
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

		//���ͷ�ؼ�
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
		  //����Ҫ��������Ϊ�Ҽ���header footer
        lastItem = firstItemIndex + visibleItemCount - 2;
        this.totalItemCount=totalItemCount;
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int scrollState) {
		 //������ֹͣ�ҹ����������������ݵ�������ȥ����
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
						// ʲô������
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

					// ��֤������padding�Ĺ����У���ǰ��λ��һֱ����head������������б�����Ļ�Ļ����������Ƶ�ʱ���б��ͬʱ���й���

					// ��������ȥˢ����
					if (state == RELEASE_To_REFRESH) {

						setSelection(0);

						// �������ˣ��Ƶ�����Ļ�㹻�ڸ�head�ĳ̶ȣ����ǻ�û���Ƶ�ȫ���ڸǵĵز�
						if (((tempY - startY) / RATIO < headContentHeight)
								&& (tempY - startY) > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();

						}
						// һ�����Ƶ�����
						else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();

						}
						// �������ˣ����߻�û�����Ƶ���Ļ�����ڸ�head�ĵز�
						else {
							// ���ý����ر�Ĳ�����ֻ�ø���paddingTop��ֵ������
						}
					}
					// ��û�е�����ʾ�ɿ�ˢ�µ�ʱ��,DONE������PULL_To_REFRESH״̬
					if (state == PULL_To_REFRESH) {

						setSelection(0);

						// ���������Խ���RELEASE_TO_REFRESH��״̬
						if ((tempY - startY) / RATIO >= headContentHeight) {
							state = RELEASE_To_REFRESH;
							isBack = true;
							changeHeaderViewByState();

						}
						// ���Ƶ�����
						else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();

						}
					}

					// done״̬��
					if (state == DONE) {
						if (tempY - startY > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
					}

					// ����headView��size
					if (state == PULL_To_REFRESH) {
						headView.setPadding(0, -1 * headContentHeight
								+ (tempY - startY) / RATIO, 0, 0);

					}

					// ����headView��paddingTop
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

	// ��״̬�ı�ʱ�򣬵��ø÷������Ը��½���
	private void changeHeaderViewByState() {
		switch (state) {
		case RELEASE_To_REFRESH:
			arrowImageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			arrowImageView.clearAnimation();
			arrowImageView.startAnimation(animation);

			tipsTextview.setText("�ɿ�ˢ��");

			break;
		case PULL_To_REFRESH:
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.VISIBLE);
			// ����RELEASE_To_REFRESH״̬ת������
			if (isBack) {
				isBack = false;
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(reverseAnimation);

				tipsTextview.setText("����ˢ��");
			} else {
				tipsTextview.setText("����ˢ��");
			}
			break;

		case REFRESHING:

			headView.setPadding(0, 0, 0, 0);

			progressBar.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.GONE);
			tipsTextview.setText("����ˢ�²�ѯ���");
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
			tipsTextview.setText("����ˢ��");
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			break;
		}
	}
	// ��״̬�ı�ʱ�򣬵��ø÷������Ը��½���
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

			tipsTextview.setText("�ɿ�ˢ��");

			break;
		case PULL_To_REFRESH:
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.VISIBLE);
			// ����RELEASE_To_REFRESH״̬ת������
			if (isBack) {
				isBack = false;
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(reverseAnimation);

				tipsTextview.setText("����ˢ��");
			} else {
				tipsTextview.setText("����ˢ��");
			}
			break;

		case REFRESHING:

			headView.setPadding(0, 0, 0, 0);

			progressBar.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.GONE);
			tipsTextview.setText("����ˢ��...");
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
			tipsTextview.setText("����ˢ��");
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			break;
		}
	}

	//��������
	public void setOnLoadDataListener(OnLoadDataListener loadDataListener) {
		this.onLoadDataListener =loadDataListener;
		footerView.setVisibility(VISIBLE);
	}
	//��������
	private OnClickListener onfooterViewOnClickListener =new OnClickListener() {

		@Override
		public void onClick(View v) {
			footer_tipsTextview.setText("���ڼ�����...");
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
		footer_tipsTextview.setText("���ظ���");
		footer_progressBar.setVisibility(View.GONE);
		footerView.setClickable(true);
		if(custemHeaderLoadView!=null){
			headView.findViewById(R.id.head_contentLayout).setVisibility(View.VISIBLE);
			custemHeaderLoadView.setVisibility(View.GONE);
		}
	}


	//ˢ��
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
		SimpleDateFormat format=new SimpleDateFormat("yyyy��MM��dd��  HH:mm");
		String date=format.format(new Date());
		lastUpdatedTextView.setText("�������:" + date);
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

	//����headView��width�Լ�height
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
		SimpleDateFormat format=new SimpleDateFormat("yyyy��MM��dd��  HH:mm");
		String date=format.format(new Date());
		lastUpdatedTextView.setText("�������:" + date);
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
	 * �����Զ������ڼ��ص���ͼ
	 * @param view
	 */
	public void setCustemHeaderLoadView(View view) {
		custemHeaderLoadView=view;
		headView.addView(view);
	}
	
	/**
	 * ���û���ʱ��
	 * @param duration
	 */
	public void setDuration(int duration ) {
		this.duration=duration;
	}

}
