package com.wagner.finances;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class DashboardFragment extends Fragment {

    DecimalFormat df;

    private OnFragmentInteractionListener mListener;

    public DashboardFragment() {
        // Required empty public constructor
        df = new DecimalFormat("0.00");
    }

    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        updateTotal(rootView);

        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void updateTotal(View rootView){
        TextView total = (TextView) rootView.findViewById(R.id.total_textview);

        PieChart composition = (PieChart) rootView.findViewById(R.id.composition);

        double total_d = MainActivity.db.itemDao().getTotal();

        List<PieEntry> pieChartEntries = new ArrayList<>();

        for(Item i:MainActivity.db.itemDao().getItems()) {
            pieChartEntries.add(new PieEntry((float)(i.amount/total_d)*100,i.name));
        }

        PieDataSet set = new PieDataSet(pieChartEntries, "");
        set.setDrawValues(false);
        set.setColors(ColorTemplate.PASTEL_COLORS);
        PieData data = new PieData(set);

        composition.setDrawEntryLabels(false);
        composition.setHoleColor(getResources().getColor(R.color.lightBackground));
        composition.setTransparentCircleAlpha(0);
        composition.setDrawMarkers(false);
        composition.setData(data);
        composition.getLegend().setTextColor(getResources().getColor(R.color.mainTextColor));
        composition.getLegend().setWordWrapEnabled(true);
        composition.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        composition.getDescription().setText("");
        total.setText(df.format(total_d)+ " USD");

    }
}
