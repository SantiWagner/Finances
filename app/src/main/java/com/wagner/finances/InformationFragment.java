package com.wagner.finances;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class InformationFragment extends Fragment implements  Constants {


    //Stored in USD as base
    HashMap<String,Double> convertionRates = new HashMap<>();

    String mainCurrency = "USD";
    String secondaryCurrency = "ARS";

    IndicatorAdapter currencyListAdapter;
    IndicatorAdapter indicatorListAdapter;


    List<Indicator> currencies = new ArrayList<>();
    List<Indicator> indicators = new ArrayList<>();


    RecyclerView currenciesRecyclerView;

    RecyclerView indicatorsRecyclerView;


    View rootView;


    private static DecimalFormat df_3 = new DecimalFormat("#.###");
    private static DecimalFormat df_2 = new DecimalFormat("#.##");

    public InformationFragment() {
        // Required empty public constructor
    }

    public static InformationFragment newInstance() {
        InformationFragment fragment = new InformationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currencyListAdapter = new IndicatorAdapter(getActivity(), currencies);
        indicatorListAdapter = new IndicatorAdapter(getActivity(), indicators);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_information, container, false);

        currenciesRecyclerView = (RecyclerView) rootView.findViewById(R.id.currency_list);
        indicatorsRecyclerView = (RecyclerView) rootView.findViewById(R.id.indicators_list);


        final GridLayoutManager currenciesLayoutManager = new GridLayoutManager(getActivity(), 1);
        final GridLayoutManager indicatorsLayoutManager = new GridLayoutManager(getActivity(), 1);


        currenciesRecyclerView.setLayoutManager(currenciesLayoutManager);
        indicatorsRecyclerView.setLayoutManager(indicatorsLayoutManager);

        currenciesRecyclerView.setAdapter(currencyListAdapter);
        indicatorsRecyclerView.setAdapter(indicatorListAdapter);


        Button button = (Button) rootView.findViewById(R.id.refreshButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), SettingsActivity.class);
                //startActivity(intent);

                getCurrencies(mainCurrency);
                getIndicators(mainCurrency);
            }
        });


        LinearLayout selectPortfolioLayout  = (LinearLayout) rootView.findViewById(R.id.selectPortfolio);

        selectPortfolioLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switchCurrencies();
            }
        });

        TextView selected = (TextView) rootView.findViewById(R.id.selected_portfolio);

        selected.setText(Html.fromHtml("Portfolio: <strong><font color='#00bcd4'>"+mainCurrency+"</font></strong>"));

        getCurrencies(mainCurrency);
        getIndicators(mainCurrency);
        return rootView;
    }

    public void switchCurrencies(){
        if(mainCurrency == "USD")
        {
            mainCurrency = "ARS";
            secondaryCurrency = "USD";

        }else{
            mainCurrency = "USD";
            secondaryCurrency = "ARS";
        }

        TextView selected = (TextView) rootView.findViewById(R.id.selected_portfolio);

        selected.setText(Html.fromHtml("Portfolio: <strong><font color='#00bcd4'>"+mainCurrency+"</font></strong>"));
        //selected.setText("Portfolio: "+mainCurrency);
        getIndicators(mainCurrency);

        buildTable(mainCurrency);
    }

    public void getCurrencies(final String base){
        String url="http://www.apilayer.net/api/live?access_key=462491ec3c04889a45aec0eef196af70&source=USD&currencies=ARS,BRL,UYU,GBP,EUR";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {

                        JSONObject json_response=(JSONObject)response;

                        try {
                            String[] items=json_response.getString("quotes").substring(1,json_response.getString("quotes").length()-1).split(",");
                            double usd_to_ars=0;
                            currencies.clear();
                            for (int i = 0; i < items.length; i++) {

                                String symbol = items[i].split(":")[0].substring(4, 7);

                                double value = Double.parseDouble(items[i].split(":")[1]);

                                convertionRates.put(symbol,value);

                                buildTable(base);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {

                            currencyListAdapter.notifyDataSetChanged();
                            rootView.findViewById(R.id.loading_currencies).setVisibility(View.GONE);
                            rootView.findViewById(R.id.currency_source).setVisibility(View.VISIBLE);
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Failed to retrieve information...",
                                Toast.LENGTH_LONG).show();

                        Log.e("Error en Volley",error.getMessage());
                    }
                });
        App.getInstance().addToRequestQueue(jsonObjReq, "getIndicators");
    }

    public void buildTable(String to){
        currencies.clear();
        if(to == "USD"){
            for(String key:convertionRates.keySet()){
                currencies.add(new Indicator(key,df_3.format(1/convertionRates.get(key))+" "+to));
            }
        }else{

            double usd_to_ars = 1/convertionRates.get("ARS");
            currencies.add(new Indicator("USD", convertionRates.get("ARS")+" ARS"));
            for(String key:convertionRates.keySet()){
                double factor = 1/convertionRates.get(key);
                currencies.add(new Indicator(key, df_3.format(convertionRates.get("ARS") * factor)+" "+to));
            }

        }

        currencyListAdapter.notifyDataSetChanged();
    }

    public void getIndicators(String currency){
        SharedPreferences preferences = getActivity().getSharedPreferences(INDICATORS_FILE, 0);
        float cost_of_living = preferences.getFloat("COST_OF_LIVING_"+currency,-1);
        float goal = preferences.getFloat("GOAL_"+currency,-1);

        double total = MainActivity.db.itemDao().getTotalByCurrency(currency);

        indicators.clear();
        indicators.add(new Indicator("Total",df_2.format(total)+" "+currency));
        if(cost_of_living == -1){
            indicators.add(new Indicator("Cost of Living", "not set"));
            indicators.add(new Indicator("Total over CoL", "-"));
        }else{
            indicators.add(new Indicator("Cost of Living", df_2.format(cost_of_living)+" "+currency));
            indicators.add(new Indicator("Total over CoL", df_2.format(total/cost_of_living)));
        }

        if(goal == -1){
            indicators.add(new Indicator("Goal", "not set"));
            indicators.add(new Indicator("% Goal", "-"));
            indicators.add(new Indicator("Remaining", "-"));
        }else{
            indicators.add(new Indicator("Goal", df_2.format(goal)+" "+currency));
            indicators.add(new Indicator("% Goal", df_2.format(100*total/goal)+"%"));
            indicators.add(new Indicator("Remaining", df_2.format(Math.max(0,goal-total))+" "+currency));
        }

        rootView.findViewById(R.id.loading_indicators).setVisibility(View.GONE);

        indicatorListAdapter.notifyDataSetChanged();
    }

}
