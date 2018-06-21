package com.ttt.liveroom.widget.getfriendview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ttt.liveroom.R;
import com.ttt.liveroom.bean.GetFriendBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 刘景 on 2017/06/11.
 */

public class GetFriendLayout extends RelativeLayout {

    private RecyclerView recyclerView;
    private Context context;
    private GetFriendAdapter adapter;

    //    一横排显示个数
    private static final int DEFUALT_COLUMN = 4;
    private static final int DEFUALT_SRC_SELECT = R.drawable.ic_page_indicator_focused;
    private static final int DEFUALT_SRC_NORMOL = R.drawable.ic_page_indicator;
    private LayoutParams mViewpagerLayoutParams, mPointLayoutParams;
    //    viewpager
    private ViewPager mViewPager;
    //     viewpager适配器
    private GetFriendViewPagerAdapter getPagerAdapter;
    //    point的layout
    private LinearLayout mPointLayout;
    //    point的集合
    private ArrayList<ImageView> mPointViews;
    //    亮point的位置
    private int mCurrentPage;
    //    每页的Recycview控件
    private ArrayList<View> mPageViews;
    //    每页的RecycleView的适配器
    private ArrayList<GetFriendAdapter> adapters;
    //    得到的friendBeanList
    private List<GetFriendBean> friendBeanList;
    //    每个pager的RecycleView的list
    private List<List<GetFriendBean>> pagerfriendBeanList;
    //    当前控件的宽高
    int with, height;
    //    pager的总页数
    int pagerCount;
    //    pager一页的item数量
    int pagerItemCount = DEFUALT_COLUMN * 2;

    private static final int DEFUALT_POINT_MARGIN = 5;
    private int mPointMargin = DEFUALT_POINT_MARGIN;
    private int mPointNorSrc = DEFUALT_SRC_NORMOL, mPointSelectSrc = DEFUALT_SRC_SELECT;

    public GetFriendLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
//        intiRecyclerView();
    }

    private void initData() {
//        计算总页数
        int listsize = friendBeanList.size();
        if (listsize / (float) pagerItemCount > listsize / pagerItemCount) {
            pagerCount = listsize / pagerItemCount + 1;
        } else {
            pagerCount = listsize / pagerItemCount;
        }
//        分割我的集合
        pagerfriendBeanList = new ArrayList<>();
        for (int i = 0; i < pagerCount; i++) {
            List<GetFriendBean> list = null;
            if (i == pagerCount - 1) {
                list = friendBeanList.subList((i) * pagerItemCount, friendBeanList.size());
            } else {
                list = friendBeanList.subList(i * pagerItemCount, (i + 1) * pagerItemCount);
            }
            pagerfriendBeanList.add(list);
        }
        intiRecyclerView();
        initPointLayout();
        initSelectPoint();
    }

    private void intiRecyclerView() {
        mPageViews = new ArrayList<>();
        adapters = new ArrayList<>();

        for (int i = 0; i < pagerCount; i++) {
//            Recycleview
            RecyclerView recycler = new RecyclerView(context);
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(DEFUALT_COLUMN, StaggeredGridLayoutManager.VERTICAL);
            recycler.setLayoutManager(staggeredGridLayoutManager);
            recycler.setLayoutParams(layoutParams);
//            适配器
            GetFriendAdapter mFriendAdapter = new GetFriendAdapter(pagerfriendBeanList.get(i), context);
            mFriendAdapter.setOnItemClickLitener(new GetFriendAdapter.OnItemClickLitener() {

                @Override
                public void onItemClick(String url) {
                }
            });
            recycler.setAdapter(mFriendAdapter);
//            添加上适配器集合
            adapters.add(mFriendAdapter);
//          添加上控件
            mPageViews.add(recycler);
        }
        getPagerAdapter = new GetFriendViewPagerAdapter(mPageViews);
        mViewPager.setAdapter(getPagerAdapter);
    }

    private void initPointLayout() {
        if (pagerCount == 0) {
            return;
        }
        mPointViews = new ArrayList<ImageView>();
        for (int i = 0; i < pagerCount; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(mPointNorSrc);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT));
            layoutParams.setMargins(mPointMargin, mPointMargin, mPointMargin, mPointMargin);

            mPointViews.add(imageView);
            mPointLayout.addView(imageView, layoutParams);
        }
    }


    private void initSelectPoint() {
        if (pagerCount == 0) {
            return;
        }
        mCurrentPage = 0;
        mViewPager.setCurrentItem(mCurrentPage);
        mPointViews.get(mCurrentPage).setImageResource(mPointSelectSrc);
//        监听变化
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mPointViews.get(mCurrentPage).setImageResource(mPointNorSrc);
                mPointViews.get(i).setImageResource(mPointSelectSrc);
                mCurrentPage = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    private void intiRecyclerViews() {
        recyclerView = new RecyclerView(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        recyclerView.setLayoutParams(layoutParams);
        recyclerView.setVisibility(GONE);
        this.addView(recyclerView);
        adapter = new GetFriendAdapter(context);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(DEFUALT_COLUMN, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initView() {
        mViewPager = new ViewPager(context);
        mPointLayout = new LinearLayout(context);
        //noinspection ResourceType
        mViewPager.setId(1);
        //noinspection ResourceType
        mPointLayout.setId(2);
//
        mViewpagerLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPointLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mViewpagerLayoutParams.addRule(RelativeLayout.ABOVE, mPointLayout.getId());

        mPointLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//        mPointLayoutParams.setMargins(0, 0, 0, 12);//左上右下

        addView(mPointLayout, mPointLayoutParams);
        addView(mViewPager, mViewpagerLayoutParams);
//
        mPointLayout.setOrientation(LinearLayout.HORIZONTAL);
        mPointLayout.setGravity(Gravity.CENTER);
    }

    public void setFriendList(List<GetFriendBean> friendBeanList) {
        this.friendBeanList = friendBeanList;
        if (friendBeanList != null && friendBeanList.size() > 0) {
//        根据数据初始化东西了
            initData();
//            adapter.setData(friendBeanList);
        } else {
            TextView textView = new TextView(context);
            textView.setText("小伙伴们空空如也哦");
            textView.setTextColor(0x99FFFFFF);
            textView.setTextSize(20);
            textView.setGravity(Gravity.CENTER);
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            addView(textView, lp);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        with = widthMeasureSpec;
        height = heightMeasureSpec;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }
}
