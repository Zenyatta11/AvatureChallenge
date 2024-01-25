package com.zenyatta.challenge.avature.model;

import com.zenyatta.challenge.avature.service.EntityInterceptor;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder(toBuilder = true)
@Entity
@Table(name = "job_postings")
@EntityListeners(EntityInterceptor.class)
@NoArgsConstructor
@AllArgsConstructor
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "job_id", unique = true, nullable = false)
    private Long jobId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "salary", nullable = false)
    private Integer salary;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "job_skill_relation", joinColumns = @JoinColumn(name = "job_id"), inverseJoinColumns = @JoinColumn(name = "id"))
    private Set<Skill> skills;

    public Long getId() {
        return this.jobId;
    }
}
