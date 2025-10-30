package practice_java;

public class Customer {
    private int cust_id;
    private String cust_name;
    private String ph_no;
    private String location;

    // Constructor
    public Customer(int cust_id, String cust_name, String ph_no, String location) {
        this.cust_id = cust_id;
        this.cust_name = cust_name;
        this.ph_no = ph_no;
        this.location = location;
    }

    // Getters and Setters
    public int getCust_id() {
        return cust_id;
    }

    public void setCust_id(int cust_id) {
        this.cust_id = cust_id;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getPh_no() {
        return ph_no;
    }

    public void setPh_no(String ph_no) {
        this.ph_no = ph_no;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
