package com.example.stankirf.model.machine.lathe;

import com.example.stankirf.model.machine.Lathe;

public class CncLathe extends Lathe {

    private String cncSystem;
    private String guides;
    private String bed;

    public CncLathe(String id, String modelName, String producingCountry, String producer, String machineGroup, String cncSystem, String guides, String bed){

        super(id, modelName, producingCountry, producer, machineGroup);
    }
}
