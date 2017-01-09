package com.example.david.dpsproject.Fragments.ViewPost;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.david.dpsproject.Class.Post;
import com.example.david.dpsproject.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by david on 2017-01-02.
 */

public class PieChartFragment extends Fragment {
    private PieChart pieChart;
    private Activity mActivity;
    private View myView;
    private Bundle bundle;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView= inflater.inflate(R.layout.piechartlayout,container,false);
        pieChart = (PieChart) myView.findViewById(R.id.chart1);
        bundle=getArguments();
        if(bundle!=null){
            init((Post) bundle.getSerializable("Post_Object"));
        }

        return myView;
    }
    public float CalcPercentage(float total, float votes){
        if(votes!=0) {
            try {
                float value = (votes / total) * 100;
                return value;
            } catch (ArithmeticException e) {
               e.printStackTrace();
            }
        }
        return 0;

    }

    public void init(Post post){


        setPieChartLook();

        List<PieEntry> entries = new ArrayList<>();
        Iterator it= post.getPiechart().entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            Float value =CalcPercentage((Integer)post.getTotalPost(),(Integer) pair.getValue());
            if(value!=0){
                entries.add(new PieEntry(value,pair.getKey().toString()+" "+value+"%"));


            }

        }


        PieDataSet set = new PieDataSet(entries, "Results");
        set.setColors(new int[]{R.color.color1,R.color.color2,R.color.color3,R.color.color4,R.color.color5,R.color.color6,R.color.color7
        ,R.color.color8,R.color.color9,R.color.color10,R.color.color11,R.color.color12,R.color.color13,R.color.color14,R.color.color15},mActivity);
        PieData data = new PieData(set);
        pieChart.setData(data);
        pieChart.invalidate(); // refresh



        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

    }
    public void setPieChartLook(){
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);


        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setDrawCenterText(true);
        pieChart.setEntryLabelTextSize(10f);
        pieChart.setEntryLabelColor(R.color.black);


        pieChart.setCenterTextSize(10f);

        pieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

    }
}
