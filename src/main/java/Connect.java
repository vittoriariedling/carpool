import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.DeleteDimensionRequest;
import com.google.api.services.sheets.v4.model.DimensionRange;
import com.google.api.services.sheets.v4.model.Request;
import me.tongfei.progressbar.ProgressBar;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;


public class Connect {
  //instance variables
  private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
  private static final String TOKENS_DIRECTORY_PATH = "tokens";
  private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
  private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

  /**
   * Creates an authorized Credential object.
   *
   * @param HTTP_TRANSPORT The network HTTP Transport.
   * @return An authorized Credential object.
   * @throws IOException If the credentials.json file cannot be found.
   */
  private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
      throws IOException {
    // Load client secrets.
    InputStream in = Connect.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
    //InputStream in = new FileInputStream(CREDENTIALS_FILE_PATH);

    if (in == null) {
      throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
    }
    GoogleClientSecrets clientSecrets =
        GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

    // Build flow and trigger user authorization request.
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
        .setAccessType("offline")
        .build();
    LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
    return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
  }


  /**
   * Creates a method to print a list of the student names
   *
   * @throws IOException If the credentials.json file cannot be found.
   * @throws GeneralSecurityException If the credentials.json file cannot be found.
   */
  public static void readStudentNames()throws IOException, GeneralSecurityException{
    // Build a new authorized API client service.
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1FMam_f9V1kPlU99GPtmPpVj46KafconuUemA_1XmZ4A";
    final String range = "Carpool!B2:D";
    Sheets service =
        new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build();
    ValueRange response = service.spreadsheets().values()
        .get(spreadsheetId, range)
        .execute();
    List<List<Object>> values = response.getValues(); //stores data from each cell as an object
    if (values == null || values.isEmpty()) {
      System.out.println("No data found.");
    } else {
      System.out.println("--------------------");
            System.out.println("View List of Student Names");
            System.out.println("--------------------");
      System.out.println("Name, Type");
      for (List row : values) {
        // print columns B, C and D, which correspond to indices 0, 1, and 2 respectively
        System.out.printf("%s %s, %s\n", row.get(1), row.get(2), row.get(0));
      }
    }
  }

  public static String findStudentRange(String name)throws IOException, GeneralSecurityException{
    // Build a new authorized API client service.
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1FMam_f9V1kPlU99GPtmPpVj46KafconuUemA_1XmZ4A";
    final String range = "Carpool!C2:D";
    int count = 1; //starts at one bc student info in sheets start at 2
    Sheets service =
        new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build();
    ValueRange response = service.spreadsheets().values()
        .get(spreadsheetId, range)
        .execute();
    List<List<Object>> values = response.getValues();
    if (values == null || values.isEmpty()) {
      System.out.println("No data found.");
      return "No data found.";
    } else {
      for (List row : values) {
        count = count + 1;
        if(name.equalsIgnoreCase(row.get(0) + " " + row.get(1))){
          return "" + count; //student's location in sheet
        }
      }
      try {
          throw new Exception("Name not found in the List");
      } catch (Exception e) {
          System.out.println("Error: " + e.getMessage());
          return "-1";
      }
    }
  }

  public static String viewStudent(String count)throws IOException, GeneralSecurityException{
    // Build a new authorized API client service.
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1FMam_f9V1kPlU99GPtmPpVj46KafconuUemA_1XmZ4A";
    String range = "Carpool!B" + count + ":M";
    Sheets service =
        new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build();
    ValueRange response = service.spreadsheets().values()
        .get(spreadsheetId, range)
        .execute();
    List<List<Object>> values = response.getValues();
    if (values == null || values.isEmpty()) {
      System.out.println("No data found.");
    } else {
      if (values == null || values.isEmpty()) {
        System.out.println("No data found.");
      } else {
        System.out.println("\n--------------------");
            System.out.println("View Student Info");
            System.out.println("--------------------");
        for (List row : values) {
          if (((String) row.get(0)).equalsIgnoreCase("rider")){
            return "Name: " + row.get(1) + " " + row.get(2) +", " + row.get(0) + "\nAddress: " + row.get(3) + "\nPhone Number: " + row.get(6) + "\nEmail: " + row.get(7) + "\nParent Name: " + row.get(8) + "\nParent Email: " + row.get(9) + "\nParent Phone Number: " + row.get(10) + "\nInterest in Carpool Group? " + row.get(11); 
          }
          else {
            return "Name: " + row.get(1) + " " + row.get(2) +", " + row.get(0) + "\nAddress: " + row.get(3) + "\nPhone Number: " + row.get(6) + "\nEmail: " + row.get(7); 
          }
        }
      }
    }
    return "Student is not at this range";
  }

  public static String riderOrDriver(String count)throws IOException, GeneralSecurityException{
    // Build a new authorized API client service.
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1FMam_f9V1kPlU99GPtmPpVj46KafconuUemA_1XmZ4A";
    String range = "Carpool!B" + count + ":C";
    Sheets service =
        new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build();
    ValueRange response = service.spreadsheets().values()
        .get(spreadsheetId, range)
        .execute();
    List<List<Object>> values = response.getValues();
    if (values == null || values.isEmpty()) {
      System.out.println("No data found.");
    } else {
      for (List row : values) {
        if (((String) row.get(0)).equalsIgnoreCase("rider")){
          return "Rider";
        }
        else {
          return "Driver";
        }
      }
    }
    return "Student is not at this range";
  }

