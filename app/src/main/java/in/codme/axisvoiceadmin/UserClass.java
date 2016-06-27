package in.codme.axisvoiceadmin;

/**
 * Created by Alen John Abraham on 26-06-2016.
 */
public class UserClass {

    String address,display_name,email,img_url,phone_number,scroll_message,uid,rateurl,myofferurl;

    public String getUid() {
        return uid;
    }

    public UserClass(String address, String rateurl, String myofferurl, String uid, String scroll_message, String phone_number, String img_url, String email, String display_name) {
        this.address = address;
        this.rateurl = rateurl;
        this.myofferurl = myofferurl;
        this.uid = uid;
        this.scroll_message = scroll_message;
        this.phone_number = phone_number;
        this.img_url = img_url;
        this.email = email;
        this.display_name = display_name;
    }

    public String getRateurl() {
        return rateurl;
    }

    public String getMyofferurl() {
        return myofferurl;
    }

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

    public String getImg_url() {
        return img_url;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getScroll_message() {
        return scroll_message;
    }
}
