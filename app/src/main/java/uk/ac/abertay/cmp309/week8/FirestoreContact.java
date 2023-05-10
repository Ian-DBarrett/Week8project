package uk.ac.abertay.cmp309.week8;

public class FirestoreContact {
    public static final String COLLECTION_PATH = "contacts";
    public static final String KEY_FIRST = "first";
    public static final String KEY_LAST = "last";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_Postcode = "postcode";

    private String first;
    private String last;
    private String email;
    private String phone;
    private String postcode;

    private String id;

    public FirestoreContact(){}

    public String getFirst(){return first;}
    public String getLast(){return last;}
    public String getEmail(){return email;}
    public String getPhone(){return phone;}
    public String getPostcode(){return postcode;}
    public void setID(String id){this.id = id;}

    @Override
    public String toString(){
        return String.format("id: %s; first %s; last %s; email: %s; phone: %s", postcode, first, last, email, phone);
    }
}
