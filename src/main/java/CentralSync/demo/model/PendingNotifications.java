//package CentralSync.demo.model;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.ManyToOne;
//import javax.persistence.FetchType;
//
//@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@Data
//public class PendingNotifications{
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String message;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    private User user;
//
//    private boolean read;
//
//    // Getters and setters
//}