  public static String getName(String count)throws IOException, GeneralSecurityException{
    // Build a new authorized API client service.
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1FMam_f9V1kPlU99GPtmPpVj46KafconuUemA_1XmZ4A";
    String range = "Carpool!B" + count + ":D";
    Sheets service =
        new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build();
    ValueRange response = service.spreadsheets().values()
        .get(spreadsheetId, range)
        .execute();
    List<List<Object>> values = response.getValues();
    if (values == null || values.isEmpty()) {
      System.out.println("No data found.");
    } else {
      for (List row : values) {
        return row.get(1) + " " + row.get(2);
      }
    }
    return "Student is not at this range";
  }

  /**
   * Validates email is in correct format.
   *
   * @param email the email address that user inputs.
   * @return a boolean that reflects if the email is valid or not.
   */
  public boolean isValidEmail(String email) {
    // Regular expression for a valid email address
    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                        "[a-zA-Z0-9_+&*-]+)*@" + 
                        "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    
    Pattern pattern = Pattern.compile(emailRegex);
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
  }

  public static boolean isValidAddress(String address) {
    String[] parts = address.split(",");
    if (parts.length < 4) {
        return false;
    }
    String streetAddress = parts[0].trim();
    String city = parts[1].trim();
    String state = parts[2].trim();
    String zipCode = parts[3].trim();
    if (streetAddress.isEmpty() || !streetAddress.matches("\\d+\\s+\\w+\\s+\\w+")) {
        return false;
    }
    if (city.isEmpty() || !city.matches("\\w+")) {
        return false;
    }
    if (state.isEmpty() || !state.matches("\\w{2}")) {
        return false;
    }
    if (zipCode.isEmpty() || !zipCode.matches("\\d{5}(-\\d{4})?")) {
        return false;
    }
    return true;
  }

  public static List<List<Object>> createValuesList(String count)throws IOException, GeneralSecurityException{
    // Build a new authorized API client service.
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1FMam_f9V1kPlU99GPtmPpVj46KafconuUemA_1XmZ4A";
    String range = "Carpool!A" + count + ":C";
    String newRange = range;
    List<List<Object>> valuesUpdated; // = Arrays.asList;
    Sheets service =
        new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build();
    ValueRange response = service.spreadsheets().values()
        .get(spreadsheetId, range)
        .execute();
    List<List<Object>> values = response.getValues();
    if (values == null || values.isEmpty()) {
      System.out.println("No data found.");
      newRange = range;
    } else {
      for (List row : values) {
        if (row.get(0).equals("rider")){
          newRange = "Carpool!A" + count + ":J";
        }
        else if (row.get(0).equals("driver")) {
          newRange = "Carpool!A" + count + ":F";
        }
        break;
      }
      response = service.spreadsheets().values()
        .get(spreadsheetId, newRange)
        .execute();
      values = response.getValues();
      if (values == null || values.isEmpty()) {
        System.out.println("No data found.");
      } else {
        for (List row : values) {
          if (row.get(0).equals("rider")){
            valuesUpdated = Arrays.asList(Arrays.asList(row.get(0), row.get(1), row.get(2), row.get(3), row.get(4), row.get(5), row.get(6), row.get(7), row.get(8), row.get(9)));
            return valuesUpdated;
          }
          else {
            valuesUpdated = Arrays.asList(Arrays.asList(row.get(0), row.get(1), row.get(2), row.get(3), row.get(4), row.get(5)));
            return valuesUpdated;
          }
        }
      }
    }
    valuesUpdated = Arrays.asList(Arrays.asList("unable to update"));
    return valuesUpdated;
  }

  /**
  * Sets values in a range of a spreadsheet.
  *
  * @param values           - List of rows of values to input.
  * @return spreadsheet with updated values
  * @throws IOException - if credentials file not found.
  * @throws GeneralSecurityException - if secure communication not established with API client.
  */
  public static UpdateValuesResponse updateValues(String count, List<List<Object>> values, String rangeStart)
    throws IOException, GeneralSecurityException {
      final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
      final String spreadsheetId = "1FMam_f9V1kPlU99GPtmPpVj46KafconuUemA_1XmZ4A";
      String range = "Carpool!" + rangeStart + count + ":" + Connect.nextLetters(rangeStart);
      String valueInputOption = "RAW";

      // Create the sheets API client
      Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY,
          //GsonFactory.getDefaultInstance(),  perhaps JSON_FACTORY
          getCredentials(HTTP_TRANSPORT))
          .setApplicationName(APPLICATION_NAME)
          .build();

