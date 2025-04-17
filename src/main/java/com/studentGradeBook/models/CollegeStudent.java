package com.studentGradeBook.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "student")
public class CollegeStudent implements Student {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    @Column
    private String firstname;
    @Column
    private String lastname;
    @Column(name="email_address")
    private String emailAddress;
    //-------------------------------
    //@OneToMany(cascade = CascadeType.ALL)
    //@JoinColumn(name = "student_id")
    //private List<MathGrade> mathGrades;
 
    //@OneToMany(cascade = CascadeType.ALL)
    //@JoinColumn(name = "student_id")
    //private List<ScienceGrade> scienceGrades;
 
    //@OneToMany(cascade = CascadeType.ALL)
    //@JoinColumn(name = "student_id")
    //private List<HistoryGrade> historyGrade;
    //-------------------------------
    public CollegeStudent() {

    }

    public CollegeStudent(String firstname, String lastname, String emailAddress) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.emailAddress = emailAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }


    public String getFullName() {
        return getFirstname() + " " + getLastname();
    }

    @Override
    public String toString() {
        return "CollegeStudent{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                '}';
    }

    public String studentInformation() {
       return getFullName() + " " + getEmailAddress();
    }
}
