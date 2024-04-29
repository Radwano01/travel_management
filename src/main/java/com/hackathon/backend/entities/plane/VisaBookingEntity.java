//package com.hackathon.backend.Entities.plane;
//
//import com.hackathon.backend.Entities.plane.PlaneSeatsEntity;
//import com.hackathon.backend.Entities.user.UserEntity;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Entity
//@Table(name = "visaBooking")
//@Getter
//@Setter
//@NoArgsConstructor
//public class VisaBookingEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "user_id", insertable = false, updatable = false)
//    private UserEntity users;
//
//    @Column(name = "user_id")
//    private Long userId;
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "visa_id", insertable = false, updatable = false)
//    private PlaneSeatsEntity visas;
//
//    @Column(name = "visa_id")
//    private Long visaId;
//
//}
