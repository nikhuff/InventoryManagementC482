package model;

public class OutSourced extends Part {

    private String companyName;

    /**
     *
     * @return company name
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     *
     * @param companyName company name
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public OutSourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }
}
