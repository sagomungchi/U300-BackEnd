package it.toping.demo.model;

import javax.persistence.*;

@Entity
@Table(name = "tbl_u300_account", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "user_id"
        })
})
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


}