      UpdateValuesResponse result = null;
      try {
        // Updates the values in the specified range.
        ValueRange body = new ValueRange()
            .setValues(values);
        result = service.spreadsheets().values().update(spreadsheetId, range, body)
            .setValueInputOption(valueInputOption)
            .execute();
        System.out.printf("%d cell updated.", result.getUpdatedCells());
      } 
      catch (GoogleJsonResponseException e) {
        GoogleJsonError error = e.getDetails();
        if (error.getCode() == 403 && error.getMessage().equals("Insufficient Permission")) {
            System.out.println("Please check if you have the correct permissions to access the sheet.");
        } else if (error.getCode() == 404) {
            System.out.println("Please check if the sheet ID is correct.");
        } else if (error.getCode() == 400) {
            System.out.println("Please check if the request is correctly formatted.");
        } else {
            System.out.println("An error occurred: " + error.getMessage());
        }
      }
      return result;
  }

  public static String numToLetter(int num){
    if (num == 1)
      return "B";
    else if (num == 2)
      return "C";
    else if (num == 3)
      return "D";
    else if (num == 4)
      return "E";
    else if (num == 5)
      return "F";
    else if (num == 6)
      return "G";
    else if (num == 7)
      return "H";
    else if (num == 8)
      return "I";
    else if (num == 9)
      return "J";
    else if (num == 10)
      return "K";
    else if (num == 11)
      return "L";
    else
      return "M";
  }

  public static String nextLetters(String letter){
    if (letter == "A")
      return "B";
    else if (letter == "B")
      return "C";
    else if (letter == "C")
      return "D";
    else if (letter == "D")
      return "E";
    else if (letter == "E")
      return "F";
    else if (letter == "F")
      return "G";
    else if (letter == "G")
      return "H";
    else if (letter == "H")
      return "I";
    else if (letter == "I")
      return "J";
    else if (letter == "J")
      return "K";
    else if (letter == "K")
      return "L";
    else if (letter == "L")
      return "M";
    else 
      return "N";
  }
  
  public static int amountOfStudents()throws IOException, GeneralSecurityException{
    // Build a new authorized API client service.
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1FMam_f9V1kPlU99GPtmPpVj46KafconuUemA_1XmZ4A";
    final String range = "Carpool!B2:C";
    int count = 1; //starts at one bc student info in sheets start at 2
    Sheets service =
        new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build();
    ValueRange response = service.spreadsheets().values()
        .get(spreadsheetId, range)
        .execute();
    List<List<Object>> values = response.getValues();
    if (values == null || values.isEmpty()) {
      System.out.println("No data found.");
      return 0;
    } else {
      for (List row : values) {
        count = count + 1;
      }
    }
    return count+1;
  }

  public static UpdateValuesResponse addRider(int count, Rider student)throws IOException, GeneralSecurityException {
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1FMam_f9V1kPlU99GPtmPpVj46KafconuUemA_1XmZ4A";
    String range = "Carpool!A" + count + ":M";
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
    String formattedDateTime = now.format(formatter);
    List<List<Object>> values = Arrays.asList(Arrays.asList(formattedDateTime, "Rider", student.getFirstName(), student.getLastName(), student.getAddress(), student.getLongitude(), student.getLatitude(), student.getPhoneNumber(), student.getEmail(), student.getParentName(), student.getParentPhoneNumber(), student.getParentEmail(), student.getParentCarpool()));
    String valueInputOption = "RAW";

    // Create the sheets API client
    Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY,
        //GsonFactory.getDefaultInstance(),  perhaps JSON_FACTORY
        getCredentials(HTTP_TRANSPORT))
        .setApplicationName(APPLICATION_NAME)
        .build();

    UpdateValuesResponse result = null;
    try {
      // Updates the values in the specified range.
      ValueRange body = new ValueRange()
          .setValues(values);
      result = service.spreadsheets().values().update(spreadsheetId, range, body)
          .setValueInputOption(valueInputOption)
          .execute();
      System.out.printf("%d cells updated.\n", result.getUpdatedCells());
    } 
    catch (GoogleJsonResponseException e) {
      GoogleJsonError error = e.getDetails();
      if (error.getCode() == 403 && error.getMessage().equals("Insufficient Permission")) {
          System.out.println("Please check if you have the correct permissions to access the sheet.");
      } else if (error.getCode() == 404) {
          System.out.println("Please check if the sheet ID is correct.");
      } else if (error.getCode() == 400) {
          System.out.println("Please check if the request is correctly formatted.");
      } else {
          System.out.println("An error occurred: " + error.getMessage());
      }
    }
    return result;
  }

  public static UpdateValuesResponse addDriver(int count, Driver student)throws IOException, GeneralSecurityException {
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1FMam_f9V1kPlU99GPtmPpVj46KafconuUemA_1XmZ4A";
    String range = "Carpool!A" + count + ":I";
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
    String formattedDateTime = now.format(formatter);
    List<List<Object>> values = Arrays.asList(Arrays.asList(formattedDateTime, "Driver", student.getFirstName(), student.getLastName(), student.getAddress(), student.getLongitude(), student.getLatitude(), student.getPhoneNumber(), student.getEmail()));
    String valueInputOption = "RAW";

    // Create the sheets API client
    Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY,
        //GsonFactory.getDefaultInstance(),  perhaps JSON_FACTORY
        getCredentials(HTTP_TRANSPORT))
        .setApplicationName(APPLICATION_NAME)
        .build();

    UpdateValuesResponse result = null;
    try {
      // Updates the values in the specified range.
      ValueRange body = new ValueRange()
          .setValues(values);
      result = service.spreadsheets().values().update(spreadsheetId, range, body)
          .setValueInputOption(valueInputOption)
          .execute();
      System.out.printf("%d cells updated.\n", result.getUpdatedCells());
    } 
    catch (GoogleJsonResponseException e) {
      GoogleJsonError error = e.getDetails();
      if (error.getCode() == 403 && error.getMessage().equals("Insufficient Permission")) {
          System.out.println("Please check if you have the correct permissions to access the sheet.");
      } else if (error.getCode() == 404) {
          System.out.println("Please check if the sheet ID is correct.");
      } else if (error.getCode() == 400) {
          System.out.println("Please check if the request is correctly formatted.");
      } else {
          System.out.println("An error occurred: " + error.getMessage());
      }
    }
    return result;
  }

  public static UpdateValuesResponse addRiderFromDriver(int count, String parentName, String parentPhoneNumber, String parentEmail, boolean parentCarpool)throws IOException, GeneralSecurityException {
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1FMam_f9V1kPlU99GPtmPpVj46KafconuUemA_1XmZ4A";
    String range = "Carpool!J" + count + ":M";
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
    String formattedDateTime = now.format(formatter);
    List<List<Object>> values = Arrays.asList(Arrays.asList(parentName, parentPhoneNumber, parentEmail, parentCarpool));
    String valueInputOption = "RAW";

    // Create the sheets API client
    Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY,
        //GsonFactory.getDefaultInstance(),  perhaps JSON_FACTORY
        getCredentials(HTTP_TRANSPORT))
        .setApplicationName(APPLICATION_NAME)
        .build();

    UpdateValuesResponse result = null;
    try {
      // Updates the values in the specified range.
      ValueRange body = new ValueRange()
          .setValues(values);
      result = service.spreadsheets().values().update(spreadsheetId, range, body)
          .setValueInputOption(valueInputOption)
          .execute();
      System.out.printf("%d cells updated.\n", result.getUpdatedCells());
    } 
    catch (GoogleJsonResponseException e) {
      GoogleJsonError error = e.getDetails();
      if (error.getCode() == 403 && error.getMessage().equals("Insufficient Permission")) {
          System.out.println("Please check if you have the correct permissions to access the sheet.");
      } else if (error.getCode() == 404) {
          System.out.println("Please check if the sheet ID is correct.");
      } else if (error.getCode() == 400) {
          System.out.println("Please check if the request is correctly formatted.");
      } else {
          System.out.println("An error occurred: " + error.getMessage());
      }
    }
    return result;
  }

  public static void deleteStudent(int rowToDelete) throws IOException, GeneralSecurityException{
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1FMam_f9V1kPlU99GPtmPpVj46KafconuUemA_1XmZ4A";

    Sheets sheetsService = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build();

    Sheets.Spreadsheets.Get request = sheetsService.spreadsheets().get(spreadsheetId);
    Spreadsheet spreadsheet = request.execute();
    List<Sheet> sheets = spreadsheet.getSheets();
    int sheetId = 0;

    for (Sheet sheet : sheets) {
        String sheetName = sheet.getProperties().getTitle();
        if (sheetName.equals("Sheet1")) {
            sheetId = sheet.getProperties().getSheetId();
        }
    }

    BatchUpdateSpreadsheetRequest content = new BatchUpdateSpreadsheetRequest();
    Request request2 = new Request()
        .setDeleteDimension(new DeleteDimensionRequest()
          .setRange(new DimensionRange()
            .setSheetId(sheetId)
            .setDimension("ROWS")
            .setStartIndex(rowToDelete-1)
            .setEndIndex(rowToDelete)
          )
        );

    List<Request> requests = new ArrayList<Request>();
    requests.add(request2);
    content.setRequests(requests);

    try {
        sheetsService.spreadsheets().batchUpdate(spreadsheetId, content).execute();
        System.out.println("Student deleted.");

    } catch (IOException e) {
        e.printStackTrace();
    }
  }

  public static UpdateValuesResponse deleteRiderToDriver(String name)throws IOException, GeneralSecurityException {
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1FMam_f9V1kPlU99GPtmPpVj46KafconuUemA_1XmZ4A";
    int count = Integer.parseInt(Connect.findStudentRange(name));
    String range = "Carpool!J" + count + ":M";
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
    String formattedDateTime = now.format(formatter);
    List<List<Object>> values = Arrays.asList(Arrays.asList("", "", "", ""));
    String valueInputOption = "RAW";

    // Create the sheets API client
    Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY,
        //GsonFactory.getDefaultInstance(),  perhaps JSON_FACTORY
        getCredentials(HTTP_TRANSPORT))
        .setApplicationName(APPLICATION_NAME)
        .build();

    UpdateValuesResponse result = null;
    try {
      // Updates the values in the specified range.
      ValueRange body = new ValueRange()
          .setValues(values);
      result = service.spreadsheets().values().update(spreadsheetId, range, body)
          .setValueInputOption(valueInputOption)
          .execute();
      System.out.printf("%d cells updated.\n", result.getUpdatedCells());
    } 
    catch (GoogleJsonResponseException e) {
      GoogleJsonError error = e.getDetails();
      if (error.getCode() == 403 && error.getMessage().equals("Insufficient Permission")) {
          System.out.println("Please check if you have the correct permissions to access the sheet.");
      } else if (error.getCode() == 404) {
          System.out.println("Please check if the sheet ID is correct.");
      } else if (error.getCode() == 400) {
          System.out.println("Please check if the request is correctly formatted.");
      } else {
          System.out.println("An error occurred: " + error.getMessage());
      }
    }
    return result;
  }

  public static String getLon(String count)throws IOException, GeneralSecurityException{
    // Build a new authorized API client service.
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1FMam_f9V1kPlU99GPtmPpVj46KafconuUemA_1XmZ4A";
    String range = "Carpool!B" + count + ":G";
    Sheets service =
        new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build();
    ValueRange response = service.spreadsheets().values()
        .get(spreadsheetId, range)
        .execute();
    List<List<Object>> values = response.getValues();
    if (values == null || values.isEmpty()) {
      System.out.println("No data found.");
    } else {
      for (List row : values) {
        return String.valueOf(row.get(5));
      }
    }
    return "0";
  }

  public static String getLat(String count)throws IOException, GeneralSecurityException{
    // Build a new authorized API client service.
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1FMam_f9V1kPlU99GPtmPpVj46KafconuUemA_1XmZ4A";
    String range = "Carpool!B" + count + ":G";
    Sheets service =
        new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build();
    ValueRange response = service.spreadsheets().values()
        .get(spreadsheetId, range)
        .execute();
    List<List<Object>> values = response.getValues();
    if (values == null || values.isEmpty()) {
      System.out.println("No data found.");
    } else {
      for (List row : values) {
        return String.valueOf(row.get(4));
      }
    }
    return "0";
  }

  public static String getAddress(String count)throws IOException, GeneralSecurityException{
    // Build a new authorized API client service.
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1FMam_f9V1kPlU99GPtmPpVj46KafconuUemA_1XmZ4A";
    String range = "Carpool!B" + count + ":I";
    Sheets service =
        new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build();
    ValueRange response = service.spreadsheets().values()
        .get(spreadsheetId, range)
        .execute();
    List<List<Object>> values = response.getValues();
    if (values == null || values.isEmpty()) {
      System.out.println("No data found.");
    } else {
      for (List row : values) {
        return String.valueOf(row.get(3));
      }
    }
    return "0";
  }

  public static String getNumber(String count)throws IOException, GeneralSecurityException{
    // Build a new authorized API client service.
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1FMam_f9V1kPlU99GPtmPpVj46KafconuUemA_1XmZ4A";
    String range = "Carpool!B" + count + ":I";
    Sheets service =
        new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build();
    ValueRange response = service.spreadsheets().values()
        .get(spreadsheetId, range)
        .execute();
    List<List<Object>> values = response.getValues();
    if (values == null || values.isEmpty()) {
      System.out.println("No data found.");
    } else {
      for (List row : values) {
        return String.valueOf(row.get(6));
      }
    }
    return "0";
  }

  public static String getEmail(String count)throws IOException, GeneralSecurityException{
    // Build a new authorized API client service.
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1FMam_f9V1kPlU99GPtmPpVj46KafconuUemA_1XmZ4A";
    String range = "Carpool!B" + count + ":I";
    Sheets service =
        new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build();
    ValueRange response = service.spreadsheets().values()
        .get(spreadsheetId, range)
        .execute();
    List<List<Object>> values = response.getValues();
    if (values == null || values.isEmpty()) {
      System.out.println("No data found.");
    } else {
      for (List row : values) {
        return String.valueOf(row.get(7));
      }
    }
    return "0";
  }

  public static boolean checkValues()throws IOException, GeneralSecurityException{
    // Build a new authorized API client service.
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1FMam_f9V1kPlU99GPtmPpVj46KafconuUemA_1XmZ4A";
    final String range = "Carpool!F2:G";
    boolean noZeros = true;
    Sheets service =
        new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build();
    ValueRange response = service.spreadsheets().values()
        .get(spreadsheetId, range)
        .execute();
    List<List<Object>> values = response.getValues();
    if (values == null || values.isEmpty()) {
      System.out.println("No data found.");
    } else {
      for (List row : values) {
        if(String.valueOf(row.get(0)).equals("0") || String.valueOf(row.get(1)).equals("0")){
          noZeros = false;
        }
      }
    }
    return noZeros;
  }



  public static double distance(double lon1, double lat1, double lon2, double lat2){
    lon1 = Math.toRadians(lon1);
    lat1 = Math.toRadians(lat1);
    lon2 = Math.toRadians(lon2);
    lat2 = Math.toRadians(lat2);

    //haversine formula
    double dlon = lon2 - lon1;
    double dlat = lat2 - lat1;
    double a = Math.pow(Math.sin(dlat / 2), 2) +Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
    double c = 2 * Math.asin(Math.sqrt(a));
    double r = 3956.0; //radius of the earth
    return c*r;
  }

  public static Rider[] compareToRiders(String name, String count) throws IOException, GeneralSecurityException{
    // Build a new authorized API client service.
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1FMam_f9V1kPlU99GPtmPpVj46KafconuUemA_1XmZ4A";
    final String range = "Carpool!B2:AR";
    Rider[] closestRiders = new Rider[5];
    int i = 1;
    int minDistanceIndex = -1;
    double minDistance = -1;
    double lon1 = Double.parseDouble(Connect.getLon(count));
    double lat1 = Double.parseDouble(Connect.getLat(count));
    Sheets service =
        new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build();
    ValueRange response = service.spreadsheets().values()
        .get(spreadsheetId, range)
        .execute();
    List<List<Object>> values = response.getValues();
    if (values == null || values.isEmpty()) {
      System.out.println("No data found.");
    }else {
      for (List row : values) {
        i = i + 1;
        if(!(row.get(1) + " " + row.get(2)).equalsIgnoreCase(name) && ((String)row.get(0))
          .equalsIgnoreCase("rider")){ //
            double distance = Connect.distance(lon1, lat1, Double.parseDouble(String.valueOf(row.get(5))), 
              Double.parseDouble(String.valueOf(row.get(4))));
            Rider rider = new Rider((String)row.get(1), (String)row.get(2), (String)row.get(3), 
              (String)row.get(6), (String)row.get(7), (String)row.get(8), (String)row.get(9), 
              (String)row.get(10), distance, i);
            int j = 0;
            while (j < closestRiders.length && closestRiders[j] != null && closestRiders[j]
              .getDistance() < rider.getDistance()) {
                j++;
            }
            
            if (j < closestRiders.length) {
              for (int k = closestRiders.length - 1; k > j; k--) {
                closestRiders[k] = closestRiders[k - 1];
              }
              closestRiders[j] = rider;
            }
        }
      }
    }
    return closestRiders;
  }

  public static Driver[] compareToDrivers(String name, String count) throws IOException, GeneralSecurityException{
    // Build a new authorized API client service.
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1FMam_f9V1kPlU99GPtmPpVj46KafconuUemA_1XmZ4A";
    final String range = "Carpool!B2:AR";
    Driver[] closestDrivers = new Driver[5];
    int i = 1;
    int minDistanceIndex = -1;
    double minDistance = -1;
    double lon1 = Double.parseDouble(Connect.getLon(count));
    double lat1 = Double.parseDouble(Connect.getLat(count));
    Sheets service =
        new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build();
    ValueRange response = service.spreadsheets().values()
        .get(spreadsheetId, range)
        .execute();
    List<List<Object>> values = response.getValues();
    if (values == null || values.isEmpty()) {
      System.out.println("No data found.");
    }else {
      for (List row : values) {
        i = i + 1;
        if(!(row.get(1) + " " + row.get(2)).equalsIgnoreCase(name) && ((String)row.get(0)).equalsIgnoreCase("Driver")){ //
          double distance = Connect.distance(lon1, lat1, Double.parseDouble(String.valueOf(row.get(5))), Double.parseDouble(String.valueOf(row.get(4))));
          Driver driver = new Driver((String)row.get(1), (String)row.get(2), (String)row.get(3), (String)row.get(6), (String)row.get(7), distance, i);
          int j = 0; 
          while (j < closestDrivers.length && closestDrivers[j] != null && closestDrivers[j].getDistance() < driver.getDistance()) {
            j++;
          }
          
          if (j < closestDrivers.length) {
            for (int k = closestDrivers.length - 1; k > j; k--) {
              closestDrivers[k] = closestDrivers[k - 1];
            }
            closestDrivers[j] = driver;
          }
        }
      }
    }
    return closestDrivers;
  }

  public static Rider[] compareToParentCarpoolers(String name, String count) throws IOException, GeneralSecurityException{
    // Build a new authorized API client service.
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1FMam_f9V1kPlU99GPtmPpVj46KafconuUemA_1XmZ4A";
    final String range = "Carpool!B2:AW";
    Rider[] closestRiders = new Rider[5];
    int i = 1;
    int minDistanceIndex = -1;
    double minDistance = -1;
    double lon1 = Double.parseDouble(Connect.getLon(count));
    double lat1 = Double.parseDouble(Connect.getLat(count));
    Sheets service =
        new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build();
    ValueRange response = service.spreadsheets().values()
        .get(spreadsheetId, range)
        .execute();
    List<List<Object>> values = response.getValues();
    if (values == null || values.isEmpty()) {
      System.out.println("No data found.");
    }else {
      for (List row : values) {
        i = i + 1;
        if(!(row.get(1) + " " + row.get(2)).equalsIgnoreCase(name) && (((String)row.get(0)).equalsIgnoreCase("rider") && ((String)row.get(11)).equalsIgnoreCase("yes"))){ //
          double distance = Connect.distance(lon1, lat1, Double.parseDouble(String.valueOf(row.get(5))), Double.parseDouble(String.valueOf(row.get(4))));
          Rider rider = new Rider((String)row.get(1), (String)row.get(2), (String)row.get(3), (String)row.get(6), (String)row.get(7), (String)row.get(8), (String)row.get(9), (String)row.get(10), distance, i);
          int j = 0;
          while (j < closestRiders.length && closestRiders[j] != null && closestRiders[j].getDistance() < rider.getDistance()) {
            j++;
          }
          
          if (j < closestRiders.length) {
            for (int k = closestRiders.length - 1; k > j; k--) {
              closestRiders[k] = closestRiders[k - 1];
            }
            closestRiders[j] = rider;
          }
        }
      }
    }
    return closestRiders;
  }

  public static void matchStudents() throws IOException, GeneralSecurityException, InterruptedException {
    // Build a new authorized API client service.
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1FMam_f9V1kPlU99GPtmPpVj46KafconuUemA_1XmZ4A";
    final String range = "Carpool!B2:O";
    String name = "";
    Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build();
    ValueRange response = service.spreadsheets().values()
            .get(spreadsheetId, range)
            .execute();
    List<List<Object>> values = response.getValues();
    if (values == null || values.isEmpty()) {
        System.out.println("No data found.");
    } else {
        ProgressBar bar = new ProgressBar("Matching Students", values.size());
        for (int i = 0; i < values.size(); i++) {
            List<Object> row = values.get(i);
            name = String.valueOf(row.get(1) + " " + row.get(2));
            if (String.valueOf(row.get(0)).equalsIgnoreCase("rider")) {
              try {
                Connect.addRiderMatches(Connect.findStudentRange(name),
                        Connect.compareToDrivers(name, Connect.findStudentRange(name)));
              } catch (GoogleJsonResponseException e) {
                  GoogleJsonError error = e.getDetails();
                  if (error.getCode() == 403 && error.getMessage().equals("Insufficient Permission")) {
                      System.out.println("Please check if you have the correct permissions to access the sheet.");
                  } else if (error.getCode() == 404) {
                      System.out.println("Please check if the sheet ID is correct.");
                  } else if (error.getCode() == 400) {
                      System.out.println("Please check if the request is correctly formatted.");
                  } else if (error.getCode() == 429) {
                      System.out.println("Quota exceeded for read requests per minute. Sleeping for 60 seconds.");
                      Thread.sleep(60000);
                      i--;
                      continue;
                  } else {
                      System.out.println("An error occurred: " + error.getMessage());
                  }
              }
                if (((String) row.get(11)).equalsIgnoreCase("yes")) {
                  try {
                    Connect.addParentCarpoolMatches(Connect.findStudentRange(name),
                            Connect.compareToParentCarpoolers(name, Connect.findStudentRange(name)));
                  } catch (GoogleJsonResponseException e) {
                      GoogleJsonError error = e.getDetails();
                      if (error.getCode() == 403 && error.getMessage().equals("Insufficient Permission")) {
                          System.out.println("Please check if you have the correct permissions to access the sheet.");
                      } else if (error.getCode() == 404) {
                          System.out.println("Please check if the sheet ID is correct.");
                      } else if (error.getCode() == 400) {
                          System.out.println("Please check if the request is correctly formatted.");
                      } else if (error.getCode() == 429) {
                          System.out.println("Quota exceeded for read requests per minute. Sleeping for 60 seconds.");
                          Thread.sleep(60000);
                          i--;
                          continue;
                      } else {
                          System.out.println("An error occurred: " + error.getMessage());
                      }
                  }
                }
            } else {
                try {
                    Connect.addDriverMatches(Connect.findStudentRange(name),
                            Connect.compareToRiders(name, Connect.findStudentRange(name)));
                } catch (GoogleJsonResponseException e) {
                    GoogleJsonError error = e.getDetails();
                    if (error.getCode() == 403 && error.getMessage().equals("Insufficient Permission")) {
                        System.out.println("Please check if you have the correct permissions to access the sheet.");
                    } else if (error.getCode() == 404) {
                        System.out.println("Please check if the sheet ID is correct.");
                    } else if (error.getCode() == 400) {
                        System.out.println("Please check if the request is correctly formatted.");
                    } else if (error.getCode() == 429) {
                        System.out.println("Quota exceeded for read requests per minute. Sleeping for 60 seconds.");
                        Thread.sleep(60000);
                        i--;
                        continue;
                    } else {
                        System.out.println("An error occurred: " + error.getMessage());
                    }
                }
            }
            bar.step();
        }
        bar.close();
    }
  }




  public static void matchOneStudent(String count)throws IOException, GeneralSecurityException{
    // Build a new authorized API client service.
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1FMam_f9V1kPlU99GPtmPpVj46KafconuUemA_1XmZ4A";
    String range = "Carpool!B" + count + ":M";
    Sheets service =
        new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build();
    ValueRange response = service.spreadsheets().values()
        .get(spreadsheetId, range)
        .execute();
    String name = Connect.getName(count);
    List<List<Object>> values = response.getValues();
    if (values == null || values.isEmpty()) {
      System.out.println("No data found.");
    } else {
      try {
          if (Connect.riderOrDriver(count).equalsIgnoreCase("Rider")){
            Connect.addRiderMatches(Connect.findStudentRange(name), Connect.compareToDrivers(name, Connect.findStudentRange(name)));
          }else{
            Connect.addDriverMatches(Connect.findStudentRange(name), Connect.compareToRiders(name, Connect.findStudentRange(name)));
          }
      } 
      catch (GoogleJsonResponseException e) {
        GoogleJsonError error = e.getDetails();
        if (error.getCode() == 403 && error.getMessage().equals("Insufficient Permission")) {
            System.out.println("Please check if you have the correct permissions to access the sheet.");
        } else if (error.getCode() == 404) {
            System.out.println("Please check if the sheet ID is correct.");
        } else if (error.getCode() == 400) {
            System.out.println("Please check if the request is correctly formatted.");
        } else {
            System.out.println("An error occurred: " + error.getMessage());
        }
      }
    }
  }

  public static UpdateValuesResponse addDriverMatches(String count, Rider[] closestRiders)throws IOException, GeneralSecurityException {
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1FMam_f9V1kPlU99GPtmPpVj46KafconuUemA_1XmZ4A";
    String range = "Carpool!O" + count + ":AR";
    List<List<Object>> values = new ArrayList<>();
    List<Object> row = new ArrayList<>();
    for (int i = 0; i < closestRiders.length; i++) {
      Rider rider = closestRiders[i];
      if(rider != null){
        row.add(rider.getName());
        row.add(rider.getAddress());
        row.add(rider.getPhoneNumber());
        row.add(rider.getEmail());
        row.add(rider.getDistance());
        row.add(rider.getParentInfo());
      }
    }
    values.add(row);
    String valueInputOption = "RAW";

    // Create the sheets API client
    Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY,
        getCredentials(HTTP_TRANSPORT))
        .setApplicationName(APPLICATION_NAME)
        .build();

    UpdateValuesResponse result = null;
    try {
      // Updates the values in the specified range.
      ValueRange body = new ValueRange()
          .setValues(values);
      result = service.spreadsheets().values().update(spreadsheetId, range, body)
          .setValueInputOption(valueInputOption)
          .execute();
      System.out.printf("%d cells updated.", result.getUpdatedCells());
    } 
    catch (GoogleJsonResponseException e) {
      GoogleJsonError error = e.getDetails();
      if (error.getCode() == 403 && error.getMessage().equals("Insufficient Permission")) {
          System.out.println("Please check if you have the correct permissions to access the sheet.");
      } else if (error.getCode() == 404) {
          System.out.println("Please check if the sheet ID is correct.");
      } else if (error.getCode() == 400) {
          System.out.println("Please check if the request is correctly formatted.");
      } else {
          System.out.println("An error occurred: " + error.getMessage());
      }
    }
    return result;
  }

  public static UpdateValuesResponse addRiderMatches(String count, Driver[] closestDrivers)throws IOException, GeneralSecurityException {
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1FMam_f9V1kPlU99GPtmPpVj46KafconuUemA_1XmZ4A";
    String range = "Carpool!O" + count + ":AR";
    List<List<Object>> values = new ArrayList<>();
    List<Object> row = new ArrayList<>();
    for (int i = 0; i < closestDrivers.length; i++) {
      Driver driver = closestDrivers[i];
      if(driver != null){
        row.add(driver.getName());
        row.add(driver.getAddress());
        row.add(driver.getPhoneNumber());
        row.add(driver.getEmail());
        row.add(driver.getDistance());
        row.add(" ");
      }
    }
    values.add(row);
    String valueInputOption = "RAW";

    // Create the sheets API client
    Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY,
        //GsonFactory.getDefaultInstance(),  perhaps JSON_FACTORY
        getCredentials(HTTP_TRANSPORT))
        .setApplicationName(APPLICATION_NAME)
        .build();

    UpdateValuesResponse result = null;
    try {
      // Updates the values in the specified range.
      ValueRange body = new ValueRange()
          .setValues(values);
      result = service.spreadsheets().values().update(spreadsheetId, range, body)
          .setValueInputOption(valueInputOption)
          .execute();
      System.out.printf("%d cells updated.", result.getUpdatedCells());
    } 
    catch (GoogleJsonResponseException e) {
      GoogleJsonError error = e.getDetails();
      if (error.getCode() == 403 && error.getMessage().equals("Insufficient Permission")) {
          System.out.println("Please check if you have the correct permissions to access the sheet.");
      } else if (error.getCode() == 404) {
          System.out.println("Please check if the sheet ID is correct.");
      } else if (error.getCode() == 400) {
          System.out.println("Please check if the request is correctly formatted.");
      } else {
          System.out.println("An error occurred: " + error.getMessage());
      }
    }
    return result;
  }

  public static UpdateValuesResponse addParentCarpoolMatches(String count, Rider[] closestParentCarpoolers)throws IOException, GeneralSecurityException {
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1FMam_f9V1kPlU99GPtmPpVj46KafconuUemA_1XmZ4A";
    String range = "Carpool!AS" + count + ":AW";
    List<List<Object>> values = new ArrayList<>();
    List<Object> row = new ArrayList<>();
    for (int i = 0; i < closestParentCarpoolers.length; i++) {
      Rider rider = closestParentCarpoolers[i];
      if(rider != null){
        row.add(rider.getParentCarpoolInfo());
      }
    }
    values.add(row);
    String valueInputOption = "RAW";

    // Create the sheets API client
    Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY,
        //GsonFactory.getDefaultInstance(),  perhaps JSON_FACTORY
        getCredentials(HTTP_TRANSPORT))
        .setApplicationName(APPLICATION_NAME)
        .build();

    UpdateValuesResponse result = null;
    try {
      // Updates the values in the specified range.
      ValueRange body = new ValueRange()
          .setValues(values);
      result = service.spreadsheets().values().update(spreadsheetId, range, body)
          .setValueInputOption(valueInputOption)
          .execute();
      System.out.printf(" %d cells updated.", result.getUpdatedCells());
    } 
    catch (GoogleJsonResponseException e) {
      GoogleJsonError error = e.getDetails();
      if (error.getCode() == 403 && error.getMessage().equals("Insufficient Permission")) {
          System.out.println("Please check if you have the correct permissions to access the sheet.");
      } else if (error.getCode() == 404) {
          System.out.println("Please check if the sheet ID is correct.");
      } else if (error.getCode() == 400) {
          System.out.println("Please check if the request is correctly formatted.");
      } else {
          System.out.println("An error occurred: " + error.getMessage());
      }
    }
    return result;
  }
}
