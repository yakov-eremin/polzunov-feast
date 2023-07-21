package com.example.polzunovfeastserver.route.entity;

import com.example.polzunovfeastserver.route.node.entity.RouteNodeEntity;
import com.example.polzunovfeastserver.route.node.util.RouteNodeTableKeys;
import com.example.polzunovfeastserver.route.util.RouteTableKeys;
import com.example.polzunovfeastserver.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(
        name = "routes",
        uniqueConstraints = {
                @UniqueConstraint(name = RouteTableKeys.UNIQUE_OWNER, columnNames = "owner_id"),
        }
)
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RouteEntity {

    @Id
    @Column(name = "route_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "owner_id",
            foreignKey = @ForeignKey(name = RouteTableKeys.FOREIGN_OWNER, value = ConstraintMode.CONSTRAINT)
    )
    private UserEntity owner;

    /**
     * Do not use getNodes.clear()/remove()/removeAll()/add()/addAll(). Use {@link #updateNodes} instead
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(
            name = "route_id",
            foreignKey = @ForeignKey(name = RouteNodeTableKeys.FOREIGN_ROUTE, value = ConstraintMode.CONSTRAINT)
    )
    @Setter(AccessLevel.NONE)
    private List<RouteNodeEntity> nodes;

    public void updateNodes(List<RouteNodeEntity> nodes) {
        this.nodes.clear();
        this.nodes.addAll(nodes);
    }
}
