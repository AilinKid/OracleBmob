package com.example.arena.oracle.fargment;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arena.oracle.R;
import com.example.arena.oracle.adapter.DemoParallaxAdapter;


public class DemoParallaxFragment extends Fragment {

    private DemoParallaxAdapter mCatsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.demo_fragment_parallax, container, false);
        final ImageView image = (ImageView) v.findViewById(R.id.image);

        image.setImageResource(getArguments().getInt("image"));
        image.post(new Runnable() {
            @Override
            public void run() {
                Matrix matrix = new Matrix();
                matrix.reset();

                //获得imageView的宽度和高度
                float wv = image.getWidth();
                float hv = image.getHeight();
                //获得imageView里面的图片的宽度和高度
                float wi = image.getDrawable().getIntrinsicWidth();
                float hi = image.getDrawable().getIntrinsicHeight();

                float width = wv;
                float height = hv;

                if (wi / wv > hi / hv) {
                    matrix.setScale(hv / hi, hv / hi);
                    width = wi * hv / hi;
                } else {
                    matrix.setScale(wv / wi, wv / wi);
                    height = hi * wv / wi;
                }
                //图片的平移操作
                matrix.preTranslate((wv - width) / 2, (hv - height) / 2);
                //按矩阵方式显示
                image.setScaleType(ImageView.ScaleType.MATRIX);
                image.setImageMatrix(matrix);
            }
        });


        TextView text = (TextView)v.findViewById(R.id.name);
        text.setText(getArguments().getString("name"));

        TextView more = (TextView)v.findViewById(R.id.more);

        more.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mCatsAdapter != null) {
                    mCatsAdapter.remove(DemoParallaxFragment.this);
                    mCatsAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCatsAdapter != null) {
                    int select = (int) (Math.random() * 4);

                    int[] resD = {R.mipmap.bg_nina, R.mipmap.bg_niju, R.mipmap.bg_yuki, R.mipmap.bg_kero};
                    String[] resS = {"Nina", "Niju", "Yuki", "Kero"};

                    DemoParallaxFragment newP = new DemoParallaxFragment();
                    Bundle b = new Bundle();
                    b.putInt("image", resD[select]);
                    b.putString("name", resS[select]);
                    newP.setArguments(b);
                    mCatsAdapter.add(newP);
                }
            }
        });
        return v;
    }

    //viewpager中的adapter中传过来，方便在在子fragment中更改fragment
    public void setAdapter(DemoParallaxAdapter catsAdapter) {
        mCatsAdapter = catsAdapter;
    }
}
