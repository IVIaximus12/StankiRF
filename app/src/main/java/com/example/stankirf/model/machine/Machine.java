package com.example.stankirf.model.machine;

import java.io.Serializable;

public class Machine {

    private String id;
    public String modelName;
    public String producingCountry;
    public String producer;
    public String machineGroup;

    public Machine() {

    }

    public Machine(String id, String modelName, String producingCountry, String producer, String machineGroup) {

        this.id = id;
        this.modelName = modelName;
        this.producingCountry = producingCountry;
        this.producer = producer;
        this.machineGroup = machineGroup;

        /*switch (machineGroup) {
            case "Токарный станок":
                this.machineGroup = MachineGroup.LATHE;
                break;
            case "Фрезерный станок":
                this.machineGroup = MachineGroup.MILLINGMACHINE;
                break;
        }*/

    }

    // public methods

    public String getId() {
        return id;
    }

    public String getModelName() {
        return modelName;
    }

    public String getProducer() {
        return producer;
    }

    public String getProducingCountry() {
        return producingCountry;
    }

    public String getMachineGroup() {

        //return machineGroup.getMachineGroup();
        return machineGroup;
    }

    public void setId(String id) {
        this.id = id;
    }

    // enum

    /*private enum MachineGroup {
        LATHE("Токарный станок"), MILLINGMACHINE("Фрезерный станок");

        private String machineGroup;

        MachineGroup(String machineGroup){
            this.machineGroup = machineGroup;
        }

        public String getMachineGroup() {
            return machineGroup;
        }
    }*/
}
