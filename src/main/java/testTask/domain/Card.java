package testTask.domain;

import jdk.jfr.Timestamp;
import lombok.*;
import testTask.domain.enums.Status;
import jakarta.persistence.*;
import testTask.util.NumberEncryptor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "card")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @JoinColumn(name = "owner_id")
    @ManyToOne
    private User owner;

    @Convert(converter = NumberEncryptor.class)
    @Column(name = "number", nullable = false, unique = true)
    private String number;

    @Column(name = "last4_numbers", nullable = false, unique = true, length = 4)
    private String last4Numbers;

    @Timestamp
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "balance", nullable = false, columnDefinition = "NUMERIC(15,2) DEFAULT 0.00")
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Version
    @Column(name = "version")
    private long version;
}
