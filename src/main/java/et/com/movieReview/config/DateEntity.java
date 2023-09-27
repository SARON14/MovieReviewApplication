/*
 * Developer: Emmanuel Israel
 * Licensed: MIT
 */
package et.com.movieReview.config;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class DateEntity{

    @Column(name = "created_date")
    private ZonedDateTime createdDate;
    @Column(name = "update_date")
    private ZonedDateTime updatedDate;
}
