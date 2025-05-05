package web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "descriptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Description {

    @Id
    @GeneratedValue(generator = "UUID")
    private Long id;

    @OneToOne
    @JoinColumn(nullable = false)
    private Photo photo;

    @Column
    private String title;

    @Column
    private String weight;

    @Column
    private String calories;

    @Column
    private String proteins;

    @Column
    private String carbs;

    @Column
    private String fats;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String message;

}
