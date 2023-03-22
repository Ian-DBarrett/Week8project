package uk.ac.abertay.cmp309.week8;

public class FirestoreContact {
    public static final String COLLECTION_PATH = "contacts";
    public static final String KEY_FIRST = "first";
    public static final String KEY_LAST = "last";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE = "phone";

    private String first;
    private String last;
    private String email;
    private String phone;
    private String id;

    public FirestoreContact(){}

    public String getFirst(){return first;}
    public String getLast(){return last;}
    public String getEmail(){return email;}
    public String getPhone(){return phone;}
    public String getID(){return id;}
    public void setID(String id){this.id = id;}

    @Override
    public String toString(){
        return String.format("id: %s; first %s; last %s; email: %s; phone: %s", id, first, last, email, phone);
    }
}
