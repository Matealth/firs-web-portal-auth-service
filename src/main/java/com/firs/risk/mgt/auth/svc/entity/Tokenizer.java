package com.firs.risk.mgt.auth.svc.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Table(	name = Tokenizer.TABLE_NAME,
        indexes = {
                @Index(name = "tokenizer_index_phone_token", columnList="phone, token"),
                @Index(name = "tokenizer_index_email_token", columnList="email, token"),
        })
public class Tokenizer {
    public static final String TABLE_NAME= "tokenizer";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ID;

    @Column(name = "phone"/*, unique = true, nullable = false*/)
    private String phone;

    @Column(name = "email"/*, unique = true, nullable = false*/)
    private String email;

    @Column(name = "token")
    private String token;

    @JsonIgnore
    @Column(name = "expire_at")
    private Date expireAt;

    @JsonIgnore
    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @JsonIgnore
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
}