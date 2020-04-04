package com.example.accountingapplication;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lecho.lib.hellocharts.model.ChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.LineChartView;

public class ChartsActivity extends Activity {
    private LineChartView mChart;
    private Map<String,Integer>table=new TreeMap<>();
    private LineChartData mData;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_view);
        mChart=(LineChartView)findViewById(R.id.chart);
        mData=new LineChartData();
        List<CostBean> allData= (List<CostBean>) getIntent().getSerializableExtra("cost_list");
        generateValues(allData);
        generateData();

    }

    private void generateData() {
        List<Line>lines=new ArrayList<>();
        List<PointValue>values=new ArrayList<>();
        //处理 点
        int indexX=0;
        for(Integer value:table.values()){
            values.add(new PointValue(indexX,value));
            indexX++;
        }
        //处理线，链接点
        Line line=new Line(values);
        line.setColor(ChartUtils.COLOR_BLUE);
        line.setShape(ValueShape.CIRCLE);
        line.setPointColor(ChartUtils.COLOR_RED);
        lines.add(line);
        mData=new LineChartData(lines);
        mChart.setLineChartData(mData);
    }

    //处理（重复）数据
    private void generateValues(List<CostBean> allData) {
        if(allData!=null){
            for (int i = 0; i <allData.size(); i++) {
                CostBean costBean=allData.get(i);
                String costDate=costBean.costDate;
                int costMoney = Integer.parseInt(costBean.costMoney);
                if(!table.containsKey(costDate)){
                    table.put(costDate,costMoney);
                }else{
                    //同一天数据累加
                    int originMoney=table.get(costDate);
                    table.put(costDate,originMoney+costMoney);
                }
            }
        }
    }
}
