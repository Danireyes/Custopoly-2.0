package com.iplusplus.custopoly.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;

import com.iplusplus.custopoly.controller.ViewPropertiesController;
import com.iplusplus.custopoly.model.MusicPlayerService;
import com.iplusplus.custopoly.model.gamemodel.element.ColoredLand;
import com.iplusplus.custopoly.model.gamemodel.element.Game;
import com.iplusplus.custopoly.model.gamemodel.element.PropertyLand;

import java.util.ArrayList;


public class PropertiesViewActivity extends Activity {

    public PropertyLand getSelectedProperty() {
        return selectedProperty;
    }

    private PropertyLand selectedProperty = null;
    private Button sellHouseButton;
    private Button mortgageButton;
    private TabHost viewsTabs;

    protected void onCreate(Bundle savedInstanceState) {
        //These will put the View on full screen
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_properties_view);

        mortgageButton = (Button) findViewById(R.id.mortgageButton);

        //Set the different tabs
        viewsTabs = (TabHost)findViewById(R.id.propertiesViewTabHost);
        viewsTabs.setup();

        TabHost.TabSpec unMortgageTab = viewsTabs.newTabSpec("UnMortgaged");
        unMortgageTab.setContent(R.id.UnMortgaged);
        unMortgageTab.setIndicator("Unmortgaged");
        viewsTabs.addTab(unMortgageTab);

        TabHost.TabSpec mortgageTab = viewsTabs.newTabSpec("Mortgaged");
        mortgageTab.setContent(R.id.Mortgaged);
        mortgageTab.setIndicator("Mortgaged");
        viewsTabs.addTab(mortgageTab);

        //implement the sell House button
        this.sellHouseButton = (Button) findViewById(R.id.sellHouseButton);
        Game game = (Game) getIntent().getSerializableExtra("game");
        new ViewPropertiesController(this, game);
    }

    public void addViewsTabsOnTabChangeListener(TabHost.OnTabChangeListener listener) {
        viewsTabs.setOnTabChangedListener(listener);
    }

    public void addSellHouseButtonOnClickListener(View.OnClickListener listener) {
        sellHouseButton.setOnClickListener(listener);
    }

    public void addMortgageButtonOnClickListener(View.OnClickListener listener) {
        mortgageButton.setOnClickListener(listener);
    }
    @Override
    public void onBackPressed() {
        finish();
    }

    public void buildPropertiesViewFromInformation(ArrayList<PropertyLand> properties, ArrayList<Integer> imageIds, String tab) {
        //Select the appropiate container
        LinearLayout propertiesContainer;
        if(tab.equals("UnMortgaged")) {
            propertiesContainer = (LinearLayout) findViewById(R.id.unmortagedContainerLayout);
        }
        else
        {
            propertiesContainer = (LinearLayout) findViewById(R.id.mortgagedContainerLayout);
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
            propertyImageView.setOnClickListener(getPropertyViewListener(prop));

            i++;
        }

        //Display Initial information
        if(!properties.isEmpty()) {
            displayPropertyInformation(properties.get(0));
        }
    }

    public View.OnClickListener getPropertyViewListener (final PropertyLand attachedProp) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPropertyInformation(attachedProp);
                if(attachedProp.getClass().getName().equals("ColoredLand")) {
                    ColoredLand land = (ColoredLand) attachedProp;
                    if(land.getBuildingHolder().hasBuilding()){
                        setSellHouseButtonVisibility(View.VISIBLE);
                    }
                    else {
                        setSellHouseButtonVisibility(View.INVISIBLE);
                    }
                }
                else {
                    setSellHouseButtonVisibility(View.INVISIBLE);
                }
            }
        };
    }

    public void setSellHouseButtonVisibility(int v) {
        sellHouseButton.setVisibility(v);
    }

    public void displayPropertyInformation(PropertyLand property)
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

    public void setTextOfMortgagedButton(String text) {
        this.mortgageButton.setText(text);
    }

    public void setMortgagedButtonEnabled(boolean b) {
        this.mortgageButton.setEnabled(b);
    }

    @Override
    public void finish() {
        super.finish();
    }
}
