package com.example.stankirf.model.machine.lathe;

import com.example.stankirf.model.machine.Lathe;

public class UniversalLathe extends Lathe {

    private String typeMachine;
    private String equipmentAccuracyClass;

    public UniversalLathe(String id, String modelName, String producingCountry, String producer, String machineGroup, String typeMachine, String equipmentAccuracyClass){

        super(id, modelName, producingCountry, producer, machineGroup);
    }
}
