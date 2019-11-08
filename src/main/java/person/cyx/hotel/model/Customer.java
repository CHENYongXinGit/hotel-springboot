package person.cyx.hotel.model;

import java.io.Serializable;

public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long cId;

    private String cName;

    private Integer cSex;

    private String cPhone;

    private String cIdentity;

    private Integer cMember;

    private String cPassword;

    private Long cCreated;

    private Long cUpdated;

    public Long getcId() {
        return cId;
    }

    public void setcId(Long cId) {
        this.cId = cId;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName == null ? null : cName.trim();
    }

    public Integer getcSex() {
        return cSex;
    }

    public void setcSex(Integer cSex) {
        this.cSex = cSex;
    }

    public String getcPhone() {
        return cPhone;
    }

    public void setcPhone(String cPhone) {
        this.cPhone = cPhone == null ? null : cPhone.trim();
    }

    public String getcIdentity() {
        return cIdentity;
    }

    public void setcIdentity(String cIdentity) {
        this.cIdentity = cIdentity == null ? null : cIdentity.trim();
    }

    public Integer getcMember() {
        return cMember;
    }

    public void setcMember(Integer cMember) {
        this.cMember = cMember;
    }

    public String getcPassword() {
        return cPassword;
    }

    public void setcPassword(String cPassword) {
        this.cPassword = cPassword == null ? null : cPassword.trim();
    }

    public Long getcCreated() {
        return cCreated;
    }

    public void setcCreated(Long cCreated) {
        this.cCreated = cCreated;
    }

    public Long getcUpdated() {
        return cUpdated;
    }

    public void setcUpdated(Long cUpdated) {
        this.cUpdated = cUpdated;
    }
}