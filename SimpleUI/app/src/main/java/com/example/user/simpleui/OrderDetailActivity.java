package com.example.user.simpleui;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.os.Handler;

import java.lang.ref.WeakReference;
import java.sql.BatchUpdateException;
import java.util.logging.LogRecord;

public class OrderDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Order order = getIntent().getParcelableExtra("order");

        TextView notetextView = (TextView) findViewById(R.id.noteTextView);
        TextView orderResultTextView = (TextView) findViewById(R.id.orderResultsTextView);
        TextView storeInfoTextView = (TextView) findViewById(R.id.storeInfoTextView);

        notetextView.setText(order.getNote());
        storeInfoTextView.setText(order.getStoreInfo());

        String orderResultsText = "";
        for (DrinkOrder drinkOrder : order.getDrinkOrders()) {

            String mNumber = String.valueOf(drinkOrder.getmNumber());
            String lNumber = String.valueOf(drinkOrder.getlNumber());
            String drinkName = drinkOrder.getDrink().getName();
            orderResultsText += drinkName + "M: " + mNumber + "L: " + lNumber;
        }

        orderResultTextView.setText(orderResultsText);

        ImageView staticMapImageView = (ImageView) findViewById(R.id.staticMapImageView);

        (new GeoCodingTask(staticMapImageView)).execute("台北市羅斯福路四段一號");
        Log.e("Main Thread ID", Long.toString(Thread.currentThread().getId()));
    }

    public static class GeoCodingTask extends AsyncTask<String, Void, Bitmap> {

        WeakReference<ImageView> imageViewWeakReference;

        @Override
        protected Bitmap doInBackground(String... params) {
            double[] latlng = Utils.getLatLngFromAddress(params[0]);
            if (latlng != null) {
                return Utils.getStaticMapFromLatLng(latlng);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null){
                if(imageViewWeakReference.get() != null){
                    ImageView imageView = imageViewWeakReference.get();
                    imageView.setImageBitmap(bitmap);
                }
            }
            Log.e("PostExecute Thread ID", Long.toString(Thread.currentThread().getId()));
        }

        public GeoCodingTask(ImageView imageView){
            imageViewWeakReference = new WeakReference<ImageView>(imageView);
        }
    }


}
