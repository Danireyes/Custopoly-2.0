package com.iplusplus.custopoly.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TabHost;
import android.widget.TextView;

import com.iplusplus.custopoly.controller.NegotiationController;
import com.iplusplus.custopoly.model.MusicPlayerService;
import com.iplusplus.custopoly.model.gamemodel.element.Game;
import com.iplusplus.custopoly.model.gamemodel.element.Player;
import com.iplusplus.custopoly.model.gamemodel.element.PropertyLand;

import java.util.ArrayList;

/**
 * Created by usuario-pc on 19/12/2015.
 */
public class NegotiationActivity extends Activity {
    public PropertyLand getSelectedProperty() {
        return selectedProperty;
    }

    private PropertyLand selectedProperty = null;
    private ArrayList<Player> players;

    public NumberPicker getOffer() {
        return offer;
    }

    public TabHost getViewsTabs() {
        return viewsTabs;
    }

    private NumberPicker offer;
    private TabHost viewsTabs;
    private Button makeOfferButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //These will put the View on full screen
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negotiation);



        offer = (NumberPicker) findViewById(R.id.numberPicker);
        offer.setMinValue(1);


        //Set the different tabs
        viewsTabs = (TabHost)findViewById(R.id.propertiesViewTabHost);
        viewsTabs.setup();


        makeOfferButton = (Button)findViewById(R.id.makeOfferButton);

        Game g = (Game) getIntent().getSerializableExtra("game");
        new NegotiationController(g, this);
    }

    public void addMakeOfferButtonOnClickListener( View.OnClickListener listener) {
        makeOfferButton.setOnClickListener(listener);
    }

    public void addOfferMaxValue(int max) {
        offer.setMaxValue(max);
    }

    public void setViewsTabsOnTabChangeListener(TabHost.OnTabChangeListener listener) {
        viewsTabs.setOnTabChangedListener(listener);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void buildPropertiesViewFromInformation(ArrayList<PropertyLand> properties, ArrayList<Integer> imageIds, String tab) {
        //Select the appropiate container
        LinearLayout propertiesContainer = null;
        if(tab.equals((players.get(0).getName()))) {
            propertiesContainer = (LinearLayout) findViewById(R.id.Player1ContainerLayout);
        }
        else if (tab.equals(players.get(1).getName()))
        {
            propertiesContainer = (LinearLayout) findViewById(R.id.Player2ContainerLayout);
        }
        else if(tab.equals(players.get(2).getName())) {
            propertiesContainer = (LinearLayout) findViewById(R.id.Player3ContainerLayout);
        }
        propertiesContainer.removeAllViews();

        //Add an image to the porpertiesContainerLayout (its an horizontal scrollview)
        int i = 0;
        for(PropertyLand prop: properties)
        {
            ImageView propertyImageView = new ImageView(this);
            //Set the image resource id
            propertyImageView.setImageResource(imageIds.get(i));
            //Add the new imageView
            propertiesContainer.addView(propertyImageView);

            //Configure the listener
            final PropertyLand attachedProp = prop;
            propertyImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayPropertyInformation(attachedProp);
                }
            });

            i++;
        }

        //Display Initial information
        if(!properties.isEmpty()) {
            displayPropertyInformation(properties.get(0));
            this.makeOfferButton.setEnabled(true);
        }
        else {
            this.makeOfferButton.setEnabled(false);
        }
    }

    private void displayPropertyInformation(PropertyLand property)
    {
        //Get the text views that will be modifed
        TextView propertyNameTextView = (TextView)findViewById(R.id.nameTextView);
        TextView propertyValueTextView = (TextView)findViewById(R.id.valueTextView);
        TextView propertyRentTextView = (TextView)findViewById(R.id.rentValueTextView);
        TextView propertyMortgageValueTextView = (TextView)findViewById(R.id.mortgageValueTextView);

        //Set values
        propertyNameTextView.setText(property.getName());
        propertyValueTextView.setText(Integer.toString(property.getPrice()));
        propertyRentTextView.setText(Integer.toString(property.getRentInfo().getBaseRent()));
        propertyMortgageValueTextView.setText(Integer.toString(property.getMortgage()));

        selectedProperty = property;
    }

    @Override
    protected void onPause() {
        super.onPause();
        //MusicPlayerService.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //MusicPlayerService.resume();
    }

    public void showAlertDialog(AlertDialog alert) {
        alert.show();
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }
}
