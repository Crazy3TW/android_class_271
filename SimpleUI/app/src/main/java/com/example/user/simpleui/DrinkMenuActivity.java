package com.example.user.simpleui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.security.interfaces.DSAKey;
import java.util.ArrayList;
import java.util.List;

public class DrinkMenuActivity extends AppCompatActivity implements DrinkOrderDialog.OnDrinkOrderListener{

    ListView drinkMenuListView;
    TextView totalTextView;

    String[] drinkNames = new String[]{"Black tea", "Milk black tea", "Ice tea", "Pearl milk tea"};
    int[] lPrices = new int[]{25,30,25,35};
    int[] mPrices = new int[]{15,20,15,25};
    int[] images = new int[]{R.drawable.blacktea, R.drawable.milkblacktea, R.drawable.icetea, R.drawable.pearlmilktea};

    List<Drink> drinkList= new ArrayList<>();
    ArrayList<DrinkOrder> drinkOrderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_menu);

        drinkMenuListView = (ListView)findViewById(R.id.drinkMenuListView);
        totalTextView = (TextView)findViewById(R.id.totalTextView);

        setData();

        drinkOrderList = getIntent().getParcelableArrayListExtra("drinkOrderList");
        setupTotalTextView();

//        setupDrinkMenuListView();

        Log.d("Debug", "DrinkMenuActivity OnCreate");
    }

    public void setData(){

        Drink.getDrinkFromLocalThenRemote(new FindCallback<Drink>() {
            @Override
            public void done(List<Drink> objects, ParseException e) {
                if (e == null) {
                    drinkList = objects;
                    setupDrinkMenuListView();
                }
            }
        });
//        for(int i =0; i<4 ;i++){
//            Drink drink = new Drink();
//            drink.name = drinkNames[i];
//            drink.lPrice = lPrices[i];
//            drink.mPrice = mPrices[i];
//            drink.imageID = images[i];
//            drinkList.add(drink);
//        }
    }

    public void setupDrinkMenuListView(){
        DrinkAdapter adapter = new DrinkAdapter(this, drinkList);
        drinkMenuListView.setAdapter(adapter);

        drinkMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Drink drink = (Drink) parent.getAdapter().getItem(position);
                showDrinkOrderDialog(drink);
                setupTotalTextView();
            }
        });
    }

    private void showDrinkOrderDialog(Drink drink){

        DrinkOrder order = new DrinkOrder(drink);

        for(DrinkOrder drinkOrder: drinkOrderList){
            if(drinkOrder.getDrink().getObjectId().equals(drink.getObjectId())){
                order = drinkOrder;
                break;
            }
        }

        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction FT = fragmentManager.beginTransaction();
        DrinkOrderDialog dialog = DrinkOrderDialog.newInstance(order);

        dialog.show(FT, "DrinkOrderDialog");
    }

    public void setupTotalTextView(){
        int total = 0;
        for(DrinkOrder drinkOrder :drinkOrderList){
            total += drinkOrder.total();
        }

        totalTextView.setText(String.valueOf(total));
    }

    public void done(View view){
        Intent intent = new Intent();
        intent.putExtra("results", drinkOrderList);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void cancel(View view){
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("debug", "DrinkMenuActivity OnStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("debug", "DrinkMenuActivity OnResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("debug", "DrinkMenuActivity OnPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("debug", "DrinkMenuActivity OnStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("debug", "DrinkMenuActivity OnRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("debug", "DrinkMenuActivity OnDestroy");
    }

    @Override
    public void onDrinkOrderFinished(DrinkOrder drinkOrder) {
        for(int i = 0 ; i < drinkOrderList.size() ; i++) {
            if (drinkOrderList.get(i).getDrink().getObjectId().equals(drinkOrder.getDrink().getObjectId())) {
                drinkOrderList.set(i, drinkOrder);
                setupTotalTextView();
                return;
            }
        }

            drinkOrderList.add(drinkOrder);
            setupTotalTextView();
    }
}
