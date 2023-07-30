import java.util.Scanner;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Arrays;

public class Main {
    //instance variables
    private Connect connect;
    private boolean changes = false; //indicates if changes to the database have been made and database needs to be saved
    private Scanner input;

    public Main(){
        connect = new Connect();
        input = new Scanner(System.in);
    }

    public void mainMenu() throws IOException, GeneralSecurityException, InterruptedException {

        int selection = 0;

        do{
            System.out.println("--------------------");
            System.out.println("     Main Menu");
            System.out.println("--------------------");
            System.out.println(" 1  Add Student");
            System.out.println(" 2  Edit Student Information");
            System.out.println(" 3  View List of Student Names");
            System.out.println(" 4  Delete Student");
            System.out.println(" 5  Match All Students");
            System.out.println(" 6  Match One Student");
            System.out.println(" 7  Save");
            System.out.println(" 8  Exit");

            System.out.print("What would you like to do? Enter the corresponding number: ");
            selection = input.nextInt();

            if(selection == 1){
                System.out.println("--------------------");
                System.out.println("     Add Student");
                System.out.println("--------------------");
                
                Scanner input2 = new Scanner(System.in);
                System.out.print("Is your student a rider or a driver? Enter r or d: ");
                String rOrD = input2.nextLine();
        
                if(rOrD.equalsIgnoreCase("r")){
                    System.out.print("Enter student's first name: ");
                    String firstName = input2.nextLine();
                    System.out.print("Enter student's last name: ");
                    String lastName = input2.nextLine();
                    String tempAddress = " ";
                    Scanner input6 = new Scanner(System.in);
                    while (!connect.isValidAddress(tempAddress)){
                        System.out.print("Enter student's address including street, city, state abbreviation, and zipcode seperated by commas: ");
                        tempAddress = input6.nextLine();
                        if(!connect.isValidAddress(tempAddress)){
                            System.out.print("This is not a valid address. ");
                        }
                    }
                    String address = tempAddress;
                    System.out.print("Enter student's longitude (if you dont know the longitude, enter 0): ");
                    String longitude = input2.nextLine();
                    System.out.print("Enter student's latitude (if you don't know the latitude, enter 0): ");
                    String latitude = input2.nextLine();
                    System.out.print("Enter student's phone number: ");
                    String phoneNumber = input2.nextLine();
                    String tempEmail = " ";
                    Scanner input7 = new Scanner(System.in);
                    while (!connect.isValidEmail(tempEmail)){
                        System.out.print("Enter student's email: ");
                        tempEmail = input7.nextLine();
                        if(!connect.isValidEmail(tempEmail)){
                            System.out.print("This is not a valid email address. ");
                        }
                    }
                    String email = tempEmail;
                    System.out.print("Enter parent's full name: ");
                    String parentName = input2.nextLine();
                    System.out.print("Enter parent's phone number: ");
                    String parentPhoneNumber = input2.nextLine();
                    System.out.print("Enter parent's email: ");
                    String parentEmail = input2.nextLine();
                    String parentCarpoolString = "";
                    boolean parentCarpool = false;
                    while (!parentCarpoolString.equalsIgnoreCase("yes") && !parentCarpoolString.equalsIgnoreCase("no")){
                        System.out.print("Is this student interested in participating in a parent carpool group? Enter yes or no: ");
                        parentCarpoolString = input2.nextLine();
                        if(!parentCarpoolString.equalsIgnoreCase("yes") && !parentCarpoolString.equalsIgnoreCase("no")){
                            System.out.print("This is not a valid response. ");
                        }
                    }
                    if(parentCarpoolString.equalsIgnoreCase("yes")){
                        parentCarpool = true;
                    }
                    
                    Rider rider = new Rider(firstName, lastName, address, longitude, latitude, phoneNumber, email, parentName, parentPhoneNumber, parentEmail, parentCarpool);
                    Connect.addRider(Connect.amountOfStudents(), rider);
                    
                    changes = true; 

                    System.out.println(Connect.viewStudent(Integer.toString(Connect.amountOfStudents()-1)));

                } else if(rOrD.equalsIgnoreCase("d")){
                    System.out.print("Enter student's first name: ");
                    String firstName = input2.nextLine();
                    System.out.print("Enter student's last name: ");
                    String lastName = input2.nextLine();
                    String tempAddress = " ";
                    Scanner input9 = new Scanner(System.in);
                    while (!connect.isValidAddress(tempAddress)){
                        System.out.print("Enter student's address including street, city, state abbreviation, and zipcode seperated by commas: ");
                        tempAddress = input9.nextLine();
                        if(!connect.isValidAddress(tempAddress)){
                            System.out.print("This is not a valid address. ");
                        }
                    }
                    String address = tempAddress;
                    System.out.print("Enter student's longitude: ");
                    String longitude = input2.nextLine();
                    System.out.print("Enter student's latitude: ");
                    String latitude = input2.nextLine();
                    System.out.print("Enter student's phone number: ");
                    String phoneNumber = input2.nextLine();
                    String tempEmail = " ";
                    Scanner input6 = new Scanner(System.in);
                    while (!connect.isValidEmail(tempEmail)){
                        System.out.print("Enter student's email: ");
                        tempEmail = input6.nextLine();
                        if(!connect.isValidEmail(tempEmail)){
                            System.out.print("This is not a valid email address. ");
                        }
                    }
                    String email = tempEmail;
        
                    Driver driver = new Driver(firstName, lastName, address, longitude, latitude, phoneNumber, email);
                    Connect.addDriver(Connect.amountOfStudents(), driver);

                    changes = true; 

                    System.out.println(Connect.viewStudent(Integer.toString(Connect.amountOfStudents()-1)));
                } else {
                    System.out.println("This is not a valid option, please try again.");
                }
            }
            if(selection == 2){
                Scanner input4 = new Scanner(System.in);
                String name = new String();
                System.out.print("Enter the name of the student you would like to edit: ");
                name = input4.nextLine();
                String theRange = Connect.findStudentRange(name);
                if(theRange.equals("-1")){
                    System.out.println("This student can't be found.");
                }else if (Connect.riderOrDriver(theRange).equals("Driver")){
                    System.out.println(Connect.viewStudent(theRange));
                    System.out.println("--------------------");
                    System.out.println("     Edit Student");
                    System.out.println("--------------------");
                    System.out.println(" 1  Rider/Driver");
                    System.out.println(" 2  First Name");
                    System.out.println(" 3  Last Name");
                    System.out.println(" 4  Address");
                    System.out.println(" 5  Longitude");
                    System.out.println(" 6  Latitude");
                    System.out.println(" 7  Phone Number");
                    System.out.println(" 8  Email");
                    System.out.println("--------------------");
                    System.out.print("What would you like to change? Enter the corresponding number: ");
                    int selection2 = input4.nextInt();
                    Scanner input5 = new Scanner(System.in);
                    String selection3 = " ";
                    if(selection2 == 8){
                        String tempEmail = " ";
                        Scanner input6 = new Scanner(System.in);
                        while (!connect.isValidEmail(tempEmail)){
                            System.out.print("What would you like to replace this with? ");
                            tempEmail = input6.nextLine();
                            if(!connect.isValidEmail(tempEmail)){
                                System.out.print("This is not a valid email address. ");
                            }
                        }
                        selection3 = tempEmail;
                    } else if(selection2 == 4) {
                        String tempAddress = " ";
                        Scanner input10 = new Scanner(System.in);
                        while (!connect.isValidAddress(tempAddress)){
                            System.out.print("Enter student's address including street, city, state abbreviation, and zipcode seperated by commas: ");
                            tempAddress = input10.nextLine();
                            if(!connect.isValidAddress(tempAddress)){
                                System.out.print("This is not a valid address. ");
                            }
                        }
                        selection3 = tempAddress;
                    } else if(selection2 == 1) {
                        Scanner input12 = new Scanner(System.in);
                        String temp = " ";
                        while (!(temp.equalsIgnoreCase("yes") || temp.equalsIgnoreCase("no"))){
                            System.out.println("Would you like to change " + name + " from a driver to a rider? Enter yes or no: ");
                            temp = input12.nextLine();
                            if(!(temp.equalsIgnoreCase("yes") || temp.equalsIgnoreCase("no"))){
                                System.out.println("This is not a valid input. ");
                            } else if (temp.equalsIgnoreCase("yes")) {
                                selection3 = "Rider";
                                Scanner input13 = new Scanner(System.in);
                                System.out.print("Enter parent's full name: ");
                                String parentName = input13.nextLine();
                                System.out.print("Enter parent's phone number: ");
                                String parentPhoneNumber = input13.nextLine();
                                System.out.print("Enter parent's email: ");
                                String parentEmail = input13.nextLine();
                                String parentCarpoolString = "";
                                boolean parentCarpool = false;
                                while (!parentCarpoolString.equalsIgnoreCase("yes") && parentCarpoolString.equalsIgnoreCase("no")){
                                    System.out.print("Is this student interested in participating in a parent carpool group? Enter yes or no: ");
                                    parentCarpoolString = input13.nextLine();
                                    if(!parentCarpoolString.equalsIgnoreCase("yes") && !parentCarpoolString.equalsIgnoreCase("no")){
                                        System.out.print("This is not a valid response. ");
                                    }
                                }
                                if(parentCarpoolString.equalsIgnoreCase("yes")){
                                    parentCarpool = true;
                                }
                            
                                Connect.addRiderFromDriver(Connect.amountOfStudents(), parentName, parentPhoneNumber, parentEmail, parentCarpool);
                            } else if (temp.equalsIgnoreCase("no")) {
                                selection3 = "Driver";
                            }
                        }
                    } else {
                        System.out.print("What would you like to replace this with? ");
                        selection3 = input5.nextLine();
                    }

                    List<List<Object>> values = Arrays.asList(Arrays.asList(selection3));
                    Connect.updateValues(theRange, values, Connect.numToLetter(selection2));
                    changes = true;
                    System.out.println(Connect.viewStudent(theRange));
                } else if (Connect.riderOrDriver(theRange).equals("Rider")){
                    System.out.println(Connect.viewStudent(theRange));
                    System.out.println("--------------------");
                    System.out.println("     Edit Student");
                    System.out.println("--------------------");
                    System.out.println(" 1  Rider/Driver");
                    System.out.println(" 2  First Name");
                    System.out.println(" 3  Last Name");
                    System.out.println(" 4  Address");
                    System.out.println(" 5  Longitude");
                    System.out.println(" 6  Latitude");
                    System.out.println(" 7  Phone Number");
                    System.out.println(" 8  Email");
                    System.out.println(" 9  Parent's Name");
                    System.out.println(" 10  Parent's Email");
                    System.out.println(" 11  Parent's Phone Number");
                    System.out.println(" 12 Carpool Group Status");
                    System.out.println("--------------------");
                    System.out.print("What would you like to change? Enter the corresponding number: ");
                    int selection2 = input4.nextInt();
                    Scanner input6 = new Scanner(System.in);
                    String selection3 = " ";
                    if(selection2 == 8 || selection2 == 10){
                        String tempEmail = " ";
                        Scanner input7 = new Scanner(System.in);
                        while (!connect.isValidEmail(tempEmail)){
                            System.out.print("What would you like to replace this with? ");
                            tempEmail = input7.nextLine();
                            if(!connect.isValidEmail(tempEmail)){
                                System.out.print("This is not a valid email address. ");
                            }
                        }
                        selection3 = tempEmail;
                    } else if(selection2 == 4) {
                        String tempAddress = " ";
                        Scanner input11 = new Scanner(System.in);
                        while (!connect.isValidAddress(tempAddress)){
                            System.out.print("Enter student's address including street, city, state abbreviation, and zipcode seperated by commas: ");
                            tempAddress = input11.nextLine();
                            if(!connect.isValidAddress(tempAddress)){
                                System.out.print("This is not a valid address. ");
                            }
                        }
                        selection3 = tempAddress;
                    } else if (selection2 == 1){
                        Scanner input12 = new Scanner(System.in);
                        String temp = " ";
                        while (!(temp.equalsIgnoreCase("yes") || temp.equalsIgnoreCase("no"))){
                            System.out.println("Would you like to change " + name + " from a rider to a driver? This means deleting all parent information from the record. Enter yes or no: ");
                            temp = input12.nextLine();
                            if(!(temp.equalsIgnoreCase("yes") || temp.equalsIgnoreCase("no"))){
                                System.out.println("This is not a valid input. ");
                            } else if (temp.equalsIgnoreCase("yes")) {
                                selection3 = "Driver";
                                Connect.deleteRiderToDriver(name);
                            } else if (temp.equalsIgnoreCase("no")) {
                                selection3 = "Driver";
                            }
                        }
                    }else if(selection2 == 12){
                        String parentCarpoolString = "";
                        Scanner input8 = new Scanner(System.in);
                        while (!parentCarpoolString.equalsIgnoreCase("yes") && !parentCarpoolString.equalsIgnoreCase("no")){
                            System.out.print("Is this student interested in participating in a parent carpool group? Enter yes or no: ");
                            parentCarpoolString = input8.nextLine();
                            if(!parentCarpoolString.equalsIgnoreCase("yes") && !parentCarpoolString.equalsIgnoreCase("no")){
                                System.out.print("This is not a valid response. ");
                            }
                        }
                        selection3 = parentCarpoolString;
                    } else{
                        System.out.print("What would you like to replace this with? ");
                        selection3 = input6.nextLine();
                    }
                    List<List<Object>> values = Arrays.asList(Arrays.asList(selection3));
                    Connect.updateValues(theRange, values, Connect.numToLetter(selection2));
                    changes = true;
                    System.out.println("\n" + Connect.viewStudent(theRange));
                }
            }
            if (selection == 3){
                Connect.readStudentNames();
            }
            if (selection == 4){
                Scanner input4 = new Scanner(System.in);
                System.out.print("Enter the name of the student you would like to delete: ");
                String name = input4.nextLine();
                String theRange = Connect.findStudentRange(name);
                if(theRange.equals("Student not found")){
                    System.out.println(theRange);
                }else{
                    System.out.println(Connect.viewStudent(theRange));
                    System.out.print("Confirm you would like to permanently delete this student. Enter yes or no: ");
                    String delete = input4.nextLine();
                    if(delete.equalsIgnoreCase("yes")){
                        Connect.deleteStudent(Integer.valueOf(theRange));
                    }else{
                        System.out.println("The student will not be deleted.");
                    }
                }
            }
            if (selection == 5){
                
                if(!Connect.checkValues()){
                    System.out.println("Please update longitude and latitude values and refresh the google sheet page");
                }else{
                    Connect.matchStudents();
                }
            }
            if (selection == 6){
                Scanner input5 = new Scanner(System.in);
                System.out.println("Enter the name of the student you would like to find matches for: ");
                String name = input5.nextLine();
                String theRange = Connect.findStudentRange(name);
                if(theRange.equals("-1")){
                    System.out.println("This student can't be found.");
                }else if(!Connect.checkValues()){
                    System.out.println("Please update longitude and latitude values and refresh the google sheet page");
                }else{
                    Connect.matchOneStudent(theRange);
                }
            }
            if (selection == 7){
                System.out.println("Changes to the database are automatically saved!!");
            }
        }

        while(selection != 8); 

        if(selection == 8){
            if(changes == true){
                Scanner input3 = new Scanner(System.in);
                System.out.print("You have made changes to the database. Changes to the database are automatically saved!! ");
            }
        }
        System.out.println("Goodbye");
        System.exit(0);
    }
    
    public static void main(String... args) throws IOException, GeneralSecurityException, InterruptedException {
        Main app = new Main();
        app.mainMenu();
    }
}