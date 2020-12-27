package com.amap.map3d.demo.routepoi;

public class RoutePOIListItem {

    private final String name;
    private final String address;

    RoutePOIListItem(String name, String address){
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
