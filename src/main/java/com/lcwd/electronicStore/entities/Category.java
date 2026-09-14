package com.lcwd.electronicStore.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "categories")
public class Category {
    @Id
    @Column(name = "id")
    private String categoryId;
    @Column(name = "category_title", length = 60, nullable = false)
    private String title;
    @Column(name = "category_desc", length = 500)
    private String description;

    private String coverImage;

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

}
