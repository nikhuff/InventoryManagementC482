package model;

public class InHouse extends Part {

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    private int machineId;

    /**
     *
     * @return the machineId
     */
    public int getMachineId() {
        return machineId;
    }

    /**
     *
     * @param machineId the machineId
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}
