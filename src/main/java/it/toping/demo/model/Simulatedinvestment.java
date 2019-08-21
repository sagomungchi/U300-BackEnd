package it.toping.demo.model;

import javax.persistence.*;

@Entity
@Table(name = "tbl_u300_investment", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "post_id",
                "user_id"
        })
})
public class Simulatedinvestment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}
