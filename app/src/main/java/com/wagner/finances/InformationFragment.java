package com.wagner.finances;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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


public class InformationFragment extends Fragment {

    CurrencyAdapter currencyListAdapter;
    IndicatorAdapter indicatorListAdapter;

    List<Currency> currencies = new ArrayList<>();
    List<Indicator> indicators = new ArrayList<>();

    RecyclerView currenciesRecyclerView;

    RecyclerView indicatorsRecyclerView;

    View rootView;


    private static DecimalFormat df = new DecimalFormat("#.###");

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
        currencyListAdapter = new CurrencyAdapter(getActivity(), currencies);
        indicatorListAdapter = new IndicatorAdapter(getActivity(), indicators);
        getCurrencies();
        getPerformance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView=inflater.inflate(R.layout.fragment_information, container, false);

        currenciesRecyclerView = (RecyclerView) rootView.findViewById(R.id.currency_list);
        indicatorsRecyclerView = (RecyclerView) rootView.findViewById(R.id.indicators_list);

        final GridLayoutManager currenciesLayoutManager = new GridLayoutManager(getActivity(), 1);
        final GridLayoutManager indicatorsLayoutManager = new GridLayoutManager(getActivity(), 1);

        currenciesRecyclerView.setLayoutManager(currenciesLayoutManager);
        indicatorsRecyclerView.setLayoutManager(indicatorsLayoutManager);

        currenciesRecyclerView.setAdapter(currencyListAdapter);
        indicatorsRecyclerView.setAdapter(indicatorListAdapter);

        Button button = (Button) rootView.findViewById(R.id.refreshButton);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                IndicatorAdapter.current_row=0;
                CurrencyAdapter.current_row=0;
                getCurrencies();
                getPerformance();
            }
        });

        return rootView;
    }

    public void getCurrencies(){
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

                                String symbol = items[i].split(":")[0].substring(4,7);

                                double value = Double.parseDouble(items[i].split(":")[1]);

                                if(symbol.equals("ARS")){
                                    currencies.add(new Currency("USD",df.format(value)+" ARS"));
                                    usd_to_ars=value;
                                }else{

                                    currencies.add(new Currency(symbol,df.format(usd_to_ars/value)+" ARS"));
                                }

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


    public void getPerformance(){
        String url="https://www.alphavantage.co/query?function=SECTOR&apikey=HANRA6QVP1HOFPYH";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        Log.e("Response: ",response.toString());
                        JSONObject json_response=(JSONObject)response;

                        try {
                            JSONObject elements = json_response.getJSONObject("Rank D: 1 Month Performance");
                            indicators.clear();
                            Iterator<String> keys = elements.keys();

                            while(keys.hasNext()) {
                                String key = keys.next();
                                indicators.add(new Indicator(key,elements.getString(key)));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            indicatorListAdapter.notifyDataSetChanged();
                            rootView.findViewById(R.id.loading_indicators).setVisibility(View.GONE);
                            rootView.findViewById(R.id.indicators_source).setVisibility(View.VISIBLE);
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Failed to retrieve information...",
                                Toast.LENGTH_LONG).show();

                    }
                });
        App.getInstance().addToRequestQueue(jsonObjReq, "getPerformance");
    }

}
