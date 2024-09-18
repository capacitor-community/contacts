package getcapacitor.community.contacts;

import androidx.annotation.NonNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

public class CreateContactInput {

    // @TODO:
    // Contact Type

    // Name
    public String nameGiven;
    public String nameMiddle;
    public String nameFamily;
    public String namePrefix;
    public String nameSuffix;
    // private final String nickname;

    // Organization
    public String organizationName;
    public String organizationJobTitle;
    public String organizationDepartment;

    // Birthday
    public String birthday;

    // Note
    public String note;

    // Phones
    public ArrayList<PhoneInput> phones = new ArrayList<>();

    // Emails
    public ArrayList<EmailInput> emails = new ArrayList<>();

    // URLs
    public ArrayList<String> urls = new ArrayList<>();

    // Postal Addresses
    public ArrayList<PostalAddressInput> postalAddresses = new ArrayList<>();

    // Image
    public ImageInput image;

    CreateContactInput(JSONObject fromJSONObject) {
        // Name
        JSONObject nameObject = fromJSONObject.optJSONObject("name");
        if (nameObject != null) {
            this.nameGiven = nameObject.has("given") ? nameObject.optString("given") : null;
            this.nameMiddle = nameObject.has("middle") ? nameObject.optString("middle") : null;
            this.nameFamily = nameObject.has("family") ? nameObject.optString("family") : null;
            this.namePrefix = nameObject.has("prefix") ? nameObject.optString("prefix") : null;
            this.nameSuffix = nameObject.has("suffix") ? nameObject.optString("suffix") : null;
        }

        // Organization
        JSONObject organizationObject = fromJSONObject.optJSONObject("organization");
        if (organizationObject != null) {
            this.organizationName = organizationObject.has("company") ? organizationObject.optString("company") : null;
            this.organizationJobTitle = organizationObject.has("jobTitle") ? organizationObject.optString("jobTitle") : null;
            this.organizationDepartment = organizationObject.has("department") ? organizationObject.optString("department") : null;
        }

        // Birthday
        JSONObject birthdayObject = fromJSONObject.optJSONObject("birthday");
        if (birthdayObject != null) {
            if (birthdayObject.has("month") && birthdayObject.has("day")) {
                String month = birthdayObject.optString("month");
                String day = birthdayObject.optString("day");

                @NonNull
                String birthdayFormat = "MM-dd";
                @NonNull
                String birthdayString = month + "-" + day;

                if (birthdayObject.has("year")) {
                    String year = birthdayObject.optString("year");

                    birthdayFormat = "yyyy-MM-dd";
                    birthdayString = year + "-" + birthdayString;
                }

                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(birthdayFormat, Locale.getDefault());
                    // `setLenient` makes sure input like "2020-06-50" does not get parsed to a valid date.
                    dateFormat.setLenient(false);
                    Date date = dateFormat.parse(birthdayString);
                    if (date != null) {
                        if (birthdayFormat.equals("MM-dd")) {
                            // We have to prefix with "--",
                            // because otherwise it may get saved with the current year attached instead of no year at all.
                            this.birthday = "--" + birthdayString;
                        } else {
                            this.birthday = birthdayString;
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    // Something wrong with this date format
                }
            }
        }

        // Note
        this.note = fromJSONObject.has("note") ? fromJSONObject.optString("note") : null;

        // Phones
        JSONArray phonesArray = fromJSONObject.optJSONArray("phones");
        if (phonesArray != null && phonesArray.length() > 0) {
            for (int n = 0; n < phonesArray.length(); n++) {
                JSONObject phoneObject = phonesArray.optJSONObject(n);
                if (phoneObject != null) {
                    this.phones.add(new PhoneInput(phoneObject));
                }
            }
        }

        // Emails
        JSONArray emailsArray = fromJSONObject.optJSONArray("emails");
        if (emailsArray != null && emailsArray.length() > 0) {
            for (int n = 0; n < emailsArray.length(); n++) {
                JSONObject emailObject = emailsArray.optJSONObject(n);
                if (emailObject != null) {
                    this.emails.add(new EmailInput(emailObject));
                }
            }
        }

        // URLs
        JSONArray urlsArray = fromJSONObject.optJSONArray("urls");
        if (urlsArray != null && urlsArray.length() > 0) {
            for (int n = 0; n < urlsArray.length(); n++) {
                String url = urlsArray.optString(n);
                if (url != null) {
                    this.urls.add(url);
                }
            }
        }

        // Postal Addresses
        JSONArray postalAddressesArray = fromJSONObject.optJSONArray("postalAddresses");
        if (postalAddressesArray != null && postalAddressesArray.length() > 0) {
            for (int n = 0; n < postalAddressesArray.length(); n++) {
                JSONObject postalAddressObject = postalAddressesArray.optJSONObject(n);
                if (postalAddressObject != null) {
                    this.postalAddresses.add(new PostalAddressInput(postalAddressObject));
                }
            }
        }

        // Image
        JSONObject imageObject = fromJSONObject.optJSONObject("image");
        if (imageObject != null) {
            this.image = new ImageInput(imageObject);
        }
    }

    public static class PhoneInput {

        public final int type;
        public final String label;
        public final Boolean isPrimary;

        public final String number;

        PhoneInput(JSONObject fromJSONObject) {
            this.type = Contacts.phoneTypeMap.getValue(fromJSONObject.optString("type"));
            this.label = fromJSONObject.has("label") ? fromJSONObject.optString("label") : null;
            this.isPrimary = fromJSONObject.optBoolean("isPrimary", false);

            this.number = fromJSONObject.has("number") ? fromJSONObject.optString("number") : null;
        }
    }

    public static class EmailInput {

        public final int type;
        public final String label;
        public final Boolean isPrimary;

        public final String address;

        EmailInput(JSONObject fromJSONObject) {
            this.type = Contacts.emailTypeMap.getValue(fromJSONObject.optString("type"));
            this.label = fromJSONObject.has("label") ? fromJSONObject.optString("label") : null;
            this.isPrimary = fromJSONObject.optBoolean("isPrimary", false);

            this.address = fromJSONObject.has("address") ? fromJSONObject.optString("address") : null;
        }
    }

    public static class PostalAddressInput {

        public final int type;
        public final String label;
        public final Boolean isPrimary;

        public final String street;
        public final String neighborhood;
        public final String city;
        public final String region;
        public final String postcode;
        public final String country;

        PostalAddressInput(JSONObject fromJSONObject) {
            this.type = Contacts.postalAddressTypeMap.getValue(fromJSONObject.optString("type"));
            this.label = fromJSONObject.has("label") ? fromJSONObject.optString("label") : null;
            this.isPrimary = fromJSONObject.optBoolean("isPrimary", false);

            this.street = fromJSONObject.has("street") ? fromJSONObject.optString("street") : null;
            this.neighborhood = fromJSONObject.has("neighborhood") ? fromJSONObject.optString("neighborhood") : null;
            this.city = fromJSONObject.has("city") ? fromJSONObject.optString("city") : null;
            this.region = fromJSONObject.has("region") ? fromJSONObject.optString("region") : null;
            this.postcode = fromJSONObject.has("postcode") ? fromJSONObject.optString("postcode") : null;
            this.country = fromJSONObject.has("country") ? fromJSONObject.optString("country") : null;
        }
    }

    public static class ImageInput {

        public final String base64String;

        ImageInput(JSONObject fromJSONObject) {
            this.base64String = fromJSONObject.has("base64String") ? fromJSONObject.optString("base64String") : null;
        }
    }
}
