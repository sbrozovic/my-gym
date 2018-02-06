package com.example.sara.mygym.Common.Network.Wrappers;

import com.example.sara.mygym.Common.Models.PriceList.Tariff;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sara on 14.1.2018..
 */

public class TariffModel {
    @SerializedName("Id")
    int id;
    @SerializedName("Name")
    String name;
    @SerializedName("DiscountPercent")
    int discountPercent;

    public TariffModel(Tariff tariff) {
        this.id = tariff.getId();
        this.discountPercent = tariff.getDiscount();
        this.name = tariff.getName();
    }

    public Tariff getTariff() {
        return new Tariff(this.id, this.name, this.discountPercent);
    }
}
