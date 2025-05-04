package io.rp.job.offer.viewer.crawler;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import java.time.Instant;

@Entity
@NoArgsConstructor
@Table(name = "url", schema = "crawler")
@Getter
@Setter
@ToString
class URLEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(min = 4, max = 150)
    @URL
    private String url;

    @Column(name = "creation_date", columnDefinition = "timestamptz not null DEFAULT now()")
    private Instant creation_date;

    URLEntity(String url) {
        this.url = url;
    }
}
