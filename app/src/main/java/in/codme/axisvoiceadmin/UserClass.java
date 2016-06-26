package in.codme.axisvoiceadmin;

/**
 * Created by Alen on 26-Jun-16.
 */
public class UserClass {

    public UserClass(String address, String img_url, String scroll_message, String phone_number, String email, String display_name) {
        this.address = address;
        this.img_url = img_url;
        this.scroll_message = scroll_message;
        this.phone_number = phone_number;
        this.email = email;
        this.display_name = display_name;
    }

    String address;
    String display_name;
    String email;
    String phone_number;
    String scroll_message;

    public String getImg_url() {
        return img_url;
    }

    String img_url;


    public UserClass() {
    }


    public String getAddress() {
        return address;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getScroll_message() {
        return scroll_message;
    }
}
