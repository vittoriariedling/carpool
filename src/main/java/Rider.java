/*
 * Child class inherits Student base class, adding additional 
 * information about Riders, such as additional parent information,
 * which not all Students need to have
 * 
 * Stores rider's email, parent's first and last name, parent's 
 * email, parent's phone number, and indication of participation 
 * in parent carpool group
 */

public class Rider extends Student{
    //instance variables
    private String email; 
    private String parentName; 
    private String parentEmail;
    private String parentPhoneNumber; 
    private boolean parentCarpool; //indicates interest in a parent driven carpool group
    private String parentCarpoolPrint;
    private double distance;
    private int index;

    //constructor
    public Rider(String firstName, String lastName, String address, String longitude, 
        String latitude, String phoneNumber, String email, String parentName,  
        String parentPhoneNumber, String parentEmail,  boolean parentCarpool){
            super(firstName, lastName, address, longitude, latitude, phoneNumber);
            this.email = email;
            this.parentName = parentName;
            this.parentEmail = parentEmail;
            this.parentPhoneNumber = parentPhoneNumber;
            this.parentCarpool = parentCarpool;
    }

    public Rider(String firstName, String lastName, String address, String phoneNumber, String email, String parentName,  String parentPhoneNumber, String parentEmail,  double distance, int index){
        super(firstName, lastName, address, phoneNumber);
        this.email = email;
        this.parentName = parentName;
        this.parentEmail = parentEmail;
        this.parentPhoneNumber = parentPhoneNumber;
        this.distance = distance;
        this.index = index;
    }

    //accessor methods
    public String getParentName(){
        return parentName; 
    }

    public String getParentPhoneNumber(){
        return parentPhoneNumber; 
    }

    public String getParentInfo(){
        return parentName + " (" + parentPhoneNumber + ", " + parentEmail + ")";
    }
    
    public String getEmail(){
        return email; 
    }

    public String getParentEmail(){
        return parentEmail; 
    }

    public String getParentCarpool(){
        if(parentCarpool){
            parentCarpoolPrint = "yes";
        }
        else{
            parentCarpoolPrint = "no";
        }
        return parentCarpoolPrint; 
    }

    public double getDistance() {
        return distance;
    }

    public int getIndex() {
        return index;
    }

    public String getParentCarpoolInfo() {
        return "Student Name: " + super.getFirstName()  + " " + super.getLastName() + "\nAddress: " + super.getAddress() + "\nStudent Phone Number: " + super.getPhoneNumber() + "\nStudent Email: " + email +"\nParent Name: " + parentName + "\nParent Phone Number: " + parentPhoneNumber + "\nParent Email: " + parentEmail;
    }

    //mutator methods
    public void setParentName(String parentName){
        this.parentName = parentName;
    }

    public void setParentPhoneNumber(String parentPhoneNumber){
        this.parentPhoneNumber = parentPhoneNumber;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setParentEmail(String parentEmail){
        this.parentEmail = parentEmail;
    }

    public void setParentCarpool(boolean parentCarpool){
        this.parentCarpool = parentCarpool;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
    
    public void setIndex(int index) {
        this.index = index;
    };

    //toString
    public String toString(){
        if(parentCarpool){
            parentCarpoolPrint = "yes";
        }
        else{
            parentCarpoolPrint = "no";
        }
        return "\n--------------------" + "\n     New Student" + "\n--------------------" + "\nStudent Name: " + super.getFirstName()  + " " + super.getLastName() + "\nAddress: " + super.getAddress() + "\nStudent Phone Number: " + super.getPhoneNumber() + "\nStudent Email: " + email +"\nParent Name: " + parentName + "\nParent Phone Number: " + parentPhoneNumber + "\nParent Email: " + parentEmail + "\nInterest in Parent Carpool Group? " + parentCarpoolPrint + "\n";
    }
}