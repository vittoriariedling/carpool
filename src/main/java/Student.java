//This class stores each student's first name, last name, address, and phone number

public class Student {
    //instance variables
    private String firstName; 
    private String lastName;
    private String address; 
    private double longitude;
    private double latitude; 
    private String phoneNumber; 

    //constructors
    public Student(String firstName, String lastName, String address, String longitude, String latitude, String phoneNumber){
        this.firstName = firstName; 
        this.lastName = lastName;
        this.address = address;
        this.longitude = Double.parseDouble(longitude); //converts from String to double
        this.latitude = Double.parseDouble(latitude);
        this.phoneNumber = phoneNumber; 
    }

    public Student(String firstName, String lastName, String address, String phoneNumber){
        this.firstName = firstName; 
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber; 
    }

    //accessor methods
    public String getFirstName(){
        return firstName; 
    }

    public String getLastName(){
        return lastName; 
    }

    public String getName(){
        return firstName + " " + lastName;
    }

    public String getAddress(){
        return address; 
    }

    public double getLongitude(){
        return longitude;
    }

    public double getLatitude(){
        return latitude;
    }

    public String getPhoneNumber(){
        return phoneNumber; 
    }

    //mutator methods
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setLongitude(String longitude){
        this.longitude = Double.parseDouble(longitude);
    }
    
    public void setLatitude(String latitude){
        this.latitude = Double.parseDouble(latitude);
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    //toString
    @Override
    public String toString(){
        return "\n--------------------" + "\n     New Student" + "\n--------------------" + "\nStudent Name: " + firstName + " " + lastName +  "\nAddress: " + address + "\nStudent Phone Number: " + phoneNumber + "\n";
    }
}