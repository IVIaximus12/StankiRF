package com.example.stankirf.model.machine;

public class Machine {

    private String id;
    private String modelName;
    private String producingCountry;
    private String producer;
    private MachineGroup machineGroup;


    public Machine(String id, String modelName, String producingCountry, String producer, String machineGroup) {

        this.id = id;
        this.modelName = modelName;
        this.producingCountry = producingCountry;
        this.producer = producer;

        switch (machineGroup) {
            case "Токарный станок":
                this.machineGroup = MachineGroup.LATHE;
                break;
            case "Фрезерный станок":
                this.machineGroup = MachineGroup.MILLINGMACHINE;
                break;
        }

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

        return machineGroup.getMachineGroup();
    }

    // enum

    private enum MachineGroup {
        LATHE("Токарный станок"), MILLINGMACHINE("Фрезерный станок");

        private String machineGroup;

        MachineGroup(String machineGroup){
            this.machineGroup = machineGroup;
        }

        public String getMachineGroup() {
            return machineGroup;
        }
    }
}
