//package com.hackathon.backend.Entities.hotel;
//
//import com.hackathon.backend.Entities.user.UserEntity;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "roomBooking")
//@Getter
//@Setter
//@NoArgsConstructor
//public class RoomBookingEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "user_id", insertable = false, updatable = false)
//    private UserEntity users;
//
//    @Column(name = "user_id")
//    private Long userId;
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "room_id", insertable = false, updatable = false)
//    private RoomEntity rooms;
//
//    @Column(name = "room_id")
//    private Long roomId;
//
//    private LocalDateTime startTime;
//    private LocalDateTime endTime;
//
//    public RoomBookingEntity(Long userId, Long roomId,
//                             LocalDateTime startTime,
//                             LocalDateTime endTime) {
//        this.userId = userId;
//        this.roomId = roomId;
//        this.startTime = startTime;
//        this.endTime = endTime;
//    }
//}
