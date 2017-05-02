package com.example.arena.oracle.fargment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.arena.oracle.R;
import com.example.arena.oracle.adapter.BannerAdapter;
import com.example.arena.oracle.adapter.PostsListAdapter;
import com.example.arena.oracle.bean.Post;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



/**
 * Updated by Arena on 2017/4/3.
 */
public class DiscoverFragment extends Fragment implements View.OnClickListener{


    private Context context;

    private PullToRefreshRecyclerView ptrrvPostList;
    private ImageButton ibtnAdd;
    private ImageButton ibtnSerach;

    private List<Post> posts = new ArrayList<Post>();
    private PostsListAdapter adapter;
    private Handler handler = new Handler();

    private ViewPager vpBanner; // android-support-v4中的滑动组件
    private List<SimpleDraweeView> bannerImages; // 滑动的图片集合

    private String[] imageUris; // 图片网址

    private int currentItem = 0; // 当前图片的索引号

    private static final int STATE_REFRESH = 0;// 下拉刷新
    private static final int STATE_MORE = 1;// 加载更多

    private int limit = 10;		// 每页的数据是10条
    private int curPage = 0;		// 当前页的编号，从0开始
    private String lastTime;
    private int count = 10;		// 每页的数据是10条

    private int height;

    // An ExecutorService that can schedule commands to run after a given delay,
    // or to execute periodically.
    private ScheduledExecutorService scheduledExecutorService;

    // 切换当前显示的图片
    private Handler bannerHandler = new Handler() {

        @Override
        public void handleMessage(android.os.Message msg) {
            vpBanner.setCurrentItem(currentItem);// 切换当前显示的图片
        };
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_discover, container,false);
        //下拉刷新组件
        ptrrvPostList = (PullToRefreshRecyclerView)view.findViewById(R.id.ptrrvPostList);
        ibtnAdd = (ImageButton)view.findViewById(R.id.ibtnAdd);
        ibtnSerach = (ImageButton)view.findViewById(R.id.ibtnSearch);
        initView();
        initData();

        ibtnAdd.setOnClickListener(this);
        ibtnSerach.setOnClickListener(this);
        return view;
    }


    public void onStart(){
        super.onStart();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 当Activity显示出来后，每两秒钟切换一次图片显示
        //scrolltask是一个进程
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 3, TimeUnit.SECONDS);

    }

    public void onStop(){
        super.onStop();
        scheduledExecutorService.shutdown();
    }

    public void initBanner() {

        //可以直接使用后台的推送图片网址
        imageUris = new String[]{
                "http://img.mukewang.com/529dc3380001379906000338-300-170.jpg",
                "http://img.mukewang.com/572b067f00019abf06000338-300-170.jpg",
                "http://img.mukewang.com/578386af00010eea06000338-300-170.jpg",
                "http://img.mukewang.com/5387e32e000196cb06000338-300-170.jpg"};
                //"http://www.kedo.gov.cn/upload/resources/image/2016/05/30/146458817515017925.jpg?1464854581356",
                //"http://www.kedo.gov.cn/upload/resources/image/2016/05/30/146458813054817921.jpeg?1464854581350",
                //"http://www.kedo.gov.cn/upload/resources/image/2016/05/23/146398435933417830.jpg?1464854581290",
                //"http://www.kedo.gov.cn/upload/resources/image/2016/05/16/146336359944417743.jpg?1464854581384"};

        bannerImages = new ArrayList<>();

        for(int i = 0;i<imageUris.length;i++)
        {
            //创建将要下载的图片的URI
            Uri imageUri = Uri.parse(imageUris[i]);
            SimpleDraweeView bannerSimpleDraweeView = new SimpleDraweeView(context);
            //开始下载
            bannerSimpleDraweeView.setImageURI(imageUri);
            bannerImages.add(bannerSimpleDraweeView);
        }
        vpBanner.setAdapter(new BannerAdapter(bannerImages));
    }

    public void initView() {

        WindowManager wm = (WindowManager) getContext()
                    .getSystemService(Context.WINDOW_SERVICE);
        height = wm.getDefaultDisplay().getHeight();

        //先加载更多的动态图
        //CustomLoadMoreView loadMoreView = new CustomLoadMoreView(context, ptrrvPostList.getRecyclerView());
        //loadMoreView.setLoadmoreString("Loading...");
        //loadMoreView.setLoadMorePadding(100);

        //ptrrvPostList.setLoadMoreFooter(loadMoreView);

        ptrrvPostList.setSwipeEnable(true);

        ptrrvPostList.setLayoutManager(new LinearLayoutManager(context));

        ptrrvPostList.setPagingableListener(new PullToRefreshRecyclerView.PagingableListener() {
            @Override
            public void onLoadMoreItems() {
                //do loadmore here
                getData(curPage,STATE_MORE);
            }
        });

        ptrrvPostList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // do refresh here
                getData(0,STATE_REFRESH);
            }
        });

        View headerView = View.inflate(context, R.layout.item_recycleview_header, null);

        ptrrvPostList.addHeaderView(headerView);

        vpBanner = (ViewPager)headerView.findViewById(R.id.bvpBanner);

        initBanner();

        ptrrvPostList.onFinishLoading(true, false);

        //ListView添加适配器
        adapter = new PostsListAdapter(context,posts);

        ptrrvPostList.setAdapter(adapter);


        // adapter添加点击事件
        adapter.setOnItemClickListener(new PostsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Intent intent = new Intent(context, PostContentActivity.class);
                //intent.putExtra("post",posts.get(position));
                //startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

    }


    public void initData() {
        //延时加载
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getData(0,STATE_REFRESH);
            }
        }, 1500);
    }

    /**
     * 获取测试数据
     */


    private void getData(int page,final int actionType) {
        ptrrvPostList.setRefreshing(true);
    }

    @Override
    public void onClick(View v) {
        if(v == ibtnAdd){
            //Intent intent = new Intent(context,NewPostActivity.class);
            //context.startActivity(intent);
        }else if(v == ibtnSerach){
            //Intent intent = new Intent(context,SearchActivity.class);
            //context.startActivity(intent);
        }
    }

    /**
     * 换行切换任务
     *
     * @author Administrator
     *
     */
    private class ScrollTask implements Runnable {

        public void run() {
            synchronized (vpBanner) {
                //System.out.println("currentItem: " + currentItem);
                currentItem = (currentItem + 1) % bannerImages.size();
                bannerHandler.obtainMessage().sendToTarget(); // 通过Handler切换图片
            }
        }

    }


}

