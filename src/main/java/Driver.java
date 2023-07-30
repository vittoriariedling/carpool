/*
 * Child class inherits Student base class, adding additional 
 * information about Drivers, such as additional parent information,
 * which not all Students need to have
 * 
 * Stores rider's email, distance, and index
 */

public class Driver extends Student{
    //instance variables
    private String email; 
    private double distance;
    private int index;

    //constructor
    public Driver(String firstName, String lastName, String address, String longitude, String latitude, String phoneNumber,String email){
        super(firstName, lastName, address, longitude, latitude, phoneNumber);
        this.email = email;
    }

    public Driver(String firstName, String lastName, String address, String phoneNumber, String email, double distance, int index){
        super(firstName, lastName, address, phoneNumber);
        this.email = email;
        this.distance = distance;
        this.index = index;
    }

    //accessor methods
    public String getEmail(){
        return email;
    }

    public double getDistance() {
        return distance;
    }

    public int getIndex() {
        return index;
    }

    //mutator methods
    public void setEmail(String email){
        this.email = email;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }

    //toString
    public String toString(){
        return "\n--------------------" + "\n     New Student " + "\n--------------------" + "\nStudent Name: " + super.getFirstName()  + " " + super.getLastName() + "\nAddress: " + super.getAddress() + "\nStudent Phone Number: " + super.getPhoneNumber() + "\nStudent Email: " + email + "\n";
    }
}