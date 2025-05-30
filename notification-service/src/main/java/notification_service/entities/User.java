package notification_service.entities;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "created_notifications")
    private boolean createdNotifications;

    @Column(name = "updated_notifications")
    private boolean updatedNotifications;

    @Column(name = "deleted_notifications")
    private boolean deletedNotifications;
}
