package util;

public class CustoemrTM {
    String Id;
    String name;
    String address;
    String contactNo;

    public CustoemrTM() {
    }

    public CustoemrTM(String id, String name, String address, String contactNo) {
        Id = id;
        this.name = name;
        this.address = address;
        this.contactNo = contactNo;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    @Override
    public String toString() {
        return "CustoemrTM{" +
                "Id='" + Id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", contactNo='" + contactNo + '\'' +
                '}';
    }
}
