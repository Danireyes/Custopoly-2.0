package com.iplusplus.custopoly.model.gamemodel.util;

import com.iplusplus.custopoly.model.gamemodel.element.Building;
import com.iplusplus.custopoly.model.gamemodel.element.Hotel;
import com.iplusplus.custopoly.model.gamemodel.element.House;

import java.util.ArrayList;

public class BuildingHolder extends ArrayList<Building> {
	
	public static final int MAX_HOUSE_AMOUNT = 4;
	public static final int MAX_HOTEL_AMOUNT = 1;

	public void addBuilding() {
		if (getHouseAmount() == 4) {
			removeHouse();
            removeHouse();
            removeHouse();
            removeHouse();
            addHotel(new Hotel());
		} else {
			addHouse(new House());
		}
	}

    public void removeBuilding(Building building) {
        if(building.isHotel()) {
            removeHotel();
            addHouse(new House());
            addHouse(new House());
            addHouse(new House());
            addHouse(new House());
        }
        if(building.isHouse()) {
            removeHouse();
        }
    }
	private void addHotel(Building hotel) {
		if (getHotelAmount() < MAX_HOTEL_AMOUNT && getHouseAmount() == 0) {
			add(hotel);
		} else {
			throw new RuntimeException("MAX HOTEL REACHED OR HOLDER HAS HOUSE.");
		}
	}

	private void addHouse(Building house) {
		if (getHouseAmount() < MAX_HOUSE_AMOUNT && getHotelAmount() == 0) {
			add(house);
		} else {
			throw new RuntimeException("MAX HOUSE REACHED OR HOLDER HAS HOTEL.");
		}					
	}

	public int getHouseAmount() {
		int numberHouse = 0;
		
		for (Building building : this) {
			numberHouse += (building.isHouse())? 1 : 0;
		}
		
		return numberHouse;
	}

	public int getHotelAmount() {
		int numberHotel = 0;
		
		for (Building building : this) {
			numberHotel += (building.isHotel())? 1 : 0;
		}
		
		return numberHotel;	
	}
	
	public House removeHouse(){
		for (int i = 0; i < this.size(); i++) {
			if(this.get(i) instanceof House) {
				return (House) this.remove(i);
			}
		}
		return null;

	}
	
	public Hotel removeHotel() {
		for (int i = 0; i < this.size(); i++) {
			if(this.get(i) instanceof Hotel) {
				return (Hotel) this.remove(i);
			}
		}
		return null;
	}

    public boolean hasBuilding() {
        return ((this.getHouseAmount() > 0) ||(this.getHotelAmount() > 0));
    }
}
