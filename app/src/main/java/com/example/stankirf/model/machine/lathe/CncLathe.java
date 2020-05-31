package com.example.stankirf.model.machine.lathe;

public class CncLathe {

    public String id;
    public String cncSystem;
    public String bed;
    public String maximumDiameterRotation;
    public String maximumTurningDiameter;
    public String maximumWorkpieceLength;
    public String maximumSpindleSpeed;
    public String spindleMotorPower;
    public String counterSpindle;
    public String tool;
    public String toolNumber;
    public String totalMachinePower;
    public String length;
    public String machineWidth;
    public String height;
    public String weight;

    public CncLathe() {}

    public String getBed() {
        return bed;
    }

    public String getCncSystem() {
        return cncSystem;
    }

    public String getCounterSpindle() {
        return counterSpindle;
    }

    public String getMaximumDiameterRotation() {
        return maximumDiameterRotation;
    }

    public String getMaximumSpindleSpeed() {
        return maximumSpindleSpeed;
    }

    public String getMaximumTurningDiameter() {
        return maximumTurningDiameter;
    }

    public String getMaximumWorkpieceLength() {
        return maximumWorkpieceLength;
    }

    public String getSpindleMotorPower() {
        return spindleMotorPower;
    }

    public String getLength() {
        return length;
    }

    public String getTool() {
        return tool;
    }

    public String getToolNumber() {
        return toolNumber;
    }

    public String getTotalMachinePower() {
        return totalMachinePower;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }

    public String getMachineWidth() {
        return machineWidth;
    }

    public String getId() {
        return id;
    }
}
